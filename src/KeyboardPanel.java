import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Vector;

import javax.sound.midi.*;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * This Class houses all the Keys for a virtual piano. Has functionality to play a tone based on a Key's pitch and velocity.
 * @author Bao Tran
 * @version 1.00, 24 January 2017
 * @see Key
 */
public class KeyboardPanel extends JPanel implements MouseListener {
	private MidiChannel[] mChannels;
	private Instrument[] instr;
	private Synthesizer midiSynth;
	
	private Key prevKey;
	private Key key;
	private Key[] blackKeys = new Key[36];
	private Key[] whiteKeys = new Key[52];
	private Vector<Key> Keys = new Vector<Key>();
	private HashMap<Integer,Key> keysByPitch = new HashMap<Integer,Key>();
	
	private JCheckBox mouseOverCB = new JCheckBox("mouseOver", true);

	private String[] channelColor = {"#00FFDE","#21A812","#297FFF","#FF8D29","#9529FF","#EB29FF","#A88132","#CC0066","#6072BA","#EB92BE"};

	/**
	 * Constructor
	 */
	public KeyboardPanel(){
		makeKeyboard();
	}
	
	/**
	 * This function access Java's built in MIDI library and initializes it to be able to produce note tones.
	 * It also functions to set up a listener when a key in the UI is hovered over by a mouse. The key will then play the corresponding note.
	 * As well, functions builds the piano interface with all 88 white and black keys
	 */
	public void makeKeyboard(){
			//Creates a new synthesizer and opens it.
		  	try{
		  
		        midiSynth = MidiSystem.getSynthesizer(); 
		        midiSynth.open();

		        //get and load default instrument and channel lists
		        instr = midiSynth.getDefaultSoundbank().getInstruments();
		        mChannels = midiSynth.getChannels();

		        midiSynth.loadInstrument(instr[0]);//loads an instrument, 0 for piano
		  }catch (Exception ex) { ex.printStackTrace(); return; }
		  	
		  //Creates a listener for mouse over a key. Plays the key	
		  this.setPreferredSize(new Dimension (832,80));
		  addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                if (mouseOverCB.isSelected()) {
                    key = getKey(e.getPoint());
                    if (prevKey != null && prevKey != key) {
                        prevKey.off();
                        
                        mChannels[0].noteOff(prevKey.getPitch());
                    } 
                    if (key != null && prevKey != key) {
                  	
                      key.on();
                     
                      mChannels[0].noteOn(key.getPitch(),100);
              		
                    }
                    prevKey = key;
                   repaint();
                }
                key = null;
            }
        });
        addMouseListener(this);
     
        //Temporary variables used to create black and white key visuals
        int transpose = 21;  
        int whiteIDs[] = { 0, 2, 3, 5, 7, 8, 10 }; 
        int keyWidth = 16;
        int keyHeight = 80;
        int whiteKeyCount = 0;
        int noteCounter = 0;
        int octive = 0;
        
        //Sets up all 88 keys on the piano based on pitch and position
        for (int i = 0; i < 52; i ++)
        {
      	  if (noteCounter == 7){
      		  noteCounter = 0;
      		  octive ++;
      	  }
      	  int pitch = octive * 12 + whiteIDs[i%7] + transpose;
      	  noteCounter ++;
            whiteKeys[whiteKeyCount++] = new Key(pitch,keyWidth*i+1);
        }
        int blackKeyCount = 0;
        for (int i = 0, x = 8; i < 7; x+= keyWidth,i++) {
            int pitch = i * 12 + transpose;
            
            blackKeys[blackKeyCount++]=new Key(pitch+1,x);
            x += keyWidth;
            blackKeys[blackKeyCount++]=new Key(pitch+4,x += keyWidth);
            blackKeys[blackKeyCount++]=new Key(pitch+6,x += keyWidth);
            x += keyWidth;
            blackKeys[blackKeyCount++]=new Key(pitch+9,x += keyWidth);
            blackKeys[blackKeyCount++]=new Key(pitch+11,x += keyWidth);
            if(i == 6)
          	  blackKeys[blackKeyCount++]=new Key(pitch+13,x += keyWidth);
        }
        
        
        //Add to data lists for later access
        for(int i = 0; i <blackKeys.length; i ++ ){
       	 Keys.add(blackKeys[i]);
        }
         
       for(int i = 0; i <whiteKeys.length; i ++ ){
      	 Keys.add(whiteKeys[i]);
      	 
       }
       for(Key k : Keys){
    	   keysByPitch.put(k.getPitch(), k);
       }

	}
	
	/**
	 * Function overrides the painting of each key. When a key is played, it will be repainted with a new color.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
         Graphics2D g2 = (Graphics2D) g;
         for (Key k : whiteKeys){
        	 if(k.isNoteOn()){
            	 //g2.setColor(Color.decode(channelColor[k.getcurrentChannel()]));
        		 g2.setColor(Color.decode(channelColor[8]));
        	 	}
        	 else{
        		 g2.setColor(Color.white);
        	 	}
        	 g2.fill(k);
             g2.setColor(Color.black);
             g2.draw(k);
         }
         for (Key k : blackKeys){
        	 if(k.isNoteOn()){
            	 //g2.setColor(Color.decode(channelColor[k.getcurrentChannel()]));
            	 g2.setColor(Color.decode(channelColor[8]));
                }
        	 else{
        		 g2.setColor(Color.black);
        	 	}
             g2.fill(k);
             g2.setColor(Color.white);
             g2.draw(k);
         }
         
     }
	
	//Additional Even Overrides
		@Override
		public void mouseClicked(MouseEvent arg0) {}
	
		@Override
		public void mouseEntered(MouseEvent e) {}
	
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			  if (prevKey != null) {
	              prevKey.off();
	              repaint();
	              prevKey = null;
	          }
		}
	
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			 prevKey = getKey(e.getPoint());
	         if (prevKey != null) {
	             prevKey.on();
	             repaint();
	         }
		}
	
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			if (prevKey != null) {
	            prevKey.off();
	            repaint();
	        }
		}
		
	/**
	 * 	Function finds which key the mouse is pointing to
	 * @param point is the object used to indicate where the mouse is positioned
	 * @return Returns the corresponding key that is being pointed to.
	 */
	public Key getKey(Point point) {
         for (int i = 0; i < Keys.size(); i++) {
             if (((Key) Keys.get(i)).contains(point)) {
                 return (Key) Keys.get(i);
             }
         }
         return null;
     }
	/**
	 * This function turns a key on. Used when animating the note rectangles in Interface
	 * @param keyPitch is the corresponding note's pitch
	 * @param velocity is the corresponding note's velocity
	 * @param channel is the corresponding note's channel (used for color)
	 */
	public void NoteStateOn(int keyPitch,int velocity,int channel){
		if(keysByPitch.containsKey(keyPitch)){
			Key k = keysByPitch.get(keyPitch);
			if(k.isNoteOn()){}
			else
			{
				k.setNoteState(true);
			 mChannels[0].noteOn(k.getPitch(),velocity);
			}
			repaint();
		}
	}
	/**
	 * This function turns a key off. Used when animating the note rectangles in Interface
	 * @param keyPitch is the corresponding note's pitch
	 * @param velocity is the corresponding note's velocity
	 * @param channel is the corresponding note's channel (used for color)
	 */
	public void NoteStateOff(int keyPitch,int velocity, int channel){
		if(keysByPitch.containsKey(keyPitch)){
			Key k = keysByPitch.get(keyPitch);
			if(k.isNoteOn()){
				k.setNoteState(false);
				mChannels[0].noteOff(k.getPitch());
				}
			else
			{
			
			}
			repaint();
		}
	}
	/**
	 * Gets a HashMap with all 88 keys that can be accessed by their pitch values.
	 * @return
	 */
	public HashMap<Integer,Key> getKeysByPitch(){
		return keysByPitch;
	}
}

