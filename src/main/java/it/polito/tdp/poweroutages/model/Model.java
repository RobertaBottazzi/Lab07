package it.polito.tdp.poweroutages.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO podao;
	List<PowerOutage> poList;
	List<PowerOutage> soluzioneMigliore;
	
	public Model() {
		podao = new PowerOutageDAO();
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}
	
	public List<PowerOutage> getAllPowerOutagePerNerc(Nerc nerc){
		return podao.getAllPowerOutagePerNerc(nerc);
	}
	
	public int getHoursBetweenDates(LocalDateTime beginning, LocalDateTime ending) {
		return podao.getHoursBetweenDates(beginning, ending);
	}
	
	public List<PowerOutage> trovaSequenza(Nerc nerc, int years, int hours) {
		poList=podao.getAllPowerOutagePerNerc(nerc);
		List<PowerOutage> parziale= new ArrayList<>();
		soluzioneMigliore=null;
		cerca(parziale,0,years,hours);
		return soluzioneMigliore;
	} 
	
	private void cerca(List<PowerOutage> parziale, int livello, int years, int hours) {		
		//caso terminale
		if(parziale.size()>poList.size())
			return;
		if(calcolaMaxHours(parziale)>hours)
			return;
		if(calcolaMaxYears(parziale)>years)
			return;
		if((calcolaMaxHours(parziale)<=hours && calcolaMaxYears(parziale)<=years)) {
			int maxPeople= calcolaMaxPersone(parziale);			
			if(soluzioneMigliore==null || maxPeople>calcolaMaxPersone(soluzioneMigliore)) 
				soluzioneMigliore=new ArrayList<>(parziale);
		}
		if(livello<=poList.size()-1) {
			parziale.add(poList.get(livello));
			cerca(parziale,livello+1,years,hours);
			parziale.remove(poList.get(livello));
			cerca(parziale,livello+1,years,hours);
		}		
	}
	
	public int calcolaMaxHours(List<PowerOutage> parziale) {
		int somma=0;
		for(PowerOutage p: parziale) {
			somma+=podao.getHoursBetweenDates(p.getDateEventBegan(), p.getDateEventFinished());
			//System.out.println("CODICE: "+p.getId());
		}
		//System.out.println("ore "+somma);
		return somma;
	}
	private int calcolaMaxYears(List<PowerOutage> parziale) {
		int massimo=0;
		if(parziale.size()>0)
			massimo=parziale.get(parziale.size()-1).getDateEventBegan().getYear()-parziale.get(0).getDateEventBegan().getYear();
		//System.out.println("anni "+massimo);
		return massimo;		
	}
	
	public int calcolaMaxPersone(List<PowerOutage> parziale) {
		int somma=0;
		for(PowerOutage p: parziale) {
			somma+=p.getCustomersAffected();
			//System.out.println("CODICE: "+p.getId());
		}
		//System.out.println("people "+somma);
		return somma;
	}
}
