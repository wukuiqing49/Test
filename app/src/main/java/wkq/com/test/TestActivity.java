package wkq.com.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
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

        //使用兼容库就无需判断系统版本
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //拥有权限，执行操作
            Toast.makeText(this,"已授权",Toast.LENGTH_LONG).show();
        }else{
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10086);
        }

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
