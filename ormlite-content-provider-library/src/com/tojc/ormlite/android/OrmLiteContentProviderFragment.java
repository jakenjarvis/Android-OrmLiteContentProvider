package com.tojc.ormlite.android;

import android.content.ContentProvider;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.event.FragmentEventHandling;
import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventListenerInterfaceBase;

import java.io.Serializable;

/**
 * Created by Jaken on 2014/05/05.
 */
public abstract class OrmLiteContentProviderFragment<U extends OrmLiteBaseContentProvider<T>, T extends OrmLiteSqliteOpenHelper> implements MultiEventListenerInterfaceBase, Serializable {
    private static final long serialVersionUID = 6174454914640278455L;

    private U contentProvider = null;

    @SuppressWarnings("unchecked")
    public OrmLiteContentProviderFragment(ContentProvider contentProvider) {
        this.contentProvider = (U) contentProvider;
    }

    public final void onFragmentInitialize(MatcherController matcherController) {
        this.onAppendMatcherPatterns(matcherController);
//        for (Map.Entry<String, OrmLiteContentProviderFragment> entry : this.fragmentMatcherController.getContentProviderFragments().entrySet()) {
//            matcherController.getContentProviderFragments().put(entry.getKey(), entry.getValue());
//        }
//        for (MatcherPattern fragmentPattern : this.fragmentMatcherController.getMatcherPatterns()) {
//            fragmentPattern.setParentContentProviderFragment(this);
//            matcherController.add(fragmentPattern);
//        }
    }

    /**
     * Please return the implementation class of the fragment.
     *
     * @return
     */
    public abstract Class<? extends OrmLiteContentProviderFragment<U, T>> getFragmentClass();

    /**
     * Called at the time to add a MatcherPattern that are associated with this fragment.
     *
     * @param matcherController
     */
    protected abstract void onAppendMatcherPatterns(MatcherController matcherController);

    /**
     * This method will return the event handling type of this fragment.
     * If you want to change the behavior of the event call, please override this method.
     * @see com.tojc.ormlite.android.framework.event.FragmentEventHandling
     *
     * @return
     */
    public int getFragmentEventHandling() {
        return FragmentEventHandling.FRAGMENT_ONLY;
    }

    /**
     * This method will return the events key of fragment.
     * It is good if any unique string. It is necessary to return the same value at all times.
     * If you want to change, please override this method.
     *
     * @return events key string
     */
    public String getKeyName() {
        return this.getFragmentClass().getName();
    }

    /**
     * Get a ContentProvider for this action.
     *
     * @return
     * @see android.content.ContentProvider
     * @see com.tojc.ormlite.android.OrmLiteBaseContentProvider
     * @see com.tojc.ormlite.android.OrmLiteClassifierContentProvider
     */
    public U getContentProvider() {
        return this.contentProvider;
    }

    /**
     * Get a helper for this action.
     *
     * @return Return an instance of the helper.
     * @see com.tojc.ormlite.android.OrmLiteBaseContentProvider#getHelper()
     */
    public T getHelper() {
        return this.getContentProvider().getHelper();
    }

    /**
     * Get a connection source for this action.
     *
     * @return
     * @see com.j256.ormlite.support.ConnectionSource
     * @see com.tojc.ormlite.android.OrmLiteBaseContentProvider#getConnectionSource()
     */
    public ConnectionSource getConnectionSource() {
        return this.getHelper().getConnectionSource();
    }
}
