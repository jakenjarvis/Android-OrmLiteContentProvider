package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.event.listener.OnBulkInsertListener;
import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnBulkInsertMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnBulkInsertMultiEventObject;

/**
 * Created by Jaken on 2014/05/07.
 */
public class OnBulkInsertEventExchanger extends EventExchangerBase implements OnBulkInsertMultiEventListener {
    public OnBulkInsertEventExchanger(ContentProviderEventListenerInterfaceBase forwarding) {
        super(forwarding);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBulkInsert(OnBulkInsertMultiEventObject e) {
        OnBulkInsertListener listener = (OnBulkInsertListener) this.getForwarding();
        e.setReturnValue(listener.onBulkInsert(e.getHelper(), e.getSQLiteDatabase(), e.getMatcherPattern(), e.getParameter()));
    }
}
