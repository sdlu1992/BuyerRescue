package lu.shaode.buyerrescue.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.adapter.AdapterAppraiseList;
import lu.shaode.buyerrescue.ui.dummy.ContentAppraiseList;
import lu.shaode.buyerrescue.ui.dummy.ContentGoods;
import lu.shaode.buyerrescue.ui.dummy.ContentHistoryList;
import lu.shaode.buyerrescue.ui.dummy.ContentStore;
import lu.shaode.buyerrescue.ui.dummy.ContentWishList;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

/**
 * A fragment representing a list of Items.
 */
public class FragmentAppraiseList extends FragmentParentList implements
       SwipeRefreshLayout.OnRefreshListener , View.OnClickListener{

    private final String TAG = ((Object) this).getClass().getSimpleName();

    SwipeRefreshLayout refreshLayout;
    TextView tvTotalGood;
    TextView tvTotalMiddle;
    TextView tvTotalBad;

    public AdapterAppraiseList mAdapter;

    String goodId;

    public static FragmentAppraiseList newInstance() {
        return new FragmentAppraiseList();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentAppraiseList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        goodId = getActivity().getIntent().getStringExtra("good_id");
        mAdapter = new AdapterAppraiseList(getActivity(), ContentAppraiseList.ITEMS);
        Log.e(TAG + " sdlu", "ContentWishList.ITEMS.size()= " + ContentWishList.ITEMS.size());
        setListAdapter(mAdapter);
        ContentWishList.ITEMS.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutContent(), container, false);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.frag_appraise_refresh);
        refreshLayout.setOnRefreshListener(this);
        tvTotalGood = (TextView) view.findViewById(R.id.frag_appraise_total_good);
        tvTotalMiddle = (TextView) view.findViewById(R.id.frag_appraise_total_middle);
        tvTotalBad = (TextView) view.findViewById(R.id.frag_appraise_total_bad);
        tvTotalMiddle.setOnClickListener(this);
        tvTotalGood.setOnClickListener(this);
        tvTotalBad.setOnClickListener(this);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        beginRefresh();
    }

    @Override
    protected int getLayoutContent() {
        return R.layout.fragment_act_appraise_list;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void beginRefresh() {
        refreshLayout.post(new Runnable() {

            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public void getAppraiseList() {
        BizManager.getInstance(getActivity()).getAppraiseList(goodId, new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.e(TAG + " sdlu", "jsonObject= " + jsonObject.toString());
                int responseCode = 2;
                try {
                    responseCode = jsonObject.getInt("response");
                    switch (responseCode) {
                        case 1:
                            int length = jsonObject.getInt("len");
                            ContentAppraiseList.ITEMS.clear();
                            if (length == 0) {
                                setBottomBarText(0, 0, 0);
                                break;
                            }
                            int goodCount = jsonObject.getInt("total_good");
                            int middleCount = jsonObject.getInt("total_middle");
                            int badCount = jsonObject.getInt("total_bad");
                            setBottomBarText(goodCount, middleCount, badCount);
                            JSONArray appraise_list = jsonObject.getJSONArray("appraise_list");
                            for (int i = 0; i < appraise_list.length(); i++) {
                                JSONObject foo = appraise_list.getJSONObject(i);
                                ContentAppraiseList.Appraise appraise = new ContentAppraiseList.Appraise(foo);
                                ContentAppraiseList.ITEMS.add(appraise);

                            }
                            mAdapter.notifyDataSetChanged();
                            break;
                        case 2:
                            showToastMessage(jsonObject.getString("error_msg"));
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                refreshComplete();
            }

            @Override
            public void error(String string) {
                Log.e(TAG + " sdlu", "string= " + string);
                Log.e(TAG + " sdlu", "(getActivity()==null)= " + (getActivity() == null));
                refreshComplete();
            }
        });

    }

    @Override
    public void onRefresh() {
        getAppraiseList();
    }

    public void refreshComplete() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
    }

    public void setBottomBarText(int good, int middle, int bad){
        tvTotalGood.setText(getString(R.string.appraise_good) + " " + good);
        tvTotalMiddle.setText(getString(R.string.appraise_middle) + " " + middle);
        tvTotalBad.setText(getString(R.string.appraise_bad) + " " + bad);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frag_appraise_total_good:
                mAdapter.setGoodChecked(!mAdapter.isGoodChecked());
                tvTotalGood.setBackgroundResource(mAdapter.isGoodChecked() ? R.color.green_light : R.color.transparent);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.frag_appraise_total_middle:
                mAdapter.setMiddleChecked(!mAdapter.isMiddleChecked());
                tvTotalMiddle.setBackgroundResource(mAdapter.isMiddleChecked() ? R.color.green_light : R.color.transparent);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.frag_appraise_total_bad:
                mAdapter.setBadChecked(!mAdapter.isBadChecked());
                tvTotalBad.setBackgroundResource(mAdapter.isBadChecked() ? R.color.green_light : R.color.transparent);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}
