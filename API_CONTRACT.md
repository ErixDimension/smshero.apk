# SMSHERO API Contract

Base URLs:
- `SA_BASE = https://hero-sms.com/stubs/handler_api.php`
- `V1_BASE = https://hero-sms.com/api/v1`

All requests use Cloudflare DoH (`https://1.1.1.1/dns-query`, IPv6 disabled) and Chrome Android User-Agent. API keys never enter logs or CI artifacts.

## Read-only
- `GET {SA_BASE}?action=getBalance&api_key={KEY}` — parses `ACCESS_BALANCE:<amount>`.
- `GET {SA_BASE}?action=getServicesList&api_key={KEY}` — legacy service catalog.
- `GET {SA_BASE}?action=getCountries&api_key={KEY}` — country catalog.
- `GET {SA_BASE}?action=getOperators&country={id}&api_key={KEY}` — operator catalog.

## V1 activation
- `GET {V1_BASE}/activations/offers/sms?services={svc}&countries={ctr}` with `Authorization: ApiKey {KEY}`.
- `POST {V1_BASE}/activations` with JSON `service,country,amount,maxPrice,fixedPrice,operator`.
- `GET {V1_BASE}/activations?services={svc}&countries={ctr}`.
- `POST {V1_BASE}/activations/{id}/finish`.
- `DELETE {V1_BASE}/activations/{id}`.

Legacy fallback remains available for buy, poll, finish, and cancel only when V1 fails. Paid purchase is never executed by smoke tests.
