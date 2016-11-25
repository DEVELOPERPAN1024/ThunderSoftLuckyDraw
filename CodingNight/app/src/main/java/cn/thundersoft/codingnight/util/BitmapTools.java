package cn.thundersoft.codingnight.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.Log;


public class BitmapTools
{
    public static Bitmap bmpReturn;

    public static Bitmap getBitmap(String path, int w, int h)
    {
        byte[] byt = null;
        try
        {
            byt = getBytes(new FileInputStream(path));
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Bitmap bit1 = readBitmapForFixMaxSize(byt, w, h);
        if (bit1 == null)
        {
            return bit1;
        }
        Bitmap temp = setAlpha(bit1, 200);
        //		Bitmap matrix = getMatrix(temp, 90);

        return temp;
    }

    public static Bitmap getMatrix(Bitmap img, int angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        int width = img.getWidth();
        int height = img.getHeight();
        Bitmap map = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return map;
    }

    public static Bitmap getRoundedBitmap(Bitmap mBitmap, float roundPx)
    {
        Bitmap bgBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(),
            Config.ARGB_8888);
        Canvas mCanvas = new Canvas(bgBitmap);

        Paint mPaint = new Paint();
        Rect mRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        RectF mRectF = new RectF(mRect);
        mPaint.setAntiAlias(true);
        mCanvas.drawRoundRect(mRectF, roundPx, roundPx, mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mCanvas.drawBitmap(mBitmap, mRect, mRect, mPaint);

        return bgBitmap;
    }

    public static Bitmap getGrayBitmap(Bitmap mBitmap)
    {
        Bitmap mGrayBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(),
            Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mGrayBitmap);
        Paint mPaint = new Paint();

        ColorMatrix mColorMatrix = new ColorMatrix();
        mColorMatrix.setSaturation(0);
        ColorMatrixColorFilter mColorFilter = new ColorMatrixColorFilter(mColorMatrix);
        mPaint.setColorFilter(mColorFilter);
        mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);

        return mGrayBitmap;
    }

    public static Bitmap getAlphaBitmap(Bitmap mBitmap)
    {

        Bitmap mAlphaBitmap = Bitmap.createBitmap(mBitmap.getWidth(),
            mBitmap.getHeight(), Config.ARGB_8888);

        Canvas mCanvas = new Canvas(mAlphaBitmap);
        Paint mPaint = new Paint();

        mPaint.setColor(Color.BLUE);
        Bitmap alphaBitmap = mBitmap.extractAlpha();
        mCanvas.drawBitmap(alphaBitmap, 0, 0, mPaint);

        return mAlphaBitmap;
    }

    public static Bitmap getScaleBitmap(Bitmap mBitmap, float dex)
    {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(dex, dex);
        Bitmap mScaleBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix,
            true);

        return mScaleBitmap;
    }

    public static Bitmap getScrewBitmap(Bitmap mBitmap, float dexx, float dexy)
    {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preSkew(dexx, dexy);
        Bitmap mScrewBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix,
            true);

        return mScrewBitmap;
    }

    public static Bitmap setScale(Bitmap img, float dex, int wi, int he)
    {
        float tempw = (float) wi;
        float temph = (float) he;
        float w = tempw * dex;
        float h = temph * dex;
        if (w <= 0 || h <= 0)
        {
            return null;
        }

        Bitmap mBitmap = Bitmap.createScaledBitmap(img, (int) w, (int) h, true);
        return mBitmap;
    }

    public static void getPartBitmap(Bitmap bitmap)
    {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap bm = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.STROKE);
        int deltX = 76;
        int deltY = 98;
        DashPathEffect dashStyle = new DashPathEffect(new float[]{10, 5, 5, 5}, 2);//�������߱߿���ʽ
        RectF faceRect = new RectF(0, 0, 88, 106);
        float[] faceCornerii = new float[]{30, 30, 30, 30, 75, 75, 75, 75};
        mPaint.setColor(0xFF6F8DD5);
        mPaint.setStrokeWidth(6);
        mPaint.setPathEffect(dashStyle);
        Path clip = new Path();
        clip.reset();
        clip.addRoundRect(faceRect, faceCornerii, Direction.CW);
        canvas.save();
        canvas.translate(deltX, deltY);
        canvas.clipPath(clip, Region.Op.DIFFERENCE);
        canvas.drawColor(0xDF222222);
        canvas.drawPath(clip, mPaint);
        canvas.restore();
    }

    public static Bitmap getReflectedBitmap(Bitmap mBitmap)
    {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap mInverseBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix,
            false);
        Bitmap mReflectedBitmap = Bitmap
                .createBitmap(width, height * 2, Config.ARGB_8888);

        Canvas mCanvas = new Canvas(mReflectedBitmap);
        mCanvas.drawBitmap(mBitmap, 0, 0, null);
        mCanvas.drawBitmap(mInverseBitmap, 0, height, null);

        Paint mPaint = new Paint();
        Shader mShader = new LinearGradient(0, height, 0, mReflectedBitmap.getHeight(),
            0x70ffffff, 0x00ffffff, TileMode.MIRROR);
        mPaint.setShader(mShader);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawRect(0, height, width, mReflectedBitmap.getHeight(), mPaint);

        return mReflectedBitmap;
    }

    public static Bitmap setAlpha(Bitmap sourceImg, int number)
    {

        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];

        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg.getWidth(),
            sourceImg.getHeight());

        number = number * 255 / 100;

        for (int i = 0; i < argb.length; i++)
        {

            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
        }

        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(),
            sourceImg.getHeight(), Config.ARGB_8888);

        return sourceImg;

    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
            int reqHeight)
    {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        if (inSampleSize < 1)
        {
            inSampleSize = 1;
        }

        return inSampleSize;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength,
            int maxNumOfPixels)
    {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8)
        {
            roundedSize = 1;
            while (roundedSize < initialSize)
            {
                roundedSize <<= 1;
            }
        }
        else
        {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels)
    {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h
            / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
            Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound)
        {
            // return the larger one when there is no overlapping zone. 
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1))
        {
            return 1;
        }
        else if (minSideLength == -1)
        {
            return lowerBound;
        }
        else
        {
            return upperBound;
        }
    }

    public static Bitmap readBitmapForFixMaxSize(byte[] bt, int iMaxWidth, int iMaxHeight)
    {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bt, 0, bt.length, options);
        int iSampleSize = calculateInSampleSize(options, iMaxWidth, iMaxHeight);
        final int minSideLength = Math.min(iMaxWidth, iMaxHeight);
        int maxNumOfPixels = iMaxWidth * iMaxHeight;
        int sample = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int newsample = computeSampleSize(options, minSideLength, maxNumOfPixels);

        Log.d("MS", "common sample = " + iSampleSize + "  new sample = " + sample
            + "  third sample  " + newsample);

        options = initOptions();
        options.inSampleSize = iSampleSize;
        options.inJustDecodeBounds = false;
        int result = gotoGetImageWithoutException(bt, options);
        while (result < 0)
        {
            options.inJustDecodeBounds = true;
            iSampleSize = iSampleSize + 2;
            options.inSampleSize = iSampleSize;
            options.inJustDecodeBounds = false;
            result = gotoGetImageWithoutException(bt, options);
        }
        return bmpReturn;
    }

    private static int gotoGetImageWithoutException(byte[] bt,
            BitmapFactory.Options options)
    {
        try
        {

            bmpReturn = BitmapFactory.decodeByteArray(bt, 0, bt.length, options);

        }
        catch (OutOfMemoryError e)
        {
            return -1;
        }
        return 0;
    }

    public static BitmapFactory.Options initOptions()
    {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Config.ARGB_8888;
        opts.inInputShareable = true;
        opts.inTempStorage = new byte[6 * 1024];

        return opts;
    }

    private static byte[] getBytes(InputStream is) throws IOException
    {

        BufferedInputStream bis = null;
        ByteArrayOutputStream out = null;
        try
        {
            bis = new BufferedInputStream(is, 1024 * 8);
            out = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = bis.read(buffer)) != -1)
            {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            bis.close();
        }
        catch (MalformedURLException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        byte[] data = out.toByteArray();
        return data;
    }
    
    public static void saveBitmap(Bitmap b, String path)
    {

        try
        {
            FileOutputStream fout = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void deleteBitmap(String path)
    {
        File file = new File(path);
        if (file.exists() && file.isFile())
        {
            file.delete();
        }
    }
}
