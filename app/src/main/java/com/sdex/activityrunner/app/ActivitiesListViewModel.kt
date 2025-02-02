package com.sdex.activityrunner.app

import android.app.Application
import android.content.pm.ActivityInfo
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sdex.activityrunner.preferences.AppPreferences
import com.sdex.commons.pm.getPackageInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class ActivitiesListViewModel(application: Application) : AndroidViewModel(application) {

    private val packageManager = application.packageManager
    private val appPreferences by lazy { AppPreferences(application) }

    private val liveData = MutableLiveData<List<ActivityModel>>()
    private var list: List<ActivityModel>? = null

    fun getItems(packageName: String): LiveData<List<ActivityModel>> {
        viewModelScope.launch(Dispatchers.IO) {
            list = getActivitiesList(packageName)
            liveData.postValue(list)
        }
        return liveData
    }

    fun reloadItems(packageName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            list = getActivitiesList(packageName)
            liveData.postValue(list)
        }
    }

    fun filterItems(packageName: String, searchText: String?) {
        if (list != null) {
            if (searchText != null) {
                viewModelScope.launch(Dispatchers.IO) {
                    val filteredList = list!!.filter {
                        it.name.contains(searchText, true) || it.className.contains(
                            searchText,
                            true
                        )
                    }
                    liveData.postValue(filteredList)
                }
            } else {
                liveData.value = list
            }
        } else {
            getItems(packageName)
        }
    }

    @WorkerThread
    private fun getActivitiesList(packageName: String): List<ActivityModel> {
        val showNotExported = appPreferences.showNotExported
        try {
            return getPackageInfo(packageManager, packageName).activities
                .map { getActivityModel(it) }
                .filter { it.exported || showNotExported }
                .sortedBy { it.name }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return emptyList()
    }

    private fun getActivityModel(activityInfo: ActivityInfo): ActivityModel {
        return ActivityModel(
            activityInfo.name.split(".").last(),
            activityInfo.packageName, activityInfo.name,
            activityInfo.exported && activityInfo.isEnabled
        )
    }
}
