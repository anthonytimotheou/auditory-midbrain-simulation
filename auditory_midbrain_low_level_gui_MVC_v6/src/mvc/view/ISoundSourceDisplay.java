package mvc.view;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

/*
 * View which displays the sound source locations. 
 */
public class ISoundSourceDisplay extends JPanel {
  private static final long serialVersionUID = 1L;
	int width = -1;
	int height = -1;
	int numOfSources = -1;
	int[][] sourceCoordinates;
	int points = 6;
	ArrayList<String> pointDegrees = new ArrayList<String>();
	boolean[] firedSoundSource; 
	int noResults = 0;
	
	/**
	 * Create the panel.
	 */
	public ISoundSourceDisplay(int points, int start, int step, int end) {
		setForeground(Color.GREEN);
		setBackground(Color.WHITE);
		this.points = points;
		
		for (int i = start; i <= end; i+=step) {
			pointDegrees.add(0, String.valueOf(i));
		}
		
		firedSoundSource = new boolean[points];
	}
	
	/**
	 * Set no result hit. 
	 */
	public void noResult() {
		noResults++;
		repaint();
	}
	
	/**
	 * fire a sound source. 
	 * 
	 * @param soundSource
	 */
	public void fireSoundSource(int soundSource) {
		firedSoundSource[soundSource] = true;
		repaint();
	}
	
	/**
	 * unfire a sound source.
	 * 
	 * @param soundSource
	 */
	public void unfireSoundSource(int soundSource) {
		firedSoundSource[soundSource] = false;
		repaint();
	}
	
	/**
	 * clear all firing. 
	 */
	public void clearFiring() {
		for(int i = 0; i < firedSoundSource.length; i++) {
			firedSoundSource[i] = false;
		}
		repaint();
	}
	
	/**
	 * Get number of points in the window. 
	 * @return
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * set the number of points in the window. 
	 * 
	 * @param points
	 */
	public void setPoints(int points) {
		this.points = points;
		int m = Math.min(getWidth()/2, getHeight()/2);
        int r = 4 * m / 5;
		setSourceCoordinates(m, r);
	}

	/**
	 * set the source coordinates of points. 
	 * 
	 * @param m
	 * @param r
	 */
	public void setSourceCoordinates(int m, int r) {
		sourceCoordinates = new int[points][2];
        
        for (int i = 0; i <= points-1; i++) {
        	double t = 2 * Math.PI/2*i/(points-1);
            int x = (int) Math.round(getWidth()/2 + r * Math.cos(t));
            int y = (int) Math.round(getHeight()/2 + r * Math.sin(t));
            sourceCoordinates[i][0] = x;
            sourceCoordinates[i][1] = y;	
        }
	}
	
	@Override
	/**
	 * Paint the semi-circle using the method functions of the graphics object to draw
	 * a semi circle and calculate the point in space for each point. 
	 */
	public void paint(Graphics g) {
		super.paint(g);
		width = getWidth();
		height = getHeight();
		
		// draw head
		g.fillRect(width/2-5, height/2-5, 10, 10);
				
		// draw sound sources
		int m = Math.min(getWidth()/2, getHeight()/2);
        int r = 4 * m / 5;
        int r2 = Math.abs(m - r) / 2;
        g.drawOval(getWidth()/2 - r, getHeight()/2 - r, 2 * r, 2 * r);
        
        setSourceCoordinates(m ,r);
        for (int i = 0; i <= points-1; i++) {
        	if (firedSoundSource[i] == true) {
        		g.setColor(Color.RED);
        	} else {
        		g.setColor(Color.BLUE);
        	}
        	
        	g.fillOval(sourceCoordinates[i][0] - r2, sourceCoordinates[i][1] - r2, 3 * r2, 3 * r2);
        	if (firedSoundSource[i] == true) { 
        		g.setColor(Color.BLUE);
        	} else {
        		g.setColor(Color.RED);	
        	}
            g.drawBytes(pointDegrees.get(i).getBytes(), 0, pointDegrees.get(i).getBytes().length, sourceCoordinates[i][0]-r2/3, sourceCoordinates[i][1]+r2/3+5);
        }
        
        String s = "Sound Source(s)";
        g.drawBytes(s.getBytes(), 0, s.length(), 5, 15);

        String ss = "No Result(s): " + noResults;
        g.drawBytes(ss.getBytes(), 0, ss.length(), width-90, height-15);

	}

}
