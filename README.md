Android-OrmLiteContentProvider [![Continuous Integration status](https://travis-ci.org/jakenjarvis/Android-OrmLiteContentProvider.png)](https://travis-ci.org/jakenjarvis/Android-OrmLiteContentProvider)
==============================

# Overview
## What's new
### Ver1.0.4
* Extended the automatic generation of the Contract class.
This makes it possible to output by integrating multiple classes.  
* Added the TABLE_NAME(table name) to Contract class to be generated.  
* Subdivided the internal processing.
If you have to override the method, you will be able to intervene in the process some.  

### Ver1.0.2
* This project has changed the license at the same time as published in Maven Central Repository.
Changed the license to 'Apache License, Version 2.0' from 'ISC License'. By this, everyone will be easy to use!  
* Automatic generation of Contract Class.
By Java annotation processing, to automatically generate it based on the table definition class.  

## Features
### This is what can be done?
This is a library that easy to make using ContentProvider with OrmLite.  
With this library, you can focus on the operation of the table.  
You can from among the following three of the abstract class, select the inheritance class.  

    android.content.ContentProvider  
    　　└ OrmLiteBaseContentProvider  
    　　　└ OrmLiteDefaultContentProvider  
    　　　　└ OrmLiteSimpleContentProvider  

You can be used to match the level of your implementation.  
You can focus on implementing the original function.  

## Quick question

### What ORMLite?
See [ORMLite](http://ormlite.com/) and [ORMLite : Android Supports](http://ormlite.com/sqlite_java_android_orm.shtml)
, [ORMLite : Using With Android](http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_4.html#SEC40).

### What ContentProvider?
See [Android Developers : Content Provider](http://developer.android.com/intl/ja/guide/topics/providers/content-providers.html)  

# Tutorials
## Getting Started

### Implementing a Contract Class (Optional)
If you want to automatically generate, skip this step. When implemented compiler, this similar class is created.  

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
            public static final String TABLE_NAME = "accounts";

            public static final String CONTENT_URI_PATH = TABLE_NAME;

            public static final String MIMETYPE_TYPE = TABLE_NAME;
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

### Configuring a Class
See [ORMLite documents : Configuring a Class](http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_1.html#SEC3)  
You can use the annotations added by OrmLiteContentProvider library.

* @Contract
* @DefaultContentUri
* @DefaultContentMimeTypeVnd
* @DefaultSortOrder
* @ProjectionMap

For added annotations, see the javadoc.

    @DatabaseTable(tableName = Contract.Account.TABLE_NAME)
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

### Configuring a Class with Automatic generation of Contract Class.
There is no need for special difficult. You add the @Contract annotation. And Shall be given here information underlying to produce.  

    @Contract()
    @DatabaseTable(tableName = "accounts")
    @DefaultContentUri(authority = "com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample", path = "accounts")
    @DefaultContentMimeTypeVnd(name = "com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.provider", type = "accounts")
    public class Account
    {
        @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
        @DefaultSortOrder
        private int id;

        @DatabaseField
        private String name;

        public Account()
        {
            // ORMLite needs a no-arg constructor
        }

        (Omission)
    }

Compiler generates the following from this definition. You do not have to write this.

    public final class AccountContract implements BaseColumns
    {
        public static final String TABLE_NAME = "accounts";

        public static final String AUTHORITY = "com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample";

        public static final String CONTENT_URI_PATH = "accounts";

        public static final String MIMETYPE_TYPE = "accounts";
        public static final String MIMETYPE_NAME = "com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.provider";

        public static final int CONTENT_URI_PATTERN_MANY = 1;
        public static final int CONTENT_URI_PATTERN_ONE = 2;

        public static final Uri CONTENT_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .appendPath(CONTENT_URI_PATH)
            .build();

        private AccountContract()
        {
        }

        public static final String NAME = "name";
    }

### Implementing the OrmLiteSqliteOpenHelper Class
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

### Implementing the ContentProvider Class
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
	        setMatcherController(new MatcherController()
                .add(Account.class, SubType.DIRECTORY, "", Contract.Account.CONTENT_URI_PATTERN_MANY)
                .add(Account.class, SubType.ITEM, "#", Contract.Account.CONTENT_URI_PATTERN_ONE)
                );
            return true;
        }
    }

By getHelperClass() method to register the Helper class.
To register for a pattern in onCreate(). Creates an instance of MatcherController To do so, call add() method.

#### Flexibility
This is more flexible precisely because the user can set MatcherController arbitrarily.
This is the most important key points of the Android-OrmLiteContentProvider library.

    // Undefined @DefaultContentUri and @DefaultContentMimeTypeVnd annotations.
    // This can be defined using MatcherController.
    @DatabaseTable(tableName = Contract.NewTable.TABLE_NAME)
    public class NewTable
    {
        @DatabaseField(columnName = Contract.NewTable._ID, generatedId = true)
        private int key;

        @DatabaseField
        private String name;

        public NewTable()
        {
            // ORMLite needs a no-arg constructor
        }

        // etc..
    }

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
	        setMatcherController(new MatcherController()
                .add(Account.class, SubType.DIRECTORY, "", Contract.Account.CONTENT_URI_PATTERN_MANY)
                .add(Account.class, SubType.ITEM, "#", Contract.Account.CONTENT_URI_PATTERN_ONE)
                
                .add(Account.class)
                    .add(SubType.DIRECTORY, "", Contract.Account.CONTENT_URI_PATTERN_MANY)
                    .add(SubType.ITEM, "#", Contract.Account.CONTENT_URI_PATTERN_ONE)
                // Add new table. You can add more than one table.
                // Is considered to be set to the table(class) that you have added to end.
                .add(NewTable.class)
                    // Defined DefaultContentUri and DefaultContentMimeTypeVnd.
                    .setDefaultContentUri(
                            Contract.AUTHORITY,
                            Contract.NewTable.CONTENT_URI_PATH)
                    .setDefaultContentMimeTypeVnd(
                            Contract.NewTable.MIMETYPE_NAME,
                            Contract.NewTable.MIMETYPE_TYPE)
                    // (NewTable.class)
                    .add(SubType.DIRECTORY, "", Contract.NewTable.CONTENT_URI_PATTERN_MANY)
                    .add(SubType.ITEM, "#", Contract.NewTable.CONTENT_URI_PATTERN_ONE)
                    // add other pattern. 'content://com.example.app.provider/newtable/dataset'
                    .add(SubType.DIRECTORY, "dataset", Contract.NewTable.CONTENT_URI_PATTERN_DATASET)
                );
            return true;
        }
    }

### The &lt;provider&gt; Element
See [Android Developers : The &lt;provider&gt; Element](http://developer.android.com/intl/ja/guide/topics/providers/content-provider-creating.html#ProviderElement).

Add AndroidManifest.xml

    <provider android:name=".provider.MyProvider"
        android:authorities="com.tojc.ormlite.android.ormlitecontentprovidersample"
        android:exported="false"/>

### Accessing a provider
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

# Install
About the OrmLiteSimpleContentProvider easiest, I will introduce the procedure.

## Apache Maven setup
If you use maven to build your Android project you can simply add a dependency for this library.

    <dependency>
        <groupId>com.tojc.ormlite.android</groupId>
        <artifactId>ormlite-content-provider-library</artifactId>
        <version>${version}</version>
        <type>jar</type> <!-- or apklib -->
    </dependency>

* If you specify the 'apklib', you will need to imported the ormlite-content-provider-library to local workspace.

If you perform the automatic generation of Contract Class, Additional compiler is required.

    <dependency>
        <groupId>com.tojc.ormlite.android</groupId>
        <artifactId>ormlite-content-provider-compiler</artifactId>
        <version>${version}</version>
        <scope>provided</scope>
    </dependency>

## Manual setup
If you’re using the Eclipse with the ADT plugin, you can include a library project and compiler project.  

### Downloading Android-OrmLiteContentProvider

    git clone git@github.com:jakenjarvis/Android-OrmLiteContentProvider.git <Anywhere>

### Import Project's
Add these to your project.  

#### ormlite-content-provider-library
Add the Android Library Project to your project.  
See [Android Developers : Referencing a library project](http://developer.android.com/tools/projects/projects-eclipse.html#ReferencingLibraryProject)  

##### Downloading and add dependency
See [stackoverflow : How to import a jar in Eclipse?](http://stackoverflow.com/questions/3280353/how-to-import-a-jar-in-eclipse)  

Download from [ORMLite : OrmLite Releases](http://ormlite.com/releases/)  

Copy the following files to libs folder.  

* ormlite-core-X.XX.jar
* ormlite-android-X.XX.jar
* ormlite-jdbc-X.XX.jar(If you need)

#### ormlite-content-provider-compiler(Optional)
Add the Java Project(Not Android Project) to your project.  
Compiler will work when build your project. You do not need to include the compiler on your package.  
NOTE: Manual setting be a very tedious task. You must solve all the dependencies. The following shows only important point.  

##### Downloading and add dependency
Download from [github : javawriter](https://github.com/square/javawriter)  

* javawriter-X.X.X.jar

##### Settings .factorypath to your project
ormlite-content-provider-compiler-sample is going to be your reference.  

See [Eclipse help JDT Annotation Processing : Getting Started](http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Fguide%2Fjdt_apt_getting_started.htm)  

    <factorypath>
        <factorypathentry kind="VARJAR" id="YOUR_LOCATION/ormlite-content-provider-compiler-X.X.X.jar" enabled="true" runInBatchMode="false"/>
        <factorypathentry kind="VARJAR" id="YOUR_LOCATION/ormlite-core-X.XX.jar" enabled="true" runInBatchMode="false"/>
        <factorypathentry kind="VARJAR" id="YOUR_LOCATION/ormlite-android-X.XX.jar" enabled="true" runInBatchMode="false"/>
        <factorypathentry kind="VARJAR" id="YOUR_LOCATION/javawriter-X.X.X.jar" enabled="true" runInBatchMode="false"/>
        <factorypathentry kind="VARJAR" id="YOUR_LOCATION/ormlite-content-provider-library-X.X.X.jar" enabled="true" runInBatchMode="false"/>
    </factorypath>

* Another solution : m2e-apt  
This is a better solution to get annotation processing within eclipse using maven settings.

See [m2e-apt](https://github.com/jbosstools/m2e-apt/)  


# Contributor
Thanks to contributors!  
* [Stéphane NICOLAS](https://github.com/stephanenicolas)  
* [Joel Steres](https://github.com/jasco)  
* [Michael Cramer](https://github.com/BigMichi1)  

# Apache License, Version 2.0
This document is part of the Android-OrmLiteContentProvider project.

Copyright (c) 2012, Android-OrmLiteContentProvider Team.
                    Jaken Jarvis (jaken.jarvis@gmail.com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

The author may be contacted via 
https://github.com/jakenjarvis/Android-OrmLiteContentProvider

