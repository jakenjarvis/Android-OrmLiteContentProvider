package com.tojc.ormlite.android.framework.event.multievent.listener;

import com.tojc.ormlite.android.framework.event.multievent.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.object.OnDeleteCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnDeleteCompletedMultiEventListener extends MultiEventListenerInterfaceBase {
    void onDeleteCompleted(OnDeleteCompletedMultiEventObject e);
}
