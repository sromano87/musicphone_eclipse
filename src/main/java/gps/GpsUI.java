package gps;

import javax.swing.*;

import app.App;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import commons.dataClasses.GeoPoint;

public class GpsUI {
	
	private static GpsAdapter gps;
	private GeoPoint nyc;
	private App app;

	private GpsUI(App app) {
		gps = new GpsAdapter();
		nyc = new GeoPoint("42.3482", "75.1890");
		gps.setCurrentPosition(nyc);
		gps.setDistanceUnits("km");
		this.app = app;
	}

	private Container createContentPane() {
		JPanel panel = new JPanel();
		panel.setLayout(null);

		JLabel latlabel = new JLabel("Latitude:");
		panel.add(latlabel);
		latlabel.setBounds(20, 10, 100, 20);

		JTextField Lattextfield = new JTextField();
		Lattextfield.setText(gps.getCurrentPosition().getLatitude());
		Lattextfield.setEnabled(false);
		panel.add(Lattextfield);
		Lattextfield.setBounds(105, 15, 100, 20);

		JLabel longlabel = new JLabel("Longitude:");
		panel.add(longlabel);
		longlabel.setBounds(20, 35, 100, 40);

		JTextField Lontextfield = new JTextField();
		Lontextfield.setText(gps.getCurrentPosition().getLongitude());
		Lontextfield.setEnabled(false);
		panel.add(Lontextfield);
		Lontextfield.setBounds(105, 50, 100, 20);

		JLabel dislabel = new JLabel("Distance Units:");
		panel.add(dislabel);
		dislabel.setBounds(20, 70, 100, 40);

		ButtonGroup btngroup = new ButtonGroup();

		JRadioButton kmbtn = new JRadioButton("km");
		kmbtn.setBounds(105, 80, 45, 20);
		kmbtn.setSelected(true);
		btngroup.add(kmbtn);

		JRadioButton mibtn = new JRadioButton("mi");
		mibtn.setBounds(160, 80, 45, 20);
		btngroup.add(mibtn);

		panel.add(kmbtn);
		panel.add(mibtn);

		// Add action listeners to radio buttons
		ActionListener unitChangeListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (kmbtn.isSelected()) {
					gps.setDistanceUnits("km");
				} else if (!mibtn.isSelected()) {
					gps.setDistanceUnits("mi");
				}
				app.getRecommenderUI().updateItinerary();
			}
		};

		kmbtn.addActionListener(unitChangeListener);
		mibtn.addActionListener(unitChangeListener);

		return panel;
	}

	public static GpsUI createAndShowGUI(App app) {
		JFrame frame = new JFrame("MusicFone GPS");
		GpsUI gpsUI = new GpsUI(app);
		frame.add(gpsUI.createContentPane());
		frame.setBounds(0, 350, 240, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		return gpsUI;
	}

}