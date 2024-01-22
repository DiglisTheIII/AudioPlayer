package PreSetup;
import javax.swing.JFrame;
import javax.swing.JPanel;

import UI.UIFuncs;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.io.PrintWriter;
import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
public class SetupUI {

	public SetupUI() {
		JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, 434, 261);
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 434, 261);
		panel.setLayout(null);
        frame.add(panel);
		
		JButton btn = new JButton("New button");
		btn.setBounds(149, 109, 89, 23);
		btn.addActionListener(e -> {
			final JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			File chosenFile;
			fc.showOpenDialog(frame);
			
			try {
				chosenFile = fc.getSelectedFile();
				try {
                    initialize(chosenFile);
                    frame.setVisible(false);
                }catch(IOException ea) {

                }
			} catch(Exception ex) {
				
			}
		});
		panel.add(btn);
        frame.setVisible(true);
    }

    public void initialize(File f) throws IOException {
        File paths = new File("UserFiles\\Paths.txt");
        if (!paths.exists()) {
            PrintWriter writer = new PrintWriter(paths, "UTF-8");
            paths.createNewFile();
            writer.write(f.getAbsolutePath());
            writer.close();
        }
    }
}
