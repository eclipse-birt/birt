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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.ShowSummaryFieldDialog;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.ShowSummaryFieldDialog.MeasureInfo;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.extension.AggregationCellViewAdapter;
import org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider;
import org.eclipse.birt.report.item.crosstab.ui.extension.SwitchCellInfo;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureGroupModel;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;

/**
 * Add the measurehandle to the crosstab.
 */

public class AddMeasureViewHandleAction extends AbstractCrosstabAction {

	private MeasureViewHandle measureViewHandle;

	boolean needUpdateView = false;
	/**
	 * Action displayname
	 */
	// private static final String ACTION_MSG_MERGE = "Show/Hide Measures";
	/** action ID */
	public static final String ID = "org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.AddMesureViewHandleAction"; //$NON-NLS-1$

	/**
	 * Trans name
	 */
	// private static final String NAME = "Add measure handle";
	private static final String NAME = Messages.getString("AddMesureViewHandleAction.DisplayName");//$NON-NLS-1$
	private static final String ACTION_MSG_MERGE = Messages.getString("AddMesureViewHandleAction.TransName");//$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public AddMeasureViewHandleAction(DesignElementHandle handle) {
		super(handle);
		setId(ID);
		setText(NAME);
		ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle(handle);
		setHandle(extendedHandle);
		measureViewHandle = CrosstabAdaptUtil.getMeasureViewHandle(extendedHandle);

		Image image = CrosstabUIHelper.getImage(CrosstabUIHelper.SHOW_HIDE_LEVEL);
		setImageDescriptor(ImageDescriptor.createFromImage(image));
	}

	public boolean isEnabled() {
		CubeHandle cubeHandle = measureViewHandle.getCrosstab().getCube();
		if (cubeHandle == null) {
			return false;
		}
		return !DEUtil.isReferenceElement(measureViewHandle.getCrosstabHandle());
	}

	private String getExpectedView(MeasureViewHandle measure) {
		String view = "";
		AggregationCellHandle cell = measure.getCell();
		AggregationCellProviderWrapper wrapper = new AggregationCellProviderWrapper(measure.getCrosstab());
		IAggregationCellViewProvider provider = wrapper.getMatchProvider(cell);
		if (provider != null) {
			view = provider.getViewName();
		}
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		transStar(ACTION_MSG_MERGE);
		try {
			CrosstabReportItemHandle reportHandle = measureViewHandle.getCrosstab();
			ShowSummaryFieldDialog dialog = new ShowSummaryFieldDialog(UIUtil.getDefaultShell(), reportHandle);
			List list = getDimensionHandles();
			List<MeasureInfo> input = new ArrayList<MeasureInfo>();
			for (int i = 0; i < list.size(); i++) {
				MeasureHandle handle = (MeasureHandle) list.get(i);
				MeasureInfo info = new MeasureInfo();
				info.setMeasureName(handle.getQualifiedName());
				info.setMeasureDisplayName(handle.getName());
				info.setExpectedView(""); //$NON-NLS-1$
				input.add(info);
			}

			int count = reportHandle.getMeasureCount();
			for (int i = 0; i < count; i++) {
				MeasureViewHandle viewHandle = reportHandle.getMeasure(i);
				if (viewHandle == null) {
					continue;
				}
				if (viewHandle instanceof ComputedMeasureViewHandle) {
					MeasureInfo info = new MeasureInfo();
					info.setMeasureName(viewHandle.getCubeMeasureName());
					info.setMeasureDisplayName(viewHandle.getCubeMeasureName());
					info.setExpectedView(""); //$NON-NLS-1$
					info.setShow(true);
					input.add(info);
				} else {
					checkStatus(viewHandle, input);
				}

			}

			dialog.setInput(copyInfo(input));
			if (dialog.open() == Window.OK) {
				List result = (List) dialog.getResult();
				boolean isRemove = processor(input, result, false);

				if (isRemove) {
					boolean bool = CrosstabAdaptUtil.needRemoveInvaildBindings(reportHandle);
					processor(input, result, true);
					if (bool) {
						CrosstabAdaptUtil.removeInvalidBindings(reportHandle);
					}
				} else {
					processor(input, result, true);
				}

				providerWrapper.switchViews();
				if (needUpdateView) {
					providerWrapper.updateAllAggregationCells(AggregationCellViewAdapter.SWITCH_VIEW_TYPE);
				}
			}

		} catch (SemanticException e) {
			rollBack();
			ExceptionUtil.handle(e);
			return;
		}
		transEnd();
	}

	private AggregationCellProviderWrapper providerWrapper;

	private void initializeProviders() {

		providerWrapper = new AggregationCellProviderWrapper(
				(ExtendedItemHandle) measureViewHandle.getCrosstab().getModelHandle());
	}

	private MeasureInfo getOriMeasureInfo(MeasureInfo info, List list) {
		MeasureInfo ret = null;
		for (int i = 0; i < list.size(); i++) {
			MeasureInfo comparedOne = (MeasureInfo) list.get(i);
			if (info.isSameInfo(comparedOne)) {
				return comparedOne;
			}
		}
		return ret;
	}

	private MeasureViewHandle findMeasureViewHandle(MeasureHandle measure) {
		return measureViewHandle.getCrosstab().getMeasure(measure.getQualifiedName());
	}

	private boolean processor(List list, List result, boolean doChange) throws SemanticException {
		initializeProviders();

		boolean isRemove = false;

		List temp = new ArrayList(result);
		for (int i = 0; i < result.size(); i++) {
			MeasureInfo resultOne = (MeasureInfo) result.get(i);
			MeasureInfo originalOne = getOriMeasureInfo(resultOne, list);
			if (resultOne.isShow() == originalOne.isShow()) {
				MeasureInfo info = (MeasureInfo) result.get(i);
				if (info.isShow() == true && info.getExpectedView() != null && info.getExpectedView().length() != 0) {
					// MeasureViewHandle handle = findMeasureViewHandle(
					// info.getMeasure( ) );
					// updateShowStatus( handle, info );
					SwitchCellInfo swtichCellInfo = new SwitchCellInfo(measureViewHandle.getCrosstab(),
							SwitchCellInfo.MEASURE);
					swtichCellInfo.setMeasureInfo(info);
					swtichCellInfo.setIsNew(false);
					providerWrapper.addSwitchInfo(swtichCellInfo);
					needUpdateView = true;
				}
				temp.remove(resultOne);
			}
		}
		CrosstabReportItemHandle reportHandle = measureViewHandle.getCrosstab();
		for (int i = 0; i < temp.size(); i++) {
			MeasureInfo info = (MeasureInfo) temp.get(i);
			if (info.isShow()) {
				if (!doChange) {
					continue;
				}
				// reportHandle.insertMeasure( info.getMeasure( ),
				// reportHandle.getMeasureCount( ) );
				MeasureHandle measure = reportHandle.getCube().getMeasure(info.getMeasureName());
				MeasureViewHandle measureViewHandle = reportHandle.insertMeasure(measure,
						reportHandle.getMeasureCount());
				measureViewHandle.addHeader();

//				LabelHandle labelHandle = DesignElementFactory.getInstance( )
//						.newLabel( null );
//				labelHandle.setText( info.getMeasureDisplayName( ) );
				needUpdateView = true;
//				measureViewHandle.getHeader( ).addContent( labelHandle );
				if (info.getExpectedView() != null && info.getExpectedView().length() != 0) {
					// updateShowStatus( measureViewHandle, info );
					SwitchCellInfo swtichCellInfo = new SwitchCellInfo(measureViewHandle.getCrosstab(),
							SwitchCellInfo.MEASURE);
					info.setMeasureName(measureViewHandle.getCubeMeasure().getQualifiedName());
					swtichCellInfo.setMeasureInfo(info);
					swtichCellInfo.setIsNew(true);
					providerWrapper.addSwitchInfo(swtichCellInfo);
				}
			} else {
				if (doChange) {
					reportHandle.removeMeasure(info.getMeasureName());
				}
				isRemove = true;
				needUpdateView = true;
			}
		}

		return isRemove;
	}

	private void checkStatus(MeasureViewHandle viewHandle, List list) {
		for (int i = 0; i < list.size(); i++) {
			MeasureInfo info = (MeasureInfo) list.get(i);
			if (info.getMeasureName().equals(viewHandle.getCubeMeasureName())) {
				info.setShow(true);
				String view = getExpectedView(viewHandle);
				info.setExpectedView(view);
				break;
			}
		}
	}

	private List copyInfo(List list) {
		List retValue = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			retValue.add(((MeasureInfo) list.get(i)).copy());

		}
		return retValue;
	}

	private List getDimensionHandles() {
		List retValue = new ArrayList();
		CubeHandle cubeHandle = measureViewHandle.getCrosstab().getCube();
		List list = cubeHandle.getContents(ICubeModel.MEASURE_GROUPS_PROP);
		for (int i = 0; i < list.size(); i++) {
			MeasureGroupHandle groupHandle = (MeasureGroupHandle) list.get(i);
			List tempList = groupHandle.getContents(IMeasureGroupModel.MEASURES_PROP);
			retValue.addAll(tempList);
		}

		return retValue;
	}
}
