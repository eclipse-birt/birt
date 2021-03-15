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

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * When tries to delete the data source and data set, if other element
 * references them, this dialog will show up. Click the node and position the
 * node in editor to choose the node to be deleted.
 * 
 *  
 */
public class DeleteWarningDialog extends BaseDialog
{

	private List refrenceList = null;

	private String preString = ""; //$NON-NLS-1$

	private String sufString = ""; //$NON-NLS-1$

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            the parent shell
	 * @param title
	 *            the title of the dialog
	 * @param refrenceList
	 *            the list of references
	 */
	public DeleteWarningDialog( Shell parent, String title, List refrenceList )
	{
		super( parent, title );
		setRefereceList( refrenceList );
	}

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            the parent shell
	 * @param title
	 *            the title of the dialog
	 */
	public DeleteWarningDialog( Shell parent, String title )
	{
		super( parent, title );
	}

	/**
	 * Creates the dialog area.
	 * 
	 * @param parent
	 *            the parent
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );

		new Label( composite, SWT.NONE ).setText( preString );
		Tree tree = new Tree( composite, SWT.NONE );
		tree.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		for ( Iterator itor = refrenceList.iterator( ); itor.hasNext( ); )
		{
			Object reference = itor.next( );
			TreeItem item = new TreeItem( tree, SWT.NONE );
			item.setText( DEUtil.getDisplayLabel( reference ) );
			item.setImage( ReportPlatformUIImages.getImage( reference ) );
		}
		new Label( composite, SWT.NONE ).setText( sufString );
		
		UIUtil.bindHelp( parent,IHelpContextIds.DELETE_WARNING_DIALOG_ID ); 
 
		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog#initDialog()
	 */
	protected boolean initDialog( )
	{
		getButton( IDialogConstants.OK_ID ).setText( IDialogConstants.YES_LABEL );
		Button no = getButton( IDialogConstants.CANCEL_ID );
		no.setText( IDialogConstants.NO_LABEL );
		/**
		 * Set cancel button on focus when initial.
		 */
		no.setFocus();
		getShell( ).setDefaultButton( no );
		return true;
	}

	/**
	 * Gets the prefix String
	 * 
	 * @return the prefix string
	 */
	public String getPreString( )
	{
		return preString;
	}

	/**
	 * gets the suffix string
	 * 
	 * @return the suffix string
	 */
	public String getSufString( )
	{
		return sufString;
	}

	/**
	 * Sets the prefix string
	 * 
	 * @param str
	 *            the string
	 */
	public void setPreString( String str )
	{
		this.preString = str;
	}

	/**
	 * Sets suffix string
	 * 
	 * @param str
	 *            the string
	 */
	public void setSufString( String str )
	{
		this.sufString = str;
	}

	/**
	 * Gets the reference list.
	 * 
	 * @return Returns the the reference list
	 */
	public List getRefereceList( )
	{
		return refrenceList;
	}

	/**
	 * Sets the reference list.
	 * 
	 * @param showMap
	 *            the reference list.The list shouldn't be not null.
	 */
	public void setRefereceList( List newList )
	{
		Assert.isNotNull( newList );
		refrenceList = newList;
	}
}