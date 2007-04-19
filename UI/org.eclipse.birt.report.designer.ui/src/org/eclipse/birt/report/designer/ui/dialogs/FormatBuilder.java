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

package org.eclipse.birt.report.designer.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatDateTimePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatNumberPage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatStringPage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatPage;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * The builder used to generate a format string
 * <dt><b>Styles: </b></dt>
 * <dd>STRING</dd>
 * <dd>NUMBER</dd>
 * <dd>DATETIME</dd>
 */

public class FormatBuilder extends BaseDialog
{

	/** Style Constants */
	/** String format constant */
	public static final int STRING = 1;
	/** Number format constant */
	public static final int NUMBER = 2;
	/** DateTime format constant */
	public static final int DATETIME = 3;
	/** DateTime format constant */
	public static final int DATE = 4;
	/** DateTime format constant */
	public static final int TIME = 5;

	private static final String DLG_TITLE = Messages.getString( "FormatBuilder.Title" ); //$NON-NLS-1$
	private IFormatPage page;
	private String formatCategory = null;
	private String formatPattern = null;
	private String previewText = null;

	private int type;

	/**
	 * Constructs a new instance of the format builder
	 * 
	 * @param style
	 *            the style of the format builder
	 * 
	 */
	public FormatBuilder( int type )
	{
		super( DLG_TITLE );
		Assert.isLegal( type == STRING
				|| type == NUMBER
				|| type == DATETIME
				|| type == DATE
				|| type == TIME );
		this.type = type;
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );
		switch ( type )
		{
			case STRING :
				page = new FormatStringPage( composite,
						SWT.NONE,
						IFormatPage.PAGE_ALIGN_VIRTICAL );
				break;
			case NUMBER :
				page = new FormatNumberPage( composite,
						SWT.NONE,
						IFormatPage.PAGE_ALIGN_VIRTICAL );
				break;
			case DATETIME :
			case DATE:
			case TIME:
				page = new FormatDateTimePage( composite,
						type,
						SWT.NONE,
						IFormatPage.PAGE_ALIGN_VIRTICAL );
				break;
		}
		( (Composite) page ).setLayoutData( new GridData( GridData.FILL_BOTH ) );

		UIUtil.bindHelp( composite, IHelpContextIds.FORMAT_BUILDER_ID );
		return composite;
	}

	/*
	 * Set preview text
	 */
	public void setPreviewText( String previewText )
	{
		this.previewText = previewText;
	}

	/*
	 * Set format categrory and patten
	 */
	public void setInputFormat( String formatCategroy, String formatPattern )
	{
		Assert.isLegal( !StringUtil.isBlank( formatCategroy ) );
		this.formatCategory = formatCategroy;
		this.formatPattern = formatPattern;
	}

	protected boolean initDialog( )
	{
		page.setInput( formatCategory, formatPattern );
		page.setPreviewText( previewText );
		return true;
	}

	protected void okPressed( )
	{
		if ( page.isFormatModified( ) )
		{

			setResult( new String[]{
					page.getCategory( ), page.getPattern( )
			} );
			super.okPressed( );
		}
		else
		{
			cancelPressed( );
		}
	}
}
