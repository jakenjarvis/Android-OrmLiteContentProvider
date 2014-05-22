package com.tojc.ormlite.android.framework.event.dispatcher;

import com.tojc.ormlite.android.framework.event.EventDispatcherBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnQueryMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnQueryMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public final class OnQueryEventDispatcher
        extends EventDispatcherBase<OnQueryMultiEventListener, OnQueryMultiEventObject> {

    public OnQueryEventDispatcher() {
        super();
    }

    @Override
    public void dispatch(OnQueryMultiEventListener listener, OnQueryMultiEventObject param) {
        listener.onQuery(param);
    }
}
