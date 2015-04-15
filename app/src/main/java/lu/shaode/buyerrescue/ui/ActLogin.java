package lu.shaode.buyerrescue.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lu.shaode.buyerrescue.R;
import lu.shaode.netsupport.AppConfigCache;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

public class ActLogin extends ActParent implements View.OnClickListener{

    private final String TAG = ((Object) this).getClass().getSimpleName();
    public static final int         REQUEST_REGISTER = 1992;
    EditText    etPhone;
    EditText    etPwd;
    Button      btRegister;
    Button      btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutContent() {
        return R.layout.activity_act_login;
    }

    @Override
    protected void initBodyControl() {
        super.initBodyControl();
        etPhone = (EditText) findViewById(R.id.act_login_et_phone);
        etPwd = (EditText) findViewById(R.id.act_login_et_pwd);
        btRegister = (Button) findViewById(R.id.act_login_bt_register);
        btLogin = (Button) findViewById(R.id.act_login_bt_login);

        btRegister.setOnClickListener(this);
        btLogin.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_login, menu);
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
            case R.id.act_login_bt_register:
                Intent intent = new Intent(ActLogin.this, ActRegister.class);
                startActivityForResult(intent, REQUEST_REGISTER);
                break;
            case R.id.act_login_bt_login:
                login();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_REGISTER:
                if (resultCode == RESULT_OK){
                    etPhone.setText(data.getStringExtra("phone"));
                    etPwd.setText(data.getStringExtra("password"));
                }
        }
    }

    private void startActivity(Class clazz){
        Intent intent = new Intent(ActLogin.this, clazz);
        startActivity(intent);
    }

    private void login(){
        final String phone = etPhone.getText().toString();
        final String pwd = etPwd.getText().toString();
        if (phone.equals("")){
            Toast.makeText(this, getString(R.string.cannot_empty_phone), Toast.LENGTH_SHORT).show();
            return;
        } else if (pwd.equals("")){
            Toast.makeText(this, getString(R.string.cannot_empty_pwd), Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd.length() < 6){
            Toast.makeText(this, getString(R.string.pwd_more_6), Toast.LENGTH_SHORT).show();
            return;
        }
        showDialogLoading();
        BizManager bizManager = BizManager.getInstance(getApplicationContext());
        bizManager.login(phone, pwd, new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.e(TAG + "sdlu jsonObject.toString() = ", jsonObject.toString());
                int responseCode = 2;
                dismissDialogLoading();
                try {
                    responseCode = jsonObject.getInt("response");
                    switch (responseCode) {
                        case 1://success
                            JSONObject info = jsonObject.getJSONObject("info");
                            AppConfigCache.saveLoginInfo(ActLogin.this, jsonObject.getString("token"),
                                    info.getString("name"), info.getString("phone"),
                                    info.getString("email"));

                            Toast.makeText(ActLogin.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                            break;
                        case 2://error
                            Toast.makeText(ActLogin.this, jsonObject.getString("error_msg").toString(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String string) {
                Log.e(TAG + "sdlu string = ", string);
                dismissDialogLoading();
                Toast.makeText(ActLogin.this, getString(R.string.net_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
