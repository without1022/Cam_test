package com.example.myapplication.Impl;

import android.Manifest;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.MainActivity;
import com.example.myapplication.utils.Utils;

public class CamSurfaceTextureListenerImpl implements TextureView.SurfaceTextureListener , ViewTreeObserver.OnGlobalLayoutListener{
    private String m;
    private Object mActivity;
    private View mView= null;
    private double aspectRatio = 16/9.0; //宽高比 默认 16 : 9

    public CamSurfaceTextureListenerImpl(@NonNull Object activity, String m, View view){
        mActivity = activity;
        this.m = m;
        mView = view;
    }

    public CamSurfaceTextureListenerImpl(@NonNull Object activity, String m, View view,double aspectRatio){
        mActivity = activity;
        this.m = m;
        mView = view;
        this.aspectRatio = aspectRatio;
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        Utils.checkAndReqSelfPermission(mActivity, Manifest.permission.CAMERA);
        Utils.checkAndReqSelfPermission(mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Utils.checkAndReqSelfPermission(mActivity,Manifest.permission.READ_EXTERNAL_STORAGE);
        Toast.makeText((Context)mActivity, m+" is success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }

    //等待View绘制完成的回调,自动调整宽高比
    @Override
    public void onGlobalLayout() {
        ViewGroup.LayoutParams viewParams = mView.getLayoutParams();
        viewParams.width = mView.getWidth();
        viewParams.height =(int) (mView.getHeight() / aspectRatio);
        mView.setLayoutParams(viewParams);
    }
}
