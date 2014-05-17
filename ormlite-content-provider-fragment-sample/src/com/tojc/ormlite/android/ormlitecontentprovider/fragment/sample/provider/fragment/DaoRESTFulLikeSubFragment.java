package com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.tojc.ormlite.android.OrmLiteContentProviderFragment;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;
import com.tojc.ormlite.android.framework.OperationParameters;
import com.tojc.ormlite.android.framework.event.FragmentEventHandling;
import com.tojc.ormlite.android.framework.event.listenerset.DefaultOnQueryListenerSet;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.model.Block;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.BlockContract;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.SampleHelper;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.SampleProvider;

import java.sql.SQLException;

/**
 * Created by Jaken on 2014/05/12.
 */
public class DaoRESTFulLikeSubFragment extends OrmLiteContentProviderFragment<SampleProvider, SampleHelper> implements DefaultOnQueryListenerSet<SampleHelper> {
    @Override
    public Class<? extends OrmLiteContentProviderFragment<SampleProvider, SampleHelper>> getFragmentClass() {
        return DaoRESTFulLikeSubFragment.class;
    }

    @Override
    protected void onAppendMatcherPatterns(MatcherController matcherController) {
        // You register the MatcherPattern. It is only intended to handle in this fragment.
        matcherController
                .add(Block.class, SubType.DIRECTORY, BlockContract.PATTERN_BLOCKS_LNAME, BlockContract.CONTENT_URI_PATTERN_BLOCKS_LNAME)//
                .add(Block.class, SubType.DIRECTORY, BlockContract.PATTERN_BLOCKS_RNAME, BlockContract.CONTENT_URI_PATTERN_BLOCKS_RNAME);//
    }

    @Override
    public int getFragmentEventHandling() {
        // Events of non DefaultOnQueryListenerSet, delegate to ContentProvider.
        return FragmentEventHandling.FRAGMENT_AND_DEFAULT_FORWARD;
    }

    @Override
    public Cursor onQuery(SampleHelper helper, SQLiteDatabase db, MatcherPattern target, OperationParameters.QueryParameters parameter) {
        Cursor result = null;
        try {
            final Dao<Block, Integer> blockDao = this.getHelper().getDao(Block.class);
            QueryBuilder<Block, Integer> queryBlock = blockDao.queryBuilder();

            switch (target.getPatternCode()) {
                case BlockContract.CONTENT_URI_PATTERN_BLOCKS_LNAME:
                    queryBlock.where().like(
                            BlockContract.NAME,
                            parameter.getUri().getPathSegments().get(2) + "%");
                    break;

                case BlockContract.CONTENT_URI_PATTERN_BLOCKS_RNAME:
                    queryBlock.where().like(
                            BlockContract.NAME,
                            "%" + parameter.getUri().getPathSegments().get(2));
                    break;

                default:
                    break;
            }

            // orderBy
            // It can be called a public method of the content provider.
            queryBlock.orderByRaw(this.getContentProvider().getSortOrderStringForQuery(target, parameter));

            Log.d("SQL", queryBlock.prepareStatementString());

            CloseableIterator<Block> iterator = null;
            iterator = blockDao.iterator(queryBlock.prepare());
            AndroidDatabaseResults databaseResults = (AndroidDatabaseResults) iterator.getRawResults();

            result = databaseResults.getRawCursor();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onQueryCompleted(Cursor result, Uri uri, MatcherPattern target, OperationParameters.QueryParameters parameter) {
        this.getContentProvider().onQueryCompleted(result, uri, target, parameter);
    }
}
