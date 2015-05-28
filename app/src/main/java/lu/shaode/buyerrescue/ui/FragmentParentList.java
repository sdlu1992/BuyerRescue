package lu.shaode.buyerrescue.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.dialog.DialogLoading;
import lu.shaode.buyerrescue.util.UserUtil;

public class FragmentParentList extends ListFragment {

    private Toast mToast;
    private DialogLoading mDialog;
    private TextView tvNoGoods;
    private OnFragmentInteractionListener mListener;

    public static FragmentParentList newInstance() {
        FragmentParentList fragment = new FragmentParentList();
        return fragment;
    }

    public FragmentParentList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutContent(), container, false);
        tvNoGoods = (TextView) view.findViewById(R.id.frag_parent_no_goods);
        return view;
    }

    protected int getLayoutContent() {
        return R.layout.fragment_fragment_parent_list;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String String);
    }

    protected ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public void showDialogWorking() {
        if (mDialog == null) {
            mDialog = new DialogLoading(getActivity());
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }
        mDialog.show();
    }

    public void showDialogLoading() {
        showDialogWorking();
    }

    public void dismissDialogLoading() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void showToastMessage(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void showNoGoods(){
        tvNoGoods.setVisibility(View.VISIBLE);
    }

    public void hiddenNoGoods(){
        tvNoGoods.setVisibility(View.GONE);
    }

    public boolean loginVerify(){
        if (getActivity() == null){
            return true;
        }
        boolean isLogin = UserUtil.isLogin(getActivity());
        if (!isLogin){
            startLoginActivity();
        }
        return isLogin;
    }

    public void startLoginActivity(){
        Intent intent = new Intent(getActivity(), ActLogin.class);
        startActivity(intent);
    }
}
