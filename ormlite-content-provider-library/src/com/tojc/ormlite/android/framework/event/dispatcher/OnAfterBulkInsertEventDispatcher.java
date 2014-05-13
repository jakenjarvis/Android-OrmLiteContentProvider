package com.tojc.ormlite.android.framework.event.dispatcher;

import com.tojc.ormlite.android.framework.event.EventDispatcherBase;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnAfterBulkInsertMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventobject.OnAfterBulkInsertMultiEventObject;

/**
 * Created by Jaken on 2014/05/13.
 */
public final class OnAfterBulkInsertEventDispatcher
        extends EventDispatcherBase<OnAfterBulkInsertMultiEventListener, OnAfterBulkInsertMultiEventObject> {

    public OnAfterBulkInsertEventDispatcher() {
        super();
    }

    @Override
    public void dispatch(OnAfterBulkInsertMultiEventListener listener, OnAfterBulkInsertMultiEventObject param) {
        listener.onAfterBulkInsert(param);
    }
}
