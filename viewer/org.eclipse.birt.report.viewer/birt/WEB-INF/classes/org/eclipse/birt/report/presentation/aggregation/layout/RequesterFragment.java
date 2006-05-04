package org.eclipse.birt.report.presentation.aggregation.layout;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.presentation.aggregation.BirtBaseFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.DialogContainerFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.ExceptionDialogFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.ParameterDialogFragment;
import org.eclipse.birt.report.presentation.aggregation.layout.DocumentFragment;


public class RequesterFragment extends BirtBaseFragment
{
	/**
	 * Override implementation of doPostService.
	 */
	protected String doPostService( HttpServletRequest request, HttpServletResponse response )
		throws ServletException, IOException
	{
		String className = getClass( ).getName( )
				.substring( getClass( ).getName( ).lastIndexOf( '.' ) + 1 );
		return JSPRootPath + "/pages/layout/" + className + ".jsp"; //$NON-NLS-1$  //$NON-NLS-2$
	}
	
	/**
	 * Override build method.
	 */
	protected void build( )
	{
		addChild( new DocumentFragment( ) );
		addChild( new DialogContainerFragment( new ExceptionDialogFragment( ) ) );
		addChild( new DialogContainerFragment( new ParameterDialogFragment( ) ) );
	}
}
