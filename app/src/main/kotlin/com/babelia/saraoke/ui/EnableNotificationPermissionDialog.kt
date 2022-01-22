package com.babelia.saraoke.ui

import android.view.KeyEvent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.babelia.saraoke.R
import com.babelia.saraoke.ui.theme.Dimens

/**
 * Dialog to inform the user has to update the application before continue using it.
 */
@ExperimentalComposeUiApi
@Composable
fun EnableNotificationPermissionDialog(onButtonClick: () -> Unit) {
    val focusRequester = FocusRequester()

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false)
    ) {
        Surface(Modifier.width(Dimens.generalDimens.dialog_width)) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.generalDimens.layout_padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = Dimens.generalDimens.dialog_internal_padding),
                        text = stringResource(R.string.enable_notification_permission_dialog_title),
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        modifier = Modifier.padding(bottom = Dimens.generalDimens.dialog_internal_padding),
                        text = stringResource(R.string.enable_notification_permission_dialog_text),
                        style = MaterialTheme.typography.subtitle2,
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = Dimens.generalDimens.dialog_button_padding_top)
                            .focusRequester(focusRequester)
                            .focusTarget()
                            .onKeyEvent {
                                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                                    onButtonClick()
                                }
                                true
                            },
                        contentPadding = PaddingValues(Dimens.generalDimens.dialog_button_internal_padding),
                        onClick = {
                            onButtonClick()
                        }
                    ) {
                        Text(stringResource(R.string.enable_notification_permission_dialog_button).uppercase())
                    }
                }
            }
        }
    }
}