package lu.shaode.buyerrescue.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.dummy.ContentHistoryList;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

public class ActAppraise extends ActParent implements View.OnClickListener{

    private final String TAG = ((Object) this).getClass().getSimpleName();
    RadioGroup rgType;
    EditText etContent;
    Button btAppraise;

    String historyId = "";
    String orderId = "";
    int typeAppraise;
    AlertDialog okDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyId = getIntent().getStringExtra("history_id");
        orderId = getIntent().getStringExtra("order_id");
    }

    @Override
    protected int getLayoutContent() {
        return R.layout.activity_act_appraise;
    }

    @Override
    protected void initBodyControl() {
        super.initBodyControl();
        rgType = (RadioGroup) findViewById(R.id.act_appraise_type);
        etContent = (EditText) findViewById(R.id.act_appraise_content);
        btAppraise = (Button) findViewById(R.id.act_appraise_button);
        btAppraise.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_appraise, menu);
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
            case R.id.act_appraise_button:
                typeAppraise = getAppraiseType();
                if(typeAppraise == -1){
                    showToastMessage(R.string.appraise_type_empty);
                    return;
                }
                showDialog();
                break;
            default:
                break;
        }
    }

    public void appraise(){
        BizManager.getInstance(ActAppraise.this).appraise(orderId, historyId, typeAppraise,
                etContent.getText().toString(), new ApiListener() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        try {
                            int response = jsonObject.getInt("response");
                            switch (response){
                                case 1:
                                    showToastMessage(R.string.appraise_success);
                                    finish();
                                    break;
                                default:
                                    showToastMessage(jsonObject.getString("error_msg"));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissDialogLoading();
                    }

                    @Override
                    public void error(String string) {
                        dismissDialogLoading();
                        showToastMessage(string);

                    }
                });
    }

    public int getAppraiseType(){
        switch (rgType.getCheckedRadioButtonId()){
            case R.id.act_appraise_good:
                return 0;
            case R.id.act_appraise_middle:
                return 1;
            case R.id.act_appraise_bad:
                return 2;
            default:
                return -1;
        }
    }

    public void showDialog(){
        okDialog = new AlertDialog.Builder(this)
                .setTitle(getResources().getStringArray(R.array.state_will)[3])
                .setMessage(getString(R.string.is_continue))
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showDialogLoading();
                                Log.e(TAG + " sdlu", "history= " + historyId);
                                appraise();
                                dismissDialog();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismissDialog();
                                dismissDialogLoading();
                            }
                        }).create();
        okDialog.show();
    }

    public void dismissDialog() {
        if (okDialog != null && okDialog.isShowing()) {
            okDialog.dismiss();
        }
    }
}
