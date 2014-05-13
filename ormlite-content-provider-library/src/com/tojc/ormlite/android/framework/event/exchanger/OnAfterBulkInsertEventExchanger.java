package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.framework.event.listener.OnAfterBulkInsertListener;
import com.tojc.ormlite.android.framework.event.listenerbase.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnAfterBulkInsertMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventobject.OnAfterBulkInsertMultiEventObject;

/**
 * Created by Jaken on 2014/05/13.
 */
public class OnAfterBulkInsertEventExchanger extends EventExchangerBase implements OnAfterBulkInsertMultiEventListener {
    public OnAfterBulkInsertEventExchanger(ContentProviderEventListenerInterfaceBase forwarding) {
        super(forwarding);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onAfterBulkInsert(OnAfterBulkInsertMultiEventObject e) {
        OnAfterBulkInsertListener listener = (OnAfterBulkInsertListener) this.getForwarding();
        listener.onAfterBulkInsert(e.getHelper(), e.getSQLiteDatabase(), e.getMatcherPattern(), e.getUri(), e.getValues());
    }
}
