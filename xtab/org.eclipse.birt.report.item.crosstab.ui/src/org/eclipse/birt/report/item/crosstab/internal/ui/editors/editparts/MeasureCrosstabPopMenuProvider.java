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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.AddComputedMeasureAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.AddMeasureViewHandleAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.AddRelativeTimePeriodAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.DeleteMeasureHandleAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.ShowAsViewMenuAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;

/**
 * 
 */

public class MeasureCrosstabPopMenuProvider extends ContextMenuProvider {

	/**
	 * Constructor
	 * 
	 * @param viewer
	 */
	public MeasureCrosstabPopMenuProvider(EditPartViewer viewer) {
		super(viewer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface.action
	 * .IMenuManager)
	 */
	public void buildContextMenu(IMenuManager menu) {
		if (getElements().size() != 1) {
			return;
		}
		Object firstSelectedElement = getFirstElement();
		DesignElementHandle element = null;
		if (firstSelectedElement instanceof DesignElementHandle) {
			element = (DesignElementHandle) firstSelectedElement;
		} else if (firstSelectedElement instanceof CrosstabCellAdapter) {
			element = ((CrosstabCellAdapter) firstSelectedElement).getDesignElementHandle();
		}

		buildShowMenu(menu, element);

		IAction action = new AddRelativeTimePeriodAction(element);
		// if (action.isEnabled( ))
		{
			menu.add(action);
		}
		action = new AddComputedMeasureAction(element);
		menu.add(action);

		action = new AddMeasureViewHandleAction(element);
		menu.add(action);

		action = new DeleteMeasureHandleAction(element);
		menu.add(action);

	}

	protected void buildShowMenu(IMenuManager menu, DesignElementHandle element) {

		ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle(element);
		MeasureViewHandle measureViewHandle = CrosstabAdaptUtil.getMeasureViewHandle(extendedHandle);
		if (measureViewHandle == null || measureViewHandle instanceof ComputedMeasureViewHandle
				|| (measureViewHandle.getCubeMeasure() != null && measureViewHandle.getCubeMeasure().isCalculated())) {
			return;
		}
		AggregationCellProviderWrapper providerWrapper = new AggregationCellProviderWrapper(
				measureViewHandle.getCrosstab());
		IAggregationCellViewProvider[] providers = providerWrapper.getAllProviders();
		int count = 1;
		for (int i = 0; i < providers.length; i++) {
			IAggregationCellViewProvider provider = providers[i];
			if (provider == null) {
				continue;
			}
			ShowAsViewMenuAction showAsViewAction = new ShowAsViewMenuAction(element, provider.getViewName(), count);
			count++;
			menu.add(showAsViewAction);
		}
		menu.add(new Separator());

	}

	/**
	 * Gets the current selection.
	 * 
	 * @return The current selection
	 */
	protected ISelection getSelection() {
		return getViewer().getSelection();
	}

	/**
	 * Gets element handles.
	 * 
	 * @return element handles
	 */
	protected List getElements() {
		return InsertInLayoutUtil.editPart2Model(getSelection()).toList();
	}

	/**
	 * Gets the current selected object.
	 * 
	 * @return The current selected object array. If length is one, return the first
	 */
	protected Object getSelectedElement() {
		Object[] array = getElements().toArray();
		if (array.length == 1) {
			return array[0];
		}
		return array;
	}

	/**
	 * Gets the first selected object.
	 * 
	 * @return The first selected object
	 */
	protected Object getFirstElement() {
		Object[] array = getElements().toArray();
		if (array.length > 0) {
			return array[0];
		}
		return null;
	}
}
