package com.sunpra.incomeexpense.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageDialog(
    modifier: Modifier = Modifier,
    message: String?,
    onDismissRequest: () -> Unit
) {
    if (message != null) {
        BasicAlertDialog(
            modifier = modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            onDismissRequest = onDismissRequest,
            content = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    TextButton(
                        modifier = Modifier.padding(top = 16.dp).align(Alignment.End),
                        onClick = onDismissRequest
                    ) {
                        Text(
                            text = "OK",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }

            }
        )
    }
}