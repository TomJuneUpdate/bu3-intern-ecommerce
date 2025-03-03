package com.nw.intern.bu3internecommerce.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String errorCode;
    private String path;
}
