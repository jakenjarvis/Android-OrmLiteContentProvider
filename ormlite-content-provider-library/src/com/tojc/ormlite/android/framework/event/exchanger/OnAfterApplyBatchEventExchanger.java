package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.framework.event.listener.OnAfterApplyBatchListener;
import com.tojc.ormlite.android.framework.event.listenerbase.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnAfterApplyBatchMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventobject.OnAfterApplyBatchMultiEventObject;

/**
 * Created by Jaken on 2014/05/07.
 */
public class OnAfterApplyBatchEventExchanger extends EventExchangerBase implements OnAfterApplyBatchMultiEventListener {
    public OnAfterApplyBatchEventExchanger(ContentProviderEventListenerInterfaceBase forwarding) {
        super(forwarding);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onAfterApplyBatch(OnAfterApplyBatchMultiEventObject e) {
        OnAfterApplyBatchListener listener = (OnAfterApplyBatchListener) this.getForwarding();
        listener.onAfterApplyBatch(e.getHelper(), e.getSQLiteDatabase(), e.getOperations(), e.getResult());
    }
}
