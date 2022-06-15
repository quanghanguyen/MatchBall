package com.example.matchball.dashboard.matchlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.matchball.databinding.MatchRequestItemsBinding
import com.example.matchball.model.MatchRequest
import java.util.*
import kotlin.collections.ArrayList

class RecyclerAdapter(private var requestList : ArrayList<MatchRequest>):
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>(), Filterable{

    private lateinit var listerner: OnItemClickListerner

    var requestFilterList = ArrayList<MatchRequest>()

    init {
        requestFilterList = requestList
    }

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

        val requestHolder = holder as MyViewHolder
        requestHolder.bind(requestList[position])
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                requestFilterList = if (charSearch.isEmpty()) {
                    requestList
                } else {
                    val resultList = ArrayList<MatchRequest>()
                    for (row in requestList) {
                        if (row.teamName!!.contains(charSearch.lowercase(Locale.ROOT))
                            //if (row.lowercase().contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = requestFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                requestFilterList = results?.values as ArrayList<MatchRequest>
                notifyDataSetChanged()
            }
        }
    }
}