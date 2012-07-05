package hu.vsza.android.cmp;

import android.view.View;
import android.graphics.*;
import android.content.Context;
import android.util.AttributeSet;

public class ColourCircles extends View {
    protected int colors[] = new int[] {Color.RED, Color.GREEN, Color.BLUE};
    protected final static float margin = 0.03f;
    protected int selected_color_index = 0;

    public ColourCircles(Context context) {
        super(context);
    }

    public ColourCircles(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColourCircles(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float size = getCircleSize();
        for (int i = 0; i < colors.length; i++) {
            RectF rect = new RectF((i + margin) * size, margin * size,
                    (i + 1 - margin) * size - 1, (1 - margin) * size - 1);
            Paint paint = new Paint();
            synchronized (colors) {
                paint.setColor(colors[i]);
            }
            canvas.drawOval(rect, paint);
        }
    }

    protected float getCircleSize() {
        return (float)getWidth() / (float)colors.length;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(widthSize, widthSize / colors.length);
    }

    public void setCurrentColor(int color) {
        synchronized (colors) {
            colors[selected_color_index] = color;
        }
        invalidate();
    }

    public int getCurrentColor() {
        synchronized (colors) {
            return colors[selected_color_index];
        }
    }
}
