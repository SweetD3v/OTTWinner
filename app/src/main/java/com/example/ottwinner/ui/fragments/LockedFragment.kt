package com.example.ottwinner.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ottwinner.adapters.TopWinnersAdapter
import com.example.ottwinner.databinding.FragmentLockedBinding
import com.example.ottwinner.models.TopWinnerModel

class LockedFragment : Fragment() {
    lateinit var binding: FragmentLockedBinding
    lateinit var ctx: Context

    var topWinnersAdapter: TopWinnersAdapter? = null

    val winnersList by lazy {
        mutableListOf(
            TopWinnerModel("John Doe", null, "Male"),
            TopWinnerModel("Samantha Carter", null, "Female"),
            TopWinnerModel("John Doe", null, "Male"),
            TopWinnerModel("Samantha Carter", null, "Female"),
            TopWinnerModel("John Doe", null, "Male"),
            TopWinnerModel("Samantha Carter", null, "Female"),
            TopWinnerModel("John Doe", null, "Male"),
            TopWinnerModel("Samantha Carter", null, "Female"),
            TopWinnerModel("John Doe", null, "Male"),
            TopWinnerModel("Samantha Carter", null, "Female"),
            TopWinnerModel("John Doe", null, "Male"),
            TopWinnerModel("Samantha Carter", null, "Female")
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            rvTopWinners.layoutManager = LinearLayoutManager(ctx).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }

            topWinnersAdapter = TopWinnersAdapter(ctx)
            rvTopWinners.adapter = topWinnersAdapter
            topWinnersAdapter?.submitList(winnersList)
        }
    }

    companion object {
        fun newInstance(): LockedFragment {
            val fragment = LockedFragment()
            return fragment
        }
    }
}