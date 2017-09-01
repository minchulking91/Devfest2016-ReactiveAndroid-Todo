package com.nobrain.devfest.reactiveandroid.view


import android.support.v7.widget.RecyclerView

interface OnRecyclerItemClickListener {
    fun onItemClick(adapter: RecyclerView.Adapter<*>, position: Int)
}
