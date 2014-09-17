package com.cloudfoundry.dropboxer;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.cloudfoundry.dropboxer.api.DropBoxApi;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;

public class MainWindow {

	private JFrame frame;
	private JList<String> list;
	private DefaultListModel<String> listModel;
	private ArrayList<DbxEntry> entries;

	private JButton btnUploadfiles;
	private JButton btnShowFiles;
	private JButton btnDeleteFile;
	private JLabel lblInfoLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		listModel = new DefaultListModel<String>();

		btnUploadfiles = new JButton("Upload Files");
		btnUploadfiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					lblInfoLabel.setText("You chose to load this file: "
							+ fc.getSelectedFile().getAbsolutePath());

					DropBoxApi.getInstance().uploadFile(fc.getSelectedFile());
					refreshListFiles();
				}

			}
		});
		btnUploadfiles.setBounds(10, 227, 89, 23);
		frame.getContentPane().add(btnUploadfiles);

		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setVisibleRowCount(5);
		JScrollPane listScrollPane = new JScrollPane(list);
		listScrollPane.setBounds(10, 10, 300, 200);

		frame.getContentPane().add(listScrollPane);

		btnShowFiles = new JButton("Show files");
		btnShowFiles.setBounds(114, 227, 89, 23);
		frame.getContentPane().add(btnShowFiles);

		btnDeleteFile = new JButton("Delete file");
		btnDeleteFile.setBounds(213, 227, 89, 23);
		frame.getContentPane().add(btnDeleteFile);
		btnDeleteFile.addActionListener(new DeletFileListener());

		lblInfoLabel = new JLabel();
		lblInfoLabel.setBounds(334, 12, 200, 200);
		frame.getContentPane().add(lblInfoLabel);

		refreshListFiles();
	}

	private void refreshListFiles() {
		try {
			if (listModel.isEmpty() == false) {
				listModel.removeAllElements();
			}

			entries = DropBoxApi.getInstance().getFilesFromFolder("/");
			if (entries != null) {
				btnDeleteFile.setEnabled(true);
				for (DbxEntry child : entries) {
					listModel.addElement(child.name);
				}
			} else {
				btnDeleteFile.setEnabled(false);
			}
			list.setModel(listModel);

		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class DeletFileListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			int index = list.getSelectedIndex();
			DbxEntry file = entries.get(index);

			if (DropBoxApi.getInstance().deleteEntry(file)) {
				String text = lblInfoLabel.getText();

				lblInfoLabel.setText(String.format(
						"File %s was succefully deleted ", file.name));
				lblInfoLabel.setSize(lblInfoLabel.getPreferredSize());

				// Refreshing list
				refreshListFiles();

			}
		}
	}
}
