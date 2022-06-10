package com.example.matchball.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.matchball.databinding.MatchRequestItemsBinding
import com.example.matchball.model.MatchRequest

class RecyclerAdapter(private var requestList : ArrayList<MatchRequest>):
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    private lateinit var listerner: OnItemClickListerner

    var requestFilterList = ArrayList<MatchRequest>()

    init {
        requestFilterList = requestList
    }

//    class RequestHolder(var)

    interface OnItemClickListerner {
        fun onItemClick(requestData: MatchRequest)
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
        val viewHolder = MyViewHolder(matchRequestItems, listerner)
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(requestList[position])

//        val requestHolder = holder as RequestHolder
    }

    override fun getItemCount(): Int {
        return requestList.size
    }
}