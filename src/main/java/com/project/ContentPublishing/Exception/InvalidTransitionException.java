package com.project.ContentPublishing.Exception;

import com.project.ContentPublishing.model.ArticleStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidTransitionException extends RuntimeException {

    private final String from;
    private final String to;

    public InvalidTransitionException(String message) {
        super(message);
        this.from = null;
        this.to = null;
    }

    public InvalidTransitionException(ArticleStatus from, ArticleStatus to) {
        super("Cannot transition from " + from + " to " + to);
        this.from = from.name();
        this.to = to.name();
    }
}