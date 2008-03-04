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

package org.eclipse.birt.report.designer.internal.ui.views.property.widgets;

import java.util.Arrays;

import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * A cell editor that manages a dimension field.
 */
public class DimensionCellEditor extends CDialogCellEditor
{

	private String[] units;
	private String unitName;
	private Text defaultLabel;
	private int style;
	private int inProcessing = 0;

	/**
	 * Creates a new dialog cell editor parented under the given control.
	 * 
	 * @param parent
	 *            the parent control
	 * @param unitNames
	 *            the name list
	 */
	public DimensionCellEditor( Composite parent, String[] unitNames )
	{
		super( parent );
		this.units = unitNames;
	}

	/**
	 * Creates a new dialog cell editor parented under the given control.
	 * 
	 * @param parent
	 *            the parent control
	 * @param unitNames
	 *            the name list
	 * @param style
	 *            the style bits
	 */
	public DimensionCellEditor( Composite parent, String[] unitNames, int style )
	{
		super( parent, style );
		this.units = unitNames;
		this.style = style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	protected Object openDialogBox( Control cellEditorWindow )
	{
		DimensionBuilderDialog dialog = new DimensionBuilderDialog( cellEditorWindow.getShell( ) );

		DimensionValue value;
		try
		{
			value = DimensionValue.parse( (String) this.getDefaultText( ).getText( ));
		}
		catch ( PropertyValueException e )
		{
			value = null;
		}

		dialog.setUnitNames( units );
		dialog.setUnitData( Arrays.asList( units ).indexOf( unitName ) );

		if ( value != null )
		{
			dialog.setMeasureData( new Double( value.getMeasure( ) ) );
		}

		inProcessing = 1;
		if ( dialog.open( ) == Window.OK )
		{
			deactivate( );
			inProcessing = 0;
			return dialog.getMeasureData( ).toString( ) + dialog.getUnitName( );
		}
		else
		{
			getDefaultText( ).setFocus( );
			getDefaultText( ).selectAll( );
		}
		inProcessing = 0;
		return null;

	}

	/**
	 * Set current units
	 * 
	 * @param units
	 */
	public void setUnits( String units )
	{
		this.unitName = units;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	protected void doSetFocus( )
	{
		getDefaultText( ).setFocus( );
		getDefaultText( ).selectAll( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.property.widgets.CDialogCellEditor#doValueChanged()
	 */
	protected void doValueChanged( )
	{
		if ( doGetValue( ) != defaultLabel.getText( ) )
		{
			markDirty( );
			doSetValue( defaultLabel.getText( ) );
		}
	}

	/**
	 * Creates the controls used to show the value of this cell editor.
	 * <p>
	 * The default implementation of this framework method creates a label
	 * widget, using the same font and background color as the parent control.
	 * </p>
	 * <p>
	 * Subclasses may reimplement. If you reimplement this method, you should
	 * also reimplement <code>updateContents</code>.
	 * </p>
	 * 
	 * @param cell
	 *            the control for this cell editor
	 */
	protected Control createContents( Composite cell )
	{
		defaultLabel = new Text( cell, SWT.LEFT | style );
		defaultLabel.setFont( cell.getFont( ) );
		defaultLabel.setBackground( cell.getBackground( ) );

		defaultLabel.addKeyListener( new KeyAdapter( ) {

			// hook key pressed - see PR 14201
			public void keyPressed( KeyEvent e )
			{
				keyReleaseOccured( e );
			}
		} );

		defaultLabel.addTraverseListener( new TraverseListener( ) {

			public void keyTraversed( TraverseEvent e )
			{
				if ( e.detail == SWT.TRAVERSE_ESCAPE
						|| e.detail == SWT.TRAVERSE_RETURN )
				{
					e.doit = false;
				}
			}
		} );

		defaultLabel.addFocusListener( new FocusAdapter( ) {

			public void focusLost( FocusEvent e )
			{
				DimensionCellEditor.this.focusLost( );
			}
		} );

		return defaultLabel;
	}

	/**
	 * Updates the controls showing the value of this cell editor.
	 * <p>
	 * The default implementation of this framework method just converts the
	 * passed object to a string using <code>toString</code> and sets this as
	 * the text of the label widget.
	 * </p>
	 * <p>
	 * Subclasses may reimplement. If you reimplement this method, you should
	 * also reimplement <code>createContents</code>.
	 * </p>
	 * 
	 * @param value
	 *            the new value of this cell editor
	 */
	protected void updateContents( Object value )
	{
		if ( defaultLabel == null )
			return;

		String text = "";//$NON-NLS-1$
		if ( value != null )
			text = value.toString( );
		defaultLabel.setText( text );
	}

	/**
	 * Returns the default label widget created by <code>createContents</code>.
	 * 
	 * @return the default label widget
	 */
	protected Text getDefaultText( )
	{
		return defaultLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#keyReleaseOccured(org.eclipse.swt.events.KeyEvent)
	 */
	protected void keyReleaseOccured( KeyEvent keyEvent )
	{
		if ( keyEvent.character == '\u001b' )
		{ // Escape character
			fireCancelEditor( );
		}
		else if ( keyEvent.character == '\t' )
		{ // tab key
			applyEditorValueAndDeactivate( );
		}
		else if ( keyEvent.character == '\r' )
		{ // Return key
			applyEditorValueAndDeactivate( );
		}
	}

	/**
	 * 
	 */
	private void applyEditorValueAndDeactivate( )
	{
		inProcessing = 1;
		doValueChanged( );
		fireApplyEditorValue( );
		deactivate( );
		inProcessing = 0;
	}

	/**
	 * Processes a focus lost event that occurred in this cell editor.
	 * <p>
	 * The default implementation of this framework method applies the current
	 * value and deactivates the cell editor. Subclasses should call this method
	 * at appropriate times. Subclasses may also extend or reimplement.
	 * </p>
	 */
	protected void focusLost( )
	{
		if ( inProcessing == 1 )
			return;
		else
		{
			//if click button, ignore focuslost event.
			Rectangle rect = getButton( ).getBounds( );
			Point location = getButton( ).toDisplay( 0, 0 );
			rect.x = location.x;
			rect.y = location.y;
			Point cursorLocation = getButton( ).getDisplay( ).getCursorLocation( );
			if ( rect.contains( cursorLocation ) )
				return;
		}
		super.focusLost( );
	}
}