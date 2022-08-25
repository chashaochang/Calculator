@file:OptIn(ExperimentalMaterialApi::class)

package cn.xiaobai.calculator

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.ResistanceConfig
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import cn.xiaobai.calculator.ui.theme.CalculatorTheme
import cn.xiaobai.calculator.ui.widget.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        setContent {
            CalculatorContent()
        }
    }
}

val calcInputValue : MutableStateFlow<String> = MutableStateFlow("")
val calcResultValue : MutableStateFlow<String> = MutableStateFlow("")

@Composable
fun CalculatorContent() {
    //是否横屏
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scope = rememberCoroutineScope()
    val swipeState = rememberSwipeableState(0)
    val landscapeList = listOf(
        listOf("RAD", "√", "π", "7", "8", "9", "÷", "AC"),
        listOf("INV", "^", "!", "4", "5", "6", "×", "()"),
        listOf("sin", "cos", "tan", "1", "2", "3", "-", "%"),
        listOf("e", "ln", "log", "0", ".", "backspace", "+", "="),
    )
    val portraitPlusList = listOf(
        listOf("√", "π", "^", "!"),
        listOf("RAD", "sin", "cos", "tan"),
        listOf("INV", "e", "ln", "log"),
    )
    val portraitList = listOf(
        listOf("AC", "()", "%", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "backspace", "="),
    )
    var btnFontSize by remember {
        mutableStateOf(if (isLandscape) 20 else 28)
    }
    CalculatorTheme {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .apply {
                    if (!isLandscape) {
                        navigationBarsPadding()
                    }
                }
                .fillMaxHeight()
        ) {
            Surface(shape = RoundedCornerShape(0.dp, 0.dp, 28.dp, 28.dp)) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    AnimatedVisibility(swipeState.targetValue == 2) {
                        CustomTopAppBar {
                            scope.launch {
                                swipeState.animateTo(0)
                            }
                        }
                    }
                    HistoryView(modifier = Modifier
                        .fillMaxWidth()
                        .height(swipeState.offset.value.dp)
                        .apply {
                            if (swipeState.currentValue == 2)
                                weight(1f)
                        }
                        .background(color = MaterialTheme.colorScheme.background))
                    CalcInputView(
                        swipeState = swipeState
                    )
                }
            }

            AnimatedVisibility(
                swipeState.targetValue != 2,
                enter = slideInVertically { height -> height * 2 }) {
                if (isLandscape) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .onSizeChanged {
                            Log.i("TAG", "height: " + it.height)
                            btnFontSize = (it.height / 30)
                        }
                        .padding(16.dp, 8.dp, 16.dp, 16.dp)) {
                        landscapeList.forEach {
                            Row(
                                modifier = Modifier
                                    .weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                it.forEach {
                                    CalcButton(
                                        str = it,
                                        isLandscape = isLandscape,
                                        modifier = Modifier.weight(1f),
                                        fontSize = btnFontSize
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                } else {
                    Column(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            val isExpanded = remember {
                                mutableStateOf(true)
                            }
                            Column(Modifier.weight(1f)) {
                                portraitPlusList.forEachIndexed { index, list ->
                                    if (index == 0) {
                                        Row {
                                            list.forEach {
                                                WhiteBgButton(
                                                    modifier = Modifier.weight(1f),
                                                    onClick = { },
                                                    text = it, fontSize = btnFontSize.sp
                                                )
                                            }
                                        }
                                    } else {
                                        AnimatedVisibility(!isExpanded.value) {
                                            Row {
                                                list.forEach {
                                                    TextButton(
                                                        modifier = Modifier.weight(1f),
                                                        onClick = { }) {
                                                        Text(it, fontSize = btnFontSize.sp)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            TextButton(onClick = { isExpanded.value = !isExpanded.value }, contentPadding = PaddingValues(0.dp)) {
                                Icon(
                                    painterResource(id = if (isExpanded.value) R.drawable.ic_round_expand_more_24 else R.drawable.ic_round_expand_less_24),
                                    contentDescription = "expand",
                                    modifier = Modifier.size(16.dp),
                                )
                            }
                        }
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .onSizeChanged {
                                    btnFontSize = it.height / 40
                                }
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            portraitList.forEach {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    it.forEach {
                                        val itemModifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                        CalcButton(
                                            str = it,
                                            isLandscape = isLandscape,
                                            modifier = itemModifier,
                                            fontSize = btnFontSize
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
//        }
    }
}