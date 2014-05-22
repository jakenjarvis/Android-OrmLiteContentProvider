package com.tojc.ormlite.android.framework.event.multievent.listener;

import com.tojc.ormlite.android.framework.event.multievent.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.object.OnUpdateMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnUpdateMultiEventListener extends MultiEventListenerInterfaceBase {
    void onUpdate(OnUpdateMultiEventObject e);
}
