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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Shell;

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

	private static final String LINE_SEPARATOR = System.getProperty( "line.separator" ); //$NON-NLS-1$
	
	private static final int WIDTH_OFFSET = 100;
	
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
		
		// fix T66267
		wizard.getDialog( )
				.setErrorMessage( addLineSeparator( wizard.getDialog( )
						.getShell( ), errorMessage ) );
	}

	/**
	 * format error message,separator line according to the Shell width
	 * 
	 * @param shell
	 * @param errorMessage
	 * @return
	 */
	private static String addLineSeparator( Shell shell, String errorMessage )
	{
		if ( errorMessage == null )
		{
			return null;
		}

		int width = shell.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x
				- WIDTH_OFFSET;
		GC gc = new GC( shell );
		int maxChar = width / gc.getFontMetrics( ).getAverageCharWidth( );

		StringBuffer sb = new StringBuffer( );
		int currentLineLength = 0;
		char[] chars = errorMessage.toCharArray( );
		for ( char tempChar : chars )
		{
			sb.append( tempChar );
			currentLineLength++;
			if ( LINE_SEPARATOR.contains( new String( new char[]{
				tempChar
			} ) ) )
			{
				currentLineLength = 0;
			}

			if ( currentLineLength == maxChar )
			{
				// in windows, use '\n' instead of '\r\n'
				sb.append( LINE_SEPARATOR.length( ) == 2 ? LINE_SEPARATOR.charAt( 1 )
						: LINE_SEPARATOR );
				currentLineLength = 0;
			}
		}
		return sb.toString( );
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
