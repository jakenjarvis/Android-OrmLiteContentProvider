package com.tojc.ormlite.android.framework.event.dispatcher;

import com.tojc.ormlite.android.framework.event.EventDispatcherBase;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnQueryCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventobject.OnQueryCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public final class OnQueryCompletedEventDispatcher
        extends EventDispatcherBase<OnQueryCompletedMultiEventListener, OnQueryCompletedMultiEventObject> {

    public OnQueryCompletedEventDispatcher() {
        super();
    }

    @Override
    public void dispatch(OnQueryCompletedMultiEventListener listener, OnQueryCompletedMultiEventObject param) {
        listener.onQueryCompleted(param);
    }
}
