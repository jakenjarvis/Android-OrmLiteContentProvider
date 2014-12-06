package com.tojc.ormlite.android.cursor;

import android.content.ContentValues;
import android.database.Cursor;
import com.tojc.ormlite.android.annotation.OrmLiteAnnotationAccessor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by hidaka on 2014/12/02.
 */
public final class EntityUtils {
    public static final int FIELD_TYPE_INTEGER = 1; // Cursor.FIELD_TYPE_INTEGER;
    public static final int FIELD_TYPE_STRING = 3; // Cursor.FIELD_TYPE_STRING;
    public static final int FIELD_TYPE_FLOAT = 2; // Cursor.FIELD_TYPE_FLOAT;

    public static class ContractInfo {
        final String[] columnNames;
        final int[] columnTypes;
        final Map<String, Integer> columnIndexMap = new HashMap<String, Integer>();

        public ContractInfo(final String[] columnNames, final int[] columnTypes) {
            this.columnNames = columnNames.clone();
            this.columnTypes = columnTypes.clone();

            for (int i = 0; i < columnNames.length; i++) {
                columnIndexMap.put(columnNames[i], i);
            }
        }

        public int getColumnIndex(String columnName) {
            return columnIndexMap.get(columnName);
        }
    }

    // Map from Mime vnd-type to entity class.
    private static Map<String, Class<?>> sVndTypeEntityClassMap = new HashMap<String, Class<?>>();
    // Map from Entity class canonical name to contract info..
    private static Map<String, ContractInfo> sEntityContractMap = new HashMap<String, ContractInfo>();

    private EntityUtils() {
        // dummy constructor
    }

    public static Class<?> getEntityClass(String vndType) {
        return sVndTypeEntityClassMap.get(vndType);
    }

    public static <T> T loadFromCursor(Cursor cursor, Class<T> entityClass) {
        ContractInfo contractInfo = sEntityContractMap.get(entityClass.getCanonicalName());
        try {
            T result = entityClass.newInstance();
            Field[] fields = entityClass.getFields();
            for (Field field : fields) {
                String columnName = OrmLiteAnnotationAccessor.getAnnotationColumnName(field);
                if (columnName != null) {
                    int columnIndex = contractInfo.getColumnIndex(columnName);
                    int columnType = contractInfo.columnTypes[columnIndex];
                    switch (columnType) {
                        case FIELD_TYPE_INTEGER:
                            field.set(result, cursor.getInt(columnIndex));
                            break;
                        case FIELD_TYPE_FLOAT:
                            field.set(result, cursor.getFloat(columnIndex));
                            break;
                        case FIELD_TYPE_STRING:
                        default:
                            field.set(result, cursor.getString(columnIndex));
                            break;
                    }
                }
            }

            return result;
        } catch (InstantiationException e) { //NOPMD
            // ignore
        } catch (IllegalAccessException e) { //NOPMD
            // ignore
        }

        return null;
    }

    public static <T> ContentValues createContentValues(T entity) {
        ContractInfo contractInfo = sEntityContractMap.get(entity.getClass().getCanonicalName());
        ContentValues values = new ContentValues();

        Field[] fields = entity.getClass().getFields();
        for (Field field : fields) {
            String columnName = OrmLiteAnnotationAccessor.getAnnotationColumnName(field);
            if (columnName != null) {
                int columnIndex = contractInfo.getColumnIndex(columnName);
                int columnType = contractInfo.columnTypes[columnIndex];
                try {
                    switch (columnType) {
                        case FIELD_TYPE_INTEGER:
                            values.put(columnName, (Integer) field.get(entity));
                            break;
                        case FIELD_TYPE_FLOAT:
                            values.put(columnName, (Float) field.get(entity));
                            break;
                        case FIELD_TYPE_STRING:
                        default:
                            values.put(columnName, field.get(entity).toString());
                            break;
                    }
                } catch (IllegalAccessException e) { //NOPMD
                    // ignore
                }
            }
        }

        return values;
    }

    public static void registerContractInfo(String typeName, Class<?> entityClass, String[] columnNames, int[] columnTypes) {
        sVndTypeEntityClassMap.put(typeName, entityClass);
        ContractInfo contractInfo = new ContractInfo(columnNames, columnTypes);
        sEntityContractMap.put(entityClass.getCanonicalName(), contractInfo);
    }

}
