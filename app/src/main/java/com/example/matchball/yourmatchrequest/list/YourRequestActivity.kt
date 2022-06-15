package com.example.matchball.yourmatchrequest.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matchball.databinding.ActivityYourRequestBinding
import com.example.matchball.home.MainActivity
import com.example.matchball.model.MatchRequest
import com.example.matchball.yourmatchrequest.details.YourRequestDetailsActivity
import com.google.firebase.database.*

class YourRequestActivity : AppCompatActivity() {

    private val yourRequestViewModel : YourRequestViewModel by viewModels()
    private lateinit var yourRequestBinding: ActivityYourRequestBinding
    private lateinit var yourRequestAdapter : YourRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        yourRequestBinding = ActivityYourRequestBinding.inflate(layoutInflater)
        setContentView(yourRequestBinding.root)

        initList()
        initEvents()
        initObserve()
        yourRequestViewModel.handleYourRequestMatch()
    }

    private fun initEvents() {
        back()
    }

    private fun back() {
        yourRequestBinding.back.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun initObserve() {
        yourRequestViewModel.yourRequestListResult.observe(this, { result ->
            when (result) {
                is YourRequestViewModel.YourRequestListResult.ResultOk -> {
                    yourRequestAdapter.addNewData(result.matchList)
                }
                is YourRequestViewModel.YourRequestListResult.ResultError -> {
                    Toast.makeText(this, result.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initList() {
        yourRequestBinding.rcvYourRequest.apply {
            layoutManager = LinearLayoutManager(context)
            yourRequestAdapter = YourRequestAdapter(arrayListOf())
            adapter = yourRequestAdapter

            yourRequestAdapter.setOnItemClickListerner(object :
            YourRequestAdapter.OnItemClickListerner{
                override fun onItemClick(data: MatchRequest) {
                    YourRequestDetailsActivity.startDetails(context, data)
                }
            })
        }
    }
}