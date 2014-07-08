/**
 * 
 */
package com.bu6ido.bitpower.common;

import java.io.File;
import java.net.InetSocketAddress;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author bu6ido
 *
 */
public class CommonUtils {

	public static String getImagePath()
	{
		String result = "." + File.separator + "res" + File.separator + "bitpower.png";
		return result;
	}
	
	public static String getSettingsPath()
	{
		String result = System.getProperty("user.home") + File.separator + ".bitpower";
		return result;
	}
	
	public static String getDestopPath()
	{
		String result = System.getProperty("user.home") + File.separator + "Desktop";
		return result;
	}
	
    public static String getFileExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
	public static int dialogYesNo(JFrame frame, String title, String message, Object[] options)
	{
		JOptionPane pane = new JOptionPane(message);
		pane.setOptions(options);
		JDialog dialog = pane.createDialog(frame, title);
		dialog.setVisible(true);
		Object obj = pane.getValue();
		for (int i=0; i<options.length; i++)
		{
			if (options[i].equals(obj))
			{
				return i;
			}
		}
		return -1;
	}
	
	public static String address2string(InetSocketAddress isa)
	{
		if (isa == null) return null;
		return isa.getAddress().getHostAddress() + ":" + isa.getPort();
	}
}
