package wkq.com.test;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 创建：wukuiqing
 * <p>
 * 时间：2019/3/31
 * <p>
 * 描述：
 */
public class LocationService extends Service {
    private static final String TAG = "DaemonService";
    public static final int NOTICE_ID = 100;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "DaemonService---->onCreate被调用，启动前台service");
        Timer timer = new Timer();
        //适配8.0service
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(NOTICE_ID + "", "定位", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            Notification notification = new Notification.Builder(getApplicationContext(), NOTICE_ID + "").build();
            startForeground(1, notification);
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                Date date = new Date();
                date.setTime(time);

                DateFormat df2 = new SimpleDateFormat("yyyy-MM-01 hh:mm:ss EE");
                Log.e(TAG, df2.format(date) + "");
                readFile(df2.format(date) + "");
            }
        }, 0, 10000);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 如果Service被终止
        // 当资源允许情况下，重启service
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 如果Service被杀死，干掉通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            NotificationManager mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mManager.cancel(NOTICE_ID);
        }
        // 重启自己
        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        startService(intent);
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
