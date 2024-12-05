package com.arialyy.simple2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;

public class MainActivity extends PermissionActivity {

    private static final String TAG = "_MainActivity";
    private static final String DOWNLOAD_PATH = "/sdcard/gs3d/xxxx.apk";

    public static final String DOWNLOAD_URL =
            "https://cdn.llscdn.com/yy/files/tkzpx40x-lls-LLS-5.7-785-20171108-111118.apk";
    private long taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Aria.download(this).register();
    }

    @Override
    protected void onResulttPermissionsFail(String what) {
        Log.d(TAG, "onResulttPermissionsFail: ");
    }

    @Override
    public void init() {
        Log.d(TAG, "init: ");
    }

    @Override
    public void initEnv() {
        Log.d(TAG, "initEnv: ");
    }

    public void goStart(View view) {
        //创建并启动下载
        taskId = Aria.download(this)
                .load(DOWNLOAD_URL)     //读取下载地址
                .setFilePath(DOWNLOAD_PATH) //设置文件保存的完整路径
                .create();

        Log.d(TAG, "goStart: taskId=" + taskId);
    }

    public void goStop(View view) {
        Aria.download(this)
                .load(taskId)     //读取任务id
                .stop();       // 停止任务
        //.resume();
    }

    public void goResume(View view) {
        Aria.download(this)
                .load(taskId)     //读取任务id
                .resume();       // 恢复任务
        //.resume();
    }

    public void goCancel(View view) {
        Aria.download(this)
                .load(taskId)     //读取任务id
                .cancel();       // 恢复任务
    }


    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        Log.d(TAG, "running: " + task.getEntity().rowID);
        int p = task.getPercent();    //任务进度百分比
        String speed = task.getConvertSpeed();    //转换单位后的下载速度，单位转换需要在配置文件中打开
        long speed1 = task.getSpeed(); //原始byte长度速度
        Log.d(TAG, "running: p=" + p + " speed1=" + speed1);
    }

    @Download.onTaskCancel
    void taskCancel(DownloadTask task) {
        //在这里处理任务完成的状态
        Log.d(TAG, "taskCancel: " + task.getEntity().rowID);
    }

    @Download.onTaskFail
    void taskFail(DownloadTask task) {
        //在这里处理任务完成的状态
        Log.d(TAG, "taskFail: " + task.getEntity().rowID);
    }

    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        //在这里处理任务完成的状态
        Log.d(TAG, "taskComplete: " + task.getEntity().rowID);
    }
}