package in.cfcomputing.odin.core.lb;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by gibugeorge on 28/03/2017.
 */
@ConfigurationProperties(prefix = "load-balancer-client")
public class LoadBalancingClientProperties {

    private Integer readTimeout = 5000;
    private Integer connectionTimeout = 2000;
    private Integer maxRetries = 0;
    private Integer maxRetriesNextServer = 0;

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Integer getMaxRetriesNextServer() {
        return maxRetriesNextServer;
    }

    public void setMaxRetriesNextServer(Integer maxRetriesNextServer) {
        this.maxRetriesNextServer = maxRetriesNextServer;
    }
}
