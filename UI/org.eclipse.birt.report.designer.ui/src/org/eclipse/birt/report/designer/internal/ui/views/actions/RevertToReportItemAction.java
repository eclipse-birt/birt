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
package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.gef.Request;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * 
 */

public class RevertToReportItemAction extends AbstractElementAction
{	
	/**
	 * @param selectedObject
	 */
	public RevertToReportItemAction( Object selectedObject )
	{
		super( selectedObject);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param selectedObject
	 * @param text
	 */
	public RevertToReportItemAction( Object selectedObject, String text )
	{
		super( selectedObject, text );
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction#doAction()
	 */
	protected boolean doAction( ) throws Exception
	{		
		if ( getSelectedElement( ) == null )
		{
			return false;
		}		
		
		return ProviderFactory.createProvider( getSelectedElement( ) )
		.performRequest( getSelectedElement( ),
				new Request( IRequestConstants.REQUEST_TRANSFER_PLACEHOLDER ) );
		
	}
	




	/**
	 * @return the model of selected GUI object.
	 */
	ReportElementHandle getSelectedElement( )
	{
		Object obj = super.getSelection( );
		if ( obj instanceof IStructuredSelection )
		{
			IStructuredSelection selection = (IStructuredSelection) obj;
			if ( selection.size( ) != 1 )
			{//multiple selection
				return null;
			}
			obj = selection.getFirstElement( );
		}
		if ( obj instanceof TemplateReportItemHandle )
		{
			return (TemplateReportItemHandle) obj;
		}
		return null;
	}
	
	public boolean isEnabled( )
	{
		return super.isEnabled( ) && getSelectedElement() instanceof TemplateReportItemHandle;
	}

}
