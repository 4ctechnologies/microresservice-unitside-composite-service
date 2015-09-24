package be.foreseegroup.micro.resourceservice.unitsidecomposite.model;

/**
 * Created by Kaj on 24/09/15.
 */
public class ConsultantAggregated extends Consultant {
    private Unit unit;
    private Contract contract;

    public ConsultantAggregated(Unit unit, Contract contract) {
        this.unit = unit;
        this.contract = contract;
    }

    public ConsultantAggregated(String id, String firstname, String lastname, String address, Unit unit, Contract contract) {
        super(id, firstname, lastname, address);
        this.unit = unit;
        this.contract = contract;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }
}
