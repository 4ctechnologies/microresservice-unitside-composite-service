package be.foreseegroup.micro.resourceservice.unitsidecomposite.service;

import be.foreseegroup.micro.resourceservice.unitsidecomposite.model.Contract;
import be.foreseegroup.micro.resourceservice.unitsidecomposite.model.Unit;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by Kaj on 24/09/15.
 */
public class UnitCompositeIntegration {
    private static final Logger LOG = LoggerFactory.getLogger(UnitCompositeIntegration.class);

    @Autowired
    private ServiceUtils util;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * @todo:
     * Hystrix is also using the fallbackMethod when the person resource returns (intended) 4xx errors, e.g. 404 not found
     */


    @HystrixCommand(fallbackMethod = "unitsFallback")
    public ResponseEntity<Iterable<Unit>> getAllUnits() {
        LOG.debug("Will call getAllUnits with Hystrix protection");

        URI uri = util.getServiceUrl("unit");
        String url = uri.toString() + "/units";
        LOG.debug("getAllUnits from URL: {}", url);

        ParameterizedTypeReference<Iterable<Unit>> responseType = new ParameterizedTypeReference<Iterable<Unit>>() {};
        ResponseEntity<Iterable<Unit>> units = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        return units;
    }

    @HystrixCommand(fallbackMethod = "unitFallback")
    public ResponseEntity<Unit> getUnitById(String unitId) {
        LOG.debug("Will call getUnitById with Hystrix protection");

        URI uri = util.getServiceUrl("unit");
        String url = uri.toString() + "/units/"+unitId;
        LOG.debug("getUnitById from URL: {}", url);

        ResponseEntity<Unit> unit = restTemplate.getForEntity(url, Unit.class);
        return unit;
    }

    @HystrixCommand(fallbackMethod = "unitFallback")
    public ResponseEntity<Unit> createUnit(Unit unit) {
        LOG.debug("Will call createUnit with Hystrix protection");

        URI uri = util.getServiceUrl("unit");
        String url = uri.toString() + "/units";
        LOG.debug("createUnit from URL: {}", url);

        ResponseEntity<Unit> resultUnit = restTemplate.postForEntity(url, unit, Unit.class);
        return resultUnit;
    }

    @HystrixCommand(fallbackMethod = "unitFallback")
    public ResponseEntity<Unit> updateUnit(String unitId, Unit unit, HttpHeaders headers) {
        LOG.debug("Will call updateUnit with Hystrix protection");

        URI uri = util.getServiceUrl("unit");
        String url = uri.toString() + "/units/"+unitId;
        LOG.debug("updateUnit from URL: {}", url);

        HttpEntity<Unit> requestEntity = new HttpEntity<>(unit, headers);
        ResponseEntity<Unit> resultUnit = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Unit.class);
        return resultUnit;
    }

    @HystrixCommand(fallbackMethod = "unitFallback")
    public ResponseEntity<Unit> deleteUnit(String unitId, HttpHeaders headers) {
        LOG.debug("Will call deleteUnit with Hystrix protection");

        URI uri = util.getServiceUrl("unit");
        String url = uri.toString() + "/units/"+unitId;
        LOG.debug("deleteUnits from URL: {}", url);

        HttpEntity<Unit> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Unit> resultUnit = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Unit.class);
        return resultUnit;
    }

    public ResponseEntity<Unit> unitFallback(String unitId) {
        return unitFallback();
    }
    public ResponseEntity<Unit> unitFallback(Unit unit) {
        return unitFallback();
    }
    public ResponseEntity<Unit> unitFallback(String unitId, Unit unit, HttpHeaders headers) {
        return unitFallback();
    }
    public ResponseEntity<Unit> unitFallback(String unitId, HttpHeaders headers) {
        return unitFallback();
    }
    public ResponseEntity<Unit> unitFallback() {
        LOG.warn("Using fallback method for unit-service");
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<Iterable<Unit>> unitsFallback() {
        LOG.warn("Using fallback method for unit-service");
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

}
