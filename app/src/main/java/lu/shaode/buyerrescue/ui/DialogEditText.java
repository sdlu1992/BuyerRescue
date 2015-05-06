package lu.shaode.buyerrescue.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import lu.shaode.buyerrescue.R;

/**
 * Created by sdlu on 15/4/13.
 */
public class DialogEditText extends Dialog {

    Button btOk;
    Button btCancel;
    EditText etNumber;
    View.OnClickListener onOkListener;

    public DialogEditText(Context context) {
        super(context, R.style.DialogNumberPicker);
        setContentView(R.layout.dialog_edit);
        initViews();
    }

    public DialogEditText(Context context, int theme) {
        super(context, R.style.DialogNumberPicker);
        setContentView(R.layout.dialog_edit);
        initViews();
    }

    protected DialogEditText(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, R.style.DialogNumberPicker);
        setContentView(R.layout.dialog_edit);
        initViews();
    }

    public void initViews(){
        btOk = (Button) findViewById(R.id.dialog_et_ok);
        btCancel = (Button) findViewById(R.id.dialog_et_cancel);
        etNumber = (EditText) findViewById(R.id.dialog_et_et);
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

    public void setEditTextHint(String string){
        etNumber.setHint(string);
    }

    public String getEditTextString(){
        return etNumber.getText().toString();
    }
}
