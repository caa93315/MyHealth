package com.example.health;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class DeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device); // 关联布局

        // 消息提醒开关
        @SuppressLint("WrongViewCast") SwitchCompat switchNotification = findViewById(R.id.switch_notification);
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "消息提醒已开启", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "消息提醒已关闭", Toast.LENGTH_SHORT).show();
            }
        });

        // 心率监测开关
        @SuppressLint("WrongViewCast") SwitchCompat switchHeartRate = findViewById(R.id.switch_heart_rate);
        switchHeartRate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "心率监测已开启", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "心率监测已关闭", Toast.LENGTH_SHORT).show();
            }
        });

        // 久坐提醒开关
        @SuppressLint("WrongViewCast") SwitchCompat switchSitting = findViewById(R.id.switch_sitting_reminder);
        switchSitting.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "久坐提醒已开启", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "久坐提醒已关闭", Toast.LENGTH_SHORT).show();
            }
        });

        // 夜间模式开关
        @SuppressLint("WrongViewCast") SwitchCompat switchNight = findViewById(R.id.switch_night_mode);
        switchNight.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "夜间模式已开启", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "夜间模式已关闭", Toast.LENGTH_SHORT).show();
            }
        });
    }
}