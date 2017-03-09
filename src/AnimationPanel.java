import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPanel;
/**
 * This Class contains all the rectangles used for animating.
 * Creates a new paint method to animate the rectangles and change their colors.
 * @author Bao Tran
 * @version 1.00, 24 January 2017
 * @see AnimationRectangle
 */
public class AnimationPanel extends JPanel{
	private AnimationRectangles rect;
	private HashMap<Long,ArrayList<AnimationRectangles>> currentRectangles = new HashMap<Long,ArrayList<AnimationRectangles>>();
	private String[] channelColor = {"#00FFDE","#21A812","#297FFF","#FF8D29","#9529FF","#EB29FF","#A88132","#CC0066","#6072BA","#EB92BE"};	
	private ArrayList <AnimationRectangles> rectangles = new ArrayList<AnimationRectangles>();
	
	/**
	 * Constructor sets up the JPanel preferences
	 */
	public AnimationPanel(){
		this.setPreferredSize(new Dimension(832,800));
		this.setBackground(Color.DARK_GRAY);
	}
	
	
	/**
	 * Creates a rectangle with y position that makes the entire bar not visible in the animation panel
	 * Adds it to a HashMap and an Array for easy searching and modifying of rectangle objects
	 * x position represents the key it plays as it animates down
	 * @param pitch The pitch of the note being generated
	 * @param velocity The velocity of the note being generated
	 * @param xpos The x position of the note being generated
	 * @param duration The duration of the note being generated
	 * @param start The time the note actually starts
	 * @param channel The channel of the note being generated
	 */
	public void addrect(int pitch, int velocity, long duration, long start, int xpos,int channel){
		rect = new AnimationRectangles(pitch,velocity,xpos,duration,start,channel);
		if (currentRectangles.containsKey(start)){
			currentRectangles.get(start).add(rect);
			rectangles.add(rect);
		}
		else
		{
			ArrayList<AnimationRectangles> r = new ArrayList<AnimationRectangles>();
			r.add(rect);
			currentRectangles.put(start, r);
			rectangles.add(rect);
		}
		
	}
	/**
	 * Returns the HashMap of Rectangles
	 * @return currentRectangles
	 */
	public HashMap<Long,ArrayList<AnimationRectangles>> getCurrentRectangles(){return currentRectangles;}
	
	/**
	 * Iterates through the collection of current rectangles
	 * If within bounds, repaint
	 * if outside of bounds, delete
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
       
        for (int i = 0; i < rectangles.size(); i ++)
        {
        
        		AnimationRectangles r = rectangles.get(i);
        		if(r.y <780){
		       	 g2.setColor(Color.decode(channelColor[r.getcurrentChannel()%10]));
		       	 
		       	 g2.fill(r);
		         g2.setColor(Color.DARK_GRAY);
		         g2.draw(r);
		        	
        		}
        		else
        		{
        			rectangles.remove(i);
        			i--;
        		}
           
        }}
       
}
