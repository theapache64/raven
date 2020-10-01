package com.theapache64.raven.feature.quotes

/**
 * Created by theapache64 : Oct 01 Thu,2020 @ 20:03
 */
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theapache64.raven.data.remote.Quote
import com.theapache64.raven.databinding.ItemQuoteBinding

class QuotesAdapter(
    context: Context,
    private val quotes: List<Quote>,
    private val callback: (position: Int) -> Unit
) : RecyclerView.Adapter<QuotesAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuoteBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = quotes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quote = quotes[position]
        holder.binding.quote = quote
    }

    inner class ViewHolder(val binding: ItemQuoteBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                callback(layoutPosition)
            }
        }
    }
}