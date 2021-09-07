package com.hh.composeplayer.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.hh.composeplayer.HhCpApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.hellocompose.util
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/23  9:41
 */

val SP_CONFIG by lazy{"setting"}

val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = SP_CONFIG)
object DataStorePreference {
     suspend fun getValue(key: String,default:Any): Any {
         return  HhCpApp.context.dataStore.data.map {
              when (default) {
                 is Long ->  it[longPreferencesKey(key)]?:default
                 is String -> it[stringPreferencesKey(key)]?:default
                 is Float -> it[floatPreferencesKey(key)]?:default
                 is Int -> it[intPreferencesKey(key)]?:default
                 is Boolean -> it[booleanPreferencesKey(key)]?:default
                  else ->  throw IllegalArgumentException("This type can't be saved into Preferences")
              }
         }.first()
    }

    suspend fun getStringValue(key: String,default: String = ""): String {
        return  HhCpApp.context.dataStore.data.map {
                it[stringPreferencesKey(key)]?:default
        }.first()
    }

    suspend fun getLongValue(key: String,default: Long = 0L): Long {
        return  HhCpApp.context.dataStore.data.map {
                 it[longPreferencesKey(key)]?:default
        }.first()
    }
    suspend fun getIntValue(key: String,default: Int = 0): Int {
        return  HhCpApp.context.dataStore.data.map {
                it[intPreferencesKey(key)]?:default
        }.first()
    }
    suspend fun getFloatValue(key: String,default: Float = 0f): Float {
        return  HhCpApp.context.dataStore.data.map {
                it[floatPreferencesKey(key)]?:default
        }.first()
    }

    suspend fun getBooleanValue(key: String,default: Boolean = false): Boolean {
        return  HhCpApp.context.dataStore.data.map {
                it[booleanPreferencesKey(key)]?:default
        }.first()
    }

    suspend fun setValue(key: String, value: Any) {
        HhCpApp.context.dataStore.edit { settings ->
            when (value) {
                is Long -> settings[longPreferencesKey(key)] = value
                is String -> settings[stringPreferencesKey(key)] = value
                is Float -> settings[floatPreferencesKey(key)] = value
                is Int -> settings[intPreferencesKey(key)] = value
                is Boolean ->settings[booleanPreferencesKey(key)] = value
                else -> {
                    throw IllegalArgumentException("This type can't be saved into Preferences")
                }
            }
        }
    }
}