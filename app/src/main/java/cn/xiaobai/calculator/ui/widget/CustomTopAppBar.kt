@file:OptIn(ExperimentalMaterial3Api::class)

package cn.xiaobai.calculator.ui.widget

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun CustomTopAppBar(navigateBackClick:()->Unit = {}) {
    SmallTopAppBar(
        title = {
            Text("历史记录")
        },
        navigationIcon = {
            IconButton(onClick = navigateBackClick) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "back"
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        actions = {
            val expandState = remember {
                mutableStateOf(false)
            }
            IconButton(onClick = {
                expandState.value = true
            }) {
                Icon(Icons.Filled.MoreVert, "")
            }
            DropdownMenu(
                expanded = expandState.value,
                modifier = Modifier.width(160.dp),
                onDismissRequest = { expandState.value = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = "清除", fontSize = 16.sp) },
                    onClick = {
                        expandState.value = false
                    })
            }
        }
    )
}