package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.event.listener.OnBeforeBulkInsertListener;
import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnBeforeBulkInsertMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnBeforeBulkInsertMultiEventObject;

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
