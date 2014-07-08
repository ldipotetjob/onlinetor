/**
 * 
 */
package com.bu6ido.bitpower.models;

import java.util.ArrayList;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.bu6ido.bitpower.BitpowerFrame;

/**
 * @author bu6ido
 *
 */
public class FilesTreeModel implements TreeModel {
	
	protected BitpowerFrame frame;
	
	protected FileNode root;
	protected ArrayList<TreeModelListener> tmls = new ArrayList<TreeModelListener>();
	
	public FilesTreeModel(BitpowerFrame frame)
	{
		this.frame = frame;
	}
	
    protected void fireTreeStructureChanged(FileNode oldRoot) {
        TreeModelEvent e = new TreeModelEvent(this, 
                                              new Object[] {oldRoot});
        for (TreeModelListener tml : tmls) {
            tml.treeStructureChanged(e);
        }
    }
    
	@Override
	public void addTreeModelListener(TreeModelListener l) {
		tmls.add(l);
	}

	@Override
	public Object getChild(Object parent, int index) {
		FileNode work = (FileNode) parent;
		if (work != null)
		{
			return work.getChildAt(index);
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		FileNode work = (FileNode) parent;
		if (work != null)
		{
			return work.getChildren().size();
		}
		return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		FileNode pnode = (FileNode) parent;
		FileNode cnode = (FileNode) child;
		if (pnode != null)
		{
			return pnode.getChildren().indexOf(cnode);
		}
		return 0;
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(Object parent) {
		FileNode work = (FileNode) parent;
		if (work != null)
		{
			return work.getChildren().size() == 0;
		}
		return true; 
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		tmls.remove(l);
	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {

	}

	public void setRoot(FileNode root)
	{
		if (root != null)
		{
			this.root = root;
			fireTreeStructureChanged(this.root);
		}
	}
	
/*	public class ProgressBarTreeCellRenderer extends DefaultTreeCellRenderer
	{
		private static final long serialVersionUID = 3122700115706524240L;
		protected JProgressBar pb = new JProgressBar(0, 100);
		
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			FileNode node = (FileNode) value;
			if (leaf)
			{
				String path = frame.getSettings().getDownloadDir() + 
						File.separator + root.getFullPath() + node.getFullPath();
				File testFile = new File(path);
				long available = 0l;
				if (testFile.exists())
				{
					available = testFile.length();
				}
				
				if (node.getLength() == 0)
				{
					pb.setValue(0);
				}
				else
				{
					pb.setValue((int) ((double) available / (double) node.getLength() * 100));
				}
				pb.setStringPainted(true);
				pb.setString(node.toString());
				return pb;
			}
			
			return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
					row, hasFocus);
		}
	}*/
}
