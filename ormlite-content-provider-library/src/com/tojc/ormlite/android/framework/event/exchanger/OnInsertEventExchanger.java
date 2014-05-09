package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.framework.event.listener.OnInsertListener;
import com.tojc.ormlite.android.framework.event.listenerbase.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnInsertMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventobject.OnInsertMultiEventObject;

/**
 * Created by Jaken on 2014/05/07.
 */
public class OnInsertEventExchanger extends EventExchangerBase implements OnInsertMultiEventListener {
    public OnInsertEventExchanger(ContentProviderEventListenerInterfaceBase forwarding) {
        super(forwarding);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onInsert(OnInsertMultiEventObject e) {
        OnInsertListener listener = (OnInsertListener) this.getForwarding();
        e.setReturnValue(listener.onInsert(e.getHelper(), e.getSQLiteDatabase(), e.getMatcherPattern(), e.getParameter()));
    }
}
