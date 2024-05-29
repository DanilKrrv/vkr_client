package com.example.desktopclient.model;

public class Attestation {
    private Long id;
    private String title;
    private Boolean isValid;
    private String dataPassed;
    private Long userId; // Changed from UserEntity to Long to avoid cyclic dependencies

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public String getDataPassed() {
        return dataPassed;
    }

    public void setDataPassed(String dataPassed) {
        this.dataPassed = dataPassed;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
