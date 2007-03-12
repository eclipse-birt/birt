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

package org.eclipse.birt.integration.wtp.ui.internal.wizards;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.integration.wtp.ui.internal.exception.BirtCoreException;
import org.eclipse.birt.integration.wtp.ui.internal.resource.BirtWTPMessages;
import org.eclipse.birt.integration.wtp.ui.internal.util.Logger;
import org.eclipse.birt.integration.wtp.ui.internal.util.WebArtifactUtil;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.WebAppBean;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jst.servlet.ui.project.facet.WebProjectWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

/**
 * Implement a wizard for creating a new BIRT Web Project. This wizard extends
 * "Dynamic Web Project" wizard.
 * 
 */
public class BirtWebProjectWizard extends WebProjectWizard
		implements
			IBirtWizardConstants
{

	/**
	 * Configuration Element of birt wizard
	 */
	private IConfigurationElement wizardConfigElement;

	/**
	 * Birt Project Configuration Wizard Page
	 */
	private IWizardPage configPage = null;

	/**
	 * Birt Project Properties
	 */
	private Map properties;

	/**
	 * Web Content Folder
	 */
	private String configFolder;

	/**
	 * Constructor
	 * 
	 */
	public BirtWebProjectWizard( )
	{
		super( );
		setWindowTitle( BirtWTPMessages.BIRTProjectCreationWizard_title );
		setNeedsProgressMonitor( true );
		properties = new HashMap( );
	}

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public BirtWebProjectWizard( IDataModel model )
	{
		super( model );
		setWindowTitle( BirtWTPMessages.BIRTProjectCreationWizard_title );
		setNeedsProgressMonitor( true );
		properties = new HashMap( );
	}

	/**
	 * Get template for project facets selection
	 */
	protected IFacetedProjectTemplate getTemplate( )
	{
		return ProjectFacetsManager.getTemplate( "template.birt.runtime" ); //$NON-NLS-1$
	}

	/**
	 * Initialize wizard
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init( IWorkbench workbench, IStructuredSelection selection )
	{
		super.init( workbench, selection );

		// find configuration element of new wizard
		this.wizardConfigElement = BirtWizardUtil.findConfigurationElementById(
				NEW_WIZARDS_EXTENSION_POINT, BIRT_WIZARD_ID );

		// set window title
		String title = wizardConfigElement.getAttribute( "name" ); //$NON-NLS-1$
		if ( title != null )
			setWindowTitle( title );

		// initialize webapp settings
		BirtWizardUtil.initWebapp( this.properties );
	}

	/**
	 * Returns next wizard page.
	 * <p>
	 * Append BIRT Configuration wizard page.
	 * 
	 * @see org.eclipse.wst.common.project.facet.ui.AddRemoveFacetsWizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	public IWizardPage getNextPage( IWizardPage wizardPage )
	{
		if ( wizardPage == configPage )
			return null;

		IWizardPage nextPage = super.getNextPage( wizardPage );
		if ( nextPage == null
				&& BirtWizardUtil.isSelectedFacetInstalled( getDataModel( ),
						"birt.runtime" ) ) //$NON-NLS-1$
		{
			if ( configPage == null )
			{
				// Create BIRT Configuration wizard page
				configPage = new BirtWebProjectWizardConfigurationPage(
						(Map) properties.get( EXT_CONTEXT_PARAM ) );
				addPage( configPage );
			}
			return configPage;
		}
		else
		{
			return nextPage;
		}
	}

	/**
	 * Do perform finish
	 * 
	 * @see org.eclipse.wst.web.ui.internal.wizards.NewProjectDataModelFacetWizard#performFinish(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void performFinish( IProgressMonitor monitor )
			throws CoreException
	{
		super.performFinish( monitor );

		// get web content folder
		configFolder = BirtWizardUtil.getConfigFolder( getDataModel( ) );
		if ( configFolder == null )
		{
			String message = BirtWTPMessages.BIRTErrors_wrong_webcontent;
			Logger.log( Logger.ERROR, message );
			throw BirtCoreException.getException( message, null );
		}

		// process BIRT Configuration
		preConfiguration( monitor );
		processConfiguration( monitor );

		// done
		monitor.done( );
	}

	/**
	 * Action before process configuration
	 * 
	 * @param monitor
	 * @throws CoreException
	 */
	protected void preConfiguration( IProgressMonitor monitor )
			throws CoreException
	{
		// check folder settings
		BirtWizardUtil.processCheckFolder( properties,
				this.fproj.getProject( ), configFolder, monitor );
	}

	/**
	 * Process BIRT deployment configuration.
	 * <p>
	 * Save user-defined settings into web.xml file.
	 * 
	 * @param monitor
	 * @throws CoreException
	 */
	protected void processConfiguration( IProgressMonitor monitor )
			throws CoreException
	{
		IProject project = fproj.getProject( );

		// Simple OverwriteQuery
		SimpleImportOverwriteQuery query = new SimpleImportOverwriteQuery( );

		// configure WebArtifact
		WebArtifactUtil.configureWebApp( (WebAppBean) properties
				.get( EXT_WEBAPP ), project, query, monitor );		
		
		WebArtifactUtil.configureContextParam( (Map) properties
				.get( EXT_CONTEXT_PARAM ), project, query, monitor );

		WebArtifactUtil.configureListener(
				(Map) properties.get( EXT_LISTENER ), project, query, monitor );

		WebArtifactUtil.configureServlet( (Map) properties.get( EXT_SERVLET ),
				project, query, monitor );

		WebArtifactUtil.configureServletMapping( (Map) properties
				.get( EXT_SERVLET_MAPPING ), project, query, monitor );

		WebArtifactUtil.configureTaglib( (Map) properties.get( EXT_TAGLIB ),
				project, query, monitor );
	}
}
