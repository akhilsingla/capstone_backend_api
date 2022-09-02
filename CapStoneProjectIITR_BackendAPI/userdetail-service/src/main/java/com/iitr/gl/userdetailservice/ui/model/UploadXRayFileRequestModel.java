package com.iitr.gl.userdetailservice.ui.model;

public class UploadXRayFileRequestModel {
    private String fileData;
    private String xrayType;

    private String fileName;

    private String userId;

    private String xrayId;

    private String adminId;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getXrayId() {
        return xrayId;
    }

    public void setXrayId(String xrayId) {
        this.xrayId = xrayId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public String getXrayType() {
        return xrayType;
    }

    public void setXrayType(String xrayType) {
        this.xrayType = xrayType;
    }
}
