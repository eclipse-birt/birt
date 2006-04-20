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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.model.elements.interfaces.ICascadingParameterGroupModel;

/**
 * Represents the group of cascading parameters. Cascading parameters are
 * created under the group.
 */

public class CascadingParameterGroupHandle extends ParameterGroupHandle
		implements
			ICascadingParameterGroupModel
{

	/**
	 * Constructs the handle for a group of cascading parameters with the given
	 * design and element.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the cascading parameter group element instance.
	 */

	public CascadingParameterGroupHandle( Module module,
			CascadingParameterGroup element )
	{
		super( module, element );
	}

	/**
	 * Returns the handle for the data set defined on the cascading parameter
	 * group.
	 * 
	 * @return the handle to the data set
	 */

	public DataSetHandle getDataSet( )
	{
		DesignElement dataSet = ( (CascadingParameterGroup) getElement( ) )
				.getDataSetElement( module );
		if ( dataSet == null )
			return null;

		return (DataSetHandle) dataSet.getHandle( dataSet.getRoot( ) );
	}

	/**
	 * Sets the data set of the report item.
	 * 
	 * @param handle
	 *            the handle of the data set, if <code>handle</code> is null,
	 *            data set property will be cleared.
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setDataSet( DataSetHandle handle ) throws SemanticException
	{
		if ( handle == null )
			setStringProperty( DATA_SET_PROP, null );
		else
		{
			ModuleHandle moduleHandle = handle.getRoot( );
			String valueToSet = handle.getName( );
			if ( moduleHandle instanceof LibraryHandle )
			{
				String namespace = ( (LibraryHandle) moduleHandle )
						.getNamespace( );
				valueToSet = StringUtil.buildQualifiedReference( namespace,
						handle.getName( ) );
			}

			setStringProperty( DATA_SET_PROP, valueToSet );
		}
	}

	/**
	 * Set the value for the display prompt context.
	 * 
	 * @param promptValue
	 *            The display prompt context.
	 * 
	 * @throws SemanticException
	 * 
	 */

	public void setPromptText( String promptValue ) throws SemanticException
	{
		setStringProperty( PROMPT_TEXT_PROP, promptValue );
	}

	/**
	 * get the display prompt text.
	 * 
	 * @return the display prompt.
	 * 
	 */

	public String getPromptText( )
	{
		return getStringProperty( PROMPT_TEXT_PROP );
	}
}
