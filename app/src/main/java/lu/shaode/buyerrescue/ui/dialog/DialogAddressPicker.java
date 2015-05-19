package lu.shaode.buyerrescue.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import lu.shaode.buyerrescue.R;

/**
 * Created by sdlu on 15/4/13.
 */
public class DialogAddressPicker extends Dialog {

    Button btOk;
    Button btCancel;
    public NumberPicker npCount;
    View.OnClickListener onOkListener;

    public DialogAddressPicker(Context context) {
        super(context, R.style.DialogNumberPicker);
        setContentView(R.layout.dialog_number_picker);
        initViews();
    }

    public DialogAddressPicker(Context context, int theme) {
        super(context, R.style.DialogNumberPicker);
        setContentView(R.layout.dialog_number_picker);
        initViews();
    }

    protected DialogAddressPicker(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, R.style.DialogNumberPicker);
        setContentView(R.layout.dialog_number_picker);
        initViews();
    }

    public void initViews(){
        btOk = (Button) findViewById(R.id.dialog_np_ok);
        btCancel = (Button) findViewById(R.id.dialog_np_cancel);
        npCount = (NumberPicker) findViewById(R.id.dialog_np_number_picker);
        npCount.setMaxValue(100);
        npCount.setMinValue(1);
        npCount.setValue(1);
        btCancel.setOnClickListener(new OnCancelClickListener());
    }

    class OnCancelClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

    public void setOnOkListener(View.OnClickListener onOkListener) {
        this.onOkListener = onOkListener;
        btOk.setOnClickListener(onOkListener);
    }
}
