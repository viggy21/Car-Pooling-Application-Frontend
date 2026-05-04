package com.example.carpoolingapplicationfrontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

val AppPrimaryGreen = Color(0xFF16A34A)

@Composable
fun LoginLogo(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(64.dp)
            .shadow(
                elevation = 10.dp,
                shape = CircleShape,
                ambientColor = AppPrimaryGreen.copy(alpha = 0.22f),
                spotColor = AppPrimaryGreen.copy(alpha = 0.28f)
            )
            .background(AppPrimaryGreen, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = LeafIcon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(34.dp)
        )
    }
}

//@Composable
//fun LoginInputField(
//    label: String,
//    value: String,
//    onValueChange: (String) -> Unit,
//    placeholder: String,
//    leadingIcon: ImageVector,
//    enabled: Boolean,
//    keyboardOptions: KeyboardOptions,
//    modifier: Modifier = Modifier,
//    visualTransformation: VisualTransformation = VisualTransformation.None
//) {
//    Text(
//        text = label,
//        style = MaterialTheme.typography.titleMedium,
//        fontWeight = FontWeight.SemiBold,
//        color = MaterialTheme.colorScheme.onSurfaceVariant,
//        modifier = modifier.fillMaxWidth()
//    )
//    TextField(
//        value = value,
//        onValueChange = onValueChange,
//        enabled = enabled,
//        singleLine = true,
//        placeholder = {
//            Text(
//                text = placeholder,
//                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.62f)
//            )
//        },
//        leadingIcon = {
//            Icon(
//                imageVector = leadingIcon,
//                contentDescription = null,
//                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.62f)
//            )
//        },
//        visualTransformation = visualTransformation,
//        keyboardOptions = keyboardOptions,
//        shape = RoundedCornerShape(12.dp),
//        colors = TextFieldDefaults.colors(
//            focusedContainerColor = Color(0xFFF8FAFC),
//            unfocusedContainerColor = Color(0xFFF8FAFC),
//            disabledContainerColor = Color(0xFFF1F5F9),
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.Transparent,
//            disabledIndicatorColor = Color.Transparent,
//            cursorColor = AppPrimaryGreen
//        ),
//        modifier = modifier.fillMaxWidth()
//    )
//}

@Composable
fun LoginInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 360.dp) // 👈 limits horizontal size
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp) // 👈 label spacing
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = true,
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.62f)
                )
            },
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.62f)
                    )
                }
            },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF3F4F6),
                unfocusedContainerColor = Color(0xFFF3F4F6),
                disabledContainerColor = Color(0xFFF1F5F9),

                // remove default underline
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,

                cursorColor = AppPrimaryGreen
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp) // 👈 slightly tighter height
                .border(
                    width = 1.dp,
                    color = Color(0xFFD1D5DB), // 👈 subtle grey border
                    shape = RoundedCornerShape(12.dp)
                )
        )
    }
}

@Composable
fun LoginErrorMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(PaddingValues(horizontal = 14.dp, vertical = 12.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

val EmailIcon: ImageVector = ImageVector.Builder(
    name = "EmailIcon",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(4f, 6.5f)
        horizontalLineTo(20f)
        quadTo(21.5f, 6.5f, 21.5f, 8f)
        verticalLineTo(18f)
        quadTo(21.5f, 19.5f, 20f, 19.5f)
        horizontalLineTo(4f)
        quadTo(2.5f, 19.5f, 2.5f, 18f)
        verticalLineTo(8f)
        quadTo(2.5f, 6.5f, 4f, 6.5f)
        close()
        moveTo(3.5f, 8f)
        lineTo(12f, 13.5f)
        lineTo(20.5f, 8f)
    }
}.build()

val LockIcon: ImageVector = ImageVector.Builder(
    name = "LockIcon",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(6f, 10.5f)
        horizontalLineTo(18f)
        quadTo(19.5f, 10.5f, 19.5f, 12f)
        verticalLineTo(19f)
        quadTo(19.5f, 20.5f, 18f, 20.5f)
        horizontalLineTo(6f)
        quadTo(4.5f, 20.5f, 4.5f, 19f)
        verticalLineTo(12f)
        quadTo(4.5f, 10.5f, 6f, 10.5f)
        close()
        moveTo(8f, 10.5f)
        verticalLineTo(8f)
        quadTo(8f, 4f, 12f, 4f)
        quadTo(16f, 4f, 16f, 8f)
        verticalLineTo(10.5f)
    }
}.build()

val PersonIcon: ImageVector = ImageVector.Builder(
    name = "PersonIcon",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(12f, 12f)
        quadTo(15.5f, 12f, 15.5f, 8.5f)
        quadTo(15.5f, 5f, 12f, 5f)
        quadTo(8.5f, 5f, 8.5f, 8.5f)
        quadTo(8.5f, 12f, 12f, 12f)
        close()
        moveTo(4.5f, 20f)
        quadTo(6.5f, 15.5f, 12f, 15.5f)
        quadTo(17.5f, 15.5f, 19.5f, 20f)
    }
}.build()

val CarIcon: ImageVector = ImageVector.Builder(
    name = "CarIcon",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(4f, 14f)
        lineTo(5.6f, 9.5f)
        quadTo(6.1f, 8f, 7.7f, 8f)
        horizontalLineTo(16.3f)
        quadTo(17.9f, 8f, 18.4f, 9.5f)
        lineTo(20f, 14f)
        verticalLineTo(18f)
        horizontalLineTo(18f)
        verticalLineTo(16.5f)
        horizontalLineTo(6f)
        verticalLineTo(18f)
        horizontalLineTo(4f)
        verticalLineTo(14f)
        close()
        moveTo(6f, 14f)
        horizontalLineTo(8f)
        moveTo(16f, 14f)
        horizontalLineTo(18f)
        moveTo(6f, 12f)
        horizontalLineTo(18f)
    }
}.build()

private val LeafIcon: ImageVector = ImageVector.Builder(
    name = "LeafIcon",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(5f, 19.5f)
        curveTo(5.5f, 13.5f, 9.5f, 8.5f, 15.5f, 8f)
        curveTo(18f, 7.8f, 20f, 5f, 20f, 5f)
        curveTo(22f, 12f, 19.3f, 17.5f, 14f, 18.2f)
        curveTo(10.4f, 18.7f, 7.8f, 17.2f, 6.6f, 14.9f)
        moveTo(6f, 17f)
        curveTo(9f, 15f, 11.3f, 14.2f, 14f, 12.5f)
    }
}.build()
