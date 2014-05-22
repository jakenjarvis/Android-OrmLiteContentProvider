package com.tojc.ormlite.android.framework.event.dispatcher;

import com.tojc.ormlite.android.framework.event.EventDispatcherBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnDeleteCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnDeleteCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public final class OnDeleteCompletedEventDispatcher
        extends EventDispatcherBase<OnDeleteCompletedMultiEventListener, OnDeleteCompletedMultiEventObject> {

    public OnDeleteCompletedEventDispatcher() {
        super();
    }

    @Override
    public void dispatch(OnDeleteCompletedMultiEventListener listener, OnDeleteCompletedMultiEventObject param) {
        listener.onDeleteCompleted(param);
    }
}
