package com.malta.birdlife.recipesprinter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;

import com.zj.btsdk.PrintPic;
/*
*  @this class will check the content and remove scripts and others things to have a good result.
        *  @for printing web page
        */
public class PrintPicEx extends PrintPic {


    public PrintPicEx() {
        super();
    }

    public void drawImageResource(float x, float y, Resources res,int id) {
        try {
            Bitmap e = BitmapFactory.decodeResource(res,id);
            this.canvas.drawBitmap(e, x, y, (Paint)null);
            if(this.length < y + (float)e.getHeight()) {
                this.length = y + (float)e.getHeight();
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }
}
