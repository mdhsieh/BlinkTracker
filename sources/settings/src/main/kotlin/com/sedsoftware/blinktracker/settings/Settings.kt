package com.sedsoftware.blinktracker.settings

import kotlinx.coroutines.flow.Flow

interface Settings {
    suspend fun getPerMinuteThreshold(): Flow<Int>
    suspend fun getNotifySoundEnabled(): Flow<Boolean>
    suspend fun getNotifyVibrationEnabled(): Flow<Boolean>
    suspend fun setPerMinuteThreshold(value: Int)
    suspend fun setNotifySoundEnabled(value: Boolean)
    suspend fun setNotifyVibrationEnabled(value: Boolean)
}