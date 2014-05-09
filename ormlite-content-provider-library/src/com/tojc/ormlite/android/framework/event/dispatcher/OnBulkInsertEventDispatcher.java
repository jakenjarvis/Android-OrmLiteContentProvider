package com.tojc.ormlite.android.framework.event.dispatcher;

import com.tojc.ormlite.android.framework.event.EventDispatcherBase;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnBulkInsertMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventobject.OnBulkInsertMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public final class OnBulkInsertEventDispatcher
        extends EventDispatcherBase<OnBulkInsertMultiEventListener, OnBulkInsertMultiEventObject> {

    public OnBulkInsertEventDispatcher() {
        super();
    }

    @Override
    public void dispatch(OnBulkInsertMultiEventListener listener, OnBulkInsertMultiEventObject param) {
        listener.onBulkInsert(param);
    }
}
