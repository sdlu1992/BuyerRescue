package lu.shaode.buyerrescue.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import lu.shaode.buyerrescue.R;
import lu.shaode.netsupport.AppConfigCache;

public class ActSearch extends ActParent implements View.OnClickListener, AdapterView.OnItemClickListener{

    private final String TAG = ((Object) this).getClass().getSimpleName();
    public final static String CACHE_SEARCH_SET = "_cache_search_set";
    EditText etSearch;
    TextView tvCancel;
    ListView lvHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutContent() {
        return R.layout.activity_act_search;
    }

    @Override
    protected void initBodyControl() {
        super.initBodyControl();
        lvHistory = (ListView) findViewById(R.id.act_search_list);
        lvHistory.setOnItemClickListener(this);
        initHistory();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.action_bar_search);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        tvCancel = (TextView) actionBar.getCustomView().findViewById(R.id.action_bar_search_cancel);
        tvCancel.setOnClickListener(this);
        etSearch = (EditText) actionBar.getCustomView().findViewById(R.id.action_bar_search_edit);
        etSearch.setOnEditorActionListener(new OnSearchEnterListener());
    }

    public void initHistory(){
        List<String> stringList = AppConfigCache.getStringArray(this, CACHE_SEARCH_SET);
        Log.e(TAG + " sdlu", "stringList.toString()= " + stringList.toString());
        Collections.reverse(stringList);
        lvHistory.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringList));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHistory();
        //弹出软键盘
        etSearch.postDelayed(new Runnable() {
            @Override
            public void run() {
                etSearch.requestFocus();
                InputMethodManager inputManager =

                        (InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.showSoftInput(etSearch, 0);
            }
        }, 300);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_bar_search_cancel:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<String> strings = AppConfigCache.getStringArray(this, CACHE_SEARCH_SET);
        Collections.reverse(strings);
        etSearch.setText(strings.get(position));
        search();
    }

    class OnSearchEnterListener implements TextView.OnEditorActionListener{

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            Log.e(TAG + " sdlu", "actionId= " + actionId);
            switch (actionId){
                case EditorInfo.IME_ACTION_SEARCH:
                    search();
                    break;
            }
            return false;
        }
    }

    @Override
    protected boolean isNeedFlipAnimation() {
        return false;
    }

    public void search(){
        AppConfigCache.addSearchArrayString(ActSearch.this, CACHE_SEARCH_SET, etSearch.getText().toString());
        Intent intent = new Intent(ActSearch.this, ActGoodsList.class);
        intent.putExtra("search_key", etSearch.getText().toString());
        startActivity(intent);
    }
}
