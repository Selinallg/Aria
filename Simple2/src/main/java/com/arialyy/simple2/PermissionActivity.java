/*
 * *
 *   Created by LG
 *  项目:s3dapi
 *  邮箱：liu.lg@163.com
 *  创建时间：2023年12月21日 16:42:32
 *  修改人：
 *  修改时间：
 *  修改备注：
 *  版权所有违法必究
 *  Copyright(c) 北京凌宇智控科技有限公司 https://www.nolovr.com
 *
 *
 */

package com.arialyy.simple2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.util.ArrayList;
import java.util.List;

public abstract class PermissionActivity extends AppCompatActivity {
    private static String TAG = "_PermissionActivity";
    protected String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //protected String[] permissions = new String[]{Manifest.permission.CAMERA};
    List<String> mPermissionList = new ArrayList<>();
    protected final int mRequestCameraCode = 100;
    protected final int mRequestStorageCode = 11024;
    protected boolean hasCameraPermissionDismiss = false; // camera 权限
    protected boolean hasStoragePermissionDismiss = false; // Storage 权限
    protected boolean hasResult = false; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestStoragePermission();
        requestCameraPermission();
    }


    @Override
    protected void onResume() {
        super.onResume();


        if (hasResult) {
            checkPermissions();
        }
    }


    public void requestStoragePermission() {
        //   Log.e(TAG, Log.getStackTraceString(new Throwable()));
        if (Build.VERSION.SDK_INT >= 30) {
            Log.d(TAG, "requestStoragePermission: Build.VERSION.SDK_INT >= 30 ");
            if (Environment.isExternalStorageManager()) {
                hasStoragePermissionDismiss = true;
                hasResult = true;
                Log.d(TAG, "requestStoragePermission: 有权限");
            } else {
                Intent intent = new Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION");
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                this.startActivityForResult(intent, mRequestStorageCode);
            }
        } else if (Build.VERSION.SDK_INT >= 23) {
            Log.d(TAG, "requestStoragePermission: Build.VERSION.SDK_INT >= 23");
            if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 &&
                    ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                Log.d(TAG, "requestStoragePermission: 有权限");
                hasStoragePermissionDismiss = true;
                hasResult = true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, mRequestStorageCode);
            }
        } else {
            Log.d(TAG, "requestStoragePermission: 有权限");
        }

    }


    public void requestCameraPermission() {
        mPermissionList.clear();
        if (ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            mPermissionList.add(permissions[0]);
            hasCameraPermissionDismiss = true;
            hasResult = true;
        }
        if (!hasCameraPermissionDismiss) {
            ActivityCompat.requestPermissions(this, permissions, mRequestCameraCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        hasResult = true;
        Log.d(TAG, "onRequestPermissionsResult: requestCode" + requestCode + " |permissions.length=" + permissions.length + " |grantResults.length=" + grantResults.length);

        if ((permissions != null) && (permissions.length > 0)) {
            if (mRequestCameraCode == requestCode) {
                if (ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                    hasCameraPermissionDismiss = true;
                    hasResult = true;
                }
            }
        }


        if (requestCode == mRequestStorageCode) {
            if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                hasStoragePermissionDismiss = true;
            } else {
                Log.d(TAG, "onRequestPermissionsResult: 存储权限获取失败");
                //Toast.makeText(this, "存储权限获取失败", Toast.LENGTH_LONG).show();
            }
        }

        Log.d(TAG, "onRequestPermissionsResult: hasCameraPermissionDismiss=" + hasCameraPermissionDismiss + " hasStoragePermissionDismiss=" + hasStoragePermissionDismiss);

        //checkPermissions();
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hasResult = true;
        if (ActivityCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0) {
            hasCameraPermissionDismiss = true;
        }

        if (requestCode == mRequestStorageCode && Build.VERSION.SDK_INT >= 30) {
            if (Environment.isExternalStorageManager()) {
                hasStoragePermissionDismiss = true;
                if (hasCameraPermissionDismiss && hasStoragePermissionDismiss) {
                    init();
                } else if (!hasCameraPermissionDismiss) {


                } else if (!hasStoragePermissionDismiss) {

                } else {
                    //Toast.makeText(this, "缺少权限", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onActivityResult: 缺少权限");
                }

            } else {
                // Toast.makeText(this, "存储权限获取失败", Toast.LENGTH_LONG).show();
                Log.e(TAG, "onActivityResult: 存储权限获取失败");
            }
        }

        // checkPermissions();

    }


    protected void checkPermissions() {
        int cameraResult = ContextCompat.checkSelfPermission(this, permissions[0]);
        Log.d(TAG, "checkPermissions: cameraResult=" + cameraResult);
        if (cameraResult == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: hasCameraPermissionDismiss = true");
            hasCameraPermissionDismiss = true;
        } else {
            Log.d(TAG, "checkPermissions: hasCameraPermissionDismiss = false");
        }


        // 需要两个权限才开始往下走？
        if (hasCameraPermissionDismiss && hasStoragePermissionDismiss) {
            init();
        } else if (!hasCameraPermissionDismiss && !hasStoragePermissionDismiss) {
//            onResulttPermissionsFail(CommConstants.PermissionConstants.PERMISSION_STORAGE_AND_CAMERA);
            Log.d(TAG, "checkPermissions: camera 权限 && 存储权限 fail");
            Toast.makeText(this, "缺少 存储权限 camera 权限", Toast.LENGTH_LONG).show();
        } else if (!hasCameraPermissionDismiss) {
            Log.d(TAG, "checkPermissions: camera 权限 fail");
            Toast.makeText(this, "缺少camera权限", Toast.LENGTH_LONG).show();
//            onResulttPermissionsFail(CommConstants.PermissionConstants.PERMISSION_CAMERA);
            if (hasStoragePermissionDismiss){
                Log.d(TAG, "checkPermissions: ");
                initEnv();
            }

        } else if (!hasStoragePermissionDismiss) {
            Log.d(TAG, "onRequestPermissionsResult: Storage 权限 fail");
//            onResulttPermissionsFail(CommConstants.PermissionConstants.PERMISSION_STORAGE);
            Toast.makeText(this, "缺少存储权限", Toast.LENGTH_LONG).show();
        }
    }


    public void go2AppMng() {
        if (ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_DENIED) {
            goSysAppMng();
        }
    }


    public void goSysAppMng() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        this.startActivity(intent);

//        //华为手机手机-跳转本应用权限
//        try {
//            Intent intent = new Intent();
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("packageName", this.getApplicationInfo().packageName);
//            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
//            intent.setComponent(comp);
//            this.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }


    protected abstract void onResulttPermissionsFail(String what);

    public abstract void init();
    public abstract void initEnv();

}
