package id.wahyu.apixuweather;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import id.wahyu.apixuweather.model.CurrentForeCast;
import id.wahyu.apixuweather.model.ForeCast;
import id.wahyu.apixuweather.model.NextForeCast;
import id.wahyu.apixuweather.model.view.NextForeCastView;
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
    private static int DAY = 6;

    private Context context;
    private Typeface fontThin, fontBlack;
    private BroadcastReceiver broadcastReceiver;
    private ActivityMainBinding layoutBinding;
    private CompositeSubscription subscription = new CompositeSubscription();
    private ForeCastViewModel foreCastViewModel;
    private NextForeCastAdapter nextForeCastAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.context = this;

        fontThin = Typeface.createFromAsset(getAssets(), "fonts/Roboto/Roboto-Thin.ttf");
        fontBlack = Typeface.createFromAsset(getAssets(), "fonts/Roboto/Roboto-Black.ttf");
        swipeRefreshLayout = layoutBinding.swipe;
        Animation rotate = AnimationUtils.loadAnimation(context, R.anim.image_rotation_animation);
        // Call Retrofit
        foreCastViewModel = new ForeCastViewModel(new ApiService(context), AndroidSchedulers.mainThread(), TOKEN, CITY, DAY);

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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layoutBinding.ivLoading.setVisibility(View.VISIBLE);
                layoutBinding.clContentWrapper.setVisibility(View.GONE);
                layoutBinding.clErrorWrapper.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                    }
                }, 2000);
            }
        });

        layoutBinding.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 2000);
    }

    public void loadData() {
        subscription.add(foreCastViewModel.getForeCast()
                .subscribe(new Observer<ForeCast>() {
                    @Override public void onCompleted() {
                        layoutBinding.ivLoading.setVisibility(View.GONE);
                        layoutBinding.clContentWrapper.setVisibility(View.VISIBLE);
                        layoutBinding.clErrorWrapper.setVisibility(View.GONE);
                    }

                    @Override public void onError(Throwable e) {
                        layoutBinding.ivLoading.setVisibility(View.GONE);
                        layoutBinding.clContentWrapper.setVisibility(View.GONE);
                        layoutBinding.clErrorWrapper.setVisibility(View.VISIBLE);
                        Snackbar.make(layoutBinding.wrapper, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }

                    @Override public void onNext(ForeCast cast) {
                        try {
                            layoutBinding.ivLoading.setVisibility(View.GONE);
                            layoutBinding.clContentWrapper.setVisibility(View.VISIBLE);
                            layoutBinding.clErrorWrapper.setVisibility(View.GONE);
                            updateHeader(cast.getCurrent(), cast.getLocation());
                            updateList(cast.getForecast());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Snackbar.make(layoutBinding.wrapper, "Escape Date Error", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }));

        swipeRefreshLayout.setRefreshing(false);
    }

    private void updateList(NextForeCast data) throws ParseException {
        List<NextForeCast.DetailBean> beans = data.getDetailNextForeCast();
        nextForeCastAdapter = new NextForeCastAdapter(context, beans);
        layoutBinding.rcNextTempList.setLayoutManager(new LinearLayoutManager(this));
        layoutBinding.rcNextTempList.setAdapter(nextForeCastAdapter);
    }

    private void updateHeader(CurrentForeCast temp, CurrentForeCast city){
        double d = Double.parseDouble(temp.getCurrent_temperature());
        String s = String.valueOf((int) Math.ceil(d))+"\u00B0";
        layoutBinding.txtTemperature.setText(s);
        layoutBinding.txtCity.setText(city.getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        broadcastReceiver.unregisterNetworkChanges();
        subscription.unsubscribe();
    }
}
