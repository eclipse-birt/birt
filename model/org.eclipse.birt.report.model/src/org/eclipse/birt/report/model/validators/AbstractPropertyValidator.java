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

package org.eclipse.birt.report.model.validators;

import java.util.List;

import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;

/**
 * The base abstract validator class to validate one specific property of
 * element in report.
 */

public abstract class AbstractPropertyValidator
		extends
			AbstractSemanticValidator
{

	/**
	 * Validates the specific property of the given element in report.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the given element to validate
	 * @param propName
	 *            name of the property to validate
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public abstract List validate( Module module, DesignElement element,
			String propName );

	/**
	 * Checks whether the given element is contained by one of template
	 * parameter definition.
	 * 
	 * @param element
	 *            the design element
	 * @return <code>true</code> if the element is in the template parameter
	 *         definition. Otherwise, <code>false</code>.
	 */

	protected static boolean isTemplateParameterDefinition(
			DesignElement element )
	{
		if ( element == null )
			return false;

		DesignElement tmpContainer = element.getContainer( );
		ContainerContext containerInfo = null;

		while ( tmpContainer != null && !( tmpContainer instanceof Module ) )
		{
			containerInfo = tmpContainer.getContainerInfo( );
			tmpContainer = tmpContainer.getContainer( );
		}

		int slot = containerInfo == null
				? IDesignElementModel.NO_SLOT
				: containerInfo.getSlotID( );

		if ( IReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT == slot )
			return true;

		return false;
	}
}