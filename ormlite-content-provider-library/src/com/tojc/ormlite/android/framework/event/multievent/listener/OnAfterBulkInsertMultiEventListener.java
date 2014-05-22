package com.tojc.ormlite.android.framework.event.multievent.listener;

import com.tojc.ormlite.android.framework.event.multievent.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.object.OnAfterBulkInsertMultiEventObject;

/**
 * Created by Jaken on 2014/05/13.
 */
public interface OnAfterBulkInsertMultiEventListener extends MultiEventListenerInterfaceBase {
    void onAfterBulkInsert(OnAfterBulkInsertMultiEventObject e);
}
