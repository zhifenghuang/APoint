package com.blokbase.pos;

import android.app.ActivityManager;
import android.content.Context;
import android.os.StrictMode;

import androidx.multidex.MultiDexApplication;

import com.common.lib.manager.ConfigurationManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;

public class BaseApplication extends MultiDexApplication {


    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.color_bg_theme, R.color.color_16_16_16);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        int pid = android.os.Process.myPid();
        int count = 0;
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
            if (runningAppProcessInfo.pid == pid) {
                if (runningAppProcessInfo.processName.equals("com.blokbase.pos")) {
                    ConfigurationManager.Companion.getInstance().setContext(this);
                    return;
                }
                ++count;
            }
        }
        if (count > 1) {
            return;
        }
        ConfigurationManager.Companion.getInstance().setContext(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        CrashReport.initCrashReport(getApplicationContext(), "a251403d70", false);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
