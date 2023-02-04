package com.example.jettrivia.component

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettrivia.screens.QuestionViewModel
import com.example.jettrivia.ui.theme.mDarkPurple
import com.example.jettrivia.ui.theme.mLightGray
import com.example.jettrivia.ui.theme.mOffDarkPurple
import com.example.jettrivia.ui.theme.mOffWhite

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
        questionsList?.forEach { question ->
            Log.d("TAG", "Questions: ${question.question}")
        }
    }
}

@Preview
@Composable
fun QuestionDisplay(modifier: Modifier = Modifier) {
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
                .background(color = MaterialTheme.colorScheme.primary),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            QuestionTracker()
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f)
            DottedLine(pathEffect)
        }
    }
}

@Composable
fun DottedLine(pathEffect: PathEffect){

    Canvas(modifier = Modifier.fillMaxWidth().height(1.dp).padding(horizontal = 12.dp), onDraw = {
        drawLine(
            color = mOffWhite,
            start = Offset(0f,0f),
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
    Text(text = buildAnnotatedString {
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
            ){
                 append("$totalSize")
            }
        }
    },
    modifier = Modifier.padding(20.dp))
}
