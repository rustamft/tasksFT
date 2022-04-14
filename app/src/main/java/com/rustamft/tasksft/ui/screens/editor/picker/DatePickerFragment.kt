package com.rustamft.tasksft.ui.screens.editor.picker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.rustamft.tasksft.utils.DATE_PICKER_DIALOG_TAG
import com.rustamft.tasksft.utils.datetime.DateTimeInt
import com.rustamft.tasksft.utils.datetime.DateTimeProvider
import com.rustamft.tasksft.utils.datetime.DateTimeString
import java.util.Calendar

open class DatePickerFragment(
    private val manager: FragmentManager,
    private val button: Button
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val string = button.text.toString()
        val date = DateTimeString(string, "")
        val millis = DateTimeProvider.stringToMillis(date)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val date = DateTimeInt(p1, p2, p3) // year, month, day.
        button.text = DateTimeProvider.intToDateString(date)
    }

    fun show() = show(manager, DATE_PICKER_DIALOG_TAG)
}
