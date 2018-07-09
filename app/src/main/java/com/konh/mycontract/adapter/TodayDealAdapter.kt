package com.konh.mycontract.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.konh.mycontract.databinding.ItemDealBinding
import com.konh.mycontract.model.TodayDealModel

class TodayDealAdapter(private val context: Context, private var items:List<TodayDealModel>, private val doneHandler:(TodayDealModel)->Unit) : BaseAdapter() {

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
        val item = getItem(position) as TodayDealModel
        binding.item = item
        binding.dealDone.setOnClickListener {
            doneHandler.invoke(item)
        }
        return binding.root
    }

    fun updateItems(items:List<TodayDealModel>) {
        this.items = items
        notifyDataSetChanged()
    }
}