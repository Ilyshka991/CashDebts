package com.pechuro.cashdebts.ui.fragment.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import com.pechuro.cashdebts.R
import kotlinx.android.synthetic.main.item_spinner_settings.view.*

class SettingSpinnerAdapter(private val items: Array<String>) : BaseAdapter(), SpinnerAdapter {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
        convertView?.apply {
            text.text = items[position]
        } ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.item_spinner_settings, parent, false).apply {
                text.text = items[position]
            }

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int) = items[position].hashCode().toLong()

    override fun getCount() = items.size
}