package com.tojc.ormlite.android;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventListenerInterfaceBase;

import java.io.Serializable;

/**
 * Created by Jaken on 2014/05/05.
 */
public abstract class OrmLiteContentProviderFragment<U extends OrmLiteBaseContentProvider<T>, T extends OrmLiteSqliteOpenHelper> implements MultiEventListenerInterfaceBase, Serializable {
    private static final long serialVersionUID = 6174454914640278455L;

    private U contentProvider = null;

    public OrmLiteContentProviderFragment(U contentProvider) {
        this.contentProvider = contentProvider;
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

    // TODO: http://yuki312.blogspot.jp/2012/03/androiduriuribuilderapi.html

    public abstract String getKeyName();

    protected abstract void onAppendMatcherPatterns(MatcherController matcherController);

    public U getContentProvider() {
        return this.contentProvider;
    }

    public T getHelper() {
        return this.getContentProvider().getHelper();
    }

    public ConnectionSource getConnectionSource() {
        return this.getHelper().getConnectionSource();
    }
}
