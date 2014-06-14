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
package com.tojc.ormlite.android.test.provider;

import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;
import com.tojc.ormlite.android.framework.event.FragmentEventHandling;
import com.tojc.ormlite.android.test.model.Account;
import com.tojc.ormlite.android.test.model.Membership;
import com.tojc.ormlite.android.test.provider.fragment.UnderTestSampleFragment;

public class UnderTestSampleProvider extends OrmLiteSimpleContentProvider<SampleHelper> {
    private int testCase = 0;

    public UnderTestSampleProvider() {
        super();
    }

    public UnderTestSampleProvider(int testCase) {
        super();
        this.testCase = testCase;
    }

    @Override
    protected Class<SampleHelper> getHelperClass() {
        return SampleHelper.class;
    }

    @Override
    public boolean onCreate() {
        switch (this.testCase) {
            default:
                setMatcherController(new MatcherController(this)//
                        .add(Account.class, SubType.DIRECTORY, "", AccountContract.CONTENT_URI_PATTERN_MANY)//
                        .add(Account.class, SubType.ITEM, "#", AccountContract.CONTENT_URI_PATTERN_ONE)//
                        .add(Membership.class, SubType.DIRECTORY, "", MembershipContract.CONTENT_URI_PATTERN_MANY)//
                        .add(Membership.class, SubType.ITEM, "#", MembershipContract.CONTENT_URI_PATTERN_ONE)//
                );
                break;

            case 1:
                setMatcherController(new MatcherController(this)//
                                .add(Account.class, SubType.DIRECTORY, "", AccountContract.CONTENT_URI_PATTERN_MANY)//
                                .add(Account.class, SubType.ITEM, "#", AccountContract.CONTENT_URI_PATTERN_ONE)//
                                .addFragment(new UnderTestSampleFragment() {
                                    @Override
                                    public String getKeyName() {
                                        return "UnderTestSampleFragment";
                                    }

                                    @Override
                                    protected void onAppendMatcherPatterns(MatcherController matcherController) {
                                        matcherController
                                                .add(Membership.class, SubType.DIRECTORY, "", MembershipContract.CONTENT_URI_PATTERN_MANY)//
                                                .add(Membership.class, SubType.ITEM, "#", MembershipContract.CONTENT_URI_PATTERN_ONE)//
                                        ;
                                    }
                                })
                );
                break;

            case 2:
                setMatcherController(new MatcherController(this)//
                        .addFragment(new UnderTestSampleFragment() {
                            @Override
                            public String getKeyName() {
                                return "UnderTestSampleFragment";
                            }

                            @Override
                            protected void onAppendMatcherPatterns(MatcherController matcherController) {
                                matcherController
                                        .add(Account.class, SubType.DIRECTORY, "", AccountContract.CONTENT_URI_PATTERN_MANY)//
                                        .add(Account.class, SubType.ITEM, "#", AccountContract.CONTENT_URI_PATTERN_ONE)//
                                ;
                            }
                        })
                        .add(Membership.class, SubType.DIRECTORY, "", MembershipContract.CONTENT_URI_PATTERN_MANY)//
                        .add(Membership.class, SubType.ITEM, "#", MembershipContract.CONTENT_URI_PATTERN_ONE)//
                );
                break;

            case 3:
                setMatcherController(new MatcherController(this)//
                                .addFragment(new UnderTestSampleFragment() {
                                    @Override
                                    public String getKeyName() {
                                        return "UnderTestSampleFragment1";
                                    }

                                    @Override
                                    protected void onAppendMatcherPatterns(MatcherController matcherController) {
                                        matcherController
                                                .add(Account.class, SubType.DIRECTORY, "", AccountContract.CONTENT_URI_PATTERN_MANY)//
                                                .add(Account.class, SubType.ITEM, "#", AccountContract.CONTENT_URI_PATTERN_ONE)//
                                        ;
                                    }
                                })

                                .addFragment(new UnderTestSampleFragment() {
                                    @Override
                                    public String getKeyName() {
                                        return "UnderTestSampleFragment2";
                                    }

                                    @Override
                                    protected void onAppendMatcherPatterns(MatcherController matcherController) {
                                        matcherController
                                                .add(Membership.class, SubType.DIRECTORY, "", MembershipContract.CONTENT_URI_PATTERN_MANY)//
                                                .add(Membership.class, SubType.ITEM, "#", MembershipContract.CONTENT_URI_PATTERN_ONE)//
                                        ;
                                    }
                                })
                );
                break;

            case 4:
                setMatcherController(new MatcherController(this)//
                        .addFragment(new UnderTestSampleFragment() {
                            @Override
                            public String getKeyName() {
                                return "UnderTestSampleFragment";
                            }

                            @Override
                            public int getFragmentEventHandling() {
                                return FragmentEventHandling.FRAGMENT_AND_DEFAULT_DUPLICATE;
                            }

                            @Override
                            protected void onAppendMatcherPatterns(MatcherController matcherController) {
                                matcherController
                                        .add(Account.class, SubType.DIRECTORY, "", AccountContract.CONTENT_URI_PATTERN_MANY)//
                                        .add(Account.class, SubType.ITEM, "#", AccountContract.CONTENT_URI_PATTERN_ONE)//
                                ;
                            }
                        })
                        .add(Membership.class, SubType.DIRECTORY, "", MembershipContract.CONTENT_URI_PATTERN_MANY)//
                        .add(Membership.class, SubType.ITEM, "#", MembershipContract.CONTENT_URI_PATTERN_ONE)//
                );
                break;
        }
        return true;
    }
}
