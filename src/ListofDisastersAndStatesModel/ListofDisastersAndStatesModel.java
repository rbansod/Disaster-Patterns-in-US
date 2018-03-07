package ListofDisastersAndStatesModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.DisastersBean;
import db.DbConnection;

public class ListofDisastersAndStatesModel {

	public Map<String,ArrayList<String>> getDisasters(String stateCodeVal, String disasterTypeVal){
		Map<String,ArrayList<String>> disasterMap = new HashMap<String, ArrayList<String>>();
		
		DbConnection d = new DbConnection();
		Statement ps = null;
		ResultSet rs=null;
		ps=d.DbConnection1();

		String sql = "select State, Incident_Type, count(Incident_Type) as incidentCount from disaster";
		if((stateCodeVal!=null && !"".equals(stateCodeVal.trim())) || (disasterTypeVal!=null && !"".equals(disasterTypeVal.trim()))){
			if((stateCodeVal!=null && !"".equals(stateCodeVal.trim()) && !"ALL".equalsIgnoreCase(stateCodeVal.trim())) 
					&& (disasterTypeVal!=null && !"".equals(disasterTypeVal.trim()) && !"ALL".equalsIgnoreCase(disasterTypeVal.trim()))){
				sql+= " where State='"+stateCodeVal+"' and Incident_Type='"+disasterTypeVal+"'";
			}
			else if(stateCodeVal!=null && !"".equals(stateCodeVal.trim()) && !"ALL".equalsIgnoreCase(stateCodeVal.trim())){
				sql+= " where State='"+stateCodeVal+"'";
			}
			else if(disasterTypeVal!=null && !"".equals(disasterTypeVal.trim()) && !"ALL".equalsIgnoreCase(disasterTypeVal.trim())){
				sql += " where state IN ('HI', 'AK', 'FL', 'SC', 'GA', 'AL', 'NC', 'TN', 'RI', 'CT', 'MA',"+
						"'ME','NH', 'VT', 'NY', 'NJ', 'PA', 'DE', 'MD', 'WV', 'KY', 'OH',"+ 
						"'MI', 'WY', 'MT', 'ID', 'WA', 'DC', 'TX', 'CA', 'AZ', 'NV', 'UT'," +
						"'CO', 'NM', 'OR', 'ND', 'SD', 'NE', 'IA', 'MS', 'IN', 'IL', 'MN'," +
						"'WI', 'MO', 'AR', 'OK', 'KS', 'LA', 'VA')";
				sql+= " and Incident_Type='"+disasterTypeVal+"'";
			}
		}
		if(stateCodeVal==null || "".equalsIgnoreCase(stateCodeVal.trim())|| "ALL".equalsIgnoreCase(stateCodeVal.trim()) && (disasterTypeVal==null || "".equals(disasterTypeVal.trim()) || "ALL".equalsIgnoreCase(disasterTypeVal.trim()))){
			sql += " where state IN ('HI', 'AK', 'FL', 'SC', 'GA', 'AL', 'NC', 'TN', 'RI', 'CT', 'MA',"+
					"'ME','NH', 'VT', 'NY', 'NJ', 'PA', 'DE', 'MD', 'WV', 'KY', 'OH',"+ 
					"'MI', 'WY', 'MT', 'ID', 'WA', 'DC', 'TX', 'CA', 'AZ', 'NV', 'UT'," +
					"'CO', 'NM', 'OR', 'ND', 'SD', 'NE', 'IA', 'MS', 'IN', 'IL', 'MN'," +
					"'WI', 'MO', 'AR', 'OK', 'KS', 'LA', 'VA')";
		}
		sql+= " group by State, Incident_Type order by State, Incident_Type";
		System.out.println(sql);
		try {
			rs = ps.executeQuery(sql);
			while(rs.next()){
				String stateCode = rs.getString("State");
				if(stateCode!=null && !"".equals(stateCode.trim())){
					if(disasterMap.get(stateCode.trim())!=null){
						ArrayList<String> disasterType = disasterMap.get(stateCode.trim());
						String incidentType = rs.getString("Incident_Type");
						if(incidentType!=null && !"".equals(incidentType.trim())){
							String incidentCount = rs.getString("incidentCount");
							disasterType.add(incidentType+"-"+incidentCount);
							disasterMap.put(stateCode, disasterType);
						}
					}
					else{
						ArrayList<String> disasterType = new ArrayList<String>();
						String incidentType = rs.getString("Incident_Type");
						String count = rs.getString("incidentCount");
						
						disasterType.add(incidentType+"-"+count);
						disasterMap.put(stateCode, disasterType);
					}
				}
			}
			ps.close();
			return disasterMap;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	public HashMap<String,String> getData(String type){
		
		HashMap<String,String> typecount = new HashMap<String,String>();
		try {
			DbConnection d = new DbConnection();
			Statement ps = null;
			ResultSet rs=null;
			ps=d.DbConnection1();
			String sql = " select state ,count(Incident_type) as total from disaster";
			if(type!=null && !"".equals(type.trim()) && !"ALL".equalsIgnoreCase(type.trim())){
				sql+= " where Incident_type='"+type+"' group by state";
			}
			
			rs=ps.executeQuery(sql);
			
			while(rs.next()){
				typecount.put(rs.getString("state"), ((Integer)(rs.getInt("total"))).toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return  typecount ;
	}
	
	public HashMap<String,String> getDisaster(String state){
		
		HashMap<String,String> statecount = new HashMap<String,String>();
		
		try {
			DbConnection d = new DbConnection();
			Statement ps = null;
			ResultSet rs=null;
			ps=d.DbConnection1();
			String sql = "select Incident_type as type , count(Incident_type) as count from disaster where state='"+state+"' group by Incident_type";
			rs=ps.executeQuery(sql);
			
			while(rs.next()){
				statecount.put(rs.getString("type"), rs.getString("count"));
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return statecount;
	}
	
}
