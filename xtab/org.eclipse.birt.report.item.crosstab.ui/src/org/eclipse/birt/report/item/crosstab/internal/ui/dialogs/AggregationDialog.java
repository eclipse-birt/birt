/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Add the subtotal and grand toatal to the special LevelViewHandle
 * AggregationDialog
 */
public class AggregationDialog extends BaseDialog {

	private static final String DIALOG_NAME = Messages.getString("AggregationDialog.Title"); //$NON-NLS-1$
	private List<SubTotalInfo> rowSubList = new ArrayList<SubTotalInfo>();
	private List<GrandTotalInfo> rowGrandList = new ArrayList<GrandTotalInfo>();
	private List<SubTotalInfo> colSubList = new ArrayList<SubTotalInfo>();
	private List<GrandTotalInfo> colGrandList = new ArrayList<GrandTotalInfo>();

	private TabFolder tabFolder;
	private TabItem rowArea, columnArea;
	private int currentAxis = ICrosstabConstants.NO_AXIS_TYPE;

	private CrosstabReportItemHandle crosstab;

	private void setCrosstab(CrosstabReportItemHandle crosstab) {
		this.crosstab = crosstab;
//		initialization( );
	}

	/**
	 * Constructor
	 * 
	 * @param shell
	 */
	public AggregationDialog(Shell shell, CrosstabReportItemHandle crosstab) {
		super(shell, DIALOG_NAME);
		setCrosstab(crosstab);
	}

	public void setAxis(int axis) {
		this.currentAxis = axis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.
	 * Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.XTAB_AGGREGATION_DIALOG);

		Composite dialogArea = (Composite) super.createDialogArea(parent);
		createTabFolder(dialogArea);

		for (int i = 0; i < 2; i++) {
			Composite content = new Composite(tabFolder, SWT.NONE);
			content.setLayoutData(new GridData(GridData.FILL_BOTH));
			GridLayout layout = new GridLayout();
			content.setLayout(layout);
			SubTotalDialog subTotal = new SubTotalDialog();
			if (i == 0) {
				subTotal.setAxis(ICrosstabConstants.ROW_AXIS_TYPE);
				subTotal.setInput(rowSubList, rowGrandList);
			} else if (i == 1) {
				subTotal.setAxis(ICrosstabConstants.COLUMN_AXIS_TYPE);
				subTotal.setInput(colSubList, colGrandList);
			}

			subTotal.createSubTotalArea(content);
			subTotal.createGrandTotalArea(content);
			subTotal.init();
			tabFolder.getItem(i).setControl(content);
		}
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		ini();
		return dialogArea;
	}

	private void ini() {
		if (currentAxis == ICrosstabConstants.ROW_AXIS_TYPE) {
			tabFolder.setSelection(rowArea);
		} else if (currentAxis == ICrosstabConstants.COLUMN_AXIS_TYPE) {
			tabFolder.setSelection(columnArea);
		}
	}

	private TabFolder createTabFolder(Composite parent) {
		tabFolder = new TabFolder(parent, SWT.NONE);
		// Should not set layout for TabFolder.
		// GridData gd = new GridData( GridData.FILL_BOTH );
		// tabFolder.setLayoutData( gd );
		// GridLayout layout = new GridLayout( );
		// tabFolder.setLayout( layout );

		rowArea = new TabItem(tabFolder, SWT.NONE);
		rowArea.setText(Messages.getString("AggregationDialog.TabItem.Title.RowArea")); //$NON-NLS-1$
		rowArea.setImage(CrosstabUIHelper.getImage(CrosstabUIHelper.ROWS_AREA_IMAGE));

		columnArea = new TabItem(tabFolder, SWT.NONE);
		columnArea.setText(Messages.getString("AggregationDialog.TabItem.Title.ColumnArea")); //$NON-NLS-1$
		columnArea.setImage(CrosstabUIHelper.getImage(CrosstabUIHelper.COLUMNS_AREA_IMAGE));

		return tabFolder;
	}

	/**
	 * Set the input
	 * 
	 * @param subList   subtotal info list
	 * @param grandList grand total list info
	 */
	public void setInput(List<SubTotalInfo> rowSubList, List<GrandTotalInfo> rowGrandList,
			List<SubTotalInfo> colSubList, List<GrandTotalInfo> colGrandList) {
		this.rowSubList.addAll(rowSubList);
		this.rowGrandList.addAll(rowGrandList);
		this.colSubList.addAll(colSubList);
		this.colGrandList.addAll(colGrandList);
	}

	public Object getResult() {
		return new Object[] { rowSubList, rowGrandList, colSubList, colGrandList };
	}

	private class SubTotalDialog {

		private CheckboxTableViewer grandTableViewer;
		private CheckboxTableViewer subTableViewer;
		private List<SubTotalInfo> subList;
		private List<GrandTotalInfo> grandList;
		private int axis;

		public void setInput(List<SubTotalInfo> subList, List<GrandTotalInfo> grandList) {
			this.subList = subList;
			this.grandList = grandList;
		}

		public void setAxis(int axis) {
			this.axis = axis;
		}

		private void init() {

			if (subList != null) {
				subTableViewer.setInput(subList);
				for (int i = 0; i < subTableViewer.getTable().getItemCount(); i++) {
					TableItem item = subTableViewer.getTable().getItem(i);
					if (item.getData() != null && item.getData() instanceof SubTotalInfo) {
						item.setChecked(((SubTotalInfo) item.getData()).isAggregationOn());
					}
				}
				subTableViewer.getTable().addSelectionListener(new SelectionAdapter() {

					public void widgetSelected(SelectionEvent e) {
						TableItem item = (TableItem) e.item;
						if (item != null && item.getData() != null && item.getData() instanceof SubTotalInfo) {
							SubTotalInfo info = (SubTotalInfo) item.getData();
							info.setAggregationOn(item.getChecked());
							if (info.isAssociation()) {
								for (int i = 0; i < subTableViewer.getTable().getItemCount(); i++) {
									TableItem temp = subTableViewer.getTable().getItem(i);
									if (temp == item)
										continue;
									if (temp.getData() != null && temp.getData() instanceof SubTotalInfo) {
										if (((SubTotalInfo) temp.getData()).getLevel() == info.getLevel()) {
											temp.setChecked(item.getChecked());
											((SubTotalInfo) temp.getData()).setAggregationOn(item.getChecked());
										}
									}
								}
							}
						}
					}

				});
			}

			if (grandList != null) {
				grandTableViewer.setInput(grandList);
				for (int i = 0; i < grandTableViewer.getTable().getItemCount(); i++) {
					TableItem item = grandTableViewer.getTable().getItem(i);
					if (item.getData() != null && item.getData() instanceof GrandTotalInfo) {
						item.setChecked(((GrandTotalInfo) item.getData()).isAggregationOn());
					}
				}
				grandTableViewer.getTable().addSelectionListener(new SelectionAdapter() {

					public void widgetSelected(SelectionEvent e) {
						TableItem item = (TableItem) e.item;
						if (item != null && item.getData() != null && item.getData() instanceof GrandTotalInfo) {
							GrandTotalInfo info = (GrandTotalInfo) item.getData();
							info.setAggregationOn(item.getChecked());
							if (info.isAssociation()) {
								for (int i = 0; i < grandTableViewer.getTable().getItemCount(); i++) {
									TableItem temp = grandTableViewer.getTable().getItem(i);
									if (temp == item)
										continue;
									if (temp.getData() != null && temp.getData() instanceof GrandTotalInfo) {
										temp.setChecked(item.getChecked());
										((GrandTotalInfo) temp.getData()).setAggregationOn(item.getChecked());
									}
								}
							}
						}
					}

				});
			}
		}

		private void createGrandTotalArea(Composite content) {
			CLabel grandTotalLabel = new CLabel(content, SWT.NONE);

			grandTotalLabel.setText(Messages.getString("AggregationDialog.Label.Grand")); //$NON-NLS-1$
			if (axis == ICrosstabConstants.ROW_AXIS_TYPE) {
				grandTotalLabel.setImage(CrosstabUIHelper.getImage(CrosstabUIHelper.ROW_GRAND_TOTAL_IMAGE));
			} else if (axis == ICrosstabConstants.COLUMN_AXIS_TYPE) {
				grandTotalLabel.setImage(CrosstabUIHelper.getImage(CrosstabUIHelper.COLUMN_GRAND_TOTAL_IMAGE));
			}

			Table table = new Table(content,
					SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.CHECK);
			table.setLinesVisible(false);
			table.setHeaderVisible(true);

			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.minimumHeight = 200;
			table.setLayoutData(gd);

			grandTableViewer = new CheckboxTableViewer(table);
			GrandTotalProvider provider = new GrandTotalProvider(grandTableViewer, crosstab, axis);

			String[] columnNames = provider.getColumnNames();
			int[] columnWidths = provider.columnWidths();
			for (int i = 0; i < columnNames.length; i++) {
				TableColumn column = new TableColumn(table, SWT.LEFT);
				column.setText(columnNames[i]);
				column.setWidth(columnWidths[i]);
			}

			grandTableViewer.setUseHashlookup(true);
			grandTableViewer.setColumnProperties(provider.getColumnNames());
			grandTableViewer.setCellEditors(provider.getCellEditors());
			grandTableViewer.setCellModifier(provider);
			grandTableViewer.setCellEditors(provider.getCellEditors());
			grandTableViewer.setContentProvider(provider);
			grandTableViewer.setLabelProvider(provider);
			grandTableViewer.setCellModifier(provider);

		}

		private void createSubTotalArea(Composite content) {
			CLabel subTotalLabel = new CLabel(content, SWT.NONE);
			subTotalLabel.setText(Messages.getString("AggregationDialog.Label.Sub")); //$NON-NLS-1$
			if (axis == ICrosstabConstants.ROW_AXIS_TYPE) {
				subTotalLabel.setImage(CrosstabUIHelper.getImage(CrosstabUIHelper.ROW_SUB_TOTAL_IMAGE));
			} else if (axis == ICrosstabConstants.COLUMN_AXIS_TYPE) {
				subTotalLabel.setImage(CrosstabUIHelper.getImage(CrosstabUIHelper.COLUMN_SUB_TOTAL_IMAGE));
			}

			Table table = new Table(content,
					SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.CHECK);
			table.setLinesVisible(false);
			table.setHeaderVisible(true);

			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.minimumHeight = 200;
			table.setLayoutData(gd);

			subTableViewer = new CheckboxTableViewer(table);
			SubTotalProvider provider = new SubTotalProvider(subTableViewer, crosstab, axis);

			String[] columnNames = provider.getColumnNames();
			int[] columnWidths = provider.columnWidths();
			for (int i = 0; i < columnNames.length; i++) {
				TableColumn column = new TableColumn(table, SWT.LEFT);
				column.setText(columnNames[i]);
				column.setWidth(columnWidths[i]);
			}

			subTableViewer.setUseHashlookup(true);
			subTableViewer.setColumnProperties(provider.getColumnNames());
			subTableViewer.setCellEditors(provider.getCellEditors());
			subTableViewer.setContentProvider(provider);
			subTableViewer.setLabelProvider(provider);
			subTableViewer.setCellModifier(provider);

		}
	}

	/**
	 * SubTotalInfo
	 */
	public static class SubTotalInfo {
		private String expectedView = ""; //$NON-NLS-1$
		private LevelHandle level;
		private LevelViewHandle levelView;

		private String measureQualifiedName = "";
		private String measureDisplayName = "";

		private boolean aggregationOn = false;

		private boolean isAssociation = false;

		private String function = ""; //$NON-NLS-1$

		public SubTotalInfo copy() {
			SubTotalInfo retValue = new SubTotalInfo();

			retValue.setAggregationOn(isAggregationOn());
			retValue.setFunction(getFunction());
			retValue.setLevelView(getLevelView());
			retValue.setAssociation(isAssociation());
			retValue.setExpectedView(expectedView);
			retValue.setAggregateOnMeasureName(getAggregateOnMeasureName());
			retValue.setAggregateOnMeasureDisplayName(getAggregateOnMeasureDisplayName());
			return retValue;
		}

		public String getPosition() {
			return levelView.getAggregationHeaderLocation();
		}

		public void setPosition(String pos) {
			try {
				levelView.setAggregationHeaderLocation(pos);
			} catch (SemanticException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public String getExpectedView() {
			return expectedView;
		}

		public void setExpectedView(String expectedView) {
			this.expectedView = expectedView;
		}

		public String getAggregateOnMeasureName() {
			return measureQualifiedName;
		}

		public String getAggregateOnMeasureDisplayName() {
			return measureDisplayName;
		}

		public String getFunction() {
			return function;
		}

		public LevelHandle getLevel() {
			return level;
		}

		public LevelViewHandle getLevelView() {
			return levelView;
		}

		public boolean isAggregationOn() {
			return aggregationOn;
		}

		public void setAggregateOnMeasureName(String name) {
			this.measureQualifiedName = name;
		}

		public void setAggregateOnMeasureDisplayName(String displayName) {
			this.measureDisplayName = displayName;
		}

		public void setAggregationOn(boolean aggregationOn) {
			this.aggregationOn = aggregationOn;
		}

		public void setFunction(String function) {
			this.function = function;
		}

		public void setLevelView(LevelViewHandle levelView) {
			this.levelView = levelView;
			this.level = levelView.getCubeLevel();
		}

		public boolean isSameInfo(Object obj) {
			if (!(obj instanceof SubTotalInfo)) {
				return false;
			}
			SubTotalInfo temp = (SubTotalInfo) obj;
			return temp.getLevel() == level && temp.getAggregateOnMeasureName().equals(measureQualifiedName);
		}

		public boolean equals(Object obj) {
			if (!(obj instanceof SubTotalInfo)) {
				return false;
			}
			SubTotalInfo temp = (SubTotalInfo) obj;
			return temp.getLevel() == level
					&& StringUtil.isEqual(temp.getAggregateOnMeasureName(), measureQualifiedName)
					&& StringUtil.isEqual(temp.getFunction(), function) && temp.isAggregationOn() == aggregationOn
					&& StringUtil.isEqual(temp.getExpectedView(), expectedView)
					&& temp.getPosition().equals(levelView.getAggregationHeaderLocation());
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			int hash = 31;
			hash = hash * 31 + level.hashCode();
			hash = hash * 31 + (measureQualifiedName == null ? 0 : measureQualifiedName.hashCode());
			return hash;
		}

		public boolean isAssociation() {
			return isAssociation;
		}

		public void setAssociation(boolean isAssociation) {
			this.isAssociation = isAssociation;
		}
	}

	/**
	 * GrandTotalInfo
	 */
	public static class GrandTotalInfo {

		private String expectedView = ""; //$NON-NLS-1$

		// Use MeasureName instead of MeasureHandle
//		private MeasureHandle measure;	
		private CrosstabViewHandle viewHandle;

		private String measureQualifiedName = "";

		private String measureDisplayName = "";

		private boolean aggregationOn = false;

		private String function = ""; //$NON-NLS-1$

		private boolean isAssociation = false;

		public GrandTotalInfo copy() {
			GrandTotalInfo retValue = new GrandTotalInfo();
			retValue.setAggregationOn(isAggregationOn());
			retValue.setFunction(getFunction());
			retValue.setMeasureQualifiedName(getMeasureQualifiedName());
			retValue.setMeasureDisplayName(getMeasureDisplayName());
			retValue.setAssociation(isAssociation());
			retValue.setExpectedView(expectedView);
			retValue.setViewHandle(getViewHandle());
			retValue.setPosition(getPosition());
			return retValue;
		}

		public String getPosition() {
			return viewHandle.getGrandTotalLocation();
		}

		public void setPosition(String position) {
			try {
				viewHandle.setGrandTotalLocation(position);
			} catch (SemanticException e) {
				// do nothing now
			}
		}

		public CrosstabViewHandle getViewHandle() {
			return viewHandle;
		}

		public void setViewHandle(CrosstabViewHandle viewHandle) {
			this.viewHandle = viewHandle;
		}

		public String getExpectedView() {
			return expectedView;
		}

		public void setExpectedView(String expectedView) {
			this.expectedView = expectedView;
		}

		public String getFunction() {
			return function;
		}

		public String getMeasureQualifiedName() {
			return measureQualifiedName;
		}

		public String getMeasureDisplayName() {
			return measureDisplayName;
		}

		public boolean isAggregationOn() {
			return aggregationOn;
		}

		public void setAggregationOn(boolean aggregationOn) {
			this.aggregationOn = aggregationOn;
		}

		public void setFunction(String function) {
			this.function = function;
		}

		public void setMeasureQualifiedName(String name) {
			this.measureQualifiedName = name;
		}

		public void setMeasureDisplayName(String displayName) {
			this.measureDisplayName = displayName;
		}

		public boolean isSameInfo(Object obj) {
			if (!(obj instanceof GrandTotalInfo)) {
				return false;
			}
			GrandTotalInfo temp = (GrandTotalInfo) obj;
			return temp.getMeasureQualifiedName().equals(measureQualifiedName);
		}

		public boolean isAssociation() {
			return isAssociation;
		}

		public void setAssociation(boolean isAssociation) {
			this.isAssociation = isAssociation;
		}

	}
}
