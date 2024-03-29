package hu.vsza.android.cmp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.*;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.content.DialogInterface;
import android.content.Intent;

public class Main extends Activity implements SeekBar.OnSeekBarChangeListener, ColorChangeListener
{
    protected ColourCirclesEditor circles;
    protected final float[] hsv = new float[3];
    protected final static int HUE = 0, SATURATION = 1, VALUE = 2;
    protected final static float SV_SCALE = 100.0f;
    protected final static int LOAD_COLORSET = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        circles = (ColourCirclesEditor)findViewById(R.id.circles);
        circles.setColorChangeListener(this);
        final int[] seekbar_ids = {R.id.seekbar_red, R.id.seekbar_green,
            R.id.seekbar_blue, R.id.seekbar_hue, R.id.seekbar_saturation,
            R.id.seekbar_value};
        for (int i = 0; i < seekbar_ids.length; i++) {
            SeekBar sb = (SeekBar)findViewById(seekbar_ids[i]);
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
            case R.id.seekbar_hue:
                changeColorWithHSV(HUE, progress);
                break;
            case R.id.seekbar_saturation:
                changeColorWithHSV(SATURATION, (float)progress / SV_SCALE);
                break;
            case R.id.seekbar_value:
                changeColorWithHSV(VALUE, (float)progress / SV_SCALE);
                break;
        }
    }

    public void changeColorWithMaskShift(int value, int mask, int shift) {
        int color = circles.getCurrentColor();
        color = color & mask | value << shift;
        circles.setCurrentColor(color);
    }

    public void changeColorWithHSV(int index, float value) {
        hsv[index] = value;
        circles.setCurrentColor(Color.HSVToColor(hsv));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.circle_count_2:
                circles.setCount(2);
                item.setChecked(true);
                break;
            case R.id.circle_count_3:
                circles.setCount(3);
                item.setChecked(true);
                break;
            case R.id.circle_count_5:
                circles.setCount(5);
                item.setChecked(true);
                break;
            case R.id.save_color_set:
                saveColorSet();
                break;
            case R.id.load_color_set:
                startActivityForResult(new Intent(this, ColorSetList.class),
                        LOAD_COLORSET);
                break;
            case R.id.about_current_color:
                showColorInfo();
                break;
        }
        return false;
    }

    public void saveColorSet() {
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this)
            .setTitle(R.string.save_color_set)
            .setMessage(R.string.enter_color_set_name)
            .setView(et)
            .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    new ColorStore(Main.this).save(new ColorStore.ColorSet(
                            et.getText().toString(), circles.getColors()));
                }
            })
        .setNegativeButton(R.string.cancel, null)
            .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == LOAD_COLORSET && resultCode == RESULT_OK) {
            final int[] colors = data.getIntArrayExtra(ColorSetList.SELECTED_COLORSET);
            circles.setColors(colors);
        }
    }

    protected void showColorInfo() {
        final int current_color = circles.getCurrentColor();
        new AlertDialog.Builder(this)
            .setTitle(R.string.about_current_color)
            .setMessage(getString(R.string.color_info,
                        Color.red(current_color),
                        Color.green(current_color),
                        Color.blue(current_color)))
            .setNeutralButton(R.string.close, null)
            .show();
    }
}
