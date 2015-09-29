package be.foreseegroup.micro.resourceservice.unitsidecomposite.service;

import be.foreseegroup.micro.resourceservice.unitsidecomposite.model.Consultant;
import be.foreseegroup.micro.resourceservice.unitsidecomposite.model.Contract;
import be.foreseegroup.micro.resourceservice.unitsidecomposite.model.ContractAggregated;
import be.foreseegroup.micro.resourceservice.unitsidecomposite.model.Unit;
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
public class ContractCompositeIntegration {
    private static final Logger LOG = LoggerFactory.getLogger(ContractCompositeIntegration.class);

    @Autowired
    private ServiceUtils util;

    @Autowired
    ConsultantCompositeIntegration consultantIntegration;

    @Autowired
    UnitCompositeIntegration unitIntegration;



    private RestTemplate restTemplate = new RestTemplate();

    /**
     * @todo:
     * Hystrix is also using the fallbackMethod when the person resource returns (intended) 4xx errors, e.g. 404 not found
     */

    @HystrixCommand(fallbackMethod = "aggregatedContractsFallback")
    public ResponseEntity<Iterable<ContractAggregated>> getAllAggregatedContracts() {
        LOG.debug("Will call getAllAggregatedContracts with Hystrix protection");

        URI uri = util.getServiceUrl("contract");
        String url = uri.toString() + "/contracts";
        LOG.debug("getAllAggregatedContracts from URL: {}", url);

        ParameterizedTypeReference<Iterable<Contract>> responseType = new ParameterizedTypeReference<Iterable<Contract>>() {};
        ResponseEntity<Iterable<Contract>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        Iterable<Contract> contracts = response.getBody();

        ArrayList<ContractAggregated> aggregatedContracts = new ArrayList<ContractAggregated>();

        for (Contract contract : contracts) {
            ContractAggregated aggregatedContract = new ContractAggregated(contract);

            String unitId = contract.getUnitId();
            String consultantId = contract.getConsultantId();

            ResponseEntity<Unit> unitResponse = unitIntegration.getUnitById(unitId);
            if (unitResponse.getStatusCode() == HttpStatus.OK) {
                Unit unit = unitResponse.getBody();
                aggregatedContract.setUnitName(unit.getName());
            }

            ResponseEntity<Consultant> consultantResponse = consultantIntegration.getConsultantById(consultantId);
            if (consultantResponse.getStatusCode() == HttpStatus.OK) {
                Consultant consultant = consultantResponse.getBody();
                aggregatedContract.setConsultantFirstName(consultant.getFirstname());
                aggregatedContract.setConsultantLastName(consultant.getLastname());
            }

            aggregatedContracts.add(aggregatedContract);
        }

        return new ResponseEntity<Iterable<ContractAggregated>>(aggregatedContracts, response.getHeaders(), response.getStatusCode());
    }


    @HystrixCommand(fallbackMethod = "contractsFallback")
    public ResponseEntity<Iterable<Contract>> getAllContracts() {
        LOG.debug("Will call getAllContracts with Hystrix protection");

        URI uri = util.getServiceUrl("contract");
        String url = uri.toString() + "/contracts";
        LOG.debug("getAllContracts from URL: {}", url);

        ParameterizedTypeReference<Iterable<Contract>> responseType = new ParameterizedTypeReference<Iterable<Contract>>() {};
        ResponseEntity<Iterable<Contract>> contracts = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        return contracts;
    }

    @HystrixCommand(fallbackMethod = "aggregatedContractFallback")
    public ResponseEntity<ContractAggregated> getAggregatedContractById(String contractId) {
        LOG.debug("Will call getAggregatedContractById with Hystrix protection");

        URI uri = util.getServiceUrl("contract");
        String url = uri.toString() + "/contracts/"+contractId;
        LOG.debug("getAggregatedContractById from URL: {}", url);

        ResponseEntity<Contract> response = restTemplate.getForEntity(url, Contract.class);

        Contract contract = response.getBody();
        ContractAggregated aggregatedContract = new ContractAggregated(contract);


        String unitId = contract.getUnitId();
        String consultantId = contract.getConsultantId();

        ResponseEntity<Unit> unitResponse = unitIntegration.getUnitById(unitId);
        if (unitResponse.getStatusCode() == HttpStatus.OK) {
            Unit unit = unitResponse.getBody();
            aggregatedContract.setUnitName(unit.getName());
        }

        ResponseEntity<Consultant> consultantResponse = consultantIntegration.getConsultantById(consultantId);
        if (consultantResponse.getStatusCode() == HttpStatus.OK) {
            Consultant consultant = consultantResponse.getBody();
            aggregatedContract.setConsultantFirstName(consultant.getFirstname());
            aggregatedContract.setConsultantLastName(consultant.getLastname());
        }

        return new ResponseEntity<ContractAggregated>(aggregatedContract, response.getHeaders(), response.getStatusCode());
    }

    @HystrixCommand(fallbackMethod = "contractFallback")
    public ResponseEntity<Contract> getContractById(String contractId) {
        LOG.debug("Will call getContractById with Hystrix protection");

        URI uri = util.getServiceUrl("contract");
        String url = uri.toString() + "/contracts/"+contractId;
        LOG.debug("getContractById from URL: {}", url);

        ResponseEntity<Contract> contract = restTemplate.getForEntity(url, Contract.class);
        return contract;
    }

    @HystrixCommand(fallbackMethod = "contractFallback")
    public ResponseEntity<Contract> createContract(Contract contract) {
        LOG.debug("Will call createContract with Hystrix protection");

        URI uri = util.getServiceUrl("contract");
        String url = uri.toString() + "/contracts";
        LOG.debug("createContract from URL: {}", url);

        ResponseEntity<Contract> resultContract = restTemplate.postForEntity(url, contract, Contract.class);
        return resultContract;
    }

    @HystrixCommand(fallbackMethod = "contractFallback")
    public ResponseEntity<Contract> updateContract(String contractId, Contract contract, HttpHeaders headers) {
        LOG.debug("Will call updateContract with Hystrix protection");

        URI uri = util.getServiceUrl("contract");
        String url = uri.toString() + "/contracts/"+contractId;
        LOG.debug("updateContract from URL: {}", url);

        HttpEntity<Contract> requestEntity = new HttpEntity<>(contract, headers);
        ResponseEntity<Contract> resultContract = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Contract.class);
        return resultContract;
    }

    @HystrixCommand(fallbackMethod = "contractFallback")
    public ResponseEntity<Contract> deleteContract(String contractId, HttpHeaders headers) {
        LOG.debug("Will call deleteContract with Hystrix protection");

        URI uri = util.getServiceUrl("contract");
        String url = uri.toString() + "/contracts/"+contractId;
        LOG.debug("deleteContracts from URL: {}", url);

        HttpEntity<Contract> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Contract> resultContract = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Contract.class);
        return resultContract;
    }

    @HystrixCommand(fallbackMethod = "aggregatedContractsFallback")
    public ResponseEntity<Iterable<ContractAggregated>> getAllAggregatedContractsByUnitId(String unitId) {
        LOG.debug("Will call getAllAggregatedContractsByUnitId with Hystrix protection");

        URI uri = util.getServiceUrl("contract");
        String url = uri.toString() + "/contractsbyuid/"+unitId;
        LOG.debug("getAllAggregatedContractsByUnitId from URL: {}", url);

        ParameterizedTypeReference<Iterable<Contract>> responseType = new ParameterizedTypeReference<Iterable<Contract>>() {};
        ResponseEntity<Iterable<Contract>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        Iterable<Contract> contracts = response.getBody();

        ArrayList<ContractAggregated> aggregatedContracts = new ArrayList<ContractAggregated>();

        for (Contract contract : contracts) {
            ContractAggregated aggregatedContract = new ContractAggregated(contract);

            String unitIdentifier = contract.getUnitId();
            String consultantId = contract.getConsultantId();

            ResponseEntity<Unit> unitResponse = unitIntegration.getUnitById(unitIdentifier);
            if (unitResponse.getStatusCode() == HttpStatus.OK) {
                Unit unit = unitResponse.getBody();
                aggregatedContract.setUnitName(unit.getName());
            }

            ResponseEntity<Consultant> consultantResponse = consultantIntegration.getConsultantById(consultantId);
            if (consultantResponse.getStatusCode() == HttpStatus.OK) {
                Consultant consultant = consultantResponse.getBody();
                aggregatedContract.setConsultantFirstName(consultant.getFirstname());
                aggregatedContract.setConsultantLastName(consultant.getLastname());
            }

            aggregatedContracts.add(aggregatedContract);
        }

        return new ResponseEntity<Iterable<ContractAggregated>>(aggregatedContracts, response.getHeaders(), response.getStatusCode());
    }


    @HystrixCommand(fallbackMethod = "contractsFallback")
    public ResponseEntity<Iterable<Contract>> getContractsByUnitId(String unitId) {
        LOG.debug("Will call getContractsByUnitId with Hystrix protection");

        URI uri = util.getServiceUrl("contract");
        String url = uri.toString() + "/contractsbyuid/"+unitId;
        LOG.debug("getContractsByUnitId from URL: {}", url);

        ParameterizedTypeReference<Iterable<Contract>> responseType = new ParameterizedTypeReference<Iterable<Contract>>() {};
        ResponseEntity<Iterable<Contract>> contracts = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        return contracts;
    }

    @HystrixCommand(fallbackMethod = "aggregatedContractsFallback")
    public ResponseEntity<Iterable<ContractAggregated>> getAllAggregatedContractsByConsultantId(String consultantId) {
        LOG.debug("Will call getAllAggregatedContractsByConsultantId with Hystrix protection");

        URI uri = util.getServiceUrl("contract");
        String url = uri.toString() + "/contractsbycid/"+consultantId;
        LOG.debug("getAllAggregatedContractsByConsultantId from URL: {}", url);

        ParameterizedTypeReference<Iterable<Contract>> responseType = new ParameterizedTypeReference<Iterable<Contract>>() {};
        ResponseEntity<Iterable<Contract>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        Iterable<Contract> contracts = response.getBody();

        ArrayList<ContractAggregated> aggregatedContracts = new ArrayList<ContractAggregated>();

        for (Contract contract : contracts) {
            ContractAggregated aggregatedContract = new ContractAggregated(contract);

            String unitId = contract.getUnitId();
            String consultantIdentifier = contract.getConsultantId();

            ResponseEntity<Unit> unitResponse = unitIntegration.getUnitById(unitId);
            if (unitResponse.getStatusCode() == HttpStatus.OK) {
                Unit unit = unitResponse.getBody();
                aggregatedContract.setUnitName(unit.getName());
            }

            ResponseEntity<Consultant> consultantResponse = consultantIntegration.getConsultantById(consultantIdentifier);
            if (consultantResponse.getStatusCode() == HttpStatus.OK) {
                Consultant consultant = consultantResponse.getBody();
                aggregatedContract.setConsultantFirstName(consultant.getFirstname());
                aggregatedContract.setConsultantLastName(consultant.getLastname());
            }

            aggregatedContracts.add(aggregatedContract);
        }

        return new ResponseEntity<Iterable<ContractAggregated>>(aggregatedContracts, response.getHeaders(), response.getStatusCode());
    }

    @HystrixCommand(fallbackMethod = "contractsFallback")
    public ResponseEntity<Iterable<Contract>> getContractsByConsultantId(String consultantId) {
        LOG.debug("Will call getContractsByConsultantId with Hystrix protection");

        URI uri = util.getServiceUrl("contract");
        String url = uri.toString() + "/contractsbycid/"+consultantId;
        LOG.debug("getContractsByConsultantId from URL: {}", url);

        ParameterizedTypeReference<Iterable<Contract>> responseType = new ParameterizedTypeReference<Iterable<Contract>>() {};
        ResponseEntity<Iterable<Contract>> contracts = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        return contracts;
    }

    public ResponseEntity<Contract> contractFallback(String contractId) {
        return contractFallback();
    }
    public ResponseEntity<Contract> contractFallback(Contract contract) {
        return contractFallback();
    }
    public ResponseEntity<Contract> contractFallback(String contractId, Contract contract, HttpHeaders headers) {
        return contractFallback();
    }
    public ResponseEntity<Contract> contractFallback(String contractId, HttpHeaders headers) {
        return contractFallback();
    }
    public ResponseEntity<Contract> contractFallback() {
        LOG.warn("Using fallback method for contract-service");
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<Iterable<Contract>> contractsFallBack(String consultantOrUnitId) {
        return contractsFallback();
    }

    public ResponseEntity<Iterable<Contract>> contractsFallback() {
        LOG.warn("Using fallback method for contract-service");
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }



    public ResponseEntity<ContractAggregated> aggregatedContractFallback(String contractId) {
        return aggregatedContractFallback();
    }
    public ResponseEntity<ContractAggregated> aggregatedContractFallback() {
        LOG.warn("Using fallback method for contract-service");
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<Iterable<ContractAggregated>> aggregatedContractsFallback(String consultantOrUnitId) {
        return aggregatedContractsFallback();
    }

    public ResponseEntity<Iterable<ContractAggregated>> aggregatedContractsFallback() {
        LOG.warn("Using fallback method for contract-service");
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

}
