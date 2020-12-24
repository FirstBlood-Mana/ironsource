package com.zfinix.ironsource;

import android.app.Activity;

import com.ironsource.adapters.supersonicads.SupersonicConfig;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.ironsource.mediationsdk.sdk.OfferwallListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;


/**
 * IronSourcePlugin
 */
public class IronSourcePlugin {

    public final String TAG = "IronSourcePlugin";
    public String APP_KEY = "85460dcd";
    public Placement mPlacement;
    public final String FALLBACK_USER_ID = "userId";
    public static MethodCallHandlerImpl handler  = null;


    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), IronSourceConsts.MAIN_CHANNEL);
        handler = new MethodCallHandlerImpl(registrar.activity(), channel);
        channel.setMethodCallHandler(handler);

        final MethodChannel interstitialAdChannel = new MethodChannel(registrar.messenger(), IronSourceConsts.INTERSTITIAL_CHANNEL);
        registrar.platformViewRegistry().registerViewFactory(IronSourceConsts.BANNER_AD_CHANNEL, new IronSourceBanner(registrar.activity(), registrar.messenger()));
    }




}
