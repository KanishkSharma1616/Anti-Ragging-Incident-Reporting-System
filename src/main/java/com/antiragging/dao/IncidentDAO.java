package com.antiragging.dao;

import com.antiragging.db.DBConnection;
import com.antiragging.model.Incident;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Incidents.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class IncidentDAO {

    public boolean addIncident(Incident incident) throws SQLException {
        String sql = """
            INSERT INTO incidents (
                student_user_id, student_name, student_id, department, semester,
                contact_number, email, incident_date, incident_time, incident_location,
                ragging_type, description, persons_involved, witness_details,
                evidence_description, immediate_help_required, status
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Pending')
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, incident.getStudentUserId());
            pstmt.setString(2, incident.getStudentName());
            pstmt.setString(3, incident.getStudentId());
            pstmt.setString(4, incident.getDepartment());
            pstmt.setString(5, incident.getSemester());
            pstmt.setString(6, incident.getContactNumber());
            pstmt.setString(7, incident.getEmail());
            pstmt.setDate(8, incident.getIncidentDate());
            pstmt.setString(9, incident.getIncidentTime());
            pstmt.setString(10, incident.getIncidentLocation());
            pstmt.setString(11, incident.getRaggingType());
            pstmt.setString(12, incident.getDescription());
            pstmt.setString(13, incident.getPersonsInvolved());
            pstmt.setString(14, incident.getWitnessDetails());
            pstmt.setString(15, incident.getEvidenceDescription());
            pstmt.setString(16, incident.getImmediateHelpRequired());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        incident.setIncidentId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public List<Incident> getAllIncidents() throws SQLException {
        List<Incident> list = new ArrayList<>();
        String sql = "SELECT * FROM incidents ORDER BY incident_id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapIncident(rs));
            }
        }
        return list;
    }

    public List<Incident> getStudentIncidents(int studentUserId) throws SQLException {
        List<Incident> list = new ArrayList<>();
        String sql = "SELECT * FROM incidents WHERE student_user_id = ? ORDER BY incident_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentUserId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapIncident(rs));
                }
            }
        }
        return list;
    }

    public Incident getIncidentById(int incidentId) throws SQLException {
        String sql = "SELECT * FROM incidents WHERE incident_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, incidentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapIncident(rs);
                }
            }
        }
        return null;
    }

    public boolean updateIncidentStatusAndDetails(int incidentId, String status, String assignedOfficer,
                                                  String notes, String actionTaken, String resolutionDetails) throws SQLException {
        String sql = """
            UPDATE incidents SET 
                status = ?, 
                assigned_officer = ?, 
                investigation_notes = ?, 
                action_taken = ?, 
                resolution_details = ? 
            WHERE incident_id = ?
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, assignedOfficer);
            pstmt.setString(3, notes);
            pstmt.setString(4, actionTaken);
            pstmt.setString(5, resolutionDetails);
            pstmt.setInt(6, incidentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteIncident(int incidentId) throws SQLException {
        String sql = "DELETE FROM incidents WHERE incident_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, incidentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Incident> searchIncidents(String query, String statusFilter, String raggingTypeFilter) throws SQLException {
        List<Incident> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM incidents WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (query != null && !query.trim().isEmpty()) {
            sql.append(" AND (CAST(incident_id AS CHAR) LIKE ? OR student_id LIKE ? OR student_name LIKE ? OR incident_location LIKE ?) ");
            String searchPattern = "%" + query.trim() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        if (statusFilter != null && !statusFilter.equals("All")) {
            sql.append(" AND status = ? ");
            params.add(statusFilter);
        }

        if (raggingTypeFilter != null && !raggingTypeFilter.equals("All")) {
            sql.append(" AND ragging_type = ? ");
            params.add(raggingTypeFilter);
        }

        sql.append(" ORDER BY incident_id DESC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapIncident(rs));
                }
            }
        }
        return list;
    }

    public Map<String, Integer> getStatistics() throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("Total", 0);
        stats.put("Pending", 0);
        stats.put("Under Investigation", 0);
        stats.put("Resolved", 0);
        stats.put("Rejected", 0);
        stats.put("Closed", 0);

        String sql = "SELECT status, COUNT(*) as cnt FROM incidents GROUP BY status";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int total = 0;
            while (rs.next()) {
                String st = rs.getString("status");
                int count = rs.getInt("cnt");
                stats.put(st, count);
                total += count;
            }
            stats.put("Total", total);
        }
        return stats;
    }

    private Incident mapIncident(ResultSet rs) throws SQLException {
        Incident inc = new Incident();
        inc.setIncidentId(rs.getInt("incident_id"));
        inc.setStudentUserId(rs.getInt("student_user_id"));
        inc.setStudentName(rs.getString("student_name"));
        inc.setStudentId(rs.getString("student_id"));
        inc.setDepartment(rs.getString("department"));
        inc.setSemester(rs.getString("semester"));
        inc.setContactNumber(rs.getString("contact_number"));
        inc.setEmail(rs.getString("email"));
        inc.setIncidentDate(rs.getDate("incident_date"));
        inc.setIncidentTime(rs.getString("incident_time"));
        inc.setIncidentLocation(rs.getString("incident_location"));
        inc.setRaggingType(rs.getString("ragging_type"));
        inc.setDescription(rs.getString("description"));
        inc.setPersonsInvolved(rs.getString("persons_involved"));
        inc.setWitnessDetails(rs.getString("witness_details"));
        inc.setEvidenceDescription(rs.getString("evidence_description"));
        inc.setImmediateHelpRequired(rs.getString("immediate_help_required"));
        inc.setStatus(rs.getString("status"));
        inc.setAssignedOfficer(rs.getString("assigned_officer"));
        inc.setInvestigationNotes(rs.getString("investigation_notes"));
        inc.setActionTaken(rs.getString("action_taken"));
        inc.setResolutionDetails(rs.getString("resolution_details"));
        inc.setSubmissionDate(rs.getTimestamp("submission_date"));
        inc.setLastUpdated(rs.getTimestamp("last_updated"));
        return inc;
    }
}
