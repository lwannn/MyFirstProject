package com.ytj.project_login.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * ѹ��ͼƬ�Ĺ�����
 * compressImg:����Bitmap������ѹ��
 * getimage������ͼƬ·������ѹ���������compressImg
 * <p/>
 * ��CicleImageViewһ���û��ó�ͻ������
 *
 * @author luowang
 */
public class compressImgUtil {

    public static String compressImg(Bitmap bitmap) {
        Bitmap imgBitmap = bitmap;
        // ѹ��ͼƬ
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// ���Ǻܶ�����࣬���Բ����ڴ滺���������ݣ�ת�����ֽ����顣
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��

        int options = 100;
        while (baos.toByteArray().length / 1024 > 300) {// ѭ���ж����ѹ�����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
            baos.reset();// ����baos�����baos
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;// ÿ�ζ�����10
        }

        System.out.println(baos.toByteArray().length / 1024);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
//        imgBitmap = BitmapFactory.decodeStream(bais, null, null);//这个代码运行之后会把bais清空

        //将压缩的图片转存到“图片目录下”
        String path = Environment.getExternalStorageDirectory() + "/图片/icon.jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int len = 0;
        byte[] b = new byte[1024];
        try {
            while ((len = bais.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            baos.close();
            bais.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return imgBitmap;
        return path;
    }

    public static String getimage(String srcPath, Context context) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//��ʱ����bmΪ��

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ
        int px = DensityUtil.dip2px(context, 300);
        float scale = px / 360;
        float hh = 600f * scale;//�������ø߶�Ϊ800f
        float ww = 360f * scale;//�������ÿ��Ϊ480f
        //���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
        int be = 1;//be=1��ʾ������
        if (w > h && w > ww) {//�����ȴ�Ļ����ݿ�ȹ̶���С����
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//����߶ȸߵĻ����ݿ�ȹ̶���С����
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//�������ű���
        //���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImg(bitmap);//ѹ���ñ�����С���ٽ�������ѹ��
    }
}
