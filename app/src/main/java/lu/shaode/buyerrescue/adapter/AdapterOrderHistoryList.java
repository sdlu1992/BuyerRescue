package lu.shaode.buyerrescue.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.dummy.ContentGoods;
import lu.shaode.buyerrescue.ui.dummy.ContentHistoryList;
import lu.shaode.buyerrescue.util.BuyerApplication;
import lu.shaode.buyerrescue.util.BuyerImageCache;
import lu.shaode.buyerrescue.util.StringUtil;
import lu.shaode.buyerrescue.util.ViewHolder;
import lu.shaode.netsupport.ApiConfig;

/**
 * Created by sdlu on 15/4/12.
 */
public class AdapterOrderHistoryList extends BaseAdapter{

    private final String TAG = ((Object) this).getClass().getSimpleName();
    Context context;
    List<ContentHistoryList.History> list;

    public AdapterOrderHistoryList(Context context, List<ContentHistoryList.History> list) {
        this.context = context;
        this.list = list;
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
                    .inflate(R.layout.item_order_detail_list, parent, false);
        }

        NetworkImageView imageView = ViewHolder.get(convertView, R.id.item_order_image);
        ImageLoader loader = new ImageLoader(BuyerApplication.queue, BuyerImageCache.getInstance());
        imageView.setDefaultImageResId(R.drawable.no_pic);
        TextView tvTitle = ViewHolder.get(convertView, R.id.item_order_title);
        TextView tvCount = ViewHolder.get(convertView, R.id.item_order_count);
        TextView tvPrice = ViewHolder.get(convertView, R.id.item_order_price);
        TextView tvStore = ViewHolder.get(convertView, R.id.item_order_store);
        TextView tvStorePhone = ViewHolder.get(convertView, R.id.item_order_store_phone);

        ContentHistoryList.History history = getItem(position);
        imageView.setImageUrl(ApiConfig._DOMAIN_ROOT + history.good.imageUrlTitle, loader);
        tvTitle.setText(history.good.name);
        tvPrice.setText(StringUtil.getMoneyString(history.good.price) + context.getString(R.string.money_suffix));
        tvCount.setText("x "+history.count);
        tvStore.setText(history.good.store.name);
        tvStorePhone.setText(history.good.store.solder.phone);
        return convertView;
    }

}
