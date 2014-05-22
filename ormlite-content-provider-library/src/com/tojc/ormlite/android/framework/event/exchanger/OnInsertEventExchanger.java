package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.event.listener.OnInsertListener;
import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnInsertMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnInsertMultiEventObject;

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
