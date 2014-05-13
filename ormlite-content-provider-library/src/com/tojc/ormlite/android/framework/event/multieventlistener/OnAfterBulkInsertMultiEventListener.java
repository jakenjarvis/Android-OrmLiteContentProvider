package com.tojc.ormlite.android.framework.event.multieventlistener;

import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventobject.OnAfterBulkInsertMultiEventObject;

/**
 * Created by Jaken on 2014/05/13.
 */
public interface OnAfterBulkInsertMultiEventListener extends MultiEventListenerInterfaceBase {
    void onAfterBulkInsert(OnAfterBulkInsertMultiEventObject e);
}
