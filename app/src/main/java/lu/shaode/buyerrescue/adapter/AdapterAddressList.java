package lu.shaode.buyerrescue.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.dummy.ContentAddressList;
import lu.shaode.buyerrescue.ui.dummy.ContentAppraiseList;
import lu.shaode.buyerrescue.util.StringUtil;
import lu.shaode.buyerrescue.util.ViewHolder;

/**
 * Created by sdlu on 15/4/12.
 */
public class AdapterAddressList extends BaseAdapter{

    private final String TAG = ((Object) this).getClass().getSimpleName();
    Context context;
    List<ContentAddressList.Address> list;

    public AdapterAddressList(Context context, List<ContentAddressList.Address> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        Log.e(TAG + " sdlu", "list.size()= " + list.size());
        return list.size();
    }

    @Override
    public ContentAddressList.Address getItem(int position) {
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
                    .inflate(R.layout.item_address_list, parent, false);
        }

        ContentAddressList.Address address = getItem(position);
        TextView tvName = ViewHolder.get(convertView, R.id.item_address_name);
        TextView tvPhone = ViewHolder.get(convertView, R.id.item_address_phone);
        TextView tvDetail = ViewHolder.get(convertView, R.id.item_address_detail);
        CheckBox cbDefault = ViewHolder.get(convertView, R.id.item_address_check);

        tvName.setText(address.name);
        tvPhone.setText(address.phone);
        tvDetail.setText((address.isDefault == 1 ? "[默认]": "")+address.province+address.city+address.county+address.detail);
        cbDefault.setChecked(address.isDefault == 1);

        return convertView;
    }

}
