/**
 * 
 */
package com.bu6ido.bitpower.common;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author bu6ido
 *
 */
public class TorrentFileFilter extends FileFilter {

	@Override
	public boolean accept(File file) {
		if (file.isDirectory())
		{
			return true;
		}
		String ext = CommonUtils.getFileExtension(file);
		if ((ext != null) && "torrent".equalsIgnoreCase(ext))
		{
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "Bittorrent files (*.torrent)";
	}

}
