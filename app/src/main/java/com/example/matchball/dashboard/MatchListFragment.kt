package com.example.matchball.dashboard

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matchball.joinmatch.JoinActivity
import com.example.matchball.databinding.FragmentListBinding
import com.example.matchball.model.MatchRequest

class MatchListFragment : Fragment() {

    private lateinit var listFragmentBinding: FragmentListBinding
    private lateinit var matchRequestAdapter: RecyclerAdapter
    private val matchListViewModel: MatchListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        initObserve()
        initEvent()
        matchListViewModel.handleMatchList()

    }

    private fun initObserve() {
        matchListViewModel.matchListResult.observe(this, { result ->
            listFragmentBinding.swipe.isRefreshing = false
            when (result) {
                is MatchListViewModel.MatchListResult.Loading -> {
                    listFragmentBinding.swipe.isRefreshing = true
                }
                is MatchListViewModel.MatchListResult.ResultOk -> {
                    matchRequestAdapter.addNewData(result.matchList)
                }
                is MatchListViewModel.MatchListResult.ResultError -> {
                    Toast.makeText(context, result.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initList() {
        listFragmentBinding.rcvMatchRequest.apply {
            layoutManager = LinearLayoutManager(context)
            matchRequestAdapter = RecyclerAdapter(arrayListOf())
            adapter = matchRequestAdapter
            matchRequestAdapter.setOnItemClickListerner(object :
                RecyclerAdapter.OnItemClickListerner {
                override fun onItemClick(requestData: MatchRequest) {
                    JoinActivity.startDetails(requireContext(), requestData)
                }
            })
        }
    }

    private fun initEvent() {
        listFragmentBinding.swipe.setOnRefreshListener {
            matchListViewModel.handleMatchList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listFragmentBinding = FragmentListBinding.inflate(inflater, container, false)
        return listFragmentBinding.root
    }

    companion object {
        fun newInstance() = MatchListFragment()
    }
}