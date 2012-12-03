Android-OrmLiteContentProvider
==============================

# What ORMLite?
See [ORMLite](http://ormlite.com/) and [ORMLite : Android Supports](http://ormlite.com/sqlite_java_android_orm.shtml)
, [ORMLite : Using With Android](http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_4.html#SEC40).

# What ContentProvider
See [Android Developers : Content Provider](http://developer.android.com/intl/ja/guide/topics/providers/content-providers.html)

# This is what can be done?
This is a library that easy to make using ContentProvider with OrmLite.  
With this library, you can focus on the operation of the table.  
You can from among the following three of the abstract class, select the inheritance class.  

android.content.ContentProvider  
　　└OrmLiteBaseContentProvider  
　　　└OrmLiteDefaultContentProvider  
　　　　└OrmLiteSimpleContentProvider  

Can be used to match the level of your implementation.  

# How to use.
About the OrmLiteSimpleContentProvider easiest, I will introduce the procedure.

## Downloading ORMLite and OrmLiteContentProvider Jar
* ormlite-core-4.42.jar
* ormlite-android-4.42.jar
* ormlitecontentprovider.jar (this project)

Add these to your project.

## Implementing a Contract Class
See [Android Developers : Implementing a Contract Class](http://developer.android.com/intl/ja/guide/topics/providers/content-provider-creating.html#ContractClass)  
You define the column name as a string. You are free to define it.

    public class Contract
    {
        public static final String DATABASE_NAME = "MyDatabase";
        public static final int DATABASE_VERSION = 1;

        public static final String AUTHORITY = "com.tojc.ormlite.android.ormlitecontentprovidersample";

        // accounts table info
        public static class Account implements BaseColumns
        {
            public static final String TABLENAME = "accounts";

            public static final String CONTENT_URI_PATH = TABLENAME;

            public static final String MIMETYPE_TYPE = TABLENAME;
            public static final String MIMETYPE_NAME = AUTHORITY + ".provider";

            // feild info
            public static final String NAME = "name";

            // content uri pattern code
            public static final int CONTENT_URI_PATTERN_MANY = 1;
            public static final int CONTENT_URI_PATTERN_ONE = 2;

            // Refer to activity.
            public static final Uri contentUri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(CONTENT_URI_PATH)
                .build();
        }
    }

## Configuring a Class
See [ORMLite documents : Configuring a Class](http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_1.html#SEC3)  
You can use the annotations added by OrmLiteContentProvider library. For added annotations, see the javadoc.
* @DefaultContentUri
* @DefaultContentMimeTypeVnd
* @DefaultSortOrder
* @ProjectionMap

    @DatabaseTable(tableName = Contract.Account.TABLENAME)
    @DefaultContentUri(authority=Contract.AUTHORITY, path=Contract.Account.CONTENT_URI_PATH)
    @DefaultContentMimeTypeVnd(name=Contract.Account.MIMETYPE_NAME, type=Contract.Account.MIMETYPE_TYPE)
    public class Account
    {
        @DatabaseField(columnName = Contract.Account._ID, generatedId = true)
        @DefaultSortOrder
        private int id;

        @DatabaseField
        private String name;

        public Account()
        {
            // ORMLite needs a no-arg constructor
        }

        public Account(String name)
        {
            this.id = 0;
            this.name = name;
        }

        public int getId()
        {
            return id;
        }

        public String getName()
        {
            return name;
        }
    }

## Implementing the OrmLiteSqliteOpenHelper Class
See [ORMLite : OrmLiteSqliteOpenHelper](http://ormlite.com/javadoc/ormlite-android/com/j256/ormlite/android/apptools/OrmLiteSqliteOpenHelper.html)
and [Android Developers : SQLiteOpenHelper](http://developer.android.com/intl/ja/reference/android/database/sqlite/SQLiteOpenHelper.html).  
Implementing the OrmLiteSqliteOpenHelper class is required. How to implement OrmLiteSqliteOpenHelper, please refer to manual of ORMLite.

    public class SampleHelper extends OrmLiteSqliteOpenHelper
    {
        public SampleHelper(Context context)
        {
            super(context,
                Contract.DATABASE_NAME,
                null,
                Contract.DATABASE_VERSION
                );
        }

        @Override
        public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
        {
            try
            {
                TableUtils.createTableIfNotExists(connectionSource, Account.class);
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
        {
            try
            {
                TableUtils.dropTable(connectionSource, Account.class, true);
                TableUtils.createTable(connectionSource, Account.class);
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

## Implementing the ContentProvider Class
See [Android Developers : Implementing the ContentProvider Class](http://developer.android.com/intl/ja/guide/topics/providers/content-provider-creating.html#ContentProvider)  
Implement an abstract class OrmLiteSimpleContentProvider.  

    public class MyProvider extends OrmLiteSimpleContentProvider<OrmLiteSqliteSampleHelper>
    {
        @Override
        protected Class<OrmLiteSqliteSampleHelper> getHelperClass()
        {
            return OrmLiteSqliteSampleHelper.class;
        }

        @Override
        public boolean onCreate()
        {
            Controller = new MatcherController()
                .add(Account.class, SubType.Directory, "", Contract.Account.CONTENT_URI_PATTERN_MANY)
                .add(Account.class, SubType.Item, "#", Contract.Account.CONTENT_URI_PATTERN_ONE)
                .initialize();
            return true;
        }
    }

## The <provider> Element
See [Android Developers : The <provider> Element](http://developer.android.com/intl/ja/guide/topics/providers/content-provider-creating.html#ProviderElement).

Add AndroidManifest.xml

    <provider android:name=".MyProvider"
        android:authorities="com.example.testapp.provider"
        android:exported="false"/>


## Accessing a provider
See [Android Developers : Content Provider Basics](http://developer.android.com/intl/ja/guide/topics/providers/content-provider-basics.html).
Run the test program.

    public class MainActivity extends Activity
    {
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            ContentValues values = new ContentValues();
            values.clear();
            values.put(Contract.Account.NAME, "Yamada Tarou");
            getContentResolver().insert(Contract.Account.contentUri, values);

            Cursor c = getContentResolver().query(Contract.Account.contentUri, null, null, null, null);
            while(c.moveToNext())
            {
                for (int i = 0; i < c.getColumnCount(); i++)
                {
                    Log.d(getClass().getSimpleName(), c.getColumnName(i) + " : " + c.getString(i));
                }
            }
            c.close();
        }
    }


# Open Source License (ISC License)
This document is part of the Android-OrmLiteContentProvider project.

Copyright (c) 2012, Jaken Jarvis (jaken.jarvis@gmail.com)

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

The author may be contacted via 
https://github.com/jakenjarvis/Android-OrmLiteContentProvider

