package lu.shaode.buyerrescue.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.dummy.ContentAddressList;

public class ActAddressDetail extends ActParent{

    private final String TAG = ((Object) this).getClass().getSimpleName();
    ContentAddressList.Address address;
    TextView tvName;
    TextView tvPhone;
    TextView tvZip;
    TextView tvArea;
    TextView tvDetail;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutContent() {
        return R.layout.activity_act_address_detail;
    }

    @Override
    protected void initBodyControl() {
        super.initBodyControl();
        String string = getIntent().getStringExtra("address");
        Log.e(TAG + " sdlu", "address= " + string);
        try {
            address = new ContentAddressList.Address(new JSONObject(string));
        } catch (JSONException e) {
            finish();
        }
        tvName = (TextView) findViewById(R.id.act_address_detail_name);
        tvPhone = (TextView) findViewById(R.id.act_address_detail_phone);
        tvZip = (TextView) findViewById(R.id.act_address_detail_zip);
        tvArea = (TextView) findViewById(R.id.act_address_detail_area);
        tvDetail = (TextView) findViewById(R.id.act_address_detail_detail);
        button = (Button) findViewById(R.id.act_address_detail_default_set);
        if (address.isDefault == 0){
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
        tvName.setText(address.name);
        tvPhone.setText(address.phone);
        tvZip.setText(address.zip);
        tvArea.setText(address.province+address.city+address.county);
        tvDetail.setText(address.detail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_address_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
