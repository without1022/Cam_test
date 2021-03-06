package com.example.myapplication;

import android.Manifest;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.Impl.CamSurfaceTextureListenerImpl;
import com.example.myapplication.service.CameraManagerService;
import com.example.myapplication.utils.Utils;
import com.example.myapplication.view.CamPreview;

import org.xmlpull.v1.XmlPullParser;

import java.util.Arrays;

public class MainActivity extends Activity {
    private static final String LOG_TAG = "cam_test";
    TextureView mTextureView_0 = null;
    TextureView mTextureView_1 = null;
    TextureView mTextureView_2 = null;
    Button cam_0 = null;
    Button cam_1 = null;
    Button cam_2 = null;
    CameraDevice mCameraDevice_0 = null;
    CameraDevice mCameraDevice_1 = null;
    CameraDevice mCameraDevice_2 = null;
    CameraManager mCameraManager = null;
    private Surface mPreviewSurface_0 = null;
    private Surface mPreviewSurface_1 = null;
    private Surface mPreviewSurface_2 = null;
    CameraCaptureSession mCamSession_0 = null;
    CameraCaptureSession mCamSession_1 = null;
    CameraCaptureSession mCamSession_2 = null;
    private Size mPreViewSize = new Size(1280,720);
    String[] camIdList = null;
    LinearLayout buttonLinear = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //connect cameraManager
        try {
            CameraManagerService.getinstance().connectCameraManager(this);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        Utils.checkAndReqSelfPermission(MainActivity.this,Manifest.permission.CAMERA);
        Utils.checkAndReqSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Utils.checkAndReqSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
        textureListener_0 = new CamSurfaceTextureListenerImpl(MainActivity.this,"texture0",mTextureView_0);
        textureListener_1 = new CamSurfaceTextureListenerImpl(MainActivity.this,"texture1",mTextureView_1);
        textureListener_2 = new CamSurfaceTextureListenerImpl(MainActivity.this,"texture2",mTextureView_2);
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        //config preview view
        mTextureView_2 = findViewById(R.id.textureView3);
        mTextureView_1 = findViewById(R.id.textureView2);
        mTextureView_0 = findViewById(R.id.textureView);
        mTextureView_0.setSurfaceTextureListener(textureListener_0);
        mTextureView_1.setSurfaceTextureListener(textureListener_1);
        mTextureView_2.setSurfaceTextureListener(textureListener_2);

        //config button
        buttonLinear = findViewById(R.id.buttonLinear);
        cam_0 = findViewById(R.id.button);
        cam_1 = findViewById(R.id.button2);
        cam_2 = findViewById(R.id.button3);
        cam_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Utils.checkAndReqSelfPermission(MainActivity.this,Manifest.permission.CAMERA);
                    mCameraManager.openCamera("0", mStateCallback_0, null);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        cam_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Utils.checkAndReqSelfPermission(MainActivity.this,Manifest.permission.CAMERA);
                    mCameraManager.openCamera("1", mStateCallback_1, null);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        cam_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Utils.checkAndReqSelfPermission(MainActivity.this,Manifest.permission.CAMERA);
                    mCameraManager.openCamera("2", mStateCallback_2, null);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            camIdList = mCameraManager.getCameraIdList();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        switch(camIdList.length){
            case 1:
                cam_1.setVisibility(View.GONE);
                cam_2.setVisibility(View.GONE);
                mTextureView_1.setVisibility(View.GONE);
                mTextureView_2.setVisibility(View.GONE);
                break;
            case 2:
                cam_2.setVisibility(View.GONE);
                mTextureView_2.setVisibility(View.GONE);
                break;
            case 0:
                cam_0.setVisibility(View.GONE);
                cam_1.setVisibility(View.GONE);
                cam_2.setVisibility(View.GONE);
                mTextureView_0.setVisibility(View.GONE);
                mTextureView_1.setVisibility(View.GONE);
                mTextureView_2.setVisibility(View.GONE);
                TextView textNocam = new TextView(getApplicationContext());
                textNocam.setText(R.string.no_cam);
                buttonLinear.addView(textNocam);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public TextureView.SurfaceTextureListener textureListener_0 = null;
    public TextureView.SurfaceTextureListener textureListener_1 = null;
    public TextureView.SurfaceTextureListener textureListener_2 = null;

    public CameraDevice.StateCallback mStateCallback_0 = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            if(camera!=null){
                Log.i(LOG_TAG,"camera is not null");
            }
            Log.i(LOG_TAG,"@@@wjf onOpened cam_0");
            mCameraDevice_0 = camera;
//            startPreview_0();
            showDiag("选择预览模式",0);
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            Log.i(LOG_TAG,"@@@wjf onDisconnected cam_0");
            mCameraDevice_0 = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            Log.i(LOG_TAG,"@@@wjf onError cam_0,error="+error);
            mCameraDevice_0 = null;
        }
    };
    public CameraDevice.StateCallback mStateCallback_1 = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            if(camera!=null){
                Log.i(LOG_TAG,"camera is not null");
            }
            Log.i(LOG_TAG,"@@@wjf onOpened cam_1");
            mCameraDevice_1 = camera;
            showDiag("选择预览模式",1);
            //startPreview_1();
//            startRecord_1();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            Log.i(LOG_TAG,"@@@wjf onDisconnected cam_1");
            mCameraDevice_1 = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            Log.i(LOG_TAG,"@@@wjf onError cam_1,error="+error);
            mCameraDevice_1 = null;
        }
    };
    public CameraDevice.StateCallback mStateCallback_2 = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            if(camera!=null){
                Log.i(LOG_TAG,"camera is not null");
            }
            Log.i(LOG_TAG,"@@@wjf onOpened cam_2");
            mCameraDevice_2 = camera;
//            startPreview_2();
            showDiag("选择预览模式",2);
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            Log.i(LOG_TAG,"@@@wjf onDisconnected cam_2");
            mCameraDevice_2 = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            Log.i(LOG_TAG,"@@@wjf onError cam_2,error="+error);
            mCameraDevice_2 = null;
        }
    };



    public void startPreview_0(){

        SurfaceTexture mSurfaceTexture = mTextureView_0.getSurfaceTexture();
        mSurfaceTexture.setDefaultBufferSize(mPreViewSize.getWidth(),mPreViewSize.getHeight());
        mPreviewSurface_0 = new Surface(mSurfaceTexture);

        try{
            mCameraDevice_0.createCaptureSession(Arrays.asList(mPreviewSurface_0), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try{
                        CaptureRequest.Builder b = mCameraDevice_0.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//                            b.addTarget(mPreImageReader.getSurface());
                        b.addTarget(mPreviewSurface_0);
                        CaptureRequest r = b.build();
                        mCamSession_0 = session;
                        mCamSession_0.setRepeatingRequest(r,mPreviewCaptureCallback,null);
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
    public void startPreview_1(){

        SurfaceTexture mSurfaceTexture = mTextureView_1.getSurfaceTexture();
        mSurfaceTexture.setDefaultBufferSize(mPreViewSize.getWidth(),mPreViewSize.getHeight());
        mPreviewSurface_1 = new Surface(mSurfaceTexture);

        try{
            mCameraDevice_1.createCaptureSession(Arrays.asList(mPreviewSurface_1), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try{
                        CaptureRequest.Builder b = mCameraDevice_1.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//                            b.addTarget(mPreImageReader.getSurface());
                        b.addTarget(mPreviewSurface_1);
                        CaptureRequest r = b.build();
                        mCamSession_1 = session;
                        mCamSession_1.setRepeatingRequest(r,mPreviewCaptureCallback,null);
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
    public void startPreview_2(){

        SurfaceTexture mSurfaceTexture = mTextureView_2.getSurfaceTexture();
        mSurfaceTexture.setDefaultBufferSize(mPreViewSize.getWidth(),mPreViewSize.getHeight());
        mPreviewSurface_2 = new Surface(mSurfaceTexture);

        try{
            mCameraDevice_2.createCaptureSession(Arrays.asList(mPreviewSurface_2), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try{
                        CaptureRequest.Builder b = mCameraDevice_2.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//                            b.addTarget(mPreImageReader.getSurface());
                        b.addTarget(mPreviewSurface_2);
                        CaptureRequest r = b.build();
                        mCamSession_2 = session;
                        mCamSession_2.setRepeatingRequest(r,mPreviewCaptureCallback,null);
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

    public void startRecord_0(){

        SurfaceTexture mSurfaceTexture = mTextureView_0.getSurfaceTexture();
        mSurfaceTexture.setDefaultBufferSize(mPreViewSize.getWidth(),mPreViewSize.getHeight());
        mPreviewSurface_0 = new Surface(mSurfaceTexture);

        try{
            mCameraDevice_0.createCaptureSession(Arrays.asList(mPreviewSurface_0), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try{
                        CaptureRequest.Builder b = mCameraDevice_0.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
//                            b.addTarget(mPreImageReader.getSurface());
                        b.addTarget(mPreviewSurface_0);
                        CaptureRequest r = b.build();
                        mCamSession_0 = session;
                        mCamSession_0.setRepeatingRequest(r,mPreviewCaptureCallback,null);
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
    public void startRecord_1(){
        SurfaceTexture mSurfaceTexture = mTextureView_1.getSurfaceTexture();
        mSurfaceTexture.setDefaultBufferSize(mPreViewSize.getWidth(),mPreViewSize.getHeight());
        mPreviewSurface_1 = new Surface(mSurfaceTexture);

        try{
            mCameraDevice_1.createCaptureSession(Arrays.asList(mPreviewSurface_1), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try{
                        CaptureRequest.Builder b = mCameraDevice_1.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
//                            b.addTarget(mPreImageReader.getSurface());
                        b.addTarget(mPreviewSurface_1);
                        CaptureRequest r = b.build();
                        mCamSession_1 = session;
                        mCamSession_1.setRepeatingRequest(r,mPreviewCaptureCallback,null);
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
    public void startRecord_2(){

        SurfaceTexture mSurfaceTexture = mTextureView_2.getSurfaceTexture();
        mSurfaceTexture.setDefaultBufferSize(mPreViewSize.getWidth(),mPreViewSize.getHeight());
        mPreviewSurface_2 = new Surface(mSurfaceTexture);

        try{
            mCameraDevice_2.createCaptureSession(Arrays.asList(mPreviewSurface_2), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try{
                        CaptureRequest.Builder b = mCameraDevice_2.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
//                            b.addTarget(mPreImageReader.getSurface());
                        b.addTarget(mPreviewSurface_2);
                        CaptureRequest r = b.build();
                        mCamSession_2 = session;
                        mCamSession_2.setRepeatingRequest(r,mPreviewCaptureCallback,null);
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

    public void startZSL_0(){

        SurfaceTexture mSurfaceTexture = mTextureView_0.getSurfaceTexture();
        mSurfaceTexture.setDefaultBufferSize(mPreViewSize.getWidth(),mPreViewSize.getHeight());
        mPreviewSurface_0 = new Surface(mSurfaceTexture);

        try{
            mCameraDevice_0.createCaptureSession(Arrays.asList(mPreviewSurface_0), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try{
                        CaptureRequest.Builder b = mCameraDevice_0.createCaptureRequest(CameraDevice.TEMPLATE_ZERO_SHUTTER_LAG);
//                            b.addTarget(mPreImageReader.getSurface());
                        b.addTarget(mPreviewSurface_0);
                        CaptureRequest r = b.build();
                        mCamSession_0 = session;
                        mCamSession_0.setRepeatingRequest(r,mPreviewCaptureCallback,null);
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
    public void startZSL_1(){
        SurfaceTexture mSurfaceTexture = mTextureView_1.getSurfaceTexture();
        mSurfaceTexture.setDefaultBufferSize(mPreViewSize.getWidth(),mPreViewSize.getHeight());
        mPreviewSurface_1 = new Surface(mSurfaceTexture);

        try{
            mCameraDevice_1.createCaptureSession(Arrays.asList(mPreviewSurface_1), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try{
                        CaptureRequest.Builder b = mCameraDevice_1.createCaptureRequest(CameraDevice.TEMPLATE_ZERO_SHUTTER_LAG);
//                            b.addTarget(mPreImageReader.getSurface());
                        b.addTarget(mPreviewSurface_1);
                        CaptureRequest r = b.build();
                        mCamSession_1 = session;
                        mCamSession_1.setRepeatingRequest(r,mPreviewCaptureCallback,null);
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
    public void startZSL_2(){

        SurfaceTexture mSurfaceTexture = mTextureView_2.getSurfaceTexture();
        mSurfaceTexture.setDefaultBufferSize(mPreViewSize.getWidth(),mPreViewSize.getHeight());
        mPreviewSurface_2 = new Surface(mSurfaceTexture);

        try{
            mCameraDevice_2.createCaptureSession(Arrays.asList(mPreviewSurface_2), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try{
                        CaptureRequest.Builder b = mCameraDevice_2.createCaptureRequest(CameraDevice.TEMPLATE_ZERO_SHUTTER_LAG);
//                            b.addTarget(mPreImageReader.getSurface());
                        b.addTarget(mPreviewSurface_2);
                        CaptureRequest r = b.build();
                        mCamSession_2 = session;
                        mCamSession_2.setRepeatingRequest(r,mPreviewCaptureCallback,null);
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

    private int yourChoise;
    private int currentCamId = -1;
    private void showDiag(String title,int camId ){
        currentCamId = camId;

        final String[] items = { "preview","Video Preview","ZSL Preview"};
        yourChoise = 0;
        AlertDialog.Builder singeChoiceDialog = new AlertDialog.Builder(MainActivity.this);
        singeChoiceDialog.setTitle(title);
        singeChoiceDialog.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                yourChoise = which;
            }
        });
        singeChoiceDialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(yourChoise==-1)
                    return;
                int eventCode = (currentCamId<<4) + yourChoise;
                switch (eventCode){
                    case 0x00:
                        startPreview_0();
                        break;
                    case 0x01:
                        startRecord_0();
                        break;
                    case 0x02:
                        startZSL_0();
                        break;
                    case 0x10:
                        startPreview_1();
                        break;
                    case 0x11:
                        startRecord_1();
                        break;
                    case 0x12:
                        startZSL_1();
                        break;
                    case 0x20:
                        startPreview_2();
                        break;
                    case 0x21:
                        startRecord_2();
                        break;
                    case 0x22:
                        startZSL_2();
                        break;

                }
                currentCamId = -1;
            }
        });
        singeChoiceDialog.show();
    }

}