package lu.shaode.buyerrescue.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * Created by sdlu on 15/4/12.
 */
public class BuyerImageCache implements ImageLoader.ImageCache {

    private final String TAG = ((Object) this).getClass().getSimpleName();
    /** 单例 */
    private static BuyerImageCache cache;
    /** 内存缓存 */
    private HashMap<String, Bitmap> memory;
    /** 缓存目录 */
    private String cacheDir;

    /** 获取单例 */
    public static BuyerImageCache getInstance() {
        if (null == cache) {
            cache = new BuyerImageCache();
        }
        return cache;
    }
    /** 初始化方法，Application的onCreate中调用 */
    public void initilize(Context context) {
        memory = new HashMap<String, Bitmap>();
        cacheDir = context.getCacheDir().toString()+File.separator;
    }

    @Override
    public Bitmap getBitmap(String url) {
        // 获取图片
        try {
            String key = MD5Util.MD5(url);
            if (memory.containsKey(key)) {
                return memory.get(key);
            } else {
                File file = new File(cacheDir + key);
                if (file.exists()) {
                    return BitmapFactory.decodeFile(file.toString());
                }
            }
        } catch (Exception e) {
            Log.e(TAG + "sdlu e.toString() = ", e.toString());
        }
        return null;
    }
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        // 尺寸超过10时，清理缓存并放入内存
        Log.e(TAG + "sdlu", "memory= " + memory.size());
        if (memory.size() == 10) {
            for (String s : memory.keySet()) {
                try {
                    Bitmap temp = memory.get(s);
                    File file = new File(cacheDir + s);
                    FileOutputStream os;
                    os = new FileOutputStream(file, false);
                    temp.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                memory.clear();
            }
            // 放入图片到内存
            memory.put(MD5Util.MD5(url), bitmap);
        }
    }
}
