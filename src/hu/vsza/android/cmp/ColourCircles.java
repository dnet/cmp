package hu.vsza.android.cmp;

import android.view.*;
import android.graphics.*;
import android.content.Context;
import android.widget.Toast;
import android.util.AttributeSet;
import android.os.Bundle;

public class ColourCircles extends View {
    protected int colors[] = {Color.RED, Color.GREEN, Color.BLUE};
    protected Object color_lock = new Object();
    protected final static float margin = 0.03f;
    protected int selected_color_index = 0;
    protected final static String BUNDLE_KEY_COLORS = "hu.vsza.android.cmp.ColourCircles.colors";
    protected final static String BUNDLE_KEY_COLOR_INDEX =
        "hu.vsza.android.cmp.ColourCircles.selected_color_index";

    public ColourCircles(Context context) {
        super(context);
    }

    public ColourCircles(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColourCircles(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void loadFromBundle(Bundle b) {
        if (b.containsKey(BUNDLE_KEY_COLORS) && b.containsKey(BUNDLE_KEY_COLOR_INDEX)) {
            synchronized (color_lock) {
                colors = b.getIntArray(BUNDLE_KEY_COLORS);
                selected_color_index = b.getInt(BUNDLE_KEY_COLOR_INDEX);
            }
            invalidate();
        }
    }

    public void saveToBundle(Bundle b) {
        synchronized (color_lock) {
            b.putIntArray(BUNDLE_KEY_COLORS, colors);
            b.putInt(BUNDLE_KEY_COLOR_INDEX, selected_color_index);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float size = getCircleSize();
        for (int i = 0; i < colors.length; i++) {
            RectF rect = new RectF((i + margin) * size, margin * size,
                    (i + 1 - margin) * size - 1, (1 - margin) * size - 1);
            Paint paint = new Paint();
            synchronized (color_lock) {
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

    public int[] getColors() {
        synchronized (color_lock) {
            int[] retval = new int[colors.length];
            for (int i = 0; i < colors.length; i++) {
                retval[i] = colors[i];
            }
            return retval;
        }
    }

    public void setColors(int[] colors) {
        final int count = colors.length;
        synchronized (color_lock) {
            this.colors = new int[count];
            for (int i = 0; i < count; i++) {
                this.colors[i] = colors[i];
            }
            if (selected_color_index >= count) {
                selected_color_index = count - 1;
            }
        }
        requestLayout();
    }
}
