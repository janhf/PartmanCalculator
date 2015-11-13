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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class Harddisk implements PropertyChangeListener {
	private final ArrayList<Partition> partitionList;
	private final PropertyChangeSupport pcs;

	private int size;

	public Harddisk(int size) {
		super();
		this.size = size;
		this.partitionList = new ArrayList<Partition>();
		this.pcs = new PropertyChangeSupport(this);
	}

	public void addPartition(Partition p) {
		if (p != null && !partitionList.contains(p)) {
			this.partitionList.add(p);
			pcs.firePropertyChange("partitions", null, p);
			p.addPropertyChangeListener(this);
		}

	}

	public void removePartition(Partition p) {
		if (p != null && partitionList.contains(p)) {
			this.partitionList.remove(p);
			pcs.firePropertyChange("partitions", p, null);
			p.removePropertyChangeListener(this);
		}
	}

	public List<Partition> getPartitions() {
		return (List<Partition>) partitionList.clone();
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		if (this.size != size) {
			int oldSize = this.size;
			this.size = size;
			pcs.firePropertyChange("size", oldSize, size);
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(propertyName, listener);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		pcs.firePropertyChange(new PropertyChangeEvent(this, "partitions", evt.getSource(), evt.getSource()));
	}
}
