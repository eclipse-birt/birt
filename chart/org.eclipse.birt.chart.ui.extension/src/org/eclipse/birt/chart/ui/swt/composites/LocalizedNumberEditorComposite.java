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

package org.eclipse.birt.chart.ui.swt.composites;

import java.text.ParseException;
import java.util.Vector;

import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.NumberFormat;

/**
 * LocalizedNumberEditorComposite
 */
public class LocalizedNumberEditorComposite extends Composite implements
		ModifyListener,
		FocusListener,
		KeyListener
{

	public static final int TEXT_MODIFIED = TextEditorComposite.TEXT_MODIFIED;

	public static final int TEXT_FRACTION_CONVERTED = TextEditorComposite.TEXT_FRACTION_CONVERTED;

	private transient Text txtValue;

	private transient Vector vModifyListeners;

	private transient Vector vFractionListeners;

	private transient double dValue;

	private transient boolean bTextModified = false;

	private transient boolean bValueIsSet = false;

	private transient boolean bEnabled = true;

	private transient int iStyle = SWT.NONE;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 * @param iStyle
	 */
	public LocalizedNumberEditorComposite( Composite parent, int iStyle )
	{
		super( parent, SWT.NONE );
		this.iStyle = iStyle;
		vModifyListeners = new Vector( );
		vFractionListeners = new Vector( );
		this.setLayout( new FillLayout( ) );

		placeComponents( );
	}

	private void placeComponents( )
	{
		txtValue = new Text( this, iStyle );
		txtValue.setToolTipText( Messages.getString( "TextEditorComposite.Tooltip.EnterDecimalOrFractionValue" ) ); //$NON-NLS-1$
		txtValue.addModifyListener( this );
		txtValue.addFocusListener( this );
		txtValue.addKeyListener( this );
		//		
		// txtValue.setBackground( Display.getDefault( ).getSystemColor(
		// SWT.COLOR_CYAN ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	public void setEnabled( boolean bState )
	{
		bEnabled = bState;
		txtValue.setEnabled( bState );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Control#isEnabled()
	 */
	public boolean isEnabled( )
	{
		return bEnabled;
	}

	public boolean isSetValue( )
	{
		return bValueIsSet;
	}

	public void setValue( double value )
	{
		bValueIsSet = true;
		dValue = value;
		txtValue.setText( NumberFormat.getInstance( ).format( value ) );
	}

	public double getValue( )
	{
		return dValue;
	}

	public void setToolTipText( String string )
	{
		txtValue.setToolTipText( string );
	}

	public void addModifyListener( ModifyListener listener )
	{
		vModifyListeners.add( listener );
	}

	public void addFractionListener( Listener listener )
	{
		vFractionListeners.add( listener );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText( ModifyEvent e )
	{
		this.bTextModified = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.FocusListener#focusGained(org.eclipse.swt.events.FocusEvent)
	 */
	public void focusGained( FocusEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.FocusEvent)
	 */
	public void focusLost( FocusEvent e )
	{
		if ( bTextModified )
		{
			bTextModified = false;
			fireEvent( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyPressed( KeyEvent e )
	{
		if ( e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR )
		{
			if ( bTextModified )
			{
				bTextModified = false;
				fireEvent( );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyReleased( KeyEvent e )
	{
	}

	private void handleFormatError( String value )
	{
		MessageBox mbox = new MessageBox( getShell( ), SWT.ICON_WARNING
				| SWT.OK );
		mbox.setText( Messages.getString( "LocalizedNumberEditorComposite.error.Title" ) ); //$NON-NLS-1$
		mbox.setMessage( MessageFormat.format( Messages.getString( "LocalizedNumberEditorComposite.error.Message" ), //$NON-NLS-1$
				new Object[]{
					value
				} ) );

		mbox.open( );
	}

	private void fireEvent( )
	{
		boolean isFractionConverted = false;

		String sText = txtValue.getText( );

		if ( sText == null || sText.trim( ).length( ) == 0 )
		{
			bValueIsSet = false;
			dValue = 0;
		}
		else
		{
			int iDelimiter = sText.indexOf( '/' );
			if ( iDelimiter < 0 )
			{
				iDelimiter = sText.indexOf( ':' );
			}
			if ( iDelimiter > 0 )
			{
				// Handle the fraction conversion
				isFractionConverted = true;
				String numerator = sText.substring( 0, iDelimiter );
				String denominator = sText.substring( iDelimiter + 1 );
				try
				{
					Number nume = NumberFormat.getInstance( ).parse( numerator );
					Number deno = NumberFormat.getInstance( )
							.parse( denominator );
					dValue = nume.doubleValue( ) / deno.doubleValue( );
					bValueIsSet = true;
					sText = NumberFormat.getInstance( ).format( dValue );
					this.txtValue.setText( sText );
				}
				catch ( ParseException e )
				{
					handleFormatError( sText );
				}
			}
			else
			{
				try
				{
					Number num = NumberFormat.getInstance( ).parse( sText );
					dValue = num.doubleValue( );
					bValueIsSet = true;
					sText = NumberFormat.getInstance( ).format( dValue );
				}
				catch ( ParseException e )
				{
					handleFormatError( sText );
				}
			}
		}

		for ( int i = 0; i < vModifyListeners.size( ); i++ )
		{
			Event e = new Event( );
			e.data = sText;
			e.widget = this;
			e.type = TEXT_MODIFIED;
			( (ModifyListener) vModifyListeners.get( i ) ).modifyText( new ModifyEvent( e ) );
		}

		if ( isFractionConverted )
		{
			for ( int i = 0; i < vFractionListeners.size( ); i++ )
			{
				Event e = new Event( );
				e.data = sText;
				e.widget = this;
				e.type = TEXT_FRACTION_CONVERTED;
				( (Listener) vFractionListeners.get( i ) ).handleEvent( e );
			}
		}
	}

}
