    package com.example.jettrivia.component

import android.util.Log
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun Questions(viewModel: QuestionViewModel) {
    val questionsList = viewModel.data.value.response?.toMutableList()
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
        if(questionsList?.isNotEmpty() == true){
            QuestionDisplay(questionItem = questionsList.first())
        }
    }
}


@Composable
fun QuestionDisplay(
    modifier: Modifier = Modifier,
    questionItem: QuestionItem,
//    questionIndex: MutableState<Int>,
//    viewModel: QuestionViewModel,
    onNextClicked: (Int) -> Unit ={}
) {
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
        Column(
            modifier = Modifier
                .padding(12.dp)
                .background(color = mDarkPurple),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            QuestionTracker()
            DottedLine(pathEffect)
            Column (modifier = Modifier.padding(top = 24.dp)){
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
                        Text(text = answerTxt)
                    }
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
    modifier: Modifier = Modifier,
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
