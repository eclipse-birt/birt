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

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * A WizardDialog witch can return a select path.
 */

public class SaveReportAsWizardDialog extends BaseWizardDialog
{

	private IPath saveAsPath;

	public SaveReportAsWizardDialog( Shell parentShell, IWizard newWizard )
	{
		super( parentShell, newWizard );
		setHelpAvailable( false );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardDialog#finishPressed()
	 */
	protected void finishPressed( )
	{
		super.finishPressed( );
		IWizardPage page = getCurrentPage( );
		IWizard wizard = page.getWizard( );
		this.saveAsPath = ( (SaveReportAsWizard) wizard ).getSaveAsPath( );
	}

	/**
	 * The saving path of report design
	 * @return path
	 */
	public IPath getResult( )
	{
		return this.saveAsPath;
	}

}
