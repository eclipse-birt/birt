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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFormHandleProvider;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.dialogs.CrosstabSubTotalDialog;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureModel;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;

/**
 *
 */

public class SubTotalProvider extends AbstractFormHandleProvider {
	protected static final Logger logger = Logger.getLogger(SubTotalProvider.class.getName());

	private CellEditor[] editors;
	private String[] columnNames = { Messages.getString("CrosstabSubToatalProvider.Column.AggregateOn"), //$NON-NLS-1$
			Messages.getString("CrosstabSubToatalProvider.Column.DataField"), };

	private int[] columnWidths = { 160, 160, 200 };

	private int axis;

	public void setAxis(int axis) {
		this.axis = axis;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IFormProvider#canModify(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean canModify(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IFormProvider#doAddItem(int)
	 */
	@Override
	public boolean doAddItem(int pos) throws Exception {
		// TODO Auto-generated method stub
		CrosstabReportItemHandle reportHandle = null;
		try {
			reportHandle = (CrosstabReportItemHandle) ((ExtendedItemHandle) (((List) input)).get(0)).getReportItem();
		} catch (ExtendedElementException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		CrosstabSubTotalDialog subTotalDialog = new CrosstabSubTotalDialog(reportHandle, axis);
		if (subTotalDialog.open() == Dialog.CANCEL) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IFormProvider#doDeleteItem(int)
	 */
	@Override
	public boolean doDeleteItem(int pos) throws Exception {
		// TODO Auto-generated method stub
		SubTotalInfo subTotalInfo = (SubTotalInfo) getElements(input)[pos];
		LevelViewHandle levelViewHandle = subTotalInfo.level;
//		MeasureViewHandle measureViewHandle = subTotalInfo.measure;
		String measureName = subTotalInfo.measureName;

		ExtendedItemHandle extendedItem = (ExtendedItemHandle) (((List) input)).get(0);
		List tmpMeasures = extendedItem.getPropertyHandle(ICrosstabReportItemConstants.MEASURES_PROP).getContents();
		int measureIndex = -1;
		for (int i = 0; i < tmpMeasures.size(); i++) {
			ExtendedItemHandle extHandle = (ExtendedItemHandle) tmpMeasures.get(i);
			try {
				if (((MeasureViewHandle) extHandle.getReportItem()).getCubeMeasureName().equals(measureName)) {
					measureIndex = i;
					break;
				}
			} catch (ExtendedElementException e1) {
				// TODO Auto-generated catch block
				logger.log(Level.SEVERE, e1.getMessage(), e1);
			}
		}

		ExtendedItemHandle extend = (ExtendedItemHandle) DEUtil.getInputFirstElement(this.input);
		CrosstabReportItemHandle crossTab = null;
		try {
			crossTab = (CrosstabReportItemHandle) extend.getReportItem();
		} catch (ExtendedElementException e) {
			ExceptionUtil.handle(e);
			return false;
		}
		if (crossTab == null) {
			return false;
		}

		if (CrosstabUtil.isAggregationAffectAllMeasures(crossTab, axis)) {
			levelViewHandle.removeSubTotal();
		} else {
			levelViewHandle.removeSubTotal(measureIndex);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IFormProvider#doEditItem(int)
	 */
	@Override
	public boolean doEditItem(int pos) {
		// TODO Auto-generated method stub
		CrosstabReportItemHandle reportHandle = null;
		try {
			reportHandle = (CrosstabReportItemHandle) ((ExtendedItemHandle) (((List) input)).get(0)).getReportItem();
		} catch (ExtendedElementException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		CrosstabSubTotalDialog subTotalDialog = new CrosstabSubTotalDialog(reportHandle, axis);
		subTotalDialog.setInput((SubTotalInfo) getElements(input)[pos]);
		if (subTotalDialog.open() == Dialog.CANCEL) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IFormProvider#doMoveItem(int, int)
	 */
	@Override
	public boolean doMoveItem(int oldPos, int newPos) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IFormProvider#getColumnNames()
	 */
	@Override
	public String[] getColumnNames() {
		// TODO Auto-generated method stub
		return columnNames;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IFormProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		SubTotalInfo info = (SubTotalInfo) element;
		switch (columnIndex) {
		case 0:
			return info.level.getCubeLevelName();
		case 1:
			return info.measureName == null ? "" : info.measureName; //$NON-NLS-1$

		case 2:
			if (info.function == null || info.function.trim().equals("")) { //$NON-NLS-1$
				info.function = getFunctionNames()[0];
			}
			return getFunctionDisplayName(info.function);
		default:
			break;
		}
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IFormProvider#getColumnWidths()
	 */
	@Override
	public int[] getColumnWidths() {
		// TODO Auto-generated method stub
		return columnWidths;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IFormProvider#getEditors(org.eclipse.swt.widgets.Table)
	 */
	@Override
	public CellEditor[] getEditors(Table table) {
		// TODO Auto-generated method stub
		if (editors == null) {
			editors = new CellEditor[columnNames.length];
			for (int i = 0; i < columnNames.length; i++) {
				editors[i] = new TextCellEditor();
			}
		}
		return editors;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IFormProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		input = inputElement;
		Object obj = null;
		if (inputElement instanceof List) {
			obj = ((List) inputElement).get(0);
		} else {
			obj = inputElement;
		}

		List list = new ArrayList();
		if (!(obj instanceof ExtendedItemHandle)) {
			return new Object[0];
		}
		ExtendedItemHandle element = (ExtendedItemHandle) obj;
		CrosstabReportItemHandle crossTab = null;
		try {
			crossTab = (CrosstabReportItemHandle) element.getReportItem();
		} catch (ExtendedElementException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		if (crossTab == null) {
			return new Object[0];
		}

		if (crossTab.getCrosstabView(axis) != null) {
			CrosstabViewHandle crosstabView = crossTab.getCrosstabView(axis);
			list.addAll(getLevel(crosstabView));
		}

		return list.toArray();
	}

	private List getLevel(CrosstabViewHandle crosstabViewHandle) {
		List list = new ArrayList();
		if (crosstabViewHandle == null) {
			return list;
		}
		int dimensionCount = crosstabViewHandle.getDimensionCount();

		for (int i = 0; i < dimensionCount; i++) {
			DimensionViewHandle dimension = crosstabViewHandle.getDimension(i);
			int levelCount = dimension.getLevelCount();
			for (int j = 0; j < levelCount; j++) {
				LevelViewHandle levelHandle = dimension.getLevel(j);
				List aggMeasures = levelHandle.getAggregationMeasures();
				for (int k = 0; k < aggMeasures.size(); k++) {
					MeasureViewHandle measure = (MeasureViewHandle) aggMeasures.get(k);
					if (measure instanceof ComputedMeasureViewHandle) {
						continue;
					}
					SubTotalInfo info = new SubTotalInfo();
//					info.measure = measure;
					info.measureName = measure.getCubeMeasureName();
					info.function = levelHandle.getAggregationFunction(measure);
					info.level = levelHandle;
					list.add(info);
				}

			}
		}
		return list;
	}

	/**
	 * GrandTotalInfo
	 */
	public static class SubTotalInfo {

		private LevelViewHandle level = null;
//		private MeasureViewHandle measure = null;
		private String measureName = "";
		private String function = ""; //$NON-NLS-1$

		public LevelViewHandle getLevel() {
			return level;
		}

		public void setLevel(LevelViewHandle level) {
			this.level = level;
		}

		public String getMeasureName() {
			return measureName;
		}

		public void setMeasureName(String name) {
			measureName = name;
		}

		public String getFunction() {
			return function;
		}

		public void setFunction(String function) {
			this.function = function;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IFormProvider#getImagePath(java.lang.Object, int)
	 */
	@Override
	public Image getImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IFormProvider#getValue(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object getValue(Object element, String property) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IFormProvider#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Override
	public boolean modify(Object data, String property, Object value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IFormProvider#needRefreshed(org.eclipse.birt.report.model.api.activity.
	 * NotificationEvent)
	 */
	@Override
	public boolean needRefreshed(NotificationEvent event) {
		if (event instanceof ContentEvent || event instanceof PropertyEvent) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IDescriptorProvider#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return Messages.getString("CrosstabPageGenerator.List.SubTotals"); //$NON-NLS-1$
	}

	public String[] getFunctionNames() {
		IChoice[] choices = getFunctions();
		if (choices == null) {
			return new String[0];
		}

		String[] displayNames = new String[choices.length];
		for (int i = 0; i < choices.length; i++) {
			displayNames[i] = choices[i].getName();
		}
		return displayNames;
	}

//	public String getFunctionDisplayName( String name )
//	{
//		return ChoiceSetFactory.getDisplayNameFromChoiceSet( name,
//				DEUtil.getMetaDataDictionary( )
//						.getChoiceSet( DesignChoiceConstants.CHOICE_MEASURE_FUNCTION ) );
//	}
//
//	private IChoice[] getFunctions( )
//	{
//		return DEUtil.getMetaDataDictionary( )
//				.getChoiceSet( DesignChoiceConstants.CHOICE_MEASURE_FUNCTION )
//				.getChoices( );
//	}
	public String getFunctionDisplayName(String name)

	{
		return ChoiceSetFactory.getDisplayNameFromChoiceSet(name,
				DEUtil.getMetaDataDictionary().getElement(ReportDesignConstants.MEASURE_ELEMENT)
						.getProperty(IMeasureModel.FUNCTION_PROP).getAllowedChoices());

	}

	private IChoice[] getFunctions()

	{
		return DEUtil.getMetaDataDictionary().getElement(ReportDesignConstants.MEASURE_ELEMENT)
				.getProperty(IMeasureModel.FUNCTION_PROP).getAllowedChoices().getChoices();

	}

	@Override
	public boolean isAddEnable(Object selectedObject) {
		return isAddEnable();
	}

	public boolean isAddEnable() {
		ExtendedItemHandle extend = (ExtendedItemHandle) DEUtil.getInputFirstElement(this.input);
		CrosstabReportItemHandle crossTab = null;
		try {
			crossTab = (CrosstabReportItemHandle) extend.getReportItem();
		} catch (ExtendedElementException e) {
			ExceptionUtil.handle(e);
			return false;
		}
		if (crossTab == null) {
			return false;
		}
		CrosstabViewHandle crosstabView = crossTab.getCrosstabView(axis);
		if ((getAllLevelCount(crossTab) - 1) * getMeasureCount(crossTab) - getLevel(crosstabView).size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	private int getAllLevelCount(CrosstabReportItemHandle crosstab) {
		CrosstabViewHandle crosstabView = crosstab.getCrosstabView(axis);
		if (crosstabView == null) {
			return 0;
		}
		int dimCount = crosstabView.getDimensionCount();
		int result = 0;
		for (int i = 0; i < dimCount; i++) {
			DimensionViewHandle dimensionView = crosstabView.getDimension(i);
			int levelCount = dimensionView.getLevelCount();
			for (int j = 0; j < levelCount; j++) {
				result++;
			}
		}
		return result;
	}

	// return measureView count, excluding computed measure
	private int getMeasureCount(CrosstabReportItemHandle crosstab) {
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) crosstab.getModelHandle();
		int allCount = extendedItem.getPropertyHandle(ICrosstabReportItemConstants.MEASURES_PROP).getContentCount();
		int comoputecCount = crosstab.getComputedMeasures().size();
		return allCount - comoputecCount;
	}

}
