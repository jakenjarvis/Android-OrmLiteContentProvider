package com.tojc.ormlite.android.framework.event.multievent.listener;

import com.tojc.ormlite.android.framework.event.multievent.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.object.OnUpdateCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnUpdateCompletedMultiEventListener extends MultiEventListenerInterfaceBase {
    void onUpdateCompleted(OnUpdateCompletedMultiEventObject e);
}
