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


import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
 * 
 */

public class CssErrDialog extends TitleAreaDialog
{

	private List errorList;
	private int level;
	public static final int FATAL_ERROR = 1;
	public static final int ERROR = 2;
	public static final int WARNING = 3;
	
	
	public CssErrDialog( Shell parentShell )
	{
		super( parentShell );		
	}

	public CssErrDialog( Shell parentShell , List list, int level)
	{
		this( parentShell );		
		this.errorList = list;
		this.level = level;	
	}
	
	
	protected Control createContents( Composite parent )
	{
		Control control = super.createContents( parent );
		setMessage( Messages.getString("CssErrDialog.AreaMessage") );	
		setTitle( Messages.getString("CssErrDialog.AreaTitle"));
		getShell( ).setText( Messages.getString("CssErrDialog.shellTitle.ImportCssStyleMssageTitle"));

		return control;
	}
	
	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );
		createComposite( composite );
		return composite;

	}
	
	private Composite createComposite(Composite parent)
	{	
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		composite.setData( gd );
		GridLayout layout = new GridLayout(2,false);
		composite.setLayout( layout );
		
		new Label(composite,  SWT.NONE).setText( Messages.getString( "CssErrDialog.Serverity" ));
		Label messageLine = new Label(composite, SWT.NONE);
		Label messageLabel= new Label(composite, SWT.NONE);
		messageLabel.setText( Messages.getString( "CssErrDialog.Message" ));
		messageLabel.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		Text messageText = new Text(composite, SWT.MULTI
				| SWT.WRAP
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL);
		
		gd = new GridData();
		gd.heightHint = 200;
		gd.widthHint = 340;
		messageText.setLayoutData( gd );	
		messageText.setEditable( false );
		switch(level)
		{
			case FATAL_ERROR:
				messageLine.setText(Messages.getString( "CssErrDialog.FatalError" ));
				break;
			case ERROR:
				messageLine.setText(Messages.getString( "CssErrDialog.Error" ));
				break;
			case WARNING:
				messageLine.setText(Messages.getString( "CssErrDialog.Warning" ));
				break;
			default:
		}
		
		Iterator errorIter = errorList.listIterator( );
		while ( errorIter.hasNext( ) )
		{
			messageText.append( errorIter.next( ).toString( ) ) ;
			messageText.append( "\n" );
		}	

		
		return parent;
	}
}
