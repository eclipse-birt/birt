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

package org.eclipse.birt.report.designer.ui.actions;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.action.Action;



/**
 * 
 */

public class NewParameterAction extends Action
{
	   
	public static final String INSERT_SCALAR_PARAMETER = "org.eclipse.birt.report.designer.ui.actions.newScalarParameter";
	
	public static final String INSERT_PARAMETER_GROUP = "org.eclipse.birt.report.designer.ui.actions.newParameterGroup";
	
	public static final String INSERT_CASCADING_PARAMETER_GROUP = "org.eclipse.birt.report.designer.ui.actions.newCascadingParameterGroup";
	
	private Action action = null;
	
	private String type;

	/**
	 * @param text
	 */
	public NewParameterAction( String ID ,String type)
	{
		super( );
		// TODO Auto-generated constructor stub
		setId(ID);

		this.type = type;
	}



	/*
	 * (non-Javadoc) Method declared on IAction.
	 */
	public boolean isEnabled() {
		if(action == null)
		{
			action = new InsertAction( SessionHandleAdapter.getInstance( ).getReportDesignHandle( ).getParameters( ),
					Messages.getString( "ParametersNodeProvider.menu.text.cascadingParameter" ), //$NON-NLS-1$
					type);
		}
		return action.isEnabled( );
	}
	
    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run()
    {
    	action.run( );
    }
    


}
