/**
 * 
 */
package com.bu6ido.bitpower.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bu6ido.bitpower.common.CommonUtils;

/**
 * @author bu6ido
 *
 */
public class PanelAbout extends JPanel 
{
	private static final long serialVersionUID = -1233554102324098921L;
	
	protected Image imgProduct;
	protected JLabel lblImage;
	protected JLabel lblApp;
	protected JLabel lblBasedOn;
	protected JLabel lblVersion;
	protected JLabel lblCopyright;
	
	protected JButton btnOK;
	
	public PanelAbout()
	{
		setBorder(BorderFactory.createTitledBorder("BitPower"));
		
		setLayout(new GridBagLayout());
		
		try
		{
			imgProduct = ImageIO.read(new File(CommonUtils.getImagePath()));
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		lblImage = new JLabel(new ImageIcon(imgProduct));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(lblImage, gbc);
		
		lblApp = new JLabel("<html><i>BitPower - a simple Bittorent client</i></html>");
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(lblApp, gbc);
	
		lblBasedOn = new JLabel("<html>Based on <a href=''>http://code.google.com/p/bobbin</a> library</html>");
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth= 3;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(lblBasedOn, gbc);
		
		lblVersion = new JLabel("Version 1.0");
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 1;
		gbc.gridy = 3;
		add(lblVersion, gbc);
		
	
		lblCopyright = new JLabel("<html>Copyright &copy; 2012 Ivailo Georgiev</html>");
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.gridx = 1;
		gbc.gridy = 4;
		add(lblCopyright, gbc);
		
		btnOK = new JButton("OK");
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 1;
		gbc.gridy = 5;
		add(btnOK, gbc);
	}
	
	public JButton getBtnOK()
	{
		return btnOK;
	}
}
