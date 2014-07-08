/**
 * 
 */
package com.bu6ido.bitpower.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.itadaki.bobbin.util.BitField;

/**
 * @author bu6ido
 *
 */
public class PanelPieces extends JPanel 
{
	private static final long serialVersionUID = 5235036918609280095L;

	
	public static final int PIECES_IN_ROW = 50;
	
	protected BitField pieces = new BitField(1);
	
	public PanelPieces(int main_width)
	{
		super();
		setSize(main_width, main_width);
		setPreferredSize(getSize());
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		Dimension size = getSize();
		int width = size.width;
		int height = size.height;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		int pcount = pieces.length();
		int work = 0;
		int pwidth = width / PIECES_IN_ROW;

		g.setColor(Color.RED);
		while (work < pcount)
		{
			int j = work / PIECES_IN_ROW;
			int i = work % PIECES_IN_ROW;
			int x = i * pwidth + 2;
			int y = j * pwidth + 2;
			if (pieces.get(work))
			{
				g.fillRect(x, y, pwidth - 4, pwidth - 4);
			}
			else
			{
				g.drawRect(x, y, pwidth - 4, pwidth - 4);
			}
			work++;
		}
	}
	
	public void setPieces(BitField pieces)
	{
		this.pieces = pieces.clone();
		invalidate();
		revalidate();
		repaint();
	}
}
