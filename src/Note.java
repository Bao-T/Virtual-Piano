/**
 * This Class contains all the information a MIDI note contains
 * for easily reading note data.
 * @author Bao Tran
 * @version 1.00, 24 January 2017
 */
public class Note {
	private int channel,pitch,velocity;
	long endTime = 0;
	long startTime;
	/**
	 * Constructor
	 * @param Start is the start of the note
	 * @param Channel is the note's channel(track placement)
	 * @param Velocity is the note's velocity(how loud)
	 * @param Pitch is the note's pitch(sound)
	 */
	public Note(long Start, int Channel, int Velocity, int Pitch) {
	    startTime = Start;
	    channel = Channel;
	    pitch = Pitch;
	    velocity = Velocity;

	  }
	public int getChannel(){return channel;}
	public int getPitch(){return pitch;}
	public int getVelocity(){return velocity;}
	public long getDuration(){return endTime-startTime;}
	public long getStart(){return startTime;}
	public void setEnd(long e){endTime = e;}
}