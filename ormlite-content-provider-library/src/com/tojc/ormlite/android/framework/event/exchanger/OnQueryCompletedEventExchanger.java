package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.framework.event.listener.OnQueryCompletedListener;
import com.tojc.ormlite.android.framework.event.listenerbase.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnQueryCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventobject.OnQueryCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/07.
 */
public class OnQueryCompletedEventExchanger extends EventExchangerBase implements OnQueryCompletedMultiEventListener {
    public OnQueryCompletedEventExchanger(ContentProviderEventListenerInterfaceBase forwarding) {
        super(forwarding);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onQueryCompleted(OnQueryCompletedMultiEventObject e) {
        OnQueryCompletedListener listener = (OnQueryCompletedListener) this.getForwarding();
        listener.onQueryCompleted(e.getResult(), e.getUri(), e.getMatcherPattern(), e.getParameter());
    }
}
