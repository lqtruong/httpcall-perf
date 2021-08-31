package com.turong.training.httpcall.controller;

public interface WebValidator<WebRequest> {

    boolean validate(final WebRequest request);

}
