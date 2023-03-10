package com.common.lib.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.common.lib.R;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.common.lib.utils.BaseUtils;
import com.common.lib.widget.scanner.AmbientLightManager;
import com.common.lib.widget.scanner.BeepManager;
import com.common.lib.widget.scanner.FinishListener;
import com.common.lib.widget.scanner.InactivityTimer;
import com.common.lib.widget.scanner.IntentSource;
import com.common.lib.widget.scanner.camera.CameraManager;
import com.common.lib.widget.scanner.common.BitmapUtils;
import com.common.lib.widget.scanner.decode.BitmapDecoder;
import com.common.lib.widget.scanner.decode.CaptureActivityHandler;
import com.common.lib.widget.scanner.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.HybridBinarizer;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * ?????????
 *
 * @author xyx on 2017/2/21 17:07
 * @e-mail 384744573@qq.com
 * @see [?????????/??????](??????)
 */
public class CaptureActivity extends BaseActivity<EmptyPresenter> implements EmptyContract.View,
        SurfaceHolder.Callback, View.OnClickListener {
    private static final int RC_LOCATION_PERM = 100;//????????????

    private static final int REQUEST_CODE_PHOTO = 110;
    private static final int REQUEST_CODE_CROP = 111;
    private boolean isCapture = false;
    private RelativeLayout btn_back;
    private Button btnFromAlbum;
    private static final String TYPE = "type";
    private static final int TYPE_NORMAL = 0;
    private static final int Type_WEBVIEW = 1;
    private static final int[] types = {TYPE_NORMAL, Type_WEBVIEW};
    private static final String TAG = CaptureActivity.class.getSimpleName();

    private static final int REQUEST_CODE = 100;

    private static final int PARSE_BARCODE_FAIL = 300;
    private static final int PARSE_BARCODE_SUC = 200;
    public static boolean hadNetWork = false;
    /**
     * ???????????????
     */
    private boolean hasSurface;


    private View capture_frame;
    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????activity?????????
     * ???????????????????????????????????????????????????CaptureActivity??????????????????.??????????????????????????????????????????????????????????????????
     */
    private InactivityTimer inactivityTimer;

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private BeepManager beepManager;

    /**
     * ?????????????????????????????????????????????????????????????????????????????????
     */
    private AmbientLightManager ambientLightManager;

    private CameraManager cameraManager;
    /**
     * ????????????
     */
    private ViewfinderView viewfinderView;

    private CaptureActivityHandler handler;

    private Result lastResult;

    private boolean isFlashlightOpen;
    private Toolbar toolbar;

    /**
     * ????????????????????????(??????MultiFormatReader?????????)??? ???????????????????????????????????????????????????????????????????????????EAN-13???QR
     * Code?????? ?????????DecodeHintType.POSSIBLE_FORMATS??????
     * ??????DecodeThread??????????????????????????????hints.put(DecodeHintType.POSSIBLE_FORMATS,
     * decodeFormats);
     */
    private Collection<BarcodeFormat> decodeFormats;

    /**
     * ????????????????????????(??????MultiFormatReader?????????)??? ????????????????????????MultiFormatReader???
     * ?????????decodeFormats???characterSet?????????????????????decodeHints??? ??????????????????MultiFormatReader???
     * ??????DecodeHandler???????????????????????????multiFormatReader.setHints(hints);
     */
    private Map<DecodeHintType, ?> decodeHints;

    /**
     * ????????????????????????(??????MultiFormatReader?????????)??? ????????????????????????????????????????????????????????????
     * ?????????DecodeHintType.CHARACTER_SET??????
     * ??????DecodeThread????????????????????????hints.put(DecodeHintType.CHARACTER_SET,
     * characterSet);
     */
    private String characterSet;

    private Result savedResultToShow;

    private IntentSource source;

    /**
     * ???????????????
     */
    private String photoPath;

    private final Handler mHandler = new MyHandler(this);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_capture;
    }


    @NotNull
    @Override
    protected EmptyPresenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    class MyHandler extends Handler {

        private final WeakReference<Activity> activityReference;

        public MyHandler(Activity activity) {
            activityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case PARSE_BARCODE_SUC: // ??????????????????
                    Toast.makeText(activityReference.get(),
                            CaptureActivity.this.getString(R.string.capture_success_result) + msg.obj, Toast.LENGTH_SHORT).show();
                    break;

                case PARSE_BARCODE_FAIL:// ??????????????????

                    Toast.makeText(activityReference.get(), getString(R.string.capture_error_result),
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CaptureActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreated(Bundle bundle) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        capture_frame = findViewById(R.id.capture_frame);
        toolbar = findViewById(R.id.tool_bar);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        ambientLightManager = new AmbientLightManager(this);

        setBaseBackToolbar(toolbar, getString(R.string.property_drawer_qrcode_scanner));

        // ????????????????????????
        findViewById(R.id.capture_scan_photo).setOnClickListener(this);
        findViewById(R.id.capture_flashlight).setOnClickListener(this);
        hadNetWork = isNetworkConnected();
        viewfinderView = (ViewfinderView) findViewById(R.id.capture_viewfinder_view);

        btnFromAlbum = (Button) findViewById(R.id.right);
        btnFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2SelectPhonePhoto();
            }
        });
        String[] perms = {Manifest.permission.CAMERA};
        // ???????????????????????????
        PermissionUtils.permission(PermissionConstants.CAMERA).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {
                // ??????????????????do anything.
                if (!hadNetWork) {
                    viewfinderView.drawResultBitmap(true);
                } else {
                    viewfinderView.drawResultBitmap(false);
                }
            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                //??????????????????????????????
                if (!permissionsDeniedForever.isEmpty()) {
                    showOpenAppSettingDialog();
                    return;
                }
            }
        }).request();

    }

    /**
     * ??????????????????
     */
    private void showOpenAppSettingDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(ActivityUtils.getTopActivity())
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage(R.string.permission_denied_forever_message)
                .setPositiveButton(android.R.string.ok, ((dialog, which) -> {
                    PermissionUtils.launchAppDetailsSettings();
                    dialog.dismiss();
                })).setOnCancelListener((dialog -> {
            dialog.dismiss();
        })).setCancelable(false)
                .create()
                .show();
    }

    /**
     * ????????????title??????????????????Toolbar
     *
     * @param toolbar
     * @param title
     */
    protected void setBaseBackToolbar(Toolbar toolbar, String title) {
        toolbar.setContentInsetsAbsolute(0, 0);
        TextView tv_title = toolbar.findViewById(R.id.tv_toolbar_title);
        if (tv_title != null) {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
        }
        ImageButton leftButton = (ImageButton) toolbar.findViewById(R.id.ib_toolbar_left);
        if (leftButton != null) {
            leftButton.setVisibility(View.VISIBLE);
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        initResume();
        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.

        // ??????????????????????????????????????????????????????????????????????????????
        // ???????????????onCreate?????????????????????onCreate??????????????????????????????????????????????????? ??????
        // ?????????????????????????????????????????????bug

    }

    public void initResume() {
        cameraManager = new CameraManager(getApplication());

        viewfinderView = (ViewfinderView) findViewById(R.id.capture_viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);
        handler = null;
        lastResult = null;

        // ?????????????????????????????????SurfaceView???????????????????????????????????????????????????
        // ??????????????????SurfaceView?????????
        // ??????:http://blog.csdn.net/luoshengyang/article/details/8661317
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview_view); // ??????
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);

        } else {
            // ??????sdk8??????????????????????????????
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            surfaceHolder.addCallback(this);
        }

        // ??????????????????????????????BeemManager?????????????????????????????????????????????onCreate????????????????????????
        beepManager.updatePrefs();

        // ????????????????????????
        ambientLightManager.start(cameraManager);

        // ?????????????????????
        inactivityTimer.onResume();

        source = IntentSource.NONE;
        decodeFormats = null;
        characterSet = null;
    }


    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        ambientLightManager.stop();
        beepManager.close();

        // ???????????????
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if ((source == IntentSource.NONE) && lastResult != null) { // ??????????????????
                    restartPreviewAfterDelay(0L);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_FOCUS:
            case KeyEvent.KEYCODE_CAMERA:
                // Handle these events so they don't launch the Camera app
                return true;

            case KeyEvent.KEYCODE_VOLUME_UP:
                cameraManager.zoomIn();
                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                cameraManager.zoomOut();
                return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            final ProgressDialog progressDialog;
            switch (requestCode) {
                case REQUEST_CODE:

                    // ???????????????????????????
                    Cursor cursor = getContentResolver().query(
                            intent.getData(), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photoPath = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    cursor.close();

                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage(getString(R.string.capture_scan_doing));
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            Bitmap img = BitmapUtils
                                    .getCompressedBitmap(photoPath);

                            BitmapDecoder decoder = new BitmapDecoder(
                                    CaptureActivity.this);
                            Result result = decoder.getRawResult(img);

                            if (result != null) {
                                Message m = mHandler.obtainMessage();
                                m.what = PARSE_BARCODE_SUC;
                                m.obj = ResultParser.parseResult(result)
                                        .toString();
                                mHandler.sendMessage(m);
                            } else {
                                Message m = mHandler.obtainMessage();
                                m.what = PARSE_BARCODE_FAIL;
                                mHandler.sendMessage(m);
                            }

                            progressDialog.dismiss();

                        }
                    }).start();

                    break;
                case REQUEST_CODE_PHOTO:
                    goToCropActivity(intent.getData());
                    break;
                case REQUEST_CODE_CROP:
                    String pathAfterCrop = BaseUtils.StaticParams.getSaveFilePath(this, "ssx_zxing.jpg");
                    Result result = scanningImage(pathAfterCrop);
                    if (result != null) {
                        go2WebView(result);
                    } else {
                        finish();
                        Toast.makeText(CaptureActivity.this, getString(R.string.capture_scan_error), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }


    /**
     * ????????????????????????Webview
     *
     * @param result
     */
    private void go2WebView(Result result) {
        String url = ResultParser.parseResult(result).toString();
        if (TextUtils.isEmpty(url)) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        if (url.contains("?data=")) {
            try {
                String data = URLDecoder.decode(url.split("data=")[1]);
                map.put(EventBusEvent.SCAN_GO_PAGER_RESULT, data);
            } catch (Exception e) {
            }
        } else {
            map.put(EventBusEvent.SCAN_RESULT, url);
        }
        EventBus.getDefault().post(map);
        finish();
    }


    /**
     * ?????????????????????????????????
     *
     * @param path
     * @return
     */
    protected Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        Bitmap scanBitmap;
        // DecodeHintType ???EncodeHintType
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // ??????????????????????????????
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // ??????????????????
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // ??????????????????
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);

        if (scanBitmap != null) {
            //????????????????????????,????????????????????????????????????
            LuminanceSource source = new PlanarYUVLuminanceSource(
                    rgb2YUV(scanBitmap), scanBitmap.getWidth(),
                    scanBitmap.getHeight(), 0, 0, scanBitmap.getWidth(),
                    scanBitmap.getHeight(), false);

            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
            MultiFormatReader reader = new MultiFormatReader();
            try {
                Result result = reader.decode(binaryBitmap, hints);
                return result;

            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * @param bitmap ???????????????
     * @return YUV??????
     */
    public byte[] rgb2YUV(Bitmap bitmap) {
        // ???????????????QQ??????
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int len = width * height;
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = pixels[i * width + j] & 0x00FFFFFF;

                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;

                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;

                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);

                yuv[i * width + j] = (byte) y;
                // yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
                // yuv[len + (i >> 1) * width + (j & ~1) + 1] = (byte) v;
            }
        }
        return yuv;
    }

    /**
     * ?????????????????????
     *
     * @param uri
     */
    private void goToCropActivity(final Uri uri) {
        //String path =SharedPreferencesUtil.getString("temppath", "");
        if (uri == null) {
//            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
            return;
        }

        //????????????????????????????????????????????????????????????????????????
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = BitmapUtils.getFilePathFromUri(CaptureActivity.this, uri);
                Result result = scanningImage(path);
                if (result != null) {//????????????????????????????????????????????????
                    go2WebView(result);
                }
            }
        }).start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG,
                    "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        hasSurface = false;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {

        // ????????????
        inactivityTimer.onActivity();

        lastResult = rawResult;


        beepManager.playBeepSoundAndVibrate();

        String url = ResultParser.parseResult(rawResult).toString();
        if (TextUtils.isEmpty(url)) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        if (url.contains("?data=")) {
            try {
                String data = URLDecoder.decode(url.split("data=")[1]);
                map.put(EventBusEvent.SCAN_GO_PAGER_RESULT, data);
            } catch (Exception e) {
            }
        } else {
            map.put(EventBusEvent.SCAN_RESULT, url);
        }
        EventBus.getDefault().post(map);
        finish();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
        lastResult = null;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }

        if (cameraManager.isOpen()) {
            Log.w(TAG,
                    "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats,
                        decodeHints, characterSet, cameraManager);
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * ???CaptureActivityHandler?????????????????????????????????????????????
     *
     * @param bitmap
     * @param result
     */
    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        // Bitmap isn't used yet -- will be used soon
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler,
                        R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.zxing_msg_camera_framework_bug));
        builder.setPositiveButton(R.string.zxing_button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.capture_scan_photo) { // ????????????
            // ????????????????????????
            Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
            innerIntent.setType("image/*");
            Intent wrapperIntent = Intent.createChooser(innerIntent,
                    getString(R.string.capture_choose_qcode_photo));
            this.startActivityForResult(wrapperIntent, REQUEST_CODE);
        } else if (id == R.id.capture_flashlight) {
            if (isFlashlightOpen) {
                cameraManager.setTorch(false); // ???????????????
                isFlashlightOpen = false;
            } else {
                cameraManager.setTorch(true); // ???????????????
                isFlashlightOpen = true;
            }
        }

    }


    private void go2SelectPhonePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");//????????????
        startActivityForResult(intent, REQUEST_CODE_PHOTO);
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    @Override
    public void finish() {
        super.finish();
    }
}






