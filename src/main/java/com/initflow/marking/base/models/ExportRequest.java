package com.initflow.marking.base.models;

import java.util.List;

public class ExportRequest<SR> {
    SR searchRequest;
    int userCount;

    SortingProperties sortingProperties;

    List<String> emails;

    public SR getSearchRequest() {
        return searchRequest;
    }

    public void setSearchRequest(SR searchRequest) {
        this.searchRequest = searchRequest;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public SortingProperties getSortingProperties() {
        return sortingProperties;
    }

    public void setSortingProperties(SortingProperties sortingProperties) {
        this.sortingProperties = sortingProperties;
    }
}
