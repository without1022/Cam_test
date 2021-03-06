package com.example.myapplication.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.util.AttributeSet;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.R;
import com.example.myapplication.service.CameraManagerService;
import com.example.myapplication.utils.Utils;

import java.util.Arrays;


public class CamPreview extends FrameLayout{
    private TextureView textureView = null;
    private CheckBox m_chk_preview = null;
    private Surface preSurface = null;
    private Size mPreViewSize = new Size(1280, 720);
    private String m_cam_id = "0";
    private CameraDevice mCameraDevice = null;
    CameraCaptureSession mCamSession = null;

    public CamPreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        Utils.checkAndReqSelfPermission(CameraManagerService.getinstance().getMainActivity(),Manifest.permission.CAMERA);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CamPreview);
        m_cam_id = ta.getString(R.styleable.CamPreview_cam_id);
//        if(CameraManagerService.getinstance().getCameraList()!=null && CameraManagerService.getinstance().getCameraList().length<=Integer.getInteger(m_cam_id)){
//            this.setVisibility(GONE);
//        }
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.cam_preview, this);
        textureView = view.findViewById(R.id.camPreView);
        m_chk_preview = view.findViewById(R.id.startPreview);
        m_chk_preview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo add camera open
                if (isChecked) {//open camera
                    Utils.checkAndReqSelfPermission(CameraManagerService.getinstance().getMainActivity(),Manifest.permission.CAMERA);
                    try {
                        CameraManagerService.getinstance().getmCameraManager().openCamera(m_cam_id, mStateCallback, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }else{//close camera
                    closeCamera();
                }
            }
        });
    }

    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback(){

        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            mCameraDevice = null;
            m_chk_preview.setChecked(false);
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            mCameraDevice = null;
            m_chk_preview.setChecked(false);
        }
    };

    public void startPreview(){
        SurfaceTexture mSurfaceTexture = textureView.getSurfaceTexture();
        mSurfaceTexture.setDefaultBufferSize(mPreViewSize.getWidth(), mPreViewSize.getHeight());
        preSurface = new Surface(mSurfaceTexture);
        try{
            mCameraDevice.createCaptureSession(Arrays.asList(preSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try{
                        CaptureRequest.Builder b = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//                            b.addTarget(mPreImageReader.getSurface());
                        b.addTarget(preSurface);
                        CaptureRequest r = b.build();
                        mCamSession = session;
                        mCamSession.setRepeatingRequest(r,mPreviewCaptureCallback,null);

                    }catch (CameraAccessException e){
                        e.printStackTrace();
                    }
                }
                CameraCaptureSession.CaptureCallback mPreviewCaptureCallback = new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
//                            super.onCaptureCompleted(session, request, result);

                    }
                };

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            },null);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    private void release(){
        closeCamera();
        textureView= null;
        m_chk_preview=null;
        preSurface = null;
    }

    private void closeCamera(){
        if(mCamSession!=null){
            mCamSession.close();
            mCamSession = null;
        }
        if(mCameraDevice!=null){
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        release();
    }
}
