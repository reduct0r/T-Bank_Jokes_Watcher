package com.example.homework_project_1.main.recycler.util


import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.example.homework_project_1.main.data.ViewTyped
import com.example.homework_project_1.main.data.ViewTyped.*

class JokeDiffUtilCallback(
    private val oldList: List<Joke>,
    private val newList: List<Joke>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        val diffBundle = Bundle()

        if (oldItem.avatar != newItem.avatar) {
            diffBundle.putInt("KEY_AVATAR", newItem.avatar ?: 0)
        }
        if (oldItem.category != newItem.category) {
            diffBundle.putString("KEY_CATEGORY", newItem.category)
        }
        if (oldItem.question != newItem.question) {
            diffBundle.putString("KEY_QUESTION", newItem.question)
        }
        if (oldItem.answer != newItem.answer) {
            diffBundle.putString("KEY_ANSWER", newItem.answer)
        }

        return if (diffBundle.size() == 0) null else diffBundle
    }
}

data class PersonNamePayload(val name: String)