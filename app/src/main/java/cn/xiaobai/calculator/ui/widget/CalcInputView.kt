@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)

package cn.xiaobai.calculator.ui.widget

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.ResistanceConfig
import androidx.compose.material.SwipeableState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.xiaobai.calculator.calcInputValue
import cn.xiaobai.calculator.calcResultValue

@Composable
fun CalcInputView(swipeState: SwipeableState<Int>) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
//    val initTargetIndex = 0
    val initValue = calcInputValue.collectAsState().value
//    val initSelectionIndex = initTargetIndex.takeIf { it <= initValue.length } ?: initValue.length
//    val textFieldValueState = remember {
//        mutableStateOf(
//            TextFieldValue(
//                text = initValue,
//                selection = TextRange(initSelectionIndex)
//            )
//        )
//    }
    val result = calcResultValue.collectAsState().value
    val height by remember {
        mutableStateOf(if(isLandscape) 150 else 234)
    }
    //一级锚点距离顶部 = 历史布局高度
    val historyHeight = with(LocalDensity.current) {
        if (isLandscape) 24.dp.toPx() else 60.dp.toPx()
    }
    val screenHeight =
        with(LocalConfiguration.current) {
            screenHeightDp
        }
    //二级锚点位置 = 屏幕高度 - 这个控件高度 - AppBar高度64
    val secondHeight = with(LocalDensity.current){
        screenHeight - height - (if(isLandscape) 20.dp.toPx() else 30.dp.toPx())
    }
    val anchors = mapOf(secondHeight to 2, historyHeight to 1, 0f to 0)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .swipeable(
                state = swipeState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Vertical,
                resistance = ResistanceConfig(2f, 0.5f, 0.5f)
            )
            .height(height.dp)
    ) {
        SmallTopAppBar(
            title = {
                if (swipeState.currentValue > 0) {
                    Text("当前表达式")
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
                    modifier = Modifier.width(140.dp),
                    onDismissRequest = { expandState.value = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "历史记录", fontSize = 16.sp) },
                        onClick = {
                            expandState.value = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "选择主题", fontSize = 16.sp) },
                        onClick = {
                            expandState.value = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "发送反馈", fontSize = 16.sp) },
                        onClick = {
                            expandState.value = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "帮助", fontSize = 16.sp) },
                        onClick = {
                            expandState.value = false
                        })
                }
            }
        )
        BasicTextField(
            value = initValue,
            onValueChange = {
//                Log.i("TAG", "onValueChange: " + it)
//                calcInputValue.value = it
//                calcResultValue.value = Calculator.cal(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isLandscape) 60.dp else 100.dp)
                .background(Color.Transparent)
                .padding(20.dp, 0.dp)
                .align(Alignment.End),
            readOnly = false,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = if (isLandscape) 52.sp else 80.sp,
                textAlign = TextAlign.End
            ),
        )
        //横屏隐藏计算结果
        if (!isLandscape) {
            Text(
                text = result,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(16.dp, 8.dp),
                textAlign = TextAlign.End,
                fontSize = 32.sp
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Divider(
                Modifier
                    .width(20.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(4.dp)), color = Color.Black
            )
        }
    }
}