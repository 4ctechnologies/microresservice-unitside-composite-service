package be.foreseegroup.micro.resourceservice.unitsidecomposite.model;

import java.util.Date;

/**
 * Created by Kaj on 24/09/15.
 */
public class Contract {
    private String id;

    private String unitId;
    private String consultantId;

    private Date startDate;
    private Date endDate;

    private String type;

    public Contract() {
    }

    public Contract(String id, String unitId, String consultantId, Date startDate, Date endDate, String type) {
        this.id = id;
        this.unitId = unitId;
        this.consultantId = consultantId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(String consultantId) {
        this.consultantId = consultantId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
