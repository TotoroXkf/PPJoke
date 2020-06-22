package com.xkf.ppjoke.exoplayer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView


class PageListPlayDetector(
    owner: LifecycleOwner,
    val recyclerView: RecyclerView
) {
    private val targets = arrayListOf<IPlayTarget>()
    private var playingTarget: IPlayTarget? = null
    private lateinit var recyclerViewLocation: Pair<Int, Int>

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (dx == 0 && dy == 0) {
                postAutoPlay()
            } else if (playingTarget != null &&
                playingTarget!!.isPlaying() &&
                !isTargetInBounds(playingTarget!!)
            ) {
                playingTarget!!.inActive()
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                autoPlay()
            }
        }
    }

    private val delayAutoPlay = Runnable { autoPlay() }

    private val dataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)

            postAutoPlay()
        }
    }

    init {
        owner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    playingTarget = null
                    targets.clear()
                    recyclerView.removeCallbacks(delayAutoPlay)
                    recyclerView.removeOnScrollListener(scrollListener)
                    owner.lifecycle.removeObserver(this)
                }
            }
        })
        recyclerView.adapter?.registerAdapterDataObserver(dataObserver)
        recyclerView.addOnScrollListener(scrollListener)
    }


    fun addTarget(target: IPlayTarget) {
        targets.add(target)
    }

    fun removeTarget(target: IPlayTarget) {
        targets.remove(target)
    }

    private fun postAutoPlay() {
        recyclerView.post(delayAutoPlay)
    }

    private fun autoPlay() {
        if (targets.size <= 0 || recyclerView.childCount <= 0) {
            return
        }

        if (playingTarget != null && playingTarget!!.isPlaying() && isTargetInBounds(playingTarget!!)) {
            return
        }

        var activeTarget: IPlayTarget? = null
        for (target in targets) {
            if (isTargetInBounds(target)) {
                activeTarget = target
                break
            }
        }
        if (activeTarget != null) {
            if (playingTarget != null) {
                playingTarget!!.inActive()
            }
            playingTarget = activeTarget
            activeTarget.onActive()
        }
    }

    private fun isTargetInBounds(target: IPlayTarget): Boolean {
        val viewGroup = target.getOwner()
        ensureRecyclerViewLocation()
        if (!viewGroup.isShown || !viewGroup.isAttachedToWindow) {
            return false
        }
        val location = IntArray(2)
        viewGroup.getLocationOnScreen(location)
        val center = location[1] + viewGroup.height / 2
        if (this::recyclerViewLocation.isInitialized) {
            return recyclerViewLocation.first <= center && center <= recyclerViewLocation.second
        }
        return false
    }

    private fun ensureRecyclerViewLocation() {
        if (!this::recyclerViewLocation.isInitialized) {
            val location = IntArray(2)
            recyclerView.getLocationOnScreen(location)
            recyclerViewLocation = Pair(location[1], location[1] + recyclerView.height)
        }
    }

    fun onPause() {
        playingTarget?.inActive()
    }

    fun onResume() {
        playingTarget?.onActive()
    }
}