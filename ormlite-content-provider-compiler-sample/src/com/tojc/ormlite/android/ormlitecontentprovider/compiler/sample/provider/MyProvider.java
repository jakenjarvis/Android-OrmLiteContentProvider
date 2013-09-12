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
package com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.provider;

import java.sql.SQLException;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;
import com.tojc.ormlite.android.framework.OperationParameters.QueryParameters;
import com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.model.Account;
import com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.model.AccountContract;
import com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.model.Car;
import com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.model.CarContract;
import com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.model.Fuel;
import com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.model.FuelContract;

public class MyProvider extends OrmLiteSimpleContentProvider<SampleHelper> {
	@Override
    protected Class<SampleHelper> getHelperClass() {
        return SampleHelper.class;
    }

    @Override
    public boolean onCreate() {
        setMatcherController(new MatcherController()//
                .add(Account.class, SubType.DIRECTORY, "", AccountContract.CONTENT_URI_PATTERN_MANY)//
                .add(Account.class, SubType.ITEM, "#", AccountContract.CONTENT_URI_PATTERN_ONE)
                .add(Car.class, SubType.DIRECTORY, "", CarContract.CONTENT_URI_PATTERN_MANY)//
                .add(Car.class, SubType.ITEM, "#", CarContract.CONTENT_URI_PATTERN_ONE)
                .add(Fuel.class, SubType.DIRECTORY, "", FuelContract.CONTENT_URI_PATTERN_MANY)//
                .add(Fuel.class, SubType.ITEM, "#", FuelContract.CONTENT_URI_PATTERN_ONE));
        return true;
    }

    @Override
	public Cursor onQuery(SampleHelper helper, SQLiteDatabase db, MatcherPattern target, QueryParameters parameter)
	{
    	Cursor result = null;
    	if(target.getTableInfo().getName().equals("car"))
    	{
            try {
                final Dao<Car, Integer> carDao = this.getHelper().getDao(Car.class);
                QueryBuilder<Car, Integer> query = carDao.queryBuilder();

                // where
            	switch(target.getPatternCode())
            	{
            		case CarContract.CONTENT_URI_PATTERN_MANY:
            			break;

            		case CarContract.CONTENT_URI_PATTERN_ONE:
                        query.where().eq(
                        	target.getTableInfo().getIdColumnInfo().getColumnName(),
                        	parameter.getUri().getPathSegments().get(1));
            			break;

            		default:
        				break;
            	}

                // orderBy
                if (parameter.getSortOrder() != null && parameter.getSortOrder().length() >= 1) {
                    query.orderByRaw(parameter.getSortOrder());
                } else {
                    query.orderByRaw(target.getTableInfo().getDefaultSortOrderString());
                }

                CloseableIterator<Car> iterator = null;
                iterator = carDao.iterator(query.prepare());
                AndroidDatabaseResults databaseResults = (AndroidDatabaseResults)iterator.getRawResults();

                result = databaseResults.getRawCursor();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    	}
    	else
    	{
			result = super.onQuery(helper, db, target, parameter);
    	}
		return result;
	}


}
