package com.mediafriends.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class AdvancedCursorAdapter extends CursorAdapter
{
    /**
     * A list of columns containing the data to bind to the UI.
     * This field should be made private, so it is hidden from the SDK.
     * {@hide}
     */
    protected int[] mFrom;
    /**
     * A list of View ids representing the views to which the data must be bound.
     * This field should be made private, so it is hidden from the SDK.
     * {@hide}
     */
    protected int[] mTo;
    protected String[] mOriginalFrom;

    ViewBinder viewBinder;

    private static final String TAG = AdvancedCursorAdapter.class.getSimpleName();

    public AdvancedCursorAdapter(Context context, String[] from, int[] to)
    {
        super(context, null, 0);

        mOriginalFrom = from;
        mTo = to;
        findColumns(from);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v;

        if (!mDataValid)
        {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position))
        {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }

        if (convertView == null)
        {
            v = newView(mContext, mCursor, parent);
        } else
        {
            v = convertView;
        }

        bindView(v, mContext, mCursor);
        return v;
    }

    /**
     * Create a map from an array of strings to an array of column-id integers in mCursor.
     * If mCursor is null, the array will be discarded.
     *
     * @param from the Strings naming the columns of interest
     */
    private void findColumns(String[] from)
    {
        if (mCursor != null)
        {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count)
            {
                mFrom = new int[count];
            }
            for (i = 0; i < count; i++)
            {
                mFrom[i] = mCursor.getColumnIndexOrThrow(from[i]);
            }
        } else
        {
            mFrom = null;
        }
    }

    /**
     * Binds all of the field names passed into the "to" parameter of the
     * constructor with their corresponding cursor columns as specified in the
     * "from" parameter.
     *
     * Binding occurs in two phases. First, if a
     * {@link android.widget.SimpleCursorAdapter.ViewBinder} is available,
     * {@link ViewBinder#setViewValue(android.view.View, android.database.Cursor, int)}
     * is invoked. If the returned value is true, binding has occured. If the
     * returned value is false and the view to bind is a TextView,
     * {@link #setViewText(TextView, String)} is invoked. If the returned value is
     * false and the view to bind is an ImageView,
     * {@link #setViewImage(ImageView, String)} is invoked. If no appropriate
     * binding can be found, an {@link IllegalStateException} is thrown.
     *
     * @throws IllegalStateException if binding cannot occur
     *
     * @see android.widget.CursorAdapter#bindView(android.view.View,
     *      android.content.Context, android.database.Cursor)
     * @see #getViewBinder()
     * @see #setViewBinder(android.widget.SimpleCursorAdapter.ViewBinder)
     * @see #setViewImage(ImageView, String)
     * @see #setViewText(TextView, String)
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {

        final ViewBinder binder = viewBinder;
        final int count = mTo.length;
        final int[] from = mFrom;
        final int[] to = mTo;

        for (int i = 0; i < count; i++)
        {
            final View v = view.findViewById(to[i]);
            if (v != null)
            {
                boolean bound = false;
                if (binder != null)
                {
                    bound = binder.setViewValue(v, cursor, from[i]);
                }

                if (!bound)
                {
                    String text = cursor.getString(from[i]);
                    if (text == null)
                    {
                        text = "";
                    }

                    if (v instanceof TextView)
                    {
                        setViewText((TextView)v, text);
                    } else if (v instanceof ImageView)
                    {
                        setViewImage((ImageView)v, text);
                    } else
                    {
                        throw new IllegalStateException(v.getClass().getName() + " is not a view that can be bound by "
                                                                + this.getClass().getSimpleName() + ". Try using a ViewBinder!");
                    }
                }
            }
        }

    }

    protected void setViewImage(ImageView v, String text)
    {
        // TODO Use this for loading images?
    }

    protected void setViewText(TextView v, String text)
    {
        v.setText(text);
    }


    @Override
    public Cursor swapCursor(Cursor c)
    {
        Cursor arg1 = super.swapCursor(c);
        // rescan columns in case cursor layout is different
        findColumns(mOriginalFrom);
        return arg1;
    }

    /**
     * Change the cursor and change the column-to-view mappings at the same time.
     *
     * @param c The database cursor.  Can be null if the cursor is not available yet.
     * @param from A list of column names representing the data to bind to the UI.  Can be null
     *            if the cursor is not available yet.
     * @param to The views that should display column in the "from" parameter.
     *            These should all be TextViews. The first N views in this list
     *            are given the values of the first N columns in the from
     *            parameter.  Can be null if the cursor is not available yet.
     */
    public void changeCursorAndColumns(Cursor c, String[] from, int[] to)
    {
        mOriginalFrom = from;
        mTo = to;
        super.changeCursor(c);
        findColumns(mOriginalFrom);
    }

    public static interface ViewBinder
    {
        /**
         * Binds the Cursor column defined by the specified index to the specified view.
         *
         * When binding is handled by this ViewBinder, this method must return true.
         * If this method returns false, SimpleCursorAdapter will attempts to handle
         * the binding on its own.
         *
         * @param view the view to bind the data to
         * @param cursor the cursor to get the data from
         * @param columnIndex the column at which the data can be found in the cursor
         *
         * @return true if the data was bound to the view, false otherwise
         */
        boolean setViewValue(View view, Cursor cursor, int columnIndex);
    }

    public ViewBinder getViewBinder()
    {
        return viewBinder;
    }

    public void setViewBinder(ViewBinder viewBinder)
    {
        this.viewBinder = viewBinder;
    }

}

