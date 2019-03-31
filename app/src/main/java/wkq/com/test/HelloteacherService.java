package wkq.com.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 创建：wukuiqing
 * <p>
 * 时间：2019/3/31
 * <p>
 * 描述：
 */
public class HelloteacherService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
