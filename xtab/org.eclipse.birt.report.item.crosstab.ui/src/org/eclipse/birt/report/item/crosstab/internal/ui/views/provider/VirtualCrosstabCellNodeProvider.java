/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.swt.graphics.Image;

/**
 *
 */

public class VirtualCrosstabCellNodeProvider extends DefaultNodeProvider {

	@Override
	public Object[] getChildren(Object model) {
		return new Object[0];
	}

	@Override
	public Image getNodeIcon(Object model) {
		if (model instanceof VirtualCrosstabCellAdapter) {
			VirtualCrosstabCellAdapter adapter = (VirtualCrosstabCellAdapter) model;
			if (adapter.getType() == VirtualCrosstabCellAdapter.ROW_TYPE) {
				return CrosstabUIHelper.getImage(CrosstabUIHelper.ROWS_AREA_IMAGE);
			}
			if (adapter.getType() == VirtualCrosstabCellAdapter.COLUMN_TYPE) {
				return CrosstabUIHelper.getImage(CrosstabUIHelper.COLUMNS_AREA_IMAGE);
			}
			if (adapter.getType() == VirtualCrosstabCellAdapter.MEASURE_TYPE) {
				return CrosstabUIHelper.getImage(CrosstabUIHelper.DETAIL_AREA_IMAGE);
			}
		}
		return super.getNodeIcon(model);
	}

	@Override
	public String getNodeDisplayName(Object model) {
		if (model instanceof VirtualCrosstabCellAdapter) {
			VirtualCrosstabCellAdapter adapter = (VirtualCrosstabCellAdapter) model;
			if (adapter.getType() == VirtualCrosstabCellAdapter.ROW_TYPE) {
				return Messages.getString("VirtualCrosstabCellNodeProvider.Display.RowArea"); //$NON-NLS-1$
			}
			if (adapter.getType() == VirtualCrosstabCellAdapter.COLUMN_TYPE) {
				return Messages.getString("VirtualCrosstabCellNodeProvider.Display.ColumnArea"); //$NON-NLS-1$
			}
			if (adapter.getType() == VirtualCrosstabCellAdapter.MEASURE_TYPE) {
				return Messages.getString("VirtualCrosstabCellNodeProvider.Display.DetailArea"); //$NON-NLS-1$
			}
			return Messages.getString("VirtualCrosstabCellNodeProvider.Display.UnknownArea"); //$NON-NLS-1$
		}
		return super.getNodeDisplayName(model);
	}

	@Override
	public String getNodeTooltip(Object model) {
		return super.getNodeDisplayName(model);
	}
}
