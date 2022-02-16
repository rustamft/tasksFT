package com.rustamft.tasksft.database.entity

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.rustamft.tasksft.utils.datetime.DateTimeUtil

class ObservableTask {

    var id = -1
    val title = ObservableField("") // Two-way binding.
    val description = ObservableField("") // Two-way binding.
    val hasReminder = ObservableField(false) // Two-way binding.
    val date = ObservableField("") // Updated via fragment method.
    val time = ObservableField("") // Updated via fragment method.
    var isChanged = false

    fun fillFrom(task: AppTask) {
        id = task.id
        if (id != -1) {
            title.set(task.title)
            description.set(task.description)
            hasReminder.set(DateTimeUtil.isInFuture(task.millis))
        }
        val dateTime = if (hasReminder.get()!!) {
            DateTimeUtil.millisToString(task.millis)
        } else {
            DateTimeUtil.nextFullHourString()
        }
        date.set(dateTime.date)
        time.set(dateTime.time)
    }

    fun observeChanges() {
        val callback = OnPropertyChangedCallback()
        callback.activate()
    }

    /*
    /////////////////////////// Callback ///////////////////////////
    */
    inner class OnPropertyChangedCallback : Observable.OnPropertyChangedCallback() {

        private val listOfCurrent = listOf(
            title,
            description,
            hasReminder,
            date,
            time
        )
        private val listOfOld = mutableListOf<Any>()

        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            isChanged = isPropertyChanged()
            if (isChanged) {
                for (item in listOfCurrent) {
                    item.removeOnPropertyChangedCallback(this)
                }
            }
        }

        private fun isPropertyChanged(): Boolean {
            for ((i, item) in listOfCurrent.withIndex()) {
                val equals = item.get()!! == listOfOld[i]
                if (!equals) {
                    return true
                }
            }
            return false
        }

        fun activate() {
            for (item in listOfCurrent) {
                listOfOld.add(item.get()!!)
                item.addOnPropertyChangedCallback(this)
            }
        }
    }
}
