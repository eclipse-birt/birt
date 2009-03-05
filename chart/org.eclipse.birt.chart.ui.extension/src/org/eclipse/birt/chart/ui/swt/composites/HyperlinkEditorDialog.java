/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.attribute.impl.AttributeFactoryImpl;
import org.eclipse.birt.chart.model.component.impl.ComponentFactoryImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The class defines a dialog to set a hyperlink.
 * 
 * @since 2.5
 */

public class HyperlinkEditorDialog extends TrayDialog implements
		SelectionListener
{

	private URLValue fURLValue;
	private ChartWizardContext fContext;
	private Button fBtnBaseURL;
	private Button fBtnAdvanced;
	private Text fTxtBaseParm;
	private Text fTxtValueParm;
	private Text fTxtSeriesParm;
	private boolean bAdvanced = false;
	private int fOptionalStyle;
	private Group fGrpParameters;
	private boolean fbEnableURLParameters;
	private Text fTxtHyperlinkLabel;
	private TriggerSupportMatrix fTriggerMatrix;
	private String fsBaseURL;
	private List<String> fExistingLabels;

	/**
	 * @param shell
	 */
	public HyperlinkEditorDialog( Shell shell, URLValue urlValue,
			ChartWizardContext context, TriggerSupportMatrix triggerMatrix,
			int optionalStyle )
	{
		super( shell );

		fURLValue = urlValue;
		fContext = context;
		fTriggerMatrix = triggerMatrix;
		fOptionalStyle = optionalStyle;
		fbEnableURLParameters = ( ( optionalStyle & TriggerDataComposite.ENABLE_URL_PARAMETERS ) == TriggerDataComposite.ENABLE_URL_PARAMETERS );

		if ( fURLValue == null )
		{
			fURLValue = AttributeFactoryImpl.eINSTANCE.createURLValue( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		getShell( ).setText( Messages.getString("HyperlinkEditorDialog.Title.HyperlinkEditor") ); //$NON-NLS-1$
		Composite c = (Composite) super.createDialogArea( parent );
		createURLComposite( c );
		updateUIValues( );
		return c;
	}

	private void updateUIValues( )
	{
		if ( fURLValue.getLabel( ) == null )
		{
			org.eclipse.birt.chart.model.component.Label l = ComponentFactoryImpl.eINSTANCE.createLabel( );
			fURLValue.setLabel( l );
			l.eAdapters( ).addAll( fURLValue.eAdapters( ) );
			org.eclipse.birt.chart.model.attribute.Text t = AttributeFactoryImpl.eINSTANCE.createText( );
			l.setCaption( t );
			t.eAdapters( ).addAll( l.eAdapters( ) );
		}
		String v = fURLValue.getLabel( ).getCaption( ).getValue( );
		fTxtHyperlinkLabel.setText( v == null ? "" : v );//$NON-NLS-1$

		fsBaseURL = fURLValue.getBaseUrl( );

		fTxtBaseParm.setText( fURLValue.getBaseParameterName( ) == null ? ""//$NON-NLS-1$
				: fURLValue.getBaseParameterName( ) );
		fTxtSeriesParm.setText( fURLValue.getSeriesParameterName( ) == null ? ""//$NON-NLS-1$
				: fURLValue.getSeriesParameterName( ) );
		fTxtValueParm.setText( fURLValue.getValueParameterName( ) == null ? ""//$NON-NLS-1$
				: fURLValue.getValueParameterName( ) );
	}

	/**
	 * @param glURL
	 * @param glParameter
	 */
	private void createURLComposite( Composite parent )
	{
		GridLayout gl = (GridLayout) parent.getLayout( );
		gl.numColumns = 2;
		
		Label lblTarget = new Label( parent, SWT.NONE );
		GridData gdLBLTarget = new GridData( );
		gdLBLTarget.horizontalIndent = 2;
		lblTarget.setLayoutData( gdLBLTarget );
		lblTarget.setText( Messages.getString("HyperlinkEditorDialog.Text.MenuLabel") ); //$NON-NLS-1$

		fTxtHyperlinkLabel = new Text( parent, SWT.BORDER );
		GridData gdTXTTarget = new GridData( GridData.FILL_HORIZONTAL );
		fTxtHyperlinkLabel.setLayoutData( gdTXTTarget );

		Label lblBaseURL = new Label( parent, SWT.NONE );
		GridData gdLBLBaseURL = new GridData( );
		gdLBLBaseURL.horizontalIndent = 2;
		lblBaseURL.setLayoutData( gdLBLBaseURL );
		lblBaseURL.setText( Messages.getString( "TriggerDataComposite.Lbl.BaseURL" ) ); //$NON-NLS-1$
		
		fBtnBaseURL = new Button( parent, SWT.NONE );
		{
			GridData gd = new GridData( );
			fBtnBaseURL.setLayoutData( gd );
			fBtnBaseURL.setText( Messages.getString( "TriggerDataComposite.Text.EditBaseURL" ) ); //$NON-NLS-1$
			fBtnBaseURL.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.InvokeURLBuilder" ) ); //$NON-NLS-1$
			fBtnBaseURL.addSelectionListener( this );
			fBtnBaseURL.setEnabled( fContext.getUIServiceProvider( )
					.isInvokingSupported( ) );
		}

		Label lblDefine = new Label( parent, SWT.WRAP );
		{
			GridData gd = new GridData( );
			gd.horizontalIndent = 2;
			gd.horizontalSpan = 3;
			gd.widthHint = 200;
			lblDefine.setLayoutData( gd );
			lblDefine.setText( Messages.getString( "TriggerDataComposite.Label.Description" ) ); //$NON-NLS-1$
		}

		fBtnAdvanced = new Button( parent, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalSpan = 2;
			fBtnAdvanced.setLayoutData( gd );
			fBtnAdvanced.setText( getAdvancedButtonText( bAdvanced ) );
			fBtnAdvanced.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.Advanced" ) ); //$NON-NLS-1$
			fBtnAdvanced.addSelectionListener( this );
			fBtnAdvanced.setEnabled( fbEnableURLParameters );
		}

		fGrpParameters = new Group( parent, SWT.NONE );
		GridData gdGRPParameters = new GridData( GridData.FILL_HORIZONTAL );
		gdGRPParameters.horizontalSpan = 3;
		fGrpParameters.setLayoutData( gdGRPParameters );

		GridLayout glParameter = new GridLayout( );
		glParameter.marginWidth = 2;
		glParameter.marginHeight = 6;
		glParameter.horizontalSpacing = 6;
		glParameter.numColumns = 3;

		fGrpParameters.setLayout( glParameter );
		fGrpParameters.setText( Messages.getString( "TriggerDataComposite.Lbl.ParameterNames" ) ); //$NON-NLS-1$
		fGrpParameters.setVisible( bAdvanced );

		StyledText stParameters = new StyledText( fGrpParameters, SWT.WRAP
				| SWT.READ_ONLY );
		{
			GridData gd = new GridData( );
			gd.horizontalIndent = 2;
			gd.horizontalSpan = 3;
			gd.widthHint = 200;
			stParameters.setLayoutData( gd );
			stParameters.setText( Messages.getString( "TriggerDataComposite.Label.OptionalURLParameters" ) ); //$NON-NLS-1$
			StyleRange[] sr = {
					new StyleRange( 0,
							4,
							parent.getForeground( ),
							parent.getBackground( ),
							SWT.ITALIC ),
					new StyleRange( 4,
							stParameters.getText( ).length( ) - 4,
							parent.getForeground( ),
							parent.getBackground( ),
							SWT.NORMAL )
			};
			stParameters.setStyleRanges( sr );
			stParameters.setBackground( parent.getBackground( ) );
		}

		Label lblBaseParm = new Label( fGrpParameters, SWT.NONE );
		{
			GridData gdLBLBaseParm = new GridData( );
			gdLBLBaseParm.horizontalIndent = 2;
			lblBaseParm.setLayoutData( gdLBLBaseParm );
			lblBaseParm.setText( Messages.getString( "TriggerDataComposite.Lbl.CategorySeries" ) ); //$NON-NLS-1$
			lblBaseParm.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.ParameterCategory" ) ); //$NON-NLS-1$
		}

		fTxtBaseParm = new Text( fGrpParameters, SWT.BORDER );
		GridData gdTXTBaseParm = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTBaseParm.horizontalSpan = 2;
		fTxtBaseParm.setLayoutData( gdTXTBaseParm );
		fTxtBaseParm.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.ParameterCategory" ) ); //$NON-NLS-1$
		fTxtBaseParm.setEnabled( fbEnableURLParameters
				&& ( ( fOptionalStyle & TriggerDataComposite.DISABLE_CATEGORY_SERIES ) != TriggerDataComposite.DISABLE_CATEGORY_SERIES ) );
		Label lblValueParm = new Label( fGrpParameters, SWT.NONE );
		{
			GridData gdLBLValueParm = new GridData( );
			gdLBLValueParm.horizontalIndent = 2;
			lblValueParm.setLayoutData( gdLBLValueParm );
			lblValueParm.setText( Messages.getString( "TriggerDataComposite.Lbl.ValueSeries" ) ); //$NON-NLS-1$
			lblValueParm.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.ParameterValue" ) ); //$NON-NLS-1$
		}

		fTxtValueParm = new Text( fGrpParameters, SWT.BORDER );
		GridData gdTXTValueParm = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTValueParm.horizontalSpan = 2;
		fTxtValueParm.setLayoutData( gdTXTValueParm );
		fTxtValueParm.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.ParameterValue" ) ); //$NON-NLS-1$
		fTxtValueParm.setEnabled( fbEnableURLParameters
				&& ( ( fOptionalStyle & TriggerDataComposite.DISABLE_VALUE_SERIES ) != TriggerDataComposite.DISABLE_VALUE_SERIES ) );

		Label lblSeriesParm = new Label( fGrpParameters, SWT.NONE );
		{
			GridData gdLBLSeriesParm = new GridData( );
			gdLBLSeriesParm.horizontalIndent = 2;
			lblSeriesParm.setLayoutData( gdLBLSeriesParm );
			lblSeriesParm.setText( Messages.getString( "TriggerDataComposite.Lbl.ValueSeriesName" ) ); //$NON-NLS-1$
			lblSeriesParm.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.ParameterSeries" ) ); //$NON-NLS-1$
		}

		fTxtSeriesParm = new Text( fGrpParameters, SWT.BORDER );
		GridData gdTXTSeriesParm = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTSeriesParm.horizontalSpan = 2;
		fTxtSeriesParm.setLayoutData( gdTXTSeriesParm );
		fTxtSeriesParm.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.ParameterSeries" ) ); //$NON-NLS-1$
		fTxtSeriesParm.setEnabled( fbEnableURLParameters
				&& ( ( fOptionalStyle & TriggerDataComposite.DISABLE_VALUE_SERIES_NAME ) != TriggerDataComposite.DISABLE_VALUE_SERIES_NAME ) );
	}

	private String getAdvancedButtonText( boolean bAdvanced )
	{
		if ( bAdvanced )
		{
			return Messages.getString( "TriggerDataComposite.Text.OpenAdvanced" ); //$NON-NLS-1$
		}
		return Messages.getString( "TriggerDataComposite.Text.Advanced" ); //$NON-NLS-1$

	}

	private int getHyperlinkBuilderCommand( )
	{
		int type = this.fTriggerMatrix.getType( );
		if ( ( type & TriggerSupportMatrix.TYPE_DATAPOINT ) == TriggerSupportMatrix.TYPE_DATAPOINT )
		{
			return IUIServiceProvider.COMMAND_HYPERLINK_DATAPOINTS;
		}
		if ( ( type & TriggerSupportMatrix.TYPE_LEGEND ) == TriggerSupportMatrix.TYPE_LEGEND )
		{
			return IUIServiceProvider.COMMAND_HYPERLINK_LEGEND;
		}
		return IUIServiceProvider.COMMAND_HYPERLINK;
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub

	}

	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( fBtnBaseURL ) )
		{
			try
			{
				if ( fContext != null )
				{
					fsBaseURL = fContext.getUIServiceProvider( )
							.invoke( getHyperlinkBuilderCommand( ),
									fsBaseURL,
									fContext.getExtendedItem( ),
									null );
				}
			}
			catch ( ChartException ex )
			{
				WizardBase.displayException( ex );
			}
		}
		else if ( e.getSource( ).equals( fBtnAdvanced ) )
		{
			bAdvanced = !bAdvanced;
			fBtnAdvanced.setText( getAdvancedButtonText( bAdvanced ) );
			fGrpParameters.setVisible( bAdvanced );
			( (Composite) this.getDialogArea( ) ).layout( true, true );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		if ( !isValidLabel( ) )
		{
			// TODO ...
			return;
		}
		fURLValue.getLabel( )
				.getCaption( )
				.setValue( fTxtHyperlinkLabel.getText( ) );
		fURLValue.setBaseUrl( fsBaseURL );
		fURLValue.setBaseParameterName( fTxtBaseParm.getText( ) );
		fURLValue.setSeriesParameterName( fTxtSeriesParm.getText( ) );
		fURLValue.setValueParameterName( fTxtValueParm.getText( ) );
		super.okPressed( );
	}

	/**
	 * Check if specified label already is used.
	 * 
	 * @return
	 */
	private boolean isValidLabel( )
	{
		if ( "".equals( fTxtHyperlinkLabel.getText( ) ) )//$NON-NLS-1$
		{
			return false;
		}
		if ( fExistingLabels == null )
		{
			return true;
		}

		if ( fExistingLabels.contains( fTxtHyperlinkLabel.getText( ) ) )
		{
			return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	public URLValue getURLValue( )
	{
		return fURLValue;
	}

	public void setExistingLabels( List<String> labels )
	{
		fExistingLabels = labels;
	}
}
