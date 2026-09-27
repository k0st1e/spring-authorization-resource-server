package dev.kostie.oauth2authorizationserver.api.model;

public class ErrorDetails {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
}
