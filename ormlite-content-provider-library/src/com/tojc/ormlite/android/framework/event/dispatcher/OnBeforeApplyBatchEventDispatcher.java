package com.tojc.ormlite.android.framework.event.dispatcher;

import com.tojc.ormlite.android.framework.event.EventDispatcherBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnBeforeApplyBatchMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnBeforeApplyBatchMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public final class OnBeforeApplyBatchEventDispatcher
        extends EventDispatcherBase<OnBeforeApplyBatchMultiEventListener, OnBeforeApplyBatchMultiEventObject> {
    public OnBeforeApplyBatchEventDispatcher() {
        super();
    }

    @Override
    public void dispatch(OnBeforeApplyBatchMultiEventListener listener, OnBeforeApplyBatchMultiEventObject param) {
        listener.onBeforeApplyBatch(param);
    }
}
