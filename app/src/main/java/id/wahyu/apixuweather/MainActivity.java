package id.wahyu.apixuweather;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.text.ParseException;
import java.util.List;

import id.wahyu.apixuweather.adapter.NextForeCastAdapter;
import id.wahyu.apixuweather.databinding.ActivityMainBinding;
import id.wahyu.apixuweather.model.ForeCast;
import id.wahyu.apixuweather.model.NextForeCast;
import id.wahyu.apixuweather.model.viewmodel.ForeCastViewModel;
import id.wahyu.apixuweather.util.ApiInterface;
import id.wahyu.apixuweather.util.ApiService;
import id.wahyu.apixuweather.util.BroadcastReceiver;
import id.wahyu.apixuweather.util.internet.InternetNetworkChangeReceiver;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    private static String TOKEN = ApiService.TOKEN;
    private static String CITY = "Bangalore";
    private static int DAY = 4;

    private Context context;
    private Typeface fontThin, fontBlack;
    private BroadcastReceiver broadcastReceiver;
    private ActivityMainBinding layoutBinding;
    private CompositeSubscription subscription = new CompositeSubscription();
    private ForeCastViewModel foreCastViewModel;
    private NextForeCastAdapter nextForeCastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.context = this;

        fontThin = Typeface.createFromAsset(getAssets(), "fonts/Roboto/Roboto-Thin.ttf");
        fontBlack = Typeface.createFromAsset(getAssets(), "fonts/Roboto/Roboto-Black.ttf");
        Animation rotate = AnimationUtils.loadAnimation(context, R.anim.image_rotation_animation);
        // Call Retrofit
        foreCastViewModel = new ForeCastViewModel(new ApiService(context), AndroidSchedulers.mainThread(), TOKEN, CITY, DAY);
//        foreCastViewModel.getForeCast().request().url().toString();

        // Get Intent From Broadcast Internet Device Adapter
        broadcastReceiver = new BroadcastReceiver(this, new InternetNetworkChangeReceiver(), layoutBinding);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(InternetNetworkChangeReceiver.NETWORK_AVAILABLE_ACTION));
        // For SDK > M
        broadcastReceiver.registerBroadcastInternetReceiver();

        layoutBinding.ivLoading.setAnimation(rotate);
        layoutBinding.txtErrorMessage.setTypeface(fontThin);
        layoutBinding.txtTemperature.setTypeface(fontBlack);
        layoutBinding.txtCity.setTypeface(fontThin);

        loadListKota();
    }

    public void loadListKota() {
        subscription.add(foreCastViewModel.getForeCast()
                .subscribe(new Observer<ForeCast>() {
                    @Override public void onCompleted() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                layoutBinding.clLoadingWrapper.setVisibility(View.GONE);
                            }
                        }, 1000);
                    }

                    @Override public void onError(Throwable e) {
                        layoutBinding.clErrorWrapper.setVisibility(View.VISIBLE);
                        Snackbar.make(layoutBinding.wrapper, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }

                    @Override public void onNext(ForeCast cast) {
                        try {
                            updateUi(cast.getForecast());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Snackbar.make(layoutBinding.wrapper, "Escape Date Error", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }));
    }

    private void updateUi(NextForeCast data) throws ParseException {
        List<NextForeCast.DetailBean> beans = data.getDetailNextForeCast();
        nextForeCastAdapter = new NextForeCastAdapter(context, beans);
        layoutBinding.rcNextTempList.setLayoutManager(new LinearLayoutManager(this));
        layoutBinding.rcNextTempList.setAdapter(nextForeCastAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        broadcastReceiver.unregisterNetworkChanges();
        subscription.unsubscribe();
    }
}
