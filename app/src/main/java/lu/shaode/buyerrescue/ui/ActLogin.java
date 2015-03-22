package lu.shaode.buyerrescue.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import lu.shaode.buyerrescue.R;

public class ActLogin extends ActParent implements View.OnClickListener{

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
}
