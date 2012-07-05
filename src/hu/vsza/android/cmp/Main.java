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
        int color = circles.getCurrentColor();
        switch (seekBar.getId()) {
            case R.id.seekbar_red:
                color = color & 0xff00ffff | progress << 16;
                break;
            case R.id.seekbar_green:
                color = color & 0xffff00ff | progress << 8;
                break;
            case R.id.seekbar_blue:
                color = color & 0xffffff00 | progress;
                break;
        }
        circles.setCurrentColor(color);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
