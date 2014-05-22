package com.tojc.ormlite.android.framework.event;

import com.tojc.ormlite.android.framework.event.multievent.MultiEventListenerInterfaceBase;

import java.util.EventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public abstract class EventDispatcherBase<U extends MultiEventListenerInterfaceBase, V extends EventObject>
        implements EventMulticaster.MultiEventDispatcher<U, V> {

    public EventDispatcherBase() {
    }

    @SuppressWarnings("unchecked")
    public EventDispatcherBase<MultiEventListenerInterfaceBase, EventObject> downcast() {
        return (EventDispatcherBase<MultiEventListenerInterfaceBase, EventObject>) this;
    }
}
