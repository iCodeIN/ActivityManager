package com.sdex.activityrunner.db.cache

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = ApplicationModel.TABLE)
data class ApplicationModel(
    @PrimaryKey val packageName: String,
    val name: String?,
    val activitiesCount: Int,
    val exportedActivitiesCount: Int,
    val system: Boolean,
) : Serializable {

    companion object {

        const val TABLE = "ApplicationModel"
        const val NAME = "name"
        const val PACKAGE_NAME = "packageName"
        const val ACTIVITIES_COUNT = "activitiesCount"
        const val EXPORTED_ACTIVITIES_COUNT = "exportedActivitiesCount"
    }
}
