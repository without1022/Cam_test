package com.example.myapplication.service;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

import androidx.annotation.NonNull;

public class CameraManagerService {
    private CameraManager mCameraManager = null;
    private Activity mActivity = null;
    private static CameraManagerService instance = new CameraManagerService();
    private String[] m_camera_list = null;

    private CameraManagerService(){}

    public static CameraManagerService getinstance(){
        return instance;
    }

    public void connectCameraManager(Activity mainActivity) throws CameraAccessException {
        mActivity = mainActivity;
        if(mActivity!=null) {
            mCameraManager = (CameraManager) mActivity.getSystemService(Context.CAMERA_SERVICE);
            m_camera_list = mCameraManager.getCameraIdList();
        }
    }

    public Activity getMainActivity(){
        return mActivity;
    }

    public CameraManager getmCameraManager(){
        return mCameraManager;
    }

    public String[] getCameraList() {
        return m_camera_list;
    }
}
