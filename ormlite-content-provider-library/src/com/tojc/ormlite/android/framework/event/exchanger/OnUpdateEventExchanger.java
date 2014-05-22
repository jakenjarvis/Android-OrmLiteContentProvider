package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.event.listener.OnUpdateListener;
import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnUpdateMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnUpdateMultiEventObject;

/**
 * Created by Jaken on 2014/05/07.
 */
public class OnUpdateEventExchanger extends EventExchangerBase implements OnUpdateMultiEventListener {
    public OnUpdateEventExchanger(ContentProviderEventListenerInterfaceBase forwarding) {
        super(forwarding);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onUpdate(OnUpdateMultiEventObject e) {
        OnUpdateListener listener = (OnUpdateListener) this.getForwarding();
        e.setReturnValue(listener.onUpdate(e.getHelper(), e.getSQLiteDatabase(), e.getMatcherPattern(), e.getParameter()));
    }
}
