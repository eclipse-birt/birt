/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Actuate Corporation
 * 
 */
public class FormatSpecifierDialog extends TrayDialog
{

	private transient FormatSpecifierComposite editor = null;

	private transient FormatSpecifier formatspecifier = null;

	/**
	 * 
	 * @param shellParent
	 *            dialog shell
	 * @param formatspecifier
	 *            format model
	 * @param sTitle
	 *            this argument is obsolete
	 */
	public FormatSpecifierDialog( Shell shellParent,
			FormatSpecifier formatspecifier, String sTitle )
	{
		super( shellParent );
		if ( formatspecifier != null )
		{
			this.formatspecifier = (FormatSpecifier) EcoreUtil.copy( formatspecifier );
		}
	}

	protected void setShellStyle( int newShellStyle )
	{
		super.setShellStyle( newShellStyle
				| SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL );
	}

	protected Control createContents( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.DIALOG_EDIT_FORMAT );
		getShell( ).setText( Messages.getString( "FormatSpecifierDialog.Title.EditFormat" ) ); //$NON-NLS-1$
		UIHelper.centerOnScreen( getShell( ) );
		return super.createContents( parent );
	}

	protected Control createDialogArea( Composite parent )
	{
		editor = new FormatSpecifierComposite( parent,
				SWT.NONE,
				formatspecifier );
		GridData gdEditor = new GridData( GridData.FILL_BOTH );
		editor.setLayoutData( gdEditor );

		return editor;
	}

	public FormatSpecifier getFormatSpecifier( )
	{
		return formatspecifier;
	}

	protected void okPressed( )
	{
		formatspecifier = editor.getFormatSpecifier( );
		super.okPressed( );
	}

}