package org.avmrus.fopds;

public class DownloadRecord {
    private String type;
    private String extension;
    private String displayType;
    private String url;

    public DownloadRecord(String type, String extension, String displayType) {
        super();
        this.type = type;
        this.extension = extension;
        this.displayType = displayType;
    }

    public DownloadRecord(DownloadRecord record) {
        super();
        this.type = record.getType();
        this.extension = record.getExtension();
        this.displayType = record.getDisplayType();
    }

    public String getType() {
        return type;
    }

    public String getExtension() {
        return extension;
    }

    public String getDisplayType() {
        return displayType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
