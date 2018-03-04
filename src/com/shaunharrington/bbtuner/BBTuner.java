package com.shaunharrington.bbtuner;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ComponentListener;
import java.text.NumberFormat;
import java.awt.Component;
import java.awt.Font;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.ImageIcon;

public class BBTuner extends JFrame implements WindowListener, ComponentListener {

   public JLabel labelPercentOff;

   public JLabel labelNote;

   public JLabel labelFreq;

   private static String fontFile = "fonts/bbtuner.ttf";

   private static String starFile = "images/sofindicator_off.jpg";

   private static String starFile2 = "images/tune_indicator_off.jpg";

   private static String starFile3 = "images/sofindicator_on.jpg";

   private static String starFile4 = "images/tune_indicator_on.jpg";

   private static String[] refFreqs = { "430", "431", "432", "433", "434",
    "435", "436", "437", "438", "439", "440", "441", "442", "443", "444", "445",
    "446", "447", "448", "449", "450" };

   private static String[] instKeys = { "A", "Bb/A#", "B", "C", "Db/C#", "D",
    "Eb/D#", "E", "F", "Gb/F#", "G", "Ab/G#" };

   private static int refFreq = 440;

	private static int instKey = 3;

   private float m_fPercent = 0.0f;

   private static float g_fMargin = 3.0f;

	private AudioFormat audioFormat;

	private TargetDataLine targetDataLine;

	private Font fontBBTuner;

	private JLabel labelFlat;

	private JLabel labelSharp;

	private TunerDSP tunerDSP = new TunerDSP();

	private JComboBox comboRefFreq = new JComboBox(refFreqs);

	private JComboBox comboInstKey = new JComboBox(instKeys);

   private JPanel panelLightIndicator;

   private JLabel labelImage1;

   private JLabel labelImage2;

   private JLabel labelImage3;

   private JLabel labelImage4;

   private JLabel labelImage5;

   private JLabel labelImage6;

   private JLabel labelImage7;

   private JLabel labelImage8;

   private JLabel labelImage9;

   private ImageIcon imageIconRedOff;

   private ImageIcon imageIconGreenOff;

   private ImageIcon imageIconRedOn;

   private ImageIcon imageIconGreenOn;

	public BBTuner() throws Exception {
		super("BB-Tuner by Shaun Harrington");

		initComponents();
	}

	private void captureAudio() {
		try {
			// Get things set up for capture
			audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(
					TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

			// Create a thread to capture the microphone
			// data into an audio file and start the
			// thread running. It will run until the
			// Stop button is clicked. This method
			// will return after starting the thread.
			new CaptureThread().start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}// end catch
	}// end captureAudio method

	private AudioFormat getAudioFormat() {
		float sampleRate = 44100.0F;
		// 8000,11025,16000,22050,44100
		int sampleSizeInBits = 16;
		// 8,16
		int channels = 1;
		// 1,2
		boolean signed = true;
		// true,false
		boolean bigEndian = false;
		// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}// end getAudioFormat

	class CaptureThread extends Thread {
		public boolean stop = false;

		public void run() {

			try {
				targetDataLine.open(audioFormat, (44100 * 2) * Short.SIZE);
				targetDataLine.start();
				tunerDSP.SetAudioInput(targetDataLine);
			} catch (Exception e) {
				e.printStackTrace();
			}// end catch
			try {
				while (!stop) {
					tunerDSP.ComputeAudio();
					sleep(200);
				}
				;
			} catch (Exception e) {
				e.printStackTrace();
			}// end catch
		}// end run
	}// end inner class CaptureThread

	private static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = BBTuner.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	private void initComponents() throws Exception {
		addWindowListener(this);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - 280) / 2,
				((screenSize.height - 180) / 2) + 45, 280, 180);

      GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.CENTER;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;

		imageIconRedOff = createImageIcon(starFile);
		imageIconGreenOff = createImageIcon(starFile2);
		imageIconRedOn = createImageIcon(starFile3);
		imageIconGreenOn = createImageIcon(starFile4);

		labelImage1 = new JLabel(imageIconRedOff);
		labelImage2 = new JLabel(imageIconRedOff);
		labelImage3 = new JLabel(imageIconRedOff);
		labelImage4 = new JLabel(imageIconRedOff);
		labelImage5 = new JLabel(imageIconGreenOff);
		labelImage6 = new JLabel(imageIconRedOff);
		labelImage7 = new JLabel(imageIconRedOff);
		labelImage8 = new JLabel(imageIconRedOff);
		labelImage9 = new JLabel(imageIconRedOff);
		labelImage1.setHorizontalAlignment(JLabel.CENTER);
		labelImage1.setVerticalAlignment(JLabel.CENTER);
		labelImage2.setHorizontalAlignment(JLabel.CENTER);
		labelImage2.setVerticalAlignment(JLabel.CENTER);
		labelImage3.setHorizontalAlignment(JLabel.CENTER);
		labelImage3.setVerticalAlignment(JLabel.CENTER);
		labelImage4.setHorizontalAlignment(JLabel.CENTER);
		labelImage4.setVerticalAlignment(JLabel.CENTER);
		labelImage5.setHorizontalAlignment(JLabel.CENTER);
		labelImage5.setVerticalAlignment(JLabel.CENTER);
		labelImage6.setHorizontalAlignment(JLabel.CENTER);
		labelImage6.setVerticalAlignment(JLabel.CENTER);
		labelImage7.setHorizontalAlignment(JLabel.CENTER);
		labelImage7.setVerticalAlignment(JLabel.CENTER);
		labelImage8.setHorizontalAlignment(JLabel.CENTER);
		labelImage8.setVerticalAlignment(JLabel.CENTER);
		labelImage9.setHorizontalAlignment(JLabel.CENTER);
		labelImage9.setVerticalAlignment(JLabel.CENTER);

		tunerDSP.Init(this);
		tunerDSP.SetOptions(refFreq, instKey);
		tunerDSP.SetAudioInput(targetDataLine);

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		String strRefFreq = nf.format(refFreq);
		comboRefFreq.setSelectedItem(strRefFreq);
		comboInstKey.setSelectedIndex(instKey);
		comboRefFreq.setToolTipText("Reference Frequency");
		comboInstKey.setToolTipText("Instrument Key");
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = -1;
      JPanel mToolbar = new JPanel();
		mToolbar.setLayout(new GridBagLayout());
		mToolbar.setMinimumSize(new Dimension(200, 50));
		mToolbar.add(comboRefFreq, gridBagConstraints);
		mToolbar.add(comboInstKey, gridBagConstraints);
		getContentPane().add(mToolbar, BorderLayout.NORTH);
		comboRefFreq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refFreqActionPerformed(e);
			}
		});
		comboInstKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instKeyActionPerformed(e);
			}
		});

		labelPercentOff = new JLabel();
		labelNote = new JLabel();
		labelFreq = new JLabel();
		labelPercentOff.setHorizontalAlignment(JLabel.CENTER);
		labelNote.setHorizontalAlignment(JLabel.CENTER);
		labelFreq.setHorizontalAlignment(JLabel.CENTER);
		labelPercentOff.setVerticalAlignment(JLabel.CENTER);
		labelNote.setVerticalAlignment(JLabel.CENTER);
		labelFreq.setVerticalAlignment(JLabel.CENTER);

      JPanel mDatabar = new JPanel();
		mDatabar.setLayout(new GridLayout());
		mDatabar.setMinimumSize(new Dimension(200, 50));

		mDatabar.add(labelPercentOff);
		mDatabar.add(labelNote);
		mDatabar.add(labelFreq);

      JPanel mDisplay = new JPanel();
		mDisplay.setLayout(new BorderLayout());
		mDisplay.setMinimumSize(new Dimension(200, 50));
		getContentPane().add(mDisplay, BorderLayout.CENTER);
		mDisplay.add(mDatabar, BorderLayout.NORTH);

		panelLightIndicator = new JPanel();
		mDisplay.add(panelLightIndicator, BorderLayout.CENTER);
		panelLightIndicator.setLayout(new GridLayout(1, 9));
		panelLightIndicator.addComponentListener(this);

		labelFlat = new JLabel("H");
		labelFlat.setVerticalAlignment(JLabel.CENTER);
		labelFlat.setHorizontalAlignment(JLabel.CENTER);
		panelLightIndicator.add(labelFlat);

		labelImage1.setVerticalAlignment(JLabel.CENTER);
		labelImage1.setHorizontalAlignment(JLabel.CENTER);
		panelLightIndicator.add(labelImage1);
		labelImage2.setVerticalAlignment(JLabel.CENTER);
		labelImage2.setHorizontalAlignment(JLabel.CENTER);
		panelLightIndicator.add(labelImage2);
		labelImage3.setVerticalAlignment(JLabel.CENTER);
		labelImage3.setHorizontalAlignment(JLabel.CENTER);
		panelLightIndicator.add(labelImage3);
		labelImage4.setVerticalAlignment(JLabel.CENTER);
		labelImage4.setHorizontalAlignment(JLabel.CENTER);
		panelLightIndicator.add(labelImage4);
		labelImage5.setVerticalAlignment(JLabel.CENTER);
		labelImage5.setHorizontalAlignment(JLabel.CENTER);
		panelLightIndicator.add(labelImage5);
		labelImage6.setVerticalAlignment(JLabel.CENTER);
		labelImage6.setHorizontalAlignment(JLabel.CENTER);
		panelLightIndicator.add(labelImage6);
		labelImage7.setVerticalAlignment(JLabel.CENTER);
		labelImage7.setHorizontalAlignment(JLabel.CENTER);
		panelLightIndicator.add(labelImage7);
		labelImage8.setVerticalAlignment(JLabel.CENTER);
		labelImage8.setHorizontalAlignment(JLabel.CENTER);
		panelLightIndicator.add(labelImage8);
		labelImage9.setVerticalAlignment(JLabel.CENTER);
		labelImage9.setHorizontalAlignment(JLabel.CENTER);
		panelLightIndicator.add(labelImage9);

		labelSharp = new JLabel("I");
		labelSharp.setVerticalAlignment(JLabel.CENTER);
		labelSharp.setHorizontalAlignment(JLabel.CENTER);
		panelLightIndicator.add(labelSharp);

		java.net.URL imgURL = BBTuner.class.getResource(fontFile);
		fontBBTuner = Font.createFont(Font.TRUETYPE_FONT, imgURL.openStream());
		labelNote.setText("-");
		captureAudio();
	}

	protected void refFreqActionPerformed(ActionEvent e) {
		Object source = e.getSource();
		String stringVal = comboRefFreq.getSelectedItem().toString();
		try {
			refFreq = Integer.parseInt(stringVal);
		} catch (Exception exc) {
			return;
		}
		tunerDSP.SetOptions(refFreq, instKey);
	}

	protected void instKeyActionPerformed(ActionEvent e) {
		Object source = e.getSource();
		instKey = comboInstKey.getSelectedIndex();
		tunerDSP.SetOptions(refFreq, instKey);
	}

	public void offDisplay() {
		m_fPercent = 0.0f;
		labelImage1.setIcon(imageIconRedOff);
		labelImage2.setIcon(imageIconRedOff);
		labelImage3.setIcon(imageIconRedOff);
		labelImage4.setIcon(imageIconRedOff);
		labelImage5.setIcon(imageIconGreenOff);
		labelImage6.setIcon(imageIconRedOff);
		labelImage7.setIcon(imageIconRedOff);
		labelImage8.setIcon(imageIconRedOff);
		labelImage9.setIcon(imageIconRedOff);
		panelLightIndicator.invalidate();
		panelLightIndicator.repaint();
	}

	public void updateDisplay(float fPercent) {
		float fTest = fPercent;
		if (fTest > 50.0)
			fTest = 50.0f;
		else if (fTest < -50.0)
			fTest = -50.0f;
		// Only check whole numbers.
		if (((int) fTest) != ((int) m_fPercent) || (fTest == 0.0f)
				|| (m_fPercent == 0.0f)) {
			m_fPercent = fTest;
		}

		labelImage9.setIcon(imageIconRedOff);
		if (m_fPercent >= g_fMargin) { // Sharp
			labelImage5.setIcon(imageIconGreenOff);
			if (m_fPercent >= 25.0f) {
				labelImage9.setIcon(imageIconRedOn);
				labelImage8.setIcon(imageIconRedOff);
				labelImage7.setIcon(imageIconRedOff);
				labelImage6.setIcon(imageIconRedOff);
				labelImage4.setIcon(imageIconRedOff);
				labelImage3.setIcon(imageIconRedOff);
				labelImage2.setIcon(imageIconRedOff);
				labelImage1.setIcon(imageIconRedOff);
			} else if (m_fPercent >= 12.0f) {
				labelImage8.setIcon(imageIconRedOn);
				labelImage9.setIcon(imageIconRedOff);
				labelImage7.setIcon(imageIconRedOff);
				labelImage6.setIcon(imageIconRedOff);
				labelImage4.setIcon(imageIconRedOff);
				labelImage3.setIcon(imageIconRedOff);
				labelImage2.setIcon(imageIconRedOff);
				labelImage1.setIcon(imageIconRedOff);
			} else if (m_fPercent >= 6.0f) {
				labelImage7.setIcon(imageIconRedOn);
				labelImage9.setIcon(imageIconRedOff);
				labelImage8.setIcon(imageIconRedOff);
				labelImage6.setIcon(imageIconRedOff);
				labelImage4.setIcon(imageIconRedOff);
				labelImage3.setIcon(imageIconRedOff);
				labelImage2.setIcon(imageIconRedOff);
				labelImage1.setIcon(imageIconRedOff);
			} else {
				labelImage6.setIcon(imageIconRedOn);
				labelImage9.setIcon(imageIconRedOff);
				labelImage8.setIcon(imageIconRedOff);
				labelImage7.setIcon(imageIconRedOff);
				labelImage4.setIcon(imageIconRedOff);
				labelImage3.setIcon(imageIconRedOff);
				labelImage2.setIcon(imageIconRedOff);
				labelImage1.setIcon(imageIconRedOff);
			}
		} else if (m_fPercent <= -g_fMargin) {// Flat
			labelImage5.setIcon(imageIconGreenOff);
			if (m_fPercent <= -25.0f) {
				labelImage1.setIcon(imageIconRedOn);
				labelImage9.setIcon(imageIconRedOff);
				labelImage8.setIcon(imageIconRedOff);
				labelImage7.setIcon(imageIconRedOff);
				labelImage6.setIcon(imageIconRedOff);
				labelImage4.setIcon(imageIconRedOff);
				labelImage3.setIcon(imageIconRedOff);
				labelImage2.setIcon(imageIconRedOff);
			} else if (m_fPercent <= -12.0f) {
				labelImage9.setIcon(imageIconRedOff);
				labelImage8.setIcon(imageIconRedOff);
				labelImage7.setIcon(imageIconRedOff);
				labelImage6.setIcon(imageIconRedOff);
				labelImage4.setIcon(imageIconRedOff);
				labelImage3.setIcon(imageIconRedOff);
				labelImage1.setIcon(imageIconRedOff);
				labelImage2.setIcon(imageIconRedOn);
			} else if (m_fPercent <= -6.0f) {
				labelImage9.setIcon(imageIconRedOff);
				labelImage8.setIcon(imageIconRedOff);
				labelImage7.setIcon(imageIconRedOff);
				labelImage6.setIcon(imageIconRedOff);
				labelImage4.setIcon(imageIconRedOff);
				labelImage2.setIcon(imageIconRedOff);
				labelImage1.setIcon(imageIconRedOff);
				labelImage3.setIcon(imageIconRedOn);
			} else {
				labelImage9.setIcon(imageIconRedOff);
				labelImage8.setIcon(imageIconRedOff);
				labelImage7.setIcon(imageIconRedOff);
				labelImage6.setIcon(imageIconRedOff);
				labelImage3.setIcon(imageIconRedOff);
				labelImage2.setIcon(imageIconRedOff);
				labelImage1.setIcon(imageIconRedOff);
				labelImage4.setIcon(imageIconRedOn);
			}
		} else { // In our +- margin.
			labelImage9.setIcon(imageIconRedOff);
			labelImage8.setIcon(imageIconRedOff);
			labelImage7.setIcon(imageIconRedOff);
			labelImage6.setIcon(imageIconRedOff);
			labelImage4.setIcon(imageIconRedOff);
			labelImage3.setIcon(imageIconRedOff);
			labelImage2.setIcon(imageIconRedOff);
			labelImage1.setIcon(imageIconRedOff);
			if ((labelNote.getText() != "") && (labelNote.getText() != "-")) {
				labelImage5.setIcon(imageIconGreenOn);
			} else {
				// m_nStrobeSpeed = STROBE_OFF; // Off.
				labelPercentOff.setText("");
			}
		}
		/*
		 * if( m_nDisplayMode != 1 ) { m_staticFlat4.SetBitmap( *pFlat4 );
		 * m_staticFlat3.SetBitmap( *pFlat3 ); m_staticFlat2.SetBitmap( *pFlat2 );
		 * m_staticFlat1.SetBitmap( *pFlat1 ); m_staticSharp1.SetBitmap(
		 * *pSharp1 ); m_staticSharp2.SetBitmap( *pSharp2 );
		 * m_staticSharp3.SetBitmap( *pSharp3 ); m_staticSharp4.SetBitmap(
		 * *pSharp4 ); } m_staticCtr.SetBitmap( *pCenter );
		 */
		panelLightIndicator.invalidate();
		panelLightIndicator.repaint();
	}

	public void Closing() {
		targetDataLine.stop();
		targetDataLine.close();
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		Component c = e.getComponent();
		int nNewWidth = (c.getSize().width / 11)
				- ((c.getSize().width / 11) / 10);
		int nNewHeight = c.getSize().height - 10;

		Image imageToSize = imageIconRedOff.getImage();
		Image imageResized = imageToSize.getScaledInstance(nNewWidth,
				nNewHeight, Image.SCALE_FAST);
		imageIconRedOff.setImage(imageResized);
		imageToSize = imageIconGreenOff.getImage();
		imageResized = imageToSize.getScaledInstance(nNewWidth, nNewHeight,
				Image.SCALE_FAST);
		imageIconGreenOff.setImage(imageResized);

		imageToSize = imageIconGreenOn.getImage();
		imageResized = imageToSize.getScaledInstance(nNewWidth, nNewHeight,
				Image.SCALE_FAST);
		imageIconGreenOn.setImage(imageResized);

		imageToSize = imageIconRedOn.getImage();
		imageResized = imageToSize.getScaledInstance(nNewWidth, nNewHeight,
				Image.SCALE_FAST);
		imageIconRedOn.setImage(imageResized);

		labelImage1.setIcon(imageIconRedOff);
		labelImage2.setIcon(imageIconRedOff);
		labelImage3.setIcon(imageIconRedOff);
		labelImage4.setIcon(imageIconRedOff);
		labelImage5.setIcon(imageIconGreenOff);
		labelImage6.setIcon(imageIconRedOff);
		labelImage7.setIcon(imageIconRedOff);
		labelImage8.setIcon(imageIconRedOff);
		labelImage9.setIcon(imageIconRedOff);

		int cy = (((c.getSize().height) / 4));
		int cx = (((c.getSize().width) / 20));
		float nFontHeight = cx > cy ? cy : cx;
		Font font2 = fontBBTuner.deriveFont(nFontHeight * 2);
		Font font3 = labelFreq.getFont().deriveFont(nFontHeight);
		labelFreq.setFont(font3);
		labelNote.setFont(font3);
		labelPercentOff.setFont(font3);
		labelSharp.setFont(font2);
		labelFlat.setFont(font2);

		c.invalidate();
		c.repaint();
	}

	public void componentShown(ComponentEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		Closing();
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowGainedFocus(WindowEvent e) {
	}

	public void windowLostFocus(WindowEvent e) {
	}

	public void windowStateChanged(WindowEvent e) {
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e1) {
			e1.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new BBTuner().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
