package com.example.matchball.yourmatchrequest.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.matchball.dashboard.filterbar.FilterAdapter
import com.example.matchball.dashboard.matchlist.RecyclerAdapter
import com.example.matchball.databinding.YourRequestItemsBinding
import com.example.matchball.model.MatchRequest

class YourRequestAdapter(private var yourRequestList : ArrayList<MatchRequest>) :
    RecyclerView.Adapter<YourRequestAdapter.MyViewHolder>(){

    private lateinit var listerner: OnItemClickListerner

    interface OnItemClickListerner {
        fun onItemClick(data: MatchRequest)
    }

    fun setOnItemClickListerner(listerner: OnItemClickListerner) {
        this.listerner = listerner
    }

    fun addNewData(list: ArrayList<MatchRequest>) {
        yourRequestList = list
        notifyDataSetChanged()
    }

    fun addMoreData(list: ArrayList<MatchRequest>) {
        yourRequestList.addAll(list)
        notifyDataSetChanged()
    }

    fun remove(matchRequest: MatchRequest) {
    }

    class MyViewHolder(
        private val yourRequestItemsBinding: YourRequestItemsBinding,
        private val listerner: OnItemClickListerner
        ) : RecyclerView.ViewHolder(yourRequestItemsBinding.root) {
            fun bind(data : MatchRequest) {
                with(yourRequestItemsBinding) {
                    tvTime.text = data.time
                    tvAmount.text = data.people
                    tvPitch.text = data.pitch

                    yourRequestItemsBinding.items.setOnClickListener {
                        listerner.onItemClick(data)
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val yourRequestItems =
            YourRequestItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder : MyViewHolder = MyViewHolder(yourRequestItems, listerner)
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(yourRequestList[position])
    }

    override fun getItemCount(): Int {
        return yourRequestList.size
    }
}