package be.foreseegroup.micro.resourceservice.unitsidecomposite.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import java.net.URI;

/**
 * Created by Kaj on 24/09/15.
 */
public class ServiceUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceUtils.class);

    @Autowired
    private LoadBalancerClient loadBalancer;

    public URI getServiceUrl(String serviceId) {
        return getServiceUrl(serviceId, null);
    }

    public URI getServiceUrl(String serviceId, String fallbackUri) {
        URI uri = null;
        try {
            ServiceInstance instance = loadBalancer.choose(serviceId);

            if (instance == null) {
                throw new RuntimeException("Can't find a service with serviceId = " + serviceId);
            }

            uri = instance.getUri();
            LOG.debug("Resolved serviceId '{}' to URL '{}'.", serviceId, uri);

        } catch (RuntimeException e) {
            // Eureka not available, use fallback if specified otherwise rethrow the error
            if (fallbackUri == null) {
                throw e;
            } else {
                uri = URI.create(fallbackUri);
                LOG.warn("Failed to resolve serviceId '{}'. Fallback to URL '{}'.", serviceId, uri);
            }
        }
        return uri;
    }

}
