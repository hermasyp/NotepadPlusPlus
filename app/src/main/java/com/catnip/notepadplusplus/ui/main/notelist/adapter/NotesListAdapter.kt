package com.catnip.notepadplusplus.ui.main.notelist.adapter;

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.catnip.notepadplusplus.data.model.Note
import com.catnip.notepadplusplus.databinding.ItemLockedNoteBinding
import com.catnip.notepadplusplus.databinding.ItemNoteBinding
import com.catnip.notepadplusplus.utils.CommonFunction

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class NotesListAdapter(private val itemClick: (Note) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: MutableList<Note> = mutableListOf()

    fun setItems(items: List<Note>) {
        clearItems()
        addItems(items)
        notifyDataSetChanged()
    }

    fun addItems(items: List<Note>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int =
        if (items[position].isLocked)
            ITEM_TYPE_LOCKED
        else
            ITEM_TYPE_NOTES

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_NOTES) {
            val binding =
                ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            NoteViewHolder(binding, itemClick)
        } else {
            val binding =
                ItemLockedNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LockedNoteViewHolder(binding, itemClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NoteViewHolder) {
            holder.bindView(items[position])
        } else if (holder is LockedNoteViewHolder) {
            holder.bindView(items[position])
        }
    }

    override fun getItemCount(): Int = items.size


    class NoteViewHolder(private val binding: ItemNoteBinding, val itemClick: (Note) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Note) {
            with(item) {
                binding.root.background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius =
                        CommonFunction.dpToPixels(itemView.context, 8).toFloat()
                    setColor(Color.parseColor(item.hexCardColor))
                    mutate()
                }
                binding.tvTitleNote.text = item.title
                binding.tvBodyNote.text = item.body
                itemView.setOnClickListener { itemClick(this) }
            }

        }
    }

    class LockedNoteViewHolder(
        private val binding: ItemLockedNoteBinding,
        val itemClick: (Note) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Note) {
            with(item) {
                binding.root.background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius =
                        CommonFunction.dpToPixels(itemView.context, 8).toFloat()
                    setColor(Color.parseColor(item.hexCardColor))
                    mutate()
                }
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }

    companion object {
        const val ITEM_TYPE_NOTES = 1
        const val ITEM_TYPE_LOCKED = 2
    }

}