package id.wahyu.apixuweather.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import id.wahyu.apixuweather.R;
import id.wahyu.apixuweather.databinding.LayoutForecastItemBinding;
import id.wahyu.apixuweather.model.NextForeCast;
import id.wahyu.apixuweather.model.view.NextForeCastView;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public class NextForeCastAdapter extends RecyclerView.Adapter<NextForeCastAdapter.ViewHolder> {
    private List<NextForeCastView> foreCasts;
    private Context context;
    private Typeface fontReg;

    public NextForeCastAdapter(Context c, List<NextForeCast.DetailBean> data) throws ParseException {
        this.foreCasts = new ArrayList<>();
        this.context = c;
        fontReg = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-Regular.ttf");
        for (NextForeCast.DetailBean dataBean : data) {
            String day = dataBean.getDate();
            double d = Double.parseDouble(dataBean.getDayTemperature().getAverageTemperature());
            foreCasts.add(new NextForeCastView(day, String.valueOf((int) Math.ceil(d))));
        }
        Log.d("FORECAST", String.valueOf(foreCasts));
        Log.d("DATABEAN", String.valueOf(data));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        LayoutForecastItemBinding viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_forecast_item, parent, false);
        viewDataBinding.txtDay.setTypeface(fontReg);
        viewDataBinding.txtNextTemp.setTypeface(fontReg);
        return new ViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.viewDataBinding.setForecast(foreCasts.get(position));
    }

    @Override
    public int getItemCount() {
        return (null != foreCasts ? foreCasts.size() : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutForecastItemBinding viewDataBinding;

        private ViewHolder(LayoutForecastItemBinding binding) {
            super(binding.getRoot());
            this.viewDataBinding = binding;
        }

        public LayoutForecastItemBinding getViewDataBinding() {
            return viewDataBinding;
        }
    }
}
