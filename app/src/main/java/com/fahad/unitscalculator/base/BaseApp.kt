package com.fahad.unitscalculator.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.fahad.common_data.utils.CurrentActivityProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApp : Application(),
    Application.ActivityLifecycleCallbacks,
    CurrentActivityProvider {

    private lateinit var currentActivity: Activity

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
    override fun get(): Activity = currentActivity
}