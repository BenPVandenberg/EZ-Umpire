// Ben Vandenberg © 2019
package application;

import java.time.LocalDateTime;
import java.util.*;

public class Control {
    public static ArrayList<Umpire>   		umpires = new ArrayList<>();
    public static ArrayList<Match>    		matches = new ArrayList<>();
    public static Umpire 			  		umpWorking;
    public static Match 			  		matchWorking;
    public static HashMap<String, String> 	settings = new HashMap<>();
    
    /**
     * Function to add a Umpire or Match to their respective array
     *
     * @param toAdd the object to be added
     */
    public static void add(Changeable toAdd){
        if (toAdd instanceof Match){
            matches.add((Match) toAdd);
        } else if (toAdd instanceof Umpire){
            umpires.add((Umpire) toAdd);
        }
    }

    
    /**
     * Used to convert a LocalDateTime to a string reference, used for availability
     *
     * @param time the date to convert
     * @return String, the respective reference. Will return "INVALID" if date is on the weekend or not 18h/20h
     */
    public static String convertTime(LocalDateTime time){
        String rtn = "";
        switch (time.getDayOfWeek().getValue()){
            case 1: rtn += "m"; break;
            case 2: rtn += "t"; break;
            case 3: rtn += "w"; break;
            case 4: rtn += "th"; break;
            case 5: rtn += "f"; break;
            default: rtn = "Not a weekday"; break;
        }
        if (time.getHour() == 18 && rtn.length() <= 2) {
            rtn += "6";
            return rtn;
        }
        else if (time.getHour() == 20 && rtn.length() <= 2) {
            rtn += "8";
            return rtn;
        }
        FxDialogs.showWarning("Invalid Date Entered", "An invalid date was entered, report this error if the date was a weekday and is 6-6:59pm or 8-8:59pm");
        return "INVALID";
    }
    
    /**
     * Function to get an umpire
     *
     * @param toGet the reference to the umpire
     * @return Umpire
     */
    public static Umpire getUmp(int toGet){
			for (Umpire i : Control.umpires)
				if (i.id == toGet)
					return i;
			return null;
    	}
    
    /**
     * Function to get match
     *
     * @param toGet the reference to the match
     * @return Match
     */
    public static Match getMatch(int toGet){ 
    		for (Match i : Control.matches)
    			if (i.gameID == toGet)
    				return i;
    		return null;
    	}
    
    /**
     * Returns the reference to the object
     *
     * @param toGet the date to check if ump is available for
     * @return boolean true if available, false if date is invalid or ump is not available
     */
    public static int getReference(Changeable toGet){ 
    		if (toGet instanceof Match)
    			return ((Match) toGet).gameID;
    		
    		if (toGet instanceof Umpire)
    			return ((Umpire) toGet).id;
    		
    		return -1;
    	}

    public static void update(Changeable toUpdate){ toUpdate.update(); }
}
