package com.cloudfoundry.dropboxer;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.cloudfoundry.dropboxer.api.DropBoxApi;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;

public class MainWindow {

	private JFrame frame;
	private JList list;
    private DefaultListModel listModel;
    private ArrayList<DbxEntry> entries;
    
    private JButton btnUploadfiles;
    private JButton btnShowFiles;
    private JButton btnDeleteFile;
    
    
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
		
		

		btnUploadfiles = new JButton("Upload Files");
		btnUploadfiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("You chose to open this file: "
							+ fc.getSelectedFile().getAbsolutePath());
					
					DropBoxApi.getInstance().uploadFile(fc.getSelectedFile());
				}

			}
		});
		btnUploadfiles.setBounds(10, 227, 89, 23);
		frame.getContentPane().add(btnUploadfiles);
			
		listModel = new DefaultListModel<String>();
		try {
			entries = DropBoxApi.getInstance().getFilesFromFolder("/");
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(entries !=null ){
			for (DbxEntry child : entries) {
				listModel.addElement(child.name);
			}
		}
                       
        list =  new JList<String>(listModel);
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

	}
	
	class DeletFileListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            DbxEntry file = entries.get(index);
            
            listModel.remove(index);
 
            int size = listModel.getSize();
 
            if (size == 0) { //Nobody's left, disable firing.
                btnDeleteFile.setEnabled(false);
 
            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }
 
                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }
}
