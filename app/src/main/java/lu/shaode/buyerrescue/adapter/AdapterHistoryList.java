package lu.shaode.buyerrescue.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.dummy.ContentHistoryList;
import lu.shaode.buyerrescue.ui.dummy.ContentWishList;
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
    private OnCountChange mListener;

    public AdapterHistoryList(Context context, List<ContentHistoryList.History> list, OnCountChange mListener) {
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
        imageView.setImageUrl("https://www.baidu.com/img/bdlogo.png", loader);
        TextView tvTitle = ViewHolder.get(convertView, R.id.item_history_title);
        TextView tvCount = ViewHolder.get(convertView, R.id.item_history_count);
        TextView tvPrice = ViewHolder.get(convertView, R.id.item_history_price);
        TextView tvTotal = ViewHolder.get(convertView, R.id.item_history_price_total);
        TextView tvState = ViewHolder.get(convertView, R.id.item_history_state);
        Button button = ViewHolder.get(convertView, R.id.item_history_button);
        Button btRefund = ViewHolder.get(convertView, R.id.item_history_bt_refund);

        ContentHistoryList.History history = getItem(position);
        tvTitle.setText(history.good.name);
        tvPrice.setText(StringUtil.getMoneyString(history.good.price) + context.getString(R.string.money_suffix));
        tvCount.setText("x "+history.count);
        float total = history.count * Float.parseFloat(history.good.price);
        tvTotal.setText(context.getString(R.string.total_price_prefix)
                + StringUtil.getMoneyString(total)
                + context.getString(R.string.money_suffix));
        tvState.setText(context.getResources().getStringArray(R.array.state_order)[history.state]);
        switch (history.state){
            case 0:
                button.setVisibility(View.VISIBLE);
                button.setText(context.getString(R.string.pay_now));
                break;
            case 2:
                button.setVisibility(View.VISIBLE);
                button.setText(context.getString(R.string.affirm_take_good));
                break;
            case 3:
                button.setVisibility(View.VISIBLE);
                button.setText(context.getString(R.string.appraise_order));
                //its no break on purpose
            case 1:
            case 4:
                btRefund.setVisibility(View.VISIBLE);
                break;
            default:
        }
        return convertView;
    }

    public interface OnCountChange{
    }
}
