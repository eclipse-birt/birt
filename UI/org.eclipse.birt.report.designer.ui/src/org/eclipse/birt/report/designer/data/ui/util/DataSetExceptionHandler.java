
package org.eclipse.birt.report.designer.data.ui.util;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.UIUtil;

public class DataSetExceptionHandler
{

	public static void handle( Throwable e )
	{
		handle( Messages.getString( "ExceptionHandler.Title.Error" ), Messages.getString( "ExceptionHandler.Meesage.ExceptionOccur" ), e ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static void handle( String message, Throwable e )
	{
		handle( Messages.getString( "ExceptionHandler.Title.Error" ), message, e ); //$NON-NLS-1$
	}

	public static void handle( String dialogTitle, String message, Throwable e )
	{
		new ExceptionDialog( UIUtil.getDefaultShell( ), dialogTitle, message, e ).open( );
	}

}
