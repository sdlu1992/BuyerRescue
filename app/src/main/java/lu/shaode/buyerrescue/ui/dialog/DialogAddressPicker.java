package lu.shaode.buyerrescue.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.ui.WheelView;

/**
 * Created by sdlu on 15/4/13.
 */
public class DialogAddressPicker extends Dialog {

    private final String TAG = ((Object) this).getClass().getSimpleName();
    Button btOk;
    Button btCancel;
    public WheelView wvProvince;
    public WheelView wvCity;
    public WheelView wvCounty;
    OnOkListener onOkListener;

    Context context;
    Context test_Context = null;
    List<String> provinces;
    List<String> cities;
    List<String> counties;
    JSONObject root;
    JSONArray provinceJson;
    JSONArray cityJson;
    JSONArray countyJson;
    public DialogAddressPicker(Context context) {
        super(context, R.style.DialogNumberPicker);
        setContentView(R.layout.dialog_address_picker);
        this.context = context;
        initViews();
    }

    public DialogAddressPicker(Context context, int theme) {
        super(context, R.style.DialogNumberPicker);
        setContentView(R.layout.dialog_address_picker);
        this.context = context;
        initViews();
    }

    protected DialogAddressPicker(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, R.style.DialogNumberPicker);
        setContentView(R.layout.dialog_address_picker);
        this.context = context;
        initViews();
    }

    public void initViews(){
        btOk = (Button) findViewById(R.id.dialog_np_ok);
        btCancel = (Button) findViewById(R.id.dialog_np_cancel);
        btCancel.setOnClickListener(new OnCancelClickListener());
        wvProvince = (WheelView) findViewById(R.id.dialog_address_wheel_province);
        wvCity = (WheelView) findViewById(R.id.dialog_address_wheel_city);
        wvCounty = (WheelView) findViewById(R.id.dialog_address_wheel_county);
        wvProvince.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.e(TAG + " sdlu", "item= " + item+selectedIndex);
                notifyCity(selectedIndex);
            }
        });
        wvCity.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                notifyCounty(selectedIndex);
            }
        });
        try
        {
            test_Context = context.createPackageContext(
                    context.getPackageName(), Context.CONTEXT_IGNORE_SECURITY);
            AssetManager s =  test_Context.getAssets();
            try{
                InputStream is = s.open("province.json");
                byte [] buffer = new byte[is.available()] ;
                is.read(buffer);
                String json = new String(buffer,"utf-8");
                is.close();
                try {
                    root = new JSONObject(json).getJSONObject("root");
                    provinceJson = root.getJSONArray("province");
                    cityJson = provinceJson.getJSONObject(0).getJSONArray("city");
                    countyJson = cityJson.getJSONObject(0).getJSONArray("district");
                    provinces = new ArrayList<>();
                    cities = new ArrayList<>();
                    counties = new ArrayList<>();
                    for (int i=0; i<provinceJson.length(); i++){
                        provinces.add(provinceJson.getJSONObject(i).getString("-name"));
                    }
                    for (int i=0; i<cityJson.length(); i++){
                        cities.add(cityJson.getJSONObject(i).getString("-name"));
                    }
                    for (int i=0; i<countyJson.length(); i++){
                        counties.add(countyJson.getJSONObject(i).getString("-name"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }catch(IOException e){
                e.printStackTrace();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        wvProvince.setItems(provinces);
        wvCity.setItems(cities);
        wvCounty.setItems(counties);

    }

    class OnCancelClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

    public void setOnOkListener(OnOkListener onOkListener) {
        this.onOkListener = onOkListener;
        btOk.setOnClickListener(new ButtonOkClickListener());
    }

    class ButtonOkClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            onOkListener.ok(getProvince(), getCity(), getCounty());
            dismiss();
        }
    }

    public void notifyCity(int position){
        try {
            cityJson = provinceJson.getJSONObject(position-1).getJSONArray("city");
        } catch (JSONException e) {
            cityJson = new JSONArray();
            try {
                cityJson.put(provinceJson.getJSONObject(position - 1).getJSONObject("city"));
            } catch (JSONException e1){
                e1.printStackTrace();
            }
        }
        try{
            cities.clear();
            for (int i=0; i<cityJson.length(); i++){
                cities.add(cityJson.getJSONObject(i).getString("-name"));
            }
            wvCity.setItems(cities);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void notifyCounty(int position){
        try {
            countyJson = cityJson.getJSONObject(position-1).getJSONArray("district");
            counties.clear();
            for (int i=0; i<countyJson.length(); i++){
                counties.add(countyJson.getJSONObject(i).getString("-name"));
            }
            wvCounty.setItems(counties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getProvince(){
        return wvProvince.getSeletedItem();
    }
    public String getCity(){
        return wvCity.getSeletedItem();
    }
    public String getCounty(){
        return wvCounty.getSeletedItem();
    }

    public interface OnOkListener{
        public void ok(String province, String city, String county);
    }

}
