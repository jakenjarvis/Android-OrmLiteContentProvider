package com.tojc.ormlite.android.compiler.sample;

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;

/**
 * A sample to be tested with @ContentUri and @DefaultContentMimeTypeVnd
 * @author SNI
 */
@Contract()
@DefaultContentUri(authority = "com.tojc.ormlite.android.compiler.sample", path = "get_lucky_from_daft_punk")
@DefaultContentMimeTypeVnd(name = "c2c", type = "tetra")
public class Pojo4 {

}
