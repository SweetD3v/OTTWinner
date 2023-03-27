package com.example.ottwinner.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ottwinner.databinding.ItemTopWinnersBinding
import com.example.ottwinner.models.TopWinnerModel

class TopWinnersAdapter(var ctx: Context) :
    ListAdapter<TopWinnerModel, TopWinnersAdapter.VH>(WinnersValueComparator()) {

    val listDiffer by lazy { AsyncListDiffer(this, WinnersValueComparator()) }

    fun updateList(listOttWinners: MutableList<TopWinnerModel>) {
        listDiffer.submitList(listOttWinners)
    }

    inner class VH(val binding: ItemTopWinnersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemTopWinnersBinding.inflate(LayoutInflater.from(ctx), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val winnerModel = listDiffer.currentList[holder.adapterPosition]
        holder.binding.run {
            winnerModel.picResource?.let { imgUserPic.setImageResource(it) }
            txtUserName.text = winnerModel.name
        }
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }
}

class WinnersValueComparator : DiffUtil.ItemCallback<TopWinnerModel>() {
    override fun areItemsTheSame(oldItem: TopWinnerModel, newItem: TopWinnerModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: TopWinnerModel, newItem: TopWinnerModel): Boolean {
        return oldItem.name == newItem.name
    }

}