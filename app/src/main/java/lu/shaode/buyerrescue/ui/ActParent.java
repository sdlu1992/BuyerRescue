package lu.shaode.buyerrescue.ui;

/**
 * Created by sdlu on 15/3/20.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import lu.shaode.buyerrescue.R;

public class ActParent extends ActionBarActivity{

    private Toast mToast;
    private Dialog mDialog;
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutContent());
        if (getIntent() == null) {
            finish();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (isNeedFlipAnimation()) {
            overridePendingTransition(R.anim.anim_window_in, R.anim.anim_window_out);
        }

        initBodyControl();
    }

    protected int getLayoutContent() {
        return R.layout.activity_main;
    }
    protected void initBodyControl() {
    }

    @Override
    protected void onDestroy() {
//        dismissDialogLoading();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected boolean isNeedDisplayNetworkUnavailable() {
        return false;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mNetworkInfo = connMgr.getActiveNetworkInfo();

        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }

        return false;
    }

//    public void showDialogWorking(String msg) {
//        if (mDialog == null) {
//            mDialog = new Dialog(this, R.style.LoadingCustomDialog);
//            mDialog.setContentView(R.layout.layout_dialog_loading);
//            mTextMessage = (TextView) mDialog.findViewById(R.id.id_message_dialog_loading);
//            mDialog.setCancelable(false);
//            mDialog.setCanceledOnTouchOutside(false);
//        }
//        if (!StringUtil.isBlank(msg)) {
//            mTextMessage.setText(msg);
//        }
//        mDialog.show();
//    }

//    public void showDialogLoading() {
//        showDialogWorking(getString(R.string.str_loading));
//    }

//    public void dismissDialogLoading() {
//        if (mDialog != null && mDialog.isShowing()) {
//            mDialog.dismiss();
//        }
//    }

    public void showToastMessage(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void showToastMessage(int msg) {
        showToastMessage(getString(msg));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isNeedFlipAnimation()) {
            overridePendingTransition(R.anim.anim_window_close_in, R.anim.anim_window_close_out);
        }
    }

    protected void finishActivity() {
        finish();
        if (isNeedFlipAnimation()) {
            overridePendingTransition(R.anim.anim_window_close_in, R.anim.anim_window_close_out);
        }
    }

    protected boolean isNeedFlipAnimation() {
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig); // default
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    public HashMap<String, String> getAnalyticsTrackPageEnterParams() {
        return new HashMap<String, String>();
    }

}

