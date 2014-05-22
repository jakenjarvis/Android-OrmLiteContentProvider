package com.tojc.ormlite.android.framework.event.dispatcher;

import com.tojc.ormlite.android.framework.event.EventDispatcherBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnUpdateMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnUpdateMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public final class OnUpdateEventDispatcher
        extends EventDispatcherBase<OnUpdateMultiEventListener, OnUpdateMultiEventObject> {

    public OnUpdateEventDispatcher() {
        super();
    }

    @Override
    public void dispatch(OnUpdateMultiEventListener listener, OnUpdateMultiEventObject param) {
        listener.onUpdate(param);
    }
}
