package com.example.matchball.dashboard.filterbar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.matchball.databinding.FilterItemsBinding
import com.example.matchball.model.FilterModel

class FilterAdapter(private var filterList : ArrayList<FilterModel>) :
        RecyclerView.Adapter<FilterAdapter.MyViewHolder>() {

    private lateinit var listerner : OnItemClickListerner

    interface OnItemClickListerner {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListerner(listerner: OnItemClickListerner) {
        this.listerner = listerner
    }

    fun addFilterNewData(list : ArrayList<FilterModel>){
//        filterList.addAll(list)
        filterList = list
        notifyDataSetChanged()
    }

    class MyViewHolder(
        private val filterItemsBinding: FilterItemsBinding,
        private val listerner: OnItemClickListerner
    ) : RecyclerView.ViewHolder(filterItemsBinding.root) {
        fun bind(filterData : FilterModel) {
            with(filterItemsBinding) {
                labelFilter.text = filterData.attribute

                filterItemsBinding.items.setOnClickListener {
                    listerner.onItemClick(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val filterItems = FilterItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = MyViewHolder(filterItems, listerner)
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(filterList[position])
    }

    override fun getItemCount(): Int {
        return filterList.size
    }
}