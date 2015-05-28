package lu.shaode.buyerrescue.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import lu.shaode.buyerrescue.R;

public class ActAddressChoose extends ActParent{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FragmentAddressChoose())
                    .commit();
        }
    }

    @Override
    protected int getLayoutContent() {
        return R.layout.activity_act_address_choose;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_address_choose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_address_manage:
                Intent intent = new Intent(this, ActAddressManage.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
