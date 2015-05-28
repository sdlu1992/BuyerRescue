package lu.shaode.buyerrescue.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.adapter.AdapterOrderHistoryList;
import lu.shaode.buyerrescue.ui.dummy.ContentAddressList;
import lu.shaode.buyerrescue.ui.dummy.ContentHistoryList;
import lu.shaode.buyerrescue.ui.dummy.ContentSolder;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

public class ActOrderVerify extends ActParent implements View.OnClickListener {

    private final String TAG = ((Object) this).getClass().getSimpleName();
    public final static int REQUEST_VERIFY_ORDER = 1992;
    Button btOk;
    ListView listHistory;
    AdapterOrderHistoryList mAdapter;
    TextView tvAddressName;
    TextView tvAddressPhone;
    TextView tvAddressDetail;
    TextView tvPriceTotal;
    View addressView;

    ContentSolder.Solder buyer;
    AlertDialog okDialog;
    int state = Integer.MAX_VALUE;

    JSONArray goods ;
    JSONArray wishes;

    ContentAddressList.Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String goods = getIntent().getStringExtra("goods");
        String wishes = getIntent().getStringExtra("wishes");
        try {
            this.goods = new JSONArray(goods);
            Log.e(TAG + " sdlu", "goods.toString()= " + goods.toString());
        } catch (Exception e) {
            e.printStackTrace();
            this.goods = null;
        }
        try {
            this.wishes = new JSONArray(wishes);
            Log.e(TAG + " sdlu", "wishes.toString()= " + wishes.toString());
        } catch (Exception e){
            this.wishes = null;
        }
    }

    @Override
    protected int getLayoutContent() {
        return R.layout.activity_act_order_verify;
    }

    @Override
    protected void initBodyControl() {
        super.initBodyControl();
        btOk = (Button) findViewById(R.id.act_order_verify_buy);
        btOk.setOnClickListener(this);
        tvAddressDetail = (TextView) findViewById(R.id.act_order_verify_address_detail);
        tvAddressName = (TextView) findViewById(R.id.act_order_verify_address_name);
        tvAddressPhone = (TextView) findViewById(R.id.act_order_verify_address_phone);
        addressView = (View) findViewById(R.id.act_order_verify_address_layout);
        addressView.setOnClickListener(this);
//        listHistory = (ListView) findViewById(R.id.act_order_history_list);
//        tvPriceTotal = (TextView) findViewById(R.id.act_order_total);
        getAddressList();
    }

    public void getAddressList(){
        btOk.post(new Runnable() {
            @Override
            public void run() {
                showDialogLoading();
            }
        });
        BizManager.getInstance(this).getAddressList(new AddressApiListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_order_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.act_order_verify_buy:
                order();
                break;
            case R.id.act_order_verify_address_layout:
                Intent intent = new Intent(ActOrderVerify.this, ActAddressChoose.class);
                startActivityForResult(intent, REQUEST_VERIFY_ORDER);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_VERIFY_ORDER:
                if (resultCode == RESULT_OK){
                    Log.e(TAG + " sdlu", "requestCode= " + requestCode);
                    String string = data.getStringExtra("address");
                    Log.e(TAG + " sdlu", "string= " + string);
                    try {
                        address = new ContentAddressList.Address(new JSONObject(string));
                        displayAddress();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void order() {
        showDialogLoading();
        BizManager.getInstance(this).addOrder(goods, wishes, address.id, new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.e(TAG + " sdlu", "jsonObject.toString()= " + jsonObject.toString());
                try {
                    int response = jsonObject.getInt("response");
                    switch (response){
                        case 1:
                            String orderId = jsonObject.getString("order_id");
                            Intent intent = new Intent(ActOrderVerify.this, ActOrderDetail.class);
                            intent.putExtra("order_id", orderId);
                            startActivity(intent);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dismissDialogLoading();
            }

            @Override
            public void error(String string) {
                Log.e(TAG + " sdlu", "string= " + string);
                dismissDialogLoading();
            }
        });
    }

    public void displayOrder() {
        mAdapter = new AdapterOrderHistoryList(this, ContentHistoryList.ITEMS);
//        listHistory.setAdapter(mAdapter);
//        ViewUtil.setListViewHeightBasedOnChildren(listHistory);
//        setTotalPrice();
    }

    public void setTotalPrice() {
        float total = 0;
        for (ContentHistoryList.History history : ContentHistoryList.ITEMS) {
            total += history.count * Float.parseFloat(history.price);
        }
        tvPriceTotal.setText("总价: " + total + "元");
    }

    class AddressApiListener implements ApiListener{

        @Override
        public void success(JSONObject jsonObject) {
            Log.e(TAG + " sdlu", "jsonObject= " + jsonObject.toString());
            int responseCode = 2;
            try {
                responseCode = jsonObject.getInt("response");
                switch (responseCode){
                    case 1:
                        ContentAddressList.ITEMS.clear();
                        int length = jsonObject.getInt("len");
                        if (length != 0){
                            JSONArray addresses= jsonObject.getJSONArray("address_list");
                            for (int i = 0; i < addresses.length(); i++){
                                JSONObject foo = addresses.getJSONObject(i);
                                ContentAddressList.Address address = new ContentAddressList.Address(foo);
                                if (address.isDefault == 1){
                                    ActOrderVerify.this.address = address;
                                    break;
                                }
                            }
                            displayAddress();
                        } else{
                            tvAddressName.setText(R.string.please_add_address);
                        }
                        break;
                    case 2:
                        break;
                }

                dismissDialogLoading();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void error(String string) {
            Log.e(TAG + " sdlu", "string= " + string);
            dismissDialogLoading();

        }
    }

    public void displayAddress(){
        tvAddressDetail.setText(getString(R.string.address_address_prefix)+address.province+address.city+address.county);
        tvAddressName.setText(getString(R.string.address_name_prefix)+address.name);
        tvAddressPhone.setText(address.phone);
    }
}
