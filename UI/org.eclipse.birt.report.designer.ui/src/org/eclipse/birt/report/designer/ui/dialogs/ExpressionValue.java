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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.data.ui.util.SelectValueFetcher;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.widget.PopupSelectionList;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class ExpressionValue
{
	private Text valueText;
	private Button btnPopup;
	
	public void setVisible (boolean visible) {
		valueText.setVisible( visible );
		btnPopup.setVisible( visible );
	}
	
	public void setEnabled (boolean enabled)
	{
		valueText.setEnabled( enabled );
		btnPopup.setEnabled( enabled );
	}
	
	public boolean getVisible()
	{
		return valueText.getVisible( );
	}
	
	public boolean isVisible()
	{
		return valueText.isVisible( );
	}
	
	public boolean getEnabled()
	{
		return valueText.getEnabled( );
	}
	
	public Text getTextControl( )
	{
		return valueText;
	}

//	public Button getButtonControl( )
//	{
//		return btnPopup;
//	}
	
	public void addTextControlListener( int eventType, Listener listener)
	{
		valueText.addListener( eventType, listener );
	}
	
	public void addButtonControlListener( int eventType, Listener listener)
	{
		btnPopup.addListener( eventType, listener );
	}
	
	public String getText()
	{
		return valueText.getText( );
	}

	public void setText(String string)
	{
		valueText.setText( string );
	}
	
	private Text createText( Composite parent )
	{
		Text txt = new Text( parent, SWT.BORDER );
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.widthHint = 100;
		txt.setLayoutData( gdata );
		return txt;
	}
		
	public ExpressionValue( Composite parent,GridData gd, final Combo expressionText )
	{
		if(gd == null)
		{
			gd = new GridData( GridData.END | GridData.FILL_HORIZONTAL);
			gd.heightHint = 20;
		}
			
		Composite composite = new Composite( parent, SWT.NONE );
		composite.setLayout( new ExpressionLayout( ) );
		composite.setLayoutData( gd );
		valueText = createText( composite );	
		btnPopup = new Button( composite, SWT.ARROW | SWT.DOWN );
	}
	
	public ExpressionValue( Composite parent, final Combo expressionText )
	{
		this( parent, null, expressionText );
	}


	private class ExpressionLayout extends Layout
	{

		public void layout( Composite editor, boolean force )
		{
			Rectangle bounds = editor.getClientArea( );
			Point size = btnPopup.computeSize( SWT.DEFAULT, SWT.DEFAULT, force );
			valueText.setBounds( 0, 0, bounds.width - size.x, bounds.height );
			btnPopup.setBounds( bounds.width - size.x, 0, size.x, bounds.height );
		}

		public Point computeSize( Composite editor, int wHint, int hHint,
				boolean force )
		{
			if ( wHint != SWT.DEFAULT && hHint != SWT.DEFAULT )
				return new Point( wHint, hHint );
			Point contentsSize = valueText.computeSize( SWT.DEFAULT,
					SWT.DEFAULT,
					force );
			Point buttonSize = btnPopup.computeSize( SWT.DEFAULT,
					SWT.DEFAULT,
					force );
			// Just return the button width to ensure the button is not
			// clipped
			// if the label is long.
			// The label will just use whatever extra width there is
			Point result = new Point( buttonSize.x, Math.max( contentsSize.y,
					buttonSize.y ) );
			return result;
		}
	}

}
