package com.tojc.ormlite.android.framework.event.multievent.listener;

import com.tojc.ormlite.android.framework.event.multievent.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.object.OnInsertMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnInsertMultiEventListener extends MultiEventListenerInterfaceBase {
    void onInsert(OnInsertMultiEventObject e);
}
