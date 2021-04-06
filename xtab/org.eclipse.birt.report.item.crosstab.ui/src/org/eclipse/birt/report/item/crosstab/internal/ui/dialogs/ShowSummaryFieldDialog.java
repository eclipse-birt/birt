/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider;
import org.eclipse.birt.report.item.crosstab.ui.extension.SwitchCellInfo;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ShowSummaryFieldDialog extends BaseDialog {

	private String[] columnNames = new String[] { Messages.getString("ShowSummaryFieldDialog.Column.Measures"), //$NON-NLS-1$
			Messages.getString("ShowSummaryFieldDialog.Column.View") }; //$NON-NLS-1$
	private int[] columnWidth = new int[] { 230, 130 };
	private CellEditor[] cellEditor;

	private String[] comboItems = null;
	// private IAggregationCellViewProvider[] providers;
	private String[] viewNames;

	private CrosstabReportItemHandle crosstab;
	private AggregationCellProviderWrapper cellProviderWrapper;

	private void setCrosstab(CrosstabReportItemHandle crosstab) {
		this.crosstab = crosstab;
		cellProviderWrapper = new AggregationCellProviderWrapper(crosstab);
	}

	public ShowSummaryFieldDialog(Shell parentShell, CrosstabReportItemHandle crosstab) {
		super(parentShell, Messages.getString("ShowSummaryFieldDialog.Title")); //$NON-NLS-1$
		setCrosstab(crosstab);
	}

	protected Control createDialogArea(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.XTAB_SHOW_SUMMARY_FIELD_DIALOG);

		Composite dialogArea = (Composite) super.createDialogArea(parent);

		Label infoLabel = new Label(dialogArea, SWT.NONE);
		infoLabel.setText(Messages.getString("ShowSummaryFieldDialog.Label.Info")); //$NON-NLS-1$

		createSummaryFiledViewer(dialogArea);

		init();

		return dialogArea;
	}

	private List input = new ArrayList();
	private CheckboxTableViewer summaryFieldViewer;

	public void setInput(List input) {
		if (input != null)
			this.input.addAll(input);
	}

	private ICellModifier cellModifier = new ICellModifier() {

		public boolean canModify(Object element, String property) {
			// TODO Auto-generated method stub
			if (element instanceof Item) {
				element = ((Item) element).getData();
			}
//			MeasureInfo info = (MeasureInfo) element;
//			MeasureViewHandle measureView = crosstab.getMeasure( info.getMeasureName( ) );
//			if (measureView == null
//					|| measureView instanceof ComputedMeasureViewHandle
//					|| (measureView.getCubeMeasure() != null && measureView.getCubeMeasure().isCalculated()))
//			{
//				return false;
//			}

			if (Arrays.asList(columnNames).indexOf(property) == 1) {
				return summaryFieldViewer.getChecked(element);
			} else {
				return false;
			}
		}

		public Object getValue(Object element, String property) {
			if (element instanceof Item) {
				element = ((Item) element).getData();
			}
			Object value = null;
			// TODO Auto-generated method stub
			int index = Arrays.asList(columnNames).indexOf(property);
			switch (index) {
			case 0:
				value = "Measure"; //$NON-NLS-1$
				break;
			case 1:
				initializeItems((MeasureInfo) element);
				((ComboBoxCellEditor) cellEditor[1]).setItems(comboItems);
				String expectedView = ((MeasureInfo) (element)).getExpectedView();
				if (expectedView == null) {
					expectedView = "";
				}
				int sel = Arrays.asList(viewNames).indexOf(expectedView);
				value = sel <= 0 ? Integer.valueOf(0) : Integer.valueOf(sel);
				break;
			default:
			}
			return value;
		}

		public void modify(Object element, String property, Object value) {
			// TODO Auto-generated method stub

			if (element instanceof Item) {
				element = ((Item) element).getData();
			}

			int index = Arrays.asList(columnNames).indexOf(property);
			switch (index) {
			case 0:
				break;
			case 1:
				int sel = ((Integer) value).intValue();
				if (sel < 0) {
					((MeasureInfo) (element)).setExpectedView(""); //$NON-NLS-1$
				} else {
					((MeasureInfo) element).setExpectedView(viewNames[sel]);
				}
				break;
			default:
			}
			summaryFieldViewer.refresh();
		}

	};

	private void createSummaryFiledViewer(Composite dialogArea) {
		Table table = new Table(dialogArea,
				SWT.BORDER | SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.CHECK);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 250;
		table.setLayoutData(gd);

		summaryFieldViewer = new CheckboxTableViewer(table);
		SummaryFieldProvider provider = new SummaryFieldProvider();

		for (int i = 0; i < columnNames.length; i++) {
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setText(columnNames[i]);
			column.setWidth(columnWidth[i]);
		}
		ComboBoxCellEditor comboCell = new ComboBoxCellEditor(table, new String[0], SWT.READ_ONLY);
		// TextCellEditor textCell = new TextCellEditor(table, SWT.NONE);
		cellEditor = new CellEditor[] { null, comboCell };
		summaryFieldViewer.setColumnProperties(columnNames);
		summaryFieldViewer.setCellEditors(cellEditor);
		summaryFieldViewer.setCellModifier(cellModifier);
		summaryFieldViewer.setUseHashlookup(true);
		summaryFieldViewer.setContentProvider(provider);
		summaryFieldViewer.setLabelProvider(provider);

		summaryFieldViewer.addCheckStateListener(new ICheckStateListener() {

			public void checkStateChanged(CheckStateChangedEvent event) {
				MeasureInfo info = (MeasureInfo) event.getElement();
				if (event.getChecked()) {
					info.setShow(true);
				} else {
					info.setShow(false);
				}
				checkOKButtonStatus();
			}

		});
	}

	private void init() {
		if (input != null) {
			summaryFieldViewer.setInput(input);
			for (int i = 0; i < summaryFieldViewer.getTable().getItemCount(); i++) {
				TableItem item = summaryFieldViewer.getTable().getItem(i);
				if (item.getData() != null && item.getData() instanceof MeasureInfo) {
					item.setChecked(((MeasureInfo) item.getData()).isShow());
				}
			}
		}

	}

	public Object getResult() {
		return input;
	}

	private void checkOKButtonStatus() {
		int count = 0;
		int listSize = input.size();
		for (int i = 0; i < listSize; i++) {
			MeasureInfo measureInfo = (MeasureInfo) input.get(i);
			if (measureInfo.isShow()) {
				count++;
			}
		}
		if (count <= 0 && getOkButton() != null) {
			getOkButton().setEnabled(false);
		} else if (getOkButton() != null) {
			getOkButton().setEnabled(true);
		}

	}

	class SummaryFieldProvider extends LabelProvider implements ITableLabelProvider, IStructuredContentProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			Image image = null;
			switch (columnIndex) {
			case 0:
				image = CrosstabUIHelper.getImage(CrosstabUIHelper.MEASURE_IMAGE);
				break;
			case 1:
				break;
			default:
			}
			return image;

		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof MeasureInfo) {
				if (columnIndex == 0) {
					return ((MeasureInfo) element).getMeasureDisplayName() == null ? "" //$NON-NLS-1$
							: ((MeasureInfo) element).getMeasureDisplayName();
				} else {
					initializeItems((MeasureInfo) element);
					((ComboBoxCellEditor) cellEditor[1]).setItems(comboItems);

					String expectedView = ((MeasureInfo) element).getExpectedView();
					if (expectedView == null) {
						return comboItems[0];
					} else {
						int index = Arrays.asList(viewNames).indexOf(expectedView);
						if (index < 0) {
							index = 0;
							((MeasureInfo) element).setExpectedView(viewNames[index]);
						}
						return comboItems[index];
					}

				}
			}

			return ""; //$NON-NLS-1$
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List)
				return ((List) inputElement).toArray();
			return new Object[0];
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * MeasureInfo
	 */
	public static class MeasureInfo {

		// private MeasureHandle measure ;
		private String measureName = "";
		private String measureDisplayName = "";

		private boolean isShow = false;

		private String expectedView = ""; //$NON-NLS-1$

		public MeasureInfo copy() {
			MeasureInfo retValue = new MeasureInfo();
			retValue.setShow(isShow());
			retValue.setMeasureName(getMeasureName());
			retValue.setMeasureDisplayName(getMeasureDisplayName());
			retValue.setExpectedView(expectedView);
			return retValue;
		}

		public boolean isSameInfo(MeasureInfo comparedOne) {
			if (comparedOne.measureName.equals(this.measureName)) {
				return true;
			} else {
				return false;
			}
		}

		public void setExpectedView(String view) {
			this.expectedView = view;
		}

		public String getExpectedView() {
			return this.expectedView;
		}

		public void setShow(boolean show) {
			isShow = show;
		}

		public boolean isShow() {
			return isShow;
		}

		// public MeasureHandle getMeasure( )
		// {
		// return measure;
		// }
		//
		// public void setMeasure( MeasureHandle measure )
		// {
		// this.measure = measure;
		// }

		public String getMeasureName() {
			return measureName;
		}

		public void setMeasureName(String name) {
			measureName = name;
		}

		public String getMeasureDisplayName() {
			return measureDisplayName;
		}

		public void setMeasureDisplayName(String displayName) {
			measureDisplayName = displayName;
		}

		// public boolean isSameInfo( Object obj )
		// {
		// if ( !( obj instanceof MeasureInfo ) )
		// {
		// return false;
		// }
		// MeasureInfo temp = (MeasureInfo) obj;
		// return temp.getMeasure( ) == measure;
		// }

		public boolean equals(Object obj) {
			if (!(obj instanceof MeasureInfo)) {
				return false;
			}
			MeasureInfo temp = (MeasureInfo) obj;
			return StringUtil.isEqual(temp.getMeasureName(), measureName) && temp.isShow() == isShow
					&& StringUtil.isEqual(temp.getExpectedView(), expectedView);
		}

		public int hashCode() {
			int hash = 31;
			hash = hash * 31 + measureName.hashCode();
			hash = hash * 31 + (expectedView == null ? 0 : expectedView.hashCode());
			return hash;
		}
	}

	private void initializeItems(MeasureInfo measureInfo) {
		List<String> viewNameList = new ArrayList<String>();
		List<String> itemList = new ArrayList<String>();

		MeasureViewHandle measureView = crosstab.getMeasure(measureInfo.getMeasureName());
		if (measureView != null && measureView instanceof ComputedMeasureViewHandle) {
			itemList.add("");
			viewNameList.add(""); //$NON-NLS-1$
		} else
		// NOT ComputedMeasureViewHandle -- begin --
		{
			AggregationCellHandle cell = getAggregationCell(measureInfo);
			if (cell != null && cellProviderWrapper.getMatchProvider(cell) == null) {
				itemList.add("");
				viewNameList.add(""); //$NON-NLS-1$
			}

			IAggregationCellViewProvider providers[] = cellProviderWrapper.getAllProviders();
			for (int i = 0; i < providers.length; i++) {
				IAggregationCellViewProvider tmp = (IAggregationCellViewProvider) providers[i];
				if (tmp == null) {
					continue;
				}
				SwitchCellInfo info = new SwitchCellInfo(crosstab, SwitchCellInfo.MEASURE);
				info.setMeasureInfo(measureInfo);

				if (!providers[i].canSwitch(info)) {
					continue;
				}

				String displayName = tmp.getViewDisplayName();
				viewNameList.add(tmp.getViewName());
				itemList.add(Messages.getString("GrandTotalProvider.ShowAs", //$NON-NLS-1$
						new String[] { displayName }));
			}
		} // NOT ComputedMeasureViewHandle -- end --
		comboItems = (String[]) itemList.toArray(new String[itemList.size()]);
		viewNames = (String[]) viewNameList.toArray(new String[viewNameList.size()]);
	}

	private AggregationCellHandle getAggregationCell(MeasureInfo measureInfo) {
		AggregationCellHandle cell = null;
		// MeasureHandle measure = measureInfo.getMeasure( );
		// if(measure == null)
		// {
		// return cell;
		// }
		MeasureViewHandle measureView = crosstab.getMeasure(measureInfo.getMeasureName());
		if (measureView == null) {
			return cell;
		}

		cell = measureView.getCell();
		return cell;
	}
}
