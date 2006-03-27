/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.context;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.MessageConstants;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.servlet.ViewerServlet;
import org.eclipse.birt.report.utility.MessageUtility;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Data bean for viewing request. Birt viewer distributes process logic into
 * viewer fragments. Each fragment seperates its front-end and back-end process
 * into jsp page and "code behand" fragment class. Viewer attribute bean serves
 * as:
 * <ol>
 * <li> object that carries the data shared among different fragments</li>
 * <li> object that carries the date shared between front-end jsp page and
 * back-end class</li>
 * </ol>
 * In current implementation, ViewerAttributeBean uses request scope.
 * <p>
 */
public class ViewerAttributeBean
{

	/**
	 * Identify the incoming request category.
	 */
	private String category = "BIRT"; //$NON-NLS-1$

	/**
	 * Need to store the exception.
	 */
	private Exception exception = null;

	/**
	 * Report runnable.
	 */
	private IReportRunnable reportRunnable = null;

	/**
	 * Report document.
	 */
	private IReportDocument reportDocumentInstance = null;

	/**
	 * Get report parameter task.
	 */
	private IGetParameterDefinitionTask parameterTask = null;

	/**
	 * Get report parameters passed in by URL.
	 */
	private HashMap parameters = null;

	/**
	 * Whether missing parameters.
	 */
	private boolean missingParameter = false;

	/**
	 * scalar parameter bean.
	 */
	private ParameterAttributeBean parameterBean = null;

	/**
	 * Report document name.
	 */
	private String reportDocumentName = null;

	/**
	 * Report title.
	 */
	private String reportTitle = null;

	/**
	 * Report page number.
	 */
	private String reportPage = null;

	/**
	 * Current locale.
	 */
	private Locale locale = null;

	/**
	 * Enable master page content.
	 */
	private boolean masterPageContent = true;

	/**
	 * In designer context.
	 */
	private boolean isDesigner = false;

	/**
	 * Bookmark.
	 */
	private String bookmark = null;

	/**
	 * Constructor.
	 */
	public ViewerAttributeBean( HttpServletRequest request )
	{
		this.init( request );
	}

	/**
	 * Init the bean.
	 * 
	 * @param request
	 * @throws Exception
	 */
	public void init( HttpServletRequest request )
	{
		String servletPath = request.getServletPath( );
		if ( servletPath.indexOf( "/wr" ) != -1 ) //$NON-NLS-1$
		{
			this.category = "ERNI"; //$NON-NLS-1$
		}

		if ( ParameterAccessor.isGetImageOperator( request ) )
		{
			return;
		}

		this.masterPageContent = ParameterAccessor
				.isMasterPageContent( request );

		// Is in designer?
		this.isDesigner = ParameterAccessor.isDesigner( request );

		// Determine the report design and doc 's timestamp
		this.reportDocumentName = ParameterAccessor.getReportDocument( request );
		File reportDocFile = new File( this.reportDocumentName );

		String reportDesignName = ParameterAccessor.getReport( request );
		File reportDesignDocFile = new File( reportDesignName );

		// If it is in designer and refresh the browser.
		if ( reportDesignDocFile != null && reportDesignDocFile.exists( )
				&& reportDesignDocFile.isFile( ) && reportDocFile != null
				&& reportDocFile.exists( ) && reportDocFile.isFile( )
				&& "get".equalsIgnoreCase( request.getMethod( ) ) ) //$NON-NLS-1$
		{
			if ( reportDesignDocFile.lastModified( ) > reportDocFile
					.lastModified( )
					|| ParameterAccessor.isOverwrite( request ) )
			{
				reportDocFile.delete( );
			}
		}

		// bug129777 add fold document
		if ( reportDocFile.exists( ) && ( reportDocFile.isFile( ) || reportDocFile.isDirectory( ) ) )
		{
			this.reportDocumentInstance = ReportEngineService.getInstance( )
					.openReportDocument( this.reportDocumentName );
			if ( this.reportDocumentInstance == null )
			{
				this.exception = new Exception( "Can not open report doc from " //$NON-NLS-1$
						+ ParameterAccessor.getReportDocument( request ) );
				return;
			}

			this.reportRunnable = this.reportDocumentInstance
					.getReportRunnable( );
		}
		else
		{
			try
			{
				this.reportRunnable = ReportEngineService.getInstance( )
						.openReportDesign(
								ParameterAccessor.getReport( request ) );
			}
			catch ( EngineException e )
			{
				this.exception = e;
				return;
			}
		}

		// Double check the runnable.
		if ( this.reportRunnable == null )
		{
			this.exception = new Exception( "Can not get report runnable" ); //$NON-NLS-1$
			return;
		}

		// Report locale.
		this.locale = ParameterAccessor.getLocale( request );

		// Report page.
		this.reportPage = String.valueOf( ParameterAccessor.getPage( request ) );

		// Report title.
		String title = null;
		if ( this.reportRunnable != null )
		{
			title = (String) this.reportRunnable.getProperty( "title" ); //$NON-NLS-1$
		}
		if ( title == null || title.trim( ).length( ) <= 0 )
		{
			title = BirtResources.getString( "birt.viewer.title" ); //$NON-NLS-1$
		}
		this.reportTitle = ParameterAccessor.htmlEncode( title );

		// Bookmark.
		this.bookmark = ParameterAccessor.getBookmark( request );

		// Prameter task.
		this.parameterTask = ReportEngineService.getInstance( )
				.createGetParameterDefinitionTask( this.reportRunnable );
		if ( this.parameterTask != null )
		{
			this.parameterTask.setLocale( this.locale );
		}

		// Prepare the report parameters
		this.parameters = ReportEngineService.getInstance( ).parseParameters(
				request, this.parameterTask,
				this.reportRunnable.getTestConfig( ), this.locale );

		this.missingParameter = ReportEngineService.getInstance( )
				.validateParameters( this.parameterTask, this.parameters );
	}

	/**
	 * Finalize the instance.
	 */
	public void finalize( )
	{
		if ( this.parameterTask != null )
		{
			this.parameterTask.close( );
			this.parameterTask = null;
		}

		if ( this.reportDocumentInstance != null )
		{
			this.reportDocumentInstance.close( );
			this.reportDocumentInstance = null;
		}

		if ( this.reportRunnable != null )
		{
			this.reportRunnable = null;
		}
	}

	/**
	 * @return Returns the parameterTask.
	 */
	public IGetParameterDefinitionTask getParameterTask( )
	{
		return parameterTask;
	}

	/**
	 * @param parameterTask
	 *            The parameterTask to set.
	 */
	public void setParameterTask( IGetParameterDefinitionTask parameterTask )
	{
		this.parameterTask = parameterTask;
	}

	/**
	 * @return Returns the reportRunnable.
	 */
	public IReportRunnable getReportRunnable( )
	{
		return reportRunnable;
	}

	/**
	 * @param reportRunnable
	 *            The reportRunnable to set.
	 */
	public void setReportRunnable( IReportRunnable reportRunnable )
	{
		this.reportRunnable = reportRunnable;
	}

	/**
	 * @return Returns the parameterBean.
	 */
	public ParameterAttributeBean getParameterBean( )
	{
		return parameterBean;
	}

	/**
	 * @param parameterBean
	 *            The parameterBean to set.
	 */
	public void setParameterBean( ParameterAttributeBean parameterBean )
	{
		this.parameterBean = parameterBean;
	}

	/**
	 * Get report parameters.
	 * 
	 * @return collection of report parameter definition.
	 */
	public Collection getReportParameters( )
	{
		if ( parameterTask != null )
		{
			return parameterTask.getParameters( ).getContents( );
		}
		return null;

	}

	/**
	 * Get report test config variables.
	 * 
	 * @return hash map of test config variables
	 */
	public HashMap getReportTestConfig( )
	{
		if ( reportRunnable != null )
		{
			return reportRunnable.getTestConfig( );
		}

		return null;
	}

	/**
	 * @return Returns the reportTitle.
	 */
	public String getReportTitle( )
	{
		return reportTitle;
	}

	/**
	 * @param reportTitle
	 *            The reportTitle to set.
	 */
	public void setReportTitle( String reportTitle )
	{
		this.reportTitle = reportTitle;
	}

	/**
	 * @return Returns the reportPage.
	 */
	public String getReportPage( )
	{
		return reportPage;
	}

	/**
	 * @param reportPage
	 *            The reportPage to set.
	 */
	public void setReportPage( String reportPage )
	{
		this.reportPage = reportPage;
	}

	/**
	 * @return Returns the locale.
	 */
	public Locale getLocale( )
	{
		return locale;
	}

	/**
	 * @param locale
	 *            The locale to set.
	 */
	public void setLocale( Locale locale )
	{
		this.locale = locale;
	}

	/**
	 * @return Returns the useTestConfig.
	 */
	public boolean isDesigner( )
	{
		return isDesigner;
	}

	/**
	 * @return Returns the exception.
	 */
	public Exception getException( )
	{
		return exception;
	}

	/**
	 * @return Returns the reportDocumentName.
	 */
	public String getReportDocumentName( )
	{
		return reportDocumentName;
	}

	/**
	 * @return Returns the reportDocumentInstance.
	 */
	public IReportDocument getReportDocumentInstance( )
	{
		return reportDocumentInstance;
	}

	/**
	 * @return Returns the bookmark.
	 */
	public String getBookmark( )
	{
		return bookmark;
	}

	/**
	 * @return Returns the parameters.
	 */
	public HashMap getParameters( )
	{
		return parameters;
	}

	/**
	 * @return Returns the masterPageContent.
	 */
	public boolean isMasterPageContent( )
	{
		return masterPageContent;
	}

	/**
	 * @return Returns the missingParameter.
	 */
	public boolean isMissingParameter( )
	{
		return missingParameter;
	}

	/**
	 * @return Returns incoming request's category.
	 */
	public String getCategory( )
	{
		return category;
	}
}