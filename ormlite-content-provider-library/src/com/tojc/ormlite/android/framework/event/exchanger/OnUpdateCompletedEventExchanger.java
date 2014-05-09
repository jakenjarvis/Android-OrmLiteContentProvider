package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.framework.event.listener.OnUpdateCompletedListener;
import com.tojc.ormlite.android.framework.event.listenerbase.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnUpdateCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventobject.OnUpdateCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/07.
 */
public class OnUpdateCompletedEventExchanger extends EventExchangerBase implements OnUpdateCompletedMultiEventListener {
    public OnUpdateCompletedEventExchanger(ContentProviderEventListenerInterfaceBase forwarding) {
        super(forwarding);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onUpdateCompleted(OnUpdateCompletedMultiEventObject e) {
        OnUpdateCompletedListener listener = (OnUpdateCompletedListener) this.getForwarding();
        listener.onUpdateCompleted(e.getResult(), e.getUri(), e.getMatcherPattern(), e.getParameter());
    }
}
