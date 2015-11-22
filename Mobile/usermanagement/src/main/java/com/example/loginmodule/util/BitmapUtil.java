package com.example.loginmodule.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;

/**
 * Created by david on 20.11.2015..
 */
public class BitmapUtil {

    public static RequestBody toRequest(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return RequestBody.create(MediaType.parse("image/*"), byteArray);
        }
        return null;
    }


    public static Bitmap fromFile(Context context, File file) {
        try {
            return Picasso.with(context).load(file).resize(400, 400).onlyScaleDown().get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Observable<Bitmap> fromFileAsync(Context context, File file) {
        return Observable.defer(() -> Observable.just(fromFile(context, file)));
    }


    public static Bitmap mergeToPin(Bitmap back, Bitmap front) {
        Bitmap result = Bitmap.createBitmap(back.getWidth(), back.getHeight(), back.getConfig());
        Canvas canvas = new Canvas(result);
        int widthBack = back.getWidth();
        int widthFront = front.getWidth();
        float move = (widthBack - widthFront) / 2;
        canvas.drawBitmap(back, 0f, 0f, null);
        canvas.drawBitmap(front, move, move, null);
        return result;
    }

    /**
     * Creates circular bitmap with white edge
     * Source: http://stackoverflow.com/questions/17040475/adding-a-round-frame-circle-on-rounded-bitmap
     *
     * @param bitmap Bitmap to be converted
     * @return Processed bitmap
     */
    public static Bitmap circularize(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);
        Paint p = new Paint();
        p.setAntiAlias(true);
        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        c.drawBitmap(bitmap, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
//        p.setColor(strokeColor);
        p.setStrokeWidth(2);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);
        return output;
    }

    public static Bitmap fromResource(Context context, int resourceId) {
        return BitmapFactory.decodeResource(context.getResources(), resourceId);
    }

    public static File toFile(Context context, Bitmap bitmap) {
        File f = new File(context.getCacheDir(), "img_tmp.png");
        try {
            f.createNewFile();
            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return f;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Observable<File> toFileAsync(Context context, Bitmap bitmap) {
        return Observable.defer(() -> Observable.just(toFile(context, bitmap)));
    }
}
