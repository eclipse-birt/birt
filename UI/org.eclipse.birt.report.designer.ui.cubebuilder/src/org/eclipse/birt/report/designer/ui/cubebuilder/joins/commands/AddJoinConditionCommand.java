/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.commands;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.ColumnEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.DatasetNodeEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.HierarchyNodeEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.HierarchyColumnEditPart;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.api.elements.structures.DimensionJoinCondition;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

/**
 * 
 * The Command for creating a new Join , when the user has selected columns from
 * different tables. It notifies the various listeners by sending a
 * JoinCreationEvent.
 */

public class AddJoinConditionCommand extends Command
{

	protected EditPart source;
	protected ColumnEditPart target;

	/**
	 * Standard for constructor for a compound command.
	 * 
	 * @param domain
	 *            The editing domain
	 * @param owner
	 *            The object to be modified
	 * @param value
	 *            The value to "set"
	 */
	public AddJoinConditionCommand( final EditPart source,
			final ColumnEditPart target )
	{
		super( );
		this.source = source;
		this.target = target;
	}

	public boolean canExecute( )
	{
		// return super.canExecute();
		boolean canExecute = ( target != null && source != null );

		return canExecute;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute( )
	{

		if ( source == null || target == null )
		{
			return;
		}

		DimensionJoinCondition joinCondition = StructureFactory.createDimensionJoinCondition( );
		joinCondition.setCubeKey( target.getColumnName( ) );
		if ( source instanceof HierarchyColumnEditPart )
			joinCondition.setHierarchyKey( ( (HierarchyColumnEditPart) source ).getColumnName( ) );

		TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) ( (HierarchyNodeEditPart) source.getParent( ) ).getModel( );

		try
		{
			TabularCubeHandle cube = ( (DatasetNodeEditPart) target.getParent( ) ).getCube( );
			getDimensionCondition( cube,
					hierarchy ).addJoinCondition( joinCondition );

		}
		catch ( Exception e )
		{
			ExceptionUtil.handle( e );
		}

	}

	private DimensionConditionHandle getDimensionCondition(
			TabularCubeHandle cube, TabularHierarchyHandle hierarchy )
			throws Exception
	{
		DimensionConditionHandle conditionHandle = cube.findDimensionCondition( hierarchy );
		if ( conditionHandle != null )
			return conditionHandle;
		DimensionCondition dimensionCondition = StructureFactory.createCubeJoinCondition( );
		conditionHandle = cube.addDimensionCondition( dimensionCondition );
		conditionHandle.setHierarchy( hierarchy );
		return conditionHandle;
	}
}