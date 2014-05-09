package com.tojc.ormlite.android.framework.event;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters;
import com.tojc.ormlite.android.framework.event.listener.OnInsertCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnUpdateListener;
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

/**
 * Created by Jaken on 2014/05/08.
 */
public class EventClassesIsImplementedTest extends AndroidTestCase {

    public void testIsImplementedMultiEventListenerTest_the_simple_implementation() {
        TestClassA testA = new TestClassA();
        assertTrue(EventClasses.OnInsertCompleted.isImplementedMultiEventListener(testA));
    }

    public void testIsImplementedMultiEventListenerTest_the_implementation_of_multiple() {
        TestClassB testB = new TestClassB();
        assertTrue(EventClasses.OnInsertCompleted.isImplementedMultiEventListener(testB));
    }

    public void testIsImplementedMultiEventListenerTest_the_implementation_of_a_complex_interface() {
        TestClassC testC = new TestClassC();
        assertTrue(EventClasses.OnInsertCompleted.isImplementedMultiEventListener(testC));
    }

    public void testIsImplementedMultiEventListenerTest_the_implementation_of_other_interfaces() {
        TestClassD testD = new TestClassD();
        assertFalse(EventClasses.OnInsertCompleted.isImplementedMultiEventListener(testD));
    }

    public void testIsImplementedMultiEventListenerTest_the_not_implemented_class() {
        TestClassE testE = new TestClassE();
        assertFalse(EventClasses.OnInsertCompleted.isImplementedMultiEventListener(testE));
    }

    public void testIsImplementedMultiEventListenerTest_the_inherited_class() {
        TestClassF testF = new TestClassF();
        assertTrue(EventClasses.OnInsertCompleted.isImplementedMultiEventListener(testF));
    }

    public void testIsImplementedMultiEventListenerTest_the_null() {
        assertFalse(EventClasses.OnInsertCompleted.isImplementedMultiEventListener(null));
    }

    public void testIsImplementedContentProviderListenerTest_the_simple_implementation() {
        TestClassG testG = new TestClassG();
        assertTrue(EventClasses.OnInsertCompleted.isImplementedContentProviderListener(testG));
    }

    public void testIsImplementedContentProviderListenerTest_the_implementation_of_other_interfaces() {
        TestClassH testH = new TestClassH();
        assertFalse(EventClasses.OnInsertCompleted.isImplementedMultiEventListener(testH));
    }


    private interface InsertPlusUpdateInterface extends OnInsertCompletedMultiEventListener,
            OnUpdateCompletedMultiEventListener {
    }

    // ----------------------------------
    // CLASSES UNDER TEST
    // ----------------------------------
    private class TestClassA implements OnInsertCompletedMultiEventListener {
        public TestClassA() {
        }

        @Override
        public void onInsertCompleted(OnInsertCompletedMultiEventObject e) {
        }
    }

    private class TestClassB implements OnQueryMultiEventListener,
            OnQueryCompletedMultiEventListener,
            OnInsertMultiEventListener,
            OnInsertCompletedMultiEventListener,
            OnDeleteMultiEventListener,
            OnDeleteCompletedMultiEventListener,
            OnUpdateMultiEventListener,
            OnUpdateCompletedMultiEventListener,
            OnBulkInsertMultiEventListener,
            OnBulkInsertCompletedMultiEventListener,
            OnBeforeApplyBatchMultiEventListener,
            OnAfterApplyBatchMultiEventListener {
        public TestClassB() {
        }

        @Override
        public void onAfterApplyBatch(OnAfterApplyBatchMultiEventObject e) {
        }

        @Override
        public void onBeforeApplyBatch(OnBeforeApplyBatchMultiEventObject e) {
        }

        @Override
        public void onBulkInsertCompleted(OnBulkInsertCompletedMultiEventObject e) {
        }

        @Override
        public void onBulkInsert(OnBulkInsertMultiEventObject e) {
        }

        @Override
        public void onDeleteCompleted(OnDeleteCompletedMultiEventObject e) {
        }

        @Override
        public void onDelete(OnDeleteMultiEventObject e) {
        }

        @Override
        public void onInsertCompleted(OnInsertCompletedMultiEventObject e) {
        }

        @Override
        public void onInsert(OnInsertMultiEventObject e) {
        }

        @Override
        public void onQueryCompleted(OnQueryCompletedMultiEventObject e) {
        }

        @Override
        public void onQuery(OnQueryMultiEventObject e) {
        }

        @Override
        public void onUpdateCompleted(OnUpdateCompletedMultiEventObject e) {
        }

        @Override
        public void onUpdate(OnUpdateMultiEventObject e) {
        }
    }

    private class TestClassC implements InsertPlusUpdateInterface {
        public TestClassC() {
        }

        @Override
        public void onInsertCompleted(OnInsertCompletedMultiEventObject e) {
        }

        @Override
        public void onUpdateCompleted(OnUpdateCompletedMultiEventObject e) {
        }
    }

    private class TestClassD implements OnUpdateCompletedMultiEventListener {
        public TestClassD() {
        }

        @Override
        public void onUpdateCompleted(OnUpdateCompletedMultiEventObject e) {
        }
    }

    private class TestClassE {
        public TestClassE() {
        }
    }

    private class TestClassF extends TestClassA {
        public TestClassF() {
        }
    }

    private class TestClassG implements OnInsertCompletedListener<OrmLiteSqliteOpenHelper> {
        @Override
        public void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, OperationParameters.InsertParameters parameter) {
        }
    }

    private class TestClassH implements OnUpdateListener<OrmLiteSqliteOpenHelper> {
        @Override
        public int onUpdate(OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, MatcherPattern target, OperationParameters.UpdateParameters parameter) {
            return 0;
        }
    }

}
