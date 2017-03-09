import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.print.DocFlavor.URL;
import javax.sound.midi.*;
/**
 * This Class opens a specified midi file and converts it into a ArrayList(ArrayList(Note))  and HashMap( Long,ArrayList( Note)) ;
 * for easily reading note data.
 * @author Bao Tran
 * @version 1.00, 24 January 2017
 * @see Note
 */
public class MidiSequencer {
	  private ArrayList<ArrayList<Note>> tracks;
	  private long maxTick = 0;
	  private HashMap<Long,ArrayList<Note>> notesByST= new HashMap<Long,ArrayList<Note>>();
	  private static final int NOTE_ON = 0x90;
	  private static final int NOTE_OFF = 0x80;



	  /**
	   * Constructor
	   * Iterates through all tracks and midi events to find data for each note.
	   * When all of a note's data is found, create a new note object and add it into the tracks and notesByST data structures
	   * @param path is the directory of the file
	   */
	  public MidiSequencer(String path) {
	    //Notes are stored in this nested array list to mirror the track-note structure (depending on type of midi file)
	    tracks = new ArrayList<ArrayList<Note>>();
	    Track[] trx;
	    System.out.println(path);
	    	String curdir = path;
	      File myMidiFile = new File(curdir);
	      Sequence sequence;
		try {
			sequence = MidiSystem.getSequence(myMidiFile);
	        int trackNumber = 0;
	        for (Track track :  sequence.getTracks()) {
	        	int pitch;
	        	int channel;
	        	int velocity;
	        	long Time;
	            trackNumber++;
	            channel = trackNumber;
	            //System.out.println("Track " + trackNumber + ": size = " + track.size());
	            //System.out.println();
	            ArrayList<Note> t = new ArrayList<Note>();
	            for (int i=0; i < track.size(); i++) { 
	                MidiEvent event = track.get(i);
	                //System.out.print("@" + event.getTick() + " ");
	                Time = event.getTick()/3;
	                MidiMessage message = event.getMessage();
	                if (message instanceof ShortMessage) {
	                    ShortMessage sm = (ShortMessage) message;
	                    //System.out.print("Channel: " + sm.getChannel() + " ");
	                    //System.out.printf("Track " + trackNumber);
	                    if (sm.getCommand() == NOTE_ON) {
	                        int key = sm.getData1();
	                        pitch = key;
	                       
	                       // int octave = (key / 12)-1;
	                        //int note = key % 12;
	                    
	                        velocity = sm.getData2();
	                        if(key <21 || key>109)
	                        System.out.println("Note on, key=" + key + " velocity: " + velocity);
	                        Note n = new Note(Time,channel,velocity,pitch);
	                        t.add(n);
	                        if (Time > maxTick)
	                		{
	                			maxTick = Time;
	                		}

	                    } else if (sm.getCommand() == NOTE_OFF) {
	                        int key = sm.getData1();
	                        pitch = key;
	                       // int octave = (key / 12)-1;
	                       // int note = key % 12;
	                       
	                        velocity = sm.getData2();
	                        if(key <21 || key>109)
	                        System.out.println("Note off, key=" + key + " velocity: " + velocity);
	                        for (Note n : t){
	                        	if (n.getPitch() == pitch && n.endTime == 0)
	                        	{
	                        		n.endTime = Time;
	                        		if (Time > maxTick)
	                        		{
	                        			maxTick = Time;
	                        		}
	                        	}
	                        }
	                        
	                        
	                    } else {
	                       // System.out.println("Command:" + sm.getCommand());
	                    }
	                } else {
	                   // System.out.println("Other message: " + message.getClass());
	                }
	            }
	            tracks.add(t);
	            //System.out.println();
	        }
	        sortNotes();
		} catch (InvalidMidiDataException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();
		}
	
	    }
	     
	  /**
	   * Converts the two dimensional ArrayList tracks into a two dimensional HashMap where the key is a note's startTime
	   */
	  public void sortNotes(){
		  for (ArrayList<Note> a : tracks){
			  for (Note n : a){
				  if(notesByST.containsKey(n.getStart()))
					  notesByST.get(n.getStart()).add(n);
				  else
				  {
					  ArrayList<Note> an = new ArrayList<Note>();
					  an.add(n);
					  notesByST.put(n.getStart(), an);
					  
				  }
			  }
		  }
	  }
	  int numTracks() {
	    return tracks.size();
	  }
	  /**
	   * Returns the HashMap of notes sorted by startTime
	   * @return notesByST
	   */
	  public  HashMap<Long,ArrayList<Note>> getnotesByST(){return notesByST;}
	}
