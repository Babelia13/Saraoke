package com.babelia.saraoke.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babelia.saraoke.R

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
        Image(
            painter = painterResource(R.drawable.ic_sad_face),
            contentDescription = "Mickey Mouse logo",
            modifier = Modifier
                .size(72.dp)
                .padding(12.dp)
        )
        Text(
            text = stringResource(errorTextRes),
            fontSize = 14.sp,
        )
    }
}