package application;

import java.util.ArrayList;
import java.util.HashMap;

import Controller.MainMenuController;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Umpire implements Changeable, Serializable, Comparable<Umpire>  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final int id;
	public static int NEXT_UMP_ID = 1;
	public String name, email;
    public float level;
    public HashMap<String, Boolean> availability;
    public ArrayList<Integer> assignments;

    //    Constructors
    public Umpire() {
        this("", "", 1f);
    }

    public Umpire(String name, String email, float level) {
    	this.id = NEXT_UMP_ID++;
        this.name = name.trim();
        this.email = email.trim();
        this.level = level;
        this.assignments = new ArrayList<>();
        this.availability = new HashMap<>();

        availability.put("m6", true);
        availability.put("m8", true);
        availability.put("t6", true);
        availability.put("t8", true);
        availability.put("w6", true);
        availability.put("w8", true);
        availability.put("th6", true);
        availability.put("th8", true);
        availability.put("f6", true);
        availability.put("f8", true);
    }
//    Methods

    /**
     * Function to check if an ump is available
     *
     * @param time the date to check if ump is available for
     * @return boolean true if available, false if date is invalid or ump is not available
     */
    public boolean isAvailable(LocalDateTime time) {
        for (int i : assignments) {
        	Match working = Control.getMatch(i);
            if (working.startTime.equals(time))
                return false;
        }

        String temp = Control.convertTime(time);
        if (!temp.equals("INVALID"))
            return availability.get(temp);
        return false;
    }

    /**
     * Gets the amount owed from an umpire from all games in current view period
     *
     * @return int, the amount due in $
     */
    public int payDue() {
        return payDue(MainMenuController.start,MainMenuController.end);
    }

    /**
     * Gets the amount owed from an umpire from all games before deadline
     *
     * @param deadline The date of the end of the paycheck period (inclusive)
     * @return the amount due in $
     */
    public int payDue(LocalDateTime start,LocalDateTime end) {
        int rtn = 0;
        for (int i : assignments) {
        	Match working = Control.getMatch(i);
            if (working.startTime.isAfter(start) && working.startTime.isBefore(end) && !working.umpsPaid) {
                if (level < 2)
                	rtn += 20;
                else
                    rtn += working.fee;
            }
        }
        return rtn;
    }
    
    /**
     * Deletes a game from umpires assignments
     *
     * @param toDel The match to delete
     */
    public void delAssignment(Match toDel) {
    	int id = toDel.gameID;
    	for (int i =0; i < assignments.size(); i++)
    		if (assignments.get(i) == id) {
    			assignments.remove(i);
    			return;
    		}
    }
    
    /**
     * Generates a message including all games within view period
     *
     * @return String The generated message
     */
    public String generateMessage() {
    	String rtn = "";
    	DateTimeFormatter tmd = DateTimeFormatter.ofPattern("EEE MMMM d");
    	LocalDate cur = LocalDate.MIN;
    	String place;
    	
        for (Match t : Control.matches)
        	if (t.startTime.isAfter(MainMenuController.start) && t.startTime.isBefore(MainMenuController.end) && assignments.contains(Control.getReference(t))) {
        		if (!t.startTime.toLocalDate().equals(cur)) {
        			rtn += t.startTime.format(tmd) + ":\n";
        			cur = t.startTime.toLocalDate();
        		}
        		place = (t.diamond.equals("D1") || t.diamond.equals("D2") || t.diamond.equals("D3")) ? "" : " in " + t.diamond;
        		rtn += "- " + t.toString() + place + "\n";
        	}
    	
    	return rtn;
    }

    @Override
    public void update() {}

    @Override
    public int compareTo(Umpire o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString(){
        return name;
    }
    
    public String getName() { return name; }
    public int getNumGamesThisWeek() {
    	int rtn = 0; 
    	for (int i : assignments) {
    		if (Control.getMatch(i).startTime.isAfter(MainMenuController.start) && Control.getMatch(i).startTime.isBefore(MainMenuController.end))
    			rtn++;
    	}
    	
    	return rtn;
    }
}