package lu.shaode.buyerrescue.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.util.AES;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

public class ActRegister extends ActParent implements View.OnClickListener {

    EditText    etPhone;
    EditText    etPwd;
    EditText    etPwdAgain;
    EditText    etName;
    EditText    etEmail;
    Button      btRegister;

    private final String TAG = ((Object) this).getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutContent() {
        return R.layout.activity_act_register;
    }

    @Override
    protected void initBodyControl() {
        super.initBodyControl();
        etPhone = (EditText) findViewById(R.id.act_register_et_phone);
        etPwd = (EditText) findViewById(R.id.act_register_et_pwd);
        etPwdAgain = (EditText) findViewById(R.id.act_register_et_pwd_again);
        etName = (EditText) findViewById(R.id.act_register_et_name);
        etEmail = (EditText) findViewById(R.id.act_register_et_email);
        btRegister = (Button) findViewById(R.id.act_register_bt_register);
        btRegister.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_register, menu);
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

    public void register(){
        final String phone = etPhone.getText().toString();
        final String pwd = etPwd.getText().toString();
        String pwdAgain = etPwdAgain.getText().toString();
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();

        if (phone.equals("")){
            Toast.makeText(this, getString(R.string.cannot_empty_phone), Toast.LENGTH_SHORT).show();
            return;
        } else if (pwd.equals("") || pwdAgain.equals("")){
            Toast.makeText(this, getString(R.string.cannot_empty_pwd), Toast.LENGTH_SHORT).show();
            return;
        } else if (name.equals("")){
            Toast.makeText(this, getString(R.string.cannot_empty_name), Toast.LENGTH_SHORT).show();
            return;
        } else if (email.equals("")){
            Toast.makeText(this, getString(R.string.cannot_empty_email), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pwd.equals(pwdAgain)){
            Toast.makeText(this, getString(R.string.pwd_not_same), Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd.length() < 6){
            Toast.makeText(this, getString(R.string.pwd_more_6), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!email.contains("@") || !email.contains(".")){
            Toast.makeText(this, getString(R.string.email_format_wrong), Toast.LENGTH_SHORT).show();
            return;
        }

        BizManager bizManager = BizManager.getInstance(getApplicationContext());
        String aesPwd = "";
        try {
            aesPwd = AES.getInstance().encrypt_string(pwd);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.net_wrong), Toast.LENGTH_SHORT).show();
            return;
        }
        bizManager.register(phone, name, aesPwd, email, new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.e(TAG + "sdlu jsonObject.toString() = " , jsonObject.toString());
                int responseCode = 2;
                try {
                    responseCode = jsonObject.getInt("response");
                    switch (responseCode){
                        case 1://success
                            Toast.makeText(ActRegister.this, getString(R.string.register_success),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("phone", phone);
                            intent.putExtra("password", pwd);
                            setResult(RESULT_OK, intent);
                            finish();
                            break;
                        case 2://error
                            Toast.makeText(ActRegister.this, jsonObject.getString("error_msg").toString(),Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String string) {
                Log.e(TAG + "sdlu string = ", string);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_register_bt_register:
                register();
                break;
        }
    }

}
