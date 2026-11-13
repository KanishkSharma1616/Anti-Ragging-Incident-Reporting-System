package com.antiragging.model;

import java.sql.Timestamp;

/**
 * Incident Update History Entity Model.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class IncidentUpdate {
    private int updateId;
    private int incidentId;
    private int updatedBy;
    private String updatedByName;
    private String oldStatus;
    private String newStatus;
    private String updateNotes;
    private String actionTaken;
    private Timestamp updatedAt;

    public IncidentUpdate() {}

    public int getUpdateId() { return updateId; }
    public void setUpdateId(int updateId) { this.updateId = updateId; }

    public int getIncidentId() { return incidentId; }
    public void setIncidentId(int incidentId) { this.incidentId = incidentId; }

    public int getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(int updatedBy) { this.updatedBy = updatedBy; }

    public String getUpdatedByName() { return updatedByName; }
    public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }

    public String getOldStatus() { return oldStatus; }
    public void setOldStatus(String oldStatus) { this.oldStatus = oldStatus; }

    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }

    public String getUpdateNotes() { return updateNotes; }
    public void setUpdateNotes(String updateNotes) { this.updateNotes = updateNotes; }

    public String getActionTaken() { return actionTaken; }
    public void setActionTaken(String actionTaken) { this.actionTaken = actionTaken; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
