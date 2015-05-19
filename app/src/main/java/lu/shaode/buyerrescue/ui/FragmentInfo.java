package lu.shaode.buyerrescue.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.dialog.DialogEditText;
import lu.shaode.netsupport.AppConfigCache;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInfo extends FragmentParent implements View.OnClickListener {

    private final String TAG = ((Object) this).getClass().getSimpleName();
    private OnFragmentInteractionListener mListener;

    TextView tvName;
    TextView tvPhone;
    TextView tvEmail;
    TextView tvRestMoney;
    Button btLogout;
    Button btRecharge;

    DialogEditText dialogEditText;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentInfo.
     */
    public static FragmentInfo newInstance() {
        FragmentInfo fragment = new FragmentInfo();
        return fragment;
    }

    public FragmentInfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle(getString(R.string.title_section2_2));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_info, container, false);
        tvName = (TextView) v.findViewById(R.id.frag_info_name);
        tvPhone = (TextView) v.findViewById(R.id.frag_info_phone);
        tvEmail = (TextView) v.findViewById(R.id.frag_info_email);
        tvRestMoney = (TextView) v.findViewById(R.id.frag_info_money);
        btLogout = (Button) v.findViewById(R.id.frag_info_logout);
        btRecharge = (Button) v.findViewById(R.id.frag_info_recharge);
        btRecharge.setOnClickListener(this);
        btLogout.setOnClickListener(new OnLogoutClickListener());
        setInfo();

        getInfo();
        return v;
    }

    public void getInfo(){
        showDialogLoading();
        BizManager.getInstance(getActivity()).getUserInfo(new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                JSONObject info = null;
                try {
                    if (getActivity() == null) {
                        return;
                    }
                    info = jsonObject.getJSONObject("info");
                    AppConfigCache.setCacheConfig(getActivity(), "name", info.getString("name"));
                    AppConfigCache.setCacheConfig(getActivity(), "phone", info.getString("phone"));
                    AppConfigCache.setCacheConfig(getActivity(), "email", info.getString("email"));
                    AppConfigCache.setCacheConfig(getActivity(), "money", info.getString("money"));
                    setInfo();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setInfo() {
        tvName.setText(AppConfigCache.getCacheConfigString(getActivity(), "name"));
        tvPhone.setText(AppConfigCache.getCacheConfigString(getActivity(), "phone"));
        tvEmail.setText(AppConfigCache.getCacheConfigString(getActivity(), "email"));
        tvRestMoney.setText(AppConfigCache.getCacheConfigString(getActivity(), "money") + getString(R.string.money_suffix));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_info_recharge:
                showEditTextDialog();
                break;
        }
    }

    class OnLogoutClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            AppConfigCache.logout(getActivity().getApplicationContext());
            ((ActMain) getActivity()).selectDrawerItem(0);
        }
    }

    public void showEditTextDialog() {
        if (dialogEditText == null) {
            dialogEditText = new DialogEditText(getActivity());
        }
        dialogEditText.setTitle(getString(R.string.recharge));
        dialogEditText.setEditTextHint(getString(R.string.recharge_hint));
        dialogEditText.setOnOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogLoading();
                BizManager.getInstance(getActivity()).recharge(dialogEditText.getEditTextString(), new ApiListener() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        Log.e(TAG + " sdlu", "jsonObject= " + jsonObject.toString());
                        try {
                            int response = jsonObject.getInt("response");
                            switch (response) {
                                case 1:
                                    showToastMessage(getString(R.string.recharge_success));
                                    dismissEditTextDialog();
                                    getInfo();
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
        });
        dialogEditText.show();

    }

    public void dismissEditTextDialog() {
        if (dialogEditText != null && dialogEditText.isShowing()) {
            dialogEditText.dismiss();
        }
    }
}
