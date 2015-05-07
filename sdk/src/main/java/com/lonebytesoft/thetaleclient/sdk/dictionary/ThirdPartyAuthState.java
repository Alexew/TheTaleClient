package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 03.04.2015
 */
public enum ThirdPartyAuthState {

    NOT_REQUESTED(0, "����������� �� �������������"),
    NOT_DECIDED(1, "������������ ��� �� ������ �������"),
    SUCCESS(2, "����������� ������ �������"),
    REJECT(3, "� ����������� ��������"),
    ;

    public final int code;
    public final String name;

    ThirdPartyAuthState(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
