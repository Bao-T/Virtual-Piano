import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * This Class houses the interface and main functionality of the program. Instructions are initialized here.
 * @author Bao Tran
 * @version 1.00, 24 January 2017
 * @see MidiSequencer
 * @see Note
 * @see KeyboardPanel
 * @see Key
 * @see AnimationPanel
 * @see AnimationRectangles
 */
public class Interface extends JFrame implements Runnable{
	private int speed = 1;
	private Long timeCount = (long) -1;
	private Boolean fileSelected = false, playButton = true;
	private Thread thread = new Thread(this);
	
	private KeyboardPanel keyboard = new KeyboardPanel();
	private AnimationPanel animation = new AnimationPanel();
	private MidiSequencer sequence; //new MidiSequencer(System.getProperty("user.dir") + "\\src\\resource\\This Game.mid");
	
	private HashMap<Integer,Key> keysByPitch;
	private HashMap<Long,ArrayList<Note>> notesByST;
	
	private JPanel East ;
	private JButton play;
	private JSlider speedSlider;
	private JButton load ;
	/**
	 * Constructor
	 * Creates main UI of program including virtual piano, note animation screen, speed slider, and load/play buttons
	 * 
	 */
	public Interface(){
		//Sets up JFrame
		this.setTitle("MIDI Visualizer");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(keyboard, BorderLayout.SOUTH);
        add(animation,BorderLayout.CENTER);
        
        //Creates panel for play, load buttons and slider
        East = new JPanel(new FlowLayout());
        East.setPreferredSize(new Dimension(90,816));

        //Slider for play speed
		
		speedSlider = new JSlider(JSlider.VERTICAL,0,15,0);
		speedSlider.setMajorTickSpacing(5);
		speedSlider.setMinorTickSpacing(1);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent event) {
				// TODO Auto-generated method stub
				speed = ((JSlider)event.getSource()).getValue() + 1;
			}
			
		});
		
		//button to load .mid files
        load = new JButton(new AbstractAction("Load") {
            @Override
            public void actionPerformed(ActionEvent e) {
            	final JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new GridLayout(0, 1));
                FileDialog fd = new FileDialog(frame, "Test", FileDialog.LOAD);

                fd.setDirectory(System.getProperty("user.dir") + "\\src\\resource");
                System.out.println(fd.getDirectory());
                fd.setVisible(true);
               
                if(fd.getFile() != null){
                String path = fd.getDirectory()+fd.getFile();
                System.out.println(path);
                sequence = new MidiSequencer(path);
                keysByPitch = keyboard.getKeysByPitch();
        		notesByST = sequence.getnotesByST();
        		play.setEnabled(true);
        		fileSelected = true;
        		
                }
            }
        });
        load.setPreferredSize(new Dimension(80,160));
        
        //button to play loaded file
        play =new JButton("Play");
        play.setPreferredSize(new Dimension(80,160));
        play.setEnabled(false);
        play.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e){
				play.setEnabled(false);
				load.setEnabled(false);
				thread.start();
			}
				});
        
        //add components and create east panel
        East.add(play);
        East.add(load);
        East.add(speedSlider);
        add(East,BorderLayout.EAST);
        setSize(930,880);
        setResizable(false);
		setVisible(true);

	}

	/**
	 * Thread animates graphics and plays a note when an AnimatedRectangle is within the position to play a tone
	 * @see AnimationRectangles
	 * @see AnimatinPanel
	 */
	@Override
	public void run() {
		while(true){
			
			try {
				Thread.sleep(speed);
			} catch (InterruptedException i) {
				// TODO Auto-generated catch block
				i.printStackTrace();
			}
			animation.repaint();
			timeCount ++;
			
			//if there is a note for the current timeCount, find all in HashMap and create rectangle objects to add to AnimationPanel
			if(notesByST.containsKey(timeCount)){
				for(Note n :notesByST.get(timeCount)){
					//Checks if within range of keys
					if(n.getPitch() >=21 && n.getPitch() <= 108){
						if(n.getVelocity() == 0)
							System.out.println(n.getPitch());
						animation.addrect(n.getPitch(),n.getVelocity(),n.getDuration(),n.getStart(),keysByPitch.get(n.getPitch()).x,n.getChannel());}}}
			
			//For all current rectangles, go through and animate the rectangles
			for ( ArrayList<AnimationRectangles> a: animation.getCurrentRectangles().values())
			{
				for(AnimationRectangles r: a){
					keysByPitch.get(r.getPitch()).currentChannel = r.getcurrentChannel();
					//While r upper bound is within panel range, animate
					if (r.y < 810)
						r.animate();
					//if rectangle bar bottom is past 780 and top != 780
					if (r.y+r.getDuration() >= 780 && r.y < 780){
						if(r.on == false){
							keyboard.NoteStateOn(r.getPitch(), r.getVelocity(),r.getcurrentChannel());
							keysByPitch.get(r.getPitch()).setNoteState(true);
							r.on = true;
						}
						
					}
					if (r.y == 780){

						keysByPitch.get(r.getPitch()).setNoteState(false);
						r.on = false;

					}
				
				
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Interface i = new Interface();

	}
	
}
