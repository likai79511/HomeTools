package com.agera.hometools.locate.friends

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.agera.hometools.R

/**
 * Created by Agera on 2018/4/27.
 */
class FriendAdapter(ctx: Context, friends: ArrayList<String>?) : RecyclerView.Adapter<FriendAdapter.ViewHolder>() {

    lateinit var mContext: Context
    var mFriends: ArrayList<String>? = null

    init {
        mContext = ctx
        mFriends = friends
    }

    fun setData(friends: ArrayList<String>) {
        mFriends = friends
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.tv?.text = mFriends?.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_friend, null))
    }

    override fun getItemCount(): Int {
        return if (mFriends == null) 0 else mFriends!!.size
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var tv:TextView = itemView?.findViewById(R.id.tv) as TextView
    }
}