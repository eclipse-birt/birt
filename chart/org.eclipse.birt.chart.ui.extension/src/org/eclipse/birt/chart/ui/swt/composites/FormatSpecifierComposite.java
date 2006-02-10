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

import java.text.MessageFormat;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.DateFormatDetail;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.DateFormatType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

/**
 * @author Actuate Corporation
 * 
 */
public class FormatSpecifierComposite extends Composite implements
		SelectionListener,
		Listener,
		ModifyListener
{

	private transient Button btnUndefined = null;

	private transient Button btnStandard = null;

	private transient Button btnAdvanced = null;

	private transient Combo cmbDataType = null;

	// Composites for Standard Properties
	private transient Composite cmpStandardDetails = null;

	private transient StackLayout slStandardDetails = null;

	private transient Composite cmpStandardDateDetails = null;

	private transient Composite cmpDateStandard = null;

	private transient Combo cmbDateType = null;

	private transient Combo cmbDateForm = null;

	private transient Composite cmpStandardNumberDetails = null;

	private transient Composite cmpNumberStandard = null;

	private transient Text txtPrefix = null;

	private transient Text txtSuffix = null;

	private transient Text txtMultiplier = null;

	private transient IntegerSpinControl iscFractionDigits = null;

	// Composites for Advanced Properties
	private transient Composite cmpAdvancedDetails = null;

	private transient StackLayout slAdvancedDetails = null;

	private transient Composite cmpAdvancedDateDetails = null;

	private transient Composite cmpDateAdvanced = null;

	private transient Text txtDatePattern = null;

	private transient Composite cmpAdvancedNumberDetails = null;

	private transient Composite cmpNumberAdvanced = null;

	private transient Text txtNumberPattern = null;

	private transient Text txtAdvMultiplier = null;

	private transient FormatSpecifier formatspecifier = null;

	private transient boolean bEnableEvents = true;

	/**
	 * @param parent
	 * @param style
	 */
	public FormatSpecifierComposite( Composite parent, int style,
			FormatSpecifier formatspecifier )
	{
		super( parent, style );
		this.formatspecifier = formatspecifier;
		init( );
		placeComponents( );
	}

	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
	}

	private void placeComponents( )
	{
		// Layout for the content composite
		GridLayout glContent = new GridLayout( );
		glContent.numColumns = 2;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;

		// Layout for the details composite
		slStandardDetails = new StackLayout( );

		// Layout for the details composite
		slAdvancedDetails = new StackLayout( );

		this.setLayout( glContent );

		Label lblDataType = new Label( this, SWT.NONE );
		GridData gdLBLDataType = new GridData( );
		lblDataType.setLayoutData( gdLBLDataType );
		lblDataType.setText( Messages.getString( "FormatSpecifierComposite.Lbl.DataType" ) ); //$NON-NLS-1$

		cmbDataType = new Combo( this, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBDataType = new GridData( GridData.FILL_HORIZONTAL );
		cmbDataType.setLayoutData( gdCMBDataType );
		cmbDataType.addSelectionListener( this );

		btnUndefined = new Button( this, SWT.RADIO );
		GridData gdBTNUndefined = new GridData( GridData.FILL_HORIZONTAL );
		gdBTNUndefined.horizontalSpan = 2;
		btnUndefined.setLayoutData( gdBTNUndefined );
		btnUndefined.setText( Messages.getString( "FormatSpecifierComposite.Lbl.Undefined" ) ); //$NON-NLS-1$
		btnUndefined.addSelectionListener( this );

		Label lblDummyStandard = new Label( this, SWT.NONE );
		GridData gdLBLDummyStandard = new GridData( );
		gdLBLDummyStandard.horizontalSpan = 2;
		gdLBLDummyStandard.heightHint = 10;
		lblDummyStandard.setLayoutData( gdLBLDummyStandard );

		btnStandard = new Button( this, SWT.RADIO );
		GridData gdBTNStandard = new GridData( GridData.FILL_HORIZONTAL );
		gdBTNStandard.horizontalSpan = 2;
		btnStandard.setLayoutData( gdBTNStandard );
		btnStandard.setText( Messages.getString( "FormatSpecifierComposite.Lbl.Standard" ) ); //$NON-NLS-1$
		btnStandard.addSelectionListener( this );

		cmpStandardDetails = new Composite( this, SWT.NONE );
		GridData gdCMPStandardDetails = new GridData( GridData.FILL_BOTH );
		gdCMPStandardDetails.horizontalIndent = 16;
		gdCMPStandardDetails.horizontalSpan = 2;
		cmpStandardDetails.setLayoutData( gdCMPStandardDetails );
		cmpStandardDetails.setLayout( slStandardDetails );

		// Date/Time details Composite
		GridLayout glDate = new GridLayout( );
		glDate.verticalSpacing = 5;
		glDate.marginHeight = 0;
		glDate.marginWidth = 0;

		cmpStandardDateDetails = new Composite( cmpStandardDetails, SWT.NONE );
		cmpStandardDateDetails.setLayout( glDate );

		// Date/Time Standard Composite
		// Layout
		GridLayout glDateStandard = new GridLayout( );
		glDateStandard.verticalSpacing = 5;
		glDateStandard.numColumns = 2;
		glDateStandard.marginHeight = 2;
		glDateStandard.marginWidth = 2;

		cmpDateStandard = new Composite( cmpStandardDateDetails, SWT.NONE );
		GridData gdGRPDateStandard = new GridData( GridData.FILL_BOTH );
		cmpDateStandard.setLayoutData( gdGRPDateStandard );
		cmpDateStandard.setLayout( glDateStandard );

		Label lblDateType = new Label( cmpDateStandard, SWT.NONE );
		GridData gdLBLDateType = new GridData( );
		lblDateType.setLayoutData( gdLBLDateType );
		lblDateType.setText( Messages.getString( "FormatSpecifierComposite.Lbl.Type" ) ); //$NON-NLS-1$

		cmbDateType = new Combo( cmpDateStandard, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBDateType = new GridData( GridData.FILL_HORIZONTAL );
		cmbDateType.setLayoutData( gdCMBDateType );
		cmbDateType.addSelectionListener( this );

		Label lblDateDetails = new Label( cmpDateStandard, SWT.NONE );
		GridData gdLBLDateDetails = new GridData( );
		lblDateDetails.setLayoutData( gdLBLDateDetails );
		lblDateDetails.setText( Messages.getString( "FormatSpecifierComposite.Lbl.Details" ) ); //$NON-NLS-1$

		cmbDateForm = new Combo( cmpDateStandard, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBDateForm = new GridData( GridData.FILL_HORIZONTAL );
		cmbDateForm.setLayoutData( gdCMBDateForm );
		cmbDateForm.addSelectionListener( this );

		// Number details Composite
		GridLayout glNumber = new GridLayout( );
		glNumber.verticalSpacing = 5;
		glNumber.marginHeight = 0;
		glNumber.marginWidth = 0;

		cmpStandardNumberDetails = new Composite( cmpStandardDetails, SWT.NONE );
		cmpStandardNumberDetails.setLayout( glNumber );

		// Number Standard Composite
		// Layout
		GridLayout glNumberStandard = new GridLayout( );
		glNumberStandard.verticalSpacing = 5;
		glNumberStandard.numColumns = 4;
		glNumberStandard.marginHeight = 2;
		glNumberStandard.marginWidth = 2;

		cmpNumberStandard = new Composite( cmpStandardNumberDetails, SWT.NONE );
		GridData gdGRPNumberStandard = new GridData( GridData.FILL_BOTH );
		cmpNumberStandard.setLayoutData( gdGRPNumberStandard );
		cmpNumberStandard.setLayout( glNumberStandard );

		Label lblPrefix = new Label( cmpNumberStandard, SWT.NONE );
		GridData gdLBLPrefix = new GridData( );
		lblPrefix.setLayoutData( gdLBLPrefix );
		lblPrefix.setText( Messages.getString( "FormatSpecifierComposite.Lbl.Prefix" ) ); //$NON-NLS-1$

		txtPrefix = new Text( cmpNumberStandard, SWT.BORDER | SWT.SINGLE );
		GridData gdTXTPrefix = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTPrefix.widthHint = 60;
		txtPrefix.setLayoutData( gdTXTPrefix );
		txtPrefix.addModifyListener( this );

		Label lblSuffix = new Label( cmpNumberStandard, SWT.NONE );
		GridData gdLBLSuffix = new GridData( );
		lblSuffix.setLayoutData( gdLBLSuffix );
		lblSuffix.setText( Messages.getString( "FormatSpecifierComposite.Lbl.Suffix" ) ); //$NON-NLS-1$

		txtSuffix = new Text( cmpNumberStandard, SWT.BORDER | SWT.SINGLE );
		GridData gdTXTSuffix = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTSuffix.widthHint = 60;
		txtSuffix.setLayoutData( gdTXTSuffix );
		txtSuffix.addModifyListener( this );

		Label lblMultiplier = new Label( cmpNumberStandard, SWT.NONE );
		GridData gdLBLMultiplier = new GridData( );
		lblMultiplier.setLayoutData( gdLBLMultiplier );
		lblMultiplier.setText( Messages.getString( "FormatSpecifierComposite.Lbl.Multiplier" ) ); //$NON-NLS-1$

		txtMultiplier = new Text( cmpNumberStandard, SWT.BORDER | SWT.SINGLE );
		GridData gdTXTMultiplier = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTMultiplier.widthHint = 60;
		txtMultiplier.setLayoutData( gdTXTMultiplier );
		txtMultiplier.addModifyListener( this );

		Label lblFractionDigit = new Label( cmpNumberStandard, SWT.NONE );
		GridData gdLBLFractionDigit = new GridData( );
		lblFractionDigit.setLayoutData( gdLBLFractionDigit );
		lblFractionDigit.setText( Messages.getString( "FormatSpecifierComposite.Lbl.FractionDigits" ) ); //$NON-NLS-1$

		iscFractionDigits = new IntegerSpinControl( cmpNumberStandard,
				SWT.NONE,
				2 );
		GridData gdISCFractionDigits = new GridData( GridData.FILL_HORIZONTAL );
		gdISCFractionDigits.widthHint = 60;
		iscFractionDigits.setLayoutData( gdISCFractionDigits );
		iscFractionDigits.addListener( this );

		Label lblDummyAdvanced = new Label( this, SWT.NONE );
		GridData gdLBLDummyAdvanced = new GridData( );
		gdLBLDummyAdvanced.horizontalSpan = 2;
		gdLBLDummyAdvanced.heightHint = 10;
		lblDummyAdvanced.setLayoutData( gdLBLDummyAdvanced );

		btnAdvanced = new Button( this, SWT.RADIO );
		GridData gdBTNAdvanced = new GridData( GridData.FILL_HORIZONTAL );
		gdBTNAdvanced.horizontalSpan = 2;
		btnAdvanced.setLayoutData( gdBTNAdvanced );
		btnAdvanced.setText( Messages.getString( "FormatSpecifierComposite.Lbl.Advanced" ) ); //$NON-NLS-1$
		btnAdvanced.addSelectionListener( this );

		cmpAdvancedDetails = new Composite( this, SWT.NONE );
		GridData gdCMPAdvancedDetails = new GridData( GridData.FILL_BOTH );
		gdCMPAdvancedDetails.horizontalIndent = 16;
		gdCMPAdvancedDetails.horizontalSpan = 2;
		cmpAdvancedDetails.setLayoutData( gdCMPAdvancedDetails );
		cmpAdvancedDetails.setLayout( slAdvancedDetails );

		// Date/Time details Composite
		GridLayout glAdvDate = new GridLayout( );
		glAdvDate.verticalSpacing = 5;
		glAdvDate.marginHeight = 0;
		glAdvDate.marginWidth = 0;

		cmpAdvancedDateDetails = new Composite( cmpAdvancedDetails, SWT.NONE );
		cmpAdvancedDateDetails.setLayout( glAdvDate );

		// Date/Time Advanced Composite
		// Layout
		GridLayout glDateAdvanced = new GridLayout( );
		glDateAdvanced.verticalSpacing = 5;
		glDateAdvanced.numColumns = 2;
		glDateAdvanced.marginHeight = 2;
		glDateAdvanced.marginWidth = 2;

		cmpDateAdvanced = new Composite( cmpAdvancedDateDetails, SWT.NONE );
		GridData gdGRPDateAdvanced = new GridData( GridData.FILL_BOTH );
		cmpDateAdvanced.setLayoutData( gdGRPDateAdvanced );
		cmpDateAdvanced.setLayout( glDateAdvanced );

		Label lblDatePattern = new Label( cmpDateAdvanced, SWT.NONE );
		GridData gdLBLDatePattern = new GridData( );
		lblDatePattern.setLayoutData( gdLBLDatePattern );
		lblDatePattern.setText( Messages.getString( "FormatSpecifierComposite.Lbl.DatePattern" ) ); //$NON-NLS-1$

		txtDatePattern = new Text( cmpDateAdvanced, SWT.BORDER | SWT.SINGLE );
		GridData gdTXTDatePattern = new GridData( GridData.FILL_HORIZONTAL );
		txtDatePattern.setLayoutData( gdTXTDatePattern );
		txtDatePattern.addModifyListener( this );

		// Number details Composite
		GridLayout glAdvNumber = new GridLayout( );
		glAdvNumber.verticalSpacing = 5;
		glAdvNumber.marginHeight = 0;
		glAdvNumber.marginWidth = 0;

		cmpAdvancedNumberDetails = new Composite( cmpAdvancedDetails, SWT.NONE );
		cmpAdvancedNumberDetails.setLayout( glAdvNumber );

		// Number Advanced Composite
		// Layout
		GridLayout glNumberAdvanced = new GridLayout( );
		glNumberAdvanced.verticalSpacing = 5;
		glNumberAdvanced.numColumns = 2;
		glNumberAdvanced.marginHeight = 2;
		glNumberAdvanced.marginWidth = 2;

		cmpNumberAdvanced = new Composite( cmpAdvancedNumberDetails, SWT.NONE );
		GridData gdGRPNumberAdvanced = new GridData( GridData.FILL_BOTH );
		cmpNumberAdvanced.setLayoutData( gdGRPNumberAdvanced );
		cmpNumberAdvanced.setLayout( glNumberAdvanced );

		Label lblAdvMultiplier = new Label( cmpNumberAdvanced, SWT.NONE );
		GridData gdLBLAdvMultiplier = new GridData( );
		lblAdvMultiplier.setLayoutData( gdLBLAdvMultiplier );
		lblAdvMultiplier.setText( Messages.getString( "FormatSpecifierComposite.Lbl.Multiplier" ) ); //$NON-NLS-1$

		txtAdvMultiplier = new Text( cmpNumberAdvanced, SWT.BORDER | SWT.SINGLE );
		GridData gdTXTAdvMultiplier = new GridData( GridData.FILL_HORIZONTAL );
		txtAdvMultiplier.setLayoutData( gdTXTAdvMultiplier );
		txtAdvMultiplier.addModifyListener( this );

		Label lblNumberPattern = new Label( cmpNumberAdvanced, SWT.NONE );
		GridData gdLBLNumberPattern = new GridData( );
		lblNumberPattern.setLayoutData( gdLBLNumberPattern );
		lblNumberPattern.setText( Messages.getString( "FormatSpecifierComposite.Lbl.NumberPattern" ) ); //$NON-NLS-1$

		txtNumberPattern = new Text( cmpNumberAdvanced, SWT.BORDER | SWT.SINGLE );
		GridData gdTXTNumberPattern = new GridData( GridData.FILL_HORIZONTAL );
		txtNumberPattern.setLayoutData( gdTXTNumberPattern );
		txtNumberPattern.addModifyListener( this );

		populateLists( );
	}

	private void populateLists( )
	{
		this.bEnableEvents = false;
		cmbDataType.add( "Number" ); //$NON-NLS-1$
		cmbDataType.add( "Date/Time" ); //$NON-NLS-1$

		if ( formatspecifier == null )
		{
			cmbDataType.select( 0 );
			btnUndefined.setSelection( true );
			slStandardDetails.topControl = this.cmpStandardNumberDetails;
			slAdvancedDetails.topControl = this.cmpAdvancedNumberDetails;
		}
		else if ( formatspecifier instanceof DateFormatSpecifier
				|| formatspecifier instanceof JavaDateFormatSpecifier )
		{
			cmbDataType.select( 1 );
			if ( formatspecifier instanceof DateFormatSpecifier )
			{
				btnStandard.setSelection( true );
			}
			else if ( formatspecifier instanceof JavaDateFormatSpecifier )
			{
				btnAdvanced.setSelection( true );
			}
			else
			{
				btnUndefined.setSelection( true );
			}
			slStandardDetails.topControl = this.cmpStandardDateDetails;
			slAdvancedDetails.topControl = this.cmpAdvancedDateDetails;
		}
		else
		{
			cmbDataType.select( 0 );
			if ( formatspecifier instanceof NumberFormatSpecifier )
			{
				btnStandard.setSelection( true );
			}
			else if ( formatspecifier instanceof JavaNumberFormatSpecifier )
			{
				btnAdvanced.setSelection( true );
			}
			else
			{
				btnUndefined.setSelection( true );
			}
			slStandardDetails.topControl = this.cmpStandardNumberDetails;
			slAdvancedDetails.topControl = this.cmpAdvancedNumberDetails;
		}
		updateUIState( );

		// Populate Date Types
		NameSet ns = LiteralHelper.dateFormatTypeSet;
		cmbDateType.setItems( ns.getDisplayNames( ) );
		if ( formatspecifier instanceof DateFormatSpecifier )
		{
			cmbDateType.select( ns.getSafeNameIndex( ( (DateFormatSpecifier) formatspecifier ).getType( )
					.getName( ) ) );
		}
		if ( cmbDateType.getSelectionIndex( ) == -1 )
		{
			cmbDateType.select( 0 );
		}

		// Populate Date Details
		ns = LiteralHelper.dateFormatDetailSet;
		cmbDateForm.setItems( ns.getDisplayNames( ) );
		if ( formatspecifier instanceof DateFormatSpecifier )
		{
			cmbDateForm.select( ns.getSafeNameIndex( ( (DateFormatSpecifier) formatspecifier ).getDetail( )
					.getName( ) ) );
		}
		if ( cmbDateForm.getSelectionIndex( ) == -1 )
		{
			cmbDateForm.select( 0 );
		}

		String str = ""; //$NON-NLS-1$
		if ( formatspecifier instanceof JavaDateFormatSpecifier )
		{
			str = ( (JavaDateFormatSpecifier) formatspecifier ).getPattern( );
			if ( str == null )
			{
				str = ""; //$NON-NLS-1$
			}
			txtDatePattern.setText( str );
		}
		if ( formatspecifier instanceof NumberFormatSpecifier )
		{
			str = ( (NumberFormatSpecifier) formatspecifier ).getPrefix( );
			if ( str == null )
			{
				str = ""; //$NON-NLS-1$
			}
			txtPrefix.setText( str );
			str = ( (NumberFormatSpecifier) formatspecifier ).getSuffix( );
			if ( str == null )
			{
				str = ""; //$NON-NLS-1$
			}
			txtSuffix.setText( str );
			str = String.valueOf( ( (NumberFormatSpecifier) formatspecifier ).getMultiplier( ) );
			if ( str == null
					|| !( (NumberFormatSpecifier) formatspecifier ).eIsSet( AttributePackage.eINSTANCE.getNumberFormatSpecifier_Multiplier( ) ) )
			{
				str = ""; //$NON-NLS-1$
			}
			txtMultiplier.setText( str );
			iscFractionDigits.setValue( ( (NumberFormatSpecifier) formatspecifier ).getFractionDigits( ) );
		}
		if ( formatspecifier instanceof JavaNumberFormatSpecifier )
		{
			str = String.valueOf( ( (JavaNumberFormatSpecifier) formatspecifier ).getMultiplier( ) );
			if ( str == null
					|| !( (JavaNumberFormatSpecifier) formatspecifier ).eIsSet( AttributePackage.eINSTANCE.getJavaNumberFormatSpecifier_Multiplier( ) ) )
			{
				str = ""; //$NON-NLS-1$
			}
			txtAdvMultiplier.setText( str );
			str = ( (JavaNumberFormatSpecifier) formatspecifier ).getPattern( );
			if ( str == null )
			{
				str = ""; //$NON-NLS-1$
			}
			txtNumberPattern.setText( str );
		}
		this.layout( );
		this.bEnableEvents = true;
	}

	public FormatSpecifier getFormatSpecifier( )
	{
		if ( this.btnUndefined.getSelection( ) )
		{
			return null;
		}
		// Build (or set) the format specifier instance
		formatspecifier = buildFormatSpecifier( );
		return this.formatspecifier;
	}

	private void handleFormatError( String val )
	{
		MessageBox mbox = new MessageBox( this.getShell( ), SWT.ICON_WARNING
				| SWT.OK );
		mbox.setText( Messages.getString( "FormatSpecifierComposite.error.Title" ) ); //$NON-NLS-1$
		mbox.setMessage( MessageFormat.format( Messages.getString( "FormatSpecifierComposite.error.Message" ), //$NON-NLS-1$
				new Object[]{
					val
				} ) );

		mbox.open( );
	}

	private FormatSpecifier buildFormatSpecifier( )
	{
		FormatSpecifier fs = null;
		if ( cmbDataType.getText( ).equals( "Date/Time" ) ) //$NON-NLS-1$
		{
			if ( this.btnAdvanced.getSelection( ) )
			{
				fs = JavaDateFormatSpecifierImpl.create( txtDatePattern.getText( ) );
			}
			else if ( this.btnStandard.getSelection( ) )
			{
				fs = AttributeFactory.eINSTANCE.createDateFormatSpecifier( );
				( (DateFormatSpecifier) fs ).setType( DateFormatType.getByName( LiteralHelper.dateFormatTypeSet.getNameByDisplayName( cmbDateType.getText( ) ) ) );
				( (DateFormatSpecifier) fs ).setDetail( DateFormatDetail.getByName( LiteralHelper.dateFormatDetailSet.getNameByDisplayName( cmbDateForm.getText( ) ) ) );
			}
		}
		else
		{
			if ( this.btnAdvanced.getSelection( ) )
			{
				fs = JavaNumberFormatSpecifierImpl.create( txtNumberPattern.getText( ) );
				if ( txtAdvMultiplier.getText( ).length( ) > 0 )
				{
					try
					{
						( (JavaNumberFormatSpecifierImpl) fs ).setMultiplier( Double.valueOf( txtAdvMultiplier.getText( ) )
								.doubleValue( ) );
					}
					catch ( NumberFormatException e )
					{
						handleFormatError( txtAdvMultiplier.getText( ) );
					}
				}
			}
			else if ( this.btnStandard.getSelection( ) )
			{
				fs = NumberFormatSpecifierImpl.create( );
				( (NumberFormatSpecifier) fs ).setPrefix( txtPrefix.getText( ) );
				( (NumberFormatSpecifier) fs ).setSuffix( txtSuffix.getText( ) );
				( (NumberFormatSpecifier) fs ).setFractionDigits( iscFractionDigits.getValue( ) );
				if ( txtMultiplier.getText( ).length( ) > 0 )
				{
					try
					{
						( (NumberFormatSpecifier) fs ).setMultiplier( Double.valueOf( txtMultiplier.getText( ) )
								.doubleValue( ) );
					}
					catch ( NumberFormatException e )
					{
						handleFormatError( txtMultiplier.getText( ) );
					}
				}
			}
		}
		return fs;
	}

	private void updateUIState( )
	{
		if ( cmbDataType.getText( ).equals( "Number" ) ) //$NON-NLS-1$
		{
			if ( this.btnStandard.getSelection( ) )
			{
				// Enable Standard properties for number
				this.txtPrefix.setEnabled( true );
				this.txtSuffix.setEnabled( true );
				this.txtMultiplier.setEnabled( true );
				this.iscFractionDigits.setEnabled( true );

				// Disable Advanced properties for number
				this.txtAdvMultiplier.setEnabled( false );
				this.txtNumberPattern.setEnabled( false );
			}
			else if ( this.btnAdvanced.getSelection( ) )
			{
				// Disable Standard properties for number
				this.txtPrefix.setEnabled( false );
				this.txtSuffix.setEnabled( false );
				this.txtMultiplier.setEnabled( false );
				this.iscFractionDigits.setEnabled( false );

				// Enable Standard properties for number
				this.txtAdvMultiplier.setEnabled( true );
				this.txtNumberPattern.setEnabled( true );
			}
			else
			{
				// Disable both Standard and Advanced properties
				this.txtPrefix.setEnabled( false );
				this.txtSuffix.setEnabled( false );
				this.txtMultiplier.setEnabled( false );
				this.iscFractionDigits.setEnabled( false );

				this.txtAdvMultiplier.setEnabled( false );
				this.txtNumberPattern.setEnabled( false );
			}
		}
		else
		{
			if ( this.btnStandard.getSelection( ) )
			{
				// Enable Standard properties for date
				this.cmbDateForm.setEnabled( true );
				this.cmbDateType.setEnabled( true );

				// Disable Standard properties for date
				this.txtDatePattern.setEnabled( false );
			}
			else if ( this.btnAdvanced.getSelection( ) )
			{
				// Disable Standard properties for date
				this.cmbDateForm.setEnabled( false );
				this.cmbDateType.setEnabled( false );

				// Enable Standard properties for date
				this.txtDatePattern.setEnabled( true );
			}
			else
			{
				// Disable both Standard and Advanced properties
				this.cmbDateForm.setEnabled( false );
				this.cmbDateType.setEnabled( false );

				this.txtDatePattern.setEnabled( false );
			}
		}
	}

	/**
	 * @return A preferred size for this composite when used in a layout
	 */
	public Point getPreferredSize( )
	{
		return new Point( 200, 150 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( !bEnableEvents )
		{
			return;
		}
		if ( e.getSource( ).equals( cmbDataType ) )
		{
			if ( cmbDataType.getText( ).equals( "Number" ) ) //$NON-NLS-1$
			{
				slStandardDetails.topControl = cmpStandardNumberDetails;
				slAdvancedDetails.topControl = cmpAdvancedNumberDetails;
				updateUIState( );
			}
			else
			{
				slStandardDetails.topControl = cmpStandardDateDetails;
				slAdvancedDetails.topControl = cmpAdvancedDateDetails;
				updateUIState( );
			}
			cmpStandardDetails.layout( );
			cmpAdvancedDetails.layout( );
		}
		else if ( e.getSource( ) instanceof Button )
		{
			updateUIState( );
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
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText( ModifyEvent e )
	{
		Object oSource = e.getSource( );
		this.bEnableEvents = false;
		if ( oSource.equals( txtDatePattern ) )
		{
			if ( !( formatspecifier instanceof JavaDateFormatSpecifier ) )
			{
				formatspecifier = JavaDateFormatSpecifierImpl.create( "" ); //$NON-NLS-1$
			}
			( (JavaDateFormatSpecifier) formatspecifier ).setPattern( txtDatePattern.getText( ) );
		}
		else if ( oSource.equals( txtPrefix ) )
		{
			if ( !( formatspecifier instanceof NumberFormatSpecifier ) )
			{
				formatspecifier = NumberFormatSpecifierImpl.create( );
				( (NumberFormatSpecifier) formatspecifier ).setSuffix( txtSuffix.getText( ) );
				( (NumberFormatSpecifier) formatspecifier ).setFractionDigits( iscFractionDigits.getValue( ) );
				try
				{
					String str = txtMultiplier.getText( );
					if ( str.length( ) > 0 )
					{
						( (NumberFormatSpecifier) formatspecifier ).setMultiplier( new Double( str ).doubleValue( ) );
					}
					else
					{
						( (NumberFormatSpecifier) formatspecifier ).eUnset( AttributePackage.eINSTANCE.getNumberFormatSpecifier_Multiplier( ) );
					}
				}
				catch ( NumberFormatException e1 )
				{
					handleFormatError( txtMultiplier.getText( ) );
				}
			}
			( (NumberFormatSpecifier) formatspecifier ).setPrefix( txtPrefix.getText( ) );
		}
		else if ( oSource.equals( txtSuffix ) )
		{
			if ( !( formatspecifier instanceof NumberFormatSpecifier ) )
			{
				formatspecifier = NumberFormatSpecifierImpl.create( );
				( (NumberFormatSpecifier) formatspecifier ).setPrefix( txtPrefix.getText( ) );
				( (NumberFormatSpecifier) formatspecifier ).setFractionDigits( iscFractionDigits.getValue( ) );
				try
				{
					String str = txtMultiplier.getText( );
					if ( str.length( ) > 0 )
					{
						( (NumberFormatSpecifier) formatspecifier ).setMultiplier( new Double( str ).doubleValue( ) );
					}
					else
					{
						( (NumberFormatSpecifier) formatspecifier ).eUnset( AttributePackage.eINSTANCE.getNumberFormatSpecifier_Multiplier( ) );
					}
				}
				catch ( NumberFormatException e1 )
				{
					handleFormatError( txtMultiplier.getText( ) );
				}
			}
			( (NumberFormatSpecifier) formatspecifier ).setSuffix( txtSuffix.getText( ) );
		}
		else if ( oSource.equals( txtMultiplier ) )
		{
			if ( !( formatspecifier instanceof NumberFormatSpecifier ) )
			{
				formatspecifier = NumberFormatSpecifierImpl.create( );
				( (NumberFormatSpecifier) formatspecifier ).setPrefix( txtPrefix.getText( ) );
				( (NumberFormatSpecifier) formatspecifier ).setSuffix( txtSuffix.getText( ) );
				( (NumberFormatSpecifier) formatspecifier ).setFractionDigits( iscFractionDigits.getValue( ) );
			}
			try
			{
				if ( "".equals( txtMultiplier.getText( ) ) ) //$NON-NLS-1$
				{
					( (NumberFormatSpecifier) formatspecifier ).eUnset( AttributePackage.eINSTANCE.getNumberFormatSpecifier_Multiplier( ) );
				}
				else
				{
					( (NumberFormatSpecifier) formatspecifier ).setMultiplier( new Double( txtMultiplier.getText( ) ).doubleValue( ) );
				}
			}
			catch ( NumberFormatException e1 )
			{
				handleFormatError( txtMultiplier.getText( ) );
			}
		}
		else if ( oSource.equals( txtAdvMultiplier ) )
		{
			if ( !( formatspecifier instanceof JavaNumberFormatSpecifier ) )
			{
				formatspecifier = JavaNumberFormatSpecifierImpl.create( txtNumberPattern.getText( ) );
			}
			try
			{
				if ( "".equals( txtAdvMultiplier.getText( ) ) ) //$NON-NLS-1$
				{
					( (JavaNumberFormatSpecifier) formatspecifier ).eUnset( AttributePackage.eINSTANCE.getJavaNumberFormatSpecifier_Multiplier( ) );
				}
				else
				{
					( (JavaNumberFormatSpecifier) formatspecifier ).setMultiplier( new Double( txtAdvMultiplier.getText( ) ).doubleValue( ) );
				}
			}
			catch ( NumberFormatException e1 )
			{
				handleFormatError( txtAdvMultiplier.getText( ) );
			}
		}
		else if ( oSource.equals( txtNumberPattern ) )
		{
			if ( !( formatspecifier instanceof JavaNumberFormatSpecifier ) )
			{
				formatspecifier = JavaNumberFormatSpecifierImpl.create( "" ); //$NON-NLS-1$
			}
			( (JavaNumberFormatSpecifier) formatspecifier ).setPattern( txtNumberPattern.getText( ) );
		}
		this.bEnableEvents = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		this.bEnableEvents = false;
		if ( event.widget.equals( iscFractionDigits ) )
		{
			if ( !( formatspecifier instanceof NumberFormatSpecifier ) )
			{
				formatspecifier = NumberFormatSpecifierImpl.create( );
				( (NumberFormatSpecifier) formatspecifier ).setPrefix( txtPrefix.getText( ) );
				( (NumberFormatSpecifier) formatspecifier ).setSuffix( txtSuffix.getText( ) );
				try
				{
					String str = txtMultiplier.getText( );
					if ( str.length( ) > 0 )
					{
						( (NumberFormatSpecifier) formatspecifier ).setMultiplier( new Double( str ).doubleValue( ) );
					}
				}
				catch ( NumberFormatException e1 )
				{
					handleFormatError( txtMultiplier.getText( ) );
				}
			}
			( (NumberFormatSpecifier) formatspecifier ).setFractionDigits( ( (Integer) event.data ).intValue( ) );
		}
		this.bEnableEvents = true;
	}

}