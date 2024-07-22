package com.fahad.common.extensions

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics.DENSITY_DEVICE_STABLE
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fahad.common.R
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.util.*

fun Context.getActivity(): AppCompatActivity? = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return ContextCompat.getColor(this, typedValue.resourceId)
}

@StyleRes
fun Context.getStyleFromAttr(
    @AttrRes style: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(style, typedValue, resolveRefs)
    return typedValue.data
}

fun Context.getVersionName(): String {
    return try {
        val pi = packageManager.getPackageInfo(this.packageName, 0)

        // if (BuildConfig.DEBUG) {
        if (true) {
            // versionCode from build.gradle (launcher) ie bitrise build eg: 2929
            // getBuildNumber().toString()
            (pi.versionName.removeBuildNumber() ?: pi.versionName)
        } else {
            // versionName from build.gradle (launcher) eg: 1.0.0.2929
            (pi.versionName.removeBuildNumber() ?: pi.versionName)
        }
    } catch (e: Exception) {
        ""
    }
}

fun Context.getBuildNumber(): Long {
    val pi = packageManager.getPackageInfo(this.packageName, 0)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        pi.longVersionCode
    } else {
        pi.versionCode.toLong()
    }
}

private fun String.removeBuildNumber(): String? {
    return """^(\d+\.)?(\d+\.)?(\*|\d+)""".toRegex()
        .find(this)?.value
}

fun Context.isPackageNameInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Context.openAppSystemSettings() {
    startActivity(
        Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        }
    )
}

fun Context.getStringFromName(id: String) =
    try {
        resources.getString(resources.getIdentifier(id, "string", packageName))
    } catch (exception: Exception) {
        ""
    }

fun Context.getStringFromNameLocalEng(id: String) = try {
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(Locale.ENGLISH)

    val localizedContext = createConfigurationContext(configuration)
    val resourcesWithLocale = localizedContext.resources

    resourcesWithLocale.getString(resources.getIdentifier(id, "string", packageName))
} catch (exception: Exception) {
    ""
}

fun Context.getResourceIdFromName(id: String) =
    try {
        resources.getIdentifier(id, "drawable", packageName)
    } catch (exception: Exception) {
        0
    }

fun Context.isRtlEnabled() =
    TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL

@RequiresApi(Build.VERSION_CODES.O)
fun Context.arabicFormatterOf(pattern: String): DateTimeFormatter =
    DateTimeFormatter.ofPattern(pattern).withDecimalStyle(
        DecimalStyle.of(Locale("ar"))
    )

fun Context.widthRequiresAccessibilityChanges(): Boolean {
    val configuration: Configuration = resources.configuration

    val metrics = resources.displayMetrics
    val currentDeviceDensity = metrics.densityDpi
    val displayDensityScaleFactor = currentDeviceDensity.toFloat() / DENSITY_DEVICE_STABLE

    return (displayDensityScaleFactor > 1 && configuration.fontScale >= 1) || (displayDensityScaleFactor >= 1 && configuration.fontScale > 1.2)
}

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = getResources().getDimensionPixelSize(resourceId)
    }
    return result
}

fun Context.doesNotHaveLocationPermissions() =
    ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED

fun Context.hasLocationPermissions() = !doesNotHaveLocationPermissions()

fun Context.isDeviceLocationEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ContextCompat.getSystemService(this, LocationManager::class.java)?.isLocationEnabled
            ?: false
    } else {
        val mode = Settings.Secure.getInt(
            contentResolver,
            Settings.Secure.LOCATION_MODE,
            Settings.Secure.LOCATION_MODE_OFF
        )
        mode != Settings.Secure.LOCATION_MODE_OFF
    }
}

fun Context.isBackgroundLocationPermissionGranted() =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    } else {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

fun Context.displayNoConnectivityDialog(clickListener: (() -> Unit)? = null) {
    AlertDialog.Builder(this).apply {
        setMessage(resources.getString(R.string.home_no_connectivity_dialog_message_other))
        setPositiveButton(resources.getString(R.string.home_no_connectivity_dialog_button_text)) { dialog, _ ->
            if (clickListener == null) dialog.cancel() else (clickListener.invoke())
        }
    }.show()
}

fun Context.displayMessageDialog(
    title: String,
    message: String,
    clickListener: (() -> Unit)? = null
) {
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(resources.getString(R.string.home_no_connectivity_dialog_button_text)) { dialog, _ ->
            if (clickListener == null) dialog.cancel() else (clickListener.invoke())
        }
    }.show()
}

fun Context.displayMessageDialog(
    message: String,
    isCancelable: Boolean = true,
    clickListener: (() -> Unit)? = null
) {
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setCancelable(isCancelable)
        setPositiveButton(resources.getString(R.string.home_no_connectivity_dialog_button_text)) { dialog, _ ->
            if (clickListener == null) dialog.cancel() else (clickListener.invoke())
        }
    }.show()
}

fun Context.displayConfirmationMessageDialog(
    message: String,
    positiveButtonText: String = resources.getString(R.string.dialog_button_continue_text),
    negativeButtonText: String = resources.getString(R.string.dialog_button_cancel_text),
    isCancelable: Boolean = true,
    clickListener: (() -> Unit)? = null
) {
    val themedContext: Context = ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog)
    val builder = AlertDialog.Builder(themedContext).apply {
        setMessage(message)
        setCancelable(isCancelable)
        setNegativeButton(negativeButtonText) { dialog, _ ->
            dialog.cancel()
        }
        setPositiveButton(positiveButtonText) { dialog, _ ->
            if (clickListener == null) dialog.cancel() else (clickListener.invoke()).also { dialog.cancel() }
        }
    }

    val alertDialog = builder.create()

    alertDialog.setOnShowListener {
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.let { positiveButton ->
            positiveButton.setTextColor(getColor(android.R.color.black))
            // positiveButton.setBackgroundColor(getColor(android.R.color.black))
        }

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.let { negativeButton ->
            negativeButton.setTextColor(getColor(android.R.color.black))
            // negativeButton.setBackgroundColor(getColor(android.R.color.white))
        }
    }

    alertDialog.show()

}