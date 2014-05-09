package com.tojc.ormlite.android.framework.event;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.tojc.ormlite.android.framework.event.multieventlistener.OnInsertCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventlistener.OnUpdateCompletedMultiEventListener;
import com.tojc.ormlite.android.framework.event.multieventobject.OnInsertCompletedMultiEventObject;
import com.tojc.ormlite.android.framework.event.multieventobject.OnUpdateCompletedMultiEventObject;

/**
 * Created by Jaken on 2014/05/07.
 */
@SmallTest
public class EventMulticasterTest extends AndroidTestCase {

    private static final String TEST_AUTHORITY = "tojc.com";
    private static final String TEST_EVENT_KEY = "TEST";

    private final Uri targetTestUri = new Uri.Builder()
            .scheme("http")
            .authority(TEST_AUTHORITY)
            .build();

    private int count = 0;

    public void testEventMulticaster_the_call_by_specifying_key_events_of_one() {
        EventMulticaster eventMulticaster = new EventMulticaster();

        eventMulticaster.registerMultiEventDispatcher(
                OnInsertCompletedMultiEventListener.class,
                new EventMulticaster.MultiEventDispatcher<OnInsertCompletedMultiEventListener, OnInsertCompletedMultiEventObject>() {
                    @Override
                    public void dispatch(OnInsertCompletedMultiEventListener listener, OnInsertCompletedMultiEventObject param) {
                        listener.onInsertCompleted(param);
                    }
                }
        );

        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, new OnInsertCompletedMultiEventListener() {
            @Override
            public void onInsertCompleted(OnInsertCompletedMultiEventObject e) {
                Uri testUri = e.getUri();
                assertEquals(testUri.toString(), targetTestUri.toString());
            }
        });

        OnInsertCompletedMultiEventObject param = new OnInsertCompletedMultiEventObject(this, null, targetTestUri, null, null);
        eventMulticaster.fireEvent(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, param);
    }

    public void testEventMulticaster_the_call_by_not_specifying_key_events_of_one() {
        EventMulticaster eventMulticaster = new EventMulticaster();

        eventMulticaster.registerMultiEventDispatcher(
                OnInsertCompletedMultiEventListener.class,
                new EventMulticaster.MultiEventDispatcher<OnInsertCompletedMultiEventListener, OnInsertCompletedMultiEventObject>() {
                    @Override
                    public void dispatch(OnInsertCompletedMultiEventListener listener, OnInsertCompletedMultiEventObject param) {
                        listener.onInsertCompleted(param);
                    }
                }
        );

        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, new OnInsertCompletedMultiEventListener() {
            @Override
            public void onInsertCompleted(OnInsertCompletedMultiEventObject e) {
                Uri testUri = e.getUri();
                assertEquals(testUri.toString(), targetTestUri.toString());
            }
        });

        OnInsertCompletedMultiEventObject param = new OnInsertCompletedMultiEventObject(this, null, targetTestUri, null, null);
        eventMulticaster.fireEvent(OnInsertCompletedMultiEventListener.class, param);
    }


    public void testEventMulticaster_the_call_by_not_specifying_key_events_of_two() {
        EventMulticaster eventMulticaster = new EventMulticaster();
        this.count = 0;

        eventMulticaster.registerMultiEventDispatcher(
                OnInsertCompletedMultiEventListener.class,
                new EventMulticaster.MultiEventDispatcher<OnInsertCompletedMultiEventListener, OnInsertCompletedMultiEventObject>() {
                    @Override
                    public void dispatch(OnInsertCompletedMultiEventListener listener, OnInsertCompletedMultiEventObject param) {
                        listener.onInsertCompleted(param);
                    }
                }
        );

        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, new OnInsertCompletedMultiEventListener() {
            @Override
            public void onInsertCompleted(OnInsertCompletedMultiEventObject e) {
                Uri testUri = e.getUri();
                assertEquals(testUri.toString(), targetTestUri.toString());
                count++;
            }
        });

        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, new OnInsertCompletedMultiEventListener() {
            @Override
            public void onInsertCompleted(OnInsertCompletedMultiEventObject e) {
                Uri testUri = e.getUri();
                assertEquals(testUri.toString(), targetTestUri.toString());
                count++;
            }
        });

        OnInsertCompletedMultiEventObject param = new OnInsertCompletedMultiEventObject(this, null, targetTestUri, null, null);
        eventMulticaster.fireEvent(OnInsertCompletedMultiEventListener.class, param);

        assertEquals(this.count, 2);
    }


    public void testEventMulticaster_the_call_by_mixed_specifying_key_events_of_two_case1() {
        EventMulticaster eventMulticaster = new EventMulticaster();
        this.count = 0;

        eventMulticaster.registerMultiEventDispatcher(
                OnInsertCompletedMultiEventListener.class,
                new EventMulticaster.MultiEventDispatcher<OnInsertCompletedMultiEventListener, OnInsertCompletedMultiEventObject>() {
                    @Override
                    public void dispatch(OnInsertCompletedMultiEventListener listener, OnInsertCompletedMultiEventObject param) {
                        listener.onInsertCompleted(param);
                    }
                }
        );

        // KEY
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, new OnInsertCompletedMultiEventListener() {
            @Override
            public void onInsertCompleted(OnInsertCompletedMultiEventObject e) {
                Uri testUri = e.getUri();
                assertEquals(testUri.toString(), targetTestUri.toString());
                count++;
            }
        });

        // NOT KEY
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, new OnInsertCompletedMultiEventListener() {
            @Override
            public void onInsertCompleted(OnInsertCompletedMultiEventObject e) {
                count++;
                fail();
            }
        });

        OnInsertCompletedMultiEventObject param = new OnInsertCompletedMultiEventObject(this, null, targetTestUri, null, null);
        eventMulticaster.fireEvent(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, param);

        assertEquals(this.count, 1);
    }

    public void testEventMulticaster_the_call_by_mixed_specifying_key_events_of_two_case2() {
        EventMulticaster eventMulticaster = new EventMulticaster();
        this.count = 0;

        eventMulticaster.registerMultiEventDispatcher(
                OnInsertCompletedMultiEventListener.class,
                new EventMulticaster.MultiEventDispatcher<OnInsertCompletedMultiEventListener, OnInsertCompletedMultiEventObject>() {
                    @Override
                    public void dispatch(OnInsertCompletedMultiEventListener listener, OnInsertCompletedMultiEventObject param) {
                        listener.onInsertCompleted(param);
                    }
                }
        );

        // KEY
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, new OnInsertCompletedMultiEventListener() {
            @Override
            public void onInsertCompleted(OnInsertCompletedMultiEventObject e) {
                Uri testUri = e.getUri();
                assertEquals(testUri.toString(), targetTestUri.toString());
                count++;
            }
        });

        // NOT KEY
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, new OnInsertCompletedMultiEventListener() {
            @Override
            public void onInsertCompleted(OnInsertCompletedMultiEventObject e) {
                Uri testUri = e.getUri();
                assertEquals(testUri.toString(), targetTestUri.toString());
                count++;
            }
        });

        OnInsertCompletedMultiEventObject param = new OnInsertCompletedMultiEventObject(this, null, targetTestUri, null, null);
        eventMulticaster.fireEvent(OnInsertCompletedMultiEventListener.class, param);

        assertEquals(this.count, 2);
    }


    public void testEventMulticaster_the_call_by_specifying_key_events_of_two_different_mixed_event_case1() {
        EventMulticaster eventMulticaster = new EventMulticaster();
        this.count = 0;

        eventMulticaster.registerMultiEventDispatcher(
                OnInsertCompletedMultiEventListener.class,
                new EventMulticaster.MultiEventDispatcher<OnInsertCompletedMultiEventListener, OnInsertCompletedMultiEventObject>() {
                    @Override
                    public void dispatch(OnInsertCompletedMultiEventListener listener, OnInsertCompletedMultiEventObject param) {
                        listener.onInsertCompleted(param);
                    }
                }
        );

        eventMulticaster.registerMultiEventDispatcher(
                OnUpdateCompletedMultiEventListener.class,
                new EventMulticaster.MultiEventDispatcher<OnUpdateCompletedMultiEventListener, OnUpdateCompletedMultiEventObject>() {
                    @Override
                    public void dispatch(OnUpdateCompletedMultiEventListener listener, OnUpdateCompletedMultiEventObject param) {
                        listener.onUpdateCompleted(param);
                    }
                }
        );

        // KEY Insert
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, new OnInsertCompletedMultiEventListener() {
            @Override
            public void onInsertCompleted(OnInsertCompletedMultiEventObject e) {
                Uri testUri = e.getUri();
                assertEquals(testUri.toString(), targetTestUri.toString());
                count++;
            }
        });

        // KEY Update
        eventMulticaster.addEventListener(OnUpdateCompletedMultiEventListener.class, TEST_EVENT_KEY, new OnUpdateCompletedMultiEventListener() {
            @Override
            public void onUpdateCompleted(OnUpdateCompletedMultiEventObject e) {
                count++;
                fail();
            }
        });

        OnInsertCompletedMultiEventObject param = new OnInsertCompletedMultiEventObject(this, null, targetTestUri, null, null);
        eventMulticaster.fireEvent(OnInsertCompletedMultiEventListener.class, param);

        assertEquals(this.count, 1);
    }

    public void testEventMulticaster_the_call_by_specifying_key_events_of_two_different_mixed_event_case2() {
        EventMulticaster eventMulticaster = new EventMulticaster();
        this.count = 0;

        eventMulticaster.registerMultiEventDispatcher(
                OnInsertCompletedMultiEventListener.class,
                new EventMulticaster.MultiEventDispatcher<OnInsertCompletedMultiEventListener, OnInsertCompletedMultiEventObject>() {
                    @Override
                    public void dispatch(OnInsertCompletedMultiEventListener listener, OnInsertCompletedMultiEventObject param) {
                        listener.onInsertCompleted(param);
                    }
                }
        );

        eventMulticaster.registerMultiEventDispatcher(
                OnUpdateCompletedMultiEventListener.class,
                new EventMulticaster.MultiEventDispatcher<OnUpdateCompletedMultiEventListener, OnUpdateCompletedMultiEventObject>() {
                    @Override
                    public void dispatch(OnUpdateCompletedMultiEventListener listener, OnUpdateCompletedMultiEventObject param) {
                        listener.onUpdateCompleted(param);
                    }
                }
        );

        // KEY Insert
        eventMulticaster.addEventListener(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, new OnInsertCompletedMultiEventListener() {
            @Override
            public void onInsertCompleted(OnInsertCompletedMultiEventObject e) {
                Uri testUri = e.getUri();
                assertEquals(testUri.toString(), targetTestUri.toString());
                count++;
            }
        });

        // KEY Update
        eventMulticaster.addEventListener(OnUpdateCompletedMultiEventListener.class, TEST_EVENT_KEY, new OnUpdateCompletedMultiEventListener() {
            @Override
            public void onUpdateCompleted(OnUpdateCompletedMultiEventObject e) {
                count++;
                fail();
            }
        });

        OnInsertCompletedMultiEventObject param = new OnInsertCompletedMultiEventObject(this, null, targetTestUri, null, null);
        eventMulticaster.fireEvent(OnInsertCompletedMultiEventListener.class, TEST_EVENT_KEY, param);

        assertEquals(this.count, 1);
    }
}
