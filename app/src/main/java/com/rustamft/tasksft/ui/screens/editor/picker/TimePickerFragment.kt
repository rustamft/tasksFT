package com.rustamft.tasksft.ui.screens.editor.picker

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.rustamft.tasksft.utils.TIME_PICKER_DIALOG_TAG
import com.rustamft.tasksft.utils.datetime.DateTimeInt
import com.rustamft.tasksft.utils.datetime.DateTimeProvider
import com.rustamft.tasksft.utils.datetime.DateTimeString
import java.util.Calendar

class TimePickerFragment(
    private val manager: FragmentManager,
    private val button: Button
) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val string = button.text.toString()
        val time = DateTimeString("", string)
        val millis = DateTimeProvider.stringToMillis(time)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(requireContext(), this, hour, minute, true)
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        val time = DateTimeInt(p1, p2) // hour, minute.
        button.text = DateTimeProvider.intToTimeString(time)
    }

    fun show() = show(manager, TIME_PICKER_DIALOG_TAG)
}
