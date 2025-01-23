package com.example.tagarela.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.tagarela.data.models.Card

@Composable
fun CustomModal(card: Card, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(card.name) },
        text = { Text("Detalhes do cartão") },
        confirmButton = {
            Button(onClick = onClose) {
                Text("Fechar")
            }
        }
    )
}
