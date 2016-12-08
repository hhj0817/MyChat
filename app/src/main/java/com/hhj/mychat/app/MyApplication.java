package com.hhj.mychat.app;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hhj.mychat.BuildConfig;
import com.hhj.mychat.R;
import com.hhj.mychat.adapter.EMMessageListenerAdapter;
import com.hhj.mychat.database.DatabaseManager;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;

public class MyApplication extends Application {
    public static final String TAG = "MyApplication";


    private SoundPool mSoundPool;
    private int mDuan;
    private int mYulu;

    /**
     * App有多少个进程，那么onCreate的方法就会执行多少次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        initEaseMob();
        initBmob();
        initSoundPool();
        DatabaseManager.getInstance().initDatabase(getApplicationContext());
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListenerAdapter);
    }

    private void initSoundPool() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        mDuan = mSoundPool.load(getApplicationContext(), R.raw.duan, 1);
        mYulu = mSoundPool.load(getApplicationContext(), R.raw.yulu, 1);
    }

    private void initBmob() {
        Bmob.initialize(this, "6531b81cf4bc2f0653be401896b24488");
    }

    /**
     * 初始化环信SDK
     */
    private void initEaseMob() {
        int pid = android.os.Process.myPid();//拿到当前进程的id
        String processAppName = getAppName(pid);//获取到当前进程的名字
        //默认进程的app的进程名是包名


// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次

        if (processAppName == null ||!processAppName.equalsIgnoreCase(getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }



        EMOptions options = new EMOptions();
// 当接收到好友请求，默认同意添加好友
        options.setAcceptInvitationAlways(true);
//初始化 只在默认进程初始化一次
        EMClient.getInstance().init(getApplicationContext(), options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        if (BuildConfig.DEBUG) {
            EMClient.getInstance().setDebugMode(true);
        }
    }


    /**
     * 根据当前进程的pID，获取进程名
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();//获取到正在运行app的进程
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            //遍历所有进程的信息
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private EMMessageListenerAdapter mEMMessageListenerAdapter = new EMMessageListenerAdapter() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //判断app如果在后台，弹出Notification.
            if (isForeground()) {
                //播放短的音频
                mSoundPool.play(mDuan, 1, 1, 0, 0, 1);
            } else {
                //播放长的音频
                mSoundPool.play(mYulu, 1, 1, 0, 0, 1);
                //弹出Notification
                showNotification(list.get(0));
            }
        }
    };

    private void showNotification(EMMessage emMessage) {
        Notification.Builder builder = new Notification.Builder(getApplicationContext());

        String content = "";
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            content = ((EMTextMessageBody) body).getMessage();
        } else {
            content = getString(R.string.no_text_message);
        }

        Notification notification = builder.setContentTitle(getString(R.string.receive_new_message))
                .setContentText(content)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.avatar1))
                .setSmallIcon(R.mipmap.contact_selected)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }


    public boolean isForeground() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for (int i = 0; i < runningAppProcesses.size(); i++) {
            ActivityManager.RunningAppProcessInfo info = runningAppProcesses.get(i);
            if (info.processName.equals(getPackageName()) && info.importance == IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
