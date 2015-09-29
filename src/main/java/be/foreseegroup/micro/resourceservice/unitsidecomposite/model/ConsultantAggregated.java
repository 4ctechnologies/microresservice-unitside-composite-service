package be.foreseegroup.micro.resourceservice.unitsidecomposite.model;

/**
 * Created by Kaj on 24/09/15.
 */
public class ConsultantAggregated extends Consultant {
    private Iterable<ContractAggregated> contracts;

    public ConsultantAggregated(Consultant consultant) {
        super(consultant.getId(), consultant.getFirstname(), consultant.getLastname(), consultant.getAddress());
    }

    public Iterable<ContractAggregated> getContracts() {
        return contracts;
    }

    public void setContracts(Iterable<ContractAggregated> contracts) {
        this.contracts = contracts;
    }
}
