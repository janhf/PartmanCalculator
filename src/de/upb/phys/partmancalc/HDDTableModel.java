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
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class HDDTableModel implements TableModel, PropertyChangeListener {

	private final Harddisk hdd;
	private final List<TableModelListener> tableModelListeners;

	public HDDTableModel(Harddisk hdd) {
		this.hdd = hdd;
		tableModelListeners = new ArrayList<TableModelListener>();

		hdd.addPropertyChangeListener(this);

		//register for PCS
		for (Partition p : hdd.getPartitions()) {
			p.addPropertyChangeListener(this);
		}
	}

	public int getRowCount() {
		return hdd.getPartitions().size();
	}

	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		default:
			return Integer.class;
		}
	}

	public int getColumnCount() {
		return 5;
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Part. No.";
		case 1:
			return "Min. Size";
		case 2:
			return "Max. Size";
		case 3:
			return "Priority";
		case 4:
			return "Calculated Size";
		default:
			return "<n/a>";
		}
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Partition p = hdd.getPartitions().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return "#" + rowIndex;
		case 1:
			return p.getMinSize();
		case 2:
			return p.getMaxSize();
		case 3:
			return p.getPriority();
		case 4:
			return p.getCalculatedSize();
		default:
			return null;
		}
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Partition p = hdd.getPartitions().get(rowIndex);
		switch (columnIndex) {
		case 1:
			int minSize = (Integer) value;
			p.setMinSize(minSize);
			break;
		case 2:
			int maxSize = (Integer) value;
			p.setMaxSize(maxSize);
			break;
		case 3:
			int priority = (Integer) value;
			p.setPriority(priority);
			break;
		default:
			break;
		}
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return false;
		case 1:
			return true;
		case 2:
			return true;
		case 3:
			return true;
		case 4:
			return false;
		default:
			return false;
		}
	}

	public void removeTableModelListener(TableModelListener l) {
		tableModelListeners.remove(l);
	}

	public void addTableModelListener(TableModelListener l) {
		tableModelListeners.add(l);
	}

	public void fireTableChanged(TableModelEvent e) {
		for (TableModelListener l : tableModelListeners) {
			l.tableChanged(e);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		Object source = evt.getSource();
		if (source instanceof Partition) {
			int pos = hdd.getPartitions().indexOf(source);
			if (pos >= 0) {
				int cell = -1;
				if (evt.getPropertyName().equals("minSize")) {
					cell = 1;
				} else if (evt.getPropertyName().equals("maxSize")) {
					cell = 2;
				} else if (evt.getPropertyName().equals("priority")) {
					cell = 3;
				} else if (evt.getPropertyName().equals("calculatedSize")) {
					cell = 4;
				}

				if (cell >= 0) {
					fireTableChanged(new TableModelEvent(this, pos, cell));
				} else {
					fireTableChanged(new TableModelEvent(this, pos));
				}
			}
		} else if (source instanceof Harddisk) {
			if (evt.getPropertyName().equals("partitions")) {
				if (evt.getNewValue() == null) {
					((Partition) evt.getOldValue()).removePropertyChangeListener(this);
					fireTableChanged(new TableModelEvent(this));
				} else if (evt.getOldValue() == null) {
					((Partition) evt.getNewValue()).addPropertyChangeListener(this);
					fireTableChanged(new TableModelEvent(this));
				}
			}
		}
	}

}
