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

import java.net.URL;
import java.util.Map;

import org.eclipse.birt.integration.wtp.ui.BirtWTPUIPlugin;
import org.eclipse.birt.integration.wtp.ui.internal.resource.BirtWTPMessages;
import org.eclipse.birt.integration.wtp.ui.internal.util.DataUtil;
import org.eclipse.birt.integration.wtp.ui.internal.util.UIUtil;
import org.eclipse.birt.integration.wtp.ui.internal.util.WebArtifactUtil;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;

/**
 * This wizard page is to configure Birt deployment settings.
 * <p>
 * These settings will be rewrited in web.xml.
 * <p>
 * <ol>
 * <li>BIRT_RESOURCE_PATH</li>
 * <li>BIRT_VIEWER_DOCUMENT_FOLDER</li>
 * <li>BIRT_VIEWER_IMAGE_DIR</li>
 * <li>BIRT_VIEWER_SCRIPTLIB_DIR</li>
 * <li>BIRT_VIEWER_LOG_DIR</li>
 * <li>DOCUMENT_FOLDER_ACCESS_ONLY</li>
 * <li>BIRT_OVERWRITE_DOCUMENT</li>
 * <li>BIRT_VIEWER_MAX_ROWS</li>
 * <li>BIRT_VIEWER_LOG_LEVEL</li>
 * </ol>
 * 
 */
public class BirtWebProjectWizardConfigurationPage extends WizardPage
		implements
			IBirtWizardConstants
{

	/**
	 * Page Properties Map
	 */
	protected Map properties;

	/**
	 * Value for "BIRT_RESOURCE_PATH" setting
	 */
	protected Text txtResourceFolder;

	/**
	 * Value for "BIRT_VIEWER_DOCUMENT_FOLDER" setting
	 */
	protected Text txtDocumentFolder;

	/**
	 * Value for "DOCUMENT_FOLDER_ACCESS_ONLY" setting
	 */
	protected Button btAccessOnly;

	/**
	 * Value for "BIRT_VIEWER_IMAGE_DIR" setting
	 */
	protected Text txtImageFolder;

	/**
	 * Value for "BIRT_VIEWER_SCRIPTLIB_DIR" setting
	 */
	protected Text txtScriptlibFolder;

	/**
	 * Value for "BIRT_VIEWER_LOG_DIR" setting
	 */
	protected Text txtLogFolder;

	/**
	 * Value for "BIRT_VIEWER_LOG_LEVEL" setting
	 */
	protected Button btLogLevel;

	/**
	 * Value for "BIRT_OVERWRITE_DOCUMENT" setting
	 */
	protected Button btOverwrite;

	/**
	 * Value for "BIRT_VIEWER_MAX_ROWS" setting
	 */
	protected Text txtMaxRows;

	/**
	 * Value for "BIRT_VIEWER_LOG_LEVEL" setting
	 */
	protected Combo cbLogLevel;

	/**
	 * Constructor
	 * 
	 * @param props
	 */
	public BirtWebProjectWizardConfigurationPage( Map properties )
	{
		super( BIRT_CONFIGURATION_PAGE_NAME );
		this.properties = properties;
		setTitle( BirtWTPMessages.BIRTProjectConfigurationPage_title );
		setDescription( BirtWTPMessages.BIRTProjectConfigurationPage_desc );
		ImageDescriptor imageDesc = getDefaultPageImageDescriptor( );
		if ( imageDesc != null )
			setImageDescriptor( imageDesc );
	}

	/**
	 * Create Configuration Page
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewProjectCreationPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NULL );
		composite.setFont( parent.getFont( ) );

		initializeDialogUnits( parent );

		composite.setLayout( new GridLayout( ) );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		// create folder configuration group
		Group paths = new Group( composite, SWT.NULL );
		paths.setLayout( new GridLayout( ) );
		paths.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		paths
				.setText( BirtWTPMessages.BIRTConfiguration_group_paths );
		paths.setEnabled( true );

		// Initialize UI Utility
		UIUtil uit = new UIUtil( properties );

		// create resource folder setting group
		this.txtResourceFolder = uit.createResourceFolderGroup( paths );

		// create document folder setting group
		this.txtDocumentFolder = uit.createDocumentFolderGroup( paths );

		// create image folder setting group
		this.txtImageFolder = uit.createImageFolderGroup( paths );

		// create scriptlib folder setting group
		this.txtScriptlibFolder = uit.createScriptLibFolderGroup( paths );

		// create log folder setting group
		this.txtLogFolder = uit.createLogFolderGroup( paths );

		// create other configuration group
		Group others = new Group( composite, SWT.NULL );
		others.setLayout( new GridLayout( ) );
		others.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		others
				.setText( BirtWTPMessages.BIRTConfiguration_group_others );
		others.setEnabled( true );

		// create ifaccess only setting group
		this.btAccessOnly = uit.createAccessOnlyGroup( others );

		// create overwrite setting group
		this.btOverwrite = uit.createOverwriteGroup( others );

		// create log level setting group
		this.cbLogLevel = uit.createLogLevelGroup( others );

		// create max rows setting group
		this.txtMaxRows = uit.createMaxRowsGroup( others );

		// initialize page properties map
		initializeProperties( );

		setControl( composite );
	}

	/**
	 * Returns the default page banner image
	 * 
	 * @return
	 */
	protected ImageDescriptor getDefaultPageImageDescriptor( )
	{
		final Bundle bundle = Platform.getBundle( BirtWTPUIPlugin.PLUGIN_ID );
		if ( bundle != null )
		{
			final URL url = bundle.getEntry( BIRT_PROJECT_WIZBANNER );
			return ImageDescriptor.createFromURL( url );
		}

		return null;
	}

	/**
	 * Do initialize page properties map
	 * 
	 */
	protected void initializeProperties( )
	{
		WebArtifactUtil.setContextParamValue( properties,
				BIRT_RESOURCE_FOLDER_SETTING, txtResourceFolder.getText( ) );
		WebArtifactUtil.setContextParamValue( properties,
				BIRT_DOCUMENT_FOLDER_SETTING, txtDocumentFolder.getText( ) );
		WebArtifactUtil.setContextParamValue( properties,
				BIRT_DOCUMENT_ACCESSONLY_SETTING, new String( BLANK_STRING
						+ btAccessOnly.getSelection( ) ) );
		WebArtifactUtil.setContextParamValue( properties,
				BIRT_IMAGE_FOLDER_SETTING, txtImageFolder.getText( ) );
		WebArtifactUtil.setContextParamValue( properties,
				BIRT_SCRIPTLIB_FOLDER_SETTING, txtScriptlibFolder.getText( ) );
		WebArtifactUtil.setContextParamValue( properties,
				BIRT_LOG_FOLDER_SETTING, txtLogFolder.getText( ) );
		WebArtifactUtil.setContextParamValue( properties,
				BIRT_OVERWRITE_DOCUMENT_SETTING, new String( BLANK_STRING
						+ btOverwrite.getSelection( ) ) );
		WebArtifactUtil.setContextParamValue( properties,
				BIRT_MAX_ROWS_SETTING, DataUtil.getMaxRows( txtMaxRows
						.getText( ) ) );
		WebArtifactUtil.setContextParamValue( properties,
				BIRT_LOG_LEVEL_SETTING, cbLogLevel.getText( ) );
	}

	/**
	 * @return the properties
	 */
	public Map getProperties( )
	{
		return properties;
	}
}
