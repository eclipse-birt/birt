/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.ui.frameworks.taskwizard;

/**
 * 
 */

public class ErrorsManager
{

	// Singleton Instance of ErrorsManager
	private static ErrorsManager thisInstance = null;

	public static final String YSERIESDATA = "YSeriesData"; //$NON-NLS-1$

	private String errorMessage = null;

	private WizardBase wizard = null;

	/**
	 * This method returns the instance of ErrorsManager. If an instance does
	 * not exist, one is created.
	 * 
	 * @return Singleton instance of ErrorsManager
	 */
	public static ErrorsManager instance( )
	{
		if ( thisInstance == null )
		{
			thisInstance = new ErrorsManager( );
		}
		return thisInstance;
	}

	// PRIVATE CONSTRUCTOR OF A SINGLETON
	private ErrorsManager( )
	{

	}

	/**
	 * This method registers a wizard with the ErrorsManager.
	 * 
	 * @param wizard
	 */
	public void registerWizard( WizardBase wizard )
	{
		this.wizard = wizard;
	}

	/**
	 * Set the error message to the wizard dialog.
	 * 
	 * @param t
	 *            Exception
	 */
	public void showErrors( String errorMessage )
	{
		this.errorMessage = errorMessage;
		if ( wizard.getDialog( ).getShell( ) == null || wizard.getDialog( ).getShell( ).isDisposed( ) )
		{
			return;
		}
		
		wizard.getDialog( ).setErrorMessage( errorMessage );
	}

	/**
	 * Clean the error message in the wizard dialog.
	 */
	public void removeErrors( )
	{
		this.errorMessage = null;
		if ( !wizard.isDisposed( ) )
		{
			wizard.getDialog( ).setErrorMessage( null );
		}
	}

	public String getErrors( )
	{
		return errorMessage;
	}
}
