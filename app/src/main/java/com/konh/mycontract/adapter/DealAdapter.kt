package com.konh.mycontract.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.konh.mycontract.databinding.ItemDealBinding
import com.konh.mycontract.model.DealModel

class DealAdapter(private val context: Context) : BaseAdapter() {
    var items: List<DealModel> = emptyList()

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any? = items[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ItemDealBinding
        if (convertView == null) {
            binding = ItemDealBinding.inflate(LayoutInflater.from(context), parent, false)
            binding.root.tag = binding
        } else {
            binding = convertView.tag as ItemDealBinding
        }
        binding.item = getItem(position) as DealModel

        return binding.root
    }
}