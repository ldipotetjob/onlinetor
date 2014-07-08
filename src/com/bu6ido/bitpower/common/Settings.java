/**
 * 
 */
package com.bu6ido.bitpower.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author bu6ido
 *
 */
public class Settings implements Serializable 
{
	
	private static final long serialVersionUID = 7213680945018340676L;

	protected String downloadDirectory = CommonUtils.getDestopPath();
	protected boolean isAutoDownload = true;
	protected ArrayList<String> torrentFiles = new ArrayList<String>();
	
	
	public Settings()
	{
	}
	
	public String getDownloadDir()
	{
		return downloadDirectory;
	}
	
	public void setDownloadDir(String downloadDirectory)
	{
		this.downloadDirectory = downloadDirectory;
	}
	
	public boolean isAutoDownload()
	{
		return isAutoDownload;
	}
	
	public void setAutoDownload(boolean isAutoDownload)
	{
		this.isAutoDownload = isAutoDownload;
	}
	
	public ArrayList<String> getTorrentFiles()
	{
		return torrentFiles;
	}
	
	public void setTorrentFiles(ArrayList<String> torrentFiles)
	{
		this.torrentFiles = torrentFiles;
	}
}
