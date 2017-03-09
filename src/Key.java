import java.awt.Rectangle;
import javax.sound.midi.*;
/**
 * This Class is used to reference Keys on a piano. Each object is a rectangle object.
 * @author Bao Tran
 * @version 1.00, 24 January 2017
 */
public class Key extends Rectangle{
	int currentChannel=0;
	private Boolean noteState = false;
	private int pitch;


	private final int [] blackKeyValues = {	1,13,25,37,49,61,73,85,97,109,121,
											3,15,27,39,51,63,75,87,99,111,123,
											6,18,30,42,54,66,78,90,102,114,126,
											8,20,32,44,56,68,80,92,104,116,
											10,22,34,46,58,70,82,94,106,118};
	
	/**
	 * Constructor
	 * Creates the object with pitch and x position parameters
	 * @param pitch is the pitch of the key
	 * @param xpos is the position in the x direction
	 */
	public Key(int pitch, int xpos){
		this.pitch = pitch;
		if (isWhiteKey(pitch)){setBounds(xpos, 0, 16, 80);}
		else{setBounds(xpos, 0, 16, 40);}
	}
	  public boolean isNoteOn() {return noteState;}
	  
      public void on() {setNoteState(true); }
      public void off() {setNoteState(false);}
      public void setChannel(int c){currentChannel = c;}
      public int getcurrentChannel(){return currentChannel;}
      public void setNoteState(Boolean state) {noteState = state;}
      public int getPitch(){return pitch;}
      
      /**
       * Checks if a key is white
       * @param pitch is the pitch of the note being checked
       * @return true if note with pitch x is white
       */
      public Boolean isWhiteKey(int pitch){
		for (int i : blackKeyValues){
			if (i == pitch)
				return false;
		}
		return true;
	}
}
