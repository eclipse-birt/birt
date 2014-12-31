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

import java.util.List;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;

/**
 * This class set the Time option ,format,TimeZone ,date and time
 */
public class TimeOptionDialog extends SelectionStatusDialog
		implements
			IPropertyChangeListener
{

	private SpinnerYear year = null;

	private Combo month = null;

	private SpinnerTime time = null;

	private SpinnerTable day = null;

	private Combo combo = null;

	private Combo zoneCombo = null;

	private String format = TimeFormat.DATE_TIME;

	private Calendar cale = Calendar.getInstance( TimeZone.getDefault( ) );

	private String timeZone = ""; //$NON-NLS-1$

	private static final String LABEL_FORMAT = Messages.getString( "TimeOptionDialog.Label.Format" ); //$NON-NLS-1$

	private static final String LABEL_TIMEZONE = Messages.getString( "TimeOptionDialog.Label.TimeZone" ); //$NON-NLS-1$

	/**
	 * Creates an instance of a <code>TimeOptionDialog</code>.
	 */
	public TimeOptionDialog( Shell parentShell )
	{
		super( parentShell );
		setHelpAvailable( false );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#computeResult()
	 */
	protected void computeResult( )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell( Shell shell )
	{

		super.configureShell( shell );

		initComponent( shell );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#create()
	 */
	public void create( )
	{
		super.create( );
		initActions( );
		updateComponent( );
		//
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createButtonBar( Composite parent )
	{
		return super.createButtonBar( parent );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#getFirstResult()
	 */
	public Object getFirstResult( )
	{
		return super.getFirstResult( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		super.okPressed( );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#setImage(org.eclipse.swt.graphics.Image)
	 */
	public void setImage( Image image )
	{
		super.setImage( image );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#setResult(int,
	 *      java.lang.Object)
	 */
	protected void setResult( int position, Object element )
	{
		super.setResult( position, element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#setStatusLineAboveButtons(boolean)
	 */
	public void setStatusLineAboveButtons( boolean aboveButtons )
	{
		super.setStatusLineAboveButtons( aboveButtons );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#updateButtonsEnableState(org.eclipse.core.runtime.IStatus)
	 */
	protected void updateButtonsEnableState( IStatus status )
	{
		super.updateButtonsEnableState( status );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#updateStatus(org.eclipse.core.runtime.IStatus)
	 */
	protected void updateStatus( IStatus status )
	{
		super.updateStatus( status );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createButton(org.eclipse.swt.widgets.Composite,
	 *      int, java.lang.String, boolean)
	 */
	protected org.eclipse.swt.widgets.Button createButton( Composite parent,
			int id, String label, boolean defaultButton )
	{
		return super.createButton( parent, id, label, defaultButton );
	}

	private void initComponent( Shell shell )
	{
		//shell.setSize( 500, 220 );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		parent.setBackground( ColorConstants.blue );
		Composite compo = (Composite) super.createDialogArea( parent );
		GridLayout layout = (GridLayout) compo.getLayout( );

		layout.numColumns = 3;
		Composite leftLabel = new Composite( compo, SWT.BORDER );

		Composite rightLabel = new Composite( compo, SWT.BORDER );

		createRightComponent( rightLabel );
		createLeftComponent( leftLabel );
		return compo;
	}

	private void createLeftComponent( Composite composite )
	{
		year = new SpinnerYear( composite, SWT.NONE );
		month = new MonthCombo( composite, SWT.READ_ONLY );
		month.setFont( FontManager.getFont( "Dialog", 12, SWT.BOLD ) ); //$NON-NLS-1$

		for ( int i = 1; i <= 12; i++ )
		{
			month.add( String.valueOf( i ) );
		}

		month.setSize( 50, 20 );
		month.select( 0 );
		time = new SpinnerTime( composite, SWT.NONE );
		day = new SpinnerTable( composite, SWT.NONE );

		//set size
		month.setLocation( year.getSize( ).x + 2, 0 );
		time.setLocation( year.getSize( ).x + month.getSize( ).x + 4, 0 );
		day.setLocation( 0, year.getSize( ).y + 1 );

		day.pack( );
		composite.pack( );
	}

	private void createRightComponent( Composite composite )
	{
		Label formatLabel = new Label( composite, SWT.CENTER | SWT.SINGLE );
		formatLabel.setText( LABEL_FORMAT );
		formatLabel.setBounds( 0, 8, 60, 30 );

		combo = new Combo( composite, SWT.READ_ONLY | SWT.DROP_DOWN );
		List list = TimeFormat.getDefaultFormat( ).getSupportList( );
		String[] items = new String[list.size( )];
		list.toArray( items );
		combo.setBounds( 60, 2, 150, 30 );
		combo.setVisibleItemCount( 30 );
		combo.setItems( items );
		combo.select( 0 );

		Label zoneLabel = new Label( composite, SWT.CENTER );
		zoneLabel.setText( LABEL_TIMEZONE );
		zoneLabel.setBounds( 0, 108, 60, 30 );

		zoneCombo = new Combo( composite, SWT.READ_ONLY | SWT.SINGLE );
		zoneCombo.setVisibleItemCount( 30 );
		items = TimeZone.getAvailableIDs( );
		zoneCombo.setBounds( 60, 102, 150, 30 );
		zoneCombo.setItems( items );
		zoneCombo.select( 0 );

	}

	/**
	 * Sets the information
	 * 
	 * @param dialogInfo
	 * @return true
	 */
	public boolean setInfo( DialogInfo dialogInfo )
	{

		if ( !( dialogInfo instanceof TimeDialogInfo ) )
		{
			return false;
		}
		TimeDialogInfo timeInfo = (TimeDialogInfo) dialogInfo;

		timeZone = timeInfo.getTimeZoneID( );
		format = timeInfo.getFormat( );
		cale.setTimeInMillis( timeInfo.getTime( ) );

		return true;
	}

	/**
	 * Gets the information
	 * 
	 * @return information
	 */
	public DialogInfo getInfo( )
	{
		TimeDialogInfo info = new TimeDialogInfo( );
		info.setTime( cale.getTimeInMillis( ) );

		info.setTimeZoneID( timeZone );
		info.setFormat( format );
		return info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange( PropertyChangeEvent event )
	{
		String name = event.getProperty( );
		if ( name.equals( IPropertyEventConstants.YEAR_CHANGE_EVENT ) ) //$NON-NLS-1$
		{
			int year = ( (Integer) event.getNewValue( ) ).intValue( );
			cale.set( Calendar.YEAR, year );
			day.setCalendar( cale );
		}
		else if ( name.equals( IPropertyEventConstants.DAY_CHANGE_EVENT ) ) //$NON-NLS-1$
		{
			int day = ( (Integer) event.getNewValue( ) ).intValue( );
			cale.set( Calendar.DAY_OF_MONTH, day );
		}
		else if ( name.equals( IPropertyEventConstants.HOUR_CHANGE_EVENT ) ) //$NON-NLS-1$
		{
			int hour = ( (Integer) event.getNewValue( ) ).intValue( );
			cale.set( Calendar.HOUR_OF_DAY, hour );
		}
		else if ( name.equals( IPropertyEventConstants.MIN_CHANGE_EVENT ) ) //$NON-NLS-1$
		{
			int min = ( (Integer) event.getNewValue( ) ).intValue( );
			cale.set( Calendar.MINUTE, min );
		}
		else if ( name.equals( IPropertyEventConstants.SECOND_CHANGE_EVENT ) ) //$NON-NLS-1$
		{
			int sec = ( (Integer) event.getNewValue( ) ).intValue( );
			cale.set( Calendar.SECOND, sec );
		}
	}

	private void initActions( )
	{
		combo.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				Combo source = (Combo) e.getSource( );
				format = source.getText( );
			}

		} );
		zoneCombo.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				Combo source = (Combo) e.getSource( );
				//cale.setTimeZone(TimeZone.getTimeZone( source.getText()));
				updateTimeForTimeZone( timeZone, source.getText( ) );
				timeZone = source.getText( );

			}

		} );

		month.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				Combo source = (Combo) e.getSource( );
				cale.set( Calendar.MONTH,
						Integer.parseInt( source.getText( ) ) - 1 );
				day.setCalendar( cale );
			}

		} );

		year.addPropertyChangeListener( this );
		time.addPropertyChangeListener( this );
		day.addPropertyChangeListener( this );

	}

	private void updateComponent( )
	{
		combo.select( combo.indexOf( format ) );
		zoneCombo.select( zoneCombo.indexOf( timeZone ) );
		month.select( month.indexOf( String.valueOf( cale.get( Calendar.MONTH ) + 1 ) ) );

		year.setYear( cale.get( Calendar.YEAR ) );
		day.setCalendar( cale );
		time.setTimeInfo( new SpinnerTime.SpinnerTimeInfo( cale.get( Calendar.HOUR_OF_DAY ),
				cale.get( Calendar.MINUTE ),
				cale.get( Calendar.SECOND ) ) );
	}

	public boolean updateTimeForTimeZone( String oldID, String newID )
	{
		long time = cale.getTimeInMillis( );
		TimeZone oldZone = TimeZone.getTimeZone( oldID );
		TimeZone newZone = TimeZone.getTimeZone( newID );

		int oldOff = oldZone.getRawOffset( );
		int newOff = newZone.getRawOffset( );

		time = time + ( newOff - oldOff );
		cale.setTimeInMillis( time );

		return true;
	}
}

class MonthCombo extends Combo
{

	public MonthCombo( Composite parent, int style )
	{
		super( parent, style );
	}

	protected Point computeSize( Composite composite, int wHint, int hHint,
			boolean changed )
	{

		Point point = super.computeSize( wHint, hHint, changed );
		return new Point( point.x, point.y + 130 );

	}

	protected void checkWidget( )
	{

	}

	protected void checkSubclass( )
	{

	}
}