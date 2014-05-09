package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.framework.event.listener.OnQueryListener;
import com.tojc.ormlite.android.framework.event.listenerbase.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnQueryMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventobject.OnQueryMultiEventObject;

/**
 * Created by Jaken on 2014/05/07.
 */
public class OnQueryEventExchanger extends EventExchangerBase implements OnQueryMultiEventListener {
    public OnQueryEventExchanger(ContentProviderEventListenerInterfaceBase forwarding) {
        super(forwarding);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onQuery(OnQueryMultiEventObject e) {
        OnQueryListener listener = (OnQueryListener) this.getForwarding();
        e.setReturnValue(listener.onQuery(e.getHelper(), e.getSQLiteDatabase(), e.getMatcherPattern(), e.getParameter()));
    }
}
