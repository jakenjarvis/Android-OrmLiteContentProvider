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

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters;
import com.tojc.ormlite.android.framework.event.dispatcher.OnInsertCompletedEventDispatcher;
import com.tojc.ormlite.android.framework.event.dispatcher.OnInsertEventDispatcher;
import com.tojc.ormlite.android.framework.event.dispatcher.OnUpdateCompletedEventDispatcher;
import com.tojc.ormlite.android.framework.event.exchanger.OnInsertCompletedEventExchanger;
import com.tojc.ormlite.android.framework.event.exchanger.OnInsertEventExchanger;
import com.tojc.ormlite.android.framework.event.exchanger.OnUpdateCompletedEventExchanger;
import com.tojc.ormlite.android.event.listener.OnInsertCompletedListener;
import com.tojc.ormlite.android.event.listener.OnInsertListener;
import com.tojc.ormlite.android.event.listener.OnUpdateCompletedListener;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnInsertCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnInsertMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.listener.OnUpdateCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multievent.object.OnInsertCompletedMultiEventObject;
import com.tojc.ormlite.android.framework.event.multievent.object.OnInsertMultiEventObject;

/**
 * Created by Jaken on 2014/05/07.
 */
public class EventDispatcherBaseWithEventExchangerTest extends AndroidTestCase {

    private static final String TEST_AUTHORITY = "tojc.com";
    private static final String TEST_EVENT_KEY = "TEST";

    private final Uri targetTestUri = new Uri.Builder()
            .scheme("http")
            .authority(TEST_AUTHORITY)
            .build();

    private int count = 0;

    public void testEventDispatcherBase2_the_call_by_specifying_key_events_of_one() {
        EventMulticaster eventMulticaster = new EventMulticaster();

        eventMulticaster.registerMultiEventDispatcher(OnInsertCompletedMultiEventListener.class, new OnInsertCompletedEventDispatcher());

        OnInsertCompletedEventExchanger exchanger1 = new OnInsertCompletedEventExchanger(new OnInsertCompletedListener() {
            @Override
            public void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, OperationParameters.InsertParameters parameter) {
                assertEquals(uri.toString(), targetTestUri.toString());
            }
        });
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, exchanger1);

        OnInsertCompletedMultiEventObject param = new OnInsertCompletedMultiEventObject(this, null, targetTestUri, null, null);
        eventMulticaster.fireEvent(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, param);
    }

    public void testEventDispatcherBase2_the_call_by_not_specifying_key_events_of_one() {
        EventMulticaster eventMulticaster = new EventMulticaster();
        eventMulticaster.registerMultiEventDispatcher(OnInsertCompletedMultiEventListener.class, new OnInsertCompletedEventDispatcher());

        OnInsertCompletedEventExchanger exchanger1 = new OnInsertCompletedEventExchanger(new OnInsertCompletedListener() {
            @Override
            public void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, OperationParameters.InsertParameters parameter) {
                assertEquals(uri.toString(), targetTestUri.toString());
            }
        });
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, exchanger1);

        OnInsertCompletedMultiEventObject param = new OnInsertCompletedMultiEventObject(this, null, targetTestUri, null, null);
        eventMulticaster.fireEvent(OnInsertCompletedMultiEventListener.class, param);
    }


    public void testEventDispatcherBase2_the_call_by_not_specifying_key_events_of_two() {
        EventMulticaster eventMulticaster = new EventMulticaster();
        this.count = 0;

        eventMulticaster.registerMultiEventDispatcher(OnInsertCompletedMultiEventListener.class, new OnInsertCompletedEventDispatcher());

        OnInsertCompletedEventExchanger exchanger1 = new OnInsertCompletedEventExchanger(new OnInsertCompletedListener() {
            @Override
            public void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, OperationParameters.InsertParameters parameter) {
                assertEquals(uri.toString(), targetTestUri.toString());
                count++;
            }
        });
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, exchanger1);

        OnInsertCompletedEventExchanger exchanger2 = new OnInsertCompletedEventExchanger(new OnInsertCompletedListener() {
            @Override
            public void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, OperationParameters.InsertParameters parameter) {
                assertEquals(uri.toString(), targetTestUri.toString());
                count++;
            }
        });
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, exchanger2);

        OnInsertCompletedMultiEventObject param = new OnInsertCompletedMultiEventObject(this, null, targetTestUri, null, null);
        eventMulticaster.fireEvent(OnInsertCompletedMultiEventListener.class, param);

        assertEquals(this.count, 2);
    }


    public void testEventDispatcherBase2_the_call_by_mixed_specifying_key_events_of_two_case1() {
        EventMulticaster eventMulticaster = new EventMulticaster();
        this.count = 0;

        eventMulticaster.registerMultiEventDispatcher(OnInsertCompletedMultiEventListener.class, new OnInsertCompletedEventDispatcher());

        // KEY
        OnInsertCompletedEventExchanger exchanger1 = new OnInsertCompletedEventExchanger(new OnInsertCompletedListener() {
            @Override
            public void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, OperationParameters.InsertParameters parameter) {
                assertEquals(uri.toString(), targetTestUri.toString());
                count++;
            }
        });
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, exchanger1);

        // NOT KEY
        OnInsertCompletedEventExchanger exchanger2 = new OnInsertCompletedEventExchanger(new OnInsertCompletedListener() {
            @Override
            public void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, OperationParameters.InsertParameters parameter) {
                count++;
                fail();
            }
        });
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, exchanger2);

        OnInsertCompletedMultiEventObject param = new OnInsertCompletedMultiEventObject(this, null, targetTestUri, null, null);
        eventMulticaster.fireEvent(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, param);

        assertEquals(this.count, 1);
    }

    public void testEventDispatcherBase2_the_call_by_mixed_specifying_key_events_of_two_case2() {
        EventMulticaster eventMulticaster = new EventMulticaster();
        this.count = 0;

        eventMulticaster.registerMultiEventDispatcher(OnInsertCompletedMultiEventListener.class, new OnInsertCompletedEventDispatcher());

        // KEY
        OnInsertCompletedEventExchanger exchanger1 = new OnInsertCompletedEventExchanger(new OnInsertCompletedListener() {
            @Override
            public void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, OperationParameters.InsertParameters parameter) {
                assertEquals(uri.toString(), targetTestUri.toString());
                count++;
            }
        });
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, exchanger1);

        // NOT KEY
        OnInsertCompletedEventExchanger exchanger2 = new OnInsertCompletedEventExchanger(new OnInsertCompletedListener() {
            @Override
            public void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, OperationParameters.InsertParameters parameter) {
                assertEquals(uri.toString(), targetTestUri.toString());
                count++;
            }
        });
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, exchanger2);

        OnInsertCompletedMultiEventObject param = new OnInsertCompletedMultiEventObject(this, null, targetTestUri, null, null);
        eventMulticaster.fireEvent(OnInsertCompletedMultiEventListener.class, param);

        assertEquals(this.count, 2);
    }


    public void testEventDispatcherBase2_the_call_by_specifying_key_events_of_two_different_mixed_event_case1() {
        EventMulticaster eventMulticaster = new EventMulticaster();
        this.count = 0;

        eventMulticaster.registerMultiEventDispatcher(OnInsertCompletedMultiEventListener.class, new OnInsertCompletedEventDispatcher());
        eventMulticaster.registerMultiEventDispatcher(OnUpdateCompletedMultiEventListener.class, new OnUpdateCompletedEventDispatcher());

        // KEY Insert
        OnInsertCompletedEventExchanger exchanger1 = new OnInsertCompletedEventExchanger(new OnInsertCompletedListener() {
            @Override
            public void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, OperationParameters.InsertParameters parameter) {
                assertEquals(uri.toString(), targetTestUri.toString());
                count++;
            }
        });
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, exchanger1);

        // KEY Update
        OnUpdateCompletedEventExchanger exchanger2 = new OnUpdateCompletedEventExchanger(new OnUpdateCompletedListener() {
            @Override
            public void onUpdateCompleted(int result, Uri uri, MatcherPattern target, OperationParameters.UpdateParameters parameter) {
                count++;
                fail();
            }
        });
        eventMulticaster.addEventListener(OnUpdateCompletedMultiEventListener.class, TEST_EVENT_KEY, exchanger2);

        OnInsertCompletedMultiEventObject param = new OnInsertCompletedMultiEventObject(this, null, targetTestUri, null, null);
        eventMulticaster.fireEvent(OnInsertCompletedMultiEventListener.class, param);

        assertEquals(this.count, 1);
    }

    public void testEventDispatcherBase2_the_call_by_specifying_key_events_of_two_different_mixed_event_case2() {
        EventMulticaster eventMulticaster = new EventMulticaster();
        this.count = 0;

        eventMulticaster.registerMultiEventDispatcher(OnInsertCompletedMultiEventListener.class, new OnInsertCompletedEventDispatcher());
        eventMulticaster.registerMultiEventDispatcher(OnUpdateCompletedMultiEventListener.class, new OnUpdateCompletedEventDispatcher());

        // KEY Insert
        OnInsertCompletedEventExchanger exchanger1 = new OnInsertCompletedEventExchanger(new OnInsertCompletedListener() {
            @Override
            public void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, OperationParameters.InsertParameters parameter) {
                assertEquals(uri.toString(), targetTestUri.toString());
                count++;
            }
        });
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, exchanger1);

        // KEY Update
        OnUpdateCompletedEventExchanger exchanger2 = new OnUpdateCompletedEventExchanger(new OnUpdateCompletedListener() {
            @Override
            public void onUpdateCompleted(int result, Uri uri, MatcherPattern target, OperationParameters.UpdateParameters parameter) {
                count++;
                fail();
            }
        });
        eventMulticaster.addEventListener(OnUpdateCompletedMultiEventListener.class, TEST_EVENT_KEY, exchanger2);

        OnInsertCompletedMultiEventObject param = new OnInsertCompletedMultiEventObject(this, null, targetTestUri, null, null);
        eventMulticaster.fireEvent(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, param);

        assertEquals(this.count, 1);
    }


    public void testEventDispatcherBase2_the_check_return_value() {
        EventMulticaster eventMulticaster = new EventMulticaster();
        eventMulticaster.registerMultiEventDispatcher(OnInsertMultiEventListener.class, new OnInsertEventDispatcher());

        OnInsertEventExchanger exchanger1 = new OnInsertEventExchanger(new OnInsertListener() {
            @Override
            public Uri onInsert(OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, MatcherPattern target, OperationParameters.InsertParameters parameter) {
                return targetTestUri;
            }
        });
        eventMulticaster.addEventListener(OnInsertMultiEventListener.class, TEST_EVENT_KEY, exchanger1);

        OnInsertMultiEventObject param = new OnInsertMultiEventObject(this, null, null, null, null);
        eventMulticaster.fireEvent(OnInsertMultiEventListener.class, TEST_EVENT_KEY, param);

        assertEquals(param.getReturnValue().toString(), targetTestUri.toString());
    }

}
