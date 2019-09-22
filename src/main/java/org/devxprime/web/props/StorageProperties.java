package org.devxprime.web.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";
    private long maxRetainTime = 60 * 60 * 1000;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getMaxRetainTime() {
        return maxRetainTime;
    }

    public void setMaxRetainTime(long maxRetainTime) {
        this.maxRetainTime = maxRetainTime;
    }
}
