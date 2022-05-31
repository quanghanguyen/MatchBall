package com.example.matchball.dashboard

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matchball.joinmatch.JoinActivity
import com.example.matchball.databinding.FragmentListBinding
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.DatabaseConnection
import com.example.matchball.model.MatchRequest
import com.google.firebase.database.*


class ListFragment : Fragment() {

    private lateinit var listFragmentBinding : FragmentListBinding
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var matchRequestArrayList : ArrayList<MatchRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()

        listFragmentBinding.swipe.setOnRefreshListener {
            getData()
            Handler().postDelayed(Runnable {
                listFragmentBinding.swipe.isRefreshing = false
            }, 2000)
        }
    }

    private fun getData() {
        listFragmentBinding.rcvMatchRequest.apply {
            layoutManager = LinearLayoutManager(context)
            matchRequestArrayList = arrayListOf<MatchRequest>()
            recyclerAdapter = RecyclerAdapter(matchRequestArrayList)
            getMatchRequestData()
        }

        recyclerAdapter.setOnItemClickListerner(object : RecyclerAdapter.OnItemClickListerner{
            override fun onItemClick(requestData: MatchRequest) {
                JoinActivity.startDetails(requireContext(), requestData)
            }
        })
    }

    private fun getMatchRequestData() {

        val uid = AuthConnection.auth.currentUser!!.uid

        DatabaseConnection.databaseReference.getReference("MatchRequest").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (requestSnapshot in snapshot.children) {
                        val request = requestSnapshot.getValue(MatchRequest::class.java)
                        matchRequestArrayList.add(request!!)
                    }
                    listFragmentBinding.rcvMatchRequest.adapter = recyclerAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        listFragmentBinding = FragmentListBinding.inflate(inflater, container, false)
        return listFragmentBinding.root
    }

    companion object {

        fun newInstance() = ListFragment()
    }
}