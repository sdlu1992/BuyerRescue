package lu.shaode.buyerrescue.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONObject;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.dummy.ContentGoods;
import lu.shaode.buyerrescue.ui.dummy.ContentSolder;
import lu.shaode.buyerrescue.ui.dummy.ContentStore;
import lu.shaode.buyerrescue.util.BuyerApplication;
import lu.shaode.buyerrescue.util.BuyerImageCache;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

public class ActGoodDetail extends ActParent implements ViewPager.OnPageChangeListener, View.OnClickListener{

    private final String TAG = ((Object) this).getClass().getSimpleName();
    TextView tvTitle;
    TextView tvPrice;
    TextView tvCount;
    TextView tvDes;
    TextView tvSolderName;
    TextView tvSolderCredit;
    TextView tvSolderPhone;
    TextView tvSolderEmail;
    Button btCollect;
    Button btWishAdd;
    Button btBuy;
    ViewPager imagePager;
    ViewGroup imageTips;
    /**
     * 装点点的ImageView数组
     */
    private ImageView[] tips;

    /**
     * 装ImageView数组
     */
    private NetworkImageView[] mImageViews;

    /**
     * 图片资源id
     */
    private String[] imgUrlArray ;

    ContentSolder.Solder solder;
    ContentGoods.Good good;
    ContentStore.Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutContent() {
        return R.layout.activity_act_good_detail;
    }

    @Override
    protected void initBodyControl() {
        imageTips = (ViewGroup)findViewById(R.id.act_good_pager_tips);
        imagePager = (ViewPager) findViewById(R.id.act_good_pager);
        btBuy = (Button) findViewById(R.id.act_good_buy);
        btCollect = (Button) findViewById(R.id.act_good_collect);
        btWishAdd = (Button) findViewById(R.id.act_good_wish_add);
        tvCount = (TextView) findViewById(R.id.act_good_count);
        tvTitle = (TextView) findViewById(R.id.act_good_title);
        tvDes = (TextView) findViewById(R.id.act_good_des);
        tvPrice = (TextView) findViewById(R.id.act_good_price);
        tvSolderCredit = (TextView) findViewById(R.id.act_good_solder_credit);
        tvSolderEmail = (TextView) findViewById(R.id.act_good_solder_email);
        tvSolderPhone = (TextView) findViewById(R.id.act_good_solder_phone);
        tvSolderName = (TextView) findViewById(R.id.act_good_solder_name);
        tvSolderPhone.setOnClickListener(this);
        btCollect.setOnClickListener(this);
        btWishAdd.setOnClickListener(this);
        btBuy.setOnClickListener(this);

        String good_id = getIntent().getStringExtra("good_id");
        Log.e(TAG + " sdlu", "good_id= " + good_id);

        showDialogLoading();
        BizManager.getInstance(this).getGoodById(good_id, new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                try{
                    JSONObject jsonGood = jsonObject.getJSONObject("good");
                    JSONObject jsonSolder = jsonObject.getJSONObject("solder");
                    JSONObject jsonStore = jsonObject.getJSONObject("store");
                    solder = new ContentSolder.Solder(
                            jsonSolder.getString("id"),
                            jsonSolder.getString("phone"),
                            jsonSolder.getString("name"),
                            jsonSolder.getString("email"));
                    store = new ContentStore.Store(
                            jsonStore.getString("id"),
                            jsonStore.getString("name"),
                            jsonStore.getString("credit"),
                            solder);
                    good = new ContentGoods.Good(
                            jsonGood.getString("id"),
                            jsonGood.getString("des"),
                            jsonGood.getString("name"),
                            jsonGood.getString("price"),
                            store,
                            jsonGood.getString("count"),
                            jsonGood.getString("category")
                    );
                    tvTitle.setText(good.name);
                    tvPrice.setText(good.price + " 元");
                    tvCount.setText("已售出" + good.count + "件");
                    if (good.describe.equals("")){
                        tvDes.setText(getString(R.string.good_no_des));
                    } else {
                        tvDes.setText(good.describe);
                    }
                    tvSolderEmail.setText("Email: " + solder.email);
                    tvSolderName.setText(store.name);
                    tvSolderCredit.setText("信誉: " + store.credit);
                    tvSolderPhone.setText("手机号: " + solder.phone);
                } catch(Exception e){
                    e.printStackTrace();
                }
                dismissDialogLoading();
            }

            @Override
            public void error(String string) {
                Log.e(TAG + " sdlu", "string= " + string);
                Toast.makeText(ActGoodDetail.this, string, Toast.LENGTH_SHORT).show();
                dismissDialogLoading();

            }
        });
        initViewPager();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_good_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_good_solder_phone:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+solder.phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.act_good_wish_add:
                addWishList();
        }
    }

    public void addWishList(){
        showDialogLoading();
        BizManager.getInstance(ActGoodDetail.this).addWishList(good.id, "1", new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.e(TAG + " sdlu", "jsonObject.toString()= " + jsonObject.toString());
                Toast.makeText(ActGoodDetail.this, getString(R.string.wish_added), Toast.LENGTH_SHORT).show();
                dismissDialogLoading();
            }

            @Override
            public void error(String string) {
                Log.e(TAG + " sdlu", "string= " + string);
                dismissDialogLoading();
            }
        });
    }

    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViews.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);

        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            try{
//                if (mImageViews[position % mImageViews.length].getParent() == null){
                    container.addView(mImageViews[position], 0);
//                }
            }catch (Exception e){
                e.printStackTrace();

            }
//            ((ViewPager)container).addView(mImageViews[position], 0);
            return mImageViews[position];
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setImageBackground(arg0 % mImageViews.length);
    }

    /**
     * 设置选中的tip的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

    public void initViewPager(){
        //载入图片资源ID
        imgUrlArray = new String[]{"https://www.baidu.com/img/bdlogo.png", "http://images.csdn.net/20150413/201504101030411793.jpg"};

        //将点点加入到ViewGroup中
        tips = new ImageView[imgUrlArray.length];
        for(int i=0; i<tips.length; i++){
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10,10));
            tips[i] = imageView;
            if(i == 0){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            imageTips.addView(imageView, layoutParams);
        }


        //将图片装载到数组中
        mImageViews = new NetworkImageView[imgUrlArray.length];
        for(int i=0; i<mImageViews.length; i++) {
            NetworkImageView imageView = new NetworkImageView(this);
            mImageViews[i] = imageView;
            ImageLoader loader = new ImageLoader(BuyerApplication.queue, BuyerImageCache.getInstance());
            imageView.setImageUrl(imgUrlArray[i], loader);

        }
        //设置Adapter
        imagePager.setAdapter(new MyAdapter());
        //设置监听，主要是设置点点的背景
        imagePager.setOnPageChangeListener(this);

    }

}
