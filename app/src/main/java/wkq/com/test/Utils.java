package wkq.com.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static android.content.Context.POWER_SERVICE;

/**
 * 创建：wukuiqing
 * <p>
 * 时间：2019/3/31
 * <p>
 * 描述：
 */
public class Utils {

    public static boolean isIgnoringBatteryOptimizations(Context conxtext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = conxtext.getPackageName();
            PowerManager pm = (PowerManager) conxtext.getSystemService(POWER_SERVICE);
            boolean is = pm.isIgnoringBatteryOptimizations(packageName);
            return is;
        }
        return false;
    }


    private final static int REQUEST_IGNORE_BATTERY_CODE = 1001;

    public static void gotoSettingIgnoringBatteryOptimizations(Context conxtext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Intent intent = new Intent();
                String packageName = conxtext.getPackageName();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                conxtext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void ignoreBatteryOptimization(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            PowerManager powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);
            boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
            //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
            if (!hasIgnored) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivity(intent);
            }
        }
    }

    public static void readFile(String content) {
        try {
            String filePath = null;
            boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            if (hasSDCard) { // SD卡根目录的hello.text
                filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "定位测试服务.txt";
            } else { // 系统下载缓存根目录的hello.text
                filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "定位测试服务.txt";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(filePath, "rw");

            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);

            randomFile.writeBytes(content);

            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
