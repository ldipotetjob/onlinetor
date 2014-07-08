/**
 * 
 */
package com.bu6ido.bitpower.models;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.itadaki.bobbin.peer.Peer;
import org.itadaki.bobbin.peer.PeerState;
import org.itadaki.bobbin.peer.PeerStatistics;

import com.bu6ido.bitpower.common.CommonUtils;
import com.bu6ido.bitpower.common.GeoLocationService;

/**
 * @author bu6ido
 *
 */
public class PeerTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -6180999208555731852L;

	
	private String[] columnNames = { "IP address", "Peer ID", "Country", "Flags", "Uploaded", "Downloaded", "View length" };

	protected ArrayList<Peer> peers = new ArrayList<Peer>();
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int column) 
	{
		return this.columnNames[column];
	}

	@Override
	public int getRowCount() {
		return peers.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) 
	{
		Peer peer = peers.get (rowIndex);
		switch (columnIndex) {
			case 0:
				return CommonUtils.address2string(peer.getRemoteSocketAddress());
			case 1:
				return new String(peer.getPeerState().getRemotePeerID().getBytes());
			case 2:
				if ((peer != null) && (peer.getRemoteSocketAddress() != null))
				{
					return GeoLocationService.getInstance().getCountry(peer.getRemoteSocketAddress().getAddress());
				}
				return null;
			case 3:
				StringBuilder sb = new StringBuilder();
				PeerState state = peer.getPeerState();
				if (state.getWeAreChoking())
				{
					sb.append("choking");
				}
				if (state.getWeAreInterested())
				{
					if (sb.length() > 0)
					{
						sb.append(",");
					}
					sb.append("interesting");
				}
				if (state.getTheyAreChoking())
				{
					if (sb.length() > 0)
					{
						sb.append(",");
					}
					sb.append("choked");
				}
				if (state.getTheyAreInterested())
				{
					if (sb.length() > 0)
					{
						sb.append(",");
					}
					sb.append("interested");
				}
				return sb.toString();
			case 4:
				return String.format ("%1.2f KB/s", (float)peer.getReadableStatistics().getPerSecond (PeerStatistics.Type.PROTOCOL_BYTES_SENT) / 1000);
			case 5:
				return String.format ("%1.2f KB/s", (float)peer.getReadableStatistics().getPerSecond (PeerStatistics.Type.PROTOCOL_BYTES_RECEIVED) / 1000);
			case 6:
				return peer.getPeerState().getRemoteView().getLength();
		}

		return null;
	}
	
	public void setData(ArrayList<Peer> peers)
	{
		this.peers.clear();
		this.peers.addAll(peers);
		fireTableDataChanged();
	}
	
	public void refresh()
	{
		fireTableDataChanged();
	}

}
