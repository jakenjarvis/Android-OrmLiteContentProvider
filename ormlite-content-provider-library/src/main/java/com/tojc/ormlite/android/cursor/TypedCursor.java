package com.tojc.ormlite.android.cursor;


import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Cursor wrapper with type.
 * Created by hidaka on 2014/12/02.
 */
public class TypedCursor<T> extends CursorWrapper {
    private final Class<T> mType;
    private final Cursor mWrappedCursor;

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public TypedCursor(Cursor cursor, Class<T> type) {
        super(cursor);
        mWrappedCursor = cursor;
        mType = type;
    }

    T getEntity() {
        return EntityUtils.loadFromCursor(mWrappedCursor, mType);
    }
}
