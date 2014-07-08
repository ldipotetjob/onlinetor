/**
 * 
 */
package com.bu6ido.bitpower.components;

import java.awt.Button;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.bu6ido.bitpower.BitpowerFrame;
import com.bu6ido.bitpower.common.Settings;

/**
 * @author bu6ido
 *
 */
public class PanelSettings extends JPanel
{
	private static final long serialVersionUID = -7323013714734906131L;
	
	protected BitpowerFrame frame;

	protected JTextField tfDownloadDir;
	protected Button btnDownloadDir;
	protected JCheckBox chkAutoDownload;
	protected JButton btnSave;
	
	public PanelSettings(BitpowerFrame frame)
	{
		this.frame = frame;
		setBorder(BorderFactory.createTitledBorder("Settings"));
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		tfDownloadDir = new JTextField(frame.getSettings().getDownloadDir());
		tfDownloadDir.setEditable(false);
		add(tfDownloadDir, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.weightx = 0.1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 2;
		gbc.gridy = 0;
		
		btnDownloadDir = new Button("...");
		add(btnDownloadDir, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		
		chkAutoDownload = new JCheckBox("Start download automatically");
		add(chkAutoDownload, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 1;
		gbc.gridy = 2;
		
		btnSave = new JButton("Save");
		add(btnSave, gbc);
		
		setSettings();
		addListeners();
	}
	
	protected void setSettings()
	{
		if (frame != null)
		{
			Settings set = frame.getSettings();
			chkAutoDownload.setSelected(set.isAutoDownload());
			tfDownloadDir.setText(set.getDownloadDir());
		}
	}
	
	public JButton getBtnSave()
	{
		return btnSave;
	}
	
	protected void addListeners()
	{
		btnDownloadDir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Select download directory:");
				fc.setAcceptAllFileFilterUsed(false);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fc.showOpenDialog(PanelSettings.this) == JFileChooser.APPROVE_OPTION)
				{
					tfDownloadDir.setText(fc.getSelectedFile().toString());
				}
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (frame != null)
				{
					Settings settings = frame.getSettings();
					settings.setAutoDownload(chkAutoDownload.isSelected());
					settings.setDownloadDir(tfDownloadDir.getText());
					frame.saveSettings();
				}
			}
		});
	}
}
