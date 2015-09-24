package be.foreseegroup.micro.resourceservice.unitsidecomposite.service;

import be.foreseegroup.micro.resourceservice.unitsidecomposite.model.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Kaj on 24/09/15.
 */

@RestController
public class ContractCompositeService {
    private static final Logger LOG = LoggerFactory.getLogger(ContractCompositeService.class);

    @Autowired
    ContractCompositeIntegration contractIntegration;

    @RequestMapping(method = RequestMethod.GET, value="/contracts")
    public ResponseEntity<Iterable<Contract>> getAllContracts() {
        return contractIntegration.getAllContracts();
    }

    @RequestMapping(method = RequestMethod.GET, value="/contracts/{contractId}")
    public ResponseEntity<Contract> getContractById(@PathVariable String contractId) {
        return contractIntegration.getContractById(contractId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        return contractIntegration.createContract(contract);
    }

    @RequestMapping(method = RequestMethod.PUT, value="/contracts/{contractId}")
    public ResponseEntity<Contract> updateContract(@PathVariable String contractId, @RequestBody Contract contract, @RequestHeader HttpHeaders headers) {
        return contractIntegration.updateContract(contractId, contract, headers);
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/contracts/{contractId}")
    public ResponseEntity<Contract> deleteContract(@PathVariable String contractId, @RequestHeader HttpHeaders headers) {
        return contractIntegration.deleteContract(contractId, headers);
    }
}
