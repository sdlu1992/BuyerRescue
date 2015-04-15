package lu.shaode.buyerrescue.adapter;

import android.content.Context;
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
import lu.shaode.buyerrescue.util.ViewHolder;

/**
 * Created by sdlu on 15/4/12.
 */
public class AdapterGoodsList extends BaseAdapter{

    Context context;
    List<ContentGoods.Good> list;

    public AdapterGoodsList(Context context, List<ContentGoods.Good> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
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
                    .inflate(R.layout.item_goods_list, parent, false);
        }

        NetworkImageView imageView = ViewHolder.get(convertView, R.id.item_goods_image);
        ImageLoader loader = new ImageLoader(BuyerApplication.queue, BuyerImageCache.getInstance());
        imageView.setImageUrl("https://www.baidu.com/img/bdlogo.png", loader);
        TextView tvTitle = ViewHolder.get(convertView, R.id.item_goods_title);
        TextView tvCount = ViewHolder.get(convertView, R.id.item_goods_count);
        TextView tvPrice = ViewHolder.get(convertView, R.id.item_goods_price);

        ContentGoods.Good good = getItem(position);
        tvTitle.setText(good.name);
        tvCount.setText("已售出 " + good.count + " 件");
        tvPrice.setText(good.price + " 元");

        return convertView;
    }
}
