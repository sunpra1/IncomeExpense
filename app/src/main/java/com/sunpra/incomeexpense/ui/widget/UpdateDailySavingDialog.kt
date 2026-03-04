package com.sunpra.incomeexpense.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateDailySavingDialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    currentDailySavingAmount: Double,
    onDismissRequest: () -> Unit,
    onDailySavingAmountUpdated: (Double) -> Unit
) {
    if (showDialog) {

        var amount by remember { mutableStateOf(currentDailySavingAmount.toString()) }
        var amountError by remember { mutableStateOf<String?>(null) }

        fun onAmountChanged(value: String) {
            amount = value
        }

        fun handleUpdateClick() {
            amountError = if (amount.trim().isEmpty()) {
                "Daily saving amount is required."
            } else if (amount.toDoubleOrNull() == null) {
                "Daily saving amount must be number."
            } else {
                null
            }

            if (amountError != null) return
            onDismissRequest().also {
                onDailySavingAmountUpdated(amount.toDouble())
            }
        }

        BasicAlertDialog(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background),
            onDismissRequest = onDismissRequest,
            content = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp),
                        text = "Update Daily Saving",
                        style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    TextField(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        prefix = {
                            Text(
                                text = "NRs.",
                                style = MaterialTheme.typography.bodyLarge
                                    .copy(fontWeight = FontWeight.Bold)
                            )
                        },
                        label = {
                            Text(
                                text = "Daily Saving Amount",
                                style = MaterialTheme.typography.bodyMedium
                                    .copy(fontWeight = FontWeight.SemiBold)
                            )
                        },
                        placeholder = {
                            Text(
                                text = "35000.0",
                                style = MaterialTheme.typography.bodyMedium
                                    .copy(fontWeight = FontWeight.SemiBold)
                            )
                        },
                        value = amount,
                        onValueChange = ::onAmountChanged,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    InputFieldError(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        errorText = amountError
                    )

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .padding(top = 24.dp)
                            .align(Alignment.End),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextButton(
                            onClick = onDismissRequest
                        ) {
                            Text(
                                text = "CANCEL",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.error)
                            )
                        }


                        TextButton(
                            onClick = ::handleUpdateClick
                        ) {
                            Text(
                                text = "UPDATE",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }
                }

            }
        )
    }
}