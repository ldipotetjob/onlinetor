/**
 * 
 */
package com.bu6ido.bitpower.models;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.itadaki.bobbin.peer.TorrentManager;
import org.itadaki.bobbin.torrentdb.Filespec;
import org.itadaki.bobbin.torrentdb.InfoFileset;

/**
 * @author bu6ido
 *
 */
public class Torrent 
{
	protected String torrentFileName;
	protected FileNode fileNode;
	protected TorrentManager torrentManager;
	
	public Torrent(String fileName, TorrentManager manager)
	{
		this.torrentFileName = fileName;
		this.torrentManager = manager;
		parseFiles();
	}
	
	public String getTorrentFileName()
	{
		return torrentFileName;
	}
	
	public TorrentManager getTorrentManager()
	{
		return torrentManager;
	}
	
	public FileNode getFileNode()
	{
		return fileNode;
	}
	
	public void setFileNode(FileNode fileNode)
	{
		this.fileNode = fileNode;
	}
	
	protected void parseFiles()
	{
		InfoFileset fileSet = torrentManager.getPieceDbInfo().getFileset();
		FileNode root = new FileNode();
		FileNode work;
		List<Filespec> files = fileSet.getFiles();
		for (Iterator<Filespec> iter = files.iterator(); iter.hasNext(); )
		{
			Filespec filespec = iter.next();
			work = root;
			String workPath = "";
			for (Iterator<String> siter = filespec.getName().iterator(); siter.hasNext(); )
			{
				String workName = siter.next();
				workPath += File.separator;
				workPath += workName;
				boolean found = false;
				FileNode chwork = null;
				for (Iterator<FileNode> fiter = work.getChildren().iterator(); fiter.hasNext(); )
				{
					chwork = fiter.next();
					if (chwork.getName().equals(workName))
					{
						found = true;
						break;
					}
				}
				if (!found)
				{
					chwork = new FileNode();
					chwork.setName(workName);
					chwork.setLength(filespec.getLength());
					chwork.setFullPath(workPath);
					work.addChild(chwork);
				}
				work = chwork;
			}
		}

		root.setName(fileSet.getBaseDirectoryName());
		root.setFullPath(root.getName());
/*		if (torrentFileName.length() > 8)
		{
			root.setName(torrentFileName.substring(0, torrentFileName.length() - 8));
		}
		else
		{
			root.setName(torrentFileName);
		}*/
		setFileNode(root);
	}
}
