package com.example.fromdeskhelper.data

import android.content.Context
import android.util.Log
import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.exp

private val TAG = "PreferencesManager"

enum class SortOrder { BY_NAME, BY_DATE }
data class FilterPreferences(val sortOrder: SortOrder, val hideComplete: Boolean)
data class FilterUserPreferences(val presentation:Boolean,val loginInit:Boolean)
data class FilterCamera(val MoveX:Float,val MoveY:Float,val ScaleX:Float,val ScaleY:Float)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {
    private val datastore = context.createDataStore("user_preferences")
    val preferencesFlow = datastore.data
        .catch { exeption ->
            if (exeption is IOException) {
                Log.e(TAG, "Ubo un erro en las preferencias de usuaurio", exeption)
                emit(emptyPreferences())
            } else {
                throw exeption
            }

        }
        .map { preferences ->
            val sortedOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name
            )
            val hideCompleted = preferences[PreferencesKeys.HIDE_COMPLETED] ?: false
            FilterPreferences(sortedOrder, hideCompleted)
        }

    val preferencesFlowCamera = datastore.data
        .catch { exeption ->
            if (exeption is IOException) {
                Log.e(TAG, "Ubo un erro en las preferencias de Camera", exeption)
                emit(emptyPreferences())
            } else {
                throw exeption
            }

        }
        .map { preferences ->
            val xmove = preferences[PreferencesCamera.CAMERA_MOVE_X] ?: -1f
            val ymove = preferences[PreferencesCamera.CAMERA_MOVE_Y] ?: -1f
            val xscale = preferences[PreferencesCamera.CAMERA_SCALE_X] ?: 1f
            val yscale = preferences[PreferencesCamera.CAMERA_SCALE_Y] ?: 1f
            FilterCamera(xmove,ymove,xscale,yscale)
        }

    val preferencesUserFlow=datastore.data
        .catch { exeption ->
            if (exeption is IOException) {
                Log.e(TAG, "Ubo un erro en las preferencias de usuaurio", exeption)
                emit(emptyPreferences())
            } else {
                throw exeption
            }

        }
        .map { preferences ->
            FilterUserPreferences(
                preferences[PreferencesKeys.LOGIN_PRESENTATION]?:true,
                preferences[PreferencesKeys.LOGIN_USER]?:false)
        }

    suspend fun changePresentation(presentation: Boolean) {
        datastore.edit { preferences ->
            preferences[PreferencesKeys.LOGIN_PRESENTATION] = presentation
        }
    }


    suspend fun changeScalaCamera(x:Float,y:Float) {
        datastore.edit { preferences ->
            preferences[PreferencesCamera.CAMERA_SCALE_X] = x
            preferences[PreferencesCamera.CAMERA_SCALE_Y] = y
        }
    }

    suspend fun changeMoveCamera(x:Float,y:Float) {
        datastore.edit { preferences ->
            preferences[PreferencesCamera.CAMERA_MOVE_X] = x
            preferences[PreferencesCamera.CAMERA_MOVE_Y] = y
        }
    }

    suspend fun updateSortOrder(hideComplete: Boolean) {
        datastore.edit { preferences ->
            preferences[PreferencesKeys.HIDE_COMPLETED] = hideComplete
        }
    }


    private object PreferencesKeys {
        val LOGIN_DATABASE = preferencesKey<Boolean>("bd_connect")
        val LOGIN_USER = preferencesKey<Boolean>("login_user")
        val LOGIN_PRESENTATION = preferencesKey<Boolean>("init_presentation")
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val HIDE_COMPLETED = preferencesKey<Boolean>("hilde_completed")
    }
    private object PreferencesCamera{
        val CAMERA_MOVE_X= preferencesKey<Float>("camera_move_x")
        val CAMERA_MOVE_Y= preferencesKey<Float>("camera_move_y")
        val CAMERA_SCALE_Y= preferencesKey<Float>("camera_scale_y")
        val CAMERA_SCALE_X= preferencesKey<Float>("camera_scale_x")
    }
}