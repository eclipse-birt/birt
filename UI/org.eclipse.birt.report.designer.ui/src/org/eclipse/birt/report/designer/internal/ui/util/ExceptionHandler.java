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

package org.eclipse.birt.report.designer.internal.ui.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.core.runtime.ErrorStatus;
import org.eclipse.birt.report.designer.core.runtime.GUIException;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * The utility to handle exceptions
 */

public class ExceptionHandler
{

	private static final String TITLE_ERROR = Messages.getString( "ExceptionHandler.Title.Error" ); //$NON-NLS-1$

	private static final String TITLE_FILE_NOT_FOUND = Messages.getString( "ExceptionHandler.Title.FileNotFound" ); //$NON-NLS-1$

	private static final String MSG_FILE_NOT_FOUND_PREFIX = Messages.getString( "ExceptionHandler.Title.FileNotFound" ); //$NON-NLS-1$

	private static final String TITLE_UNKNOWN_HOST = Messages.getString( "ExceptionHandler.Title.UnknownHost" ); //$NON-NLS-1$

	private static final String TITLE_PART_INIT_ERROR = Messages.getString( "ExceptionHandler.Title.PartInitError" ); //$NON-NLS-1$

	private static final String MSG_UNKNOWN_HOST = Messages.getString( "ExceptionHandler.Message.UnknownHost" ); //$NON-NLS-1$

	private static final String MSG_PART_INIT_ERROR = Messages.getString( "ExceptionHandler.Message.PartInitError" ); //$NON-NLS-1$

	private static final String MSG_BIRT_EXCEPTION_OCURR = Messages.getString( "ExceptionHandler.Meesage.BirtExceptionOccur" ); //$NON-NLS-1$

	private static final String MSG_OUT_OF_MEMORY = Messages.getString( "ExceptionHandler.Message.OutOfMemory" ); //$NON-NLS-1$

	private static final String MSG_UNEXPECTED_EXCEPTION_OCURR = Messages.getString( "ExceptionHandler.Meesage.UnexceptedExceptionOccur" ); //$NON-NLS-1$

	private static final String LABEL_ERROR_MESSAGE = Messages.getString( "ExceptionHandler.Label.ErrorMessage" ); //$NON-NLS-1$

	private static final String LABEL_ERROR_CODE = Messages.getString( "ExceptionHandler.Label.ErrorCode" ); //$NON-NLS-1$

	private static final String GUI_ERROR_CODE = "Error.GUIException.invokedByIOException"; //$NON-NLS-1$

	private static List ExpectedExceptionList = new ArrayList( );

	private static boolean isNeedLog = true;

	static
	{
		ExpectedExceptionList.add( SemanticException.class );
	}

	/**
	 * Handles the exception
	 * 
	 * @param e
	 *            the exception to be handled
	 *  
	 */
	public static void handle( Throwable e )
	{
		String title = TITLE_ERROR;
		String message = e.getLocalizedMessage( );
		if ( e instanceof UnknownHostException )
		{
			title = TITLE_UNKNOWN_HOST;
			message = MSG_UNKNOWN_HOST + message;
		}
		else if ( e instanceof FileNotFoundException )
		{
			title = TITLE_FILE_NOT_FOUND;
			message = MSG_FILE_NOT_FOUND_PREFIX + ":" + e.getLocalizedMessage( ); //$NON-NLS-1$
		}
		else if ( e instanceof PartInitException )
		{
			title = TITLE_PART_INIT_ERROR;
			message = MSG_PART_INIT_ERROR;
		}

		handle( e, title, message );
	}

	/**
	 * Handles the exception
	 * 
	 * @param e
	 *            the exception to be handled
	 * @param dialogTitle
	 *            the title of the error dialog
	 * @param message
	 *            the error message
	 *  
	 */
	public static void handle( Throwable e, String dialogTitle, String message )
	{
		ErrorStatus status = createErrorStatus( e );
		if ( status != null )
		{
			ErrorDialog.openError( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ), dialogTitle, message, status );
			if ( status.getException( ) != null )
			{
				ReportPlugin.getDefault( ).getLog( ).log( status );
			}
		}
		else
		{
			openErrorMessageBox( dialogTitle, message );
		}
	}

	private static ErrorStatus createErrorStatus( Throwable e )
	{
		Throwable exception = null;
		String reason = null;
		String[] detail = null;
		if ( !needNotLog( e ) && isNeedLog )
		{
			exception = e;
		}
		if ( e instanceof DesignFileException )
		{
			detail = e.toString( ).split( "\n" ); //$NON-NLS-1$			
			reason = detail[0];
		}
		else if ( e instanceof BirtException )
		{
			BirtException birtException = (BirtException) e;
			detail = new String[]{
					LABEL_ERROR_CODE + ":" + birtException.getErrorCode( ), //$NON-NLS-1$
					LABEL_ERROR_MESSAGE + ":" //$NON-NLS-1$
							+ birtException.getLocalizedMessage( ),
			};
			reason = MSG_BIRT_EXCEPTION_OCURR;
		}
		else
		{
			if ( e instanceof IOException )
			{
				return createErrorStatus( new GUIException( GUI_ERROR_CODE, e ) );
			}
			else if ( e instanceof OutOfMemoryError )
			{
				reason = MSG_OUT_OF_MEMORY;
			}
			else
			{
				reason = MSG_UNEXPECTED_EXCEPTION_OCURR;
			}
			detail = new String[1];
			if ( e.getLocalizedMessage( ) != null )
			{
				if ( e instanceof FileNotFoundException )
				{
					detail[0] = MSG_FILE_NOT_FOUND_PREFIX + ":" //$NON-NLS-1$
							+ e.getLocalizedMessage( );
				}
				else
				{
					detail[0] = e.getLocalizedMessage( );
				}

			}
			else
			{
				detail[0] = e.getClass( ).getName( );
			}
		}
		ErrorStatus status = new ErrorStatus( ReportPlugin.REPORT_UI,
				1001,
				reason,
				exception );
		for ( int i = 0; i < detail.length; i++ )
		{
			status.addError( detail[i] );
		}
		for ( Throwable cause = e.getCause( ); cause != null; cause = cause.getCause( ) )
		{
			status.addCause( cause );
		}
		return status;
	}

	private static boolean needNotLog( Throwable e )
	{
		for ( Iterator itor = ExpectedExceptionList.iterator( ); itor.hasNext( ); )
		{
			if ( ( (Class) itor.next( ) ).isInstance( e ) )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Opens a message box with given title and message in the specified style
	 * 
	 * @param title
	 *            the title of the message box
	 * @param message
	 *            the message displayed in the message box
	 * @param style
	 *            the style of the message box
	 */
	public static void openMessageBox( String title, String message, int style )
	{
		MessageBox box = new MessageBox( UIUtil.getDefaultShell( ), style );
		box.setText( title );
		box.setMessage( message );
		box.open( );
	}

	/**
	 * Opens an error message box with given title and message. It equals to
	 * call openMessageBox(title,message,SWT.ICON_ERROR)
	 * 
	 * @param title
	 *            the title of the message box
	 * @param errorMessage
	 *            the message displayed in the message box
	 */
	public static void openErrorMessageBox( String title, String errorMessage )
	{
		openMessageBox( title, errorMessage, SWT.ICON_ERROR );
	}

	public static void setNeedLog( boolean isNeedLog )
	{
		ExceptionHandler.isNeedLog = isNeedLog;
	}
}