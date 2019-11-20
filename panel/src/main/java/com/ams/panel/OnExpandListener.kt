package com.ams.panel

import android.view.View

interface OnExpandListener {
    fun onExpandToggle(isExpand: Boolean, expandedView: View?, mainView: View?)
}