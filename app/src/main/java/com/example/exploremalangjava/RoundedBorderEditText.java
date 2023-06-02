package com.example.exploremalangjava;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class RoundedBorderEditText extends AppCompatEditText {
    private Paint borderPaint;
    private Paint backgroundPaint;
    private RectF borderRect;

    public RoundedBorderEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Set warna border
        int borderColor = Color.BLACK;
        // Set warna background
        int backgroundColor = Color.WHITE;

        // Mengatur atribut paint untuk border
        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2f);
        borderPaint.setColor(borderColor);

        // Mengatur atribut paint untuk background
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(backgroundColor);

        // Hilangkan garis bawah
        setBackground(null);

        borderRect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Mendapatkan lebar dan tinggi EditText
        float width = getWidth();
        float height = getHeight();

        // Mengatur batas border rounded
        borderRect.set(0f, 0f, width, height);

        // Menggambar background dengan border rounded
        canvas.drawRoundRect(borderRect, height / 2, height / 2, backgroundPaint);

        // Menggambar border rounded
        canvas.drawRoundRect(borderRect, height / 2, height / 2, borderPaint);

        // Memanggil onDraw dari superclass untuk menggambar teks
        super.onDraw(canvas);
    }
}
