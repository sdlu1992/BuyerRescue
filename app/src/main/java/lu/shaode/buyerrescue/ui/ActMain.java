package lu.shaode.buyerrescue.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import lu.shaode.buyerrescue.R;
import lu.shaode.netsupport.AppConfigCache;


public class ActMain extends ActParent
        implements FragmentNavigationDrawer.NavigationDrawerCallbacks,
        FragmentParent.OnFragmentInteractionListener,
        FragmentParentList.OnFragmentInteractionListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private FragmentNavigationDrawer mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutContent(){
        return R.layout.activity_main;
    }

    @Override
    public void initBodyControl(){
        mNavigationDrawerFragment = (FragmentNavigationDrawer)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position){
            case 1:
                if (AppConfigCache.getCacheConfigString(this, "token").equals("")){
                    Intent intent = new Intent();
                    intent.setClass(ActMain.this, ActLogin.class);
                    startActivity(intent);
                } else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentInfo.newInstance())
                            .commit();
                }
                return;
            case 2:
                if (AppConfigCache.getCacheConfigString(this, "token").equals("")){
                    Intent intent = new Intent();
                    intent.setClass(ActMain.this, ActLogin.class);
                    startActivity(intent);
                } else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentWishList.newInstance())
                            .commit();
                }
            case 3:
                if (AppConfigCache.getCacheConfigString(this, "token").equals("")){
                    Intent intent = new Intent();
                    intent.setClass(ActMain.this, ActLogin.class);
                    startActivity(intent);
                } else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentBuyHistoryList.newInstance())
                            .commit();
                }

        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "Buyer";
                break;
            case 2:
                if(AppConfigCache.getCacheConfigString(this, "name").equals("")){
                    mTitle = getString(R.string.title_section2);
                } else {
                    mTitle = AppConfigCache.getCacheConfigString(this, "name");
                }
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
//            restoreActionBar();
            return true;
        }
        getMenuInflater().inflate(R.menu.empty, menu);
//        return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_category:
                Intent intent = new Intent(ActMain.this, ActCategory.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String String) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((ActMain) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void selectDrawerItem(int position){
        mNavigationDrawerFragment.selectItem(position);
        restoreActionBar();
    }

    @Override
    public void logout(){
        mNavigationDrawerFragment.notifyItems();
        AppConfigCache.logout(this);
    }
}
