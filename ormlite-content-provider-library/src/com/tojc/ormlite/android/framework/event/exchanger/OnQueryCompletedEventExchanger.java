package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.event.listener.OnQueryCompletedListener;
import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnQueryCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnQueryCompletedMultiEventObject;

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
