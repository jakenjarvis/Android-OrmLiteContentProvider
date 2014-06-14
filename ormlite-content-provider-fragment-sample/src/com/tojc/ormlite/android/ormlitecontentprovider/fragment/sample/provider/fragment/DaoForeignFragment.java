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
import com.tojc.ormlite.android.event.listenerset.DefaultOnQueryListenerSet;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.model.Car;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.model.Fuel;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.CarContract;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.FuelContract;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.SampleHelper;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.SampleProvider;

import java.sql.SQLException;

/**
 * NOTE: This source code is going to be your reference(Note Ver1.0.3). Please compare.
 * This is an implementation that does not use OrmLiteContentProviderFragment.
 * https://github.com/jakenjarvis/Android-OrmLiteContentProvider/blob/Issue4_VerificationTest/ormlite-content-provider-compiler-sample/src/com/tojc/ormlite/android/ormlitecontentprovider/compiler/sample/provider/MyProvider.java
 * <p/>
 * Created by Jaken on 2014/05/10.
 */
public class DaoForeignFragment extends OrmLiteContentProviderFragment<SampleProvider, SampleHelper> implements DefaultOnQueryListenerSet<SampleHelper> {
    @Override
    public Class<? extends OrmLiteContentProviderFragment<SampleProvider, SampleHelper>> getFragmentClass() {
        return DaoForeignFragment.class;
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
            try {
                final Dao<Car, Integer> carDao = this.getHelper().getDao(Car.class);
                final Dao<Fuel, Integer> fuelDao = this.getHelper().getDao(Fuel.class);

                QueryBuilder<Car, Integer> queryCar = carDao.queryBuilder();
                QueryBuilder<Fuel, Integer> queryFuel = fuelDao.queryBuilder();

                // where
                switch (target.getPatternCode()) {
                    case CarContract.CONTENT_URI_PATTERN_MANY:
                        break;

                    case CarContract.CONTENT_URI_PATTERN_ONE:
                        queryCar.where().eq(
                                target.getTableInfo().getIdColumnInfo().getColumnName(),
                                parameter.getUri().getPathSegments().get(1));
                        break;

                    default:
                        break;
                }

                // How do to add a column of Fuel?
                // Since this is impossible to receive the results in the class object, perhaps this feature will not be implemented.
                //queryCar.selectColumns();

                queryCar.leftJoin(queryFuel);

                // orderBy
                // It can be called a public method of the content provider.
                queryCar.orderByRaw(this.getContentProvider().getSortOrderStringForQuery(target, parameter));

                Log.d("SQL", queryCar.prepareStatementString());

                CloseableIterator<Car> iterator = null;
                iterator = carDao.iterator(queryCar.prepare());
                AndroidDatabaseResults databaseResults = (AndroidDatabaseResults) iterator.getRawResults();

                result = databaseResults.getRawCursor();
            } catch (SQLException e) {
                e.printStackTrace();
            }

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
