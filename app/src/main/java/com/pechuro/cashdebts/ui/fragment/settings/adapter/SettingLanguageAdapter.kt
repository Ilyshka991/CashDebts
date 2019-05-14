package com.pechuro.cashdebts.ui.fragment.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import com.pechuro.cashdebts.R
import kotlinx.android.synthetic.main.item_settings_language.view.*

class SettingLanguageAdapter(private val languages: Array<String>) : BaseAdapter(), SpinnerAdapter {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView != null) {
            return convertView.apply {
                text_language.text = languages[position]
            }
        }
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_settings_language, parent, false).apply {
                text_language.text = languages[position]
            }
    }

    override fun getItem(position: Int) = languages[position]

    override fun getItemId(position: Int) = languages[position].hashCode().toLong()

    override fun getCount() = languages.size
}