package lu.shaode.buyerrescue.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.dummy.ContentHistoryList;
import lu.shaode.buyerrescue.util.BuyerApplication;
import lu.shaode.buyerrescue.util.BuyerImageCache;
import lu.shaode.buyerrescue.util.StringUtil;
import lu.shaode.buyerrescue.util.ViewHolder;

/**
 * Created by sdlu on 15/4/12.
 */
public class AdapterHistoryList extends BaseAdapter{

    private final String TAG = ((Object) this).getClass().getSimpleName();
    Context context;
    List<ContentHistoryList.History> list;
    private OnItemButtonClick mListener;

    public AdapterHistoryList(Context context, List<ContentHistoryList.History> list, OnItemButtonClick mListener) {
        this.context = context;
        this.list = list;
        this.mListener = mListener;
    }

    @Override
    public int getCount() {
        Log.e(TAG + " sdlu", "list.size()= " + list.size());
        return list.size();
    }

    @Override
    public ContentHistoryList.History getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e(TAG + " sdlu", "getView position= " + position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_history_list, parent, false);
        }

        NetworkImageView imageView = ViewHolder.get(convertView, R.id.item_history_image);
        ImageLoader loader = new ImageLoader(BuyerApplication.queue, BuyerImageCache.getInstance());
        imageView.setDefaultImageResId(R.drawable.pic_loading);
        imageView.setErrorImageResId(R.drawable.no_pic);
        TextView tvTitle = ViewHolder.get(convertView, R.id.item_history_title);
        TextView tvCount = ViewHolder.get(convertView, R.id.item_history_count);
        TextView tvPrice = ViewHolder.get(convertView, R.id.item_history_price);
        TextView tvTotal = ViewHolder.get(convertView, R.id.item_history_price_total);
        TextView tvState = ViewHolder.get(convertView, R.id.item_history_state);
        TextView tvDate = ViewHolder.get(convertView, R.id.item_history_date);
        Button button = ViewHolder.get(convertView, R.id.item_history_button);
        Button btRefund = ViewHolder.get(convertView, R.id.item_history_bt_refund);

        ContentHistoryList.History history = getItem(position);
        imageView.setImageUrl(history.good.imageUrlTitle, loader);
        tvTitle.setText(history.good.name);
        tvPrice.setText(StringUtil.getMoneyString(history.good.price) + context.getString(R.string.money_suffix));
        tvCount.setText("x "+history.count);
        tvDate.setText(context.getString(R.string.order_date_prefix) + "\n" + history.date.substring(0, 19));
        float total = history.count * Float.parseFloat(history.good.price);
        tvTotal.setText(context.getString(R.string.total_price_prefix)
                + StringUtil.getMoneyString(total)
                + context.getString(R.string.money_suffix));
        tvState.setText(context.getResources().getStringArray(R.array.state_order)[history.state]);
        switch (history.state){
            case 0:
                button.setVisibility(View.VISIBLE);
                button.setText(context.getString(R.string.pay_now));
                btRefund.setVisibility(View.GONE);
                break;
            case 2:
                button.setVisibility(View.VISIBLE);
                button.setText(context.getString(R.string.affirm_take_good));
                btRefund.setVisibility(View.GONE);
                break;
            case 3:
                button.setVisibility(View.VISIBLE);
                button.setText(context.getString(R.string.appraise_order));
                break;
            case 1:
                btRefund.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                break;
            case 4:
                btRefund.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                break;
            case 5:
                btRefund.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
            default:
                btRefund.setVisibility(View.GONE);
                break;
        }
        button.setOnClickListener(new OnButtonClickListener(history, position));
        btRefund.setOnClickListener(new OnButtonClickListener(history, position));
        return convertView;
    }

    class OnButtonClickListener implements View.OnClickListener{

        ContentHistoryList.History history;
        int position;

        OnButtonClickListener(ContentHistoryList.History history, int position) {
            this.history = history;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_history_button:
                    mListener.onButtonClick(history, position);
                    break;
                case R.id.item_history_bt_refund:
                    mListener.onRefundButtonClick(history, position);
                    break;
            }
        }
    }

    public interface OnItemButtonClick {
        public void onButtonClick(ContentHistoryList.History history, int position);
        public void onRefundButtonClick(ContentHistoryList.History history, int position);
    }
}
