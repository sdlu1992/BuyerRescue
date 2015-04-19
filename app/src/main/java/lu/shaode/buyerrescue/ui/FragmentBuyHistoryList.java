package lu.shaode.buyerrescue.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.adapter.AdapterHistoryList;
import lu.shaode.buyerrescue.adapter.AdapterWishList;
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
        AdapterHistoryList.OnCountChange, SwipeRefreshLayout.OnRefreshListener{

    private final String TAG = ((Object) this).getClass().getSimpleName();

    SwipeRefreshLayout refreshLayout;
    private OnFragmentInteractionListener mListener;
    public AdapterHistoryList mAdapter;

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
        refreshLayout.post(new Runnable() {

            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
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


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            mListener.onFragmentInteraction(ContentGoods.ITEMS.get(position).id);
        }
    }

    public void getHistoryList(){
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
                            if (length == 0) {
                                break;
                            }
                            JSONArray history_list = jsonObject.getJSONArray("history_list");
                            ContentHistoryList.ITEMS.clear();
                            for (int i = 0; i < history_list.length(); i++) {
                                JSONObject foo = history_list.getJSONObject(i);
                                JSONObject storeJsonObject = foo.getJSONObject("store");
                                JSONObject goodJsonObject = foo.getJSONObject("good");

                                ContentStore.Store store = new ContentStore.Store(storeJsonObject);
                                ContentGoods.Good good = new ContentGoods.Good(goodJsonObject);
                                good.setStore(store);
                                ContentHistoryList.History his = new ContentHistoryList.History(foo);
                                his.setStore(store);
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

    public void refreshComplete(){
        if (refreshLayout != null){
            refreshLayout.setRefreshing(false);
        }
    }
}
