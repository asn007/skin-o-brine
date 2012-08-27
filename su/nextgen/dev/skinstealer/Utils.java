package su.nextgen.dev.skinstealer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class Utils {
	
	public static boolean loaded = false;
	public static Locales sysloc = Locales.oth;
	public static Locale locale = Locale.getDefault();
	public static File workDir = null;
	
	public static void setLocale()
	{
		if(locale.getDisplayCountry().equalsIgnoreCase("россия") || locale.getDisplayCountry().equalsIgnoreCase("russia") || locale.getDisplayCountry().equalsIgnoreCase("российская федерация") || locale.getDisplayCountry().equals("russian federation")) Utils.sysloc = Locales.rus;
		else Utils.sysloc = Locales.oth;
	}
	
	public static BufferedImage parseSkin(File skinURL) {
		BufferedImage result = new BufferedImage(128, 256, 2);
		try {

			BufferedImage skinIMG = ImageIO.read(skinURL);

			Graphics g = result.getGraphics();
			int w = skinIMG.getWidth() / 64;
			int h = skinIMG.getHeight() / 32;

			g.drawImage(skinIMG.getSubimage(w * 8, h * 8, w * 8, h * 8), 32, 0,
					64, 64, null); // Голова
			g.drawImage(skinIMG.getSubimage(w * 20, h * 20, w * 8, h * 12), 32,
					64, 64, 96, null); // Тело
			g.drawImage(skinIMG.getSubimage(w * 44, h * 20, w * 4, h * 12), 0,
					64, 32, 96, null); // Левая рука
			g.drawImage(skinIMG.getSubimage(w * 44, h * 20, w * 4, h * 12), 96,
					64, 32, 96, null); // Правая рука
			g.drawImage(skinIMG.getSubimage(w * 4, h * 20, w * 4, h * 12), 32,
					160, 32, 96, null); // Левая нога
			g.drawImage(skinIMG.getSubimage(w * 4, h * 20, w * 4, h * 12), 64,
					160, 32, 96, null); // Правая нога
			g.drawImage(skinIMG.getSubimage(w * 40, h * 8, w * 8, h * 8), 32,
					0, 64, 64, null); // Голова

			return result;

		} catch (Exception e) {
			System.err.println("Error occured while trying to parse skin!");
			return null;
		}
	}
	
	public static void getSkin(URL url, String nickname)
	{
		loaded = false;
		File f2 = new File(Utils.getWorkingDirectory() + File.separator + nickname + ".png");
		try {
		URLConnection connection = url.openConnection();

		long down = connection.getContentLength();

		long downm = f2.length();

		
			if(downm != down){
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				
				BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
				
				FileOutputStream fw = new FileOutputStream(f2);
				
				byte[] b = new byte[1024]; 
				int count = 0;

					while ((count=bis.read(b)) != -1) {
						
					fw.write(b,0,count);
							
					
					
					

					}
				fw.close();
			}
		
			else loaded = true;
		}
			catch(Exception ex)
			{
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null,
					    "Error occured while fetching skin. Contact author via e-mail: asn007@nextgen.su \nProgram will exit now... Stacktrace:\n" + ex.toString(),
					    "Inane error",
					    JOptionPane.ERROR_MESSAGE);
			}
			
		} 
	

public static File getWorkingDirectory() {

	workDir = getWorkingDirectory("skin-o-brine");

	return workDir;
}

public static File getWorkingDirectory(String applicationName) {
	String userHome = System.getProperty("user.home", ".");
	File workingDirectory;
	switch (getPlatform().ordinal()) {
	case 0:
	case 1:
		workingDirectory = new File(userHome, '.' + applicationName + '/');
		break;
	case 2:
		String applicationData = System.getenv("APPDATA");
		if (applicationData != null)
			workingDirectory = new File(applicationData, "."
					+ applicationName + '/');
		else
			workingDirectory = new File(userHome,
					'.' + applicationName + '/');
		break;
	case 3:
		workingDirectory = new File(userHome,
				"Library/Application Support/" + applicationName);
		break;
	default:
		workingDirectory = new File(userHome, applicationName + '/');
	}
	if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs()))
		throw new RuntimeException(
				"The working directory could not be created: "
						+ workingDirectory);
	return workingDirectory;
}

public static OS getPlatform() {
	String osName = System.getProperty("os.name").toLowerCase();
	if (osName.contains("win"))
		return OS.windows;
	if (osName.contains("mac"))
		return OS.macos;
	if (osName.contains("solaris"))
		return OS.solaris;
	if (osName.contains("sunos"))
		return OS.solaris;
	if (osName.contains("linux"))
		return OS.linux;
	if (osName.contains("unix"))
		return OS.linux;
	return OS.unknown;
}

public static enum OS {
	linux, solaris, windows, macos, unknown;
}
}
