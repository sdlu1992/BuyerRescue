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
import lu.shaode.buyerrescue.util.BuyerApplication;
import lu.shaode.buyerrescue.util.BuyerImageCache;
import lu.shaode.buyerrescue.util.StringUtil;
import lu.shaode.buyerrescue.util.ViewHolder;

/**
 * Created by sdlu on 15/4/12.
 */
public class AdapterHomeList extends BaseAdapter{

    private final String TAG = ((Object) this).getClass().getSimpleName();
    Context context;
    List<ContentGoods.Good> list;

    public AdapterHomeList(Context context, List<ContentGoods.Good> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        Log.e(TAG + " sdlu", "list.size()= " + list.size());
        return list.size();
    }

    @Override
    public ContentGoods.Good getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_home_list, parent, false);
        }

        NetworkImageView imageView = ViewHolder.get(convertView, R.id.item_home_image);
        ImageLoader loader = new ImageLoader(BuyerApplication.queue, BuyerImageCache.getInstance());
        imageView.setDefaultImageResId(R.drawable.pic_loading);
        imageView.setErrorImageResId(R.drawable.no_pic);
        TextView tvTitle = ViewHolder.get(convertView, R.id.item_home_title);
        TextView tvPrice = ViewHolder.get(convertView, R.id.item_home_price);

        ContentGoods.Good good = getItem(position);
        imageView.setImageUrl(good.imageUrlTitle, loader);
        tvTitle.setText(good.name);
        tvPrice.setText(StringUtil.getMoneyString(good.price) + context.getString(R.string.money_suffix));

        return convertView;
    }
}
