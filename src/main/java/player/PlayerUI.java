package player;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import app.App;

public class PlayerUI {
	private PlayerAdapter playerAdapter;
	private JTable playlistTable;
	private JTextField distext;
	private JPanel mainpanel;
	private String currArtist;
	private App app;

	// Song data: {Artist, Song Title}
	private String[][] songs = { { "Metallica", "One" }, { "Cher", "Believe" }, { "U2", "Elevation" }};
	private String[] columnNames = { "Artist", "Song " };

	private Container createContentPane() {
		mainpanel = new JPanel();
		mainpanel.setLayout(null);

		JLabel playlabel = new JLabel("Now playing:");
		mainpanel.add(playlabel);
		playlabel.setBounds(20, 10, 80, 20);

		JLabel listlabel = new JLabel("Playlist:");
		mainpanel.add(listlabel);
		listlabel.setBounds(20, 38, 100, 20);

		DefaultTableModel tableModel = new DefaultTableModel(songs, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		playlistTable = new JTable(tableModel);
		playlistTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playlistTable.setRowSelectionInterval(0, 0); // select the first element
		getArtistName();

		mainpanel.add(playlistTable);
		playerAdapter.setCurrentArtist(currArtist);

		distext = new JTextField(currArtist);
		distext.setEnabled(false);
		mainpanel.add(distext);
		distext.setBounds(100, 10, 180, 20);

		JScrollPane tableScrollPane = new JScrollPane(playlistTable);
		mainpanel.add(tableScrollPane);
		tableScrollPane.setBounds(20, 60, 260, 200);

		playlistTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (!event.getValueIsAdjusting()) {
					getArtistName();
					distext.setText(currArtist);
					playerAdapter.setCurrentArtist(currArtist);
				}
			}
		});

		return mainpanel;
	}

	private void getArtistName() {
		int selectedRow = playlistTable.getSelectedRow();
		currArtist = (String) playlistTable.getValueAt(selectedRow, 0);
	}

	private PlayerUI(App app) {
		this.playerAdapter = new PlayerAdapter();
		this.app = app;
	}

	public static PlayerUI createAndShowGUI(App app) {
		JFrame frame = new JFrame("MusicFone Player");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PlayerUI player = new PlayerUI(app);
		frame.setContentPane(player.createContentPane());
		frame.setBounds(0, 10, 313, 300);
		frame.setVisible(true);
		return player;
	}

}
