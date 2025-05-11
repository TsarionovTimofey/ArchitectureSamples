package com.common.extensions.recycler

import android.content.Context
import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InitialPositionLayoutManager(context: Context) : LinearLayoutManager(context) {
    private var mPendingTargetPos = -1

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        if (mPendingTargetPos != -1 && state.itemCount > 0) {
            /*
            Data is present now, we can set the real scroll position
            */
            scrollToPositionWithOffset(mPendingTargetPos, 0)
            mPendingTargetPos = -1
        }
        super.onLayoutChildren(recycler, state)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        /*
        May be needed depending on your implementation.

        Ignore target start position if InstanceState is available (page existed before already, keep position that user scrolled to)
         */
        mPendingTargetPos = -1
        super.onRestoreInstanceState(state)
    }

    /**
     * Sets a start position that will be used **as soon as data is available**.
     * May be used if your Adapter starts with itemCount=0 (async data loading) but you need to
     * set the start position already at this time. As soon as itemCount > 0,
     * it will set the scrollPosition, so that given itemPosition is visible.
     * @param position
     */
    fun setInitialPos(position: Int) {
        mPendingTargetPos = position
    }
}
