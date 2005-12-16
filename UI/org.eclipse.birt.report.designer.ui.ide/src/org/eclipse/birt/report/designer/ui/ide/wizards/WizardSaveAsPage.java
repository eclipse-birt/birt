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

package org.eclipse.birt.report.designer.ui.ide.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.misc.ResourceAndContainerGroup;

/**
 * 
 */

public class WizardSaveAsPage extends WizardPage
{

	private ResourceAndContainerGroup resourceGroup;
	private IResource originalFile;
	private String originalName;

	public WizardSaveAsPage( String pageName )
	{
		super( pageName );
		// TODO Auto-generated constructor stub
	}

	public void createControl( Composite parent )
	{

		// create a composite with standard margins and spacing
		Composite composite = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginHeight = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_MARGIN );
		layout.marginWidth = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_MARGIN );
		layout.verticalSpacing = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_SPACING );
		layout.horizontalSpacing = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_SPACING );
		composite.setLayout( layout );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		composite.setFont( parent.getFont( ) );

		Listener listener = new Listener( ) {

			public void handleEvent( Event event )
			{
				validatePage( );
				getContainer().updateButtons();
			}
		};

		resourceGroup = new ResourceAndContainerGroup( composite,
				listener,
				IDEWorkbenchMessages.SaveAsDialog_fileLabel,
				IDEWorkbenchMessages.SaveAsDialog_file, false, 200 );
		resourceGroup.setAllowExistingResources( true );

		setControl( composite );
		
	}

	/**
	 * Initializes the controls of this dialog.
	 */
	private void initializeControls( )
	{
		if ( originalFile != null )
		{
			resourceGroup.setContainerFullPath( originalFile.getParent( )
					.getFullPath( ) );
			resourceGroup.setResource( originalFile.getName( ) );
		}
		else if ( originalName != null )
			resourceGroup.setResource( originalName );
	}

	/**
	 * Sets the original file to use.
	 * 
	 * @param originalFile
	 *            the original file
	 */
	public void setOriginalFile( IFile originalFile )
	{
		this.originalFile = originalFile;
	}

	/**
	 * Set the original file name to use. Used instead of
	 * <code>setOriginalFile</code> when the original resource is not an
	 * IFile. Must be called before <code>create</code>.
	 * 
	 * @param originalName
	 *            default file name
	 */
	public void setOriginalName( String originalName )
	{
		this.originalName = originalName;
	}

	/**
	 * Returns whether this page's visual components all contain valid values.
	 * 
	 * @return <code>true</code> if valid, and <code>false</code> otherwise
	 */
	public boolean validatePage( )
	{
		setErrorMessage( null );
		if ( !resourceGroup.areAllValuesValid( ) )
		{
			if ( !resourceGroup.getResource( ).equals( "" ) ) // if blank name
				// then fail
				// silently//$NON-NLS-1$
				setErrorMessage( resourceGroup.getProblemMessage( ) );
			return false;
		}
		return true;
	}

	public IPath getResult( )
	{

		IPath path = resourceGroup.getContainerFullPath( )
				.append( resourceGroup.getResource( ) );

		// If the user does not supply a file extension and if the save
		// as dialog was provided a default file name append the extension
		// of the default filename to the new name
		if ( path.getFileExtension( ) == null )
		{
			if ( originalFile != null
					&& originalFile.getFileExtension( ) != null )
				path = path.addFileExtension( originalFile.getFileExtension( ) );
			else if ( originalName != null )
			{
				int pos = originalName.lastIndexOf( '.' );
				if ( ++pos > 0 && pos < originalName.length( ) )
					path = path.addFileExtension( originalName.substring( pos ) );
			}
		}

		// If the path already exists then confirm overwrite.
		IFile file = ResourcesPlugin.getWorkspace( ).getRoot( ).getFile( path );

		if ( file.exists( ) )
		{
			String[] buttons = new String[]{
					IDialogConstants.YES_LABEL,
					IDialogConstants.NO_LABEL,
					IDialogConstants.CANCEL_LABEL
			};
			String question = NLS.bind( IDEWorkbenchMessages.SaveAsDialog_overwriteQuestion,
					path.toOSString( ) );
			MessageDialog d = new MessageDialog( getShell( ),
					IDEWorkbenchMessages.Question,
					null,
					question,
					MessageDialog.QUESTION,
					buttons,
					0 );
			int overwrite = d.open( );
			switch ( overwrite )
			{
				case 0 : // Yes
					break;
				case 1 : // No
					return null;
				case 2 : // Cancel
				default :
					return null;
			}
		}

		return path;
	}
}
