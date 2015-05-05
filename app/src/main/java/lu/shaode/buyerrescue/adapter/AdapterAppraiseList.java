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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.dummy.ContentAppraiseList;
import lu.shaode.buyerrescue.ui.dummy.ContentHistoryList;
import lu.shaode.buyerrescue.util.BuyerApplication;
import lu.shaode.buyerrescue.util.BuyerImageCache;
import lu.shaode.buyerrescue.util.StringUtil;
import lu.shaode.buyerrescue.util.ViewHolder;

/**
 * Created by sdlu on 15/4/12.
 */
public class AdapterAppraiseList extends BaseAdapter{

    private final String TAG = ((Object) this).getClass().getSimpleName();
    Context context;
    List<ContentAppraiseList.Appraise> list;
    List<ContentAppraiseList.Appraise> displayList;
    boolean isGoodChecked = false;
    boolean isMiddleChecked = false;
    boolean isBadChecked = false;

    public AdapterAppraiseList(Context context, List<ContentAppraiseList.Appraise> list) {
        this.context = context;
        this.list = list;
        getDisplayList();
    }

    @Override
    public int getCount() {
        Log.e(TAG + " sdlu", "list.size()= " + displayList.size());
        return displayList.size();
    }

    @Override
    public ContentAppraiseList.Appraise getItem(int position) {
        return displayList.get(position);
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
                    .inflate(R.layout.item_appraise_list, parent, false);
        }

        TextView tvDate = ViewHolder.get(convertView, R.id.item_appraise_date);
        TextView tvContent = ViewHolder.get(convertView, R.id.item_appraise_content);
        TextView tvUsername = ViewHolder.get(convertView, R.id.item_appraise_username);
        TextView tvType = ViewHolder.get(convertView, R.id.item_appraise_type);

        ContentAppraiseList.Appraise appraise = getItem(position);
        tvDate.setText(context.getString(R.string.appraise_date_prefix) + appraise.date.substring(0, 19));
        if (StringUtil.isNullOrEmpty(appraise.content)){
            tvContent.setText(R.string.no_appraise_content);
        } else {
            tvContent.setText(appraise.content);
        }
        tvUsername.setText(appraise.username);
        tvType.setText(context.getResources().getStringArray(R.array.appraise_type)[appraise.type]);
        return convertView;
    }

    public List<ContentAppraiseList.Appraise> getDisplayList(){
        if (!(isBadChecked || isGoodChecked || isMiddleChecked)){
            displayList = list;
        } else {
            displayList = new ArrayList<>();
            for (ContentAppraiseList.Appraise appraise : list){
                if ((appraise.type == 0 && isGoodChecked)
                        ||(appraise.type == 1 && isMiddleChecked)
                        ||(appraise.type == 2 && isBadChecked)) {
                    displayList.add(appraise);
                }
            }
        }
        return displayList;
    }

    public void setGoodChecked(boolean isGoodChecked) {
        this.isGoodChecked = isGoodChecked;
        getDisplayList();
    }

    public void setMiddleChecked(boolean isMiddleChecked) {
        this.isMiddleChecked = isMiddleChecked;
        getDisplayList();
    }

    public void setBadChecked(boolean isBadChecked) {
        this.isBadChecked = isBadChecked;
        getDisplayList();
    }

    public boolean isGoodChecked() {
        return isGoodChecked;
    }

    public boolean isMiddleChecked() {
        return isMiddleChecked;
    }

    public boolean isBadChecked() {
        return isBadChecked;
    }
}
