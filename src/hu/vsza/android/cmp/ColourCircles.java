package hu.vsza.android.cmp;

import android.view.View;
import android.graphics.*;
import android.content.Context;
import android.util.AttributeSet;

public class ColourCircles extends View {
    protected int colors[] = new int[] {Color.RED, Color.GREEN, Color.BLUE};
    protected final static float margin = 0.03f;

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
        float size = (float)getWidth() / (float)colors.length;
        for (int i = 0; i < colors.length; i++) {
            RectF rect = new RectF((i + margin) * size, margin * size,
                    (i + 1 - margin) * size - 1, (1 - margin) * size - 1);
            Paint paint = new Paint();
            paint.setColor(colors[i]);
            canvas.drawOval(rect, paint);
        }
    }
}
