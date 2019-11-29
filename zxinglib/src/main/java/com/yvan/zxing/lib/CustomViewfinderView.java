package com.yvan.zxing.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.yvan.zxing.lib.core.ResultPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yvan on 2019-11-29.
 */
public class CustomViewfinderView extends ViewfinderView {

    public int laserLinePosition = 0;
    public float[] position = new float[]{0f, 0.5f, 1f};
    public int[] colors = new int[]{0x00FFAF21, 0xFFFFAF21, 0x00FFAF21};
    public LinearGradient linearGradient;

    public CustomViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重写draw方法绘制自己的扫描框
     *
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
        if (cameraManager == null) {
            return; // not ready yet, early draw before done configuring
        }
        Rect frame = cameraManager.getFramingRect();
        Rect previewFrame = cameraManager.getFramingRectInPreview();
        if (frame == null || previewFrame == null) {
            return;
        }

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        //绘制4个角

        paint.setColor(0xFFFFAF21);//定义画笔的颜色
        canvas.drawRect(frame.left, frame.top, frame.left + 50, frame.top + 5, paint);
        canvas.drawRect(frame.left, frame.top, frame.left + 5, frame.top + 50, paint);


        canvas.drawRect(frame.right - 50, frame.top, frame.right, frame.top + 5, paint);
        canvas.drawRect(frame.right - 5, frame.top, frame.right, frame.top + 50, paint);


        canvas.drawRect(frame.left, frame.bottom - 5, frame.left + 50, frame.bottom, paint);
        canvas.drawRect(frame.left, frame.bottom - 50, frame.left + 5, frame.bottom, paint);


        canvas.drawRect(frame.right - 50, frame.bottom - 5, frame.right, frame.bottom, paint);
        canvas.drawRect(frame.right - 5, frame.bottom - 50, frame.right, frame.bottom, paint);
        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);


        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, frame, paint);
        } else {
            // paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
            // scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
            int middle = frame.height() / 2 + frame.top;
            laserLinePosition = laserLinePosition + 5;
            if (laserLinePosition > frame.height()) {
                laserLinePosition = 0;
            }
            linearGradient = new LinearGradient(frame.left + 1, frame.top + laserLinePosition, frame.right - 1, frame.top + 10 + laserLinePosition, colors, position, Shader.TileMode.CLAMP);
            // Draw a red "laser scanner" line through the middle to show decoding is active


            // paint.setColor(laserColor);
            paint.setShader(linearGradient);
            //绘制扫描线
            canvas.drawRect(frame.left + 1, frame.top + laserLinePosition, frame.right - 1, frame.top + 10 + laserLinePosition, paint);
            paint.setShader(null);
            float scaleX = frame.width() / (float) previewFrame.width();
            float scaleY = frame.height() / (float) previewFrame.height();


            List<ResultPoint> currentPossible = possibleResultPoints;
            List<ResultPoint> currentLast = lastPossibleResultPoints;
            int frameLeft = frame.left;
            int frameTop = frame.top;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new ArrayList<>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(CURRENT_POINT_OPACITY);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            POINT_SIZE, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(CURRENT_POINT_OPACITY / 2);
                paint.setColor(resultPointColor);
                float radius = POINT_SIZE / 2.0f;
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            radius, paint);
                }
            }
            postInvalidateDelayed(16,
                    frame.left,
                    frame.top,
                    frame.right,
                    frame.bottom);
            // postInvalidate();
        }
    }

}
