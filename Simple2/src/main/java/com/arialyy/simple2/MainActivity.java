package com.arialyy.simple2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.AbsEntity;
import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.task.DownloadTask;

import java.util.List;

public class MainActivity extends PermissionActivity {

    private static final String TAG = "_MainActivity";
    private static final String DOWNLOAD_PATH = "/sdcard/gs3d/xxxx.apk";

    public static final String DOWNLOAD_URL =
            "https://cdn.llscdn.com/yy/files/tkzpx40x-lls-LLS-5.7-785-20171108-111118.apk";
    private long taskId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Aria.download(this).register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Aria.download(this).unRegister();
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


    public void getAllTaskList(View view) {
        Log.d(TAG, "getAllTaskList: ");
        // 所有任务列表
        List<AbsEntity> listAll = Aria.download(this).getTotalTaskList();
        // 普通任务列表
        List<DownloadEntity> list = Aria.download(this).getTaskList();

        if (listAll != null) {
            for (AbsEntity entity : listAll) {
                long rowID = entity.rowID;
                Log.d(TAG, "getAllTaskList: id=" + rowID + "  " + entity.toString());
            }
        }

        if (list != null) {
            for (DownloadEntity entity : list) {
                long rowID = entity.rowID;
                Log.d(TAG, "getAllTaskList2 : id=" + rowID + "  " + entity.toString());
            }
        }
    }


    public void resumeAllTask(View view) {
        Log.d(TAG, "resumeAllTask: ");
        Aria.download(this).resumeAllTask();
    }
    public void stopAllTask(View view) {
        Log.d(TAG, "stopAllTask: ");
        Aria.download(this).stopAllTask();
    }

    public void removeAllTask(View view) {
        Log.d(TAG, "removeAllTask: ");
        Aria.download(this).removeAllTask(false);
    }

    public void goStart(View view) {
        //创建并启动下载
        taskId = Aria.download(this)
                .load(DOWNLOAD_URL)     //读取下载地址
                .setFilePath(DOWNLOAD_PATH) //设置文件保存的完整路径
//                .add()
                .create();

        Log.d(TAG, "goStart: taskId=" + taskId);
    }

    public void goStop(View view) {
        Aria.download(this)
                .load(taskId)     //读取任务id
                .stop();       // 停止任务
        //.resume();
        Log.d(TAG, "goStop: ");

    }

    public void goResume(View view) {
        Aria.download(this)
                .load(taskId)     //读取任务id
                .resume();       // 恢复任务
        //.resume();
        Log.d(TAG, "goResume: ");
    }

    public void goCancel(View view) {
        Aria.download(this)
                .load(taskId)     //读取任务id
                .cancel();       // 恢复任务
        Log.d(TAG, "goCancel: ");
    }

    // 1
    @Download.onWait
    void onWait(DownloadTask task) {
        Log.d(TAG, "wait ==> " + task.getDownloadEntity().getFileName());
    }

    // 2
    @Download.onPre
    protected void onPre(DownloadTask task) {
        Log.d(TAG, "onPre");
    }


    // 3
    @Download.onTaskStart
    void taskStart(DownloadTask task) {
        Log.d(TAG, "onStart");
    }

    // 4
    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        Log.d(TAG, "running: " + task.getEntity().rowID);
        int p = task.getPercent();    //任务进度百分比
        String speed = task.getConvertSpeed();    //转换单位后的下载速度，单位转换需要在配置文件中打开
        long speed1 = task.getSpeed(); //原始byte长度速度
        Log.d(TAG, "running: p=" + p + " speed1=" + speed1);
    }

    // 5
    @Download.onTaskResume
    void taskResume(DownloadTask task) {
        Log.d(TAG, "resume");
    }

    // 6
    @Download.onTaskStop
    void taskStop(DownloadTask task) {
        Log.d(TAG, "stop");
    }

    // 7
    @Download.onTaskCancel
    void taskCancel(DownloadTask task) {
        //在这里处理任务完成的状态
        Log.d(TAG, "taskCancel: " + task.getEntity().rowID);
    }

    // 8
    @Download.onTaskFail
    void taskFail(DownloadTask task) {
        //在这里处理任务完成的状态
        Log.d(TAG, "taskFail: " + task.getEntity().rowID);
    }

    // 9
    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        //在这里处理任务完成的状态
        Log.d(TAG, "taskComplete: " + task.getEntity().rowID);
    }

    public void goTaskState(View view) {
        int taskState1 = Aria.download(this).load(taskId).getTaskState();
        int taskState2 = Aria.download(this).load(DOWNLOAD_URL).getEntity().getState();
        Log.d(TAG, "goTaskState: taskState1="+taskState1 + " taskState2="+taskState2);

        // 重置状态 状态重置之后，任务将重新开始执行
        //Aria.download(this).load(taskId).resetState();

        // 修改文件保存信息
        String newPath = "/sdard/gs3d/tmp.apk";
        Aria.download(this).load(taskId).getEntity().setFilePath(newPath);

        /**
         * 删除任务记录
         *
         * @param type 需要删除的任务类型，1、表示单任务下载。2、表示任务组下载。3、单任务上传
         * @param key 下载为保存路径、任务组为任务组名、上传为上传文件路径
         */
//        Aria.get(this).delRecord(type, key,true);
        // 删除下载记录
        Aria.download(this).load(taskId).removeRecord();
    }
}