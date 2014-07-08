/**
 * 
 */
package com.bu6ido.bitpower.models;

import java.awt.Component;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.itadaki.bobbin.peer.TorrentManager;

/**
 * @author bu6ido
 *
 */
public class TorrentTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -4620292293709643235L;

	
	private String[] columnNames = { "Name", "Completed", "Download", "Upload" };
	
	protected ArrayList<Torrent> torrents = new ArrayList<Torrent>();
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	@Override
	public int getRowCount() {
		return torrents.size();
	}

	@Override
	public Object getValueAt(int row, int column) 
	{
		Torrent torrent = null;
		if ((row >= 0) && (row < this.torrents.size()))
		{
			torrent = this.torrents.get(row);
		}
		if (torrent == null) return null;
		TorrentManager manager = torrent.getTorrentManager();
		switch(column)
		{
			case 0:
				return new File(torrent.getTorrentFileName()).getName();
			case 1:
				int numPresentPieces = manager.getPresentPieces().cardinality();
				float percent = (float) numPresentPieces * 100 / manager.getNumberOfPieces();
				return percent;
			case 2:
				return String.format("%1.2f KB/s", (float) manager.getProtocolBytesReceivedPerSecond() / 1024);
			case 3:
				return String.format("%1.2f KB/s", (float) manager.getProtocolBytesSentPerSecond() / 1024);
		}
		return null;
	}
	
	public void setData(ArrayList<Torrent> torrents)
	{
		this.torrents.clear();
		this.torrents.addAll(torrents);
		fireTableDataChanged();
	}
	
	public void addTorrent(Torrent torrent)
	{
		this.torrents.add(torrent);
		fireTableDataChanged();
	}
	
	public void removeTorrent(Torrent torrent)
	{
		this.torrents.remove(torrent);
		fireTableDataChanged();
	}
	
	public void refresh()
	{
		fireTableDataChanged();
	}
	
	public Torrent getTorrentAt(int row)
	{
		return this.torrents.get(row);
	}

	public class ProgressBarCellRenderer extends DefaultTableCellRenderer
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7166936127898860117L;
		protected JProgressBar pb = new JProgressBar(0, 100);
		
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, 
				int row, int column) 
		{
			if (column == 1)
			{
				float percent = (Float) value;
				DecimalFormat df = new DecimalFormat("0.00");
				String text = df.format(percent) + "%";
				pb.setValue((int) percent);
				pb.setStringPainted(true);
				pb.setString(text);
				return pb;
			}
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
		}
	}
}