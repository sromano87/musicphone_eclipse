package recommender;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import app.App;
import commons.dataClasses.Destination;
import commons.dataClasses.Recommendation;
import dataConnectors.LastFmXmlConnector;

public class RecommenderUI implements ActionListener {

	private JPanel panel;
	private RecommenderAdapter ra;

	private JTable artistTable;
	private JTable concertTable;
	private JTable selectedTable;
	private JTable itineraryTable;

	private DefaultTableModel artistTableModel;
	private DefaultTableModel concertTableModel;
	private DefaultTableModel selectedTableModel;
	private DefaultTableModel itineraryTableModel;

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
	private static DecimalFormat simpleDoubleFormat = new DecimalFormat("0.00");

	private App app;

	String artist;

	private RecommenderUI(App app) {
		this.ra = new RecommenderAdapter(new LastFmXmlConnector());
		this.app = app;
	}

	private Container createContentPane() {
		panel = new JPanel();
		panel.setLayout(null);

		artistTableModel = new DefaultTableModel(new String[] { "Artist", "Fan Count" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		artistTable = new JTable(artistTableModel);
		artistTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		artistTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (!event.getValueIsAdjusting()) {
					concertTableModel.setRowCount(0);
					int selectedRow = artistTable.getSelectedRow();
					if (selectedRow != -1) {
						artist = (String) artistTable.getValueAt(selectedRow, 0);
						List<Destination> destinations = ra.getRecommender().getDestinationsForArtists(artist);
						for (int i = 0; i < destinations.size(); i++) {
							Destination destination = destinations.get(i);
							concertTableModel
									.addRow(new String[] { destination.getVenue() + ", " + destination.getCity(),
											simpleDateFormat.format(destination.getStartDate()) });
						}
					}
				}
			}
		});

		JScrollPane artistScrollPane = new JScrollPane(artistTable);
		artistScrollPane.setBounds(20, 50, 350, 250);
		panel.add(artistScrollPane);

		concertTableModel = new DefaultTableModel(new String[] { "Venue", "Date" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		concertTable = new JTable(concertTableModel);
		concertTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane concertScrollPane = new JScrollPane(concertTable);
		concertScrollPane.setBounds(20, 330, 350, 150);
		panel.add(concertScrollPane);

		JLabel concert = new JLabel("Artist's Concerts:");
		concert.setBounds(20, 305, 300, 25);
		panel.add(concert);

		selectedTableModel = new DefaultTableModel(new String[] { "Selected Artist" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		selectedTable = new JTable(selectedTableModel);
		selectedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane selectedScrollPane = new JScrollPane(selectedTable);
		selectedScrollPane.setBounds(624, 50, 175, 250);
		panel.add(selectedScrollPane);

		itineraryTableModel = new DefaultTableModel(new String[] { "Artist", "City", "Date", "Distance" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		itineraryTable = new JTable(itineraryTableModel);
		itineraryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane itineraryScrollPane = new JScrollPane(itineraryTable);
		itineraryScrollPane.setBounds(380, 330, 490, 150);
		panel.add(itineraryScrollPane);

		JLabel itinerary = new JLabel("Trip Itinerary:");
		itinerary.setBounds(380, 305, 300, 25);
		panel.add(itinerary);

		JButton getBtn = new JButton("Get Recommendations");
		getBtn.setBounds(20, 10, 170, 30);
		getBtn.addActionListener(this);
		panel.add(getBtn);

		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setBounds(200, 10, 80, 30);
		cancelBtn.addActionListener(this);
		panel.add(cancelBtn);

		JButton addBtn = new JButton("Add >");
		addBtn.setBounds(510, 130, 100, 25);
		addBtn.addActionListener(this);
		panel.add(addBtn);

		JButton removeBtn = new JButton("Remove <");
		removeBtn.setBounds(510, 160, 100, 25);
		removeBtn.addActionListener(this);
		panel.add(removeBtn);

		JButton clearBtn = new JButton("Clear");
		clearBtn.setBounds(510, 190, 100, 25);
		clearBtn.addActionListener(this);
		panel.add(clearBtn);

		return panel;
	}

	public static RecommenderUI createAndShowGUI(App app) {
		JFrame frame = new JFrame("MusicFone Recommender");
		frame.setBounds(380, 10, 900, 522);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		RecommenderUI recommenderUI = new RecommenderUI(app);
		frame.setContentPane(recommenderUI.createContentPane());
		frame.setVisible(true);
		return recommenderUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("Get Recommendations")) {
			artistTableModel.setRowCount(0);
			try {
				List<Recommendation> recs = ra.getRecommender().getRecommendations();
				for (Recommendation rec : recs) {
					artistTableModel.addRow(new Object[] { rec.getArtist(), rec.getFanCount() });
				}
				if (!recs.isEmpty()) {
					artistTable.setRowSelectionInterval(0, 0); // select the first element
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		if (command.equals("Add >")) {
			int row = artistTable.getSelectedRow();
			if (row != -1) {
				String artist = (String) artistTableModel.getValueAt(row, 0);
				
				boolean found = false;
				int i = 0;
				while (i > selectedTableModel.getRowCount() && !found) {
					String selectedArtist = (String) selectedTableModel.getValueAt(i, 0);
					if (artist.equalsIgnoreCase(selectedArtist)) {
						found = true;
					}
					i++;
				}

				if (!found) {
					selectedTableModel.addRow(new String[] { artist });
					selectedTable.setRowSelectionInterval(selectedTableModel.getRowCount()-1, selectedTableModel.getRowCount()-1); // select the last element
					updateItinerary();
				}
			}
		}

		if (command.equals("Remove <")) {
			int row = selectedTable.getSelectedRow();
			if (row != -1) {
				selectedTableModel.removeRow(row);
				if (selectedTableModel.getRowCount() > 0) {
					selectedTable.setRowSelectionInterval(selectedTableModel.getRowCount()-1, selectedTableModel.getRowCount()-1); // select the last element
				}
			}
		}

		if (command.equals("Clear")) {
			selectedTableModel.setRowCount(0);
			itineraryTableModel.setRowCount(0);
		}

		if (command.equals("Cancel")) {
			artistTableModel.setRowCount(0);
			concertTableModel.setRowCount(0);
			selectedTableModel.setRowCount(0);
			itineraryTableModel.setRowCount(0);
		}
	}

	public void updateItinerary() {
		itineraryTableModel.setRowCount(0);
		List<String> artistList = new ArrayList<String>();
		for (int i = 0; i < selectedTableModel.getRowCount(); i++) {
			String artist = (String) selectedTableModel.getValueAt(i, 0);
			artistList.add(artist);
		}
		List<Destination> destinations = ra.getRecommender().buildItineraryForArtists(artistList);
		for (int i = 0; i < destinations.size(); i++) {
			Destination destination = destinations.get(i);
			String artist = destination.getArtist();
			String city = destination.getCity();
			Date date = destination.getStartDate();
			double distance = destination.getDistance();
			String unit = destination.getDistanceUnits();
			itineraryTableModel.addRow(new Object[] { artist, city, simpleDateFormat.format(date),
					simpleDoubleFormat.format(distance) + unit });
		}
	}
}
