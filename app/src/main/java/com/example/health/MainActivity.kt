package com.example.health

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity(), BluetoothManager.OnDataReceivedListener {

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化藍牙
        bluetoothManager = BluetoothManager(this)
        bluetoothManager.dataListener = this

        // 初始化 ViewPager2
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(this)
    }

    override fun onDataReceived(data: ByteArray) {
        // 收到數據後的處理
    }

    override fun onStatusChanged(isConnected: Boolean) {
        runOnUiThread {
            val status = if (isConnected) "設備已連線" else "設備已斷開"
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }
    }
}