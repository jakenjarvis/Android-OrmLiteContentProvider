package com.tojc.ormlite.android.framework.event.dispatcher;

import com.tojc.ormlite.android.framework.event.EventDispatcherBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnUpdateCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnUpdateCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public final class OnUpdateCompletedEventDispatcher
        extends EventDispatcherBase<OnUpdateCompletedMultiEventListener, OnUpdateCompletedMultiEventObject> {

    public OnUpdateCompletedEventDispatcher() {
        super();
    }

    @Override
    public void dispatch(OnUpdateCompletedMultiEventListener listener, OnUpdateCompletedMultiEventObject param) {
        listener.onUpdateCompleted(param);
    }
}
