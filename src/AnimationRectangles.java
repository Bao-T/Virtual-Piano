import java.awt.Rectangle;
/**
 * This Class creates rectangle shapes for incoming notes from a Midi stream.
 * @author Bao Tran
 * @version 1.00, 24 January 2017
 */
public class AnimationRectangles extends Rectangle{
	private int pitch,velocity,channel;
	private long duration,startTime;
	boolean on = false;

	private final int [] blackKeyValues = {	1,13,25,37,49,61,73,85,97,109,121,
											3,15,27,39,51,63,75,87,99,111,123,
											6,18,30,42,54,66,78,90,102,114,126,
											8,20,32,44,56,68,80,92,104,116,
											10,22,34,46,58,70,82,94,106,118};
	/**
	 * Constructor
	 * Creates a rectangle with y position that makes the entire bar not visible in the animation panel
	 * x position represents the key it plays as it animates down
	 * @param pitch The pitch of the note being generated
	 * @param velocity The velocity of the note being generated
	 * @param xpos The x position of the note being generated
	 * @param duration The duration of the note being generated
	 * @param start The time the note actually starts
	 * @param channel The channel of the note being generated
	 */
	public AnimationRectangles(int pitch,int velocity, int xpos, long duration,long start, int channel){
		this.pitch = pitch;
		this.velocity = velocity;
		this.duration = duration;
		this.channel = channel;
		this.startTime = start;
		this.x = xpos;
		this.y= (int) (0 - duration);
		if (isWhiteKey(pitch)){
			setBounds(xpos+4,y, 8, (int) (duration));
		}
		else{
			setBounds(xpos+4, y,8, (int) (duration));
		
		}}
	/**
	 * Animate this rectangle by increasing the y coordinate (Moving it down the screen)
	 */
	public void animate(){
		y ++;
		this.setLocation(x,y);
	}
	/**
	 * Determines when the rectangle is past the animation screen's reach
	 * @return True if past 780
	 */
	public Boolean isFinished(){
		if (y >= 780)
		{
			return true;
		}
		else
			return false;
	}
	public int getPitch(){return pitch;}
	public int getVelocity(){return velocity;}
	public long getDuration(){return duration;}
	public long getStart(){return startTime;}
	public int getcurrentChannel(){return channel;}
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
