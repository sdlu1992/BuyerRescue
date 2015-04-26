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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.adapter.AdapterHistoryList;
import lu.shaode.buyerrescue.ui.dummy.ContentGoods;
import lu.shaode.buyerrescue.ui.dummy.ContentHistoryList;
import lu.shaode.buyerrescue.ui.dummy.ContentStore;
import lu.shaode.buyerrescue.ui.dummy.ContentWishList;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

/**
 * A fragment representing a list of Items.
 */
public class FragmentBuyHistoryList extends FragmentParentList implements
        AdapterHistoryList.OnItemButtonClick, SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = ((Object) this).getClass().getSimpleName();

    SwipeRefreshLayout refreshLayout;
    private OnFragmentInteractionListener mListener;
    public AdapterHistoryList mAdapter;

    AlertDialog okDialog;

    public static FragmentBuyHistoryList newInstance() {
        return new FragmentBuyHistoryList();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentBuyHistoryList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new AdapterHistoryList(getActivity(), ContentHistoryList.ITEMS, FragmentBuyHistoryList.this);
        Log.e(TAG + " sdlu", "ContentWishList.ITEMS.size()= " + ContentWishList.ITEMS.size());
        setListAdapter(mAdapter);
        ContentWishList.ITEMS.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutContent(), container, false);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.frag_history_refresh);
        refreshLayout.setOnRefreshListener(this);
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
        return R.layout.fragment_fragment_history_list;
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
        Log.e(TAG + " sdlu", "position= " + position);
        Intent intent = new Intent(getActivity(), ActOrderDetail.class);
        intent.putExtra("order_id", ContentHistoryList.ITEMS.get(position).order_id);
        Log.e(TAG + " sdlu", "ContentHistoryList.ITEMS.get(position).order_id= " + ContentHistoryList.ITEMS.get(position).order_id);
        startActivity(intent);
    }

    public void getHistoryList() {
        BizManager.getInstance(getActivity()).getHistoryList(new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.e(TAG + " sdlu", "jsonObject= " + jsonObject.toString());
                int responseCode = 2;
                try {
                    responseCode = jsonObject.getInt("response");
                    switch (responseCode) {
                        case 1:
                            int length = jsonObject.getInt("len");
                            ContentHistoryList.ITEMS.clear();
                            if (length == 0) {
                                break;
                            }
                            JSONArray history_list = jsonObject.getJSONArray("history_list");
                            for (int i = 0; i < history_list.length(); i++) {
                                JSONObject foo = history_list.getJSONObject(i);
                                JSONObject storeJsonObject = foo.getJSONObject("store");
                                JSONObject goodJsonObject = foo.getJSONObject("good");

                                ContentStore.Store store = new ContentStore.Store(storeJsonObject);
                                ContentGoods.Good good = new ContentGoods.Good(goodJsonObject);
                                good.setStore(store);
                                ContentHistoryList.History his = new ContentHistoryList.History(foo);
                                his.setGood(good);
                                ContentHistoryList.ITEMS.add(his);

                            }
                            mAdapter.notifyDataSetChanged();
                            break;
                        case 2:
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
        getHistoryList();
    }

    public void refreshComplete() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onButtonClick(ContentHistoryList.History history, int position) {
        showDialog(history);
    }

    class BizApiListener implements lu.shaode.netsupport.listener.ApiListener {

        @Override
        public void success(JSONObject jsonObject) {
            try {
                int responseCode = jsonObject.getInt("response");
                switch (responseCode) {
                    case 1:
                        showToastMessage("成功");
                        break;
                    default:
                        showToastMessage(jsonObject.getString("error_msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showToastMessage(e.toString());
            }
            dismissDialogLoading();
            beginRefresh();
        }

        @Override
        public void error(String string) {
            dismissDialogLoading();
        }
    }

    public void showDialog(final ContentHistoryList.History history) {
        okDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getStringArray(R.array.state_will)[history.state])
                .setMessage(getString(R.string.is_continue))
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showDialogLoading();
                                Log.e(TAG + " sdlu", "history= " + history.id);
                                switch (history.state) {
                                    case 0:
                                        BizManager.getInstance(getActivity()).pay(history.order_id, history.id, new BizApiListener());
                                        break;
                                    case 2:
                                        BizManager.getInstance(getActivity()).takeGoods(history.order_id, history.id, new BizApiListener());
                                        break;
                                    case 3:
                                        //TODO:appraise
                                        BizManager.getInstance(getActivity()).pay(history.order_id, history.id, new BizApiListener());
                                        break;
                                }
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
