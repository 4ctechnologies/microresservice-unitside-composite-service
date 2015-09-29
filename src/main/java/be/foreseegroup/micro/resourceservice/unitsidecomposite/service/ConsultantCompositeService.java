package be.foreseegroup.micro.resourceservice.unitsidecomposite.service;

import be.foreseegroup.micro.resourceservice.unitsidecomposite.model.Consultant;
import be.foreseegroup.micro.resourceservice.unitsidecomposite.model.ConsultantAggregated;
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
public class ConsultantCompositeService {
    private static final Logger LOG = LoggerFactory.getLogger(ConsultantCompositeService.class);

    @Autowired
    ConsultantCompositeIntegration consultantIntegration;

    @RequestMapping(method = RequestMethod.GET, value="/oconsultants")
    public ResponseEntity<Iterable<Consultant>> getAllConsultants() {
        return consultantIntegration.getAllConsultants();
    }

    @RequestMapping(method = RequestMethod.GET, value="/consultants")
    public ResponseEntity<Iterable<ConsultantAggregated>> getAllAggregatedConsultants() {
        return consultantIntegration.getAllAggregatedConsultants();
    }

    @RequestMapping(method = RequestMethod.GET, value="/oconsultants/{consultantId}")
    public ResponseEntity<Consultant> getConsultantById(@PathVariable String consultantId) {
        return consultantIntegration.getConsultantById(consultantId);
    }

    @RequestMapping(method = RequestMethod.GET, value="/consultants/{consultantId}")
    public ResponseEntity<ConsultantAggregated> getAggregatedConsultantById(@PathVariable String consultantId) {
        return consultantIntegration.getAggregatedConsultantById(consultantId);
    }

    @RequestMapping(method = RequestMethod.POST, value="/consultants")
    public ResponseEntity<Consultant> createConsultant(@RequestBody Consultant consultant) {
        return consultantIntegration.createConsultant(consultant);
    }

    @RequestMapping(method = RequestMethod.PUT, value="/consultants/{consultantId}")
    public ResponseEntity<Consultant> updateConsultant(@PathVariable String consultantId, @RequestBody Consultant consultant, @RequestHeader HttpHeaders headers) {
        return consultantIntegration.updateConsultant(consultantId, consultant, headers);
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/consultants/{consultantId}")
    public ResponseEntity<Consultant> deleteConsultant(@PathVariable String consultantId, @RequestHeader HttpHeaders headers) {
        return consultantIntegration.deleteConsultant(consultantId, headers);
    }
}
