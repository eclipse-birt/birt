/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.AttributeEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.ColumnEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.CubeEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.DatasetNodeEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.HierarchyNodeEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.JoinConditionEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.LevelEditPart;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;

/**
 * A factory for creating new Edit Parts for the Joins Page
 * 
 * @see org.eclipse.gef.EditPartFactory
 * 
 */
public class GraphicalEditPartsFactory implements EditPartFactory
{

	public EditPart createEditPart( EditPart context, Object model )
	{
		if ( model instanceof TabularHierarchyHandle )
		{
			return new HierarchyNodeEditPart( context,
					(TabularHierarchyHandle) model );
		}

		if ( model instanceof ResultSetColumnHandle )
		{
			return new ColumnEditPart( context, (ResultSetColumnHandle) model );
		}

		if ( model instanceof TabularCubeHandle )
		{
			return new CubeEditPart( context, (TabularCubeHandle) model );
		}

		if ( model instanceof DataSetHandle )
		{
			return new DatasetNodeEditPart( context, (DataSetHandle) model );
		}

		if ( model instanceof DimensionJoinConditionHandle )
		{
			return new JoinConditionEditPart( context,
					(DimensionJoinConditionHandle) model );
		}

		if ( model instanceof TabularLevelHandle )
		{
			return new LevelEditPart( context,
					(TabularLevelHandle) model );
		}
		
		if( model instanceof LevelAttributeHandle){
			return new AttributeEditPart(context,
					(LevelAttributeHandle) model);
		}

		return null;
	}

}