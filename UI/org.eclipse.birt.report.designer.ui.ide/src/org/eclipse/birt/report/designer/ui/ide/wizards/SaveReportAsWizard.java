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

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.wizards.WizardReportSettingPage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.Wizard;

/**
 * a Save as wizard with a page set basic report properties.
 */

public class SaveReportAsWizard extends Wizard
{

	private ReportDesignHandle model;
	private IFile orginalFile;
	private WizardSaveAsPage saveAsPage;
	private WizardReportSettingPage settingPage;
	private IPath saveAsPath;

	public SaveReportAsWizard( ReportDesignHandle model, IFile orginalFile )
	{
		setWindowTitle( Messages.getString( "SaveReportAsWizard.SaveAsPageTitle" ) ); //$NON-NLS-1$
		this.model = model;
		this.orginalFile = orginalFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages( )
	{

		saveAsPage = new WizardSaveAsPage( "WizardSaveAsPage" ); //$NON-NLS-1$
		saveAsPage.setOriginalFile( orginalFile );
		saveAsPage.setTitle( Messages.getString( "SaveReportAsWizard.SaveAsPageTitle" ) ); //$NON-NLS-1$
		saveAsPage.setDescription( Messages.getString( "SaveReportAsWizard.SaveAsPageMessage" ) ); //$NON-NLS-1$
		// saveAsPage.setImageDescriptor(
		// IDEInternalWorkbenchImages.getImageDescriptor(
		// IDEInternalWorkbenchImages.IMG_DLGBAN_SAVEAS_DLG ) );
		addPage( saveAsPage );

		settingPage = new WizardReportSettingPage( model );
		settingPage.setTitle( Messages.getFormattedString( "SaveReportAsWizard.SettingPage.title", new Object[]{Messages.getString( "NewReportWizard.wizardPageTitle.report" )} ) ); //$NON-NLS-1$
		settingPage.setMessage( Messages.getString( "SaveReportAsWizard.SettingPage.message" ) ); //$NON-NLS-1$

		addPage( settingPage );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	public boolean canFinish( )
	{
		return saveAsPage.validatePage( );
	}

	public boolean performFinish( )
	{
		saveAsPath = saveAsPage.getResult( );

		if ( saveAsPath != null )
		{
			try
			{
				model.setDisplayName( settingPage.getDisplayName( ) );
				model.setDescription( settingPage.getDescription( ) );
				model.setIconFile( settingPage.getPreviewImagePath( ) );
				model.setFileName( saveAsPath.toOSString( ) );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}
		}

		return true;
	}

	public IPath getSaveAsPath( )
	{
		return this.saveAsPath;
	}
}
