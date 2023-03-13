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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.TableHFHandle;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.TableHandleKit;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.TableSelectionHandle;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;

/**
 * This is the resize policy to provide support for Table cell resize
 *
 *
 */
public class TableCellResizeEditPolicy extends ResizableEditPolicy {

	/**
	 * Obtains the specified layer.
	 *
	 * @param layer the key identifying the layer
	 * @return the requested layer
	 */
	@Override
	protected IFigure getLayer(Object layer) {
		IFigure figure = null;
		if (getHost() instanceof TableCellEditPart) {
			figure = ((TableCellEditPart) getHost()).getLayer(layer);
		}
		if (figure != null) {
			return figure;
		}
		return super.getLayer(layer);
	}

	@Override
	protected List createSelectionHandles() {
		List list = new ArrayList();
		if (getHost().getSelected() != EditPart.SELECTED_PRIMARY) {
			return list;
		}
		TableHandleKit.addHandles((TableCellEditPart) getHost(), list);
		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#getTargetEditPart(org.
	 * eclipse.gef.Request)
	 */
	@Override
	public EditPart getTargetEditPart(Request request) {
		return null;
	}

	@Override
	protected void addSelectionHandles() {
		if (((ReportElementEditPart) getHost()).isDelete() || getHost().getSelected() != EditPart.SELECTED_PRIMARY) {
			return;
		}
		super.addSelectionHandles();
		IFigure layer = getTopLayer(LayerConstants.HANDLE_LAYER);
		ArrayList list = new ArrayList();
		TableHandleKit.addHandles((TableEditPart) getHost().getParent(), list);
		for (int i = 0; i < list.size(); i++) {
			layer.add((IFigure) list.get(i));
		}
		handles.addAll(list);
	}

	/**
	 * @param handle_layer
	 * @return
	 */
	private IFigure getTopLayer(String layer) {
		return LayerManager.Helper.find(getHost()).getLayer(layer);
	}

	@Override
	protected void removeSelectionHandles() {
		if (handles == null) {
			return;
		}
		IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
		IFigure topLayer = getTopLayer(LayerConstants.HANDLE_LAYER);
		for (int i = 0; i < handles.size(); i++) {
			Object figure = handles.get(i);
			if (figure instanceof TableHFHandle) {
				topLayer.remove((IFigure) figure);
			} else if (figure instanceof TableSelectionHandle) {
				layer.remove((IFigure) figure);
			} else if (figure instanceof IFigure) {
				layer.remove((IFigure) figure);
			}

		}

		handles = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#showFocus()
	 */
	@Override
	protected void showFocus() {
		// do nothing
	}
}
