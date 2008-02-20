/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;

/**
 * The class extends <code>AbstractFormHandleProvider</code> and declares two
 * new methods for UI to rebuilt filter page when the reference data set or item
 * handle are changed.
 * 
 * @since 2.3
 */
public abstract class AbstractFilterHandleProvider extends AbstractFormHandleProvider
{

	/**
	 * Returns a concrete filter provider for current data set or binding reference.
	 * 
	 * @return
	 */
	public abstract IFormProvider getConcreteFilterProvider( );
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFormHandleProvider#needRebuilded(org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	public boolean needRebuilded( NotificationEvent event )
	{
		if ( event instanceof PropertyEvent )
		{
			String propertyName = ( (PropertyEvent) event ).getPropertyName( );
			if ( ReportItemHandle.DATA_SET_PROP.equals( propertyName ) ||
					ReportItemHandle.DATA_BINDING_REF_PROP.equals( propertyName ) ||
					ReportItemHandle.CUBE_PROP.equals( propertyName ) )
			{
				return true;
			}
		}

		return false;
	}
}
