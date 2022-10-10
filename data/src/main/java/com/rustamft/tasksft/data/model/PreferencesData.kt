package com.rustamft.tasksft.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.rustamft.tasksft.domain.model.Preferences.Theme

@Entity(tableName = "preferences")
internal data class PreferencesData(
    @PrimaryKey
    val id: Int = 0,
    @field:TypeConverters(ThemeTypeConverter::class)
    val theme: Theme = Theme.Auto,
    val backupDirectory: String = ""
) {

    object ThemeTypeConverter {

        @TypeConverter
        fun themeToInt(theme: Theme): Int {
            return when (theme) {
                is Theme.Auto -> 0
                is Theme.Light -> 1
                is Theme.Dark -> 2
            }
        }

        @TypeConverter
        fun intToTheme(int: Int): Theme {
            return when (int) {
                0 -> Theme.Auto
                1 -> Theme.Light
                2 -> Theme.Dark
                else -> Theme.Auto
            }
        }
    }
}
