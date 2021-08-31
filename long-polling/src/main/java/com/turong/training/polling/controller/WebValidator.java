package com.turong.training.polling.controller;

public interface WebValidator<WebRequest> {

    boolean validate(final WebRequest request);

}
