package com.tojc.ormlite.android.framework.event.multieventlistener;

import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventobject.OnInsertCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnInsertCompletedMultiEventListener extends MultiEventListenerInterfaceBase {
    void onInsertCompleted(OnInsertCompletedMultiEventObject e);
}
