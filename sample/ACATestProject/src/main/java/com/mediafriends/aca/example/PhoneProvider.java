package com.mediafriends.aca.example;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;

import java.util.ArrayList;


public class PhoneProvider extends ContentProvider
{
    public static final Uri CONTENT_URI =
            Uri.parse("content://com.mediafriends.aca.example.provider");

    private static final ArrayList<AndroidPhone> phones = new ArrayList<AndroidPhone>();

    @Override
    public boolean onCreate ()
    {
        // We're not going to create a database here. Instead we're just oign to hold a list in memory.
        phones.add(new AndroidPhone(Build.VERSION_CODES.BASE, "HTC G1"));
        phones.add(new AndroidPhone(Build.VERSION_CODES.CUPCAKE, "HTC Magic"));
        phones.add(new AndroidPhone(Build.VERSION_CODES.DONUT, "Motorola CLIQ"));
        phones.add(new AndroidPhone(Build.VERSION_CODES.DONUT, "HTC Hero"));
        phones.add(new AndroidPhone(Build.VERSION_CODES.ECLAIR, "Motorola Droid"));
        phones.add(new AndroidPhone(Build.VERSION_CODES.ECLAIR, "Samsung Galaxy S"));
        phones.add(new AndroidPhone(Build.VERSION_CODES.ECLAIR_MR1, "Nexus One"));
        phones.add(new AndroidPhone(Build.VERSION_CODES.FROYO, "HTC Incredible S"));
        phones.add(new AndroidPhone(Build.VERSION_CODES.GINGERBREAD, "Motorola Droid 2"));
        phones.add(new AndroidPhone(Build.VERSION_CODES.GINGERBREAD, "Samsung Nexus S"));
        phones.add(new AndroidPhone(Build.VERSION_CODES.GINGERBREAD, "Samsung Galaxy S2"));
        phones.add(new AndroidPhone(Build.VERSION_CODES.ICE_CREAM_SANDWICH, "Samsung Galaxy Nexus"));
        phones.add(new AndroidPhone(Build.VERSION_CODES.ICE_CREAM_SANDWICH, "Samsung Galaxy S3"));
        phones.add(new AndroidPhone(Build.VERSION_CODES.JELLY_BEAN, "Motorola Droid Razr"));
        phones.add(new AndroidPhone(Build.VERSION_CODES.JELLY_BEAN_MR1, "LG Nexus 4"));

        return true;
    }

    @Override
    public synchronized Cursor query (Uri uri, String[] projection, String selection,
                                      String[] selectionArgs, String sortOrder)
    {
        assert (uri.getPathSegments().isEmpty());

        // In this sample, we only query without any parameters, so we can just return a cursor to
        // all the weather data.
        final MatrixCursor c = new MatrixCursor(
                new String[]{Columns.ID, Columns.MODEL, Columns.SDK_INT});
        for (int i = 0; i < phones.size(); ++i)
        {
            final AndroidPhone phone = phones.get(i);
            c.addRow(new Object[]{new Integer(i), phone.model, new Integer(phone.sdkInt)});
        }
        return c;
    }

    @Override
    public String getType (Uri uri)
    {
        return "vnd.android.cursor.dir/vnd.weatherlistwidget.temperature";
    }

    @Override
    public Uri insert (Uri uri, ContentValues values)
    {
        // no-op
        return null;
    }

    @Override
    public int delete (Uri uri, String selection, String[] selectionArgs)
    {
        // no-op
        return 0;
    }

    @Override
    public synchronized int update (Uri uri, ContentValues values, String selection,
                                    String[] selectionArgs)
    {
        // no-op
        return 0;
    }

    public static class Columns
    {
        public static final String ID = "_id";
        public static final String MODEL = "model";
        public static final String SDK_INT = "sdk_int";
    }

    public static final class AndroidPhone
    {

        private final int sdkInt;
        private final String model;

        public AndroidPhone (int sdkInt, String model)
        {
            this.sdkInt = sdkInt;
            this.model = model;
        }
    }

}

