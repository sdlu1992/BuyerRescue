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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.adapter.AdapterHomeList;
import lu.shaode.buyerrescue.ui.dummy.ContentGoods;
import lu.shaode.buyerrescue.ui.dummy.ContentStore;
import lu.shaode.buyerrescue.ui.dummy.ContentWishList;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

/**
 * A fragment representing a list of Items.
 */
public class FragmentHome extends FragmentParent implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{

    private final String TAG = ((Object) this).getClass().getSimpleName();

    SwipeRefreshLayout refreshLayout;
    GridView randomGridView;
    GridView cateGridView;
    GridView othersGridView;
    TextView tvRandomTitle;
    TextView tvCateTitle;
    TextView tvOthersTitle;

    public AdapterHomeList randomAdapter;

    public List<ContentGoods.Good> randomItems= new ArrayList<>();
    public List<ContentGoods.Good> cateItems= new ArrayList<>();
    public List<ContentGoods.Good> othersItems= new ArrayList<>();

    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentHome() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        randomItems.clear();
        cateItems.clear();
        othersItems.clear();
        getActionBar().setTitle(getString(R.string.title_section1));
        Log.e(TAG + " sdlu", "ContentWishList.ITEMS.size()= " + ContentWishList.ITEMS.size());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_home, container, false);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.frag_home_refresh);
        randomGridView = (GridView) view.findViewById(R.id.frag_home_grid_random);
        cateGridView = (GridView) view.findViewById(R.id.frag_home_grid_cate);
        othersGridView = (GridView) view.findViewById(R.id.frag_home_grid_others);
        tvCateTitle = (TextView) view.findViewById(R.id.frag_home_tv_cate);
        tvRandomTitle = (TextView) view.findViewById(R.id.frag_home_tv_random);
        tvOthersTitle = (TextView) view.findViewById(R.id.frag_home_tv_others);
        refreshLayout.setOnRefreshListener(this);
        beginRefresh();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getHome(){
        BizManager.getInstance(getActivity()).getHome(new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.e(TAG + " sdlu", "jsonObject= " + jsonObject.toString());
                int responseCode;
                try {
                    responseCode = jsonObject.getInt("response");
                    switch (responseCode) {
                        case 1:
                            int length = jsonObject.getInt("len_random");
                            randomItems.clear();
                            othersItems.clear();
                            cateItems.clear();
                            if (length != 0) {
                                displayGridView(jsonObject.getJSONArray("random_list"), randomGridView, randomItems);
                            } else {
                                tvRandomTitle.setVisibility(View.GONE);
                            }
                            length = jsonObject.getInt("len_cate");
                            if (length != 0){
                                displayGridView(jsonObject.getJSONArray("cate_list"), cateGridView, cateItems);
                            } else {
                                tvCateTitle.setVisibility(View.GONE);
                            }
                            length = jsonObject.getInt("len_others");
                            if (length != 0){
                                displayGridView(jsonObject.getJSONArray("others_list"), othersGridView, othersItems);
                            } else {
                                tvOthersTitle.setVisibility(View.GONE);
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
                refreshComplete();
            }
        });

    }

    @Override
    public void onRefresh() {
        getHome();
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

    public void displayGridView(JSONArray jsonArray, GridView gridView,
                                List<ContentGoods.Good> list) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject foo = jsonArray.getJSONObject(i);
            JSONObject jsonGoods = foo.getJSONObject("good");
            JSONObject jsonStore = foo.getJSONObject("store");
            ContentGoods.Good colle = new ContentGoods.Good(jsonGoods);
            ContentStore.Store store = new ContentStore.Store(jsonStore);
            colle.setStore(store);
            list.add(colle);
        }
        AdapterHomeList adapter = new AdapterHomeList(getActivity(), list);
        gridView.setAdapter(adapter);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                (int)(getResources().getDisplayMetrics().density * 120
                        * jsonArray.length()),
                ViewGroup.LayoutParams.MATCH_PARENT);
        gridView.setLayoutParams(lp);
        gridView.setNumColumns(list.size());
        gridView.setOnItemClickListener(new OnGoodClickListener(list));
        Log.e(TAG + " sdlu", "randomGridView.getWidth()= " + gridView.getWidth());
    }

    class OnGoodClickListener implements AdapterView.OnItemClickListener{

        List<ContentGoods.Good> list;

        OnGoodClickListener(List<ContentGoods.Good> list) {
            this.list = list;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), ActGoodDetail.class);
            intent.putExtra("good_id", list.get(position).id);
            startActivity(intent);
        }
    }
}
