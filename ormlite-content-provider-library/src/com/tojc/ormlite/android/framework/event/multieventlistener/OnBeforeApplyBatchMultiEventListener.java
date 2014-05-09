package com.tojc.ormlite.android.framework.event.multieventlistener;

import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventobject.OnBeforeApplyBatchMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnBeforeApplyBatchMultiEventListener extends MultiEventListenerInterfaceBase {
    void onBeforeApplyBatch(OnBeforeApplyBatchMultiEventObject e);
}
