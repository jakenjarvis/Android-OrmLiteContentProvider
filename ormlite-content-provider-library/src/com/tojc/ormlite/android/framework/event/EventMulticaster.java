/*
Copyright (c) 2012, Jaken
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the following conditions are met:
Redistributions of source code must retain the above copyright notice, this list
 of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this
 list of conditions and the following disclaimer in the documentation and/or
 other materials provided with the distribution.
Neither the name of the Jaken's laboratory nor the names of its contributors may
 be used to endorse or promote products derived from this software without
 specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.tojc.ormlite.android.framework.event;

import android.util.Log;

import java.util.EventListener;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EventMulticaster {
    private static final String TAG = "EventMulticaster";
    private Map<Class<?>, MultiEventDispatcher<?, ?>> multiEventDispatcherList;
    private Map<Class<?>, Map<String, EventListener>> multiEventListenerList;

    public EventMulticaster() {
        this.multiEventDispatcherList = new LinkedHashMap<Class<?>, MultiEventDispatcher<?, ?>>();
        this.multiEventListenerList = new LinkedHashMap<Class<?>, Map<String, EventListener>>();
    }

    public <T extends EventListener, S extends EventObject> void registerMultiEventDispatcher(Class<T> token, MultiEventDispatcher<T, S> dispatcher) {
        this.multiEventDispatcherList.put(token, dispatcher);
    }

    public <T extends EventListener> void addEventListener(Class<T> token, T listener) {
        this.addEventListener(token, null, listener);
    }

    public <T extends EventListener> void addEventListener(Class<T> token, String key, T listener) {
        String targetKey = getTargetKey(key);
        Log.d(TAG, token.getSimpleName() + ":" + targetKey);

        if (!this.multiEventListenerList.containsKey(token)) {
            @SuppressWarnings("unchecked")
            Map<String, EventListener> list = (Map<String, EventListener>) new LinkedHashMap<String, T>();

            list.put(targetKey, listener);
            this.multiEventListenerList.put(token, list);
        } else {
            Map<String, EventListener> list = this.multiEventListenerList.get(token);

            if (list.containsKey(targetKey)) {
                Log.d(TAG, "replace : " + targetKey);
            }

            list.put(targetKey, listener);
        }
    }

    private String getTargetKey(String key) {
        String result = "";
        if ((key == null) || (key.length() <= 0)) {
            result = UUID.randomUUID().toString();
        } else {
            result = key;
        }
        return result;
    }

    public <T extends EventListener> void removeEventListener(Class<T> token, T listener) {
        this.removeEventListener(token, null, listener);
    }

    public <T extends EventListener> void removeEventListener(Class<T> token, String key, T listener) {
        List<String> removeList = new LinkedList<String>();

        if (this.multiEventListenerList.containsKey(token)) {
            Map<String, EventListener> list = this.multiEventListenerList.get(token);

            if ((key == null) || (key.length() <= 0)) {
                for (Map.Entry<String, EventListener> entry : list.entrySet()) {
                    if (entry.getValue().equals(listener)) {
                        removeList.add(entry.getKey());
                    }
                }
            } else {
                removeList.add(key);
            }

            for (String target : removeList) {
                list.remove(target);
            }
        }
    }

    public <T extends EventListener, S extends EventObject> void fireEvent(Class<T> token, S param) {
        this.fireEvent(token, null, param);
    }

    public <T extends EventListener, S extends EventObject> void fireEvent(Class<T> token, String key, S param) {
        @SuppressWarnings("unchecked")
        MultiEventDispatcher<T, S> dispatcher = (MultiEventDispatcher<T, S>) this.multiEventDispatcherList.get(token);

        if (dispatcher != null) {
            @SuppressWarnings("unchecked")
            Map<String, T> list = (Map<String, T>) this.multiEventListenerList.get(token);

            if (list != null) {
                if ((key == null) || (key.length() <= 0)) {
                    for (Map.Entry<String, T> entry : list.entrySet()) {
                        dispatcher.dispatch(entry.getValue(), param);
                    }
                } else {
                    for (Map.Entry<String, T> entry : list.entrySet()) {
                        if (entry.getKey().equals(key)) {
                            dispatcher.dispatch(entry.getValue(), param);
                        }
                    }
                }
            }
        }
    }

    public interface MultiEventDispatcher<T extends EventListener, S extends EventObject> {
        void dispatch(T listener, S param);
    }

}
