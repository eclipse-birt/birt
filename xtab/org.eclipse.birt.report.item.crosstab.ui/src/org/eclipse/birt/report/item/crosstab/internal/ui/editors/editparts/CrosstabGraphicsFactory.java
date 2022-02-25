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

import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.ICrosstabCellAdapterFactory;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.action.MenuManager;

/**
 * Factory to create the editpart.
 */
public class CrosstabGraphicsFactory implements EditPartFactory {

	/**
	 * The singleton instance.
	 */
	public static final CrosstabGraphicsFactory INSTANCEOF = new CrosstabGraphicsFactory();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 * java.lang.Object)
	 */
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		if (context instanceof CrosstabCellEditPart) {
			if (model instanceof DataItemHandle) {
				CrosstabCellAdapter adapter = ((CrosstabCellEditPart) context).getCrosstabCellAdapter();
				String position = adapter.getPositionType();

				if ((ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE.equals(position))
						&& adapter.getFirstDataItem() == model) {
					// FirstLevelHandleDataItemEditPart first = new
					// FirstLevelHandleDataItemEditPart(model);
					// first.setManager( createMenuManager( position,
					// context.getViewer( )));
					// return first;
					return new LevelHandleDataItemEditPart(model);
				} else if (ICrosstabCellAdapterFactory.CELL_LEVEL_HANDLE.equals(position)) {
					// FirstLevelHandleDataItemEditPart
					return new LevelHandleDataItemEditPart(model);
				} else if ( // ICrosstabCellAdapterFactory.CELL_MEASURE.equals(
							// position )
				// ||
				ICrosstabCellAdapterFactory.CELL_MEASURE_HEADER.equals(position)) {
					return new MeasureHandleDataItemEditPart(model);
				} else if ( // ICrosstabCellAdapterFactory.CELL_MEASURE.equals(
							// position )
				// ||
				ICrosstabCellAdapterFactory.CELL_MEASURE_AGGREGATION.equals(position)
						|| ICrosstabCellAdapterFactory.CELL_MEASURE.equals(position)) {
					return new MeasureAggregationEditPart(model);
				}
			}
		}
		if (model instanceof VirtualCrosstabCellAdapter) {
			return new VirtualCellEditPart(model);
		}
		if (model instanceof CrosstabCellAdapter) {
			if (ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE
					.equals(((CrosstabCellAdapter) model).getPositionType())
					|| ICrosstabCellAdapterFactory.CELL_MEASURE
							.equals(((CrosstabCellAdapter) model).getPositionType())) {

				CrosstabFirstCellEditPart first = new CrosstabFirstCellEditPart(model);
				first.setManager(
						createMenuManager(((CrosstabCellAdapter) model).getPositionType(), context.getViewer()));
				return first;
			}
			return new CrosstabCellEditPart(model);
		}
		return null;
	}

	private MenuManager createMenuManager(String position, EditPartViewer viewer) {
		if (ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE.equals(position)) {
			return new LevelCrosstabPopMenuProvider(viewer);
		}
		if (ICrosstabCellAdapterFactory.CELL_MEASURE.equals(position)) {
			return new MeasureCrosstabPopMenuProvider(viewer);
		}
		throw new RuntimeException("Don't support this position");//$NON-NLS-1$
	}
}
