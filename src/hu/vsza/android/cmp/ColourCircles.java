package hu.vsza.android.cmp;

import android.view.*;
import android.graphics.*;
import android.content.Context;
import android.widget.Toast;
import android.util.AttributeSet;

public class ColourCircles extends View {
    protected int colors[] = new int[] {Color.RED, Color.GREEN, Color.BLUE};
    protected final static float margin = 0.03f;
    protected int selected_color_index = 0;
    protected ColorChangeListener color_change_listener = null;

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

    protected void init(Context context) {
        final Context c = context;
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int new_color_index = (int)(event.getX() / getCircleSize());
                    synchronized (colors) {
                        if (new_color_index != selected_color_index) {
                            selected_color_index = new_color_index;
                            if (color_change_listener != null) {
                                color_change_listener.onColorChanged(colors[selected_color_index]);
                            }
                            Toast.makeText(c,
                                c.getString(R.string.selected_color_index_changed,
                                    selected_color_index), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return true;
            }
        });
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
