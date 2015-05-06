package lu.shaode.buyerrescue.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.dummy.ContentCollectList;
import lu.shaode.buyerrescue.ui.dummy.ContentGoods;
import lu.shaode.buyerrescue.ui.dummy.ContentWishList;
import lu.shaode.buyerrescue.util.BuyerApplication;
import lu.shaode.buyerrescue.util.BuyerImageCache;
import lu.shaode.buyerrescue.util.StringUtil;
import lu.shaode.buyerrescue.util.ViewHolder;

/**
 * Created by sdlu on 15/4/12.
 */
public class AdapterCollectList extends BaseAdapter{

    private final String TAG = ((Object) this).getClass().getSimpleName();
    Context context;
    List<ContentCollectList.Collect> list;

    public AdapterCollectList(Context context, List<ContentCollectList.Collect> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        Log.e(TAG + " sdlu", "list.size()= " + list.size());
        return list.size();
    }

    @Override
    public ContentCollectList.Collect getItem(int position) {
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
                    .inflate(R.layout.item_goods_list, parent, false);
        }

        NetworkImageView imageView = ViewHolder.get(convertView, R.id.item_goods_image);
        ImageLoader loader = new ImageLoader(BuyerApplication.queue, BuyerImageCache.getInstance());
        imageView.setDefaultImageResId(R.drawable.pic_loading);
        imageView.setErrorImageResId(R.drawable.no_pic);
        TextView tvTitle = ViewHolder.get(convertView, R.id.item_goods_title);
        TextView tvCount = ViewHolder.get(convertView, R.id.item_goods_count);
        TextView tvPrice = ViewHolder.get(convertView, R.id.item_goods_price);

        ContentGoods.Good good = getItem(position).good;
        imageView.setImageUrl(good.imageUrlTitle, loader);
        tvTitle.setText(good.name);
        tvCount.setText("已售出 " + good.count + " 件");
        tvPrice.setText(StringUtil.getMoneyString(good.price) + context.getString(R.string.money_suffix));

        return convertView;
    }

}
