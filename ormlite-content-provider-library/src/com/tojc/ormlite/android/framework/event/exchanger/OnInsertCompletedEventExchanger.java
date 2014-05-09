package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.framework.event.listener.OnInsertCompletedListener;
import com.tojc.ormlite.android.framework.event.listenerbase.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnInsertCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventobject.OnInsertCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/07.
 */
public class OnInsertCompletedEventExchanger extends EventExchangerBase implements OnInsertCompletedMultiEventListener {
    public OnInsertCompletedEventExchanger(ContentProviderEventListenerInterfaceBase forwarding) {
        super(forwarding);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onInsertCompleted(OnInsertCompletedMultiEventObject e) {
        OnInsertCompletedListener listener = (OnInsertCompletedListener) this.getForwarding();
        listener.onInsertCompleted(e.getResult(), e.getUri(), e.getMatcherPattern(), e.getParameter());
    }
}
