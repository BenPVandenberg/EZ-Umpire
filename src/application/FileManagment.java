package application;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class FileManagment{
	
    /**
     * Saves Settings, Umpires, and Matches to separate files and notifies user if it was successful
     */
	public static void save() {
		String username = System.getProperty("user.name");
		String dest = ("C:\\Users\\" + username + "\\AppData\\Roaming\\EZ_Umpire\\");
		boolean setSaved = true, umpSaved = true, matchSaved = true;
		
//		Save Settings
		try {
			File file = new File(dest+"Settings.ump");
			file.getParentFile().mkdirs();
			file.delete();
			
			FileOutputStream f = new FileOutputStream(file);
			ObjectOutputStream o = new ObjectOutputStream(f);
			
			o.writeObject(Control.settings);


			o.close();
			f.close();

		} catch (Exception e) {
			setSaved = false;
		}

//		Save Umps
		try {
			File file = new File(dest+"Umpires.ump");
			file.getParentFile().mkdirs();
			file.delete();
			
			FileOutputStream f = new FileOutputStream(file);
			ObjectOutputStream o = new ObjectOutputStream(f);
			
//			Write current NEXT_UMP_ID value
			o.writeObject(Umpire.NEXT_UMP_ID);

//			Write num of umps
			o.writeObject(Control.umpires.size());
			
			// Write objects to file
			for (Umpire i: Control.umpires) {
				o.writeObject(i);
			}

			o.close();
			f.close();

		} catch (Exception e) {
			umpSaved = false;
		}
		
//		Save Matches
		try {
			File file = new File(dest+"Matches.ump");
			file.getParentFile().mkdirs();
			file.delete();
			
			FileOutputStream f = new FileOutputStream(file);
			ObjectOutputStream o = new ObjectOutputStream(f);

//			Write current NEXT_GAME_ID value
			o.writeObject(Match.NEXT_GAME_ID);
			
//			Write num of matches
			o.writeObject(Control.matches.size());
			
			// Write objects to file
			for (Match i: Control.matches) {
				o.writeObject(i);
			}

			o.close();
			f.close();

		} catch (Exception e) {
			matchSaved = false;
		}
		
		if (setSaved && matchSaved && umpSaved)
			FxDialogs.showInformation("Saved successfully", "");
		else
			FxDialogs.showError("Save could not be saved properly", "");
	}
	
	
    /**
     * Loads Settings, Umpires, and Matches to separate files
     */
	public static void load() {
		Control.matches.clear();
		Control.umpires.clear();
		String username = System.getProperty("user.name");
		String dest = ("C:\\Users\\" + username + "\\AppData\\Roaming\\EZ_Umpire\\");
		
//		Settings Load
		try {

			FileInputStream fi = new FileInputStream(new File(dest+"Settings.ump"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			
//			Get settings Hashmap
			@SuppressWarnings("unchecked")
			HashMap<String, String> set = (HashMap<String, String>) oi.readObject();
			Control.settings = set;
			
			oi.close();
			fi.close();

		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			FxDialogs.showException("Unable to load Settings.ump","", e);
		}
		
//		Umpire Load
		ArrayList<Umpire> umps = new ArrayList<>();
		try {

			FileInputStream fi = new FileInputStream(new File(dest+"Umpires.ump"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			
//			Get NEXT_GAME_ID
			int num = (int) oi.readObject();
			Umpire.NEXT_UMP_ID = num;

//			Get # of umps
			num = (int) oi.readObject();
			
			// Read objects
			Umpire toAdd;
			for (int i=0;i<num;i++) {
				toAdd = (Umpire) oi.readObject();
				umps.add(toAdd);
			}
			
			oi.close();
			fi.close();

		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			FxDialogs.showException("Unable to load Umpires.ump","", e);
		}
		Control.umpires = umps;
		
		
//		Matches load
		ArrayList<Match> match = new ArrayList<>();
		try {

			FileInputStream fi = new FileInputStream(new File(dest+"Matches.ump"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			
//			Get NEXT_GAME_ID
			int num = (int) oi.readObject();
			Match.NEXT_GAME_ID = num;

//			Get # of matches
			num = (int) oi.readObject();
			
			// Read objects
			Match toAdd;
			for (int i=0;i<num;i++) {
				toAdd = (Match) oi.readObject();
				match.add(toAdd);
			}
			
			oi.close();
			fi.close();

		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			FxDialogs.showException("Unable to load Matches.ump","", e);
		}
		Control.matches = match;
	}
}
