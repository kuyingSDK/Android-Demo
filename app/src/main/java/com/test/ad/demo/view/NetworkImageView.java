package com.test.ad.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Vicent
 * @date 2025/9/11
 * @apiNote
 */
public class NetworkImageView extends ImageView {

    public NetworkImageView(Context context) {
        super(context);
    }

    public NetworkImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NetworkImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 设置图片URL的公共方法
    public void setImageUrl(String imageUrl) {
        new ImageDownloadTask().execute(imageUrl);
    }

    // 异步下载图片任务
    private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(imageUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                setImageBitmap(bitmap);
            }
        }
    }
}
