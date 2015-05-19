package lu.shaode.buyerrescue.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.adapter.AdapterGoodsList;
import lu.shaode.buyerrescue.ui.dummy.ContentGoods;
import lu.shaode.buyerrescue.ui.dummy.ContentStore;
import lu.shaode.buyerrescue.util.StringUtil;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link lu.shaode.buyerrescue.ui.FragmentParentList.OnFragmentInteractionListener}
 * interface.
 */
public class FragmentAddressList extends FragmentParentList{

    private final String TAG = ((Object) this).getClass().getSimpleName();

    int type = 0;

    private OnFragmentInteractionListener mListener;
    public AdapterGoodsList mAdapter;

    public static FragmentAddressList newInstance() {
        return new FragmentAddressList();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentAddressList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new AdapterGoodsList(getActivity(), ContentGoods.ITEMS);
        setListAdapter(mAdapter);
        ContentGoods.ITEMS.clear();
        getGoodList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutContent(), container, false);
        return view;
    }

    @Override
    protected int getLayoutContent() {
        return R.layout.fragment_fragment_address_list;
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

    public void getGoodList(){
        ((ActParent)getActivity()).showDialogLoading();
    }

    class AddressApiListener implements ApiListener{

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
                                ContentStore.Store store = new ContentStore.Store(
                                        foo.getJSONObject("store"));
                                good.setStore(store);
                                ContentGoods.ITEMS.add(good);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            showNoGoods();
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
    }
}
