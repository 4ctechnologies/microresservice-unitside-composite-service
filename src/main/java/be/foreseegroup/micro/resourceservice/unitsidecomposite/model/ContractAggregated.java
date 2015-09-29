package be.foreseegroup.micro.resourceservice.unitsidecomposite.model;

/**
 * Created by Kaj on 28/09/15.
 */
public class ContractAggregated extends Contract {
    private String unitName;
    private String consultantFirstName;
    private String consultantLastName;

    public ContractAggregated(Contract contract) {
        super(contract.getId(), contract.getUnitId(), contract.getConsultantId(), contract.getStartDate(), contract.getEndDate(), contract.getType());
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getConsultantFirstName() {
        return consultantFirstName;
    }

    public void setConsultantFirstName(String consultantFirstName) {
        this.consultantFirstName = consultantFirstName;
    }

    public String getConsultantLastName() {
        return consultantLastName;
    }

    public void setConsultantLastName(String consultantLastName) {
        this.consultantLastName = consultantLastName;
    }
}
