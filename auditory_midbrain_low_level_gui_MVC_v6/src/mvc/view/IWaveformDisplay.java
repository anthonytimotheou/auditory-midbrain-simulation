package mvc.view;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;


/**
 * View class which represents a waveform and display it in its panel.  
 * 
 * @author anthony timotheou
 *
 */
public class IWaveformDisplay extends JPanel {
	private static final long serialVersionUID = 1L; // why warning if not here?
	double[] displayCoordinates; // = new double[119]; // initial width
	int width;
	int height;
	String title = "";
	
	/**
	 * Constructor sets title of the waveform to display and its own default colour. 
	 * @param title
	 */
	public IWaveformDisplay(String title) {
		setForeground(Color.BLACK);
		setBackground(Color.WHITE);
		this.title = title;
	}
	
	/**
	 * Set the data of the waveform which automatically refreshes to display it. 
	 * 
	 * @param audioData
	 */
	public void setData(double[] audioData) {
		this.width = this.getWidth();
		this.height = this.getHeight();
		displayCoordinates = new double[width];
		
		int nSamplesPerPixel = audioData.length / width;
		
		for (int i = 0; i < width; i++) {
			double nValue = 0.0f;
			for (int j = 0; j < nSamplesPerPixel; j++) {
				nValue += (Math.abs(audioData[i * nSamplesPerPixel + j]) / 256.0);
			}
			nValue /= nSamplesPerPixel;
			displayCoordinates[i] = nValue;
		}
		
		repaint(); // refresh to show data
	}

	@Override
	/**
	 * paint method called to draw the object by Java. 
	 */
	public void paint(Graphics g) {
		super.paint(g);		
        g.drawBytes(title.getBytes(), 0, title.length(), 5, 15); // draw title
		
		if (width > 0 && height > 0) {
			for (int i = 0; i < width; i++) {
				int value = (int) (displayCoordinates[i] * height);
				int y1 = (height - 2 * value) / 2;
				int y2 = y1 + 2 * value;
				g.drawLine(i, y1, i, y2);
			}	        
		}
		
		
	}
	
}