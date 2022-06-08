package com.example.matchball.dashboard

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.matchball.databinding.MatchRequestItemsBinding
import com.example.matchball.model.FilterModel
import com.example.matchball.model.MatchRequest

class RecyclerAdapter(private var requestList : ArrayList<MatchRequest>):
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    private lateinit var listerner: OnItemClickListerner

    interface OnItemClickListerner {
        fun onItemClick(requestData: MatchRequest)
    }

    //search list
    fun filterList(requests: ArrayList<MatchRequest>) {
        this.requestList = requests
        notifyDataSetChanged()
    }

    fun addNewData(list: ArrayList<MatchRequest>) {
        requestList = list
        notifyDataSetChanged()
    }

    fun addMoreData(list: ArrayList<MatchRequest>) {
        requestList.addAll(list)
        notifyDataSetChanged()
    }

    fun remove(matchRequest: MatchRequest) {
    }

    fun setOnItemClickListerner(listerner: OnItemClickListerner) {
        this.listerner = listerner
    }

    class MyViewHolder(
        private val requestItemsBinding: MatchRequestItemsBinding,
        private val listerner: OnItemClickListerner
    ) : RecyclerView.ViewHolder(requestItemsBinding.root) {
        fun bind(requestData: MatchRequest) {
            with(requestItemsBinding) {
                tvTeamName.text = requestData.teamName
                tvTime.text = requestData.time
                tvPitch.text = requestData.pitch
                tvAmount.text = requestData.people
                tvPhone.text = requestData.phone

                requestItemsBinding.rlRequestItems.setOnClickListener {
                    listerner.onItemClick(requestData)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val matchRequestItems =
            MatchRequestItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder: MyViewHolder = MyViewHolder(matchRequestItems, listerner)
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(requestList[position])
    }

    override fun getItemCount(): Int {
        return requestList.size
    }
}