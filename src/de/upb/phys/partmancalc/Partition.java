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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Partition {
	private final PropertyChangeSupport pcs;

	private int maxSize;
	private int minSize;
	private int priority;

	private int calculatedSize;

	public Partition(int maxSize, int minSize, int priority) {
		super();
		pcs = new PropertyChangeSupport(this);
		this.maxSize = maxSize;
		this.minSize = minSize;
		this.priority = priority;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		if (this.maxSize != maxSize) {
			int oldMaxSize = this.maxSize;
			this.maxSize = maxSize;
			pcs.firePropertyChange("maxSize", oldMaxSize, maxSize);
		}
	}

	public int getMinSize() {
		return minSize;
	}

	public void setMinSize(int minSize) {
		if (this.minSize != minSize) {
			int oldMinSize = this.minSize;
			this.minSize = minSize;
			pcs.firePropertyChange("minSize", oldMinSize, minSize);
		}
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		if (this.priority != priority) {
			int oldPriority = this.priority;
			this.priority = priority;
			pcs.firePropertyChange("priority", oldPriority, priority);
		}
	}

	public int getCalculatedSize() {
		return calculatedSize;
	}

	public void setCalculatedSize(int calculatedSize) {
		if (this.calculatedSize != calculatedSize) {
			int oldCalculatedSize = this.calculatedSize;
			this.calculatedSize = calculatedSize;
			pcs.firePropertyChange("calculatedSize", oldCalculatedSize, calculatedSize);
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
}
