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

import java.util.List;

public class PartmanAlgorithm extends Algorithm {

	public PartmanAlgorithm(Harddisk hdd) {
		super(hdd);
	}

	private long sum(long[] a) {
		long sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum;
	}

	@Override
	protected void calculate() {
		List<Partition> partitions = hdd.getPartitions();

		long[] factor = new long[partitions.size()];
		long[] min = new long[partitions.size()];
		long[] max = new long[partitions.size()];
		long[] priority = new long[partitions.size()];
		long freespace = hdd.getSize();

		for (int i = 0; i < partitions.size(); i++) {
			min[i] = partitions.get(i).getMinSize();
			max[i] = partitions.get(i).getMaxSize();
			priority[i] = partitions.get(i).getPriority();
			factor[i] = priority[i] - min[i];
		}

		boolean ready = false;
		while (!ready) {
			long minsum = sum(min);
			long factsum = sum(factor);
			ready = true;
			for (int i = 0; i < partitions.size(); i++) {
				long x = min[i] + (freespace - minsum) * factor[i] / factsum;
				if (x > max[i])
					x = max[i];
				if (x != min[i]) {
					ready = false;
					min[i] = x;
				}
			}
		}

		for (int i = 0; i < partitions.size(); i++) {
			partitions.get(i).setCalculatedSize((int)min[i]);
		}
	}
}
