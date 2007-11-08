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

package org.eclipse.birt.report.designer.ui.parameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * Parameter factory which can create Parameter and Parameter Group see
 * <code>IParameter</code>
 */
public class ParameterFactory
{

	private static final String RADIO_BUTTON = DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON;

	private static final String TEXT_BOX = DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX;

	private static final String LIST_BOX = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX;

	private IGetParameterDefinitionTask task;

	/**
	 * Constructor.
	 * 
	 * @param task
	 */
	public ParameterFactory( IGetParameterDefinitionTask task )
	{
		this.task = task;
	}

	/**
	 * Gets children of root.
	 * 
	 * @param task
	 * @return children of root.
	 */
	public List getRootChildren( )
	{
		IReportRunnable runnable = task.getReportRunnable( );
		if ( runnable == null )
			return null;

		DesignElementHandle designHandle = runnable.getDesignHandle( );
		if ( designHandle == null )
			return null;

		assert designHandle.getRoot( ) != null;

		List parameters = designHandle.getRoot( )
				.getParametersAndParameterGroups( );
		Iterator iterator = parameters.iterator( );

		// The design handle of root is null.

		List childrenList = new ArrayList( );
		while ( iterator.hasNext( ) )
		{
			DesignElementHandle handle = (DesignElementHandle) iterator.next( );

			if ( handle instanceof ScalarParameterHandle )
			{
				// build parameter
				IParameter param = createScalarParameter( (ScalarParameterHandle) handle );
				childrenList.add( param );
			}
			else if ( handle instanceof ParameterHandle )
			{
				// Now do nothing.
			}
			else if ( handle instanceof CascadingParameterGroupHandle )
			{
				// build cascading parameter
				ParameterGroupHandle groupHandle = (ParameterGroupHandle) handle;
				IParameterGroup group = new CascadingParameterGroup( groupHandle );
				childrenList.add( group );

				createParameterGroup( group, groupHandle );
			}
			else if ( handle instanceof ParameterGroupHandle )
			{
				// build parameter group
				ParameterGroupHandle groupHandle = (ParameterGroupHandle) handle;
				IParameterGroup group = new ParameterGroup( groupHandle );
				childrenList.add( group );

				createParameterGroup( group, groupHandle );
			}
		}

		return childrenList;
	}

	/**
	 * Creates parameter group.
	 * 
	 * @param group
	 * @param task
	 * @param groupHandle
	 */
	private void createParameterGroup( IParameterGroup group,
			ParameterGroupHandle groupHandle )
	{
		assert group != null;
		assert groupHandle != null;

		SlotHandle slotHandle = groupHandle.getSlot( ParameterGroupHandle.PARAMETERS_SLOT );

		// Now parameter group only contains parameter. can't contain parameter
		// group.

		Iterator iterator = slotHandle.iterator( );
		while ( iterator.hasNext( ) )
		{
			ParameterHandle handle = (ParameterHandle) iterator.next( );
			createParameter( group, handle );
		}
	}

	/**
	 * Create parameter.
	 * 
	 * @param parentGroup
	 * @param paramHandle
	 */
	private IParameter createParameter( IParameterGroup parentGroup,
			ParameterHandle paramHandle )
	{
		assert parentGroup != null;
		assert paramHandle != null;

		IParameter param = null;

		if ( paramHandle instanceof ScalarParameterHandle )
		{
			param = createScalarParameter( (ScalarParameterHandle) paramHandle );
		}

		// TODO for other parameter type.

		if ( param != null )
		{
			parentGroup.addParameter( param );
		}

		return param;
	}

	/**
	 * Creates scalar parameter.
	 * 
	 * @param paramHandle
	 * @return scalar parameter.
	 */
	private ScalarParameter createScalarParameter(
			ScalarParameterHandle paramHandle )
	{
		ScalarParameter param = null;
		String controlType = paramHandle.getControlType( );
		if ( controlType.equals( LIST_BOX ) )
		{
			boolean mustMatch = paramHandle.isMustMatch( );
			if ( mustMatch )
			{
				// list - box
				param = new ListBoxParameter( paramHandle, task );
			}
			else
			{
				// combo-box
				param = new ComboBoxParameter( paramHandle, task );
			}
		}
		else if ( controlType.equals( TEXT_BOX ) )
		{
			param = new StaticTextParameter( paramHandle, task );
		}
		else if ( controlType.equals( RADIO_BUTTON ) )
		{
			param = new RadioParameter( paramHandle, task );
		}

		return param;
	}

}
