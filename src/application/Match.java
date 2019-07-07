// Ben Vandenberg © 2019
package application;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Match implements Changeable, Serializable, Comparable<Match>{

	private static final long  serialVersionUID = 1L;
	public static int          NEXT_GAME_ID = 1;
    public final int           gameID;
    public char                gender; // 'M' or 'F'
    public String              division;
    public String              diamond;
    public LocalDateTime       startTime;
    public int                 fee,plateUmp,baseUmp;
    public boolean             umpsPaid;

    public Match() { this('M',"","",null); }

    public Match(char gender, String division, String diamond, LocalDateTime date) {
        this.gameID = NEXT_GAME_ID++;
        this.gender = gender;
        this.division = division;
        this.diamond = diamond;
        this.startTime = date;
        this.umpsPaid = false;
        this.baseUmp = -1;
        this.plateUmp = -1;
        update();
    }

    /**
     * Function to get an umpire
     *
     * @param time The time of the game to get the fee for
     * @return int, number in $ to be paid to the umpire
     */
    public static int getFee(LocalTime time){
    	
    	if (time.equals(LocalTime.of(18, 30)))
    		return 30;
    	if (time.equals(LocalTime.of(20,30)))
    		return 35;
    	
        return 0;
    }

    @Override
    public void update() {
    	if (startTime == null)
    		fee = 0;
    	else
    		fee = Match.getFee(startTime.toLocalTime());
    }


    @Override
    public int compareTo(Match o){
        if (startTime.isBefore(o.startTime))
            return -1;
        if (startTime.equals(o.startTime))
            return gameID - o.gameID;
        return 1;
    }

    @Override
    public String toString(){
        String g = (gender == 'M') ? "Boys" : "Girls";
        DateTimeFormatter time = DateTimeFormatter.ofPattern("h:mm");
		return String.format("%s %s, %s", 
        		division,
        		g,
        		startTime.format(time)
        		);
    }

    public int getGameID() { return gameID; }
    public char getGender() { return gender; }
    public void setGender(char gender) { this.gender = gender; }
    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }
    public String getDiamond() { return diamond; }
    public void setDiamond(String diamond) { this.diamond = diamond; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public boolean getUmpsPaid() { return umpsPaid; }
    public void setUmpsPaid(boolean umpsPaid) { this.umpsPaid = umpsPaid; }
    
    public String getTimeStr() {
    	DateTimeFormatter two = DateTimeFormatter.ofPattern("h:mm");
    	return startTime.format(two);
    }
    
    public String getDateStr() {
    	DateTimeFormatter one = DateTimeFormatter.ofPattern("EEE MMMM d");
    	return startTime.format(one);
    }
    
    public String getPaidTxt() {
    	return (umpsPaid) ? "Yes" : "" ;
    }
    
    public String getPresentTxt() {
    	return (plateUmp != -1 || baseUmp != -1) ? "Yes" : "" ;
    }
}
