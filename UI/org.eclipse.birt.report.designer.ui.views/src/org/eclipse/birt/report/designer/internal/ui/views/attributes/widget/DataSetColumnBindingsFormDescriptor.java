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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.DataSetColumnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Data set binding page.
 */

public class DataSetColumnBindingsFormDescriptor extends FormPropertyDescriptor
{

	private DataSetColumnBindingsFormHandleProvider provider;

	public DataSetColumnBindingsFormDescriptor( )
	{
		super( true );
		super.setStyle( FormPropertyDescriptor.FULL_FUNCTION );
		super.setButtonWithDialog( false );
	}

	public void setDescriptorProvider( IDescriptorProvider provider )
	{
		super.setDescriptorProvider( provider );
		if ( provider instanceof DataSetColumnBindingsFormHandleProvider )
			this.provider = (DataSetColumnBindingsFormHandleProvider) provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage#createControl()
	 */
	public Control createControl( Composite parent )
	{
		Control control = super.createControl( parent );
		provider.setTableViewer( getTableViewer( ) );
		btnUp.setVisible( false );
		btnDown.setVisible( false );
		return control;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage#fullLayout()
	 */
	protected void fullLayout( )
	{
		super.fullLayout( );
		FormData data = new FormData( );
		data.top = new FormAttachment( btnEdit, 0, SWT.BOTTOM );
		data.left = new FormAttachment( btnEdit, 0, SWT.LEFT );
		data.width = Math.max( 60, btnDel.computeSize( SWT.DEFAULT,
				SWT.DEFAULT,
				true ).x );
		btnDel.setLayoutData( data );

		data = new FormData( );
		data.top = new FormAttachment( btnDel, 0, SWT.BOTTOM );
		data.left = new FormAttachment( btnDel, 0, SWT.LEFT );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage#setInput(java.util.List)
	 */

	List groupList;

	public void setInput( Object object )
	{
		super.setInput( object );
		if ( DEUtil.getInputSize( object ) > 0 )
		{
			Object element = DEUtil.getInputFirstElement( object );
			setBindingObject( (ReportElementHandle) element );
		
		}

	}

	private void setBindingObject( ReportElementHandle bindingObject )
	{
		provider.setBindingObject( bindingObject );
	}
}