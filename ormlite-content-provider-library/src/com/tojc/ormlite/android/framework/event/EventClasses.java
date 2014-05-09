package com.tojc.ormlite.android.framework.event;

import com.tojc.ormlite.android.framework.event.dispatcher.OnAfterApplyBatchEventDispatcher;
import com.tojc.ormlite.android.framework.event.dispatcher.OnBeforeApplyBatchEventDispatcher;
import com.tojc.ormlite.android.framework.event.dispatcher.OnBulkInsertCompletedEventDispatcher;
import com.tojc.ormlite.android.framework.event.dispatcher.OnBulkInsertEventDispatcher;
import com.tojc.ormlite.android.framework.event.dispatcher.OnDeleteCompletedEventDispatcher;
import com.tojc.ormlite.android.framework.event.dispatcher.OnDeleteEventDispatcher;
import com.tojc.ormlite.android.framework.event.dispatcher.OnInsertCompletedEventDispatcher;
import com.tojc.ormlite.android.framework.event.dispatcher.OnInsertEventDispatcher;
import com.tojc.ormlite.android.framework.event.dispatcher.OnQueryCompletedEventDispatcher;
import com.tojc.ormlite.android.framework.event.dispatcher.OnQueryEventDispatcher;
import com.tojc.ormlite.android.framework.event.dispatcher.OnUpdateCompletedEventDispatcher;
import com.tojc.ormlite.android.framework.event.dispatcher.OnUpdateEventDispatcher;
import com.tojc.ormlite.android.framework.event.exchanger.OnAfterApplyBatchEventExchanger;
import com.tojc.ormlite.android.framework.event.exchanger.OnBeforeApplyBatchEventExchanger;
import com.tojc.ormlite.android.framework.event.exchanger.OnBulkInsertCompletedEventExchanger;
import com.tojc.ormlite.android.framework.event.exchanger.OnBulkInsertEventExchanger;
import com.tojc.ormlite.android.framework.event.exchanger.OnDeleteCompletedEventExchanger;
import com.tojc.ormlite.android.framework.event.exchanger.OnDeleteEventExchanger;
import com.tojc.ormlite.android.framework.event.exchanger.OnInsertCompletedEventExchanger;
import com.tojc.ormlite.android.framework.event.exchanger.OnInsertEventExchanger;
import com.tojc.ormlite.android.framework.event.exchanger.OnQueryCompletedEventExchanger;
import com.tojc.ormlite.android.framework.event.exchanger.OnQueryEventExchanger;
import com.tojc.ormlite.android.framework.event.exchanger.OnUpdateCompletedEventExchanger;
import com.tojc.ormlite.android.framework.event.exchanger.OnUpdateEventExchanger;
import com.tojc.ormlite.android.framework.event.listener.OnAfterApplyBatchListener;
import com.tojc.ormlite.android.framework.event.listener.OnBeforeApplyBatchListener;
import com.tojc.ormlite.android.framework.event.listener.OnBulkInsertCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnBulkInsertListener;
import com.tojc.ormlite.android.framework.event.listener.OnDeleteCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnDeleteListener;
import com.tojc.ormlite.android.framework.event.listener.OnInsertCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnInsertListener;
import com.tojc.ormlite.android.framework.event.listener.OnQueryCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnQueryListener;
import com.tojc.ormlite.android.framework.event.listener.OnUpdateCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnUpdateListener;
import com.tojc.ormlite.android.framework.event.listenerbase.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventObjectBase;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnAfterApplyBatchMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnBeforeApplyBatchMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnBulkInsertCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnBulkInsertMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnDeleteCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnDeleteMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnInsertCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnInsertMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnQueryCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnQueryMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnUpdateCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnUpdateMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventobject.OnAfterApplyBatchMultiEventObject;
import com.tojc.ormlite.android.framework.event.multieventobject.OnBeforeApplyBatchMultiEventObject;
import com.tojc.ormlite.android.framework.event.multieventobject.OnBulkInsertCompletedMultiEventObject;
import com.tojc.ormlite.android.framework.event.multieventobject.OnBulkInsertMultiEventObject;
import com.tojc.ormlite.android.framework.event.multieventobject.OnDeleteCompletedMultiEventObject;
import com.tojc.ormlite.android.framework.event.multieventobject.OnDeleteMultiEventObject;
import com.tojc.ormlite.android.framework.event.multieventobject.OnInsertCompletedMultiEventObject;
import com.tojc.ormlite.android.framework.event.multieventobject.OnInsertMultiEventObject;
import com.tojc.ormlite.android.framework.event.multieventobject.OnQueryCompletedMultiEventObject;
import com.tojc.ormlite.android.framework.event.multieventobject.OnQueryMultiEventObject;
import com.tojc.ormlite.android.framework.event.multieventobject.OnUpdateCompletedMultiEventObject;
import com.tojc.ormlite.android.framework.event.multieventobject.OnUpdateMultiEventObject;

import java.lang.reflect.Modifier;

/**
 * Created by Jaken on 2014/05/08.
 */
public enum EventClasses {
    OnQuery(OnQueryEventDispatcher.class, OnQueryMultiEventListener.class, OnQueryMultiEventObject.class, OnQueryListener.class, OnQueryEventExchanger.class),
    OnQueryCompleted(OnQueryCompletedEventDispatcher.class, OnQueryCompletedMultiEventListener.class, OnQueryCompletedMultiEventObject.class, OnQueryCompletedListener.class, OnQueryCompletedEventExchanger.class),
    OnInsert(OnInsertEventDispatcher.class, OnInsertMultiEventListener.class, OnInsertMultiEventObject.class, OnInsertListener.class, OnInsertEventExchanger.class),
    OnInsertCompleted(OnInsertCompletedEventDispatcher.class, OnInsertCompletedMultiEventListener.class, OnInsertCompletedMultiEventObject.class, OnInsertCompletedListener.class, OnInsertCompletedEventExchanger.class),
    OnDelete(OnDeleteEventDispatcher.class, OnDeleteMultiEventListener.class, OnDeleteMultiEventObject.class, OnDeleteListener.class, OnDeleteEventExchanger.class),
    OnDeleteCompleted(OnDeleteCompletedEventDispatcher.class, OnDeleteCompletedMultiEventListener.class, OnDeleteCompletedMultiEventObject.class, OnDeleteCompletedListener.class, OnDeleteCompletedEventExchanger.class),
    OnUpdate(OnUpdateEventDispatcher.class, OnUpdateMultiEventListener.class, OnUpdateMultiEventObject.class, OnUpdateListener.class, OnUpdateEventExchanger.class),
    OnUpdateCompleted(OnUpdateCompletedEventDispatcher.class, OnUpdateCompletedMultiEventListener.class, OnUpdateCompletedMultiEventObject.class, OnUpdateCompletedListener.class, OnUpdateCompletedEventExchanger.class),
    OnBulkInsert(OnBulkInsertEventDispatcher.class, OnBulkInsertMultiEventListener.class, OnBulkInsertMultiEventObject.class, OnBulkInsertListener.class, OnBulkInsertEventExchanger.class),
    OnBulkInsertCompleted(OnBulkInsertCompletedEventDispatcher.class, OnBulkInsertCompletedMultiEventListener.class, OnBulkInsertCompletedMultiEventObject.class, OnBulkInsertCompletedListener.class, OnBulkInsertCompletedEventExchanger.class),
    OnBeforeApplyBatch(OnBeforeApplyBatchEventDispatcher.class, OnBeforeApplyBatchMultiEventListener.class, OnBeforeApplyBatchMultiEventObject.class, OnBeforeApplyBatchListener.class, OnBeforeApplyBatchEventExchanger.class),
    OnAfterApplyBatch(OnAfterApplyBatchEventDispatcher.class, OnAfterApplyBatchMultiEventListener.class, OnAfterApplyBatchMultiEventObject.class, OnAfterApplyBatchListener.class, OnAfterApplyBatchEventExchanger.class);

    private final Class<?> clazzEventDispatcher;

    private final Class<?> clazzMultiEventListener;
    private final Class<?> clazzMultiEventObject;

    private final Class<?> clazzContentProviderListener;
    private final Class<?> clazzEventExchanger;

    private EventClasses(Class<?> clazzEventDispatcher, Class<?> clazzMultiEventListener, Class<?> clazzMultiEventObject, Class<?> clazzContentProviderListener, Class<?> clazzEventExchanger) {
        this.clazzEventDispatcher = clazzEventDispatcher;
        this.clazzMultiEventListener = clazzMultiEventListener;
        this.clazzMultiEventObject = clazzMultiEventObject;
        this.clazzContentProviderListener = clazzContentProviderListener;
        this.clazzEventExchanger = clazzEventExchanger;
    }

    public static EventClasses toEventClasses(String name) {
        EventClasses result = null;
        for (EventClasses value : values()) {
            if (value.name().equals(name)) {
                result = value;
                break;
            }
        }
        return result;
    }

    public static EventClasses toEventClasses(Class<?> clazz) {
        EventClasses result = null;
        for (EventClasses value : values()) {
            if (value.contains(clazz)) {
                result = value;
                break;
            }
        }
        return result;
    }

    public boolean contains(Class<?> clazz) {
        boolean result = false;
        if ((this.clazzEventDispatcher == clazz)
                || (this.clazzMultiEventListener == clazz)
                || (this.clazzMultiEventObject == clazz)
                || (this.clazzContentProviderListener == clazz)
                || (this.clazzEventExchanger == clazz)) {
            result = true;
        }
        return result;
    }

    public boolean isImplementedMultiEventListener(Object object) {
        boolean result = false;
        if (object != null) {
            if (!object.getClass().isInterface()
                    && this.getMultiEventListenerClass().isAssignableFrom(object.getClass())
                    && !Modifier.isAbstract(object.getClass().getModifiers())) {
                result = true;
            }
        }
        return result;
    }

    public boolean isImplementedContentProviderListener(Object object) {
        boolean result = false;
        if (object != null) {
            if (!object.getClass().isInterface()
                    && this.getContentProviderListenerClass().isAssignableFrom(object.getClass())
                    && !Modifier.isAbstract(object.getClass().getModifiers())) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return this.name();
    }

    @SuppressWarnings("unchecked")
    public Class<EventDispatcherBase<?, ?>> getEventDispatcherClass() {
        return (Class<EventDispatcherBase<?, ?>>) this.clazzEventDispatcher;
    }

    @SuppressWarnings("unchecked")
    public Class<MultiEventListenerInterfaceBase> getMultiEventListenerClass() {
        return (Class<MultiEventListenerInterfaceBase>) this.clazzMultiEventListener;
    }

    @SuppressWarnings("unchecked")
    public Class<MultiEventObjectBase> getMultiEventObjectClass() {
        return (Class<MultiEventObjectBase>) this.clazzMultiEventObject;
    }

    @SuppressWarnings("unchecked")
    public Class<ContentProviderEventListenerInterfaceBase> getContentProviderListenerClass() {
        return (Class<ContentProviderEventListenerInterfaceBase>) this.clazzContentProviderListener;
    }

    @SuppressWarnings("unchecked")
    public Class<EventExchangerBase> getEventExchangerClass() {
        return (Class<EventExchangerBase>) this.clazzEventExchanger;
    }
}
