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

public abstract class Algorithm implements PropertyChangeListener {

	protected Harddisk hdd;

	public Algorithm(Harddisk hdd) {
		this.hdd = hdd;
		hdd.addPropertyChangeListener(this);

		//register for PCS
		for (Partition p : hdd.getPartitions()) {
			p.addPropertyChangeListener(this);
		}
	}

	protected abstract void calculate();

	private void calculateSafe() {
		try {
			calculate();
		} catch (Throwable t) {
			System.err.println(t.getMessage());
			//t.printStackTrace();
		}
	}

	public final void propertyChange(PropertyChangeEvent evt) {
		Object source = evt.getSource();
		if (source instanceof Partition) {
			calculateSafe();
		} else if (source instanceof Harddisk) {
			if (evt.getPropertyName().equals("partitions")) {
				if (evt.getNewValue() == null) {
					((Partition) evt.getOldValue()).removePropertyChangeListener(this);
				} else if (evt.getOldValue() == null) {
					((Partition) evt.getNewValue()).addPropertyChangeListener(this);
				}
			}
			calculateSafe();
		}
	}

}
