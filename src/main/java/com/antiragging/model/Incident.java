package com.antiragging.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Incident Entity Model representing reported ragging incidents.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class Incident {
    private int incidentId;
    private int studentUserId;
    private String studentName;
    private String studentId;
    private String department;
    private String semester;
    private String contactNumber;
    private String email;
    private Date incidentDate;
    private String incidentTime;
    private String incidentLocation;
    private String raggingType;
    private String description;
    private String personsInvolved;
    private String witnessDetails;
    private String evidenceDescription;
    private String immediateHelpRequired; // Yes/No
    private String status; // Pending, Under Investigation, Resolved, Rejected, Closed
    private String assignedOfficer;
    private String investigationNotes;
    private String actionTaken;
    private String resolutionDetails;
    private Timestamp submissionDate;
    private Timestamp lastUpdated;

    public Incident() {}

    // Getters and Setters
    public int getIncidentId() { return incidentId; }
    public void setIncidentId(int incidentId) { this.incidentId = incidentId; }

    public int getStudentUserId() { return studentUserId; }
    public void setStudentUserId(int studentUserId) { this.studentUserId = studentUserId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getIncidentDate() { return incidentDate; }
    public void setIncidentDate(Date incidentDate) { this.incidentDate = incidentDate; }

    public String getIncidentTime() { return incidentTime; }
    public void setIncidentTime(String incidentTime) { this.incidentTime = incidentTime; }

    public String getIncidentLocation() { return incidentLocation; }
    public void setIncidentLocation(String incidentLocation) { this.incidentLocation = incidentLocation; }

    public String getRaggingType() { return raggingType; }
    public void setRaggingType(String raggingType) { this.raggingType = raggingType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPersonsInvolved() { return personsInvolved; }
    public void setPersonsInvolved(String personsInvolved) { this.personsInvolved = personsInvolved; }

    public String getWitnessDetails() { return witnessDetails; }
    public void setWitnessDetails(String witnessDetails) { this.witnessDetails = witnessDetails; }

    public String getEvidenceDescription() { return evidenceDescription; }
    public void setEvidenceDescription(String evidenceDescription) { this.evidenceDescription = evidenceDescription; }

    public String getImmediateHelpRequired() { return immediateHelpRequired; }
    public void setImmediateHelpRequired(String immediateHelpRequired) { this.immediateHelpRequired = immediateHelpRequired; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAssignedOfficer() { return assignedOfficer; }
    public void setAssignedOfficer(String assignedOfficer) { this.assignedOfficer = assignedOfficer; }

    public String getInvestigationNotes() { return investigationNotes; }
    public void setInvestigationNotes(String investigationNotes) { this.investigationNotes = investigationNotes; }

    public String getActionTaken() { return actionTaken; }
    public void setActionTaken(String actionTaken) { this.actionTaken = actionTaken; }

    public String getResolutionDetails() { return resolutionDetails; }
    public void setResolutionDetails(String resolutionDetails) { this.resolutionDetails = resolutionDetails; }

    public Timestamp getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(Timestamp submissionDate) { this.submissionDate = submissionDate; }

    public Timestamp getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Timestamp lastUpdated) { this.lastUpdated = lastUpdated; }
}
