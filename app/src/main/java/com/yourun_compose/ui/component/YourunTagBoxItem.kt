package com.yourun_compose.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourun_compose.ui.theme.BtnSelected
import com.yourun_compose.ui.theme.GrayBorder
import com.yourun_compose.ui.theme.SelectedBtnBorder
import com.yourun_compose.ui.theme.TextGray3

/**
 * 선택 가능한 태그를 표시하는 공통 컴포넌트
 *
 * @param text 태그에 표시될 텍스트
 * @param isSelected 태그의 선택 여부
 * @param onClick 태그를 클릭했을 때 호출될 람다
 * @param modifier 외부에서 전달될 Modifier
 */
@Composable
fun YourunTagBoxItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) BtnSelected else Color.White
    val contentColor = if (isSelected) Color.Black else TextGray3
    val borderColor = if (isSelected) SelectedBtnBorder else GrayBorder

    Box(
        modifier = modifier
            .height(50.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
