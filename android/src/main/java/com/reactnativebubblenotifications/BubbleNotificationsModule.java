package com.reactnativebubblenotifications;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

import androidx.annotation.NonNull;

import android.text.Layout;
import android.view.LayoutInflater;
import android.provider.Settings;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.net.Uri;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;


import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

@ReactModule(name = BubbleNotificationsModule.NAME)
public class BubbleNotificationsModule extends ReactContextBaseJavaModule {

    public static final String NAME = "BubbleNotifications";
    private BubblesManager bubblesManager;
    private final ReactApplicationContext reactContext;
    private BubbleLayout bubbleView;
    //Layout and resources on view
    private Button reEnter;
    private LinearLayout notificationView ;
    private ImageView wridzIcon ;
    private ImageView pathIcon;
    private TextView pickUpLoc;
    private TextView dropOffLoc;
    private TextView pickUpAddr;
    private TextView dropOffAddr;
    private TextView farePrice;
    private String pickUpLocReact;
    private String pickUpAddrReact;
    private String dropOffLocReact;
    private String dropOffAddrReact;
    private String fareReact;

    public BubbleNotificationsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void showFloatingBubble(int x, int y, final Promise promise) {
        try {
            this.addNewBubble(x, y);
            promise.resolve("");
        }catch(Exception e) {
            promise.reject("");
        }
    }


    private void addNewBubble(int x, int y) {
        this.removeBubble();
        bubbleView =
          (BubbleLayout) LayoutInflater
            .from(reactContext)
            .inflate(R.layout.bubble_layout, null);
        bubbleView.setOnBubbleRemoveListener(
          new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
              bubbleView = null;
              sendEvent("floating-bubble-remove");
            }
          }
        );
        bubbleView.setOnBubbleClickListener(
          new BubbleLayout.OnBubbleClickListener() {
            @Override
            public void onBubbleClick(BubbleLayout bubble) {

              expandNotification(
                dropOffLocReact,
                dropOffAddrReact,
                pickUpLocReact,
                pickUpAddrReact,
                fareReact
                );

              sendEvent("floating-bubble-press");
            }
          }
        );
        bubbleView.setShouldStickToWall(true);
        bubblesManager.addBubble(bubbleView, x, y);
      }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          return Settings.canDrawOverlays(reactContext);
        }
        return true;
    }

    public void expandNotification(String dropOffLocation, String dropOffAddress, String pickUpLocation, String pickUpAddress, String fare) {
      //Identify all resources
      notificationView = bubbleView.findViewById(R.id.notification_layout);
      wridzIcon = bubbleView.findViewById(R.id.imageView2);
      pathIcon = bubbleView.findViewById(R.id.imageView);
      pickUpLoc = bubbleView.findViewById(R.id.pickUpLocation);
      dropOffLoc = bubbleView.findViewById(R.id.dropOffLocation);
      pickUpAddr = bubbleView.findViewById(R.id.pickUpAddress);
      dropOffAddr = bubbleView.findViewById(R.id.dropOffAddress);
      farePrice = bubbleView.findViewById(R.id.Fare);
      reEnter = (Button) bubbleView.findViewById(R.id.re_open_app);

      if (notificationView.getVisibility() == View.GONE)
      {
        //Set Resources according to what needs to be shown
        notificationView.setVisibility(View.VISIBLE);
        wridzIcon.setImageResource(R.drawable.bubble_icon);
        pathIcon.setImageResource(R.drawable.path);
        pickUpAddr.setText(pickUpAddress);
        pickUpLoc.setText(pickUpLocation);
        dropOffLoc.setText(dropOffLocation);
        dropOffAddr.setText(dropOffAddress);
        farePrice.setText(fare);

        //Set bottom Button to reopen the app on click
        reEnter.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent launchIntent = reactContext.getPackageManager().getLaunchIntentForPackage(reactContext.getPackageName());
            if (launchIntent != null) {
              reactContext.startActivity(launchIntent);
              notificationView.setVisibility(View.GONE);
            }
          }
        });
      }
      else
      {
        //Hide notification and set text back to empty
        pickUpAddr.setText("");
        pickUpLoc.setText("");
        dropOffLoc.setText("");
        dropOffAddr.setText("");
        notificationView.setVisibility(View.GONE);
      }
    }

    @ReactMethod
    public void loadData(String dropOffLocation, String dropOffAddress, String pickUpLocation, String pickUpAddress, String fare) {
      pickUpAddrReact = pickUpAddress;
      pickUpLocReact = pickUpLocation;
      dropOffAddrReact = dropOffAddress;
      dropOffLocReact = dropOffLocation;
      fareReact = fare;

      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          expandNotification(dropOffLocation, dropOffAddress, pickUpLocation, pickUpAddress, fare);
        }
      });
    }

    @ReactMethod
    public void reopenApp() {
        Intent launchIntent = reactContext
          .getPackageManager()
          .getLaunchIntentForPackage(reactContext.getPackageName());
        if (launchIntent != null) {
          reactContext.startActivity(launchIntent);
        }
    }

    @ReactMethod // Notates a method that should be exposed to React
    public void hideFloatingBubble(final Promise promise) {
        try {
          this.removeBubble();
          promise.resolve("");
        } catch (Exception e) {
          promise.reject("");
        }
    }

    @ReactMethod // Notates a method that should be exposed to React
    public void requestPermission(final Promise promise) {
        try {
          this.requestPermissionAction(promise);
        } catch (Exception e) {}
    }

    @ReactMethod // Notates a method that should be exposed to React
    public void checkPermission(final Promise promise) {
        try {
          promise.resolve(hasPermission());
        } catch (Exception e) {
          promise.reject("");
        }
    }

    @ReactMethod // Notates a method that should be exposed to React
    public void initialize(final Promise promise) {
        try {
          this.initializeBubblesManager();
          promise.resolve("");
        } catch (Exception e) {
          promise.reject("");
        }
    }


    private void removeBubble() {
        if (bubbleView != null) {
          try {
            bubblesManager.removeBubble(bubbleView);
          } catch (Exception e) {}
        }
    }

    public void requestPermissionAction(final Promise promise) {
        if (!hasPermission()) {
          Intent intent = new Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + reactContext.getPackageName())
          );
          Bundle bundle = new Bundle();
          reactContext.startActivityForResult(intent, 0, bundle);
        }
        if (hasPermission()) {
          promise.resolve("");
        } else {
          promise.reject("");
        }
    }

    private void initializeBubblesManager() {
        try {
            bubblesManager =
              new BubblesManager.Builder(reactContext)
                .setTrashLayout(R.layout.bubble_trash_layout)
                .build();
            bubblesManager.initialize();
        }catch(Exception e) {
            System.out.println(e);
        }
    }

    private void sendEvent(String eventName) {
        WritableMap params = Arguments.createMap();
        reactContext
          .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
          .emit(eventName, params);
    }

    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @ReactMethod
    public void multiply(int a, int b, Promise promise) {
        promise.resolve(a * b);
    }

    public static native int nativeMultiply(int a, int b);
}
