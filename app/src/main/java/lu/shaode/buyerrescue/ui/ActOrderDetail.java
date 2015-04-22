package lu.shaode.buyerrescue.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import lu.shaode.buyerrescue.ui.dummy.ContentGoods;
import lu.shaode.buyerrescue.ui.dummy.ContentHistoryList;
import lu.shaode.buyerrescue.ui.dummy.ContentOrderList;
import lu.shaode.buyerrescue.ui.dummy.ContentSolder;
import lu.shaode.buyerrescue.ui.dummy.ContentStore;
import lu.shaode.buyerrescue.util.ViewUtil;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

public class ActOrderDetail extends ActParent implements View.OnClickListener {

    private final String TAG = ((Object) this).getClass().getSimpleName();

    Button btPay;
    Button btRefund;
    ListView listHistory;
    AdapterOrderHistoryList mAdapter;
    TextView tvState;
    TextView tvAddress;
    TextView tvBuyer;
    TextView tvId;
    TextView tvDate;
    TextView tvPriceTotal;

    String orderId;

    ContentOrderList.Order order;
    ContentSolder.Solder buyer;

    AlertDialog buyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderId = getIntent().getStringExtra("order_id");
        Log.e(TAG + " sdlu", "orderId= " + orderId);
        getOrder();
    }

    @Override
    protected int getLayoutContent() {
        return R.layout.activity_act_order_detail;
    }

    @Override
    protected void initBodyControl() {
        super.initBodyControl();
        btPay = (Button) findViewById(R.id.act_order_buy);
        btRefund = (Button) findViewById(R.id.act_order_refund);
        btPay.setOnClickListener(this);
        btRefund.setOnClickListener(this);
        listHistory = (ListView) findViewById(R.id.act_order_history_list);
        tvState = (TextView) findViewById(R.id.act_order_state);
        tvAddress = (TextView) findViewById(R.id.act_order_address);
        tvBuyer = (TextView) findViewById(R.id.act_order_buyer);
        tvId = (TextView) findViewById(R.id.act_order_id);
        tvDate = (TextView) findViewById(R.id.act_order_date);
        tvPriceTotal = (TextView) findViewById(R.id.act_order_total);

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
            case R.id.act_order_buy:
                showBuyDialog();
                break;
            case R.id.act_order_refund:
                break;
        }
    }

    public void getOrder() {
        showDialogLoading();
        BizManager.getInstance(this).getOrder(orderId, new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.e(TAG + " sdlu", "jsonObject.toString()= " + jsonObject.toString());
                try {
                    int response = jsonObject.getInt("response");
                    switch (response) {
                        case 1:
                            order = new ContentOrderList.Order(jsonObject.getJSONObject("order"));
                            buyer = new ContentSolder.Solder(jsonObject.getJSONObject("buyer"));
                            JSONArray historyArray = jsonObject.getJSONArray("history_list");
                            for (int i = 0; i < historyArray.length(); i++) {
                                JSONObject historyJson = historyArray.getJSONObject(i);
                                JSONObject goodJson = historyJson.getJSONObject("good");
                                JSONObject storeJson = historyJson.getJSONObject("store");
                                JSONObject solderJson = historyJson.getJSONObject("solder");
                                ContentGoods.Good good = new ContentGoods.Good(goodJson);
                                ContentSolder.Solder solder = new ContentSolder.Solder(solderJson);
                                ContentStore.Store store = new ContentStore.Store(storeJson);
                                store.setSolder(solder);
                                ContentHistoryList.History history = new ContentHistoryList.History(historyJson);
                                good.setStore(store);
                                history.setGood(good);
                                order.addHistory(history);
                            }
                            displayOrder();
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

    public void pay() {
        showDialogLoading();
        BizManager.getInstance(this).pay(order.id, new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.e(TAG + " sdlu", "jsonObject.toString()= " + jsonObject.toString());
                dismissDialogLoading();
                try {
                    int response = jsonObject.getInt("response");
                    switch (response){
                        case 1:
                            showToastMessage(getString(R.string.pay_success));
                            finish();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String string) {
                Log.e(TAG + " sdlu", "string= " + string);
                dismissDialogLoading();
            }
        });
    }

    public void displayOrder() {
        mAdapter = new AdapterOrderHistoryList(this, order.getHistories());
        listHistory.setAdapter(mAdapter);
        ViewUtil.setListViewHeightBasedOnChildren(listHistory);
        tvState.setText(getResources().getStringArray(R.array.state_order)[order.getHistories().get(0).state]);
        tvAddress.setText(buyer.address);
        tvDate.setText(getString(R.string.order_date_prefix) + order.date.substring(0, 15));
        tvId.setText(getString(R.string.order_id_prefix) + order.id);
        tvBuyer.setText(getString(R.string.order_buyer_prefix) + buyer.name);
        setTotalPrice();
    }

    public void setTotalPrice() {
        float total = 0;
        for (ContentHistoryList.History history : order.getHistories()) {
            total += history.count * Float.parseFloat(history.price);
        }
        tvPriceTotal.setText("总价: " + total + "元");
    }

    public void showBuyDialog() {
        if (buyDialog == null) {
            buyDialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.is_pay))
                    .setPositiveButton(getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pay();
                                    dismissBuyDialog();
                                }
                            })
                    .setNegativeButton(getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dismissBuyDialog();
                                }
                            }).create();
        }
        buyDialog.show();
    }

    public void dismissBuyDialog() {
        if (buyDialog != null && buyDialog.isShowing()) {
            buyDialog.dismiss();
        }
    }
}
