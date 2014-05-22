package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.event.listener.OnDeleteListener;
import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnDeleteMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnDeleteMultiEventObject;

/**
 * Created by Jaken on 2014/05/07.
 */
public class OnDeleteEventExchanger extends EventExchangerBase implements OnDeleteMultiEventListener {
    public OnDeleteEventExchanger(ContentProviderEventListenerInterfaceBase forwarding) {
        super(forwarding);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDelete(OnDeleteMultiEventObject e) {
        OnDeleteListener listener = (OnDeleteListener) this.getForwarding();
        e.setReturnValue(listener.onDelete(e.getHelper(), e.getSQLiteDatabase(), e.getMatcherPattern(), e.getParameter()));
    }
}
