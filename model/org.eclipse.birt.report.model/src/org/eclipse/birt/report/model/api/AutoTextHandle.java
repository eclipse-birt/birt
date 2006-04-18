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
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.AutoText;
import org.eclipse.birt.report.model.elements.interfaces.IAutoTextModel;

public class AutoTextHandle extends ReportItemHandle implements IAutoTextModel
{

	public AutoTextHandle( Module module, DesignElement element )
	{
		super( module, element );

	}

	/**
	 * Returns the autotext type for this parameter. The autotext type counts
	 * the page number or total page number. Types are defined in
	 * <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>PAGE_NUMBER</code>
	 * <li><code>TOTAL_PAGE</code>
	 * </ul>
	 * 
	 * @return the type for the parameter
	 * 
	 * @see #setAutoTextType(String)
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */
	public String getAutoTextType( )
	{
		return getStringProperty( AutoText.AUTOTEXT_TYPE );
	}

	/**
	 * Sets the autotext type for this parameter. The autotext type counts the
	 * page number or total page number. Types are defined in
	 * <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>PAGE_NUMBER</code>
	 * <li><code>TOTAL_PAGE</code>
	 * </ul>
	 * 
	 * @param type
	 *            the type for the parameter
	 * 
	 * @throws SemanticException
	 *             if the input type is not one of above choices.
	 * @see #getAutoTextType()
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public void setAutoTextType( String type ) throws SemanticException
	{
		setStringProperty( AutoText.AUTOTEXT_TYPE, type );
	}

}
