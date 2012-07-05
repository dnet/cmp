package hu.vsza.android.cmp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.graphics.Color;

public class Main extends Activity implements SeekBar.OnSeekBarChangeListener, ColorChangeListener
{
    protected ColourCircles circles;
    protected final static int HUE = 0, SATURATION = 1, VALUE = 2;
    protected final static float SV_SCALE = 100.0f;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        circles = (ColourCircles)findViewById(R.id.circles);
        circles.setColorChangeListener(this);
        for (int id : new Integer[] {R.id.seekbar_red, R.id.seekbar_green,
                R.id.seekbar_blue, R.id.seekbar_hue, R.id.seekbar_saturation,
                R.id.seekbar_value}) {
            SeekBar sb = (SeekBar)findViewById(id);
            sb.setOnSeekBarChangeListener(this);
        }
        onColorChanged(circles.getCurrentColor());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        circles.saveToBundle(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        circles.loadFromBundle(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onColorChanged(int color) {
        ((SeekBar)findViewById(R.id.seekbar_red)).setProgress(Color.red(color));
        ((SeekBar)findViewById(R.id.seekbar_green)).setProgress(Color.green(color));
        ((SeekBar)findViewById(R.id.seekbar_blue)).setProgress(Color.blue(color));
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        ((SeekBar)findViewById(R.id.seekbar_hue)).setProgress((int)hsv[HUE]);
        ((SeekBar)findViewById(R.id.seekbar_saturation)).setProgress((int)(hsv[SATURATION] * SV_SCALE));
        ((SeekBar)findViewById(R.id.seekbar_value)).setProgress((int)(hsv[VALUE] * SV_SCALE));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) return;
        switch (seekBar.getId()) {
            case R.id.seekbar_red:
                changeColorWithMaskShift(progress, 0xff00ffff, 16);
                break;
            case R.id.seekbar_green:
                changeColorWithMaskShift(progress, 0xffff00ff, 8);
                break;
            case R.id.seekbar_blue:
                changeColorWithMaskShift(progress, 0xffffff00, 0);
                break;
        }
    }

    public void changeColorWithMaskShift(int value, int mask, int shift) {
        int color = circles.getCurrentColor();
        color = color & mask | value << shift;
        circles.setCurrentColor(color);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
