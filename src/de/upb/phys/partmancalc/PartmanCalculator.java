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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

public class PartmanCalculator extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLabelHDDSize = null;
	private JTextField jTextFieldHDDSize = null;
	private JPanel jPanelContent = null;
	private JScrollPane jScrollPane = null;
	private JTable jTablePartitionInfo = null;
	private JButton jButtonAddPartition = null;
	private JButton jButtonRemovePartition = null;
	private JPanel jPanelResult = null;
	private final Harddisk hdd;
	private final Algorithm algo;

	/**
	 * This method initializes jTextFieldHDDSize
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldHDDSize() {
		if (jTextFieldHDDSize == null) {
			jTextFieldHDDSize = new JTextField();
		}
		return jTextFieldHDDSize;
	}

	/**
	 * This method initializes jPanelContent
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelContent() {
		if (jPanelContent == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.anchor = GridBagConstraints.EAST;
			gridBagConstraints5.gridy = 3;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.anchor = GridBagConstraints.EAST;
			gridBagConstraints4.gridy = 3;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.gridwidth = 3;
			gridBagConstraints3.insets = new Insets(7, 5, 0, 5);
			gridBagConstraints3.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.EAST;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 0.0;
			gridBagConstraints.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = -1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.gridwidth = 2;
			gridBagConstraints1.gridx = 1;
			jPanelContent = new JPanel();
			jPanelContent.setLayout(new GridBagLayout());
			jPanelContent.setBorder(BorderFactory.createTitledBorder(null, "Configuration", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			jPanelContent.add(getJTextFieldHDDSize(), gridBagConstraints1);
			jPanelContent.add(jLabelHDDSize, gridBagConstraints);
			jPanelContent.add(getJScrollPane(), gridBagConstraints3);
			jPanelContent.add(getJButtonAddPartition(), gridBagConstraints4);
			jPanelContent.add(getJButtonRemovePartition(), gridBagConstraints5);
		}
		return jPanelContent;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTablePartitionInfo());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTablePartitionInfo
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getJTablePartitionInfo() {
		if (jTablePartitionInfo == null) {
			jTablePartitionInfo = new JTable();
		}
		return jTablePartitionInfo;
	}

	/**
	 * This method initializes jButtonAddPartition
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonAddPartition() {
		if (jButtonAddPartition == null) {
			jButtonAddPartition = new JButton();
			jButtonAddPartition.setText("Add Partition");
		}
		return jButtonAddPartition;
	}

	/**
	 * This method initializes jButtonRemovePartition
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonRemovePartition() {
		if (jButtonRemovePartition == null) {
			jButtonRemovePartition = new JButton();
			jButtonRemovePartition.setText("Remove Partition");
		}
		return jButtonRemovePartition;
	}

	/**
	 * This method initializes jPanelResult
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelResult() {
		if (jPanelResult == null) {
			jPanelResult = new JPanel();
			jPanelResult.setLayout(new BorderLayout());
			jPanelResult.setBorder(BorderFactory.createTitledBorder(null, "Result", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		}
		return jPanelResult;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PartmanCalculator thisClass = new PartmanCalculator();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public PartmanCalculator() {
		super();
		initialize();

		hdd = new Harddisk(0);
		HDDDisplay hddisplay = new HDDDisplay(hdd);
		this.getJPanelResult().add(hddisplay, BorderLayout.CENTER);

		//Binding
		AutoBinding<?, ?, ?, ?> hddSizeBinding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, hdd, BeanProperty.create("size"), getJTextFieldHDDSize(), BeanProperty.create("text"));
		hddSizeBinding.bind();

		//TableModel
		HDDTableModel tableModel = new HDDTableModel(hdd);
		getJTablePartitionInfo().setModel(tableModel);

		getJButtonAddPartition().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hdd.addPartition(new Partition(0, 0, 0));
			}
		});
		getJButtonRemovePartition().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = getJTablePartitionInfo().getSelectedRow();
				if (row >= 0) {
					Partition p = hdd.getPartitions().get(row);
					hdd.removePartition(p);
				}
			}
		});

		algo = new PartmanAlgorithm(hdd);

	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(514, 543);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("Partman Calculator");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.insets = new Insets(0, 7, 7, 7);
			gridBagConstraints6.weighty = 0.5;
			gridBagConstraints6.gridwidth = 3;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(7, 7, 7, 7);
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 0.5;
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridx = 0;
			jLabelHDDSize = new JLabel();
			jLabelHDDSize.setText("HDD Size:");
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJPanelContent(), gridBagConstraints2);
			jContentPane.add(getJPanelResult(), gridBagConstraints6);
		}
		return jContentPane;
	}

} //  @jve:decl-index=0:visual-constraint="10,10"
