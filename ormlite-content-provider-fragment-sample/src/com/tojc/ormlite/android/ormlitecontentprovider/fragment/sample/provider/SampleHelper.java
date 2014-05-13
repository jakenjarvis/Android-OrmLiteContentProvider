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
package com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.model.Account;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.model.Block;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.model.Car;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.model.Fuel;

import java.sql.SQLException;

public class SampleHelper extends OrmLiteSqliteOpenHelper {
    public SampleHelper(Context context) {
        super(context, "MyDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.dropTable(connectionSource, Account.class, true);
            TableUtils.dropTable(connectionSource, Car.class, true);
            TableUtils.dropTable(connectionSource, Fuel.class, true);
            TableUtils.dropTable(connectionSource, Block.class, true);

            TableUtils.createTableIfNotExists(connectionSource, Account.class);
            TableUtils.createTableIfNotExists(connectionSource, Car.class);
            TableUtils.createTableIfNotExists(connectionSource, Fuel.class);
            TableUtils.createTableIfNotExists(connectionSource, Block.class);

            final Dao<Car, Integer> carDao = this.getDao(Car.class);
            final Dao<Fuel, Integer> fuelDao = this.getDao(Fuel.class);
            final Dao<Block, Integer> blockDao = this.getDao(Block.class);

            // create test data
            Fuel fuel = new Fuel("45L");
            fuelDao.create(fuel);

            Car car = new Car("PRIUS", fuel);
            carDao.create(car);

            // check
            for (Car targetcar : carDao.queryForAll())
            {
                Log.d("", targetcar.getName());
                Fuel targetfuel = targetcar.getFuel();
                Log.d("", targetfuel.getName());
            }

            blockDao.create(new Block("Yamada"));
            blockDao.create(new Block("Kawada"));
            blockDao.create(new Block("Hamada"));
            blockDao.create(new Block("Madara"));
            blockDao.create(new Block("Suzuki"));
            blockDao.create(new Block("Okehazama"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Account.class, true);
            TableUtils.dropTable(connectionSource, Car.class, true);
            TableUtils.dropTable(connectionSource, Fuel.class, true);
            TableUtils.dropTable(connectionSource, Block.class, true);

            TableUtils.createTable(connectionSource, Account.class);
            TableUtils.createTable(connectionSource, Car.class);
            TableUtils.createTable(connectionSource, Fuel.class);
            TableUtils.createTableIfNotExists(connectionSource, Block.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
