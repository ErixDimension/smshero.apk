package com.android.smshero.ui

import android.content.Context
import android.widget.LinearLayout
import android.widget.Button
import android.widget.ListView
import android.widget.ArrayAdapter
import android.util.TypedValue

class MainScreen(val context: Context) {
    
    fun createView(): LinearLayout {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
        
        val btnPickService = Button(context).apply {
            text = "Pilih Layanan"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(16, 16, 16, 8) }
        }
        
        val listView = ListView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ).apply { setMargins(0, 0, 0, 0) }
            val items = arrayOf("Option 1", "Option 2", "Option 3")
            adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, items)
        }
        
        layout.addView(btnPickService)
        layout.addView(listView)
        
        return layout
    }
}
