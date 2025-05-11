package com.common.extensions.recycler

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val DEFAULT_THRESHOLD = 6

class OnScrollToEndListener(
    private val layoutManager: LinearLayoutManager,
    private var threshold: Int = DEFAULT_THRESHOLD,
    var listenerEnabled: Boolean = true,
    private val onScrollToEnd: (listener: OnScrollToEndListener) -> Unit
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (listenerEnabled && dy > 0) {
            val totalItemCount = layoutManager.itemCount
            if (totalItemCount > 0) {
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if (isScrolledToEnd(lastVisibleItem, totalItemCount)) {
                    onScrollToEnd(this)
                }
            }
        }
    }

    private fun isScrolledToEnd(
        lastVisibleItem: Int,
        totalItemCount: Int
    ) = lastVisibleItem + threshold >= totalItemCount - 1
}
