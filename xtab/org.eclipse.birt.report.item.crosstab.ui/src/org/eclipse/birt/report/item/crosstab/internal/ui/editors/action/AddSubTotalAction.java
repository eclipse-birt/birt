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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.AggregationDialog;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.AggregationDialog.GrandTotalInfo;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.AggregationDialog.SubTotalInfo;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.extension.AggregationCellViewAdapter;
import org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider;
import org.eclipse.birt.report.item.crosstab.ui.extension.SwitchCellInfo;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;

/**
 * Add the sub total to the level handle.
 */
public class AddSubTotalAction extends AbstractCrosstabAction {

	boolean needUpdateView = false;
	LevelViewHandle levelHandle = null;
	private static final String NAME = Messages.getString("AddSubTotalAction.TransName");//$NON-NLS-1$
	private static final String ID = "add_subtotal";//$NON-NLS-1$
	private static final String TEXT = Messages.getString("AddSubTotalAction.DisplayName");//$NON-NLS-1$

	/**
	 * The name of the label into the sub total cell.
	 */
	// private static final String DISPALY_NAME = "TOTAL";
	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public AddSubTotalAction(DesignElementHandle handle) {
		super(handle);
		setId(ID);
		setText(TEXT);
		ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle(handle);
		setHandle(extendedHandle);
		levelHandle = CrosstabAdaptUtil.getLevelViewHandle(extendedHandle);

		Image image = CrosstabUIHelper.getImage(CrosstabUIHelper.LEVEL_AGGREGATION);
		setImageDescriptor(ImageDescriptor.createFromImage(image));
	}

	private AggregationCellProviderWrapper providerWrapper;

	private void initializeProviders() {
		providerWrapper = new AggregationCellProviderWrapper(
				(ExtendedItemHandle) levelHandle.getCrosstab().getModelHandle());
	}

	public boolean isEnabled() {
		boolean result = (levelHandle == null) || (levelHandle.getCrosstab() == null)
				|| (levelHandle.getCrosstab().getCube() == null);
		if (result) {
			return false;
		}
		return !DEUtil.isReferenceElement(levelHandle.getCrosstabHandle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		transStar(NAME);
		try {

			AggregationDialog dialog = new AggregationDialog(UIUtil.getDefaultShell(), levelHandle.getCrosstab());
			dialog.setAxis(levelHandle.getAxisType());
			List<AggregationDialog.SubTotalInfo> rowSubTotals = getSubTotalInfo(ICrosstabConstants.ROW_AXIS_TYPE);
			List<AggregationDialog.GrandTotalInfo> rowGrandTotals = getGrandTotalInfo(ICrosstabConstants.ROW_AXIS_TYPE);
			List<AggregationDialog.SubTotalInfo> colSubTotals = getSubTotalInfo(ICrosstabConstants.COLUMN_AXIS_TYPE);
			List<AggregationDialog.GrandTotalInfo> colGrandTotals = getGrandTotalInfo(
					ICrosstabConstants.COLUMN_AXIS_TYPE);
			dialog.setInput(copySubTotal(rowSubTotals), copyGrandTotal(rowGrandTotals), copySubTotal(colSubTotals),
					copyGrandTotal(colGrandTotals));
			if (dialog.open() == Window.OK) {
				initializeProviders();

				Object[] result = (Object[]) dialog.getResult();
				processSubTotal(rowSubTotals, (List<AggregationDialog.SubTotalInfo>) result[0]);
				processGrandTotal(rowGrandTotals, (List<AggregationDialog.GrandTotalInfo>) result[1],
						ICrosstabConstants.ROW_AXIS_TYPE);

				processSubTotal(colSubTotals, (List<AggregationDialog.SubTotalInfo>) result[2]);
				processGrandTotal(colGrandTotals, (List<AggregationDialog.GrandTotalInfo>) result[3],
						ICrosstabConstants.COLUMN_AXIS_TYPE);

				providerWrapper.switchViews();
				if (needUpdateView) {
					providerWrapper.updateAllAggregationCells(AggregationCellViewAdapter.SWITCH_VIEW_TYPE);
				}
			} else {
				rollBack();
				return;
			}
		} catch (SemanticException e) {
			rollBack();
			ExceptionUtil.handle(e);
			return;
		}
		transEnd();
	}

	private List<AggregationDialog.SubTotalInfo> copySubTotal(List<AggregationDialog.SubTotalInfo> list) {
		List<AggregationDialog.SubTotalInfo> retValue = new ArrayList<AggregationDialog.SubTotalInfo>();
		for (int i = 0; i < list.size(); i++) {
			retValue.add(((SubTotalInfo) (list.get(i))).copy());
		}
		return retValue;
	}

	private List<AggregationDialog.GrandTotalInfo> copyGrandTotal(List<AggregationDialog.GrandTotalInfo> list) {
		List<AggregationDialog.GrandTotalInfo> retValue = new ArrayList<AggregationDialog.GrandTotalInfo>();
		for (int i = 0; i < list.size(); i++) {
			retValue.add(((GrandTotalInfo) (list.get(i))).copy());
		}
		return retValue;
	}

	private void processGrandTotal(List<AggregationDialog.GrandTotalInfo> ori,
			List<AggregationDialog.GrandTotalInfo> newList, int axisType) throws SemanticException {
		GrandOpration oriOperation = new GrandOpration();
		GrandOpration newOperation = new GrandOpration();
		for (int i = 0; i < ori.size(); i++) {
			GrandTotalInfo oriInfo = (GrandTotalInfo) ori.get(i);
			GrandTotalInfo newInfo = (GrandTotalInfo) newList.get(i);
			oriOperation.addInfo(oriInfo);
			newOperation.addInfo(newInfo);
			if (i == ori.size() - 1) {
				processOperation(oriOperation, newOperation, axisType);
			}
		}
	}

	private void restoreViews(GrandOpration newOperation, int axisType) {
		int count = newOperation.getMeasures().size();

		for (int i = 0; i < count; i++) {
			String tmpMeasureName = newOperation.getMeasures().get(i);
			MeasureViewHandle measureView = findMeasureViewHandle(tmpMeasureName);
			String expectedView = newOperation.getExpectedViews().get(i);
			if (expectedView == null || expectedView.length() == 0) {
				continue;
			}
			CrosstabReportItemHandle crosstab = levelHandle.getCrosstab();

			AggregationCellHandle cell = getGrandAggregationCell(measureView, levelHandle, axisType);

			if (cell != null) {
				// updateShowStatus( cell, expectedView );

//				SwitchCellInfo swtichCellInfo = new SwitchCellInfo( crosstab,
//						SwitchCellInfo.GRAND_TOTAL );
//				GrandTotalInfo grandTotal = new GrandTotalInfo( );
//				grandTotal.setExpectedView( expectedView );
//				// grandTotal.setMeasure( tmpMeasure );
//				grandTotal.setMeasureQualifiedName( tmpMeasureName );

				SwitchCellInfo swtichCellInfo = new SwitchCellInfo(crosstab, SwitchCellInfo.GRAND_TOTAL);
				GrandTotalInfo grandTotal = new GrandTotalInfo();
				grandTotal.setExpectedView(expectedView);
				// grandTotal.setMeasure( tmpMeasure );
				grandTotal.setMeasureQualifiedName(tmpMeasureName);
				swtichCellInfo.setGrandTotalInfo(grandTotal, axisType);

				providerWrapper.restoreViews(swtichCellInfo);

				// Chart needs to update
				needUpdateView = true;
			}

		}

	}

	private void processOperation(GrandOpration oriOperation, GrandOpration newOperation, int axisType)
			throws SemanticException {
		if (oriOperation.getMeasures().size() == 0 && newOperation.getMeasures().size() == 0) {
			return;
		}
		if (oriOperation.getMeasures().size() == 0 && newOperation.getMeasures().size() != 0) {
			addGrandTotal(levelHandle.getCrosstab(), axisType, newOperation.getFunctions(),
					findMeasureViewHandleList(newOperation.getMeasures()));
			markSwitchViews(newOperation, axisType, true);
		} else if (oriOperation.getMeasures().size() != 0 && newOperation.getMeasures().size() == 0) {
			restoreViews(oriOperation, axisType);
			levelHandle.getCrosstab().removeGrandTotal(axisType);
			// Chart needs to update

			needUpdateView = true;
		} else {
			int oriSize = oriOperation.getMeasures().size();
			int newSize = newOperation.getMeasures().size();
			if (oriSize != newSize) {
				levelHandle.getCrosstab().removeGrandTotal(axisType);
				addGrandTotal(levelHandle.getCrosstab(), axisType, newOperation.getFunctions(),
						findMeasureViewHandleList(newOperation.getMeasures()));
				markSwitchViews(newOperation, axisType, true);

				return;
			}
			for (int i = 0; i < oriSize; i++) {
				if (!oriOperation.getMeasures().get(i).equals(newOperation.getMeasures().get(i))) {
					levelHandle.getCrosstab().removeGrandTotal(axisType);
					addGrandTotal(levelHandle.getCrosstab(), axisType, newOperation.getFunctions(),
							findMeasureViewHandleList(newOperation.getMeasures()));
					markSwitchViews(newOperation, axisType, true);
					return;
				}
			}
			for (int i = 0; i < oriSize; i++) {
				if (!oriOperation.getFunctions().get(i).equals(newOperation.getFunctions().get(i))) {
					// CrosstabUtil.setAggregationFunction( findLevelViewHandle(
					// newOperation.getLevelHandle( ) ),
					// findMeasureViewHandle(
					// (MeasureHandle)newOperation.getMeasures( ).get( i )),
					// (String)newOperation.getFunctions( ).get( i ) );
					levelHandle.getCrosstab().setAggregationFunction(axisType,
							findMeasureViewHandle(newOperation.getMeasures().get(i)),
							(String) newOperation.getFunctions().get(i));
				}
				markSwitchViews(newOperation, axisType, false);
			}
		}
	}

	private void processSubTotal(List<AggregationDialog.SubTotalInfo> ori, List<AggregationDialog.SubTotalInfo> newList)
			throws SemanticException {
		SubOpration oriOperation = new SubOpration();
		SubOpration newOperation = new SubOpration();
		for (int i = 0; i < ori.size(); i++) {
			SubTotalInfo oriInfo = (SubTotalInfo) ori.get(i);
			SubTotalInfo newInfo = (SubTotalInfo) newList.get(i);
			if (i == 0) {
				oriOperation.setLevelHandle(oriInfo.getLevel());
				newOperation.setLevelHandle(newInfo.getLevel());
			} else if (!oriOperation.isSameOperation(oriInfo)) {
				processOperation(oriOperation, newOperation);
				oriOperation = new SubOpration();
				oriOperation.setLevelHandle(oriInfo.getLevel());
				newOperation = new SubOpration();
				newOperation.setLevelHandle(newInfo.getLevel());
			}
			oriOperation.addInfo(oriInfo);
			newOperation.addInfo(newInfo);
			if (i == ori.size() - 1) {
				processOperation(oriOperation, newOperation);
			}
		}
	}

//	private void markSwitchViews( GrandOpration newOperation, int axisType )
//	{
//		markSwitchViews( newOperation, axisType, false );
//	}

	private AggregationCellHandle getGrandAggregationCell(MeasureViewHandle measureView, LevelViewHandle levelHandle,
			int axisType) {
		AggregationCellHandle cell = null;
		CrosstabReportItemHandle crosstab = levelHandle.getCrosstab();

		int counterAxisType = getOppositeAxisType(axisType);
		DimensionViewHandle counterDimension = crosstab.getDimension(counterAxisType,
				crosstab.getDimensionCount(counterAxisType) - 1);
		String counterDimensionName = null;
		String counterLevelName = null;
		if (counterDimension != null) {
			counterDimensionName = counterDimension.getCubeDimensionName();
			counterLevelName = counterDimension.getLevel(counterDimension.getLevelCount() - 1).getCubeLevelName();
		}

		String rowDimension = null;
		String rowLevel = null;
		String colDimension = null;
		String colLevel = null;

		if (axisType == ICrosstabConstants.ROW_AXIS_TYPE) {
			colDimension = counterDimensionName;
			colLevel = counterLevelName;

		} else if (axisType == ICrosstabConstants.COLUMN_AXIS_TYPE) {
			rowDimension = counterDimensionName;
			rowLevel = counterLevelName;
		}

		cell = measureView.getAggregationCell(rowDimension, rowLevel, colDimension, colLevel);

		return cell;
	}

	private AggregationCellHandle getSubAggregationCell(MeasureViewHandle measureView, LevelViewHandle levelView) {
		AggregationCellHandle cell = null;

		String rowDimension = null;
		String rowLevel = null;
		String colDimension = null;
		String colLevel = null;

		int axisType = levelView.getAxisType();

		int counterAxisType = getOppositeAxisType(levelView.getAxisType());
		CrosstabReportItemHandle crosstab = levelHandle.getCrosstab();
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
			return null;

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

	private void markSwitchViews(GrandOpration newOperation, int axisType, boolean isNew) {
		int count = newOperation.getMeasures().size();

		for (int i = 0; i < count; i++) {
			String tmpMeasureName = newOperation.getMeasures().get(i);
			MeasureViewHandle measureView = findMeasureViewHandle(tmpMeasureName);
			String expectedView = newOperation.getExpectedViews().get(i);
			if (expectedView == null || expectedView.length() == 0) {
				continue;
			}
			CrosstabReportItemHandle crosstab = levelHandle.getCrosstab();

			AggregationCellHandle cell = getGrandAggregationCell(measureView, levelHandle, axisType);

			if (cell != null) {
				// updateShowStatus( cell, expectedView );

				SwitchCellInfo swtichCellInfo = new SwitchCellInfo(crosstab, SwitchCellInfo.GRAND_TOTAL);
				GrandTotalInfo grandTotal = new GrandTotalInfo();
				grandTotal.setExpectedView(expectedView);
				// grandTotal.setMeasure( tmpMeasure );
				grandTotal.setMeasureQualifiedName(tmpMeasureName);
				swtichCellInfo.setGrandTotalInfo(grandTotal, axisType);
				swtichCellInfo.setIsNew(isNew);
				providerWrapper.addSwitchInfo(swtichCellInfo);

				// Chart needs to update
				needUpdateView = true;
			}

		}

	}

//	private void markSwitchViews( SubOpration newOperation )
//	{
//		markSwitchViews( newOperation, false );
//	}

	private void markSwitchViews(SubOpration newOperation, boolean isNew) {
		int count = newOperation.getMeasures().size();
		for (int i = 0; i < count; i++) {
			String tmpMeasureName = newOperation.getMeasures().get(i);
			String expectedView = newOperation.getExpectedViews().get(i);
			if (expectedView == null || expectedView.length() == 0) {
				continue;
			}
			MeasureViewHandle measureView = findMeasureViewHandle(tmpMeasureName);
			CrosstabReportItemHandle crosstab = levelHandle.getCrosstab();
			LevelViewHandle levelView = findLevelViewHandle(newOperation.getLevelHandle());

			AggregationCellHandle cell = getSubAggregationCell(measureView, levelView);

			if (cell != null) {
				// updateShowStatus( cell, expectedView );
				// needUpdateView = true;

				SwitchCellInfo swtichCellInfo = new SwitchCellInfo(crosstab, SwitchCellInfo.SUB_TOTAL);
				SubTotalInfo subTotal = new SubTotalInfo();
				subTotal.setExpectedView(expectedView);
				// subTotal.setAggregateOnMeasure( tmpMeasure );
				subTotal.setAggregateOnMeasureName(tmpMeasureName);
				subTotal.setLevelView(levelView);
				swtichCellInfo.setIsNew(isNew);
				swtichCellInfo.setSubTotalInfo(subTotal);

				providerWrapper.addSwitchInfo(swtichCellInfo);

			}

		}

	}

	public static int getOppositeAxisType(int axisType) {
		switch (axisType) {
		case ICrosstabConstants.COLUMN_AXIS_TYPE:
			return ICrosstabConstants.ROW_AXIS_TYPE;
		case ICrosstabConstants.ROW_AXIS_TYPE:
			return ICrosstabConstants.COLUMN_AXIS_TYPE;
		default:
			return ICrosstabConstants.NO_AXIS_TYPE;
		}
	}

	private void processOperation(SubOpration oriOperation, SubOpration newOperation) throws SemanticException {
		if (oriOperation.getMeasures().size() == 0 && newOperation.getMeasures().size() == 0) {
			return;
		}
		if (oriOperation.getMeasures().size() == 0 && newOperation.getMeasures().size() != 0) {
			addAggregationHeader(findLevelViewHandle(newOperation.getLevelHandle()), newOperation.getFunctions(),
					findMeasureViewHandleList(newOperation.getMeasures()));
			markSwitchViews(newOperation, true);
		} else if (oriOperation.getMeasures().size() != 0 && newOperation.getMeasures().size() == 0) {
			findLevelViewHandle(oriOperation.getLevelHandle()).removeSubTotal();
			// Chart needs to update
			needUpdateView = true;
		} else {
			int oriSize = oriOperation.getMeasures().size();
			int newSize = newOperation.getMeasures().size();
			if (oriSize != newSize) {
				findLevelViewHandle(oriOperation.getLevelHandle()).removeSubTotal();
				addAggregationHeader(findLevelViewHandle(newOperation.getLevelHandle()), newOperation.getFunctions(),
						findMeasureViewHandleList(newOperation.getMeasures()));
				markSwitchViews(newOperation, true);
				return;
			}
			for (int i = 0; i < oriSize; i++) {
				if (!oriOperation.getMeasures().get(i).equals(newOperation.getMeasures().get(i))) {
					findLevelViewHandle(oriOperation.getLevelHandle()).removeSubTotal();
					addAggregationHeader(findLevelViewHandle(newOperation.getLevelHandle()),
							newOperation.getFunctions(), findMeasureViewHandleList(newOperation.getMeasures()));
					markSwitchViews(newOperation, true);
					return;
				}
			}
			for (int i = 0; i < oriSize; i++) {
				if (!oriOperation.getFunctions().get(i).equals(newOperation.getFunctions().get(i))) {
					findLevelViewHandle(newOperation.getLevelHandle()).setAggregationFunction(
							findMeasureViewHandle(newOperation.getMeasures().get(i)),
							(String) newOperation.getFunctions().get(i));
				}
				markSwitchViews(newOperation, false);
			}
		}
	}

	private void addGrandTotal(CrosstabReportItemHandle crosstab, int axisType, List functions, List measures)
			throws SemanticException {
		CrosstabCellHandle cellHandle = crosstab.addGrandTotal(axisType, measures, functions);
		if (cellHandle == null) {
			return;
		}
		CrosstabUIHelper.createGrandTotalLabel(cellHandle);
	}

	private void addAggregationHeader(LevelViewHandle levelView, List functions, List measures)
			throws SemanticException {
		CrosstabCellHandle cellHandle = levelView.addSubTotal(measures, functions);
		if (cellHandle == null) {
			return;
		}
		CrosstabUIHelper.createSubTotalLabel(levelView, cellHandle);
	}

	private List<MeasureViewHandle> findMeasureViewHandleList(List<String> list) {
		List<MeasureViewHandle> retValue = new ArrayList<MeasureViewHandle>();
		for (int i = 0; i < list.size(); i++) {
			retValue.add(findMeasureViewHandle(list.get(i)));
		}
		return retValue;
	}

	private MeasureViewHandle findMeasureViewHandle(String measureName) {
		return levelHandle.getCrosstab().getMeasure(measureName);
	}

	private LevelViewHandle findLevelViewHandle(LevelHandle handle) {
		DimensionViewHandle viewHandle = getDimensionViewHandle();
		CrosstabReportItemHandle crosstab = viewHandle.getCrosstab();

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

	static class SubOpration {

		private LevelHandle levelHandle;
		private List<String> functions = new ArrayList<String>();
		private List<String> measures = new ArrayList<String>();
		private List<String> expectedViews = new ArrayList<String>();

		public boolean isSameOperation(SubTotalInfo info) {
			return info.getLevel() == levelHandle;
		}

		public LevelHandle getLevelHandle() {
			return levelHandle;
		}

		public void setLevelHandle(LevelHandle levelHandle) {
			this.levelHandle = levelHandle;
		}

		public void addInfo(SubTotalInfo info) {
			if (info.isAggregationOn()) {
				functions.add(info.getFunction());
				measures.add(info.getAggregateOnMeasureName());
				expectedViews.add(info.getExpectedView());
			}
		}

		public List<String> getFunctions() {
			return functions;
		}

		public List<String> getMeasures() {
			return measures;
		}

		public List<String> getExpectedViews() {
			return expectedViews;
		}
	}

	static class GrandOpration {

		private List<String> functions = new ArrayList<String>();
		private List<String> measures = new ArrayList<String>();
		private List<String> expectedViews = new ArrayList<String>();

		public void addInfo(GrandTotalInfo info) {
			if (info.isAggregationOn()) {
				functions.add(info.getFunction());
				measures.add(info.getMeasureQualifiedName());
				expectedViews.add(info.getExpectedView());
			}
		}

		public List<String> getFunctions() {
			return functions;
		}

		public List<String> getMeasures() {
			return measures;
		}

		public List<String> getExpectedViews() {
			return expectedViews;
		}
	}

	private boolean isVertical(CrosstabReportItemHandle reportHandle) {
		return ICrosstabConstants.MEASURE_DIRECTION_VERTICAL.equals(reportHandle.getMeasureDirection());
	}

	private boolean getAssociation(int axis) {
		DimensionViewHandle viewHandle = getDimensionViewHandle();
		CrosstabReportItemHandle reportHandle = viewHandle.getCrosstab();
		if (ICrosstabConstants.COLUMN_AXIS_TYPE == axis) {
			if (isVertical(reportHandle)) {
				return true;
			} else {
				return false;
			}
		}
		if (ICrosstabConstants.ROW_AXIS_TYPE == axis) {
			if (isVertical(reportHandle)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	private List<AggregationDialog.SubTotalInfo> getSubTotalInfo(int axis) {
		List<AggregationDialog.SubTotalInfo> retValue = new ArrayList<AggregationDialog.SubTotalInfo>();
		DimensionViewHandle viewHandle = getDimensionViewHandle();
		CrosstabReportItemHandle reportHandle = viewHandle.getCrosstab();
		int dimCount = reportHandle.getDimensionCount(axis);
		int measureCount = reportHandle.getMeasureCount();
		LevelViewHandle lastLevelHandle = getLastLevelViewHandle(axis);

		for (int k = 0; k < dimCount; k++) {
			DimensionViewHandle dimension = reportHandle.getDimension(axis, k);
			int count = dimension.getLevelCount();

			for (int i = 0; i < count; i++) {
				LevelViewHandle tempViewHandle = dimension.getLevel(i);
				if (tempViewHandle == lastLevelHandle) {
					continue;
				}
				LevelHandle tempHandle = tempViewHandle.getCubeLevel();
				for (int j = 0; j < measureCount; j++) {
					MeasureViewHandle measureView = reportHandle.getMeasure(j);
					if (measureView instanceof ComputedMeasureViewHandle) {
						continue;
					}
					AggregationDialog.SubTotalInfo info = new AggregationDialog.SubTotalInfo();
					info.setLevelView(tempViewHandle);
					// info.setAggregateOnMeasure( reportHandle.getMeasure( j )
					// .getCubeMeasure( ) );
					if (reportHandle.getMeasure(j).getCubeMeasure() != null) {
						info.setAggregateOnMeasureName(reportHandle.getMeasure(j).getCubeMeasure().getQualifiedName());
						info.setAggregateOnMeasureDisplayName(reportHandle.getMeasure(j).getCubeMeasure().getName());
					}

					info.setFunction(CrosstabUtil.getDefaultMeasureAggregationFunction(reportHandle.getMeasure(j)));
					info.setExpectedView(""); //$NON-NLS-1$
					retValue.add(info);
					// fix bug
					info.setAssociation(getAssociation(axis));
				}
			}
		}

		for (int k = 0; k < dimCount; k++) {
			DimensionViewHandle dimension = reportHandle.getDimension(axis, k);
			int count = dimension.getLevelCount();
			for (int i = 0; i < count; i++) {
				LevelViewHandle tempViewHandle = dimension.getLevel(i);
				LevelHandle tempHandle = tempViewHandle.getCubeLevel();
				List measures = tempViewHandle.getAggregationMeasures();

				for (int j = 0; j < measures.size(); j++) {

					MeasureHandle tempMeasureHandle = ((MeasureViewHandle) measures.get(j)).getCubeMeasure();
					AggregationDialog.SubTotalInfo info = new AggregationDialog.SubTotalInfo();
					info.setLevelView(tempViewHandle);
					// info.setAggregateOnMeasure( tempMeasureHandle );
					if (tempMeasureHandle != null) {
						info.setAggregateOnMeasureName(tempMeasureHandle.getQualifiedName());
						info.setAggregateOnMeasureDisplayName(tempMeasureHandle.getName());
					}

					// info.setFunction( tempViewHandle.getAggregationFunction(
					// (MeasureViewHandle) measures.get( j ) ) );
					info.setFunction(
							CrosstabUtil.getDefaultMeasureAggregationFunction((MeasureViewHandle) measures.get(j)));
					// tempMeasureHandle.getFunction( );
					// info.setFunction(
					// DesignChoiceConstants.MEASURE_FUNCTION_SUM);

					AggregationCellHandle cell = getSubAggregationCell((MeasureViewHandle) measures.get(j),
							tempViewHandle);
					String view = getExpectedView(cell);
					info.setExpectedView(view); // $NON-NLS-1$

					replaceInfo(info, retValue);
				}
			}
		}
		return retValue;
	}

	private LevelViewHandle getLastLevelViewHandle(int axis) {
		DimensionViewHandle viewHandle = getDimensionViewHandle();
		CrosstabReportItemHandle reportHandle = viewHandle.getCrosstab();
		int count = reportHandle.getDimensionCount(axis);
		if (count == 0) {
			return null;
		}
		DimensionViewHandle lastDimension = reportHandle.getDimension(axis, count - 1);

		return lastDimension.getLevel(lastDimension.getLevelCount() - 1);
	}

	private List<AggregationDialog.GrandTotalInfo> getGrandTotalInfo(int axis) {
		List<AggregationDialog.GrandTotalInfo> retValue = new ArrayList<AggregationDialog.GrandTotalInfo>();
		CrosstabReportItemHandle reportHandle = levelHandle.getCrosstab();
		CrosstabViewHandle crosstabView = reportHandle.getCrosstabView(axis);
		if (crosstabView == null || crosstabView.getDimensionCount() == 0) {
			return retValue;
		}

		int measureCount = reportHandle.getMeasureCount();
		for (int i = 0; i < measureCount; i++) {

			MeasureViewHandle measureView = reportHandle.getMeasure(i);
			if (measureView instanceof ComputedMeasureViewHandle) {
				continue;
			}
			AggregationDialog.GrandTotalInfo info = new AggregationDialog.GrandTotalInfo();
			// info.setMeasure( reportHandle.getMeasure( i ).getCubeMeasure( )
			// );
			info.setViewHandle(reportHandle.getCrosstabView(axis));
			if (reportHandle.getMeasure(i).getCubeMeasure() != null) {
				info.setMeasureQualifiedName(reportHandle.getMeasure(i).getCubeMeasure().getQualifiedName());

				info.setMeasureDisplayName(reportHandle.getMeasure(i).getCubeMeasure().getName());
			}

			info.setFunction(CrosstabUtil.getDefaultMeasureAggregationFunction(reportHandle.getMeasure(i)));
			info.setExpectedView(""); //$NON-NLS-1$

			info.setPosition(reportHandle.getCrosstabView(axis).getGrandTotalLocation());
			retValue.add(info);
			info.setAssociation(getAssociation(axis));
		}

		List measures = reportHandle.getAggregationMeasures(axis);
		for (int i = 0; i < measures.size(); i++) {
			MeasureViewHandle measureView = (MeasureViewHandle) measures.get(i);
			if (measureView instanceof ComputedMeasureViewHandle) {
				continue;
			}
			AggregationDialog.GrandTotalInfo info = new AggregationDialog.GrandTotalInfo();
			MeasureViewHandle measureViewHandle = (MeasureViewHandle) measures.get(i);
			// info.setMeasure( measureViewHandle.getCubeMeasure( ) );
			info.setViewHandle(reportHandle.getCrosstabView(axis));
			if (measureViewHandle.getCubeMeasure() != null) {
				info.setMeasureQualifiedName(measureViewHandle.getCubeMeasure().getQualifiedName());
				info.setMeasureDisplayName(measureViewHandle.getCubeMeasure().getName());
			}

			// info.setFunction( reportHandle.getAggregationFunction(
			// viewHandle.getAxisType( ),
			// measureViewHandle ) );

			info.setFunction(CrosstabUtil.getDefaultMeasureAggregationFunction(measureViewHandle));

			AggregationCellHandle cell = getGrandAggregationCell(measureView, levelHandle, axis);
			String view = getExpectedView(cell);
			info.setExpectedView(view); // $NON-NLS-1$
			info.setPosition(reportHandle.getCrosstabView(axis).getGrandTotalLocation());

			replaceInfo(info, retValue);
		}

		return retValue;

	}

	// private List

	private void replaceInfo(AggregationDialog.SubTotalInfo info, List list) {
		for (int i = 0; i < list.size(); i++) {
			if (info.isSameInfo(list.get(i))) {
				AggregationDialog.SubTotalInfo tempInfo = (AggregationDialog.SubTotalInfo) list.get(i);
				tempInfo.setAggregationOn(true);
				tempInfo.setFunction(info.getFunction());
				tempInfo.setExpectedView(info.getExpectedView());
				break;
			}
		}
	}

	private void replaceInfo(AggregationDialog.GrandTotalInfo info, List list) {
		for (int i = 0; i < list.size(); i++) {
			if (info.isSameInfo(list.get(i))) {
				AggregationDialog.GrandTotalInfo tempInfo = (AggregationDialog.GrandTotalInfo) list.get(i);
				tempInfo.setAggregationOn(true);
				tempInfo.setFunction(info.getFunction());
				tempInfo.setExpectedView(info.getExpectedView());
				tempInfo.setViewHandle(info.getViewHandle());
				tempInfo.setPosition(info.getPosition());
			}
		}
	}

	private DimensionViewHandle getDimensionViewHandle() {
		return CrosstabAdaptUtil.getDimensionViewHandle((ExtendedItemHandle) (levelHandle.getModelHandle()));
	}

	private String getExpectedView(AggregationCellHandle cell) {
		String view = "";
		AggregationCellProviderWrapper wrapper = new AggregationCellProviderWrapper(levelHandle.getCrosstab());
		IAggregationCellViewProvider provider = wrapper.getMatchProvider(cell);
		if (provider != null) {
			view = provider.getViewName();
		}
		return view;
	}

}
