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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.model.api.CommandStack;

/**
 * Base class of all actions prcess on model element for non GEF views
 */

public abstract class AbstractElementAction extends AbstractViewAction
{

	/**
	 * @param selectedObject
	 */
	public AbstractElementAction( Object selectedObject )
	{
		super( selectedObject );
	}

	/**
	 * @param selectedObject
	 * @param text
	 */
	public AbstractElementAction( Object selectedObject, String text )
	{
		super( selectedObject, text );
	}

	/**
	 * Runs this action. The detail implementation must define in the method
	 * <code>doAction</code>
	 */
	public void run( )
	{
		CommandStack stack = getCommandStack( );
		stack.startTrans( getTransactionLabel( ) );
		try
		{
			if ( doAction( ) )
			{
				stack.commit( );
			}
			else
			{
				stack.rollbackAll( );
			}
		}
		catch ( Exception e )
		{
			stack.rollbackAll( );
			handleException( e );
		}

	}

	/**
	 * Gets the activity stack of the report
	 * 
	 * @return returns the stack
	 */
	protected CommandStack getCommandStack( )
	{
		return SessionHandleAdapter.getInstance( )
				.getReportDesign( )
				.getActivityStack( );
	}

	/**
	 * Gets the label for the transaction.The default implement is to return the
	 * text of the action.Subclasses may override this method
	 * 
	 * @return Returns the label for the transaction
	 */
	protected String getTransactionLabel( )
	{
		return getText( );
	}

	/**
	 * Defines the detail implementation of the action.Subclasses must implement
	 * this method
	 * 
	 * @param Returns
	 *            if the
	 */
	abstract protected boolean doAction( ) throws Exception;

	/**
	 * Handles the exception.The default implementation is to pop up an error
	 * message box to show the exception message.
	 */
	protected void handleException( Exception e )
	{
		ExceptionHandler.handle( e );
	}

}