package com.tojc.ormlite.android.framework.event.multievent.listener;

import com.tojc.ormlite.android.framework.event.multievent.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.object.OnBeforeBulkInsertMultiEventObject;

/**
 * Created by Jaken on 2014/05/13.
 */
public interface OnBeforeBulkInsertMultiEventListener extends MultiEventListenerInterfaceBase {
    void onBeforeBulkInsert(OnBeforeBulkInsertMultiEventObject e);
}
