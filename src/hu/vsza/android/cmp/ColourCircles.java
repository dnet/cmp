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
    protected ColorChangeListener color_change_listener = null;
    protected final static String BUNDLE_KEY_COLORS = "hu.vsza.android.cmp.ColourCircles.colors";
    protected final static String BUNDLE_KEY_COLOR_INDEX =
        "hu.vsza.android.cmp.ColourCircles.selected_color_index";

    public void setColorChangeListener(ColorChangeListener ccl) {
        color_change_listener = ccl;
    }

    public ColourCircles(Context context) {
        super(context);
        init(context);
    }

    public ColourCircles(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColourCircles(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
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

    protected void init(final Context c) {
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int new_color_index = (int)(event.getX() / getCircleSize());
                    synchronized (color_lock) {
                        if (new_color_index != selected_color_index) {
                            selected_color_index = new_color_index;
                            fireColorChange();
                            Toast.makeText(c,
                                c.getString(R.string.selected_color_index_changed,
                                    selected_color_index + 1),
                                Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return true;
            }
        });
    }

    protected void fireColorChange() {
        if (color_change_listener != null) {
            color_change_listener.onColorChanged(colors[selected_color_index]);
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
        synchronized (color_lock) {
            colors[selected_color_index] = color;
            fireColorChange();
        }
        invalidate();
    }

    public int getCurrentColor() {
        synchronized (color_lock) {
            return colors[selected_color_index];
        }
    }

    public void setCount(int count) {
        if (count == colors.length) return;
        int[] new_colors = new int[count];
        for (int i = 0; i < count; i++) {
            new_colors[i] = (i >= colors.length) ? Color.RED : colors[i];
        }
        boolean index_changed;
        synchronized (color_lock) {
            colors = new_colors;
            index_changed = selected_color_index >= count;
            if (index_changed) {
                selected_color_index = count - 1;
            }
        }
        if (index_changed) {
            fireColorChange();
        }
        requestLayout();
    }
}
