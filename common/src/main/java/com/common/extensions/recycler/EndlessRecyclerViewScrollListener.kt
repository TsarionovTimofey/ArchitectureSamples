package com.common.extensions.recycler

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

private const val VISIBLE_THRESHOLD = 5

abstract class EndlessRecyclerViewScrollListener : RecyclerView.OnScrollListener {
    private val layoutManager: RecyclerView.LayoutManager
    private var visibleThreshold = VISIBLE_THRESHOLD
    private var lastRequestedIndex = 0

    constructor(layoutManager: LinearLayoutManager) {
        this.layoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager) {
        this.layoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    constructor(layoutManager: StaggeredGridLayoutManager) {
        this.layoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray) =
        lastVisibleItemPositions.maxByOrNull { it } ?: 0

    private fun getFirstVisibleItem(firstVisiblePositions: IntArray) =
        firstVisiblePositions.minByOrNull { it } ?: 0

    private fun getLastVisibleItem() = when (layoutManager) {
        is StaggeredGridLayoutManager -> {
            val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
            getLastVisibleItem(lastVisibleItemPositions)
        }
        is GridLayoutManager -> {
            layoutManager.findLastVisibleItemPosition()
        }
        is LinearLayoutManager -> {
            layoutManager.findLastVisibleItemPosition()
        }
        else -> TODO()
    }

    private fun getFirstVisibleItem() = when (layoutManager) {
        is StaggeredGridLayoutManager -> {
            val firstVisibleItemPositions = layoutManager.findFirstVisibleItemPositions(null)
            getFirstVisibleItem(firstVisibleItemPositions)
        }
        is GridLayoutManager -> {
            layoutManager.findFirstVisibleItemPosition()
        }
        is LinearLayoutManager -> {
            layoutManager.findFirstVisibleItemPosition()
        }
        else -> TODO()
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        lastRequestedIndex = if (dy < 0) {
            getFirstVisibleItem()
        } else {
            getLastVisibleItem()
        }.also { currentRequestedIndex ->
            if (currentRequestedIndex != lastRequestedIndex) {
                onLoadMore(currentRequestedIndex)
            }
        }
    }

    protected abstract fun onLoadMore(position: Int)
}
