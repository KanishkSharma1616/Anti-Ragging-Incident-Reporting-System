package com.antiragging.service;

import com.antiragging.dao.IncidentDAO;
import com.antiragging.dao.IncidentUpdateDAO;
import com.antiragging.model.Incident;
import com.antiragging.model.IncidentUpdate;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Service Layer for Incident Operations.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class IncidentService {

    private final IncidentDAO incidentDAO = new IncidentDAO();
    private final IncidentUpdateDAO updateDAO = new IncidentUpdateDAO();

    public boolean createIncident(Incident incident) throws SQLException {
        return incidentDAO.addIncident(incident);
    }

    public List<Incident> getAllIncidents() throws SQLException {
        return incidentDAO.getAllIncidents();
    }

    public List<Incident> getStudentIncidents(int studentUserId) throws SQLException {
        return incidentDAO.getStudentIncidents(studentUserId);
    }

    public Incident getIncidentById(int incidentId) throws SQLException {
        return incidentDAO.getIncidentById(incidentId);
    }

    public boolean updateIncidentStatus(int incidentId, int updatedBy, String oldStatus, String newStatus,
                                        String assignedOfficer, String notes, String actionTaken, String resolutionDetails) throws SQLException {
        boolean updated = incidentDAO.updateIncidentStatusAndDetails(incidentId, newStatus, assignedOfficer, notes, actionTaken, resolutionDetails);
        if (updated) {
            IncidentUpdate updateLog = new IncidentUpdate();
            updateLog.setIncidentId(incidentId);
            updateLog.setUpdatedBy(updatedBy);
            updateLog.setOldStatus(oldStatus);
            updateLog.setNewStatus(newStatus);
            updateLog.setUpdateNotes(notes);
            updateLog.setActionTaken(actionTaken);
            updateDAO.addUpdate(updateLog);
        }
        return updated;
    }

    public boolean deleteIncident(int incidentId) throws SQLException {
        return incidentDAO.deleteIncident(incidentId);
    }

    public List<Incident> searchIncidents(String query, String statusFilter, String raggingTypeFilter) throws SQLException {
        return incidentDAO.searchIncidents(query, statusFilter, raggingTypeFilter);
    }

    public Map<String, Integer> getStatistics() throws SQLException {
        return incidentDAO.getStatistics();
    }

    public List<IncidentUpdate> getIncidentHistory(int incidentId) throws SQLException {
        return updateDAO.getUpdatesByIncidentId(incidentId);
    }
}
