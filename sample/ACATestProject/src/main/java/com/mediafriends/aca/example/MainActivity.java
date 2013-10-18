package com.mediafriends.aca.example;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mediafriends.adapters.AdvancedCursorAdapter;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdvancedCursorAdapter adapter = new AdvancedCursorAdapter(this, new String[] {PhoneProvider.Columns.MODEL}, new int[] {android.R.id.text1})
        {
            private static final int TYPE_ANDROID_1x = 0; // Android 1.x
            private static final int TYPE_ANDROID_2x = 1; // Android 2.x
            private static final int TYPE_ANDROID_3x = 2; // Android 3.x
            private static final int TYPE_ANDROID_4x = 3; // Android 4.x


            @Override
            public View newView (Context context, Cursor cursor, ViewGroup viewGroup)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                TextView tv = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);

                int itemType = getItemViewType(cursor.getPosition());

                switch (itemType)
                {
                    case TYPE_ANDROID_1x:
                        tv.setTextColor(Color.BLACK);
                        break;
                    case TYPE_ANDROID_2x:
                        tv.setTextColor(Color.GREEN);
                        break;
                    case TYPE_ANDROID_3x:
                        tv.setTextColor(Color.BLUE);
                        break;
                    case TYPE_ANDROID_4x:
                        // Holo Dark Blue
                        tv.setTextColor(Color.argb(0xFF, 0x00, 0x99, 0xCC));
                        break;
                }

                return tv;
            }

            @Override
            public int getViewTypeCount ()
            {
                // Return the number of different view types that we have.
                return 4;
            }

            @Override
            public int getItemViewType (int position)
            {
                // Note: Integers must be in the range 0 to getViewTypeCount() - 1.

                Cursor c = getCursor();
                if(c != null && c.moveToPosition(position))
                {
                    int sdkInt = c.getInt(c.getColumnIndex(PhoneProvider.Columns.SDK_INT));
                    if(sdkInt <= Build.VERSION_CODES.DONUT)
                    {
                        return TYPE_ANDROID_1x;
                    } else if(sdkInt >= Build.VERSION_CODES.ECLAIR && sdkInt < Build.VERSION_CODES.HONEYCOMB)
                    {
                        return TYPE_ANDROID_2x;
                    } else if(sdkInt >= Build.VERSION_CODES.HONEYCOMB && sdkInt < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                    {
                        return TYPE_ANDROID_3x;
                    } else if (sdkInt >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                    {
                        return TYPE_ANDROID_4x;
                    }

                }

                return IGNORE_ITEM_VIEW_TYPE;
            }
        };

        ListView lv = (ListView) findViewById(android.R.id.list);
        if(lv != null)
        {
            lv.setAdapter(adapter);
        }

        this.getSupportLoaderManager().initLoader(0, null, this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader (int i, Bundle bundle)
    {
        return new CursorLoader(this, PhoneProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished (Loader<Cursor> cursorLoader, Cursor cursor)
    {
        ListView lv = (ListView) findViewById(android.R.id.list);
        if(lv != null && lv.getAdapter() != null)
        {
            ((AdvancedCursorAdapter) lv.getAdapter()).swapCursor(cursor);
        }

    }

    @Override
    public void onLoaderReset (Loader<Cursor> cursorLoader)
    {

    }
}
