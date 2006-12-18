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

package org.eclipse.birt.integration.wtp.ui.internal.dialogs;

import java.util.Map;

import org.eclipse.birt.integration.wtp.ui.internal.resource.BirtWTPMessages;
import org.eclipse.birt.integration.wtp.ui.internal.util.DataUtil;
import org.eclipse.birt.integration.wtp.ui.internal.util.UIUtil;
import org.eclipse.birt.integration.wtp.ui.internal.util.WebArtifactUtil;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.ContextParamBean;
import org.eclipse.birt.integration.wtp.ui.internal.wizards.IBirtWizardConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The dialog to configurate BIRT runtime settings
 * 
 */
public class BirtConfigurationDialog extends Dialog
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
	 * Value for Import Overwrite setting
	 */
	protected Button btClear;
	private boolean isClear;

	/**
	 * default contrustor
	 * 
	 * @param parentShell
	 * @param properties
	 */
	public BirtConfigurationDialog( Shell parentShell, Map properties )
	{
		super( parentShell );
		this.properties = properties;
	}

	/**
	 * default contructor
	 * 
	 * @param parentShell
	 * @param properties
	 */
	protected BirtConfigurationDialog( IShellProvider parentShell,
			Map properties )
	{
		super( parentShell );
		this.properties = properties;
	}

	/**
	 * Create Dialog Content
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		// dialog title
		getShell( ).setText( BirtWTPMessages.BIRTConfigurationDialog_title );

		Composite composite = new Composite( parent, SWT.NULL );
		composite.setFont( parent.getFont( ) );

		initializeDialogUnits( parent );

		composite.setLayout( new GridLayout( ) );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		// create folder configuration group
		Group paths = new Group( composite, SWT.NULL );
		paths.setLayout( new GridLayout( ) );
		paths.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		paths.setText( BirtWTPMessages.BIRTConfiguration_group_paths );
		paths.setEnabled( true );

		// Initialize UI Utility
		UIUtil uit = new UIUtil( properties );

		// create resource folder setting group
		this.txtResourceFolder = uit.createResourceFolderGroup( paths );
		ContextParamBean param = (ContextParamBean) properties
				.get( BIRT_RESOURCE_FOLDER_SETTING );
		String resFolder = null;
		if ( param != null )
		{
			resFolder = param.getValue( );
		}
		// if the old setting isn't null and blank, overwrite it.
		if ( resFolder != null && resFolder.trim( ).length( ) > 0 )
		{
			this.txtResourceFolder.setText( resFolder.trim( ) );
		}

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
		others.setText( BirtWTPMessages.BIRTConfiguration_group_others );
		others.setEnabled( true );

		// create ifaccess only setting group
		this.btAccessOnly = uit.createAccessOnlyGroup( others );

		// create overwrite setting group
		this.btOverwrite = uit.createOverwriteGroup( others );

		// create log level setting group
		this.cbLogLevel = uit.createLogLevelGroup( others );

		// create max rows setting group
		this.txtMaxRows = uit.createMaxRowsGroup( others );

		// create import clear setting group
		this.btClear = uit.createImportClearSetting( composite );
		this.isClear = this.btClear.getSelection( );

		// initialize page properties map
		initializeProperties( );

		return composite;
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
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		this.isClear = this.btClear.getSelection( );
		super.okPressed( );
	}

	/**
	 * @return the isClear
	 */
	public boolean isClear( )
	{
		return isClear;
	}
}
