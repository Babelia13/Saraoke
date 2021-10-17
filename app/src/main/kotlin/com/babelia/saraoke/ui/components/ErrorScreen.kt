package com.babelia.saraoke.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.babelia.saraoke.R
import com.babelia.saraoke.ui.theme.DynamicLight

/**
 * Common error screen.
 */
@Composable
fun ErrorScreen(@StringRes errorTextRes: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sad_face))
        LottieAnimation(
            modifier = Modifier.size(100.dp),
            iterations = LottieConstants.IterateForever,
            composition = composition.value
        )
        Text(
            modifier = Modifier.padding(24.dp),
            text = stringResource(errorTextRes),
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            color = DynamicLight,
        )
    }
}