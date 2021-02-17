package com.zfinix.ironsource;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

public class IronSourceBannerView implements PlatformView, BannerListener, ActivityAware {
    private FrameLayout rootView;
    private final String TAG = "IronSourceBannerView";
    private final MethodChannel channel;
    private final Map<String, String> args;
    private Context context;
    private Activity activity;

    private IronSourceBannerLayout bannerLayout;

    private boolean initialized;

    private String placementName = "DefaultBanner";


    IronSourceBannerView(Context context, int id, Map<String, String> args, BinaryMessenger messenger, Activity activity) {
        this.channel = new MethodChannel(messenger,
                IronSourceConsts.BANNER_AD_CHANNEL + id);
        this.activity = activity;
        this.args = args;
        this.context = context;

        if(args.get("placementName") != null && !args.get("placementName").isEmpty()) {
            this.placementName = args.get("placementName");
        }

        loadBanner();

    }

    public void loadBanner() {

        placementName = this.placementName;

        if (!initialized) {
            initialized = true;
        }
        if (activity == null) {
            Log.e("E_LOAD_ACTIVITY", "Found no activity");
            //channel.invokeMethod("E_LOAD_ACTIVITY", "Found no activity");
            return;
        }
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                ISBannerSize size = ISBannerSize.BANNER;
                rootView = new FrameLayout(context);
                if(bannerLayout != null)
                IronSource.destroyBanner(bannerLayout);
                bannerLayout = IronSource.createBanner(activity, size);
                bannerLayout.setBannerListener(IronSourceBannerView.this);
                IronSource.loadBanner(bannerLayout, placementName);

                if (rootView.getChildCount() > 0)
                    rootView.removeAllViews();

                    final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                    );
                    bannerLayout.setVisibility(View.VISIBLE);
                    rootView.addView(bannerLayout, 0, layoutParams);

                Log.d("E_LOAD_ACTIVITY", "Loaded activity");
            }
        });
    }


    @Override
    public View getView() {
        return rootView;
    }

    @Override
    public void dispose() {
        IronSource.destroyBanner(bannerLayout);
    }

    public void destroyBanner() {
        if (bannerLayout != null) {
                IronSource.destroyBanner(bannerLayout);
        }
    }

    public void showBanner() {
        if (bannerLayout != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bannerLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }


    public void hideBanner() {
        if (bannerLayout != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bannerLayout.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public void onBannerAdLoaded() {
        activity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        channel.invokeMethod(IronSourceConsts.ON_BANNER_AD_LOADED, null);
                    }
                }
        );
    }

    @Override
    public void onBannerAdLoadFailed(final IronSourceError ironSourceError) {

        activity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        bannerLayout.removeAllViews();

                        Map<String, Object> arguments = new HashMap<String, Object>();
                        arguments.put("errorCode", ironSourceError.getErrorCode());
                        arguments.put("errorMessage", ironSourceError.getErrorMessage());
                        channel.invokeMethod(IronSourceConsts.ON_BANNER_AD_LOAD_FAILED, arguments);
                    }
                }
        );

    }

    @Override
    public void onBannerAdClicked() {
        activity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        channel.invokeMethod(IronSourceConsts.ON_BANNER_AD_CLICKED, null);
                    }
                }
        );
    }

    @Override
    public void onBannerAdScreenPresented() {
        activity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        channel.invokeMethod(IronSourceConsts.ON_BANNER_AD_SCREEN_PRESENTED, null);

                    }
                }
        );
    }

    @Override
    public void onBannerAdScreenDismissed() {
        activity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        channel.invokeMethod(IronSourceConsts.ON_BANNER_AD_sCREEN_DISMISSED, null);

                    }
                }
        );
    }

    @Override
    public void onBannerAdLeftApplication() {
        activity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        channel.invokeMethod(IronSourceConsts.ON_BANNER_AD_LEFT_APPLICATION, null);
                    }
                }
        );

    }


    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
        context = activity.getApplicationContext();
        IronSource.onResume(activity);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        IronSource.onPause(activity);
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
        activity = binding.getActivity();
        context = activity.getApplicationContext();
        IronSource.onResume(activity);
        showBanner();
    }

    @Override
    public void onDetachedFromActivity() {
        IronSource.onPause(activity);
        onDetachedFromActivity();
        hideBanner();
    }
}
