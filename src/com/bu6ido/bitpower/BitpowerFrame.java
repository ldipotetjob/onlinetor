/**
 * 
 */
package com.bu6ido.bitpower;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;

import org.itadaki.bobbin.peer.Peer;
import org.itadaki.bobbin.peer.TorrentManager;
import org.itadaki.bobbin.peer.TorrentSetController;
import org.itadaki.bobbin.util.BitField;

import com.bu6ido.bitpower.common.CommonUtils;
import com.bu6ido.bitpower.common.GeoLocationService;
import com.bu6ido.bitpower.common.Settings;
import com.bu6ido.bitpower.common.TorrentFileFilter;
import com.bu6ido.bitpower.components.PanelAbout;
import com.bu6ido.bitpower.components.PanelPieces;
import com.bu6ido.bitpower.components.PanelSettings;
import com.bu6ido.bitpower.models.FileNode;
import com.bu6ido.bitpower.models.FilesTreeModel;
import com.bu6ido.bitpower.models.PeerTableModel;
import com.bu6ido.bitpower.models.Torrent;
import com.bu6ido.bitpower.models.TorrentTableModel;

/**
 * @author bu6ido
 *
 */
public class BitpowerFrame extends JFrame {
	
	private static final long serialVersionUID = 325212131544035196L;

	protected Settings settings;
	
	protected TorrentSetController controller;
	
	protected JFileChooser fc;
	
	protected JTable tblTorrents;
	protected TorrentTableModel mdlTorrents;
	protected int popupX, popupY;
	protected JPopupMenu popupMenuTorrents;
	
	protected JTabbedPane tpDetails;

	protected FilesTreeModel mdlFiles;
	protected JTree trFiles;
	
	protected PeerTableModel mdlPeers;
	protected JTable tblPeers;

	protected PanelPieces pnlPieces;
	
	public BitpowerFrame()
	{
		super("BitPower - version 1.0");
		try
		{
			Image imgProduct = ImageIO.read(new File(CommonUtils.getImagePath()));
			setIconImage(imgProduct);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	
		try
		{
			controller = new TorrentSetController();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			System.exit(1);
		}

		initUIManager();

		fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new TorrentFileFilter());
		
		JMenuBar menuBar = createMenuBar();
		setJMenuBar(menuBar);
		
		Container cp = getContentPane();
		cp.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		mdlTorrents = new TorrentTableModel();
		tblTorrents = new JTable(mdlTorrents);
		tblTorrents.getColumnModel().getColumn(1).setCellRenderer(mdlTorrents.new ProgressBarCellRenderer());
		JScrollPane spTorrents = new JScrollPane(tblTorrents);
		cp.add(spTorrents, gbc);
		
		popupMenuTorrents = createTorrentsPopupMenu();
		tblTorrents.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1)
				{
/*					int row = tblTorrents.rowAtPoint(e.getPoint());
					int column = tblTorrents.columnAtPoint(e.getPoint());
					
					if (!tblTorrents.isRowSelected(row))
					{
						tblTorrents.changeSelection(row, column, false, false);
					}*/
					popupX = e.getX();
					popupY = e.getY();
					popupMenuTorrents.show(e.getComponent(), popupX, popupY);
				}
			}
		});
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1;
		gbc.weighty = 0.5;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		
		tpDetails = new JTabbedPane();
		
		mdlFiles = new FilesTreeModel(this);
		trFiles = new JTree(mdlFiles);
//		trFiles.setCellRenderer(mdlFiles.new ProgressBarTreeCellRenderer());
		mdlPeers = new PeerTableModel();
		tblPeers = new JTable(mdlPeers);
		pnlPieces = new PanelPieces(550);
		
		tpDetails.addTab("Files", new JScrollPane(trFiles));
		tpDetails.addTab("Peers", new JScrollPane(tblPeers));
		tpDetails.addTab("Pieces", new JScrollPane(pnlPieces,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		cp.add(tpDetails, gbc);
		
		addListeners();
		loadSettings();
		GeoLocationService.getInstance();
	}
	
	protected void initUIManager()
	{
		UIManager.put("FileChooser.readOnly", Boolean.TRUE);
	}
	
	protected JMenuBar createMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		menuBar.setSize(getSize().width, 30);
		
		JMenu menuBP = new JMenu("BitPower");
		menuBP.setMnemonic('b');
		menuBar.add(menuBP);
		
		JMenuItem miOpen = new JMenuItem("Open...");
		miOpen.setMnemonic('o');
		menuBP.add(miOpen);
		miOpen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				actionOpenTorrent();
			}
		});
		
		JMenuItem miSettings = new JMenuItem("Settings");
		miSettings.setMnemonic('s');
		menuBP.add(miSettings);
		miSettings.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionShowSettings();
			}
		});
		
		JMenuItem miAbout = new JMenuItem("About...");
		miAbout.setMnemonic('a');
		menuBP.add(miAbout);
		miAbout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionAbout();
			}
		});
		
		menuBP.addSeparator();
		
		JMenuItem miExit = new JMenuItem("Exit");
		miExit.setMnemonic('e');
		menuBP.add(miExit);
		miExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				processWindowEvent(new WindowEvent(
						BitpowerFrame.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		return menuBar;
	}
	
	protected JPopupMenu createTorrentsPopupMenu()
	{
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem miStart = new JMenuItem("Start/Resume");
		JMenuItem miStop = new JMenuItem("Stop");
		JMenuItem miRemove = new JMenuItem("Remove");
		
		popupMenu.add(miStart);
		popupMenu.add(miStop);
		popupMenu.add(miRemove);
		
		miStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tblTorrents.rowAtPoint(new Point(popupX, popupY));
				
				Torrent torrent = mdlTorrents.getTorrentAt(row);
				torrent.getTorrentManager().start(false);
			}
		});
		
		miStop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tblTorrents.rowAtPoint(new Point(popupX, popupY));
				Torrent torrent = mdlTorrents.getTorrentAt(row);
				torrent.getTorrentManager().stop(false);
			}
		});
		
		
		miRemove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = tblTorrents.rowAtPoint(new Point(popupX, popupY));
				Torrent torrent = mdlTorrents.getTorrentAt(row);
				torrent.getTorrentManager().terminate(false);
				mdlTorrents.removeTorrent(torrent);
				controller.removeTorrentManager(torrent.getTorrentManager());
			}
		});
		
		return popupMenu;
	}
	
	protected void addListeners()
	{
		addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent e) {
				int result = CommonUtils.dialogYesNo(BitpowerFrame.this, "To Quit:", "Are you sure you want to quit?", new String[] { "Yes", "No"});
				if (result == 0)
				{
					saveSettings();
				}
				BitpowerFrame.this.setDefaultCloseOperation((result == 1)? DO_NOTHING_ON_CLOSE : EXIT_ON_CLOSE);
			}
		});
		
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				int row = tblTorrents.getSelectedRow();
				mdlTorrents.refresh();
				if (row >= 0)
				{
					tblTorrents.getSelectionModel().setSelectionInterval(row, row);
					Torrent tor = mdlTorrents.getTorrentAt(row);
					TorrentManager man = tor.getTorrentManager();
					mdlPeers.setData(new ArrayList<Peer>(man.getPeers()));
					pnlPieces.setPieces(man.getPresentPieces());
					
					if (mdlFiles.getRoot() != tor.getFileNode())
					{
						mdlFiles.setRoot(tor.getFileNode());
					}
				}
				else
				{
					mdlFiles.setRoot(new FileNode());
					mdlPeers.setData(new ArrayList<Peer>());
					pnlPieces.setPieces(new BitField(1));
				}
			}
		};
		new Timer().schedule(task, 1000, 1000);
	}
	
	protected void actionOpenTorrent()
	{
		int val = fc.showOpenDialog(this);
		if (val == JFileChooser.APPROVE_OPTION)
		{
			File tfile = fc.getSelectedFile();
			String tfilename = tfile.toString();
			try
			{
				TorrentManager manager = controller.addTorrentManager(tfile, new File(CommonUtils.getDestopPath()));
				Torrent torrent = new Torrent(tfilename, manager);
				
				mdlTorrents.addTorrent(torrent);
				if ((settings != null) && settings.isAutoDownload())
				{
					torrent.getTorrentManager().start(false);
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public void loadSettings()
	{
		File fileSettings = new File(CommonUtils.getSettingsPath());
		if (!fileSettings.exists())
		{
			this.settings = new Settings();
			return;
		}
		try
		{
			ObjectInputStream ois = new ObjectInputStream(
									new BufferedInputStream(
									new FileInputStream(fileSettings)));
			
			this.settings = (Settings) ois.readObject();
			
			File downloadDir = new File(settings.getDownloadDir());
			for (Iterator<String> iter = settings.getTorrentFiles().iterator(); iter.hasNext(); )
			{
				String line = iter.next();
				if (!((line != null) && !line.equals("")))
					break;
				File torrentFile = new File(line);
				TorrentManager manager = controller.addTorrentManager(torrentFile, downloadDir);
				Torrent torrent = new Torrent(line, manager);
				mdlTorrents.addTorrent(torrent);
				if (settings.isAutoDownload())
				{
					torrent.getTorrentManager().start(false);
				}
			}
		
			ois.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void saveSettings()
	{
		ArrayList<String> torrentFiles = new ArrayList<String>();
		for (int i=0; i<mdlTorrents.getRowCount(); i++)
		{
			Torrent tor = mdlTorrents.getTorrentAt(i);
			torrentFiles.add(tor.getTorrentFileName());
		}
		this.settings.setTorrentFiles(torrentFiles);
		
		try
		{
			File fileSettings = new File(CommonUtils.getSettingsPath());
			if (!fileSettings.exists())
			{
				fileSettings.createNewFile();
			}
			
			ObjectOutputStream oos = new ObjectOutputStream(
									 new BufferedOutputStream(
									 new FileOutputStream(fileSettings)));
			oos.writeObject(this.settings);
			oos.flush();
			oos.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public Settings getSettings()
	{
		return settings;
	}
	
	protected void actionShowSettings()
	{
		final JDialog dialog = new JDialog(this, "BitPower settings", true);
		PanelSettings pnlSettings = new PanelSettings(this);
		dialog.getContentPane().add(pnlSettings);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setResizable(false);
		
		pnlSettings.getBtnSave().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();
			}
		});
		dialog.setVisible(true);
	}
	
	protected void actionAbout()
	{
		final JDialog dialog = new JDialog(this, "About", true);
		PanelAbout pnlAbout = new PanelAbout();
		dialog.getContentPane().add(pnlAbout);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setResizable(false);
		
		pnlAbout.getBtnOK().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();
			}
		});
		dialog.setVisible(true);
	}
	
	public static void main(String[] args) 
	{
		BitpowerFrame bpframe = new BitpowerFrame();
		bpframe.setVisible(true);
	}

}
