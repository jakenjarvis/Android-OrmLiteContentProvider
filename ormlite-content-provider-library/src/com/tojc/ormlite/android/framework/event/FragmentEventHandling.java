package com.tojc.ormlite.android.framework.event;

/**
 * Created by Jaken on 2014/05/12.
 */
public final class FragmentEventHandling {
    /**
     * Events notify the only fragment. If listener is not implemented, it does nothing.
     */
    public static final int FRAGMENT_ONLY = 1;
    /**
     * If listener of fragment is not implemented, forward to default.
     * This is useful for implementing a fragment only some processing.
     */
    public static final int FRAGMENT_AND_DEFAULT_FORWARD = 2;
    /**
     * Events notify the content provider along with the fragment.
     * You must be careful that the event is duplicated.
     */
    public static final int FRAGMENT_AND_DEFAULT_DUPLICATE = 3;

    private FragmentEventHandling() {
    }
}
