package hu.vsza.android.cmp;

import android.view.*;
import android.graphics.*;
import android.content.Context;
import android.widget.Toast;
import android.util.AttributeSet;
import android.os.Bundle;

public class ColourCirclesEditor extends ColourCircles {
    protected ColorChangeListener color_change_listener = null;

    public void setColorChangeListener(ColorChangeListener ccl) {
        color_change_listener = ccl;
    }

    public ColourCirclesEditor(Context context) {
        super(context);
        init(context);
    }

    public ColourCirclesEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColourCirclesEditor(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
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

    public void setColors(int[] colors) {
        super.setColors(colors);
        fireColorChange();
    }
}
