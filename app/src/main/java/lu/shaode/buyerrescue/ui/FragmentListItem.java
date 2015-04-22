package lu.shaode.buyerrescue.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lu.shaode.buyerrescue.adapter.AdapterGoodsList;
import lu.shaode.buyerrescue.ui.dummy.ContentGoods;
import lu.shaode.buyerrescue.ui.dummy.ContentStore;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class FragmentListItem extends FragmentParentList{

    private final String TAG = ((Object) this).getClass().getSimpleName();

    String category = "";

    private OnFragmentInteractionListener mListener;
    public AdapterGoodsList mAdapter;

    public static FragmentListItem newInstance() {
        return new FragmentListItem();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentListItem() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new AdapterGoodsList(getActivity(), ContentGoods.ITEMS);
        setListAdapter(mAdapter);
        category = getActivity().getIntent().getStringExtra("category");
        Log.e(TAG + "sdlu", "category= " + category);
        ContentGoods.ITEMS.clear();
        getGoodList();
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
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(ContentGoods.ITEMS.get(position).id);
        }
    }

    public void getGoodList(){
        ((ActParent)getActivity()).showDialogLoading();
        BizManager.getInstance(getActivity()).getGoodsByCategory(category, new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.e(TAG + " sdlu", "jsonObject= " + jsonObject.toString());
                int responseCode = 2;
                try {
                    responseCode = jsonObject.getInt("response");
                    switch (responseCode){
                        case 1:
                            int length = jsonObject.getInt("len");
                            if (length != 0){
                                JSONArray goods = jsonObject.getJSONArray("goods");
                                for (int i = 0; i < goods.length(); i++){
                                    JSONObject foo = goods.getJSONObject(i);
                                    ContentGoods.Good good = new ContentGoods.Good(foo);
                                    String count = foo.getString("count");
                                    ContentStore.Store store = new ContentStore.Store(
                                            foo.getJSONObject("store"));
                                    good.setStore(store);
                                    good.setCount(count);
                                    ContentGoods.ITEMS.add(good);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                            break;
                        case 2:
                            break;
                    }

                    ((ActParent)getActivity()).dismissDialogLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String string) {
                Log.e(TAG + " sdlu", "string= " + string);
                ((ActParent)getActivity()).dismissDialogLoading();
            }
        });

    }

}
