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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;

/**
 * Wizard for Selecting Css styles from CSS file.
 */

public class SelectCssStyleWizard extends Wizard
{

	private static final String WIZARD_PAGE_DESCRIPTION = Messages.getString( "SelectCssStyleWizard.wizardPage.description" ); //$NON-NLS-1$

	private static final String WIZARD_PAGE_TITLE = Messages.getString( "SelectCssStyleWizard.wizardPage.title" ); //$NON-NLS-1$

	private static final String WIZARD_PAGE_NAME = Messages.getString( "SelectCssStyleWizard.wizardPage.name" ); //$NON-NLS-1$

	private static final String WIZARD_TITLE = Messages.getString( "SelectCssStyleWizard.wizard.title" ); //$NON-NLS-1$

	private WizardSelectCssStylePage stylePage;

	public SelectCssStyleWizard( )
	{
		setWindowTitle( WIZARD_TITLE );
	}

	public Image getDefaultPageImage( )
	{
		// return ReportPlatformUIImages.getImage(
		// IReportGraphicConstants.ICON_ELEMENT_STYLE );
		return super.getDefaultPageImage( );
	}

	public void addPages( )
	{
		stylePage = new WizardSelectCssStylePage( WIZARD_PAGE_NAME );

		stylePage.setTitle( WIZARD_PAGE_TITLE );

		stylePage.setDescription( WIZARD_PAGE_DESCRIPTION );

		addPage( stylePage );
	}

	public boolean canFinish( )
	{
		return stylePage.isPageComplete( );
	}

	public boolean performFinish( )
	{
		CssStyleSheetHandle cssHandle = stylePage.getCssHandle( );
		if ( cssHandle != null )
		{
			List styleList = stylePage.getStyleList( );
			SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.importCssStyles( cssHandle, styleList );
		}
		return true;
	}
}
