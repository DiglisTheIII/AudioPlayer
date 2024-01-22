package UI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import PreSetup.SetupUI;

import java.awt.Font;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JProgressBar;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class UI {
    
    private JFrame frame;
	
	public JButton back;
	public JToggleButton pause;
	public JButton skip;
	public JPanel panel;
	public static JLabel songName = new JLabel("");
	public JProgressBar timeLeftBar;
	public static JLabel timeLeft = new JLabel("");
    public static JLabel totalTime = new JLabel("");
    public JToggleButton shuffle;
    UIFuncs funcs;
    boolean isPaused = false;

    public UI() throws IOException, InterruptedException {
        funcs = new UIFuncs();
        panel = new JPanel();
        back = new JButton("◁");
        pause = new JToggleButton("▷/||");
        skip = new JButton("▷");
        timeLeftBar = new JProgressBar();
        timeLeft = new JLabel("");
        shuffle = new JToggleButton("Shuffle");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
        boolean isShuffled = false;
		frame = new JFrame();
		frame.setBounds(100, 100, 821, 536);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//JPanel panel = new JPanel();
		panel.setBounds(0, 0, 805, 497);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		//JButton back = new JButton("◁");
		back.setBounds(83, 412, 89, 23);
        back.addActionListener(e -> {
            funcs.reverse(UIFuncs.songList);
        });
		panel.add(back);
		
		//JButton pause = new JButton("▷/||");
		pause.setBounds(323, 412, 89, 23);
		ItemListener itemListener = new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();

                if(state == ItemEvent.SELECTED || isShuffled) {
                    funcs.play(UIFuncs.songList, false);
                } else if (state == ItemEvent.SELECTED || !isShuffled) {
                    funcs.shuffle();
                } else {
                    funcs.pause();
                }
            }

        };
        pause.addItemListener(itemListener);
        panel.add(pause);
		
		//JButton skip = new JButton("▷");
		skip.setBounds(590, 412, 89, 23);
        skip.addActionListener(e -> {
            if(!isShuffled) {
                funcs.skip(UIFuncs.songList, UIFuncs.isShuffled);
            } else {
                funcs.shuffle();
            }
        });
		panel.add(skip);
		
		//JProgressBar timeleftBar = new JProgressBar();
		timeLeftBar.setBounds(216, 362, 317, 14);
		panel.add(timeLeftBar);
		
		//JLabel timeLeft = new JLabel("");
		timeLeft.setBounds(543, 362, 46, 14);
		panel.add(timeLeft);
		
		//JLabel songName = new JLabel("");
		songName.setHorizontalAlignment(SwingConstants.CENTER);
		songName.setBounds(258, 337, 230, 14);
		panel.add(songName);

        totalTime.setBounds(580, 362, 35, 14);
		panel.add(totalTime);

        shuffle.setBounds(323, 446, 89, 23);
        ItemListener shuffleListener = new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();

                if(state == ItemEvent.SELECTED) {
                    funcs.play(UIFuncs.songList, true);
                    funcs.shuffle();
                } else {
                    funcs.pause();
                    funcs.play(UIFuncs.songList, false);
                }
            }

        };
        shuffle.addItemListener(shuffleListener);
		panel.add(shuffle);
		
		frame.setVisible(true);
	}

    public static void updateSongName(String name) {
        songName.setText(name);
    }

    public static void updateTotalTimeLeft(String time) {
        totalTime.setText(time);
    }

    public static void updateRemainingSongTime(String timeRemaining) {
        timeLeft.setText(timeRemaining);
    }


}
