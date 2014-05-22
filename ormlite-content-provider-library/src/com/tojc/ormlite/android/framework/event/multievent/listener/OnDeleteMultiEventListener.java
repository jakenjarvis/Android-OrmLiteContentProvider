package com.tojc.ormlite.android.framework.event.multievent.listener;

import com.tojc.ormlite.android.framework.event.multievent.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.object.OnDeleteMultiEventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnDeleteMultiEventListener extends MultiEventListenerInterfaceBase {
    void onDelete(OnDeleteMultiEventObject e);
}
