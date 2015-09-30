package be.foreseegroup.micro.resourceservice.unitsidecomposite.model;

/**
 * Created by Kaj on 28/09/15.
 */
public class ContractAggregated extends Contract {
    private Unit unit;
    private Consultant consultant;

    //private String unitName;
    //private String consultantFirstName;
    //private String consultantLastName;

    public ContractAggregated(Contract contract) {
        super(contract.getId(), contract.getUnitId(), contract.getConsultantId(), contract.getStartDate(), contract.getEndDate(), contract.getType());
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Consultant getConsultant() {
        return consultant;
    }

    public void setConsultant(Consultant consultant) {
        this.consultant = consultant;
    }
    /*
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
    */
}
