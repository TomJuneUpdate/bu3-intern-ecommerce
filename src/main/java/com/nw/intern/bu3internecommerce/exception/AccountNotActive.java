package com.nw.intern.bu3internecommerce.exception;

import org.springframework.security.core.AuthenticationException;

public class AccountNotActive extends AuthenticationException {
    public AccountNotActive(String msg) {
        super(msg);
    }
}
