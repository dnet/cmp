package hu.vsza.android.cmp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.graphics.Color;

public class Main extends Activity implements SeekBar.OnSeekBarChangeListener, ColorChangeListener
{
    protected ColourCircles circles;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        circles = (ColourCircles)findViewById(R.id.circles);
        circles.setColorChangeListener(this);
        for (int id : new Integer[] {R.id.seekbar_red, R.id.seekbar_green, R.id.seekbar_blue}) {
            SeekBar sb = (SeekBar)findViewById(id);
            sb.setOnSeekBarChangeListener(this);
        }
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
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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
