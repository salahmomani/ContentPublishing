package com.project.ContentPublishing.model;

import com.project.ContentPublishing.Exception.InvalidTransitionException;

public enum ArticleStatus {
    DRAFT,
    PUBLISHED,
    ARCHIVED,
    UNDER_REVIEW,
    REJECTED;

    public ArticleStatus transitionTo(ArticleStatus next) {
        boolean allowed = switch (this) {
            case DRAFT       -> next == UNDER_REVIEW;
            case UNDER_REVIEW -> next == PUBLISHED || next == REJECTED;
            case PUBLISHED   -> next == ARCHIVED;
            case REJECTED    -> next == DRAFT;
            case ARCHIVED    -> false;
        };

        if (!allowed) {
            throw new InvalidTransitionException(
                    "Cannot transition from " + this + " to " + next
            );
        }

        return next;
    }
}
