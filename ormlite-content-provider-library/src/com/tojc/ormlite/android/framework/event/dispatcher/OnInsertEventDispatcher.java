package com.tojc.ormlite.android.framework.event.dispatcher;

import com.tojc.ormlite.android.framework.event.EventDispatcherBase;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnInsertMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventobject.OnInsertMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public final class OnInsertEventDispatcher
        extends EventDispatcherBase<OnInsertMultiEventListener, OnInsertMultiEventObject> {

    public OnInsertEventDispatcher() {
        super();
    }

    @Override
    public void dispatch(OnInsertMultiEventListener listener, OnInsertMultiEventObject param) {
        listener.onInsert(param);
    }
}
