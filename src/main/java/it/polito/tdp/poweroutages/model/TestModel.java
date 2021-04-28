package it.polito.tdp.poweroutages.model;

import java.time.LocalDateTime;
import java.time.Month;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		PowerOutageDAO dao= new PowerOutageDAO();
		//System.out.println(model.getNercList());
		Nerc nerc = null;
		for(Nerc n: model.getNercList()) {
			if(n.getValue().equals("MAAC"))
				nerc=n;
		}
		System.out.println(model.trovaSequenza(nerc,4,200));
		/*LocalDateTime data= LocalDateTime.of(2002,Month.DECEMBER, 25,17, 00, 00);
		LocalDateTime date= LocalDateTime.of(2007,Month.DECEMBER, 25,17, 00, 00);
		System.out.println("data: "+data.getYear()+ "date: "+date.getYear()+"\n"+ (data.getYear()-date.getYear()));*/
		
		/*for(PowerOutage p: model.getAllPowerOutagePerNerc(nerc)) {
			System.out.println("ORE: "+dao.getHoursBetweenDates(p.getDateEventBegan(), p.getDateEventFinished())+"\n");
		}*/
		
		
	}

}
