package lu.shaode.buyerrescue.ui;

import android.app.Activity;
import android.app.AlertDialog;
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
import lu.shaode.buyerrescue.adapter.AdapterCollectList;
import lu.shaode.buyerrescue.ui.dummy.ContentCollectList;
import lu.shaode.buyerrescue.ui.dummy.ContentWishList;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

/**
 * A fragment representing a list of Items.
 */
public class FragmentCollectList extends FragmentParentList implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{

    private final String TAG = ((Object) this).getClass().getSimpleName();

    SwipeRefreshLayout refreshLayout;
    public AdapterCollectList mAdapter;

    AlertDialog orderDialog;

    public static FragmentCollectList newInstance() {
        return new FragmentCollectList();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentCollectList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setTitle(getString(R.string.title_section5));
        mAdapter = new AdapterCollectList(getActivity(), ContentCollectList.ITEMS);
        Log.e(TAG + " sdlu", "ContentWishList.ITEMS.size()= " + ContentWishList.ITEMS.size());
        setListAdapter(mAdapter);
        ContentWishList.ITEMS.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutContent(), container, false);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.frag_collect_refresh);
        refreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    protected int getLayoutContent() {
        return R.layout.fragment_fragment_collect_list;
    }

    @Override
    public void onResume() {
        super.onResume();
        beginRefresh();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), ActGoodDetail.class);
        intent.putExtra("good_id", ContentCollectList.ITEMS.get(position).good.id);
        startActivity(intent);

    }

    public void getCollectList(){
        BizManager.getInstance(getActivity()).getCollectList(new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.e(TAG + " sdlu", "jsonObject= " + jsonObject.toString());
                int responseCode;
                try {
                    responseCode = jsonObject.getInt("response");
                    switch (responseCode) {
                        case 1:
                            int length = jsonObject.getInt("len");
                            ContentCollectList.ITEMS.clear();
                            if (length != 0) {
                                JSONArray collect_list = jsonObject.getJSONArray("collect_list");
                                for (int i = 0; i < collect_list.length(); i++) {
                                    JSONObject foo = collect_list.getJSONObject(i);
                                    ContentCollectList.Collect colle = new ContentCollectList.Collect(foo);
                                    ContentCollectList.ITEMS.add(colle);

                                }
                                mAdapter.notifyDataSetChanged();
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

    @Override
    public void onRefresh() {
        getCollectList();
    }

    @Override
    public void onClick(View v) {
        Log.e(TAG + " sdlu", "v.getId()= " + v.getId());
    }

    public void refreshComplete(){
        if (refreshLayout != null){
            refreshLayout.setRefreshing(false);
        }
    }

    public void beginRefresh(){
        refreshLayout.post(new Runnable() {

            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

}
