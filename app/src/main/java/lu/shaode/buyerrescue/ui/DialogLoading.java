package lu.shaode.buyerrescue.ui;

import android.app.Dialog;
import android.content.Context;

import lu.shaode.buyerrescue.R;

/**
 * Created by sdlu on 15/4/13.
 */
public class DialogLoading extends Dialog{
    public DialogLoading(Context context) {
        super(context, R.style.DialogLoading);
        setContentView(R.layout.dialog_loading);
    }

    public DialogLoading(Context context, int theme) {
        super(context, R.style.DialogLoading);
        setContentView(R.layout.dialog_loading);
    }

    protected DialogLoading(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setContentView(R.layout.dialog_loading);
    }
}
