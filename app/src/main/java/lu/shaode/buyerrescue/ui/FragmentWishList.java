package lu.shaode.buyerrescue.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.adapter.AdapterGoodsList;
import lu.shaode.buyerrescue.adapter.AdapterWishList;
import lu.shaode.buyerrescue.ui.dummy.ContentGoods;
import lu.shaode.buyerrescue.ui.dummy.ContentStore;
import lu.shaode.buyerrescue.ui.dummy.ContentWishList;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

/**
 * A fragment representing a list of Items.
 */
public class FragmentWishList extends FragmentParentList implements AdapterWishList.OnCountChange, SwipeRefreshLayout.OnRefreshListener{

    private final String TAG = ((Object) this).getClass().getSimpleName();

    TextView tvPriceTotal;
    CheckBox cbSelectAll;
    SwipeRefreshLayout refreshLayout;
    private OnFragmentInteractionListener mListener;
    public AdapterWishList mAdapter;

    public static FragmentWishList newInstance() {
        return new FragmentWishList();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentWishList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new AdapterWishList(getActivity(), ContentWishList.ITEMS, FragmentWishList.this);
        Log.e(TAG + " sdlu", "ContentWishList.ITEMS.size()= " + ContentWishList.ITEMS.size());
        setListAdapter(mAdapter);
        ContentWishList.ITEMS.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutContent(), container, false);
        tvPriceTotal = (TextView) view.findViewById(R.id.frag_wish_total_price);
        cbSelectAll = (CheckBox) view.findViewById(R.id.frag_wish_select_all);
        cbSelectAll.setOnClickListener(new OnSelectAllListener());
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.frag_wish_refresh);
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
    protected int getLayoutContent() {
        return R.layout.fragment_fragment_wish_list;
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

    public void getWishList(){
        BizManager.getInstance(getActivity()).getWishList(new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.e(TAG + " sdlu", "jsonObject= " + jsonObject.toString());
                int responseCode = 2;
                try {
                    responseCode = jsonObject.getInt("response");
                    switch (responseCode) {
                        case 1:
                            int length = jsonObject.getInt("len");
                            if (length != 0) {
                                JSONArray wish_list = jsonObject.getJSONArray("wish_list");
                                ContentWishList.ITEMS.clear();
                                for (int i = 0; i < wish_list.length(); i++) {
                                    JSONObject foo = wish_list.getJSONObject(i);
                                    JSONObject storeJsonObject = foo.getJSONObject("store");
                                    JSONObject goodJsonObject = foo.getJSONObject("good");

                                    ContentStore.Store store = new ContentStore.Store(storeJsonObject);
                                    ContentGoods.Good good = new ContentGoods.Good(goodJsonObject);
                                    good.setStore(store);
                                    ContentWishList.Wish wish = new ContentWishList.Wish(foo);
                                    wish.setStore(store);
                                    wish.setGood(good);
                                    ContentWishList.ITEMS.add(wish);

                                }
                                mAdapter.notifyDataSetChanged();
                                setTotalPrice();
                            }
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

    public void setTotalPrice(){
        float total = 0;
        for (ContentWishList.Wish wish: ContentWishList.ITEMS){
            total += wish.isCheck ? Float.parseFloat(wish.good.price) * (float)wish.count : 0;
        }
        tvPriceTotal.setText("总价: " + total + "元");
    }

    @Override
    public void onRefresh() {
        getWishList();
    }

    class OnSelectAllListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            for (ContentWishList.Wish wish: ContentWishList.ITEMS){
                wish.setCheck(cbSelectAll.isChecked());
            }
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onCountChange(int position) {
        setTotalPrice();
    }

    @Override
    public void onCheckChange(int position) {
        setTotalPrice();
        boolean isSelectAll = true;
        for (ContentWishList.Wish wish : ContentWishList.ITEMS){
            isSelectAll = isSelectAll && wish.isCheck;
        }
        cbSelectAll.setChecked(isSelectAll);
    }

    public void refreshComplete(){
        if (refreshLayout != null){
            refreshLayout.setRefreshing(false);
        }
    }
}
