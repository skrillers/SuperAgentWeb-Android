/*
 * Copyright (C)  Justson(https://github.com/Justson/AgentWeb)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.siyee.superagentweb.middleware;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

import com.siyee.superagentweb.utils.LogUtils;

import java.lang.reflect.Method;

/**
 * 引用了AgentWeb的WebChromeClientDelegate
 *
 * @author hackycy
 */
public class WebChromeClientDelegate extends WebChromeClient {
    private WebChromeClient mDelegate;

    protected WebChromeClient getDelegate() {
        return mDelegate;
    }

    public WebChromeClientDelegate() {}

    public WebChromeClientDelegate(WebChromeClient webChromeClient) {
        this.mDelegate = webChromeClient;
    }

    public void setDelegate(WebChromeClient delegate) {
        this.mDelegate = delegate;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (this.mDelegate != null) {
            this.mDelegate.onProgressChanged(view, newProgress);
            return;
        }
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (this.mDelegate != null) {
            this.mDelegate.onReceivedTitle(view, title);
            return;
        }
        super.onReceivedTitle(view, title);
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        if (this.mDelegate != null) {
            this.mDelegate.onReceivedIcon(view, icon);
            return;
        }
        super.onReceivedIcon(view, icon);
    }

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url,
                                       boolean precomposed) {
        if (this.mDelegate != null) {
            this.mDelegate.onReceivedTouchIconUrl(view, url, precomposed);
            return;
        }
        super.onReceivedTouchIconUrl(view, url, precomposed);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (this.mDelegate != null) {
            this.mDelegate.onShowCustomView(view, callback);
            return;
        }
        super.onShowCustomView(view, callback);
    }


    @Override
    public void onShowCustomView(View view, int requestedOrientation,
                                 CustomViewCallback callback) {
        if (this.mDelegate != null) {
            this.mDelegate.onShowCustomView(view, requestedOrientation, callback);
            return;
        }
        super.onShowCustomView(view, requestedOrientation, callback);
    }


    @Override
    public void onHideCustomView() {
        if (this.mDelegate != null) {
            this.mDelegate.onHideCustomView();
            return;
        }
        super.onHideCustomView();
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog,
                                  boolean isUserGesture, Message resultMsg) {
        if (this.mDelegate != null) {
            return this.mDelegate.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onRequestFocus(WebView view) {
        if (this.mDelegate != null) {
            this.mDelegate.onRequestFocus(view);
            return;
        }
        super.onRequestFocus(view);
    }

    @Override
    public void onCloseWindow(WebView window) {
        if (this.mDelegate != null) {
            this.mDelegate.onCloseWindow(window);
            return;
        }
        super.onCloseWindow(window);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message,
                             JsResult result) {
        if (this.mDelegate != null) {
            return this.mDelegate.onJsAlert(view, url, message, result);
        }
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
                               JsResult result) {
        if (this.mDelegate != null) {
            return this.mDelegate.onJsConfirm(view, url, message, result);
        }
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result) {
        if (this.mDelegate != null) {
            return this.mDelegate.onJsPrompt(view, url, message, defaultValue, result);
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message,
                                    JsResult result) {
        if (this.mDelegate != null) {
            return this.mDelegate.onJsBeforeUnload(view, url, message, result);
        }
        return super.onJsBeforeUnload(view, url, message, result);
    }

    @Override
    @Deprecated
    public void onExceededDatabaseQuota(String url, String databaseIdentifier,
                                        long quota, long estimatedDatabaseSize, long totalQuota,
                                        WebStorage.QuotaUpdater quotaUpdater) {
        // This default implementation passes the current quota back to WebCore.
        // WebCore will interpret this that new quota was declined.
        if (this.mDelegate != null) {
            this.mDelegate.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
            return;
        }
        super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);

    }

    @Override
    @Deprecated
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota,
                                         WebStorage.QuotaUpdater quotaUpdater) {
        if (this.mDelegate != null) {
            this.mDelegate.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
            return;
        }
        super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin,
                                                   GeolocationPermissions.Callback callback) {
        if (this.mDelegate != null) {
            this.mDelegate.onGeolocationPermissionsShowPrompt(origin, callback);
            return;
        }
        super.onGeolocationPermissionsShowPrompt(origin, callback);

    }

    /**
     * notify the host application that a request for Geolocation permissions,
     * made with a previous call to
     * {@link #onGeolocationPermissionsShowPrompt(String, GeolocationPermissions.Callback) onGeolocationPermissionsShowPrompt()}
     * has been canceled. Any related UI should therefore be hidden.
     */
    @Override
    public void onGeolocationPermissionsHidePrompt() {

        if (this.mDelegate != null) {
            this.mDelegate.onGeolocationPermissionsHidePrompt();
            return;
        }

        super.onGeolocationPermissionsHidePrompt();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onPermissionRequest(PermissionRequest request) {
        if (this.mDelegate != null) {
            this.mDelegate.onPermissionRequest(request);
            return;
        }
        super.onPermissionRequest(request);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onPermissionRequestCanceled(PermissionRequest request) {

        if (this.mDelegate != null) {
            this.mDelegate.onPermissionRequestCanceled(request);
            return;
        }
        super.onPermissionRequestCanceled(request);
    }

    @Override
    public boolean onJsTimeout() {
        if (this.mDelegate != null) {
            return this.mDelegate.onJsTimeout();
        }
        return super.onJsTimeout();
    }

    @Override
    @Deprecated
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        if (this.mDelegate != null) {
            this.mDelegate.onConsoleMessage(message, lineNumber, sourceID);
            return;
        }
        super.onConsoleMessage(message, lineNumber, sourceID);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (this.mDelegate != null) {
            return this.mDelegate.onConsoleMessage(consoleMessage);
        }
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        if (this.mDelegate != null) {
            return this.mDelegate.getDefaultVideoPoster();
        }
        return super.getDefaultVideoPoster();
    }

    @Override
    public View getVideoLoadingProgressView() {
        if (this.mDelegate != null) {
            return this.mDelegate.getVideoLoadingProgressView();
        }
        return super.getVideoLoadingProgressView();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        if (this.mDelegate != null) {
            this.mDelegate.getVisitedHistory(callback);
            return;
        }
        super.getVisitedHistory(callback);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        if (this.mDelegate != null) {
            return this.mDelegate.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }


    /**
     * Android  >= 4.1
     *
     * @param uploadFile
     * @param acceptType
     * @param capture
     */
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        commonReflect(this.mDelegate, "openFileChooser", new Object[]{uploadFile, acceptType, capture}, ValueCallback.class, String.class, String.class);
    }

//    /**
//     * Android < 3.0
//     * minSdk > 4.4 so remove
//     */
//    public void openFileChooser(ValueCallback<Uri> valueCallback) {
//        commonReflect(this.mDelegate, "openFileChooser", new Object[]{valueCallback}, ValueCallback.class);
//    }

//    /**
//     * Android  >= 3.0
//     * minSdk > 4.4 so remove
//     */
//    public void openFileChooser(ValueCallback valueCallback, String acceptType) {
//        commonReflect(this.mDelegate, "openFileChooser", new Object[]{valueCallback, acceptType}, ValueCallback.class, String.class);
//    }

    /**
     * Refect
     * @param o
     * @param mothed
     * @param os
     * @param clazzs
     */
    private void commonReflect(WebChromeClient o, String mothed, Object[] os, Class... clazzs) {
        try {
            if (o == null) {
                return;
            }
            Class<?> clazz = o.getClass();
            Method mMethod = clazz.getMethod(mothed, clazzs);
            mMethod.invoke(o, os);
        } catch (Exception ignore) {
            if (LogUtils.isDebug()) {
                ignore.printStackTrace();
            }
        }
    }
}
