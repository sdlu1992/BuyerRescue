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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.dummy.ContentGoods;
import lu.shaode.buyerrescue.ui.dummy.ContentSolder;
import lu.shaode.buyerrescue.ui.dummy.ContentStore;
import lu.shaode.buyerrescue.util.BuyerApplication;
import lu.shaode.buyerrescue.util.BuyerImageCache;
import lu.shaode.buyerrescue.util.StringUtil;
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
    Button btAppraise;
    ViewPager imagePager;
    ViewGroup imageTips;
    DialogNumberPicker dialogNumberPicker;
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

    ContentSolder.Solder solder = null;
    ContentGoods.Good good = null;
    ContentStore.Store store = null;

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
        btAppraise = (Button) findViewById(R.id.act_good_appraise);
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
        btAppraise.setOnClickListener(this);

        final String good_id = getIntent().getStringExtra("good_id");
        Log.e(TAG + " sdlu", "good_id= " + good_id);

        showDialogLoading();
        BizManager.getInstance(this).getGoodById(good_id, new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                try{
                    JSONObject jsonGood = jsonObject.getJSONObject("good");
                    JSONObject jsonSolder = jsonObject.getJSONObject("solder");
                    JSONObject jsonStore = jsonObject.getJSONObject("store");
                    solder = new ContentSolder.Solder(jsonSolder);
                    store = new ContentStore.Store(jsonStore);
                    store.setSolder(solder);
                    good = new ContentGoods.Good(jsonGood);
                    good.setStore(store);
                    good.setCount(jsonGood.getString("count"));
                    tvTitle.setText(good.name);
                    tvPrice.setText(StringUtil.getMoneyString(good.price) + " 元");
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
                    initViewPager();
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_good_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.act_good_solder_phone:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+solder.phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.act_good_wish_add:
                showNumberPickerDialog(getString(R.string.wish_add), new OnWishOkClickListener());
                break;
            case R.id.act_good_buy:
                showNumberPickerDialog(getString(R.string.buy_now), new OnBuyOkClickListener());
                break;
            case R.id.act_good_appraise:
                intent = new Intent(this, ActAppraiseList.class);
                intent.putExtra("good_id", good.id);
                startActivity(intent);
                break;
        }
    }

    public void order(){
        showDialogLoading();
        Map<String, String> mGood = new HashMap<>();
        mGood.put("id", good.id);
        mGood.put("count", String.valueOf(dialogNumberPicker.npCount.getValue()));
        JSONObject jGood = new JSONObject(mGood);
        final JSONArray jsonArray = new JSONArray();
        jsonArray.put(jGood);
        Log.e(TAG + " sdlu", "jsonArray.toString()= " + jsonArray.toString());
        BizManager.getInstance(ActGoodDetail.this).addOrder(jsonArray, new ApiListener() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        Log.e(TAG + " sdlu", "jsonObject.toString()= " + jsonObject.toString());
                        try {
                            int response = jsonObject.getInt("response");
                            switch (response){
                                case 1:
                                    String orderId = jsonObject.getString("order_id");
                                    Intent intent = new Intent(ActGoodDetail.this, ActOrderDetail.class);
                                    intent.putExtra("order_id", orderId);
                                    startActivity(intent);
                                    dismissPickerDialog();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dismissDialogLoading();
                    }

                    @Override
                    public void error(String string) {
                        Log.e(TAG + " sdlu", "string= " + string);
                        showToastMessage(string);
                        dismissDialogLoading();
                    }
                });

    }

    public void addWishList(){
        showDialogLoading();
        BizManager.getInstance(ActGoodDetail.this).addWishList(good.id,
                String.valueOf(getNpDialogCount()) , new ApiListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.e(TAG + " sdlu", "jsonObject.toString()= " + jsonObject.toString());
                Toast.makeText(ActGoodDetail.this, getString(R.string.wish_added), Toast.LENGTH_SHORT).show();
                dismissDialogLoading();
                dismissPickerDialog();
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
     * @param selectItems position
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
        ArrayList<String> arrays = (ArrayList<String>)(good.imageUrlOther.clone());
        if (!StringUtil.isNullOrEmpty(good.imageUrlTitle)){
            arrays.add(0, good.imageUrlTitle);
        }
        if (arrays.size() == 0){
            imgUrlArray = new String[]{"baidu.com"};
        }
        imgUrlArray = arrays.toArray(new String[arrays.size()]);

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
            imageView.setErrorImageResId(R.drawable.no_pic);
            imageView.setDefaultImageResId(R.drawable.pic_loading);
            mImageViews[i] = imageView;
            ImageLoader loader = new ImageLoader(BuyerApplication.queue, BuyerImageCache.getInstance());
            imageView.setImageUrl(imgUrlArray[i], loader);

        }
        //设置Adapter
        imagePager.setAdapter(new MyAdapter());
        //设置监听，主要是设置点点的背景
        imagePager.setOnPageChangeListener(this);

    }

    public void showNumberPickerDialog(String title, View.OnClickListener onOkListener){
        if (dialogNumberPicker == null){
            dialogNumberPicker = new DialogNumberPicker(this);
        }
        dialogNumberPicker.setTitle(title);
        dialogNumberPicker.setOnOkListener(onOkListener);
        dialogNumberPicker.show();

    }

    public void dismissPickerDialog(){
        if (dialogNumberPicker != null && dialogNumberPicker.isShowing()){
            dialogNumberPicker.dismiss();
        }
    }

    public int getNpDialogCount(){
        return dialogNumberPicker == null ? 0 : dialogNumberPicker.npCount.getValue();
    }

    class OnBuyOkClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            order();
        }
    }

    class OnWishOkClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            addWishList();
        }
    }

}
