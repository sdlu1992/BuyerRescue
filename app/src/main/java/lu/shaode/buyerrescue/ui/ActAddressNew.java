package lu.shaode.buyerrescue.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.BatchUpdateException;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.dialog.DialogAddressPicker;
import lu.shaode.buyerrescue.util.StringUtil;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

public class ActAddressNew extends ActParent implements View.OnClickListener{

    EditText etName;
    EditText etPhone;
    EditText etZip;
    EditText etSimple;
    EditText etDetail;
    Button btSave;

    String province;
    String city;
    String county;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutContent() {
        return R.layout.activity_act_address_new;
    }

    @Override
    protected void initBodyControl() {
        super.initBodyControl();
        etName = (EditText) findViewById(R.id.act_address_new_name);
        etPhone = (EditText) findViewById(R.id.act_address_new_phone);
        etZip = (EditText) findViewById(R.id.act_address_new_zip);
        etSimple = (EditText) findViewById(R.id.act_address_new_province);
        etSimple.setOnClickListener(this);
        etDetail = (EditText) findViewById(R.id.act_address_new_detail);
        btSave = (Button) findViewById(R.id.act_address_new_save);
        btSave.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_address_new, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_address_new_province:
                DialogAddressPicker dialogAddressPicker = new DialogAddressPicker(this);
                dialogAddressPicker.setOnOkListener(new OnOkClickListener());
                dialogAddressPicker.show();
                break;
            case R.id.act_address_new_save:
                save();
                break;
        }
    }

    class OnOkClickListener implements DialogAddressPicker.OnOkListener{

        @Override
        public void ok(String province, String city, String county) {
            ActAddressNew.this.province = province;
            ActAddressNew.this.city = city;
            ActAddressNew.this.county = county;
            if (province.equals(city)){
                etSimple.setText(province+county);
            } else {
                etSimple.setText(province+city+county);
            }
        }
    }

    public void save(){
        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        String zip = etZip.getText().toString();
        String detail = etDetail.getText().toString();
        String simple = etSimple.getText().toString();
        if (StringUtil.isNullOrEmpty(name)){
            showToastMessage(R.string.cannot_empty_address_name);
        } else if (StringUtil.isNullOrEmpty(phone)){
            showToastMessage(R.string.cannot_empty_phone);
        } else if (StringUtil.isNullOrEmpty(zip)){
            showToastMessage(R.string.cannot_empty_address_zip);
        } else if (StringUtil.isNullOrEmpty(simple)){
            showToastMessage(R.string.cannot_empty_address_simple);
        } else if (StringUtil.isNullOrEmpty(detail)){
            showToastMessage(R.string.cannot_empty_address_detail);
        }
        showDialogLoading();
        BizManager.getInstance(this).addAddress(name,phone,zip,province,city,county,detail, new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                try {
                    int response = jsonObject.getInt("response");
                    switch (response){
                        case 1:
                            showToastMessage(R.string.add_address_success);
                            finish();
                            break;
                        default:
                            showToastMessage(response+"");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissDialogLoading();
            }

            @Override
            public void error(String string) {
                showToastMessage(string);
                dismissDialogLoading();
            }
        });
    }

}
