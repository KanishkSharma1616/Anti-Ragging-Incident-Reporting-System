package com.antiragging.dao;

import com.antiragging.db.DBConnection;
import com.antiragging.model.IncidentUpdate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for tracking incident update history.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class IncidentUpdateDAO {

    public boolean addUpdate(IncidentUpdate update) throws SQLException {
        String sql = """
            INSERT INTO incident_updates (incident_id, updated_by, old_status, new_status, update_notes, action_taken)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, update.getIncidentId());
            pstmt.setInt(2, update.getUpdatedBy());
            pstmt.setString(3, update.getOldStatus());
            pstmt.setString(4, update.getNewStatus());
            pstmt.setString(5, update.getUpdateNotes());
            pstmt.setString(6, update.getActionTaken());
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<IncidentUpdate> getUpdatesByIncidentId(int incidentId) throws SQLException {
        List<IncidentUpdate> list = new ArrayList<>();
        String sql = """
            SELECT u.*, us.full_name as updated_by_name 
            FROM incident_updates u 
            JOIN users us ON u.updated_by = us.user_id 
            WHERE u.incident_id = ? 
            ORDER BY u.update_id DESC
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, incidentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    IncidentUpdate up = new IncidentUpdate();
                    up.setUpdateId(rs.getInt("update_id"));
                    up.setIncidentId(rs.getInt("incident_id"));
                    up.setUpdatedBy(rs.getInt("updated_by"));
                    up.setUpdatedByName(rs.getString("updated_by_name"));
                    up.setOldStatus(rs.getString("old_status"));
                    up.setNewStatus(rs.getString("new_status"));
                    up.setUpdateNotes(rs.getString("update_notes"));
                    up.setActionTaken(rs.getString("action_taken"));
                    up.setUpdatedAt(rs.getTimestamp("updated_at"));
                    list.add(up);
                }
            }
        }
        return list;
    }
}
