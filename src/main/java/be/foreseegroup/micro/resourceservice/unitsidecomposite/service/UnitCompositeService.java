package be.foreseegroup.micro.resourceservice.unitsidecomposite.service;

import be.foreseegroup.micro.resourceservice.unitsidecomposite.model.Unit;
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
public class UnitCompositeService {
    private static final Logger LOG = LoggerFactory.getLogger(UnitCompositeService.class);

    @Autowired
    UnitCompositeIntegration unitIntegration;

    @RequestMapping(method = RequestMethod.GET, value="/units")
    public ResponseEntity<Iterable<Unit>> getAllUnits() {
        return unitIntegration.getAllUnits();
    }

    @RequestMapping(method = RequestMethod.GET, value="/units/{unitId}")
    public ResponseEntity<Unit> getUnitById(@PathVariable String unitId) {
        return unitIntegration.getUnitById(unitId);
    }

    @RequestMapping(method = RequestMethod.POST, value="/units")
    public ResponseEntity<Unit> createUnit(@RequestBody Unit unit) {
        return unitIntegration.createUnit(unit);
    }

    @RequestMapping(method = RequestMethod.PUT, value="/units/{unitId}")
    public ResponseEntity<Unit> updateUnit(@PathVariable String unitId, @RequestBody Unit unit, @RequestHeader HttpHeaders headers) {
        return unitIntegration.updateUnit(unitId, unit, headers);
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/units/{unitId}")
    public ResponseEntity<Unit> deleteUnit(@PathVariable String unitId, @RequestHeader HttpHeaders headers) {
        return unitIntegration.deleteUnit(unitId, headers);
    }
}
