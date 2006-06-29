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

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Actuate Corporation
 * 
 */
public class GradientEditorDialog extends TrayDialog
		implements
			SelectionListener,
			Listener,
			IAngleChangeListener
{

	private transient Composite cmpContent = null;

	private transient Composite cmpGeneral = null;

	private transient FillChooserComposite fccStartColor = null;

	private transient FillChooserComposite fccEndColor = null;

	private transient Button cbCyclic = null;

	private transient Group grpRotation = null;

	private transient AngleSelectorComposite ascRotation = null;

	private transient IntegerSpinControl iscRotation = null;

	private transient Gradient gCurrent = null;

	private transient Gradient gBackup = null;

	private transient FillCanvas cnvPreview = null;

	private transient ChartWizardContext wizardContext;

	/**
	 * 
	 */
	public GradientEditorDialog( Shell shellParent,
			ChartWizardContext wizardContext, Gradient gSelected,
			ColorDefinition selectedColor )
	{
		super(shellParent);
		
		this.wizardContext = wizardContext;
		if ( gSelected != null )
		{
			gCurrent = (Gradient) EcoreUtil.copy( gSelected );
			gBackup = (Gradient) EcoreUtil.copy( gSelected );
		}
		else
		{
			gCurrent = AttributeFactory.eINSTANCE.createGradient( );
			setGradientColor( gCurrent, selectedColor );
		}
	}

	public GradientEditorDialog( Shell shellParent,
			ChartWizardContext wizardContext, Gradient gSelected )
	{
		this( shellParent,
				wizardContext,
				gSelected,
				ColorDefinitionImpl.create( 0, 0, 254 ) );
	}

	protected Control createContents( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.DIALOG_COLOR_GRADIENT );
		getShell( ).setText( Messages.getString( "GradientEditorDialog.Lbl.GradientEditor" ) ); //$NON-NLS-1$
		getShell( ).setSize( 420, 320 );
		UIHelper.centerOnScreen( getShell( ) );
		return super.createContents( parent );
	}
	
	protected void setShellStyle( int newShellStyle )
	{
		super.setShellStyle( newShellStyle
				| SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL );
	}

	protected Control createDialogArea( Composite parent )
	{
		GridLayout glContent = new GridLayout( );
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;

		cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( glContent );
		cmpContent.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		GridLayout glGeneral = new GridLayout( );
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;

		cmpGeneral = new Composite( cmpContent, SWT.NONE );
		GridData gdCMPGeneral = new GridData( GridData.FILL_BOTH );
		cmpGeneral.setLayoutData( gdCMPGeneral );
		cmpGeneral.setLayout( glGeneral );

		Label lblStartColor = new Label( cmpGeneral, SWT.NONE );
		GridData gdLBLStartColor = new GridData( );
		lblStartColor.setLayoutData( gdLBLStartColor );
		lblStartColor.setText( Messages.getString( "GradientEditorDialog.Lbl.StartColor" ) ); //$NON-NLS-1$

		fccStartColor = new FillChooserComposite( cmpGeneral,
				SWT.NONE,
				wizardContext,
				gCurrent.getStartColor( ),
				false,
				false );
		GridData gdFCCStartColor = new GridData( GridData.FILL_HORIZONTAL );
		fccStartColor.setLayoutData( gdFCCStartColor );
		fccStartColor.addListener( this );

		Label lblEndColor = new Label( cmpGeneral, SWT.NONE );
		GridData gdLBLEndColor = new GridData( );
		lblEndColor.setLayoutData( gdLBLEndColor );
		lblEndColor.setText( Messages.getString( "GradientEditorDialog.Lbl.EndColor" ) ); //$NON-NLS-1$

		fccEndColor = new FillChooserComposite( cmpGeneral,
				SWT.NONE,
				wizardContext,
				gCurrent.getEndColor( ),
				false,
				false );
		GridData gdFCCEndColor = new GridData( GridData.FILL_HORIZONTAL );
		fccEndColor.setLayoutData( gdFCCEndColor );
		fccEndColor.addListener( this );

		Label lblDummy = new Label( cmpGeneral, SWT.NONE );
		GridData gdLBLDummy = new GridData( GridData.FILL_BOTH );
		lblDummy.setLayoutData( gdLBLDummy );

		createRotationPanel( );

		/*
		 * cbCyclic = new Button(cmpContent, SWT.CHECK); GridData gdCBCyclic =
		 * new GridData(GridData.FILL_HORIZONTAL); gdCBCyclic.horizontalSpan =
		 * 4; cbCyclic.setLayoutData(gdCBCyclic); cbCyclic.setText("Is Cyclic");
		 * cbCyclic.setSelection(gCurrent.isCyclic());
		 */

		Group grpPreview = new Group( cmpContent, SWT.NONE );
		GridData gdGRPPreview = new GridData( GridData.FILL_BOTH );
		gdGRPPreview.horizontalSpan = 2;
		grpPreview.setLayoutData( gdGRPPreview );
		grpPreview.setLayout( new FillLayout( ) );
		grpPreview.setText( Messages.getString( "GradientEditorDialog.Lbl.Preview" ) ); //$NON-NLS-1$

		cnvPreview = new FillCanvas( grpPreview, SWT.NO_FOCUS );
		cnvPreview.setFill( gCurrent );

		return cmpContent;
	}

	private void createRotationPanel( )
	{
		GridLayout glRotation = new GridLayout( );
		glRotation.verticalSpacing = 2;
		glRotation.marginHeight = 2;
		glRotation.marginWidth = 2;
		glRotation.numColumns = 3;

		grpRotation = new Group( cmpContent, SWT.NONE );
		GridData gdGRPRotation = new GridData( GridData.FILL_BOTH );
		gdGRPRotation.heightHint = 180;
		grpRotation.setLayoutData( gdGRPRotation );
		grpRotation.setLayout( glRotation );
		grpRotation.setText( Messages.getString( "GradientEditorDialog.Lbl.Rotation" ) ); //$NON-NLS-1$

		ascRotation = new AngleSelectorComposite( grpRotation,
				SWT.BORDER,
				(int) gCurrent.getDirection( ),
				Display.getCurrent( ).getSystemColor( SWT.COLOR_WHITE ) );
		GridData gdASCRotation = new GridData( GridData.FILL_BOTH );
		gdASCRotation.horizontalSpan = 1;
		gdASCRotation.verticalSpan = 3;
		ascRotation.setLayoutData( gdASCRotation );
		ascRotation.setAngleChangeListener( this );

		iscRotation = new IntegerSpinControl( grpRotation,
				SWT.NONE,
				(int) gCurrent.getDirection( ) );
		GridData gdISCRotation = new GridData( GridData.FILL_HORIZONTAL );
		gdISCRotation.horizontalSpan = 2;
		iscRotation.setLayoutData( gdISCRotation );
		iscRotation.setMinimum( -90 );
		iscRotation.setMaximum( 90 );
		iscRotation.setIncrement( 1 );
		iscRotation.addListener( this );
	}

	public Gradient getGradient( )
	{
		return gCurrent;
	}
	
	protected void cancelPressed( )
	{
		gCurrent = gBackup;
		super.cancelPressed( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( cbCyclic ) )
		{
			gCurrent.setCyclic( cbCyclic.getSelection( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( fccStartColor ) )
		{
			gCurrent.setStartColor( (ColorDefinition) event.data );
		}
		else if ( event.widget.equals( fccEndColor ) )
		{
			gCurrent.setEndColor( (ColorDefinition) event.data );
		}
		else if ( event.widget.equals( iscRotation ) )
		{
			gCurrent.setDirection( iscRotation.getValue( ) );
			ascRotation.setAngle( iscRotation.getValue( ) );
			ascRotation.redraw( );
		}
		cnvPreview.redraw( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.composites.IAngleChangeListener#angleChanged(int)
	 */
	public void angleChanged( int iNewAngle )
	{
		iscRotation.setValue( iNewAngle );
		gCurrent.setDirection( iNewAngle );
	}

	private int convertRGBToLuminance( int red, int green, int blue )
	{
		return (int) ( 0.3 * red + 0.59 * green + 0.11 * blue );
	}

	private int getNewColor( int lumDiff, int oldColor, double coefficient )
	{
		int newColor = (int) ( lumDiff * coefficient ) + oldColor;
		return newColor < 255 ? newColor : 255;
	}

	private void setGradientColor( Gradient gradient,
			ColorDefinition selectedColor )
	{
		int currentLuminance = convertRGBToLuminance( selectedColor.getRed( ),
				selectedColor.getGreen( ),
				selectedColor.getBlue( ) );
		if ( currentLuminance < 200 )
		{
			gradient.setStartColor( selectedColor );
			ColorDefinition newColor = (ColorDefinition) EcoreUtil.copy( selectedColor );
			newColor.eAdapters( ).addAll( selectedColor.eAdapters( ) );

			int lumDiff = 240 - currentLuminance;
			newColor.setRed( getNewColor( lumDiff, newColor.getRed( ), 0.3 ) );
			newColor.setGreen( getNewColor( lumDiff, newColor.getGreen( ), 0.59 ) );
			newColor.setBlue( getNewColor( lumDiff, newColor.getBlue( ), 0.11 ) );
			gradient.setEndColor( newColor );
		}
		else
		{
			gradient.setEndColor( selectedColor );
			ColorDefinition newColor = (ColorDefinition) EcoreUtil.copy( selectedColor );
			newColor.eAdapters( ).addAll( selectedColor.eAdapters( ) );

			int lumDiff = -100;
			newColor.setRed( getNewColor( lumDiff, newColor.getRed( ), 0.3 ) );
			newColor.setGreen( getNewColor( lumDiff, newColor.getGreen( ), 0.59 ) );
			newColor.setBlue( getNewColor( lumDiff, newColor.getBlue( ), 0.11 ) );
			gradient.setStartColor( newColor );
		}
	}

}