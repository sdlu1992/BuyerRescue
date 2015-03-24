package lu.shaode.buyerrescue.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lu.shaode.buyerrescue.R;
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
public class FragmentInfo extends FragmentParent {

    private final String TAG = ((Object) this).getClass().getSimpleName();
    private OnFragmentInteractionListener mListener;

    TextView tvName;
    TextView tvPhone;
    TextView tvEmail;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
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
        setInfo();

        Map<String, String> params = new HashMap<>();
        BizManager.getInstance(getActivity()).getUserInfo(params, new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                JSONObject info = null;
                try {
                    info = jsonObject.getJSONObject("info");
                    AppConfigCache.setCacheConfig(getActivity(), "name", info.getString("name"));
                    AppConfigCache.setCacheConfig(getActivity(), "phone", info.getString("phone"));
                    AppConfigCache.setCacheConfig(getActivity(), "email", info.getString("email"));
                    setInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String string) {

            }
        });
        return v;
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

    public void setInfo(){
        tvName.setText(AppConfigCache.getCacheConfigString(getActivity(), "name"));
        tvPhone.setText(AppConfigCache.getCacheConfigString(getActivity(), "phone"));
        tvEmail.setText(AppConfigCache.getCacheConfigString(getActivity(), "email"));
    }
}
