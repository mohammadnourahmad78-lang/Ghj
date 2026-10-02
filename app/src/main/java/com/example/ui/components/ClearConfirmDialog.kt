package com.example.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CairoFontFamily

@Composable
fun ClearConfirmDialog(
    isOpen: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDarkMode: Boolean
) {
    if (!isOpen) return

    val dialogBg = if (isDarkMode) Color(0xFF242426) else Color.White
    val textColor = if (isDarkMode) Color.White else Color(0xFF111111)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "تصفير جميع العلامات؟",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = textColor,
                textAlign = TextAlign.End
            )
        },
        text = {
            Text(
                text = "هل أنت متأكد من رغبتك في مسح كافة الدرجات المدخلة وإعادة الحقول للصفر؟",
                fontFamily = CairoFontFamily,
                fontSize = 14.sp,
                color = if (isDarkMode) Color(0xFFAAAAAA) else Color(0xFF555555),
                textAlign = TextAlign.End
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color(0xFFC62828)
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC62828),
                    contentColor = Color.White
                ),
                modifier = Modifier.testTag("confirm_clear_button")
            ) {
                Text("نعم، مسح الكل", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("إلغاء", fontFamily = CairoFontFamily, color = textColor)
            }
        },
        containerColor = dialogBg,
        shape = RoundedCornerShape(20.dp)
    )
}
