/*
 * This file is part of the Android-OrmLiteContentProvider package.
 *
 * Copyright (c) 2012, Android-OrmLiteContentProvider Team.
 *                     Jaken Jarvis (jaken.jarvis@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * The author may be contacted via
 * https://github.com/jakenjarvis/Android-OrmLiteContentProvider
 */
package com.tojc.ormlite.android.framework.event;

import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;
import com.tojc.ormlite.android.framework.event.multievent.MultiEventListenerInterfaceBase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;

/**
 * Created by Jaken on 2014/05/05.
 */
public class EventController {
    private EventMulticaster eventMulticaster = new EventMulticaster();

    public EventController() {
        this.registerEventDispatcher();
    }

    private void registerEventDispatcher() {
        for (EventClasses value : EventClasses.values()) {
            EventDispatcherBase<MultiEventListenerInterfaceBase, EventObject> dispatcher = this.createEventDispatcher(value);
            this.eventMulticaster.registerMultiEventDispatcher(value.getMultiEventListenerClass(), dispatcher);
        }
    }

    @SuppressWarnings("unchecked")
    private EventDispatcherBase<MultiEventListenerInterfaceBase, EventObject> createEventDispatcher(EventClasses eventClasses) {
        EventDispatcherBase<MultiEventListenerInterfaceBase, EventObject> instance;
        Exception innerException = null;
        try {
            instance = (EventDispatcherBase<MultiEventListenerInterfaceBase, EventObject>) eventClasses.getEventDispatcherClass().newInstance();
        } catch (InstantiationException e) {
            instance = null;
            innerException = e;
        } catch (IllegalAccessException e) {
            instance = null;
            innerException = e;
        }
        if (innerException != null) {
            throw new IllegalStateException("Failed to create an instance of EventDispatcher: " + eventClasses.toString(), innerException);
        }
        return instance;
    }

    private EventExchangerBase createEventExchanger(EventClasses eventClasses, ContentProviderEventListenerInterfaceBase forwarding) {
        EventExchangerBase instance;
        Exception innerException = null;
        try {
            Constructor constructor = eventClasses.getEventExchangerClass().getConstructor(ContentProviderEventListenerInterfaceBase.class);
            instance = (EventExchangerBase) constructor.newInstance(forwarding);
        } catch (NoSuchMethodException e) {
            instance = null;
            innerException = e;
        } catch (InstantiationException e) {
            instance = null;
            innerException = e;
        } catch (IllegalAccessException e) {
            instance = null;
            innerException = e;
        } catch (InvocationTargetException e) {
            instance = null;
            innerException = e;
        }
        if (innerException != null) {
            throw new IllegalStateException("Failed to create an instance of EventExchanger: " + eventClasses.toString(), innerException);
        }
        return instance;
    }

    public EventController registerEventListenerObject(String key, Object listener) {
        for (EventClasses value : EventClasses.values()) {
            if (value.isImplementedMultiEventListener(listener)) {
                this.eventMulticaster.addEventListener(value.getMultiEventListenerClass(), key, (MultiEventListenerInterfaceBase) listener);
            }
            if (value.isImplementedContentProviderListener(listener)) {
                EventExchangerBase exchanger = this.createEventExchanger(value, (ContentProviderEventListenerInterfaceBase) listener);
                this.eventMulticaster.addEventListener(value.getMultiEventListenerClass(), key, exchanger);
            }
        }
        return this;
    }

    public boolean containsEventKey(EventClasses eventClasses, String key) {
        return this.eventMulticaster.containsEventKey(eventClasses.getMultiEventListenerClass(), key);
    }

    public <V extends EventObject> void raise(EventClasses eventClasses, String key, V param) {
        this.eventMulticaster.fireEvent(eventClasses.getMultiEventListenerClass(), key, param);
    }

    public <V extends EventObject> void raise(EventClasses eventClasses, V param) {
        this.eventMulticaster.fireEvent(eventClasses.getMultiEventListenerClass(), param);
    }

}
