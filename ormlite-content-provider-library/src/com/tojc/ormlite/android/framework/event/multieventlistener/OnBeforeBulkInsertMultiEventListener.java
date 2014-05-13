package com.tojc.ormlite.android.framework.event.multieventlistener;

import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventobject.OnBeforeBulkInsertMultiEventObject;

/**
 * Created by Jaken on 2014/05/13.
 */
public interface OnBeforeBulkInsertMultiEventListener extends MultiEventListenerInterfaceBase {
    void onBeforeBulkInsert(OnBeforeBulkInsertMultiEventObject e);
}
