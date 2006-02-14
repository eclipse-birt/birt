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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.ScaleUnitType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FormatSpecifierDialog;
import org.eclipse.birt.chart.ui.swt.composites.IntegerSpinControl;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class AxisScaleSheet extends AbstractPopupSheet
		implements
			Listener,
			SelectionListener
{

	private transient Composite cmpContent;

	private transient Combo cmbTypes = null;

	private transient Combo cmbOrigin = null;

	private transient Label lblValue = null;

	private transient TextEditorComposite txtValue = null;

	private transient Button btnFormatSpecifier = null;

	private transient Label lblMin = null;

	private transient TextEditorComposite txtScaleMin = null;

	private transient Label lblMax = null;

	private transient TextEditorComposite txtScaleMax = null;

	private transient Label lblStep = null;

	private transient TextEditorComposite txtScaleStep = null;

	private transient Label lblUnit = null;

	private transient Combo cmbScaleUnit = null;

	private transient Label lblGridCount = null;

	private transient IntegerSpinControl iscGridCount = null;

	private transient Axis axis;

	public AxisScaleSheet( Composite parent, ChartWizardContext context,
			Axis axis )
	{
		super( parent, context, true );
		this.axis = axis;
		cmpTop = getComponent( parent );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public Composite getComponent( Composite parent )
	{
		GridLayout glContent = new GridLayout( 2, true );
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;
		glContent.horizontalSpacing = 2;
		glContent.verticalSpacing = 5;

		cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( glContent );

		if ( isAxisX( ) )
		{
			Composite cmpLeftPane = new Composite( cmpContent, SWT.NONE );
			{
				GridLayout glLeftPane = new GridLayout( 3, false );
				glLeftPane.horizontalSpacing = 2;
				glLeftPane.verticalSpacing = 5;
				glLeftPane.marginHeight = 0;
				glLeftPane.marginWidth = 2;
				cmpLeftPane.setLayout( glLeftPane );
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				cmpLeftPane.setLayoutData( gd );
			}

			Label lblType = new Label( cmpLeftPane, SWT.NONE );
			GridData gdLBLType = new GridData( );
			lblType.setLayoutData( gdLBLType );
			lblType.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Type" ) ); //$NON-NLS-1$

			cmbTypes = new Combo( cmpLeftPane, SWT.DROP_DOWN | SWT.READ_ONLY );
			GridData gdCMBTypes = new GridData( GridData.FILL_HORIZONTAL );
			cmbTypes.setLayoutData( gdCMBTypes );
			cmbTypes.addSelectionListener( this );

			btnFormatSpecifier = new Button( cmpLeftPane, SWT.PUSH );
			GridData gdBTNFormatSpecifier = new GridData( GridData.VERTICAL_ALIGN_END );
			gdBTNFormatSpecifier.widthHint = 20;
			gdBTNFormatSpecifier.heightHint = 20;
			btnFormatSpecifier.setLayoutData( gdBTNFormatSpecifier );
			btnFormatSpecifier.setImage( UIHelper.getImage( "icons/obj16/formatbuilder.gif" ) ); //$NON-NLS-1$
			btnFormatSpecifier.setToolTipText( Messages.getString( "Shared.Tooltip.FormatSpecifier" ) ); //$NON-NLS-1$
			btnFormatSpecifier.addSelectionListener( this );
			btnFormatSpecifier.getImage( )
					.setBackground( btnFormatSpecifier.getBackground( ) );

			Composite cmpRightPane = new Composite( cmpContent, SWT.NONE );
			{
				GridLayout glRightPane = new GridLayout( 2, false );
				glRightPane.marginHeight = 0;
				cmpRightPane.setLayout( glRightPane );
				GridData gd = new GridData( GridData.FILL_BOTH );
				cmpRightPane.setLayoutData( gd );
			}

			Label lblOrigin = new Label( cmpLeftPane, SWT.NONE );
			GridData gdLBLOrigin = new GridData( );
			lblOrigin.setLayoutData( gdLBLOrigin );
			lblOrigin.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Origin" ) ); //$NON-NLS-1$

			cmbOrigin = new Combo( cmpLeftPane, SWT.DROP_DOWN | SWT.READ_ONLY );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				gd.horizontalSpan = 2;
				gd.widthHint = 120;
				cmbOrigin.setLayoutData( gd );
				cmbOrigin.addSelectionListener( this );
			}

			boolean bValueOrigin = false;
			if ( getAxisForProcessing( ).getOrigin( ) != null )
			{
				if ( getAxisForProcessing( ).getOrigin( )
						.getType( )
						.equals( IntersectionType.VALUE_LITERAL ) )
				{
					bValueOrigin = true;
				}
			}

			lblValue = new Label( cmpRightPane, SWT.NONE );
			{
				GridData gd = new GridData( );
				// gd.grabExcessVerticalSpace = true;
				gd.verticalAlignment = SWT.END;
				lblValue.setLayoutData( gd );
				lblValue.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Value" ) ); //$NON-NLS-1$
				lblValue.setEnabled( bValueOrigin );
			}

			txtValue = new TextEditorComposite( cmpRightPane, SWT.BORDER
					| SWT.SINGLE );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				gd.grabExcessVerticalSpace = true;
				gd.verticalAlignment = SWT.END;
				gd.widthHint = 120;
				txtValue.setLayoutData( gd );
				txtValue.addListener( this );
				txtValue.setEnabled( bValueOrigin );
			}
		}

		Group grpScale = new Group( cmpContent, SWT.NONE );
		{
			GridData gdGRPScale = new GridData( GridData.FILL_BOTH );
			gdGRPScale.horizontalSpan = 4;
			grpScale.setLayoutData( gdGRPScale );
			GridLayout glScale = new GridLayout( );
			glScale.numColumns = 4;
			glScale.horizontalSpacing = 5;
			glScale.verticalSpacing = 5;
			glScale.marginHeight = 2;
			glScale.marginWidth = 7;
			grpScale.setLayout( glScale );
			grpScale.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Scale" ) ); //$NON-NLS-1$
		}

		lblMin = new Label( grpScale, SWT.NONE );
		GridData gdLBLMin = new GridData( );
		lblMin.setLayoutData( gdLBLMin );
		lblMin.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Minimum" ) ); //$NON-NLS-1$

		txtScaleMin = new TextEditorComposite( grpScale, SWT.BORDER
				| SWT.SINGLE );
		GridData gdTXTScaleMin = new GridData( GridData.HORIZONTAL_ALIGN_FILL );
		txtScaleMin.setLayoutData( gdTXTScaleMin );
		txtScaleMin.setText( getValue( getAxisForProcessing( ).getScale( )
				.getMin( ) ) );
		txtScaleMin.addListener( this );

		lblMax = new Label( grpScale, SWT.NONE );
		GridData gdLBLMax = new GridData( );
		lblMax.setLayoutData( gdLBLMax );
		lblMax.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Maximum" ) ); //$NON-NLS-1$

		txtScaleMax = new TextEditorComposite( grpScale, SWT.BORDER
				| SWT.SINGLE );
		GridData gdTXTScaleMax = new GridData( GridData.HORIZONTAL_ALIGN_FILL );
		txtScaleMax.setLayoutData( gdTXTScaleMax );
		txtScaleMax.setText( getValue( getAxisForProcessing( ).getScale( )
				.getMax( ) ) );
		txtScaleMax.addListener( this );

		boolean bDateTimeAxisType = getAxisForProcessing( ).getType( )
				.equals( AxisType.DATE_TIME_LITERAL );

		lblStep = new Label( grpScale, SWT.NONE );
		GridData gdLBLStep = new GridData( );
		lblStep.setLayoutData( gdLBLStep );
		lblStep.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Step" ) ); //$NON-NLS-1$
		lblStep.setEnabled( !bDateTimeAxisType );

		txtScaleStep = new TextEditorComposite( grpScale, SWT.BORDER
				| SWT.SINGLE );
		GridData gdTXTScaleStep = new GridData( GridData.FILL_HORIZONTAL );
		txtScaleStep.setLayoutData( gdTXTScaleStep );
		String str = ""; //$NON-NLS-1$
		if ( getAxisForProcessing( ).getScale( )
				.eIsSet( ComponentPackage.eINSTANCE.getScale_Step( ) ) )
		{
			str = String.valueOf( getAxisForProcessing( ).getScale( ).getStep( ) );
		}
		txtScaleStep.setText( str );
		txtScaleStep.addListener( this );
		txtScaleStep.setEnabled( !bDateTimeAxisType );

		lblUnit = new Label( grpScale, SWT.NONE );
		GridData gdLBLUnit = new GridData( );
		lblUnit.setLayoutData( gdLBLUnit );
		lblUnit.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Unit" ) ); //$NON-NLS-1$
		lblUnit.setEnabled( false );

		cmbScaleUnit = new Combo( grpScale, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBScaleUnit = new GridData( GridData.FILL_HORIZONTAL );
		cmbScaleUnit.setLayoutData( gdCMBScaleUnit );
		cmbScaleUnit.addSelectionListener( this );
		cmbScaleUnit.setEnabled( false );

		GridLayout glGridCount = new GridLayout( );
		glGridCount.numColumns = 2;
		glGridCount.horizontalSpacing = 5;
		glGridCount.marginHeight = 0;
		glGridCount.marginWidth = 0;

		Composite cmpGridCount = new Composite( grpScale, SWT.NONE );
		GridData gdCMPGridCount = new GridData( GridData.FILL_HORIZONTAL );
		gdCMPGridCount.horizontalSpan = 4;
		cmpGridCount.setLayoutData( gdCMPGridCount );
		cmpGridCount.setLayout( glGridCount );

		lblGridCount = new Label( cmpGridCount, SWT.NONE );
		GridData gdLBLGridCount = new GridData( );
		lblGridCount.setLayoutData( gdLBLGridCount );
		lblGridCount.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.MinorGridCount" ) ); //$NON-NLS-1$

		iscGridCount = new IntegerSpinControl( cmpGridCount,
				SWT.NONE,
				getAxisForProcessing( ).getScale( ).getMinorGridsPerUnit( ) );
		GridData gdISCGridCount = new GridData( GridData.FILL_HORIZONTAL );
		iscGridCount.setLayoutData( gdISCGridCount );
		iscGridCount.addListener( this );

		GridLayout glSeriesDefinitionCount = new GridLayout( );
		glSeriesDefinitionCount.numColumns = 2;
		glSeriesDefinitionCount.marginHeight = 0;
		glSeriesDefinitionCount.marginWidth = 0;
		glSeriesDefinitionCount.horizontalSpacing = 5;

		populateLists( );

		return cmpContent;
	}

	private void populateLists( )
	{
		NameSet ns = null;
		if ( isAxisX( ) )
		{
			// Populate axis types combo
			ns = LiteralHelper.axisTypeSet;
			cmbTypes.setItems( ns.getDisplayNames( ) );
			cmbTypes.select( ns.getSafeNameIndex( getAxisForProcessing( ).getType( )
					.getName( ) ) );
			setState( LiteralHelper.axisTypeSet.getNameByDisplayName( cmbTypes.getText( ) ) );

			// Populate origin types combo
			ns = LiteralHelper.intersectionTypeSet;
			cmbOrigin.setItems( ns.getDisplayNames( ) );
			cmbOrigin.select( ns.getSafeNameIndex( getAxisForProcessing( ).getOrigin( )
					.getType( )
					.getName( ) ) );
			if ( getAxisForProcessing( ).getOrigin( )
					.getType( )
					.equals( IntersectionType.VALUE_LITERAL ) )
			{
				txtValue.setText( getValue( getAxisForProcessing( ).getOrigin( )
						.getValue( ) ) );
			}
		}
		else
		{
			setState( getAxisForProcessing( ).getType( ).getName( ) );
		}

		// Populate origin types combo
		ns = LiteralHelper.scaleUnitTypeSet;
		cmbScaleUnit.setItems( ns.getDisplayNames( ) );
		cmbScaleUnit.select( ns.getSafeNameIndex( getAxisForProcessing( ).getScale( )
				.getUnit( )
				.getName( ) ) );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( this.txtValue ) )
		{
			DataElement de = getTypedDataElement( txtValue.getText( ) );
			if ( de != null )
			{
				getAxisForProcessing( ).getOrigin( ).setValue( de );
			}
		}
		else if ( event.widget.equals( txtScaleMin ) )
		{
			if ( txtScaleMin.getText( ).length( ) == 0 )
			{
				getAxisForProcessing( ).getScale( )
						.eUnset( ComponentPackage.eINSTANCE.getScale_Min( ) );
			}
			else
			{
				DataElement de = getTypedDataElement( txtScaleMin.getText( ) );
				if ( de != null )
				{
					getAxisForProcessing( ).getScale( ).setMin( de );
				}
			}
		}
		else if ( event.widget.equals( txtScaleMax ) )
		{
			if ( txtScaleMax.getText( ).length( ) == 0 )
			{
				getAxisForProcessing( ).getScale( )
						.eUnset( ComponentPackage.eINSTANCE.getScale_Max( ) );
			}
			else
			{
				DataElement de = getTypedDataElement( txtScaleMax.getText( ) );
				if ( de != null )
				{
					getAxisForProcessing( ).getScale( ).setMax( de );
				}
			}
		}
		else if ( event.widget.equals( txtScaleStep ) )
		{
			try
			{
				if ( txtScaleStep.getText( ).length( ) == 0 )
				{
					getAxisForProcessing( ).getScale( )
							.eUnset( ComponentPackage.eINSTANCE.getScale_Step( ) );
				}
				else
				{
					double dbl = Double.valueOf( txtScaleStep.getText( ) )
							.doubleValue( );
					if ( dbl == 0 )
					{
						getAxisForProcessing( ).getScale( )
								.eUnset( ComponentPackage.eINSTANCE.getScale_Step( ) );
					}
					else
					{
						getAxisForProcessing( ).getScale( ).setStep( dbl );
					}
				}
			}
			catch ( NumberFormatException e1 )
			{
				txtScaleStep.setText( String.valueOf( getAxisForProcessing( ).getScale( )
						.getStep( ) ) );
			}
		}
		else if ( event.widget.equals( iscGridCount ) )
		{
			getAxisForProcessing( ).getScale( )
					.setMinorGridsPerUnit( ( (Integer) event.data ).intValue( ) );
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
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		Object oSource = e.getSource( );
		if ( oSource.equals( cmbTypes ) )
		{
			getAxisForProcessing( ).setType( AxisType.getByName( LiteralHelper.axisTypeSet.getNameByDisplayName( cmbTypes.getText( ) ) ) );
			convertSampleData( );
			setState( LiteralHelper.axisTypeSet.getNameByDisplayName( cmbTypes.getText( ) ) );
		}
		else if ( oSource.equals( cmbOrigin ) )
		{
			if ( LiteralHelper.intersectionTypeSet.getNameByDisplayName( cmbOrigin.getText( ) )
					.equals( IntersectionType.VALUE_LITERAL.getName( ) ) )
			{
				lblValue.setEnabled( true );
				txtValue.setEnabled( true );
			}
			else
			{
				lblValue.setEnabled( false );
				txtValue.setEnabled( false );
			}
			getAxisForProcessing( ).getOrigin( )
					.setType( IntersectionType.getByName( LiteralHelper.intersectionTypeSet.getNameByDisplayName( cmbOrigin.getText( ) ) ) );
		}
		else if ( oSource.equals( cmbScaleUnit ) )
		{
			getAxisForProcessing( ).getScale( )
					.setUnit( ScaleUnitType.getByName( LiteralHelper.scaleUnitTypeSet.getNameByDisplayName( cmbScaleUnit.getText( ) ) ) );
		}
		else if ( oSource.equals( btnFormatSpecifier ) )
		{
			String sAxisTitle = Messages.getString( "BaseAxisDataSheetImpl.Lbl.BaseAxis" ); //$NON-NLS-1$
			try
			{
				String sTitleString = getAxisForProcessing( ).getTitle( )
						.getCaption( )
						.getValue( );
				int iSeparatorIndex = sTitleString.indexOf( ExternalizedTextEditorComposite.SEPARATOR );
				if ( iSeparatorIndex > 0 )
				{
					sTitleString = sTitleString.substring( iSeparatorIndex );
				}
				else if ( iSeparatorIndex == 0 )
				{
					sTitleString = sTitleString.substring( ExternalizedTextEditorComposite.SEPARATOR.length( ) );
				}
				sAxisTitle = new MessageFormat( Messages.getString( "BaseAxisDataSheetImpl.Lbl.BaseAxis2" ) ).format( new Object[]{sTitleString} ); //$NON-NLS-1$
			}
			catch ( NullPointerException e1 )
			{
			}

			FormatSpecifier formatspecifier = null;
			if ( getAxisForProcessing( ).getFormatSpecifier( ) != null )
			{
				formatspecifier = getAxisForProcessing( ).getFormatSpecifier( );
			}
			FormatSpecifierDialog editor = new FormatSpecifierDialog( cmpContent.getShell( ),
					formatspecifier,
					sAxisTitle );
			if ( !editor.wasCancelled( ) )
			{
				if ( editor.getFormatSpecifier( ) == null )
				{
					getAxisForProcessing( ).eUnset( ComponentPackage.eINSTANCE.getAxis_FormatSpecifier( ) );
					return;
				}
				getAxisForProcessing( ).setFormatSpecifier( editor.getFormatSpecifier( ) );
			}
		}
	}

	private void setState( String sType )
	{
		// Bugzilla#103961 Marker line and range only work for non-category
		// style X-axis,
		boolean bEnabled = !( getAxisForProcessing( ).isCategoryAxis( ) || sType.equals( AxisType.TEXT_LITERAL.getName( ) ) );
		lblMin.setEnabled( bEnabled );
		txtScaleMin.setEnabled( bEnabled );
		lblMax.setEnabled( bEnabled );
		txtScaleMax.setEnabled( bEnabled );
		lblStep.setEnabled( bEnabled );
		txtScaleStep.setEnabled( bEnabled );

		// lblUnit.setEnabled( sType.equals( "DateTime" ) ); //$NON-NLS-1$
		// cmbScaleUnit.setEnabled( sType.equals( "DateTime" ) ); //$NON-NLS-1$
		lblStep.setEnabled( bEnabled
				&& !sType.equals( AxisType.DATE_TIME_LITERAL.getName( ) ) );
		txtScaleStep.setEnabled( bEnabled
				&& !sType.equals( AxisType.DATE_TIME_LITERAL.getName( ) ) );
	}

	private String getValue( DataElement de )
	{
		if ( de instanceof DateTimeDataElement )
		{
			Date dt = ( (DateTimeDataElement) de ).getValueAsCalendar( )
					.getTime( );
			SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy", Locale.getDefault( ) ); //$NON-NLS-1$
			return sdf.format( dt );
		}
		else if ( de instanceof NumberDataElement )
		{
			return String.valueOf( ( (NumberDataElement) de ).getValue( ) );
		}
		return ""; //$NON-NLS-1$
	}

	private DataElement getTypedDataElement( String strDataElement )
	{
		SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy", Locale.getDefault( ) ); //$NON-NLS-1$
		NumberFormat nf = NumberFormat.getNumberInstance( Locale.getDefault( ) );
		try
		{
			// First try Date
			Date dateElement = sdf.parse( strDataElement );
			Calendar cal = Calendar.getInstance( TimeZone.getDefault( ),
					Locale.getDefault( ) );
			cal.setTime( dateElement );
			return DateTimeDataElementImpl.create( cal );
		}
		catch ( ParseException e )
		{
			// Next try double
			try
			{
				Number numberElement = nf.parse( strDataElement );
				return NumberDataElementImpl.create( numberElement.doubleValue( ) );
			}
			catch ( ParseException e1 )
			{
				return null;
			}
		}
	}

	private void convertSampleData( )
	{
		for ( int i = 0; i < chart.getSampleData( ).getBaseSampleData( ).size( ); i++ )
		{
			( (BaseSampleData) chart.getSampleData( )
					.getBaseSampleData( )
					.get( i ) ).setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( getAxisForProcessing( ).getType( ),
					( (BaseSampleData) chart.getSampleData( )
							.getBaseSampleData( )
							.get( i ) ).getDataSetRepresentation( ) ) );
		}
	}

	private Axis getAxisForProcessing( )
	{
		return axis;
	}

	private boolean isAxisX( )
	{
		// Remove type selection UI
		return false;
		// return ChartUIUtil.getAxisXForProcessing( (ChartWithAxes) chart ) ==
		// getAxisForProcessing( );
	}
}