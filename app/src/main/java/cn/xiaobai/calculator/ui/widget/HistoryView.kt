@file:OptIn(ExperimentalMaterialApi::class)

package cn.xiaobai.calculator.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cn.xiaobai.calculator.R

@Composable
fun HistoryView(modifier: Modifier) {
    val history = remember {
        mutableListOf<String>()
    }
    if (history.isNotEmpty()) {
        LazyColumn(
            modifier = modifier
        ) {

        }
    } else {
        Box(modifier = modifier,
            contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_history_24),
                    contentDescription = "history",
                    tint = Color.DarkGray
                )
                Text("没有历史记录", fontSize = 20.sp, fontWeight = FontWeight.Normal)
            }
        }
    }
}