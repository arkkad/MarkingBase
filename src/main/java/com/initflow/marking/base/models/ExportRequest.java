package com.initflow.marking.base.models;

public class ExportRequest<SR> {
    SR searchRequest;
    int userCount;

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
}
