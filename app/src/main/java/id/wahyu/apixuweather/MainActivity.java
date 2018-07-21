package id.wahyu.apixuweather;

import android.content.Context;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import id.wahyu.apixuweather.databinding.ActivityMainBinding;
import id.wahyu.apixuweather.util.BroadcastReceiver;
import id.wahyu.apixuweather.util.internet.InternetNetworkChangeReceiver;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private Typeface fontThin, fontReg, fontBlack;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.context = this;

        fontThin = Typeface.createFromAsset(getAssets(), "fonts/Roboto/Roboto-Thin.ttf");
        fontReg = Typeface.createFromAsset(getAssets(), "fonts/Roboto/Roboto-Regular.ttf");
        fontBlack = Typeface.createFromAsset(getAssets(), "fonts/Roboto/Roboto-Black.ttf");
        Animation rotate = AnimationUtils.loadAnimation(context, R.anim.image_rotation_animation);

        // Get Intent From Broadcast Internet Device Adapter
        broadcastReceiver = new BroadcastReceiver(this, new InternetNetworkChangeReceiver(), layoutBinding);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(InternetNetworkChangeReceiver.NETWORK_AVAILABLE_ACTION));
        // For SDK > M
        broadcastReceiver.registerBroadcastInternetReceiver();

        layoutBinding.ivLoading.setAnimation(rotate);
        layoutBinding.txtErrorMessage.setTypeface(fontThin);
        layoutBinding.txtTemperature.setTypeface(fontBlack);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        broadcastReceiver.unregisterNetworkChanges();
    }
}
