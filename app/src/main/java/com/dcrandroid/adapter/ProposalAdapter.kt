package com.dcrandroid.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dcrandroid.R
import com.dcrandroid.activities.ProposalDetails
import com.dcrandroid.data.Proposal
import com.dcrandroid.util.Utils
import java.util.*


class ProposalAdapter(private val proposals: List<Proposal>, private val context: Context) : RecyclerView.Adapter<ProposalAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.proposal_list_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val proposal = proposals[position]
        holder.title.text = proposal.name
        val meta = String.format(Locale.getDefault(), "updated %s \nby %s \nversion %s - %d Comments",
                Utils.calculateTime(System.currentTimeMillis() / 1000 - proposal.timestamp, context), proposal.username, proposal.version, proposal.getNumcomments())
        holder.meta.setText(meta, TextView.BufferType.SPANNABLE)
        if (proposal.voteStatus != null && proposal.voteStatus!!.totalvotes != 0) {
            holder.progress.visibility = View.VISIBLE
            holder.progressBar.visibility = View.VISIBLE
            val percentage = (proposal.voteStatus!!.yes.toFloat() / proposal.voteStatus!!.totalvotes.toFloat()) * 100
            holder.progress.text = "%.2f%%".format(percentage)
            holder.progressBar.progress = percentage.toInt()
        } else {
            holder.progress.visibility = View.GONE
            holder.progressBar.visibility = View.GONE
        }
        holder.view.setOnClickListener {
            val intent = Intent(context, ProposalDetails::class.java)
            val b = Bundle()
            b.putSerializable("proposal", proposal)
            intent.putExtras(b)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return proposals.size
    }

    inner class MyViewHolder internal constructor(var view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var meta: TextView = view.findViewById(R.id.meta)
        var progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        var progress: TextView = view.findViewById(R.id.progress)
    }
}