package id.wahyu.apixuweather;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import id.wahyu.apixuweather.util.BroadcastReceiver;
import id.wahyu.apixuweather.util.internet.InternetNetworkChangeReceiver;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        ImageView ivLoading = findViewById(R.id.iv_loading);

        // Get Intent From Broadcast Internet Device Adapter
        broadcastReceiver = new BroadcastReceiver(this, new InternetNetworkChangeReceiver());
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(InternetNetworkChangeReceiver.NETWORK_AVAILABLE_ACTION));
        broadcastReceiver.registerBroadcastInternetReceiver();

        Animation rotate = AnimationUtils.loadAnimation(context, R.anim.image_rotation_animation);
        ivLoading.setAnimation(rotate);
    }
}
