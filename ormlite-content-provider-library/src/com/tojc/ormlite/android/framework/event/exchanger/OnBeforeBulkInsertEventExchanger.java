package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.framework.event.listener.OnBeforeBulkInsertListener;
import com.tojc.ormlite.android.framework.event.listenerbase.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnBeforeBulkInsertMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventobject.OnBeforeBulkInsertMultiEventObject;

/**
 * Created by Jaken on 2014/05/13.
 */
public class OnBeforeBulkInsertEventExchanger extends EventExchangerBase implements OnBeforeBulkInsertMultiEventListener {
    public OnBeforeBulkInsertEventExchanger(ContentProviderEventListenerInterfaceBase forwarding) {
        super(forwarding);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBeforeBulkInsert(OnBeforeBulkInsertMultiEventObject e) {
        OnBeforeBulkInsertListener listener = (OnBeforeBulkInsertListener) this.getForwarding();
        listener.onBeforeBulkInsert(e.getHelper(), e.getSQLiteDatabase(), e.getMatcherPattern(), e.getUri(), e.getValues());
    }
}
