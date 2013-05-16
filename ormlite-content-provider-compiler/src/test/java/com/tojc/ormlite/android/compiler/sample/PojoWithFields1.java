package com.tojc.ormlite.android.compiler.sample;

import com.j256.ormlite.field.DatabaseField;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;

/**
 * A sample to be tested with fields. One is contractual, not the other.
 * @author SNI
 */
@Contract()
public class PojoWithFields1 {

    private String nonContractualField;

    @DatabaseField
    private String contractualField;
}
