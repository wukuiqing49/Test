package wkq.com.test;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import wkq.com.test.databinding.ActivityMainBinding;

/**
 * 创建：wukuiqing
 * <p>
 * 时间：2019/3/31
 * <p>
 * 描述：
 */
public class TestActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.ignoreBatteryOptimization(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.tvStart.setOnClickListener(view -> {

            startService();
        });

    }

    //开启服务
    private void startService() {
        Intent intent = new Intent(this, LocationService.class);
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

    }
}
