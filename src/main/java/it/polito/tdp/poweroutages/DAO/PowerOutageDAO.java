package it.polito.tdp.poweroutages.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutage;

public class PowerOutageDAO {
	
	public List<Nerc> getNercList() {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	
	public List<PowerOutage> getAllPowerOutagePerNerc(Nerc nerc){
		String sql = "SELECT p.id, p.customers_affected,p.date_event_began,p.date_event_finished,p.demand_loss "
				+ "FROM poweroutages p, nerc n "
				+ "WHERE p.nerc_id=n.id AND n.id=? AND n.value=? "
				+ "ORDER BY p.date_event_began ASC";
		
		List<PowerOutage> powerOutageList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, nerc.getId());
			st.setString(2, nerc.getValue());
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				PowerOutage powerOutage= new PowerOutage(rs.getInt("id"),rs.getInt("customers_affected"),rs.getTimestamp("date_event_began").toLocalDateTime(),rs.getTimestamp("date_event_finished").toLocalDateTime(),rs.getInt("demand_loss"));
				powerOutageList.add(powerOutage);			
			}

			conn.close();
			st.close();
			
			return powerOutageList;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		
	}
	
	public int getHoursBetweenDates(LocalDateTime beginning, LocalDateTime ending) {
		String sql="SELECT TIMESTAMPDIFF (HOUR,p.date_event_began,p.date_event_finished) AS diff  "
				+ "FROM poweroutages p "
				+ "WHERE p.date_event_began=? AND p.date_event_finished=? "
				+ "ORDER BY p.date_event_began ASC";
		int ore = 0;
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setTimestamp(1, Timestamp.valueOf(beginning));
			st.setTimestamp(2, Timestamp.valueOf(ending));
			ResultSet rs = st.executeQuery();			
			
			while (rs.next()) {
				ore=rs.getInt("diff");			
			}

			conn.close();
			st.close();
			
			return ore;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	

}
