package com.tojc.ormlite.android.framework.event.dispatcher;

import com.tojc.ormlite.android.framework.event.EventDispatcherBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnAfterApplyBatchMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnAfterApplyBatchMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public final class OnAfterApplyBatchEventDispatcher
        extends EventDispatcherBase<OnAfterApplyBatchMultiEventListener, OnAfterApplyBatchMultiEventObject> {
    public OnAfterApplyBatchEventDispatcher() {
        super();
    }

    @Override
    public void dispatch(OnAfterApplyBatchMultiEventListener listener, OnAfterApplyBatchMultiEventObject param) {
        listener.onAfterApplyBatch(param);
    }
}
