package com.tojc.ormlite.android.framework.event;

import com.tojc.ormlite.android.framework.event.listenerbase.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventListenerInterfaceBase;

/**
 * Created by Jaken on 2014/05/07.
 */
public class EventExchangerBase implements MultiEventListenerInterfaceBase {
    private ContentProviderEventListenerInterfaceBase forwarding;

    public EventExchangerBase(ContentProviderEventListenerInterfaceBase forwarding) {
        this.forwarding = forwarding;
    }

    public ContentProviderEventListenerInterfaceBase getForwarding() {
        return forwarding;
    }
}
