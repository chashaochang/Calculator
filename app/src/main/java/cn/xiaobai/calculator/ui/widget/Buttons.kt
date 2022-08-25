package cn.xiaobai.calculator.ui.widget

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.xiaobai.calculator.calcInputValue
import cn.xiaobai.calculator.calcResultValue

@Composable
fun CalcButton(str: String, isLandscape: Boolean, modifier: Modifier, fontSize: Int) {
    if (isLandscape) {
        when (str) {
            "AC" -> {
                ACButton(
                    modifier = modifier,
                    text = str,
                    fontSize = fontSize.sp
                )
            }
            "RAD", "√", "π", "INV", "^", "!", "sin", "cos", "tan", "e", "ln", "log" -> {
                WhiteBgButton(
                    modifier = modifier,
                    text = str,
                    fontSize = fontSize.sp,
                    onClick = {})
            }
            "()", "%", "÷", "×", "-", "+", "=" -> {
                TypeTwoButton(
                    modifier = modifier,
                    text = str,
                    fontSize = fontSize.sp
                )
            }
            else -> {
                TypeOneButton(
                    modifier = modifier,
                    text = str,
                    fontSize = fontSize.sp
                )
            }
        }
    } else {
        when (str) {
            "AC" -> {
                ACButton(
                    modifier = modifier,
                    text = str,
                    fontSize = fontSize.sp
                )
            }
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "." -> {
                TypeOneButton(
                    modifier = modifier,
                    text = str,
                    fontSize = fontSize.sp
                )
            }
            "()", "%", "÷", "×", "-", "+", "=" -> {
                TypeTwoButton(
                    modifier = modifier,
                    text = str,
                    fontSize = fontSize.sp
                )
            }
            else -> {
                TypeOneButton(
                    modifier = modifier,
                    text = str,
                    fontSize = fontSize.sp
                )
            }
        }
    }
}

/**
 * 数字键 . 退格
 */
@Composable
fun TypeOneButton(modifier: Modifier, text: String, fontSize: TextUnit) {
    BaseCalcButton(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    )
}

/**
 * （）+ - * / % =
 */
@Composable
fun TypeTwoButton(modifier: Modifier, text: String, fontSize: TextUnit) {
    BaseCalcButton(modifier = modifier, text = text, fontSize = fontSize)
}

/**
 * AC
 */
@Composable
fun ACButton(modifier: Modifier, text: String, fontSize: TextUnit) {
    BaseCalcButton(
        modifier = modifier,
        text = text,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        ),
        fontSize = fontSize
    )
}

@Composable
fun BaseCalcButton(
    modifier: Modifier,
    text: String,
    fontSize: TextUnit,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors()
) {
    FilledTonalButton(
        modifier = modifier,
        contentPadding = PaddingValues(0.dp),
        colors = colors,
        onClick = {
            when (text) {
                "backspace" -> {
                    calcInputValue.value.dropLast(1)
                }
                "AC" -> {
                    calcInputValue.value = ""
                    calcResultValue.value = ""
                }
                "=" -> {
                    calcInputValue.value = calcResultValue.value
                    calcResultValue.value = ""
                }
                "()" -> {

                }
                else -> {
                    calcInputValue.value = calcInputValue.value + text
                }
            }
        }
    ) {
        if (text == "backspace") {
            Icon(
                painter = painterResource(id = cn.xiaobai.calculator.R.drawable.ic_round_backspace_24),
                contentDescription = "backspace"
            )
        } else {
            Text(text = text, fontSize = fontSize)
        }
    }
}

/**
 * 根号 派 sin cos等
 */
@Composable
fun WhiteBgButton(modifier: Modifier, text: String, fontSize: TextUnit, onClick: () -> Unit) {
    TextButton(modifier = modifier, contentPadding = PaddingValues(0.dp), onClick = onClick) {
        Text(text = text, fontSize = fontSize)
    }
}