package com.tojc.ormlite.android.framework.event.multievent.listener;

import com.tojc.ormlite.android.framework.event.multievent.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.object.OnInsertCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnInsertCompletedMultiEventListener extends MultiEventListenerInterfaceBase {
    void onInsertCompleted(OnInsertCompletedMultiEventObject e);
}
