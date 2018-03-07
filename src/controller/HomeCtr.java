package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;






import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import bean.DisastersBean;
import ListofDisastersAndStatesModel.ListofDisastersAndStatesModel;

public class HomeCtr extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger("myLogger");
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.log(Level.INFO, "Getting ALL Disasters Type and States");
		Map<String,ArrayList<String>> disasters = new HashMap<String,ArrayList<String>>();
		ListofDisastersAndStatesModel disastersStatesModel = new ListofDisastersAndStatesModel();
		disasters=disastersStatesModel.getDisasters("ALL", "ALL");
		request.setAttribute("disasters",disasters);
		jsonCreation(disasters);
		logger.log(Level.INFO, "Redirecting to Home Page");
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ListofDisastersAndStatesModel disastersStatesModel = new ListofDisastersAndStatesModel();
		String query=request.getQueryString();
		if(query!=null && query.equals("barchart")){
			String state = request.getParameter("state");
			HashMap<String,String> h1 = disastersStatesModel.getDisaster(state);
			request.setAttribute("state", h1);
			tsvcreation(h1);
			request.getRequestDispatcher("barchart.jsp").forward(request, response);
		}
		logger.log(Level.INFO, "Getting ALL Disasters Type and States");
		List<DisastersBean> disasters = new ArrayList<DisastersBean>();
		Map<String,ArrayList<String>> disastersMap = new HashMap<String,ArrayList<String>>();
		
		
		//disasters=disastersStatesModel.getDisasters();
		
		String type=request.getParameter("DisasterType");
		
		/*if(query!=null && query.equals("barchart") && query.equals("piechart")){
			disastersMap=disastersStatesModel.getDisasters("ALL", type);
			request.setAttribute("disasters",disasters);
			jsonCreation(disastersMap);
		}*/
		logger.log(Level.INFO, "Redirecting to Home Page");
		if(query!=null && query.equals("piechart")){
			HashMap<String,String> h =disastersStatesModel.getData(type);
			request.setAttribute("typeMap", h);
			jsonCreation1(h);
			request.getRequestDispatcher("piechart.jsp").forward(request, response);
		}
	}
	
	protected void jsonCreation(Map<String, ArrayList<String>> disasters){
		JSONObject obj = new JSONObject();
		
		Iterator it = disasters.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        obj.put(pair.getKey(), pair.getValue());
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		try {
	 
			FileWriter file = new FileWriter("C:\\Users\\Rohit\\Desktop\\VA\\Project\\Project1_group\\WebContent\\test.json");
			file.flush();
			file.write(obj.toJSONString());
			file.flush();
			file.close();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	 
		System.out.print(obj);
	 
	}
	
	protected void jsonCreation1(Map<String,String> typeMap){
		JSONObject obj = new JSONObject();
		
		Iterator it = typeMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        obj.put(pair.getKey(), pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		try {
			FileWriter file = new FileWriter("C:\\Users\\Rohit\\Desktop\\VA\\Project\\Project1_group\\WebContent\\test1.json");
			file.flush();
			file.write(obj.toJSONString());
			file.flush();
			file.close();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	 
	 
	}
	
	protected void tsvcreation(Map<String,String> statecount){
		
		FileWriter file=null;
		try {
			file = new FileWriter("C:\\Users\\Rohit\\Desktop\\VA\\Project\\Project1_group\\WebContent\\data.tsv");
			file.flush();
			file.write("letter"+ "\t");
			file.write("frequency"+ "\t\n" );
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator it = statecount.entrySet().iterator();	
		
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			 try {
				file.write(pair.getKey()+"\t");
				 file.write(pair.getValue()+"\t\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        it.remove(); 
	        
		}
		try {
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
