package com.rustamft.tasksft.screens.editor.picker

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.rustamft.tasksft.utils.Constants.TIME_PICKER_DIALOG_TAG
import com.rustamft.tasksft.utils.datetime.DateTimeInt
import com.rustamft.tasksft.utils.datetime.DateTimeString
import com.rustamft.tasksft.utils.datetime.DateTimeUtil
import java.util.Calendar

class TimePickerFragment(
    private val manager: FragmentManager,
    private val button: Button
) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val string = button.text.toString()
        val time = DateTimeString("", string)
        val millis = DateTimeUtil.stringToMillis(time)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(requireContext(), this, hour, minute, true)
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        val time = DateTimeInt(p1, p2) // hour, minute.
        button.text = DateTimeUtil.intToTimeString(time)
    }

    fun show() = show(manager, TIME_PICKER_DIALOG_TAG)
}
