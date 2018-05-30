package com.sdex.activityrunner.intent

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LaunchParamsExtra(val key: String,
                             val value: String,
                             val type: Int,
                             val isArray: Boolean) : Parcelable