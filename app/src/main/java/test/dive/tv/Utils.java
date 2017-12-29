package test.dive.tv;

/**
 * Created by Emilio on 29/12/2017.
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import java.io.InputStream;
import java.util.Properties;

public class Utils {
    public static String getProperty(String key,Context context) throws Exception {
        Properties properties = new Properties();;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("app.properties");
        properties.load(inputStream);
        String property = properties.getProperty(key);
        if (TextUtils.isEmpty(property))
            throw new Exception("Property not found");
        return property;
    }
}
