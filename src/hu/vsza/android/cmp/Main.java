package hu.vsza.android.cmp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;

public class Main extends Activity implements SeekBar.OnSeekBarChangeListener
{
    protected ColourCircles circles;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        circles = (ColourCircles)findViewById(R.id.circles);
        for (int id : new Integer[] {R.id.seekbar_red, R.id.seekbar_green, R.id.seekbar_blue}) {
            SeekBar sb = (SeekBar)findViewById(id);
            sb.setOnSeekBarChangeListener(this);
        }
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
