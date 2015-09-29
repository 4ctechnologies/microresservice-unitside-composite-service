package be.foreseegroup.micro.resourceservice.unitsidecomposite.service;

import be.foreseegroup.micro.resourceservice.unitsidecomposite.model.Consultant;
import be.foreseegroup.micro.resourceservice.unitsidecomposite.model.ConsultantAggregated;
import be.foreseegroup.micro.resourceservice.unitsidecomposite.model.ContractAggregated;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Kaj on 24/09/15.
 */
@Component
public class ConsultantCompositeIntegration {
    private static final Logger LOG = LoggerFactory.getLogger(ConsultantCompositeIntegration.class);

   @Autowired
    private ServiceUtils util;

    @Autowired
    ContractCompositeIntegration contractIntegration;

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private RestTemplate restTemplate = new RestTemplate();

    /**
     * @todo:
     * Hystrix is also using the fallbackMethod when the person resource returns (intended) 4xx errors, e.g. 404 not found
     */

    public String getMessage() {
        String result;
        String httpResult = restTemplate.getForObject("http://www.google.com",
                String.class);
        result = "Message SUCCESS result: " + httpResult;
        System.out.println(result);
        return result;
    }

    @HystrixCommand(fallbackMethod = "aggregatedConsultantsFallback")
    public ResponseEntity<Iterable<ConsultantAggregated>> getAllAggregatedConsultants() {
        LOG.debug("Will call getAllAggregatedConsultants with Hystrix protection");

        URI uri = util.getServiceUrl("consultant");
        String url = uri.toString() + "/consultants";
        LOG.debug("getAllAggregatedConsultants from URL: {}", url);

        ParameterizedTypeReference<Iterable<Consultant>> responseType = new ParameterizedTypeReference<Iterable<Consultant>>() {};
        ResponseEntity<Iterable<Consultant>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        Iterable<Consultant> consultants = response.getBody();

        ArrayList<ConsultantAggregated> aggregatedConsultants = new ArrayList<ConsultantAggregated>();

        for (Consultant consultant : consultants) {
            ConsultantAggregated aggregatedConsultant = new ConsultantAggregated(consultant);

            String consultantId = consultant.getId();

            ResponseEntity<Iterable<ContractAggregated>> contractResponse = contractIntegration.getAllAggregatedContractsByConsultantId(consultantId);
            if (contractResponse.getStatusCode() == HttpStatus.OK) {
                Iterable<ContractAggregated> contracts = contractResponse.getBody();

                aggregatedConsultant.setContracts(contracts);

            }

            aggregatedConsultants.add(aggregatedConsultant);
        }

        return new ResponseEntity<Iterable<ConsultantAggregated>>(aggregatedConsultants, response.getHeaders(), response.getStatusCode());
    }

    @HystrixCommand(fallbackMethod = "consultantsFallback")
    public ResponseEntity<Iterable<Consultant>> getAllConsultants() {
        LOG.debug("Will call getAllConsultants with Hystrix protection");

        URI uri = util.getServiceUrl("consultant");
        String url = uri.toString() + "/consultants";
        LOG.debug("getAllConsultants from URL: {}", url);

        ParameterizedTypeReference<Iterable<Consultant>> responseType = new ParameterizedTypeReference<Iterable<Consultant>>() {};
        ResponseEntity<Iterable<Consultant>> consultants = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        return consultants;
    }

    @HystrixCommand(fallbackMethod = "aggregatedConsultantFallback")
    public ResponseEntity<ConsultantAggregated> getAggregatedConsultantById(String consultantId) {
        LOG.debug("Will call getAggregatedConsultantById with Hystrix protection");

        URI uri = util.getServiceUrl("consultant");
        String url = uri.toString() + "/consultants/"+consultantId;
        LOG.debug("getAggregatedConsultantById from URL: {}", url);

        ResponseEntity<Consultant> response = restTemplate.getForEntity(url, Consultant.class);

        Consultant consultant = response.getBody();
        ConsultantAggregated aggregatedConsultant = new ConsultantAggregated(consultant);


        ResponseEntity<Iterable<ContractAggregated>> contractResponse = contractIntegration.getAllAggregatedContractsByConsultantId(consultantId);
        if (contractResponse.getStatusCode() == HttpStatus.OK) {
            Iterable<ContractAggregated> contracts = contractResponse.getBody();
            aggregatedConsultant.setContracts(contracts);
        }

        return new ResponseEntity<ConsultantAggregated>(aggregatedConsultant, response.getHeaders(), response.getStatusCode());
    }

    @HystrixCommand(fallbackMethod = "consultantFallback")
    public ResponseEntity<Consultant> getConsultantById(String consultantId) {
        LOG.debug("Will call getConsultantById with Hystrix protection");

        URI uri = util.getServiceUrl("consultant");
        String url = uri.toString() + "/consultants/"+consultantId;
        LOG.debug("getConsultantById from URL: {}", url);

        ResponseEntity<Consultant> consultant = restTemplate.getForEntity(url, Consultant.class);
        return consultant;
    }

    @HystrixCommand(fallbackMethod = "consultantFallback")
    public ResponseEntity<Consultant> createConsultant(Consultant consultant) {
        LOG.debug("Will call createConsultant with Hystrix protection");

        URI uri = util.getServiceUrl("consultant");
        String url = uri.toString() + "/consultants";
        LOG.debug("createConsultant from URL: {}", url);

        ResponseEntity<Consultant> resultConsultant = restTemplate.postForEntity(url, consultant, Consultant.class);
        return resultConsultant;
    }

    @HystrixCommand(fallbackMethod = "consultantFallback")
    public ResponseEntity<Consultant> updateConsultant(String consultantId, Consultant consultant, HttpHeaders headers) {
        LOG.debug("Will call updateConsultant with Hystrix protection");

        URI uri = util.getServiceUrl("consultant");
        String url = uri.toString() + "/consultants/"+consultantId;
        LOG.debug("updateConsultant from URL: {}", url);

        HttpEntity<Consultant> requestEntity = new HttpEntity<>(consultant, headers);
        ResponseEntity<Consultant> resultConsultant = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Consultant.class);
        return resultConsultant;
    }

    @HystrixCommand(fallbackMethod = "consultantFallback")
    public ResponseEntity<Consultant> deleteConsultant(String consultantId, HttpHeaders headers) {
        LOG.debug("Will call deleteConsultant with Hystrix protection");

        URI uri = util.getServiceUrl("consultant");
        String url = uri.toString() + "/consultants/"+consultantId;
        LOG.debug("deleteConsultants from URL: {}", url);

        HttpEntity<Consultant> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Consultant> resultConsultant = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Consultant.class);
        return resultConsultant;
    }

    public ResponseEntity<Consultant> consultantFallback(String consultantId) {
        return consultantFallback();
    }
    public ResponseEntity<Consultant> consultantFallback(Consultant consultant) {
        return consultantFallback();
    }
    public ResponseEntity<Consultant> consultantFallback(String consultantId, Consultant consultant, HttpHeaders headers) {
        return consultantFallback();
    }
    public ResponseEntity<Consultant> consultantFallback(String consultantId, HttpHeaders headers) {
        return consultantFallback();
    }
    public ResponseEntity<Consultant> consultantFallback() {
        LOG.warn("Using fallback method for consultant-service");
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<Iterable<Consultant>> consultantsFallback() {
        LOG.warn("Using fallback method for consultant-service");
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<ConsultantAggregated> aggregatedConsultantFallback(String consultantId) {
        return aggregatedConsultantFallback();
    }
    public ResponseEntity<ConsultantAggregated> aggregatedConsultantFallback() {
        LOG.warn("Using fallback method for consultant-service");
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<Iterable<ConsultantAggregated>> aggregatedConsultantsFallback() {
        LOG.warn("Using fallback method for consultant-service");
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

}
