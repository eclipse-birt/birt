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

import java.util.Vector;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.fieldassist.TextNumberEditorAssistField;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class InsetsComposite extends Composite implements ModifyListener, SelectionListener
{

	private transient String sUnits = null;

	public static final int INSETS_CHANGED_EVENT = 1;

	private transient Insets insets = null;

	private transient Group grpInsets = null;

	private transient Label lblTop = null;

	private transient Label lblLeft = null;

	private transient Label lblBottom = null;

	private transient Label lblRight = null;

	private transient LocalizedNumberEditorComposite txtTop = null;

	private transient LocalizedNumberEditorComposite txtLeft = null;

	private transient LocalizedNumberEditorComposite txtBottom = null;

	private transient LocalizedNumberEditorComposite txtRight = null;

	private transient Vector<Listener> vListeners = null;

	private transient IUIServiceProvider serviceprovider = null;

	private transient boolean bEnabled = true;

	private transient int numberRows = 2;

	private Button btnAuto;

	private Insets defaultInsets = null;

	/**
	 * Creates a composite for <code>Inserts</code>. Default row number is 2.
	 * 
	 * @param parent
	 * @param style
	 * @param insets
	 * @param sUnits
	 * @param serviceprovider
	 */
	public InsetsComposite( Composite parent, int style, Insets insets,
			String sUnits, IUIServiceProvider serviceprovider )
	{
		this( parent, style, 2, insets, sUnits, serviceprovider );
	}

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param numberRows
	 *            specify row number. Valid number is 1,2,4.
	 * @param insets
	 * @param sUnits
	 * @param serviceprovider
	 */
	public InsetsComposite( Composite parent, int style, int numberRows,
			Insets insets, String sUnits, IUIServiceProvider serviceprovider )
	{
		super( parent, style );
		this.numberRows = numberRows;
		this.insets = insets;
		this.sUnits = sUnits;
		this.serviceprovider = serviceprovider;
		init( );
		placeComponents( );
	}

	/**
	 * 
	 */
	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
		this.vListeners = new Vector<Listener>( );
	}

	/**
	 * 
	 */
	private void placeComponents( )
	{
		FillLayout flMain = new FillLayout( );
		flMain.marginHeight = 0;
		flMain.marginWidth = 0;

		GridLayout glGroup = new GridLayout( );
		glGroup.horizontalSpacing = 8;
		glGroup.verticalSpacing = 5;
		glGroup.marginHeight = 4;
		glGroup.marginWidth = 4;
		glGroup.numColumns = 8 / numberRows;

		this.setLayout( flMain );

		grpInsets = new Group( this, SWT.NONE );
		grpInsets.setLayout( glGroup );
		grpInsets.setText( Messages.getFormattedString( "InsetsComposite.Lbl.Insets", //$NON-NLS-1$
				LiteralHelper.unitsOfMeasurementSet.getDisplayNameByName( sUnits,
						sUnits ) ) );

		btnAuto = new Button( grpInsets, SWT.CHECK );
		btnAuto.setText( ChartUIExtensionUtil.getAutoMessage( ) );
		btnAuto.addSelectionListener( this );
		GridData gd = new GridData();
		gd.horizontalSpan = glGroup.numColumns;
		btnAuto.setLayoutData( gd );
		
		lblTop = new Label( grpInsets, SWT.NONE );
		GridData gdLTop = new GridData( GridData.VERTICAL_ALIGN_CENTER );
		// gdLTop.heightHint = 20;
		lblTop.setLayoutData( gdLTop );
		lblTop.setText( Messages.getString( "InsetsComposite.Lbl.Top" ) ); //$NON-NLS-1$

		txtTop = new LocalizedNumberEditorComposite( grpInsets, SWT.BORDER );
		new TextNumberEditorAssistField( txtTop.getTextControl( ), null );
		
		GridData gdTTop = new GridData( GridData.FILL_BOTH );
		// gdTTop.heightHint = 20;
		gdTTop.widthHint = 45;
		txtTop.setLayoutData( gdTTop );

		lblLeft = new Label( grpInsets, SWT.NONE );
		GridData gdLLeft = new GridData( GridData.VERTICAL_ALIGN_CENTER );
		// gdLLeft.heightHint = 20;
		lblLeft.setLayoutData( gdLLeft );
		lblLeft.setText( Messages.getString( "InsetsComposite.Lbl.Left" ) ); //$NON-NLS-1$

		txtLeft = new LocalizedNumberEditorComposite( grpInsets, SWT.BORDER );
		new TextNumberEditorAssistField( txtLeft.getTextControl( ), null );
		
		GridData gdTLeft = new GridData( GridData.FILL_BOTH );
		// gdTLeft.heightHint = 20;
		gdTLeft.widthHint = 45;
		txtLeft.setLayoutData( gdTLeft );

		lblBottom = new Label( grpInsets, SWT.NONE );
		GridData gdLBottom = new GridData( GridData.VERTICAL_ALIGN_CENTER );
		// gdLBottom.heightHint = 20;
		lblBottom.setLayoutData( gdLBottom );
		lblBottom.setText( Messages.getString( "InsetsComposite.Lbl.Bottom" ) ); //$NON-NLS-1$

		txtBottom = new LocalizedNumberEditorComposite( grpInsets, SWT.BORDER );
		new TextNumberEditorAssistField( txtBottom.getTextControl( ), null );
		
		GridData gdTBottom = new GridData( GridData.FILL_BOTH );
		// gdTBottom.heightHint = 20;
		gdTBottom.widthHint = 45;
		txtBottom.setLayoutData( gdTBottom );

		lblRight = new Label( grpInsets, SWT.NONE );
		GridData gdLRight = new GridData( GridData.VERTICAL_ALIGN_CENTER );
		// gdLRight.heightHint = 20;
		lblRight.setLayoutData( gdLRight );
		lblRight.setText( Messages.getString( "InsetsComposite.Lbl.Right" ) ); //$NON-NLS-1$

		txtRight = new LocalizedNumberEditorComposite( grpInsets, SWT.BORDER );
		new TextNumberEditorAssistField( txtRight.getTextControl( ), null );
		
		GridData gdTRight = new GridData( GridData.FILL_BOTH );
		// gdTRight.heightHint = 20;
		gdTRight.widthHint = 45;
		txtRight.setLayoutData( gdTRight );

		updateInsetsData( insets );
		initStatus( );
		
		setModifyListener( true );
	}

	protected void setModifyListener( boolean enabled )
	{
		if ( enabled )
		{
			txtTop.addModifyListener( this );
			txtLeft.addModifyListener( this );
			txtRight.addModifyListener( this );
			txtBottom.addModifyListener( this );
		}
		else
		{
			txtTop.removeModifyListener( this );
			txtLeft.removeModifyListener( this );
			txtRight.removeModifyListener( this );
			txtBottom.removeModifyListener( this );
		}
	}

	private void updateInsetsData( Insets insets )
	{
		double dblPoints = insets.getTop( );
		double dblCurrent = serviceprovider.getConvertedValue( dblPoints,
				"Points", sUnits ); //$NON-NLS-1$
		txtTop.setValue( dblCurrent );

		dblPoints = insets.getLeft( );
		dblCurrent = serviceprovider.getConvertedValue( dblPoints,
				"Points", sUnits ); //$NON-NLS-1$
		txtLeft.setValue( dblCurrent );

		dblPoints = insets.getBottom( );
		dblCurrent = serviceprovider.getConvertedValue( dblPoints,
				"Points", sUnits ); //$NON-NLS-1$
		txtBottom.setValue( dblCurrent );

		dblPoints = insets.getRight( );
		dblCurrent = serviceprovider.getConvertedValue( dblPoints,
				"Points", sUnits ); //$NON-NLS-1$
		txtRight.setValue( dblCurrent );
	}
	
	private void initStatus( )
	{
		if ( !ChartElementUtil.isSetInsets( insets ) )
		{
			btnAuto.setSelection( true );
			updateInsetsButtons( false );
		}
		else
		{
			btnAuto.setSelection( false );
			updateInsetsButtons( true );
		}
	}
	
	public void setEnabled( boolean bState )
	{
		btnAuto.setEnabled( bState );
		grpInsets.setEnabled( bState );
		if ( btnAuto.getSelection( ) )
		{
			updateInsetsButtons( false );
		}
		else
		{
			updateInsetsButtons( true && bState );
		}
		bEnabled = bState;
	}

	public boolean isEnabled( )
	{
		return bEnabled;
	}

	public void setInsets( Insets insets, String sUnits )
	{
		if ( insets == null )
		{
			return;
		}
		setModifyListener( false );
		
		this.insets = insets;
		this.sUnits = sUnits;
		
		btnAuto.setSelection( !ChartElementUtil.isSetInsets( insets ) );
		updateInsetsButtons( bEnabled && ChartElementUtil.isSetInsets( insets ) );
		
		// Update the UI
		double dblPoints = insets.getBottom( );
		double dblCurrent = serviceprovider.getConvertedValue( dblPoints,
				"Points", sUnits ); //$NON-NLS-1$
		txtBottom.setValue( dblCurrent );

		dblPoints = insets.getLeft( );
		dblCurrent = serviceprovider.getConvertedValue( dblPoints,
				"Points", sUnits ); //$NON-NLS-1$
		txtLeft.setValue( dblCurrent );

		dblPoints = insets.getTop( );
		dblCurrent = serviceprovider.getConvertedValue( dblPoints,
				"Points", sUnits ); //$NON-NLS-1$
		txtTop.setValue( dblCurrent );

		dblPoints = insets.getRight( );
		dblCurrent = serviceprovider.getConvertedValue( dblPoints,
				"Points", sUnits ); //$NON-NLS-1$
		txtRight.setValue( dblCurrent );

		setModifyListener( true );
		
		this.grpInsets.setText( Messages.getFormattedString( "InsetsComposite.Lbl.Insets", //$NON-NLS-1$
				LiteralHelper.unitsOfMeasurementSet.getDisplayNameByName( sUnits ) ) );
	}

	public void addListener( Listener listener )
	{
		vListeners.add( listener );
	}

	public Point getPreferredSize( )
	{
		return new Point( 300, 70 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText( ModifyEvent event )
	{
		double dblCurrent = -1;
		double dblPoints = -1;
		if ( event.widget.equals( txtTop ) )
		{
			dblCurrent = txtTop.getValue( );
			dblPoints = serviceprovider.getConvertedValue( dblCurrent,
					sUnits,
					"Points" ); //$NON-NLS-1$
			insets.setTop( dblPoints );
		}
		else if ( event.widget.equals( txtLeft ) )
		{
			dblCurrent = txtLeft.getValue( );
			dblPoints = serviceprovider.getConvertedValue( dblCurrent,
					sUnits,
					"Points" ); //$NON-NLS-1$
			insets.setLeft( dblPoints );
		}
		else if ( event.widget.equals( txtBottom ) )
		{
			dblCurrent = txtBottom.getValue( );
			dblPoints = serviceprovider.getConvertedValue( dblCurrent,
					sUnits,
					"Points" ); //$NON-NLS-1$
			insets.setBottom( dblPoints );
		}
		else if ( event.widget.equals( txtRight ) )
		{
			dblCurrent = txtRight.getValue( );
			dblPoints = serviceprovider.getConvertedValue( dblCurrent,
					sUnits,
					"Points" ); //$NON-NLS-1$
			insets.setRight( dblPoints );
		}
	}

	public void widgetDefaultSelected( SelectionEvent arg0 )
	{
		// TODO Auto-generated method stub
		
	}

	public void widgetSelected( SelectionEvent event )
	{
		if ( event.widget == btnAuto )
		{
			if ( btnAuto.getSelection( ) )
			{
				updateInsetsButtons( false );
				if ( defaultInsets != null )
				{
					insets.unsetTop( );
					insets.unsetBottom( );
					insets.unsetLeft( );
					insets.unsetRight( );
					try
					{
						ChartElementUtil.setDefaultValue( insets, "top", defaultInsets.getTop( ) );  //$NON-NLS-1$
						ChartElementUtil.setDefaultValue( insets, "bottom", defaultInsets.getBottom( ) );  //$NON-NLS-1$
						ChartElementUtil.setDefaultValue( insets, "left", defaultInsets.getLeft( ) );  //$NON-NLS-1$
						ChartElementUtil.setDefaultValue( insets, "right", defaultInsets.getRight( ) );  //$NON-NLS-1$
					}
					catch ( ChartException e )
					{
						ChartWizard.displayException( e );
					}
				}
			}
			else
			{
				updateInsetsButtons( true );
				insets.setTop( txtTop.getValue( ) );
				insets.setLeft( txtLeft.getValue( ) );
				insets.setRight( txtRight.getValue( ) );
				insets.setBottom( txtBottom.getValue( ) );
			}
			
			setModifyListener( false );
			updateInsetsData( insets );
			setModifyListener( true );
		}
		
	}

	private void updateInsetsButtons( boolean bState )
	{
		lblTop.setEnabled( bState );
		txtTop.setEnabled( bState );
		lblLeft.setEnabled( bState );
		txtLeft.setEnabled( bState );
		lblBottom.setEnabled( bState );
		txtBottom.setEnabled( bState );
		lblRight.setEnabled( bState );
		txtRight.setEnabled( bState );
	}
	
	public void setDefaultInsetsValue(Insets insets )
	{
		this.defaultInsets  = insets;
	}
}