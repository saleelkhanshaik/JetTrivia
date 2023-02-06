package com.example.jettrivia.component

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.screens.QuestionViewModel
import com.example.jettrivia.ui.theme.mDarkPurple
import com.example.jettrivia.ui.theme.mLightGray
import com.example.jettrivia.ui.theme.mOffWhite
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.jettrivia.ui.theme.mOffDarkPurple
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign


@Composable
fun Questions(viewModel: QuestionViewModel) {
    val questionsList = viewModel.data.value.response?.toMutableList()
    val questionIndex = remember {
        mutableStateOf<Int>(0)
    }
    val isLast = remember {
        mutableStateOf(false)
    }
    if (viewModel.data.value.loadingStatus == true) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp
            )
        }

        Log.d("TAG", "Questions: Loading...")
    } else {
        Log.d("TAG", "Questions: stopped...")
        val question = try {
            questionsList?.get(questionIndex.value)
        } catch (e: Exception) {
            null
        }

        if (questionsList?.isNotEmpty() == true) {
            QuestionDisplay(
                questionItem = question!!,
                questionIndex = questionIndex,
                onNextClicked = {
                    if (questionIndex.value == questionsList.size - 1) {
                        isLast.value = true
                    } else {
                        questionIndex.value = it + 1
                    }
                },
                isLast = isLast,
                totalSize = questionsList.size
            )
        }
    }
}


@Composable
fun QuestionDisplay(
    modifier: Modifier = Modifier,
    questionItem: QuestionItem,
    questionIndex: MutableState<Int>,
    onNextClicked: (Int) -> Unit,
    isLast: MutableState<Boolean>,
    totalSize: Int
) {
    Log.d("Hello", "QuestionDisplay:${isLast} && ${questionIndex.value} :: $questionItem")
    val choicesState = remember(questionItem) {
        questionItem.incorrect_answers
    }

    val answerState = remember(questionItem) {
        mutableStateOf<Int?>(null)
    }
    val correctAnswerState = remember(questionItem) {
        mutableStateOf<Boolean?>(null)
    }
    val updateAnswer: (Int) -> Unit = remember(questionItem) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == questionItem.correct_answer
        }
    }

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(4.dp),
        color = mDarkPurple
    ) {
        val current = LocalContext.current
        Column(
            modifier = Modifier
                .padding(12.dp)
                .background(color = mDarkPurple),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            if(questionIndex.value > 3)  ShowProgress(score = questionIndex.value)
            QuestionTracker(currentQuestionNumber = questionIndex.value, totalSize = totalSize )
            DottedLine(pathEffect)
            Column(modifier = Modifier.padding(top = 24.dp)) {
                Text(
                    text = questionItem.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(
                            alignment = Alignment.Start
                        ),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                choicesState.forEachIndexed { index, answerTxt ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 1.dp, brush = Brush.linearGradient(
                                    colors = listOf(mOffDarkPurple, mOffDarkPurple)
                                ), shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50, topEndPercent = 50,
                                    bottomEndPercent = 50, bottomStartPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = (answerState.value == index), onClick = {
                                updateAnswer(index)
                            },
                            modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if (correctAnswerState.value == true && index == answerState.value) {
                                    Color.Green.copy(alpha = 0.2f)
                                } else {
                                    Color.Red.copy(alpha = 0.3f)
                                }
                            )
                        ) //radio button ends
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    color = if (correctAnswerState.value == true && index == answerState.value) {
                                        Color.Green
                                    } else if (correctAnswerState.value == false && index == answerState.value) {
                                        Color.Red
                                    } else {
                                        mOffWhite
                                    },
                                    fontSize = 17.sp
                                ),
                            ) {
                                append(answerTxt)
                            }
                        }
                        Text(text = annotatedString)
                    }
                }
                Button(
                    onClick = {
                        if (isLast.value) {
                            Toast.makeText(current, "You have completed ", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            onNextClicked(questionIndex.value)
                        }
                    },
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Next")
                }
            }
        }
    }
}

@Composable
fun CreateRadioButtonList(size: Int) {

}

@Composable
fun DottedLine(pathEffect: PathEffect) {

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .padding(horizontal = 12.dp), onDraw = {
        drawLine(
            color = mOffWhite,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    })
}

@Composable
fun QuestionTracker(
    totalSize: Int = 100,
    currentQuestionNumber: Int = 10
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
                withStyle(
                    style = SpanStyle(
                        fontSize = 27.sp, fontWeight = FontWeight.Bold,
                        color = mLightGray
                    )
                ) {
                    append("Questions $currentQuestionNumber/")
                }
                withStyle(
                    style = SpanStyle(
                        color = mLightGray,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                ) {
                    append("$totalSize")
                }
            }
        },
        modifier = Modifier.padding(20.dp)
    )
}
@Preview
@Composable
fun Test(){
    Button(onClick = { /*TODO*/ }) {
        Text(text = "Hellooo")
    }
}


@Preview
@Composable
fun ShowProgress(score: Int = 10) {
    val gradiantColor = Brush.linearGradient(
        listOf(
            Color(0xFFF95075),
            Color(0xFFBE6BE5)
        )
    )
    val progressFactor = remember(score) {
        mutableStateOf(score * 0.005f)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp, brush = Brush.linearGradient(listOf(mOffDarkPurple, mOffDarkPurple)),
                shape = RoundedCornerShape(34.dp)
            )
            .clip(RoundedCornerShape(50.dp))
            .background(Color.Transparent)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(contentPadding = PaddingValues(4.dp), onClick = { },
            modifier = Modifier
                .clickable { false }
                .background(brush = gradiantColor)
                .fillMaxWidth(progressFactor.value),
            enabled = false,
            elevation = null,
            colors = ButtonDefaults.buttonColors(

                contentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        ) {
            Text(
                text = "1",
                color = mOffWhite,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(23.dp)
                    )
                    .fillMaxWidth()
                    .padding(6.dp)
            )
        }
    }
}
