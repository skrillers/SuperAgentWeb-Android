package com.siyee.superagentweb.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.siyee.superagentweb.abs.Consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/12/29
 *     desc  : utils about permission
 * </pre>
 */
public final class PermissionUtils {

    private static PermissionUtils sInstance;

    private String[]            mPermissionsParam;
    private OnRationaleListener mOnRationaleListener;
    private SingleCallback      mSingleCallback;
    private SimpleCallback      mSimpleCallback;
    private FullCallback        mFullCallback;
    private ThemeCallback       mThemeCallback;
    private Set<String>         mPermissions;
    private List<String>        mPermissionsRequest;
    private List<String>        mPermissionsGranted;
    private List<String>        mPermissionsDenied;
    private List<String>        mPermissionsDeniedForever;

    private static SimpleCallback sSimpleCallback4WriteSettings;
    private static SimpleCallback sSimpleCallback4DrawOverlays;

    /**
     * Return the permissions used in application.
     *
     * @return the permissions used in application
     */
    public static List<String> getPermissions() {
        return getPermissions(SuperAgentWebUtils.getApp().getPackageName());
    }

    /**
     * Return the permissions used in application.
     *
     * @param packageName The name of the package.
     * @return the permissions used in application
     */
    public static List<String> getPermissions(final String packageName) {
        PackageManager pm = SuperAgentWebUtils.getApp().getPackageManager();
        try {
            String[] permissions = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions;
            if (permissions == null) return Collections.emptyList();
            return Arrays.asList(permissions);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Return whether <em>you</em> have been granted the permissions.
     *
     * @param permissions The permissions.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isGranted(final String... permissions) {
        for (String permission : permissions) {
            if (!isGranted(permission)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isGranted(final String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission(SuperAgentWebUtils.getApp(), permission);
    }

    /**
     * Return whether the app can modify system settings.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isGrantedWriteSettings() {
        return Settings.System.canWrite(SuperAgentWebUtils.getApp());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestWriteSettings(final SimpleCallback callback) {
        if (isGrantedWriteSettings()) {
            if (callback != null) callback.onGranted();
            return;
        }
        sSimpleCallback4WriteSettings = callback;
        PermissionActivityImpl.start(PermissionActivityImpl.TYPE_WRITE_SETTINGS);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void startWriteSettingsActivity(final Activity activity, final int requestCode) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + SuperAgentWebUtils.getApp().getPackageName()));
        if (!SuperAgentWebUtils.isIntentAvailable(intent)) {
            launchAppDetailsSettings();
            return;
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Return whether the app can draw on top of other apps.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isGrantedDrawOverlays() {
        return Settings.canDrawOverlays(SuperAgentWebUtils.getApp());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestDrawOverlays(final SimpleCallback callback) {
        if (isGrantedDrawOverlays()) {
            if (callback != null) callback.onGranted();
            return;
        }
        sSimpleCallback4DrawOverlays = callback;
        PermissionActivityImpl.start(PermissionActivityImpl.TYPE_DRAW_OVERLAYS);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void startOverlayPermissionActivity(final Activity activity, final int requestCode) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + SuperAgentWebUtils.getApp().getPackageName()));
        if (!SuperAgentWebUtils.isIntentAvailable(intent)) {
            launchAppDetailsSettings();
            return;
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Launch the application's details settings.
     */
    public static void launchAppDetailsSettings() {
        Intent intent = SuperAgentWebUtils.getLaunchAppDetailsSettingsIntent(SuperAgentWebUtils.getApp().getPackageName(), true);
        if (!SuperAgentWebUtils.isIntentAvailable(intent)) return;
        SuperAgentWebUtils.getApp().startActivity(intent);
    }

    /**
     * Set the permissions.
     *
     * @param permissions The permissions.
     * @return the single {@link PermissionUtils} instance
     */
    public static PermissionUtils permission(final String... permissions) {
        return new PermissionUtils(permissions);
    }

    private PermissionUtils(final String... permissions) {
        mPermissionsParam = permissions;
        sInstance = this;
    }

    /**
     * Set rationale listener.
     *
     * @param listener The rationale listener.
     * @return the single {@link PermissionUtils} instance
     */
    public PermissionUtils rationale(final OnRationaleListener listener) {
        mOnRationaleListener = listener;
        return this;
    }

    /**
     * Set the simple call back.
     *
     * @param callback the single call back
     * @return the single {@link PermissionUtils} instance
     */
    public PermissionUtils callback(final SingleCallback callback) {
        mSingleCallback = callback;
        return this;
    }

    /**
     * Set the simple call back.
     *
     * @param callback the simple call back
     * @return the single {@link PermissionUtils} instance
     */
    public PermissionUtils callback(final SimpleCallback callback) {
        mSimpleCallback = callback;
        return this;
    }

    /**
     * Set the full call back.
     *
     * @param callback the full call back
     * @return the single {@link PermissionUtils} instance
     */
    public PermissionUtils callback(final FullCallback callback) {
        mFullCallback = callback;
        return this;
    }

    /**
     * Set the theme callback.
     *
     * @param callback The theme callback.
     * @return the single {@link PermissionUtils} instance
     */
    public PermissionUtils theme(final ThemeCallback callback) {
        mThemeCallback = callback;
        return this;
    }

    /**
     * Start request.
     */
    public void request() {
        if (mPermissionsParam == null || mPermissionsParam.length <= 0) {
            Log.w("PermissionUtils", "No permissions to request.");
            return;
        }

        mPermissions = new LinkedHashSet<>();
        mPermissionsRequest = new ArrayList<>();
        mPermissionsGranted = new ArrayList<>();
        mPermissionsDenied = new ArrayList<>();
        mPermissionsDeniedForever = new ArrayList<>();

        List<String> appPermissions = getPermissions();
        for (String param : mPermissionsParam) {
            boolean isIncludeInManifest = false;
            String[] permissions = PermissionConstants.getPermissions(param);
            for (String permission : permissions) {
                if (appPermissions.contains(permission)) {
                    mPermissions.add(permission);
                    isIncludeInManifest = true;
                }
            }
            if (!isIncludeInManifest) {
                mPermissionsDenied.add(param);
                Log.e("PermissionUtils", "U should add the permission of " + param + " in manifest.");
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mPermissionsGranted.addAll(mPermissions);
            requestCallback();
        } else {
            for (String permission : mPermissions) {
                if (isGranted(permission)) {
                    mPermissionsGranted.add(permission);
                } else {
                    mPermissionsRequest.add(permission);
                }
            }
            if (mPermissionsRequest.isEmpty()) {
                requestCallback();
            } else {
                startPermissionActivity();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startPermissionActivity() {
        PermissionActivityImpl.start(PermissionActivityImpl.TYPE_RUNTIME);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean shouldRationale(final UtilsTransActivity activity, final Runnable againRunnable) {
        boolean isRationale = false;
        if (mOnRationaleListener != null) {
            for (String permission : mPermissionsRequest) {
                if (activity.shouldShowRequestPermissionRationale(permission)) {
                    rationalInner(activity, againRunnable);
                    isRationale = true;
                    break;
                }
            }
            mOnRationaleListener = null;
        }
        return isRationale;
    }

    private void rationalInner(final UtilsTransActivity activity, final Runnable againRunnable) {
        getPermissionsStatus(activity);
        mOnRationaleListener.rationale(activity, new OnRationaleListener.ShouldRequest() {
            @Override
            public void again(boolean again) {
                if (again) {
                    mPermissionsDenied = new ArrayList<>();
                    mPermissionsDeniedForever = new ArrayList<>();
                    againRunnable.run();
                } else {
                    activity.finish();
                    requestCallback();
                }
            }
        });
    }

    private void getPermissionsStatus(final Activity activity) {
        for (String permission : mPermissionsRequest) {
            if (isGranted(permission)) {
                mPermissionsGranted.add(permission);
            } else {
                mPermissionsDenied.add(permission);
                if (!activity.shouldShowRequestPermissionRationale(permission)) {
                    mPermissionsDeniedForever.add(permission);
                }
            }
        }
    }

    private void requestCallback() {
        if (mSingleCallback != null) {
            mSingleCallback.callback(mPermissionsDenied.isEmpty(),
                    mPermissionsGranted, mPermissionsDeniedForever, mPermissionsDenied);
            mSingleCallback = null;
        }
        if (mSimpleCallback != null) {
            if (mPermissionsDenied.isEmpty()) {
                mSimpleCallback.onGranted();
            } else {
                mSimpleCallback.onDenied();
            }
            mSimpleCallback = null;
        }
        if (mFullCallback != null) {
            if (mPermissionsRequest.size() == 0
                    || mPermissionsGranted.size() > 0) {
                mFullCallback.onGranted(mPermissionsGranted);
            }
            if (!mPermissionsDenied.isEmpty()) {
                mFullCallback.onDenied(mPermissionsDeniedForever, mPermissionsDenied);
            }
            mFullCallback = null;
        }
        mOnRationaleListener = null;
        mThemeCallback = null;
    }

    private void onRequestPermissionsResult(final Activity activity) {
        getPermissionsStatus(activity);
        requestCallback();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    static final class PermissionActivityImpl extends UtilsTransActivity.TransActivityDelegate {

        private static final String TYPE                = "TYPE";
        private static final int    TYPE_RUNTIME        = 0x01;
        private static final int    TYPE_WRITE_SETTINGS = 0x02;
        private static final int    TYPE_DRAW_OVERLAYS  = 0x03;

        private static int currentRequestCode = -1;

        private static PermissionActivityImpl INSTANCE = new PermissionActivityImpl();

        public static void start(final int type) {
            UtilsTransActivity.start(new Consumer<Intent>() {
                @Override
                public void accept(Intent data) {
                    data.putExtra(TYPE, type);
                }
            }, INSTANCE);
        }

        @Override
        public void onCreated(final UtilsTransActivity activity, @Nullable Bundle savedInstanceState) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            int type = activity.getIntent().getIntExtra(TYPE, -1);
            if (type == TYPE_RUNTIME) {
                if (sInstance == null) {
                    Log.e("PermissionUtils", "request permissions failed");
                    activity.finish();
                    return;
                }
                if (sInstance.mThemeCallback != null) {
                    sInstance.mThemeCallback.onActivityCreate(activity);
                }
                if (sInstance.shouldRationale(activity, new Runnable() {
                    @Override
                    public void run() {
                        requestPermissions(activity);
                    }
                })) {
                    return;
                }
                requestPermissions(activity);
            } else if (type == TYPE_WRITE_SETTINGS) {
                currentRequestCode = TYPE_WRITE_SETTINGS;
                startWriteSettingsActivity(activity, TYPE_WRITE_SETTINGS);
            } else if (type == TYPE_DRAW_OVERLAYS) {
                currentRequestCode = TYPE_DRAW_OVERLAYS;
                startOverlayPermissionActivity(activity, TYPE_DRAW_OVERLAYS);
            } else {
                activity.finish();
                Log.e("PermissionUtils", "type is wrong.");
            }
        }

        private void requestPermissions(Activity activity) {
            if (sInstance.mPermissionsRequest != null) {
                int size = sInstance.mPermissionsRequest.size();
                if (size <= 0) {
                    activity.finish();
                    return;
                }
                activity.requestPermissions(sInstance.mPermissionsRequest.toArray(new String[size]), 1);
            }
        }

        @Override
        public void onRequestPermissionsResult(UtilsTransActivity activity,
                                               int requestCode,
                                               String[] permissions,
                                               int[] grantResults) {
            activity.finish();
            if (sInstance != null && sInstance.mPermissionsRequest != null) {
                sInstance.onRequestPermissionsResult(activity);
            }
        }


        @Override
        public boolean dispatchTouchEvent(UtilsTransActivity activity, MotionEvent ev) {
            activity.finish();
            return true;
        }

        @Override
        public void onDestroy(final UtilsTransActivity activity) {
            if (currentRequestCode != -1) {
                checkRequestCallback(currentRequestCode);
                currentRequestCode = -1;
            }
            super.onDestroy(activity);
        }

        @Override
        public void onActivityResult(UtilsTransActivity activity, int requestCode, int resultCode, Intent data) {
            activity.finish();
        }

        private void checkRequestCallback(int requestCode) {
            if (requestCode == TYPE_WRITE_SETTINGS) {
                if (sSimpleCallback4WriteSettings == null) return;
                if (isGrantedWriteSettings()) {
                    sSimpleCallback4WriteSettings.onGranted();
                } else {
                    sSimpleCallback4WriteSettings.onDenied();
                }
                sSimpleCallback4WriteSettings = null;
            } else if (requestCode == TYPE_DRAW_OVERLAYS) {
                if (sSimpleCallback4DrawOverlays == null) return;
                if (isGrantedDrawOverlays()) {
                    sSimpleCallback4DrawOverlays.onGranted();
                } else {
                    sSimpleCallback4DrawOverlays.onDenied();
                }
                sSimpleCallback4DrawOverlays = null;
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////

    public interface OnRationaleListener {

        void rationale(UtilsTransActivity activity, ShouldRequest shouldRequest);

        interface ShouldRequest {
            void again(boolean again);
        }
    }

    public interface SingleCallback {
        void callback(boolean isAllGranted, @NonNull List<String> granted,
                      @NonNull List<String> deniedForever, @NonNull List<String> denied);
    }


    public interface SimpleCallback {
        void onGranted();

        void onDenied();
    }

    public interface FullCallback {
        void onGranted(@NonNull List<String> granted);

        void onDenied(@NonNull List<String> deniedForever, @NonNull List<String> denied);
    }

    public interface ThemeCallback {
        void onActivityCreate(Activity activity);
    }
}