package com.example.matchball.yourmatchrequest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.matchball.databinding.ActivityYourRequestBinding
import com.example.matchball.databinding.YourRequestItemsBinding
import com.example.matchball.model.MatchRequest

class YourRequestAdapter(private var yourRequestList : ArrayList<MatchRequest>) :
    RecyclerView.Adapter<YourRequestAdapter.MyViewHolder>(){

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
        private val yourRequestItemsBinding: YourRequestItemsBinding
        ) : RecyclerView.ViewHolder(yourRequestItemsBinding.root) {
            fun bind(data : MatchRequest) {
                with(yourRequestItemsBinding) {
                    tvTime.text = data.time
                    tvAmount.text = data.people
                    tvPitch.text = data.pitch
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val yourRequestItems =
            YourRequestItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder : MyViewHolder = MyViewHolder(yourRequestItems)
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(yourRequestList[position])
    }

    override fun getItemCount(): Int {
        return yourRequestList.size
    }


}