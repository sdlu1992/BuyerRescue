package lu.shaode.buyerrescue.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.FragmentWishList;
import lu.shaode.buyerrescue.ui.dummy.ContentWishList;
import lu.shaode.buyerrescue.util.BuyerApplication;
import lu.shaode.buyerrescue.util.BuyerImageCache;
import lu.shaode.buyerrescue.util.ViewHolder;

/**
 * Created by sdlu on 15/4/12.
 */
public class AdapterWishList extends BaseAdapter{

    private final String TAG = ((Object) this).getClass().getSimpleName();
    Context context;
    List<ContentWishList.Wish> list;
    private OnCountChange mListener;

    public AdapterWishList(Context context, List<ContentWishList.Wish> list, OnCountChange mListener) {
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
    public ContentWishList.Wish getItem(int position) {
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
                    .inflate(R.layout.item_wish_list, parent, false);
        }

        NetworkImageView imageView = ViewHolder.get(convertView, R.id.item_wish_image);
        ImageLoader loader = new ImageLoader(BuyerApplication.queue, BuyerImageCache.getInstance());
        imageView.setImageUrl("https://www.baidu.com/img/bdlogo.png", loader);
        TextView tvTitle = ViewHolder.get(convertView, R.id.item_wish_title);
        NumberPicker npCount = ViewHolder.get(convertView, R.id.item_wish_count);
        TextView tvPrice = ViewHolder.get(convertView, R.id.item_wish_price);
        CheckBox checkBox = ViewHolder.get(convertView, R.id.item_wish_check);

        ContentWishList.Wish wish = getItem(position);
        tvTitle.setText(wish.good.name);
//        resizeNumberPicker(npCount);
        // 显示气泡
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        npCount.measure(w, h);
        npCount.getLayoutParams().height = (int)(npCount.getMeasuredHeight()*0.7);
        npCount.setMaxValue(100);
        npCount.setMinValue(0);
        npCount.setValue(wish.count);
        npCount.setOnValueChangedListener(new OnCountChangeListener(position));
        tvPrice.setText(wish.good.price + " 元");
        checkBox.setChecked(wish.isCheck());
        checkBox.setOnCheckedChangeListener(new OnCheckBoxListener(position));
        return convertView;
    }

    class OnCountChangeListener implements NumberPicker.OnValueChangeListener{

        int position;
        OnCountChangeListener(int position) {
            this.position = position;
        }

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            ContentWishList.ITEMS.get(position).count = newVal;
            mListener.onCountChange(position);
        }
    }

    class OnCheckBoxListener implements CompoundButton.OnCheckedChangeListener{
        int position;

        OnCheckBoxListener(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ContentWishList.ITEMS.get(position).setCheck(isChecked);
            mListener.onCheckChange(position);
        }
    }

    public interface OnCountChange{
        public void onCountChange(int position);
        public void onCheckChange(int position);
    }
}
