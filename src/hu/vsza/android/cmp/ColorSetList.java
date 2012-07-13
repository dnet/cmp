package hu.vsza.android.cmp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ColorSetList extends ListActivity
{
    public static final String SELECTED_COLORSET = "hu.vsza.android.cmp.ColorSetList.SELECTED_COLORSET";
	protected List<ColorStore.ColorSet> colorsets;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        colorsets = new ColorStore(this).getAll();
        final LayoutInflater factory = LayoutInflater.from(this);

		setListAdapter(new ArrayAdapter<ColorStore.ColorSet>(this,
                   R.layout.colorset_list_item, colorsets) {
            public View getView(int position, View convertView, ViewGroup parent) {
                final View view = factory.inflate(R.layout.colorset_list_item, parent, false);
                final ColourCircles circles = (ColourCircles)view.findViewById(R.id.circles);
                final ColorStore.ColorSet current_colorset = colorsets.get(position);
                circles.setColors(current_colorset.getRawColors());
                final TextView tv = (TextView)view.findViewById(R.id.name);
                tv.setText(current_colorset.getName());
                return view;
            }
        });
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
        final ColorStore.ColorSet selected_colorset = colorsets.get(position); 
        final Intent resultData = new Intent()
            .putExtra(SELECTED_COLORSET, selected_colorset.getRawColors());
        setResult(RESULT_OK, resultData);
        finish();
	}
}
