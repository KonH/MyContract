package com.konh.mycontract.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.konh.mycontract.databinding.ItemHistoryBinding
import com.konh.mycontract.model.HistoryAggregateModel

class HistoryAggregateAdapter(private val context: Context, private var items:List<HistoryAggregateModel>, private val clickHandler:(HistoryAggregateModel)->Unit) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any? = items[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ItemHistoryBinding
        if (convertView == null) {
            binding = ItemHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
            binding.root.tag = binding
        } else {
            binding = convertView.tag as ItemHistoryBinding
        }
        val item = getItem(position) as HistoryAggregateModel
        binding.item = item
        /*binding.setOnClickListener {
            doneHandler.invoke(item)
        }*/ // TODO
        return binding.root
    }

    fun updateItems(items:List<HistoryAggregateModel>) {
        this.items = items
        notifyDataSetChanged()
    }
}