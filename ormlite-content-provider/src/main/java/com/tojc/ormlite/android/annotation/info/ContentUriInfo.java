package com.tojc.ormlite.android.annotation.info;

import java.lang.reflect.AnnotatedElement;

import android.content.ContentResolver;
import android.net.Uri;

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;

/**
 * Manage the ContentUri information.
 * @author Jaken
 */
public class ContentUriInfo extends AnnotationInfoBase {
    // ----------------------------------
    // ATTRIBUTES
    // ----------------------------------
    private String authority;
    private String path;

    // ----------------------------------
    // CONSTRUCTORS
    // ----------------------------------
    public ContentUriInfo(AnnotatedElement element) {
        DefaultContentUri contentUri = element.getAnnotation(DefaultContentUri.class);
        String authority = null;
        String path = null;
        if (contentUri != null) {
            authority = contentUri.authority();
            path = contentUri.path();
        }

        if (element instanceof Class<?>) {
            Class<?> clazz = (Class<?>) element;
            if (authority == null) {
                authority = clazz.getPackage().getName();
            }
            if (path == null) {
                // TODO use DataBase annotation
                path = clazz.getSimpleName().toLowerCase();
            }
        }
        initialize(authority, path);
    }

    public ContentUriInfo(String authority, String path) {
        initialize(authority, path);
    }

    // ----------------------------------
    // PUBLIC METHODS
    // ----------------------------------
    public String getAuthority() {
        return this.authority;
    }

    public String getPath() {
        return this.path;
    }

    public Uri getContentUri() {
        return new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(this.authority).appendPath(this.path).build();
    }

    @Override
    protected boolean isValidValue() {
        return this.authority.length() >= 1 && this.path.length() >= 1;
    }

    // ----------------------------------
    // PRIVATE METHODS
    // ----------------------------------
    private void initialize(String authority, String path) {
        this.authority = authority;
        this.path = path;
        validFlagOn();
    }
}
