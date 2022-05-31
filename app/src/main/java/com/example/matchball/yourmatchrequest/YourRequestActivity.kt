package com.example.matchball.yourmatchrequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matchball.dashboard.RecyclerAdapter
import com.example.matchball.databinding.ActivityYourRequestBinding
import com.example.matchball.model.MatchRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class YourRequestActivity : AppCompatActivity() {

    private lateinit var yourRequestBinding: ActivityYourRequestBinding
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var matchRequestList : ArrayList<MatchRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        yourRequestBinding = ActivityYourRequestBinding.inflate(layoutInflater)
        setContentView(yourRequestBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        setRecyclerView()
        itemsClick()

    }

    private fun itemsClick() {
        recyclerAdapter.setOnItemClickListerner(object : RecyclerAdapter.OnItemClickListerner{
            override fun onItemClick(requestData: MatchRequest) {
                YourRequestDetailsActivity.startRequestDetails(
                    this@YourRequestActivity,
                    requestData
                )
            }

        })
    }

    private fun setRecyclerView() {
        yourRequestBinding.rcvYourRequest.apply {
            layoutManager = LinearLayoutManager(context)
            matchRequestList = arrayListOf<MatchRequest>()

            getData()

        }
    }

    private fun getData() {
        val uid = firebaseAuth.currentUser!!.uid

        databaseReference = FirebaseDatabase.getInstance().getReference("MatchRequest").child(uid)
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (requestSnapshot in snapshot.children) {
                        val yourRequest = requestSnapshot.getValue(MatchRequest::class.java)
                        matchRequestList.add(yourRequest!!)
                    }
                    yourRequestBinding.rcvYourRequest.adapter = recyclerAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@YourRequestActivity, "Load Data Failed", Toast.LENGTH_SHORT).show()
            }
        })

    }
}