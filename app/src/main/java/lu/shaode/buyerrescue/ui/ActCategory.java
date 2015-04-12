package lu.shaode.buyerrescue.ui;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import lu.shaode.buyerrescue.R;
import lu.shaode.netsupport.AppConfigCache;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

public class ActCategory extends ActParent
        implements FragmentCategory.NavigationDrawerCallbacks {

    private final String TAG = ((Object) this).getClass().getSimpleName();
    private FragmentCategory mFragmentCategory;
    private CharSequence mTitle;

    ListView listView;

    SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListViewAdapter();
        getAndSetCategory();
    }

    @Override
    protected int getLayoutContent(){
        return R.layout.activity_act_category;
    }

    @Override
    protected void initBodyControl(){
        mFragmentCategory = (FragmentCategory)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mFragmentCategory.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        listView = (ListView) findViewById(R.id.act_category_list);
        listView.setOnItemClickListener(new CategoryRootListener());

    }

    // 初始化一个List
    private List<HashMap<String, Object>> getData() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(AppConfigCache.getCacheConfigString(this, "category_root"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getData(jsonObject);
    }

    // 初始化一个List
    private List<HashMap<String, Object>> getData(JSONObject jsonObject) {
        // 新建一个集合类，用于存放多条数据
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        try {
            HashMap<String, Object> map = null;
            Iterator<String> keys = jsonObject.keys();
            for (; keys.hasNext(); ) {
                map = new HashMap<>();
                String key = keys.next();
                Log.e(TAG + "sdlu keys. = ", key);
                map.put("category", jsonObject.getString(key));
                map.put("id", key);
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.act_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void getAndSetCategory(){

        BizManager.getInstance(this).getCategory(new HashMap<String, String>(), new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.e(TAG + "sdlu jsonObject = ", jsonObject.toString());
                try {
                    jsonObject = new JSONObject(new String(jsonObject.toString().getBytes(), "utf-8"));
                    Log.e(TAG + "sdlu jsonObject = ", jsonObject.toString());
                    AppConfigCache.setCacheConfig(ActCategory.this, "category", jsonObject.getString("category"));
                    AppConfigCache.setCacheConfig(ActCategory.this, "category_root", jsonObject.getString("root"));
                    setListViewAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void error(String string) {
                Log.e(TAG + "sdlu wrong= ", string);
                Toast.makeText(ActCategory.this, getString(R.string.net_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void setListViewAdapter(){
        String[] strings = {"category"};
        int[] ids = {android.R.id.text1};//对应布局文件的id
        simpleAdapter = new SimpleAdapter(this,
                getData(), android.R.layout.simple_list_item_activated_1, strings, ids);
        listView.setAdapter(simpleAdapter);
    }

    class CategoryRootListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e(TAG + "sdlu position = ", position + "");

            try {
                JSONObject category = new JSONObject(AppConfigCache.getCacheConfigString(ActCategory.this, "category"));
                Log.e(TAG + "sdlu category = ", category.toString());
                mFragmentCategory.setListViewAdapter(getData(category.getJSONObject(position + 1 + "")));
                mFragmentCategory.openDrawer();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if (mFragmentCategory.isDrawerOpen()){
                    mFragmentCategory.closeDrawer();
                    return true;
                } else {
                    super.onKeyDown(keyCode, event);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
