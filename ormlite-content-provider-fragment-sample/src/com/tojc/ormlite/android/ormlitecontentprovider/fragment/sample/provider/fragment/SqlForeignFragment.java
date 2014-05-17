package com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.tojc.ormlite.android.OrmLiteContentProviderFragment;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;
import com.tojc.ormlite.android.framework.OperationParameters;
import com.tojc.ormlite.android.framework.event.listenerset.DefaultOnQueryListenerSet;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.model.Car;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.model.Fuel;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.CarContract;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.FuelContract;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.SampleHelper;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.SampleProvider;

/**
 * MEMO: This source code is going to be your reference(Note Ver1.0.3). Please compare.
 * https://github.com/jakenjarvis/Android-OrmLiteContentProvider/blob/Issue4_VerificationTest/ormlite-content-provider-compiler-sample/src/com/tojc/ormlite/android/ormlitecontentprovider/compiler/sample/provider/MyProvider.java
 * This is an implementation that does not use OrmLiteContentProviderFragment.
 * <p/>
 * Created by Jaken on 2014/05/10.
 */
public class SqlForeignFragment extends OrmLiteContentProviderFragment<SampleProvider, SampleHelper> implements DefaultOnQueryListenerSet<SampleHelper> {
    @Override
    public Class<? extends OrmLiteContentProviderFragment<SampleProvider, SampleHelper>> getFragmentClass() {
        return SqlForeignFragment.class;
    }

    @Override
    protected void onAppendMatcherPatterns(MatcherController matcherController) {
        // You register the MatcherPattern. It is only intended to handle in this fragment.
        matcherController
                .add(Car.class, SubType.DIRECTORY, "", CarContract.CONTENT_URI_PATTERN_MANY)//
                .add(Car.class, SubType.ITEM, "#", CarContract.CONTENT_URI_PATTERN_ONE)//
                .add(Fuel.class, SubType.DIRECTORY, "", FuelContract.CONTENT_URI_PATTERN_MANY)//
                .add(Fuel.class, SubType.ITEM, "#", FuelContract.CONTENT_URI_PATTERN_ONE);//
    }

    @Override
    public Cursor onQuery(SampleHelper helper, SQLiteDatabase db, MatcherPattern target, OperationParameters.QueryParameters parameter) {
        Cursor result = null;
        if (target.getTableInfo().getName().equals(CarContract.TABLE_NAME)) {
            String sql = "";
            sql += "SELECT `Car`.`_id`, `Car`.`name`, `Car`.`fuel_id`, `Fuel`.`name` ";
            sql += " FROM `Car` LEFT JOIN `Fuel` ON `Car`.`fuel_id` = `Fuel`.`_id` ";

            // where
            switch (target.getPatternCode()) {
                case CarContract.CONTENT_URI_PATTERN_MANY:
                    break;

                case CarContract.CONTENT_URI_PATTERN_ONE:
                    sql += " WHERE ";
                    sql += " `Car`.`_id` = ? ";
                    break;

                default:
                    break;
            }

            // orderBy
            sql += " ORDER BY ";
            sql += " `Car`.`_id` ";
            sql += " ";

            Log.d("SQL", sql);

            result = db.rawQuery(sql, parameter.getSelectionArgs());

        } else if (target.getTableInfo().getName().equals(FuelContract.TABLE_NAME)) {
            // Not implemented.
        }
        return result;
    }

    @Override
    public void onQueryCompleted(Cursor result, Uri uri, MatcherPattern target, OperationParameters.QueryParameters parameter) {
        this.getContentProvider().onQueryCompleted(result, uri, target, parameter);
    }
}
