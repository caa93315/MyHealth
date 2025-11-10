// 檔案： MainActivity.kt (最終自動抓取版)

// 檔案： MainActivity.kt (UI + 藍牙權限的完整版)

// 1. 檔案最頂部，這是你的包名
package com.example.health

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.math.roundToInt


class MainActivity : ComponentActivity() {
    private val permissions = setOf(
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getWritePermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class)
    )


    private val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()

    @RequiresApi(Build.VERSION_CODES.O)
    private val requestPermissions =
        registerForActivityResult(requestPermissionActivityContract) { grantedPermissions ->
            if (grantedPermissions.containsAll(permissions)) {
                showMessage("權限已獲得，可以存取健康數據")
                CoroutineScope(Dispatchers.Main).launch {
                    readHealthData()
                }
            } else {
                showMessage("權限被拒絕，無法存取健康數據")
            }
            // 只在 API 26+ 執行 Health Connect 相關功能
            when {
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1 -> {
                }
            }

        }

    private lateinit var healthConnectClient: HealthConnectClient

    @RequiresApi(Build.VERSION_CODES.DONUT)
    @SuppressLint("UseKtx", "ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val providerPackageName = "com.google.android.apps.healthdata"
        val availabilityStatus = HealthConnectClient.getSdkStatus(this, providerPackageName)

        when (availabilityStatus) {
            HealthConnectClient.SDK_UNAVAILABLE -> {
                showMessage("Health Connect 未安裝，請先安裝")
                return
            }
            HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> {
                val uriString = "market://details?id=$providerPackageName&url=health connect%3A%2F%2Onboarding"
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    setPackage("com.android.vending")
                    data = Uri.parse(uriString)
                    putExtra("overlay", true)
                    putExtra("callerId", packageName)
                })
                return
            }
            else -> {
                // SDK 可用
                healthConnectClient = HealthConnectClient.getOrCreate(this)
                checkPermissionsAndRun()
            }
        }

        enableEdgeToEdge()
        setContent {
            HealthTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Health Connect",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private fun checkPermissionsAndRun() {
        CoroutineScope(Dispatchers.Main).launch {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            if (granted.containsAll(permissions)) {
                showMessage("權限已獲得，可以存取健康數據")
                readHealthData()
            } else {
                requestPermissions.launch(permissions)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun readHealthData() = try {
        val now = Instant.now()
        val startOfDay = now.atZone(ZoneId.systemDefault())
            .toLocalDate()
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC)

        // 步數
        val stepsResult = healthConnectClient.readRecords(
            ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startOfDay, now)
            )
        )

        // 心率
        val heartRateResult = healthConnectClient.readRecords(
            ReadRecordsRequest(
                recordType = HeartRateRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startOfDay, now)
            )
        )

        val totalSteps = stepsResult.records.sumOf { it.count }

        val avgHeartRate = heartRateResult.records
            .flatMap { it.samples }
            .map { it.beatsPerMinute }
            .takeIf { it.isNotEmpty() }
            ?.average() ?: 0.0

        showMessage("今日步數: $totalSteps, 平均心率: ${avgHeartRate.roundToInt()} BPM")
    } catch (e: Exception) {
        showMessage("讀取健康數據時出錯: ${e.localizedMessage}")
        e.printStackTrace()
    }


    private fun showMessage(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HealthTheme {
        Greeting("Health")
    }
}

@Composable
fun HealthTheme(content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}
// Preview 還是留給你，但它預覽不了複雜狀態
// @Preview(showBackground = true)
// @Composable
// fun GreetingPreview() {
//    HealthTheme {
//        HealthScreen(
//            uiState = UiState.HealthData(1000, 75),
//            onInstallClick = {},
//            onUpdateClick = {},
//            onGrantPermissionClick = {}
//        )
//    }
// }