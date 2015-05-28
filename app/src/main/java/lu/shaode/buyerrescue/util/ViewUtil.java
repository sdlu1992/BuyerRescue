package lu.shaode.buyerrescue.util;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by sdlu on 15/4/20.
 */
public class ViewUtil {

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        Log.e("ViewUtil"+ " sdlu", "listAdapter= " + listAdapter.getCount());
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (listAdapter.getCount()>1){
           params.height = totalHeight/2;
        } else {
            params.height = totalHeight ;
        }
        Log.e("ViewUtil" + " sdlu", "totalHeight= " + totalHeight);
        listView.setLayoutParams(params);
    }
}
