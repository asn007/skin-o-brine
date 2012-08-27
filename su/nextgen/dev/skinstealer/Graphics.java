package su.nextgen.dev.skinstealer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	public static JLabel lblStatusWaitingFor;

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

		lblStatusWaitingFor = new JLabel("Status: waiting for user input");
		lblStatusWaitingFor.setBounds(0, 517, 140, 14);
		panel.add(lblStatusWaitingFor);

		txtSkinUrlWill = new JTextField();
		txtSkinUrlWill.setEditable(false);
		txtSkinUrlWill.setText("Skin URL will be here");
		txtSkinUrlWill.setBounds(0, 486, 176, 20);
		panel.add(txtSkinUrlWill);
		txtSkinUrlWill.setColumns(10);

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
						txtSkinUrlWill.setText("");
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
								lblStatusWaitingFor
										.setText("Status: stealing skin...");
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
							lblStatusWaitingFor.setText("Status: skin stolen!");
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
							System.gc();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						txtSkinUrlWill
								.setText("http://s3.amazonaws.com/MinecraftSkins/"
										+ txtEnterNickname.getText() + ".png");
						try {
							File skf = new File(System.getProperty("user.home",
									".")
									+ File.separator
									+ txtEnterNickname.getText() + ".png");
							System.out.println(System.getProperty("user.home",
									"."));
							if (!skf.exists()) {
								skf.createNewFile();
							}
							if (skf.exists()) {
								skf.delete();
								skf.createNewFile();
							}
							skf = null;
							System.gc();
							byte[] skinarray = Utils
									.readBytesFromFile(new File(Utils
											.getWorkingDirectory()
											+ File.separator
											+ txtEnterNickname.getText()
											+ ".png"));
							FileOutputStream fos = new FileOutputStream(
									new File(System.getProperty("user.home",
											".")
											+ File.separator
											+ txtEnterNickname.getText()
											+ ".png"));
							fos.write(skinarray);
							if (fos != null) {
								fos.close();
							}
							System.gc();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out
									.println("^\n| error log. Exiting now...");
							System.exit(0);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out
									.println("^\n| error log. Exiting now...");
							System.exit(0);
						}
						txtEnterNickname.setEnabled(true);
					}
				}.start();

			}

		});

		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(110, 452, 66, 23);
		panel.add(btnReset);
		btnReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				txtEnterNickname.setText("Enter nickname");
				txtSkinUrlWill.setText("Skin URL will be here");
				txtSkinUrlWill.setEditable(false);
				txtEnterNickname.setEnabled(true);
				lblStatusWaitingFor.setText("Status: waiting for user input");
				label.setIcon(null);
				label_2.setIcon(null);
			}

		});
	}
}
