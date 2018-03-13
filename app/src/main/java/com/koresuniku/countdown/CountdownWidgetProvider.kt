package com.koresuniku.countdown

import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.app.AlarmManager
import android.os.Build
import android.app.PendingIntent
import java.util.*
import android.widget.RemoteViews

/**
 * Created by koresuniku on 13.03.18.
 */

class CountdownWidgetProvider : AppWidgetProvider() {

    private val ACTION_SCHEDULED_UPDATE = "com.koresuniku.countdown.SCHEDULED_UPDATE"

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)

        val views = RemoteViews(context?.packageName, R.layout.widget_layout)
        views.setTextViewText(R.id.timer, Counter.countDaysLeft().toString())

        val manager = AppWidgetManager.getInstance(context)
        manager.updateAppWidget(ComponentName(context, CountdownWidgetProvider::class.java), views)

        context?.let {
            val ids = manager.getAppWidgetIds(ComponentName(context, CountdownWidgetProvider::class.java))
            onUpdate(it, manager, ids)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_SCHEDULED_UPDATE) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, CountdownWidgetProvider::class.java))
            onUpdate(context, manager, ids)
        }

        super.onReceive(context, intent)
    }


    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)
        views.setTextViewText(R.id.timer, Counter.countDaysLeft().toString())

        appWidgetManager.updateAppWidget(ComponentName(context, CountdownWidgetProvider::class.java), views)

        scheduleNextUpdate(context)

        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun scheduleNextUpdate(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, CountdownWidgetProvider::class.java)
        intent.action = ACTION_SCHEDULED_UPDATE
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val midnight = Calendar.getInstance()
        midnight.set(Calendar.HOUR_OF_DAY, 0)
        midnight.set(Calendar.MINUTE, 0)
        midnight.set(Calendar.SECOND, 1)
        midnight.set(Calendar.MILLISECOND, 0)
        midnight.add(Calendar.DAY_OF_YEAR, 1)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, midnight.timeInMillis, pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, midnight.timeInMillis, pendingIntent)
        }
    }
}