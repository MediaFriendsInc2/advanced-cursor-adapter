Advanced Cursor Adapter
=======================

Implementation of CursorAdapter that supports multiple view types in a ListView

Features
----------
* Support for multiple view types in a ListAdapter
* Supports ListView's recycled views
* Standard CursorAdapter interface with from and to bindings
* Supports ViewBinder for custom bindings
* Can be used with LoaderManager
* Compatable with Android 2.1+

Usage
----------
Extend the AdvancedCursorAdapter base class and override the newView() method. If using for multiple view types user will also need to override getItemViewType(int position) and getViewTypeCount()

```java
AdvancedCursorAdapter adapter = new AdvancedCursorAdapter(Context c, String[] from, int[] to)
{

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup viewGroup)
    {
         int itemType = getItemViewType(cursor.getPosition());
         View itemView;
     
        switch (itemType)
        {
            // Inflate or configure your view depending on item type
        }
    
        return itemView;
    }
}
```

AdvancedCursorAdapter will automatically bind data to TextViews by default. Other views (ImageViews, custom views, etc) can be bound using the standard ViewBinder syntax.
```java
adapter.setViewBinder(new AdvancedCursorAdapter.ViewBinder()
{
    @Override
    public boolean setViewValue (View view, Cursor cursor, int columnIndex)
    {
        boolean bound = false;
        int id = view.getId()
        if(id == R.id.my_image_view)
        {
            // bind data to view here
            bound = true;
        }
        return bound;
    }
});
```
    
To set/swap cursor in your LoaderCallbacks:

```java
@Override
public void onLoadFinished (Loader<Cursor> cursorLoader, Cursor cursor)
{
    ((AdvancedCursorAdapter) getListAdapter()).swapCursor(cursor);
}
```
    
Please see the sample project for a complete working example


Install
----------
Download the [latest jar](https://github.com/MediaFriendsInc2/advanced-cursor-adapter/releases/download/v1.0.0/advanced-cursor-adapter-v1.0.0.jar) and add to your project

License
----------
    Copyright 2013 MediaFriends, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
