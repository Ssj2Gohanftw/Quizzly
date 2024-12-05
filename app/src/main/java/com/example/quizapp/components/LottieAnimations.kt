package com.example.quizapp.components
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.quizapp.R

@Composable
fun LoadingAnimation(){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    LottieAnimation(composition=composition, modifier = Modifier.size(300.dp))
}
@Composable
fun NoInternet(){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.nointernet))
    LottieAnimation(composition=composition, iterations = LottieConstants.IterateForever, modifier = Modifier.size(200.dp))
}
@Composable
fun LoginProfileAnimation(){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.login))
    LottieAnimation(composition = composition, iterations = 1,modifier = Modifier.size(200.dp))
}
@Composable
fun RegistrationProfileAnimation(){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.registration))
    LottieAnimation(composition = composition, iterations = 1,modifier = Modifier.size(200.dp))
}