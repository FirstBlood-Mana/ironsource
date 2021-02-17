package com.zfinix.ironsource;

import android.app.Activity;
import android.content.Context;
import java.util.HashMap;

import io.flutter.Log;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;
import android.content.Context;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;
import java.util.Map;



public class IronSourceBanner extends PlatformViewFactory {
    private final BinaryMessenger messenger;
    public final Activity mActivity;

    IronSourceBanner(Activity activity,BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
        this.mActivity = activity;
    }


    @Override
    public PlatformView create(Context context, int id, Object args) {
        final Map<String, String> creationParams = (Map<String, String>) args;
        return new IronSourceBannerView(context, id, creationParams, this.messenger, this.mActivity);
    }

}

