package lu.shaode.buyerrescue.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import lu.shaode.buyerrescue.R;

public class ActGoodsList extends ActParent implements FragmentParentList.OnFragmentInteractionListener {

    private final String TAG = ((Object) this).getClass().getSimpleName();
    FragmentListItem fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = FragmentListItem.newInstance();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    protected int getLayoutContent() {
        return R.layout.activity_act_goods_list;
    }

    @Override
    protected void initBodyControl() {
        super.initBodyControl();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_goods_list, menu);
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
    public void onFragmentInteraction(String String) {
        Log.e(TAG + " sdlu", "String= " + String);
        Intent intent = new Intent();
        intent.setClass(ActGoodsList.this, ActGoodDetail.class);
        intent.putExtra("good_id", String);
        startActivity(intent);
    }
}
