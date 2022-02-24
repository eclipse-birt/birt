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
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.AggregationDialog.SubTotalInfo;
import org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider;
import org.eclipse.birt.report.item.crosstab.ui.extension.SwitchCellInfo;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;

public class SubTotalProvider extends TotalProvider
		implements ITableLabelProvider, IStructuredContentProvider, ICellModifier {

	// private int axis;
	private CellEditor[] cellEditor;
	TableViewer viewer;

	private String[] comboItems = null;
	private String[] viewNames;

	private CrosstabReportItemHandle crosstab;
	private AggregationCellProviderWrapper cellProviderWrapper;

	private static String[] positionItems = null;
	private static String[] positionValues = null;
	private static IChoiceSet choiceSet;
	static {
		choiceSet = ChoiceSetFactory.getElementChoiceSet(ICrosstabConstants.LEVEL_VIEW_EXTENSION_NAME,
				ILevelViewConstants.AGGREGATION_HEADER_LOCATION_PROP);

		IChoice[] choices = choiceSet.getChoices(new AlphabeticallyComparator());

		positionItems = new String[choices.length];
		positionValues = new String[choices.length];
		for (int i = 0; i < choices.length; i++) {
			positionValues[i] = choices[i].getName();
			positionItems[i] = choices[i].getDisplayName();
		}

	}

	private void initializeItems(SubTotalInfo subTotalInfo) {
		List viewNameList = new ArrayList();
		List itemList = new ArrayList();

		AggregationCellHandle cell = getAggregationCell(subTotalInfo);
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

			SwitchCellInfo info = new SwitchCellInfo(crosstab, SwitchCellInfo.SUB_TOTAL);
			info.setSubTotalInfo(subTotalInfo);

			if (!providers[i].canSwitch(info)) {
				continue;
			}
			String displayName = tmp.getViewDisplayName();
			viewNameList.add(tmp.getViewName());
			itemList.add(Messages.getString("GrandTotalProvider.ShowAs", //$NON-NLS-1$
					new String[] { displayName }));
		}
		comboItems = (String[]) itemList.toArray(new String[itemList.size()]);
		viewNames = (String[]) viewNameList.toArray(new String[viewNameList.size()]);
	}

	public SubTotalProvider(TableViewer viewer, CrosstabReportItemHandle crosstab, int axis) {
		this.viewer = viewer;
		this.crosstab = crosstab;
		// this.axis = axis;

		cellProviderWrapper = new AggregationCellProviderWrapper(crosstab);
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	// private CellEditor[] editors;
	private String[] columnNames = new String[] { "", Messages.getString("SubTotalProvider.Column.AggregateOn"), //$NON-NLS-2$
			Messages.getString("SubTotalProvider.Column.View"), //$NON-NLS-1$
			Messages.getString("SubTotalProvider.Column.Position") //$NON-NLS-1$
	};

	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		SubTotalInfo info = (SubTotalInfo) element;
		switch (columnIndex) {
		case 0:
			return ""; //$NON-NLS-1$
		case 1:
			return info.getLevel().getName() + "- " + (info.getAggregateOnMeasureDisplayName() == null ? "" //$NON-NLS-1$ //$NON-NLS-2$
					: info.getAggregateOnMeasureDisplayName());
		case 2:
			initializeItems(info);
			((ComboBoxCellEditor) cellEditor[2]).setItems(comboItems);

			String expectedView = info.getExpectedView();
			if (expectedView == null) {
				expectedView = "";
			}
			int index = Arrays.asList(viewNames).indexOf(expectedView);
			if (index < 0) {
				index = 0;
				info.setExpectedView(viewNames[index]);
			}
			return comboItems[index];
		case 3:
			String position = info.getPosition();
			if (position == null) {
				position = "";
			}
			int posIndex = Arrays.asList(positionValues).indexOf(position);
			if (posIndex < 0) {
				info.setPosition(positionValues[0]);
			}
			return positionItems[posIndex];
		default:
			break;
		}
		return ""; //$NON-NLS-1$
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List)
			return ((List) inputElement).toArray();
		return new Object[] {};

	}

	public CellEditor[] getCellEditors() {
		if (cellEditor != null) {
			return cellEditor;
		}

		ComboBoxCellEditor comboCell = new ComboBoxCellEditor(viewer.getTable(), new String[0], SWT.READ_ONLY);
		ComboBoxCellEditor positionCell = new ComboBoxCellEditor(viewer.getTable(), positionItems, SWT.READ_ONLY);
		cellEditor = new CellEditor[] { null, null, comboCell, positionCell };
		return cellEditor;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public int[] columnWidths() {
		Shell shell = new Shell();
		GC gc = new GC(shell);
		int height = gc.stringExtent("").y;
		gc.dispose();
		shell.dispose();

		return new int[] { height + (int) ((((float) height) / 12) * 8), 210, 120, 120 };
	}

	public boolean canModify(Object element, String property) {
		// TODO Auto-generated method stub
		if (Arrays.asList(columnNames).indexOf(property) == 2 || Arrays.asList(columnNames).indexOf(property) == 3) {
			if (viewer instanceof CheckboxTableViewer) {
				return ((CheckboxTableViewer) viewer).getChecked(element);
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public Object getValue(Object element, String property) {
		// TODO Auto-generated method stub
		if (element instanceof Item) {
			element = ((Item) element).getData();
		}
		Object value = null;
		int index = Arrays.asList(columnNames).indexOf(property);
		switch (index) {
		case 1:
			break;
		case 2:
			initializeItems((SubTotalInfo) element);
			((ComboBoxCellEditor) cellEditor[2]).setItems(comboItems);
			String expectedView = ((SubTotalInfo) (element)).getExpectedView();
			if (expectedView == null || expectedView.length() == 0) {
				return Integer.valueOf(0);
			}
			int sel = Arrays.asList(viewNames).indexOf(expectedView);
			value = sel <= 0 ? Integer.valueOf(0) : Integer.valueOf(sel);
			break;
		case 3:
			String pos = ((SubTotalInfo) (element)).getPosition();
			if (pos == null || pos.length() == 0) {
				return Integer.valueOf(0);
			}
			int posIndex = Arrays.asList(positionValues).indexOf(pos);
			value = posIndex <= 0 ? Integer.valueOf(0) : Integer.valueOf(posIndex);
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
			break;
		case 2:
			int sel = ((Integer) value).intValue();
			if (sel == 0) {
				((SubTotalInfo) (element)).setExpectedView(""); //$NON-NLS-1$
			} else {
				((SubTotalInfo) element).setExpectedView(viewNames[sel]);
			}
			break;
		case 3:
			int posIndex = ((Integer) value).intValue();
			((SubTotalInfo) element).setPosition(positionValues[posIndex]);

		default:
		}
		viewer.refresh();
	}

	private LevelViewHandle findLevelViewHandle(LevelHandle handle) {

		int dimCount = crosstab.getDimensionCount(ICrosstabConstants.ROW_AXIS_TYPE);
		for (int i = 0; i < dimCount; i++) {
			DimensionViewHandle tmpDimView = crosstab.getDimension(ICrosstabConstants.ROW_AXIS_TYPE, i);
			LevelViewHandle levelView = tmpDimView.getLevel(handle.getQualifiedName());
			if (levelView != null) {
				return levelView;
			}
		}

		dimCount = crosstab.getDimensionCount(ICrosstabConstants.COLUMN_AXIS_TYPE);
		for (int i = 0; i < dimCount; i++) {
			DimensionViewHandle tmpDimView = crosstab.getDimension(ICrosstabConstants.COLUMN_AXIS_TYPE, i);
			LevelViewHandle levelView = tmpDimView.getLevel(handle.getQualifiedName());
			if (levelView != null) {
				return levelView;
			}
		}

		return null;
	}

	private AggregationCellHandle getAggregationCell(SubTotalInfo subTotalInfo) {
		AggregationCellHandle cell = null;
		// MeasureHandle measure = subTotalInfo.getAggregateOnMeasure();
		LevelHandle level = subTotalInfo.getLevel();
		if (level == null) {
			return cell;
		}
		MeasureViewHandle measureView = crosstab.getMeasure(subTotalInfo.getAggregateOnMeasureName());
		LevelViewHandle levelView = findLevelViewHandle(level);
		if (measureView == null || levelView == null) {
			return cell;
		}

		String rowDimension = null;
		String rowLevel = null;
		String colDimension = null;
		String colLevel = null;

		int axisType = levelView.getAxisType();

		int counterAxisType = CrosstabUtil.getOppositeAxisType(levelView.getAxisType());
		DimensionViewHandle counterDimension = crosstab.getDimension(counterAxisType,
				crosstab.getDimensionCount(counterAxisType) - 1);

		String counterDimensionName = null;
		String counterLevelName = null;
		if (counterDimension != null) {
			counterDimensionName = counterDimension.getCubeDimensionName();
			counterLevelName = counterDimension.getLevel(counterDimension.getLevelCount() - 1).getCubeLevelName();
		}

		String dimensionName = ((DimensionViewHandle) levelView.getContainer()).getCubeDimensionName();
		String levelName = levelView.getCubeLevelName();
		if (levelName == null || dimensionName == null)
			return cell;

		if (axisType == ICrosstabConstants.ROW_AXIS_TYPE) {
			rowDimension = dimensionName;
			rowLevel = levelName;
			colDimension = counterDimensionName;
			colLevel = counterLevelName;
		} else if (axisType == ICrosstabConstants.COLUMN_AXIS_TYPE) {
			colDimension = dimensionName;
			colLevel = levelName;
			rowDimension = counterDimensionName;
			rowLevel = counterLevelName;
		}

		cell = measureView.getAggregationCell(rowDimension, rowLevel, colDimension, colLevel);

		return cell;
	}
}
