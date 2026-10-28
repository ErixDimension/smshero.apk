#!/usr/bin/env bash
set -euo pipefail

OWNER="ErixDimension"
REPO="smshero.apk"
ARTIFACT_NAME="${ARTIFACT:-smshero-apk-v2.0}"
DEST="/storage/emulated/0/smshero_work"
mkdir -p "$DEST"
APK_OUT="$DEST/smshero.apk"
TMP_DIR="$(mktemp -d)"
trap 'rm -rf "$TMP_DIR"' EXIT

API="https://api.github.com/repos/$OWNER/$REPO"
TOKEN="${GITHUB_TOKEN:-}"

download_with_gh() {
  local run
  run=$(gh run list --workflow=build-apk.yml --limit=5 --json databaseId,status,conclusion,createdAt 2>/dev/null | grep '"databaseId"' || true)
  echo "GH run list: ${run:0:120}" >&2
}

download_via_api() {
  local page runs conclusion id artifact_id
  page=$(curl -sL -H "Accept: application/vnd.github+json" \
         -H "${GITHUB_TOKEN:+-H \"Authorization: token $GITHUB_TOKEN\"}" \
         "$API/actions/runs?per_page=5&branch=master&event=push&status=Completed")
  if [ -z "$page" ] || ! echo "$page" | grep -q '"data":'; then
    echo "No completed run found" >&2
    return 1
  fi
  runs=$(echo "$page" | jq '.data | map(select(.conclusion == "success"))')
  for entry in $(echo "$runs" | jq -r '.[].id'); do
    [ -n "$entry" ] && echo "$entry"
  done | sort -nr | head -1 > /tmp/last_run_id
  local last_id
  last_id=$(cat /tmp/last_run_id)
  echo "Selected run id=$last_id" >&2
  artifact_id=$(curl -sL -H "Accept: application/vnd.github+json" \
                -H "${GITHUB_TOKEN:+-H \"Authorization: token $GITHUB_TOKEN\"}" \
                "$API/actions/runs/$last_id/artifacts?per_page=50" | jq -r '.data[] | select(.name == "'$ARTIFACT_NAME'") | .id' | head -1)
  [ -z "$artifact_id" ] && echo "Artifact $ARTIFACT_NAME not found in run $last_id" >&2 && return 1
  echo "Downloading artifact id=$artifact_id ..." >&2
  curl -fL -H "Accept: application/vnd.github.mock.v1+json" \
    -H "${GITHUB_TOKEN:+-H \"Authorization: token $GITHUB_TOKEN\"}" \
    "https://api.github.com/repos/$OWNER/$REPO/actions/artifacts/$artifact_id/zip" \
    -o "$TMP_DIR/artifact.zip" || curl -L \
    "https://objects.githubusercontent.com/github-production-repository-asset-2e6d2fff/" \
    -H "${GITHUB_TOKEN:+-H \"Authorization: token $GITHUB_TOKEN\"}" \
    --header "Accept: application/octet-stream" \
    -O "$TMP_DIR/artifact.zip" 2>/dev/null || true
  unzip -o "$TMP_DIR/artifact.zip" -d "$TMP_DIR/extracted" >/dev/null 2>&1
  local apk
  apk=$(find "$TMP_DIR/extracted" -name '*.apk' | head -1)
  [ -z "$apk" ] && echo "No APK inside artifact zip" >&2 && return 1
  cp "$apk" "$APK_OUT"
  echo "$APK_OUT"
}

if command -v gh >/dev/null 2>&1; then
  download_with_gh || true
fi
output=$(download_via_api || download_via_api || true)
[ -n "$output" ] && echo "Done: $output"
sha_out=$(sha256sum "$APK_OUT")
echo "$sha_out"
