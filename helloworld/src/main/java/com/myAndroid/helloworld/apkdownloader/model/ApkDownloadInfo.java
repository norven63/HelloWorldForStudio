package com.myAndroid.helloworld.apkdownloader.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.
/**
 * Entity mapped to table "APK_DOWNLOAD_INFO".
 */
public class ApkDownloadInfo {

    private Long id;
    int taskId;
    Integer progress;
    Integer statu;
    Long total;
    String label;
    String url;
    String fileName;
    String filePath;
    String pkgName;

    public ApkDownloadInfo() {
    }

    public ApkDownloadInfo(Long id) {
        this.id = id;
    }

    public ApkDownloadInfo(Long id, int taskId, Integer progress, Integer statu, Long total, String label, String url,
            String fileName, String filePath, String pkgName) {
        this.id = id;
        this.taskId = taskId;
        this.progress = progress;
        this.statu = statu;
        this.total = total;
        this.label = label;
        this.url = url;
        this.fileName = fileName;
        this.filePath = filePath;
        this.pkgName = pkgName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + taskId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ApkDownloadInfo other = (ApkDownloadInfo) obj;
        if (taskId != other.taskId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ApkDownloadInfo [taskId=" + taskId + ", progress=" + progress + ", statu=" + statu + ", total=" + total
                + ", label=" + label + ", url=" + url + ", fileName=" + fileName + ", filePath=" + filePath
                + ", pkgName=" + pkgName + "]";
    }
}
