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

import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;
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
}
