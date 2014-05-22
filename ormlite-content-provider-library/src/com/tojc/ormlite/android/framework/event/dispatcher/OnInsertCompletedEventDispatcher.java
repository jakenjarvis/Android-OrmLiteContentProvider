package com.tojc.ormlite.android.framework.event.dispatcher;

import com.tojc.ormlite.android.framework.event.EventDispatcherBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnInsertCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnInsertCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public final class OnInsertCompletedEventDispatcher
        extends EventDispatcherBase<OnInsertCompletedMultiEventListener, OnInsertCompletedMultiEventObject> {

    public OnInsertCompletedEventDispatcher() {
        super();
    }

    @Override
    public void dispatch(OnInsertCompletedMultiEventListener listener, OnInsertCompletedMultiEventObject param) {
        listener.onInsertCompleted(param);
    }
}
