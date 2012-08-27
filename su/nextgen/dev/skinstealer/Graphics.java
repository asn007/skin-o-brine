package su.nextgen.dev.skinstealer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Graphics {

	public static JFrame frame;
	private JTextField txtSkinUrlWill;
	private static JTextField txtEnterNickname;
	public static JLabel label_1;
	public static JLabel label;
	public static JLabel label_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Utils.setLocale();
					if (Utils.sysloc == Locales.oth) {
						System.out.println("SETTING LOCALE: EN_US");
					} else {
						System.out.println("SETTING LOCALE: RU_RU");
					}
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());

					Graphics window = new Graphics();
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
	public Graphics() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		final ImageIcon loading = new ImageIcon(this.getClass().getResource(
				"loader.gif"));

		frame = new JFrame();
		frame.setBounds(100, 100, 194, 569);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblStatusWaitingFor = new JLabel(
				"Status: waiting for user input");
		lblStatusWaitingFor.setBounds(0, 517, 140, 14);
		panel.add(lblStatusWaitingFor);

		txtSkinUrlWill = new JTextField();
		txtSkinUrlWill.setEditable(false);
		txtSkinUrlWill.setText("Skin URL will be here");
		txtSkinUrlWill.setBounds(0, 486, 176, 20);
		panel.add(txtSkinUrlWill);
		txtSkinUrlWill.setColumns(10);

		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(110, 452, 66, 23);
		panel.add(btnReset);

		txtEnterNickname = new JTextField();
		txtEnterNickname.setText("Enter nickname");
		txtEnterNickname.setBounds(0, 421, 176, 20);
		panel.add(txtEnterNickname);
		txtEnterNickname.setColumns(10);

		label = new JLabel("", JLabel.CENTER);
		label.setBounds(23, 93, 128, 256);
		panel.add(label);

		label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(Graphics.class
				.getResource("/su/nextgen/dev/skinstealer/logo.png")));
		label_1.setBounds(0, 0, 176, 82);
		panel.add(label_1);
		JButton btnSteal = new JButton("Steal!");
		btnSteal.setBounds(0, 452, 66, 23);
		panel.add(btnSteal);

		label_2 = new JLabel("");
		label_2.setBounds(56, 378, 64, 32);
		panel.add(label_2);
		btnSteal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {

					URL url;

					@SuppressWarnings("static-access")
					public void run() {
						txtEnterNickname.setEnabled(false);
						try {
							url = new URL(
									"http://s3.amazonaws.com/MinecraftSkins/"
											+ txtEnterNickname.getText()
											+ ".png");
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						new Thread() {
							public void run() {
								Utils.getSkin(url, txtEnterNickname.getText());
							}
						}.start();
						while (!Utils.loaded) {
							Graphics.label.setIcon(loading);
							try {
								Thread.currentThread().sleep(1000L);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						try {
							Graphics.label.setIcon(new ImageIcon(Utils
									.parseSkin(new File(Utils
											.getWorkingDirectory()
											+ File.separator
											+ txtEnterNickname.getText()
											+ ".png"))));

							Graphics.label_2.setIcon(new ImageIcon(ImageIO
									.read(new File(Utils.getWorkingDirectory()
											+ File.separator
											+ txtEnterNickname.getText()
											+ ".png"))));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						txtEnterNickname.setEnabled(true);
					}
				}.start();

			}

		});

	}
}
