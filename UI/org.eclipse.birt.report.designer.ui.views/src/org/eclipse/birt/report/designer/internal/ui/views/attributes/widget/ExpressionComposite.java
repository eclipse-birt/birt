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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleTextAdapter;
import org.eclipse.swt.accessibility.AccessibleTextEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * ExpressionComposite contains a Text and a Button control for presenting an
 * Expression builder UI.
 */
public class ExpressionComposite extends Composite
{

	protected Button button;

	protected Text text;

	/**
	 * @param parent
	 *            A widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            The style of widget to construct
	 */
	public ExpressionComposite( Composite parent, boolean isFormStyle )
	{
		super( parent, SWT.NONE );
		GridLayout layout = new GridLayout( 2, false );
		layout.marginHeight = 2;
		layout.marginWidth = 2;
		layout.verticalSpacing = 2;
		layout.horizontalSpacing = 3;
		setLayout( layout );
		if ( isFormStyle )
			text = FormWidgetFactory.getInstance( ).createText( this,
					"",
					SWT.READ_ONLY | SWT.SINGLE );
		else
			text = new Text( this, SWT.READ_ONLY | SWT.SINGLE );
		GridData data = new GridData( );
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		text.setLayoutData( data );

		button = FormWidgetFactory.getInstance( ).createButton( this,
				SWT.FLAT,
				isFormStyle );
		data = new GridData( );
		button.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				ExpressionBuilder dialog = new ExpressionBuilder( button.getShell( ),
						text.getText( ) );
				dialog.setExpressionProvier( provider );

				if ( dialog.open( ) == Dialog.OK )
				{
					String newValue = dialog.getResult( );
					if ( !text.getText( ).equals( newValue ) )
					{
						text.setText( newValue );
						notifyListeners( SWT.Modify, null );
					}
				}
			}
		} );
		button.setLayoutData( data );
		button.setText( "..." );//$NON-NLS-1$
		button.setToolTipText( Messages.getString( "ExpressionBuilder.toolTipText.Button" ) );
		initAccessible( );
	}

	void initAccessible( )
	{

		button.getAccessible( )
				.addAccessibleListener( new AccessibleAdapter( ) {

					public void getHelp( AccessibleEvent e )
					{
						e.result = button.getToolTipText( );
					}
				} );

		text.getAccessible( )
				.addAccessibleControlListener( new AccessibleControlAdapter( ) {

					public void getRole( AccessibleControlEvent e )
					{
						e.detail = text.getEditable( ) ? ACC.ROLE_TEXT
								: ACC.ROLE_LABEL;
					}
				} );

		getAccessible( ).addAccessibleTextListener( new AccessibleTextAdapter( ) {

			public void getCaretOffset( AccessibleTextEvent e )
			{
				e.offset = text.getCaretPosition( );
			}

			public void getSelectionRange( AccessibleTextEvent e )
			{
				Point sel = text.getSelection( );
				e.offset = sel.x;
				e.length = sel.y - sel.x;
			}
		} );

		getAccessible( ).addAccessibleControlListener( new AccessibleControlAdapter( ) {

			public void getChildAtPoint( AccessibleControlEvent e )
			{
				Point testPoint = toControl( e.x, e.y );
				if ( getBounds( ).contains( testPoint ) )
				{
					e.childID = ACC.CHILDID_SELF;
				}
			}

			public void getLocation( AccessibleControlEvent e )
			{
				Rectangle location = getBounds( );
				Point pt = toDisplay( location.x, location.y );
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}

			public void getChildCount( AccessibleControlEvent e )
			{
				e.detail = 0;
			}

			public void getRole( AccessibleControlEvent e )
			{
				e.detail = ACC.ROLE_TEXT;
			}

			public void getState( AccessibleControlEvent e )
			{
				e.detail = ACC.STATE_NORMAL;
			}

			public void getValue( AccessibleControlEvent e )
			{
				e.result = getText( );
			}
		} );
	}

	/**
	 * Sets value of the Expression.
	 * 
	 * @param string
	 *            the String value.
	 */
	public void setText( String string )
	{
		text.setText( string );
	}

	/**
	 * Gets value of the Expression.
	 * 
	 * @return a String value.
	 */
	public String getText( )
	{
		return text.getText( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	public void setEnabled( boolean enabled )
	{
		text.setEnabled( enabled );
		button.setEnabled( enabled );
		super.setEnabled( enabled );
	}

	private IExpressionProvider provider;

	public void setExpressionProvider( IExpressionProvider provider )
	{
		this.provider = provider;
	}
}