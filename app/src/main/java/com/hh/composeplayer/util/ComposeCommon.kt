package com.hh.composeplayer.util

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyGridScope
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.hellocompose.util
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/25  9:17
 */
fun Modifier.tabIndicatorOffsetH(
    currentTabPosition: TabPosition,
    width: Dp = 1.dp
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = width,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = currentTabPosition.left,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset + ((currentTabPosition.width - currentTabWidth) / 2))
        .width(currentTabWidth)
}

@ExperimentalFoundationApi
inline fun <T> LazyGridScope.itemsH(
    items: List<T>,
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = items(items.size) {
    if(items.isNotEmpty()){
        itemContent(items[it])
    }
}

@ExperimentalPagerApi
fun Modifier.pagerTabIndicatorOffsetH(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
    width: Dp = 1.dp
): Modifier = composed {
    // If there are no pages, nothing to show
    if (pagerState.pageCount == 0) return@composed this

    val targetIndicatorOffset: Dp
    val indicatorWidth: Dp

    val currentTab = tabPositions[pagerState.currentPage]
    val targetPage = pagerState.targetPage
    val targetTab = targetPage.let { tabPositions.getOrNull(it) }

    if (targetTab != null) {
        // The distance between the target and current page. If the pager is animating over many
        // items this could be > 1
        val targetDistance = (targetPage - pagerState.currentPage).absoluteValue
        // Our normalized fraction over the target distance
        val fraction = (pagerState.currentPageOffset / kotlin.math.max(targetDistance, 1)).absoluteValue

        targetIndicatorOffset = lerp(currentTab.left, targetTab.left, fraction)
        indicatorWidth = lerp(currentTab.width, targetTab.width, fraction).absoluteValue
    } else {
        // Otherwise we just use the current tab/page
        targetIndicatorOffset = currentTab.left
        indicatorWidth = currentTab.width
    }

    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = targetIndicatorOffset + ((currentTab.width - width) / 2))
        .width(width)
}

private inline val Dp.absoluteValue: Dp
    get() = value.absoluteValue.dp


fun Modifier.ownTabIndicatorOffset(
    currentTabPosition: TabPosition,
    currentTabWidth: Dp = currentTabPosition.width
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val indicatorOffset by animateDpAsState(
        targetValue = currentTabPosition.left,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset + ((currentTabPosition.width - currentTabWidth) / 2))
        .width(currentTabWidth)
}

fun Modifier.percentOffsetX(percent: Float) = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.width, placeable.height) {
        placeable.placeRelative(IntOffset((placeable.width * percent).roundToInt(), 0))
    }
}

@Stable
class QureytoImageShapes(var hudu: Float = 100f) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(0f, size.height-hudu)
        path.quadraticBezierTo(size.width/2f, size.height, size.width, size.height-hudu)
        path.lineTo(size.width,0f)
        path.close()
        return Outline.Generic(path)
    }
}



