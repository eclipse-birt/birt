/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup;

import org.eclipse.birt.chart.model.attribute.ScaleUnitType;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.DateTimeDataElementComposite;
import org.eclipse.birt.chart.ui.swt.composites.NumberDataElementComposite;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataElementComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;

/**
 * 
 */

public abstract class AbstractScaleSheet extends AbstractPopupSheet
		implements
			Listener
{

	protected Label lblMin = null;

	protected IDataElementComposite txtScaleMin = null;

	protected Label lblMax = null;

	protected IDataElementComposite txtScaleMax = null;

	protected Button btnStepSize = null;

	protected Button btnStepNumber = null;

	protected Button btnStepAuto = null;

	protected Button btnFactor = null;

	protected TextEditorComposite txtFactor = null;

	protected TextEditorComposite txtStepSize = null;

	protected Label lblUnit = null;

	protected Combo cmbScaleUnit = null;

	protected Label lblStepNumber = null;

	protected Spinner spnStepNumber = null;

	protected Button btnShowOutside = null;

	protected Button btnAutoExpand;

	public AbstractScaleSheet( String title, ChartWizardContext context )
	{
		super( title, context, true );
	}

	protected Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_AXIS_SCALE );

		Composite cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( 4, false );
			glContent.marginHeight = 10;
			glContent.marginWidth = 10;
			glContent.horizontalSpacing = 5;
			glContent.verticalSpacing = 10;
			cmpContent.setLayout( glContent );
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
			grpScale.setText( Messages.getString( "AbstractScaleSheet.Label.Step" ) ); //$NON-NLS-1$
		}

		btnStepAuto = new Button( grpScale, SWT.RADIO );
		{
			GridData gd = new GridData( );
			gd.horizontalSpan = 4;
			btnStepAuto.setLayoutData( gd );
			btnStepAuto.setText( Messages.getString( "AbstractScaleSheet.Label.Auto" ) ); //$NON-NLS-1$
			btnStepAuto.addListener( SWT.Selection, this );
		}

		btnStepSize = new Button( grpScale, SWT.RADIO );
		{
			btnStepSize.setText( Messages.getString( "AbstractScaleSheet.Label.StepSize" ) ); //$NON-NLS-1$
			btnStepSize.addListener( SWT.Selection, this );
		}

		txtStepSize = new TextEditorComposite( grpScale, SWT.BORDER
				| SWT.SINGLE, TextEditorComposite.TYPE_NUMBERIC );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.widthHint = 100;
			txtStepSize.setLayoutData( gd );
			String str = ""; //$NON-NLS-1$
			if ( getScale( ).isSetStep( ) )
			{
				str = String.valueOf( getScale( ).getStep( ) );
			}
			txtStepSize.setText( str );
			txtStepSize.addListener( this );
			txtStepSize.addListener( SWT.Modify, this );
			txtStepSize.setDefaultValue( "" ); //$NON-NLS-1$
		}

		lblUnit = new Label( grpScale, SWT.NONE );
		{
			lblUnit.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Unit" ) ); //$NON-NLS-1$
		}

		cmbScaleUnit = new Combo( grpScale, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			GridData gdCMBScaleUnit = new GridData( GridData.FILL_HORIZONTAL );
			cmbScaleUnit.setLayoutData( gdCMBScaleUnit );
			cmbScaleUnit.addListener( SWT.Selection, this );
			// Populate origin types combo
			NameSet ns = LiteralHelper.scaleUnitTypeSet;
			cmbScaleUnit.setItems( ns.getDisplayNames( ) );
			cmbScaleUnit.select( ns.getSafeNameIndex( getScale( ).getUnit( )
					.getName( ) ) );
		}

		btnStepNumber = new Button( grpScale, SWT.RADIO );
		{
			btnStepNumber.setText( Messages.getString( "AbstractScaleSheet.Label.StepNumber" ) ); //$NON-NLS-1$
			btnStepNumber.addListener( SWT.Selection, this );
		}

		spnStepNumber = new Spinner( grpScale, SWT.BORDER );
		{
			spnStepNumber.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			spnStepNumber.setMinimum( 1 );
			spnStepNumber.setMaximum( 100 );
			spnStepNumber.setSelection( getScale( ).getStepNumber( ) );
			spnStepNumber.addListener( SWT.Selection, this );
		}

		new Label( grpScale, SWT.NONE );
		new Label( grpScale, SWT.NONE );

		btnFactor = new Button( cmpContent, SWT.CHECK );
		{
			btnFactor.setText( Messages.getString("AbstractScaleSheet.Label.Factor") ); //$NON-NLS-1$
			GridData gd = new GridData( );
			gd.horizontalSpan = 2;
			btnFactor.setLayoutData( gd );
			btnFactor.addListener( SWT.Selection, this );
			if ( getScale( ).isSetFactor( ) )
			{
				btnFactor.setSelection( true );
			}
			else
			{
				btnFactor.setSelection( false );
			}
		}

		txtFactor = new TextEditorComposite( cmpContent, SWT.BORDER
				| SWT.SINGLE, TextEditorComposite.TYPE_NUMBERIC );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			// gd.widthHint = 100;
			gd.horizontalSpan = 2;
			txtFactor.setLayoutData( gd );
			String str = ""; //$NON-NLS-1$
			if ( getScale( ).isSetFactor( ) )
			{
				str = String.valueOf( getScale( ).getFactor( ) );
			}
			txtFactor.setText( str );
			txtFactor.addListener( this );
			txtFactor.addListener( SWT.Modify, this );
			txtFactor.setDefaultValue( "" ); //$NON-NLS-1$
		}

		lblMin = new Label( cmpContent, SWT.NONE );
		lblMin.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Minimum" ) ); //$NON-NLS-1$

		txtScaleMin = createValuePicker( cmpContent, getScale( ).getMin( ) );

		lblMax = new Label( cmpContent, SWT.NONE );
		lblMax.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Maximum" ) ); //$NON-NLS-1$

		txtScaleMax = createValuePicker( cmpContent, getScale( ).getMax( ) );

		btnAutoExpand = new Button( cmpContent, SWT.CHECK );
		{
			GridData gd = new GridData( );
			gd.horizontalSpan = 4;
			btnAutoExpand.setLayoutData( gd );
			btnAutoExpand.setSelection( getScale( ).isAutoExpand( ) );
			btnAutoExpand.setText( Messages.getString( "AbstractScaleSheet.AutoExpand" ) ); //$NON-NLS-1$
			btnAutoExpand.addListener( SWT.Selection, this );
		}

		btnShowOutside = new Button( cmpContent, SWT.CHECK );
		{
			GridData gd = new GridData( );
			gd.horizontalSpan = 4;
			btnShowOutside.setLayoutData( gd );
			btnShowOutside.setText( Messages.getString( "AbstractScaleSheet.Label.ShowValuesOutside" ) ); //$NON-NLS-1$
			btnShowOutside.setSelection( getScale( ).isShowOutside( ) );
			btnShowOutside.addListener( SWT.Selection, this );
			// Only visible in number type
			btnShowOutside.setVisible( getValueType( ) == TextEditorComposite.TYPE_NUMBERIC );
		}

		// Set checkbox selection.
		btnStepSize.setSelection( getScale( ).isSetStep( ) );
		if ( !btnStepSize.getSelection( ) )
		{
			if ( getValueType( ) != TextEditorComposite.TYPE_NUMBERIC )
			{
				btnStepAuto.setSelection( true );
			}
			else
			{
				// Only numeric value support step number
				btnStepNumber.setSelection( getScale( ).isSetStepNumber( ) );
				btnStepAuto.setSelection( !getScale( ).isSetStep( )
						&& !getScale( ).isSetStepNumber( ) );
			}
		}

		setState( );

		if ( getValueType( ) == TextEditorComposite.TYPE_DATETIME )
		{
			parent.getShell( ).addListener( SWT.Close, new Listener( ) {

				public void handleEvent( Event event )
				{
					if ( event.type == SWT.Close )
					{
						DataElement data = txtScaleMin.getDataElement( );
						if ( data == null )
						{
							getScale( ).eUnset( ComponentPackage.eINSTANCE.getScale_Min( ) );
						}
						else
						{
							getScale( ).setMin( data );
						}
						data = txtScaleMax.getDataElement( );
						if ( data == null )
						{
							getScale( ).eUnset( ComponentPackage.eINSTANCE.getScale_Max( ) );
						}
						else
						{
							getScale( ).setMax( data );
						}
						setState( );
					}
				}
			} );
		}

		return cmpContent;
	}

	protected void setState( boolean bEnabled )
	{
		btnStepSize.setEnabled( bEnabled );
		txtStepSize.setEnabled( bEnabled && btnStepSize.getSelection( ) );

		btnStepNumber.setEnabled( bEnabled
				&& getValueType( ) == TextEditorComposite.TYPE_NUMBERIC );
		spnStepNumber.setEnabled( bEnabled
				&& btnStepNumber.getSelection( )
				&& getValueType( ) == TextEditorComposite.TYPE_NUMBERIC );

		btnFactor.setEnabled( false );
		txtFactor.setEnabled( false );

		lblMin.setEnabled( bEnabled );
		lblMax.setEnabled( bEnabled );
		txtScaleMin.setEnabled( bEnabled );
		txtScaleMax.setEnabled( bEnabled );
		// Enabled only min or max has been set
		btnShowOutside.setEnabled( bEnabled
				&& ( getScale( ).eIsSet( ComponentPackage.eINSTANCE.getScale_Min( ) ) || getScale( ).eIsSet( ComponentPackage.eINSTANCE.getScale_Max( ) ) ) );

		lblUnit.setEnabled( bEnabled
				&& btnStepSize.getSelection( )
				&& getValueType( ) == TextEditorComposite.TYPE_DATETIME );
		cmbScaleUnit.setEnabled( bEnabled
				&& btnStepSize.getSelection( )
				&& getValueType( ) == TextEditorComposite.TYPE_DATETIME );
	}

	protected void setState( )
	{
		// Could be overriden if needed
		setState( true );
	}

	protected IDataElementComposite createValuePicker( Composite parent,
			DataElement data )
	{
		IDataElementComposite picker = null;

		if ( getValueType( ) == TextEditorComposite.TYPE_NUMBERIC
				|| getValueType( ) == TextEditorComposite.TYPE_NONE )
		{
			try
			{
				picker = new NumberDataElementComposite( parent,
						(NumberDataElement) data );
			}
			catch ( Exception e )
			{
				picker = new NumberDataElementComposite( parent, null );
			}
		}
		else if ( getValueType( ) == TextEditorComposite.TYPE_DATETIME )
		{
			try
			{
				picker = new DateTimeDataElementComposite( parent,
						SWT.BORDER,
						(DateTimeDataElement) data,
						true );
			}
			catch ( Exception e )
			{
				picker = new DateTimeDataElementComposite( parent,
						SWT.BORDER,
						null,
						true );
			}
		}

		if ( picker != null )
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = 3;
			picker.setLayoutData( gd );
			picker.addListener( this );
		}

		return picker;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( txtScaleMin ) )
		{
			DataElement data = txtScaleMin.getDataElement( );
			if ( data == null )
			{
				getScale( ).eUnset( ComponentPackage.eINSTANCE.getScale_Min( ) );
			}
			else
			{
				getScale( ).setMin( data );
			}
			setState( );
		}
		else if ( event.widget.equals( txtScaleMax ) )
		{
			DataElement data = txtScaleMax.getDataElement( );
			if ( data == null )
			{
				getScale( ).eUnset( ComponentPackage.eINSTANCE.getScale_Max( ) );
			}
			else
			{
				getScale( ).setMax( data );
			}
			setState( );
		}
		else if ( event.widget.equals( txtStepSize ) )
		{
			try
			{
				if ( txtStepSize.getText( ).length( ) == 0 )
				{
					getScale( ).eUnset( ComponentPackage.eINSTANCE.getScale_Step( ) );
				}
				else
				{
					double dbl = Double.valueOf( txtStepSize.getText( ) )
							.doubleValue( );
					if ( dbl == 0 )
					{
						getScale( ).eUnset( ComponentPackage.eINSTANCE.getScale_Step( ) );
					}
					else
					{
						getScale( ).setStep( dbl );
					}
				}
			}
			catch ( NumberFormatException e1 )
			{
				txtStepSize.setText( String.valueOf( getScale( ).getStep( ) ) );
			}
		}
		else if ( event.widget.equals( cmbScaleUnit ) )
		{
			getScale( ).setUnit( ScaleUnitType.getByName( LiteralHelper.scaleUnitTypeSet.getNameByDisplayName( cmbScaleUnit.getText( ) ) ) );
		}
		else if ( event.widget.equals( btnStepAuto ) )
		{
			getScale( ).unsetStepNumber( );
			getScale( ).unsetStep( );
			setState( );
		}
		else if ( event.widget.equals( btnStepSize ) )
		{
			getScale( ).unsetStepNumber( );
			// Set step size by notification
			txtStepSize.notifyListeners( SWT.Modify, null );
			setState( );
		}
		else if ( event.widget.equals( btnStepNumber ) )
		{
			getScale( ).unsetStep( );
			getScale( ).setStepNumber( spnStepNumber.getSelection( ) );
			setState( );
		}
		else if ( event.widget.equals( btnShowOutside ) )
		{
			getScale( ).setShowOutside( btnShowOutside.getSelection( ) );
		}
		else if ( event.widget.equals( spnStepNumber ) )
		{
			getScale( ).setStepNumber( spnStepNumber.getSelection( ) );
		}
		else if ( event.widget == btnAutoExpand )
		{
			getScale( ).setAutoExpand( btnAutoExpand.getSelection( ) );
		}
		else if ( event.widget == btnFactor )
		{
			if ( btnFactor.getSelection( ) )
			{
				getScale( ).unsetStepNumber( );
				txtFactor.notifyListeners( SWT.Modify, null );
			}
			else
			{
				getScale( ).unsetFactor( );
			}
			setState( );
		}
		else if ( event.widget == txtFactor )
		{
			try
			{
				if ( txtFactor.getText( ).length( ) == 0 )
				{
					getScale( ).eUnset( ComponentPackage.eINSTANCE.getScale_Factor( ) );
				}
				else
				{
					double dbl = Double.valueOf( txtFactor.getText( ) )
							.doubleValue( );
					if ( dbl == 0 )
					{
						getScale( ).eUnset( ComponentPackage.eINSTANCE.getScale_Factor( ) );
					}
					else
					{
						getScale( ).setFactor( dbl );
					}
				}
			}
			catch ( NumberFormatException e1 )
			{
				txtFactor.setText( String.valueOf( getScale( ).getFactor( ) ) );
			}
		}

	}

	protected abstract Scale getScale( );

	/**
	 * Returns the type of scale value
	 * 
	 * @return Constant value defined in <code>TextEditorComposite</code>
	 */
	protected abstract int getValueType( );

}