package com.tojc.ormlite.android.annotation.info;

import java.lang.reflect.AnnotatedElement;

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;

/**
 * Manage the Contract information.
 * @author Jaken
 */
public class ContractInfo extends AnnotationInfoBase {
    private String contractClassName;

    public ContractInfo(AnnotatedElement element) {
        Contract contract = element.getAnnotation(Contract.class);
        if (contract != null) {
            this.contractClassName = contract.contractClassName();
            validFlagOn();
        }
    }

    public ContractInfo(String contractClassName) {
        this.contractClassName = contractClassName;
        validFlagOn();
    }

    public String getContractClassName() {
        return this.contractClassName;
    }

    @Override
    protected boolean isValidValue() {
        return true;
    }
}
