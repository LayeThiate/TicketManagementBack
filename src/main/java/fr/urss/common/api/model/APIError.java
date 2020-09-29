package fr.urss.common.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Data transfer object that holds details about an API error.
 *
 * @author lucas.david
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIError {

    private int status;
    private String title;
    private String message;
    private String path;

    public APIError() {
    }

    public APIError(int status, String title, String message, String path) {
        this.status = status;
        this.title = title;
        this.message = message;
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
