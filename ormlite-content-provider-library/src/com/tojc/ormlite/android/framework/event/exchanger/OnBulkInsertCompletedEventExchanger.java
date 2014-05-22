package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.event.listener.OnBulkInsertCompletedListener;
import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnBulkInsertCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnBulkInsertCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/07.
 */
public class OnBulkInsertCompletedEventExchanger extends EventExchangerBase implements OnBulkInsertCompletedMultiEventListener {
    public OnBulkInsertCompletedEventExchanger(ContentProviderEventListenerInterfaceBase forwarding) {
        super(forwarding);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBulkInsertCompleted(OnBulkInsertCompletedMultiEventObject e) {
        OnBulkInsertCompletedListener listener = (OnBulkInsertCompletedListener) this.getForwarding();
        listener.onBulkInsertCompleted(e.getResult(), e.getUri());
    }
}
