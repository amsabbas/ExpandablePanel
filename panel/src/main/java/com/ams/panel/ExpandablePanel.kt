package com.ams.panel

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.Transformation
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.ViewCompat

class ExpandablePanel @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs), View.OnClickListener {

    private val mainViewId: Int
    private val expandedViewId: Int
    private var expandedResId = 0
    private var collapseResId = 0
    private var isExpanded = false
    private var animationDuration = 0

    private var mainView: View? = null
    private var expandedView: View? = null

    private var expandedViewHeight = 0
    private var mainViewHeight = 0

    private var mListener: OnExpandListener? = null

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.panel, 0, 0)
        animationDuration =
            attr.getInteger(
                R.styleable.panel_animation_duration,
                DEFAULT_ANIM_DURATION
            )
        expandedResId = attr.getResourceId(R.styleable.panel_more, 0)
        collapseResId = attr.getResourceId(R.styleable.panel_less, 0)

        val mainVId = attr.getResourceId(R.styleable.panel_main_view, 0)
        require(mainVId != 0) { "You must add the main view" }

        val expandedVId = attr.getResourceId(R.styleable.panel_expanded_view, 0)
        require(expandedVId != 0) { "You must add the expanded view" }

        mainViewId = mainVId
        expandedViewId = expandedVId

        attr.recycle()
    }

    fun setOnExpandListener(listener: OnExpandListener) {
        mListener = listener
    }

    fun isExpanded(): Boolean {
        return isExpanded
    }

    fun setAnimationDuration(animationDuration: Int) {
        this.animationDuration = animationDuration
    }

    fun setExpanded(expand: Boolean) {
        if (isExpanded == expand) return
        Handler().post {
            mainView?.callOnClick()
        }
    }

    private fun setExpandedResource(){
        if (mainView !is TextView) {
            expandedResId = 0
            collapseResId = 0
        } else {
            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR)
                (mainView as TextView).setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, expandedResId, 0
                )
            else
                (mainView as TextView).setCompoundDrawablesWithIntrinsicBounds(
                    expandedResId, 0, 0, 0
                )
        }
    }

    private fun setCollapseResource()
    {
        if (ViewCompat.getLayoutDirection(this@ExpandablePanel) == ViewCompat.LAYOUT_DIRECTION_LTR)
            (mainView as TextView).setCompoundDrawablesWithIntrinsicBounds(
                0, 0, collapseResId, 0
            )
        else
            (mainView as TextView).setCompoundDrawablesWithIntrinsicBounds(
                collapseResId, 0, 0, 0
            )
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        mainView = findViewById(mainViewId)
        requireNotNull(mainView) { "You must add the main view" }

        expandedView = findViewById(expandedViewId)
        requireNotNull(expandedView) { "You must add the expanded view" }

        mainView?.setOnClickListener(this)

        val lp = expandedView?.layoutParams
        lp?.height = 1
        expandedView?.layoutParams = lp
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //measure how high content wants to be
        expandedView?.measure(widthMeasureSpec, MeasureSpec.UNSPECIFIED)
        expandedViewHeight = expandedView?.measuredHeight ?: 0

        if (mainViewHeight == 0) {
            mainView?.measure(widthMeasureSpec, MeasureSpec.UNSPECIFIED)
            mainViewHeight = mainView?.measuredHeight ?: 0
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setExpandedResource()
    }

    override fun onClick(v: View?) {
        val animation: Animation = if (isExpanded) {
            ExpandAnimation(expandedViewHeight, 1)
        } else {
            ExpandAnimation(1, expandedViewHeight)
        }
        animation.duration = animationDuration.toLong()
        expandedView?.startAnimation(animation)
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                if (isExpanded) {
                    if (expandedResId != 0) {
                      setExpandedResource()
                    }
                    mListener?.onExpandToggle(false, mainView, expandedView)
                } else {
                    if (collapseResId != 0) {
                        setCollapseResource()
                    }
                    mListener?.onExpandToggle(true, mainView, expandedView)
                }
                isExpanded = !isExpanded
            }
        })
    }

    private inner class ExpandAnimation(private val mStartHeight: Int, endHeight: Int) :
        Animation() {
        private val mDeltaHeight: Int = endHeight - mStartHeight

        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            val lp = expandedView?.layoutParams
            lp?.height = (mStartHeight + mDeltaHeight * interpolatedTime).toInt()
            expandedView?.layoutParams = lp
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    companion object {
        private const val DEFAULT_ANIM_DURATION = 200
    }
}