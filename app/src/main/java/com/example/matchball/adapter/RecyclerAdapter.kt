package com.example.matchball.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.matchball.databinding.MatchRequestItemsBinding
import com.example.matchball.model.MatchRequest

class RecyclerAdapter(private var requestList : ArrayList<MatchRequest>) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

//    fun getListRequest(requestData : ArrayList<MatchRequest>) {
//        this.requestList = requestData
//    }

    private lateinit var mListerner : OnItemClickListerner

    interface OnItemClickListerner {
        fun onItemClick(requestData: MatchRequest)
    }

    fun setOnItemClickListerner(listerner : OnItemClickListerner) {
        mListerner = listerner
    }

    class MyViewHolder(private val requestItemsBinding: MatchRequestItemsBinding, private val listerner: OnItemClickListerner)
        : RecyclerView.ViewHolder(requestItemsBinding.root){
        fun bind(requestData : MatchRequest){
            with(requestItemsBinding){
//                tvTeamName.text = ...
                tvTime.text = requestData.time
                tvPitch.text = requestData.pitch
                tvAmount.text = requestData.people

                requestItemsBinding.rlRequestItems.setOnClickListener {
                    listerner.onItemClick(requestData)
                }

            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val matchRequestItems = MatchRequestItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder : MyViewHolder = MyViewHolder(matchRequestItems, mListerner)
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(requestList[position])
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

}