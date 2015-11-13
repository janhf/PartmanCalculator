/*
 *    PartmanCalculator - Calculates partition sizes with the algorithm
 *     of debian installers partitioner
 *    Copyright (C) 2015  Jan-Philipp Hülshoff <github@bklosr.de>
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package de.upb.phys.partmancalc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JPanel;

public class HDDDisplay extends JPanel implements PropertyChangeListener {
	private final Harddisk hdd;

	public HDDDisplay(Harddisk hdd) {
		this.hdd = hdd;
		this.setLayout(null);
		this.setMinimumSize(new Dimension(400, 50));

		//register for PCS
		hdd.addPropertyChangeListener(this);
		for (Partition p : hdd.getPartitions()) {
			p.addPropertyChangeListener(this);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		if (hdd == null) {
			g.setColor(Color.BLACK);
			g.drawString("ERROR: No input.", 7, getHeight() / 2);
			return;
		}

		if (hdd.getSize() == 0) {
			g.setColor(Color.BLACK);
			g.drawString("ERROR: HDD Size == 0.", 7, getHeight() / 2);
			return;
		}

		List<Partition> partitions = hdd.getPartitions();
		int partitionCount = partitions.size();
		int partitionSizeSum = 0;
		int[] sizes = new int[partitionCount];
		for (int i = 0; i < sizes.length; i++) {
			sizes[i] = partitions.get(i).getCalculatedSize();
			partitionSizeSum += sizes[i];
		}

		int[] pixelSize = new int[partitionCount];
		for (int i = 0; i < pixelSize.length; i++) {
			pixelSize[i] = Math.round((sizes[i] / (float) hdd.getSize()) * (getWidth()));
		}

		g.setColor(Color.RED);
		g.fillRect(0, 0, getWidth(), getHeight());

		//Painting partitions
		int sizeConsumed = 0;
		for (int i = 0; i < sizes.length; i++) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(sizeConsumed, 0, pixelSize[i] - 1, getHeight() - 1);
			g.setColor(Color.BLACK);
			g.drawRect(sizeConsumed, 0, pixelSize[i] - 1, getHeight() - 1);
			g.drawString("#" + Integer.toString(i), sizeConsumed + 7, getHeight() / 2);
			sizeConsumed += pixelSize[i];
		}

		if (partitionSizeSum > hdd.getSize()) {
			g.setColor(Color.BLACK);
			g.drawString("ERROR: Partitions are greater than HD Size!", 7, getHeight() / 4);
		}
	}

	public final void propertyChange(PropertyChangeEvent evt) {
		Object source = evt.getSource();
		if (source instanceof Partition) {
			repaint();
		} else if (source instanceof Harddisk) {
			if (evt.getPropertyName().equals("partitions")) {
				if (evt.getNewValue() == null) {
					((Partition) evt.getOldValue()).removePropertyChangeListener(this);
				} else if (evt.getOldValue() == null) {
					((Partition) evt.getNewValue()).addPropertyChangeListener(this);
				}
			}
			repaint();
		}
	}
}
