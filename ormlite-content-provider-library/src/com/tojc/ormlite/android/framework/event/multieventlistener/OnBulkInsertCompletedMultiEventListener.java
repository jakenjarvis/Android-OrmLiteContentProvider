package com.tojc.ormlite.android.framework.event.multieventlistener;

import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventobject.OnBulkInsertCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnBulkInsertCompletedMultiEventListener extends MultiEventListenerInterfaceBase {
    void onBulkInsertCompleted(OnBulkInsertCompletedMultiEventObject e);
}
