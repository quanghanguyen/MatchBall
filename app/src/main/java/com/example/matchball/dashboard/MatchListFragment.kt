package com.example.matchball.dashboard

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matchball.dashboard.filterbar.FilterAdapter
import com.example.matchball.joinmatch.JoinActivity
import com.example.matchball.databinding.FragmentListBinding
import com.example.matchball.model.FilterModel
import com.example.matchball.model.MatchRequest

class MatchListFragment : Fragment() {

    private lateinit var listFragmentBinding: FragmentListBinding
    private lateinit var matchRequestAdapter: RecyclerAdapter
    private lateinit var filterAdapter : FilterAdapter
    private val matchListViewModel: MatchListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        initFilterList()
        initObserve()
        initEvent()
        matchListViewModel.handleMatchList()

        //search
        listFragmentBinding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                matchRequestAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun initObserve() {
        matchListViewModel.matchListResult.observe(this, { result ->
            listFragmentBinding.swipe.isRefreshing = false
            when (result) {
                is MatchListViewModel.MatchListResult.Loading -> {
                    listFragmentBinding.swipe.isRefreshing = true
                }
                is MatchListViewModel.MatchListResult.ResultFilterOk -> {
                    filterAdapter.addFilterNewData(result.filterList)
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

    private fun initFilterList() {
        listFragmentBinding.filter.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            filterAdapter = FilterAdapter(arrayListOf())
            adapter = filterAdapter
        }
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