package com.tojc.ormlite.android.framework.event.exchanger;

import com.tojc.ormlite.android.framework.event.EventExchangerBase;
import com.tojc.ormlite.android.event.listener.OnInsertCompletedListener;
import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnInsertCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnInsertCompletedMultiEventObject;

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
