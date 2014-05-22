package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.event.listener.OnDeleteCompletedListener;
import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnDeleteCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnDeleteCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/07.
 */
public class OnDeleteCompletedEventExchanger extends EventExchangerBase implements OnDeleteCompletedMultiEventListener {
    public OnDeleteCompletedEventExchanger(ContentProviderEventListenerInterfaceBase forwarding) {
        super(forwarding);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDeleteCompleted(OnDeleteCompletedMultiEventObject e) {
        OnDeleteCompletedListener listener = (OnDeleteCompletedListener) this.getForwarding();
        listener.onDeleteCompleted(e.getResult(), e.getUri(), e.getMatcherPattern(), e.getParameter());
    }
}
