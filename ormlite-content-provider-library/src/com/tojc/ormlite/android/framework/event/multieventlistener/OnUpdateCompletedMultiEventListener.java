package com.tojc.ormlite.android.framework.event.multieventlistener;

import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventobject.OnUpdateCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnUpdateCompletedMultiEventListener extends MultiEventListenerInterfaceBase {
    void onUpdateCompleted(OnUpdateCompletedMultiEventObject e);
}
