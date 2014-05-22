package com.tojc.ormlite.android.framework.event.multievent.listener;

import com.tojc.ormlite.android.framework.event.multievent.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.object.OnAfterApplyBatchMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnAfterApplyBatchMultiEventListener extends MultiEventListenerInterfaceBase {
    void onAfterApplyBatch(OnAfterApplyBatchMultiEventObject e);
}
