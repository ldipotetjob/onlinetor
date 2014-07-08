/**
 * 
 */
package com.bu6ido.bitpower.models;

import java.util.ArrayList;

/**
 * @author bu6ido
 *
 */
public class FileNode 
{
	private String name = "";
	private long length = 0l;
	private String fullPath = "";
	
	private ArrayList<FileNode> children = new ArrayList<FileNode>();
	
	@Override
	public String toString() {
		return getName();
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public long getLength()
	{
		return length;
	}
	
	public void setLength(long length)
	{
		this.length = length;
	}

	public String getFullPath()
	{
		return fullPath;
	}
	
	public void setFullPath(String fullPath)
	{
		this.fullPath = fullPath;
	}
	
	public ArrayList<FileNode> getChildren()
	{
		return this.children;
	}
	
	public FileNode getChildAt(int index)
	{
		return this.children.get(index);
	}
	
	public void addChild(FileNode child)
	{
		this.children.add(child);
	}
	
	public void removeChild(FileNode child)
	{
		this.children.remove(child);
	}
}
