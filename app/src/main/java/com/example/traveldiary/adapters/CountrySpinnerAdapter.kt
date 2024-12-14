package com.example.traveldiary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CountrySpinnerAdapter(
    private val context: Context,
    private val countries: List<com.example.traveldiary.models.Country>
) : BaseAdapter() {

    override fun getCount(): Int = countries.size

    override fun getItem(position: Int): com.example.traveldiary.models.Country = countries[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = countries[position].name // Display only the country name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = countries[position].name // Display only the country name
        return view
    }
}
