/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.cubebuilder.joins;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.AttributeEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.ColumnEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.CubeEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.DatasetNodeEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.HierarchyColumnEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.HierarchyNodeEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.JoinConditionEditPart;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * A factory for creating new Edit Parts for the Joins Page
 *
 * @see org.eclipse.gef.EditPartFactory
 *
 */
public class GraphicalEditPartsFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof TabularHierarchyHandle) {
			return new HierarchyNodeEditPart(context, (TabularHierarchyHandle) model);
		}

		if (model instanceof ResultSetColumnHandle) {
			if (context instanceof DatasetNodeEditPart) {
				return new ColumnEditPart(context, (ResultSetColumnHandle) model);
			} else if (context instanceof HierarchyNodeEditPart) {
				return new HierarchyColumnEditPart(context, (ResultSetColumnHandle) model);
			} else {
				return null;
			}
		}

		if (model instanceof TabularCubeHandle) {
			return new CubeEditPart(context, (TabularCubeHandle) model);
		}

		if (model instanceof DataSetHandle) {
			return new DatasetNodeEditPart(context, (DataSetHandle) model);
		}

		if (model instanceof DimensionJoinConditionHandle) {
			return new JoinConditionEditPart(context, (DimensionJoinConditionHandle) model);
		}

		if (model instanceof LevelAttributeHandle) {
			return new AttributeEditPart(context, (LevelAttributeHandle) model);
		}

		return null;
	}
}
