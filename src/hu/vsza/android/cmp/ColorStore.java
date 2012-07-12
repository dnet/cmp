package hu.vsza.android.cmp;

import android.database.sqlite.*;
import android.database.Cursor;
import android.content.*;
import java.util.*;

public class ColorStore extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String SCHEMA_NAME = "colors";
    private static final String NAME_FIELD = "name";
    private static final String COLORS_FIELD = "colors";
    private static final String COLORS_TABLE_CREATE =
        "CREATE TABLE IF NOT EXISTS " + SCHEMA_NAME + " (" +
        NAME_FIELD + " TEXT PRIMARY KEY," +
        COLORS_FIELD + " TEXT)";
    private static final String SEPARATOR = ",";
    private static final int RGB_MASK = 0xFFFFFF;
    private static final int OPAQUE_COLOR = 0xFF000000;

    public ColorStore(Context context) {
        super(context, SCHEMA_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COLORS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void save(ColorSet cs) {
        final ContentValues cv = new ContentValues(2);
        cv.put(NAME_FIELD, cs.getName());
        cv.put(COLORS_FIELD, cs.getStringColors());
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.insertWithOnConflict(SCHEMA_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        } finally {
            db.close();
        }
    }

    public List<ColorSet> getAll() {
        final String[] columns = {NAME_FIELD, COLORS_FIELD};
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor c = db.query(SCHEMA_NAME, columns, null,
                    new String[] {}, null, null, null);

            List<ColorSet> colorsets = new ArrayList<ColorSet>(c.getCount());
            while (c.moveToNext()) {
                colorsets.add(new ColorSet(c.getString(0), c.getString(1)));
            }
            return colorsets;
        } finally {
            db.close();
        }
    }

    public static class ColorSet {
        protected final String name;
        protected final int[] colors;

        public ColorSet(String name, int[] colors) {
            this.name = name;
            this.colors = colors;
        }

        public ColorSet(String name, String colors) {
            this.name = name;
            final String[] parts = colors.split(SEPARATOR);
            this.colors = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                this.colors[i] = Integer.parseInt(parts[i]) | OPAQUE_COLOR;
            }
        }

        public String getName() {
            return name;
        }

        public int[] getRawColors() {
            return colors;
        }

        public String getStringColors() {
            StringBuilder sb = new StringBuilder(colors.length * 9);
            for (int i = 0; i < colors.length; i++) {
                sb.append(SEPARATOR);
                sb.append(colors[i] & RGB_MASK);
            }
            return sb.substring(1);
        }
    }
}
