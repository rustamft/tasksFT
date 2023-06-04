package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.rustamft.tasksft.presentation.model.UIText
import com.rustamft.tasksft.presentation.theme.Shapes

@Composable
internal fun <T> DropdownMenuElement(
    items: Map<T, UIText>,
    selectedItemState: MutableState<T>,
    onClickAdditional: () -> Unit = {}
) {

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        Button(
            onClick = { expanded = true },
            content = { Text(text = items[selectedItemState.value]?.asString() ?: "null") },
            shape = Shapes.large
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { (item, uiText) ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    selectedItemState.value = item
                    onClickAdditional()
                }) {
                    Text(uiText.asString())
                }
            }
        }
    }
}
