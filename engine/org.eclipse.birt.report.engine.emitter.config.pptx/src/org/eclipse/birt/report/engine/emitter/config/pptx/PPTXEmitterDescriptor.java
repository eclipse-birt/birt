/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.config.pptx;

import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.emitter.config.AbstractConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.AbstractEmitterDescriptor;
import org.eclipse.birt.report.engine.emitter.config.ConfigurableOption;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOption;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.IOptionValue;
import org.eclipse.birt.report.engine.emitter.config.OptionValue;
import org.eclipse.birt.report.engine.emitter.config.pptx.i18n.Messages;
import org.eclipse.birt.report.engine.emitter.pptx.PPTXRender;


/**
 * This class is a descriptor of pptx emitter.
 */
public class PPTXEmitterDescriptor extends AbstractEmitterDescriptor
{

	private static final String FONT_SUBSTITUTION = "FontSubstitution";
	private static final String BIDI_PROCESSING = "BIDIProcessing";
	private static final String TEXT_WRAPPING = "TextWrapping";
	private static final String CHART_DPI = "ChartDpi";
	private static final String REPAGINATE_FOR_PDF = "repaginateForPDF";
	private static final String EDIT_MODE = PPTXRender.OPTION_EDIT_MODE;

	protected void initOptions( )
	{
		loadDefaultValues( "org.eclipse.birt.report.engine.emitter.config.pptx" );
		// Initializes the option for BIDIProcessing.
		ConfigurableOption bidiProcessing = new ConfigurableOption(
				BIDI_PROCESSING );
		bidiProcessing
				.setDisplayName( getMessage( "OptionDisplayValue.BidiProcessing" ) ); //$NON-NLS-1$
		bidiProcessing.setDataType( IConfigurableOption.DataType.BOOLEAN );
		bidiProcessing
				.setDisplayType( IConfigurableOption.DisplayType.CHECKBOX );
		bidiProcessing.setDefaultValue( Boolean.TRUE );
		bidiProcessing.setToolTip( null );
		bidiProcessing
				.setDescription( getMessage( "OptionDescription.BidiProcessing" ) ); //$NON-NLS-1$
		
		// Initializes the option for TextWrapping.
		ConfigurableOption textWrapping = new ConfigurableOption( TEXT_WRAPPING );
		textWrapping
				.setDisplayName( getMessage( "OptionDisplayValue.TextWrapping" ) ); //$NON-NLS-1$
		textWrapping.setDataType( IConfigurableOption.DataType.BOOLEAN );
		textWrapping.setDisplayType( IConfigurableOption.DisplayType.CHECKBOX );
		textWrapping.setDefaultValue( Boolean.TRUE );
		textWrapping.setToolTip( null );
		textWrapping
				.setDescription( getMessage( "OptionDescription.TextWrapping" ) ); //$NON-NLS-1$

		// Initializes the option for fontSubstitution.
		ConfigurableOption fontSubstitution = new ConfigurableOption(
				FONT_SUBSTITUTION );
		fontSubstitution
				.setDisplayName( getMessage( "OptionDisplayValue.FontSubstitution" ) );
		fontSubstitution.setDataType( IConfigurableOption.DataType.BOOLEAN );
		fontSubstitution
				.setDisplayType( IConfigurableOption.DisplayType.CHECKBOX );
		fontSubstitution.setDefaultValue( Boolean.TRUE );
		fontSubstitution.setToolTip( null );
		fontSubstitution
				.setDescription( getMessage( "OptionDescription.FontSubstitution" ) ); //$NON-NLS-1$

		// Initializes the option for PageOverFlow.
		ConfigurableOption pageOverFlow = new ConfigurableOption(
				IPDFRenderOption.PAGE_OVERFLOW );
		pageOverFlow
				.setDisplayName( getMessage( "OptionDisplayValue.PageOverFlow" ) ); //$NON-NLS-1$
		pageOverFlow.setDataType( IConfigurableOption.DataType.INTEGER );
		pageOverFlow.setDisplayType( IConfigurableOption.DisplayType.COMBO );
		pageOverFlow
				.setChoices( new OptionValue[]{
						new OptionValue( IPDFRenderOption.CLIP_CONTENT,
								getMessage( "OptionDisplayValue.CLIP_CONTENT" ) ), //$NON-NLS-1$
						new OptionValue(
								IPDFRenderOption.FIT_TO_PAGE_SIZE,
								getMessage( "OptionDisplayValue.FIT_TO_PAGE_SIZE" ) ), //$NON-NLS-1$
						new OptionValue(
								IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES,
								getMessage( "OptionDisplayValue.OUTPUT_TO_MULTIPLE_PAGES" ) ), //$NON-NLS-1$
						new OptionValue(
								IPDFRenderOption.ENLARGE_PAGE_SIZE,
								getMessage( "OptionDisplayValue.ENLARGE_PAGE_SIZE" ) ) //$NON-NLS-1$
				} );
		pageOverFlow.setDefaultValue( IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES );
		pageOverFlow.setToolTip( null );
		pageOverFlow
				.setDescription( getMessage( "OptionDescription.PageOverFlow" ) ); //$NON-NLS-1$

		// Initializes the option for chart DPI.
		ConfigurableOption chartDpi = new ConfigurableOption( CHART_DPI );
		chartDpi.setDisplayName( getMessage( "OptionDisplayValue.ChartDpi" ) ); //$NON-NLS-1$
		chartDpi.setDataType( IConfigurableOption.DataType.INTEGER );
		chartDpi.setDisplayType( IConfigurableOption.DisplayType.TEXT );
		chartDpi.setDefaultValue( new Integer( 192 ) );
		chartDpi.setToolTip( getMessage( "Tooltip.ChartDpi" ) );
		chartDpi.setDescription( getMessage( "OptionDescription.ChartDpi" ) ); //$NON-NLS-1$
		
		// Initializes the option for repaginate for PDF.
		ConfigurableOption repaginateForPDF = new ConfigurableOption( REPAGINATE_FOR_PDF );
		repaginateForPDF.setDisplayName( getMessage( "OptionDisplayValue.RepaginateForPDF" ) );
		repaginateForPDF.setDataType( IConfigurableOption.DataType.BOOLEAN );
		repaginateForPDF.setDisplayType( IConfigurableOption.DisplayType.CHECKBOX );
		repaginateForPDF.setDefaultValue( Boolean.FALSE );
		repaginateForPDF.setToolTip( null );
		repaginateForPDF.setDescription( getMessage( "OptionDescription.RepaginateForPDF" ) ); //$NON-NLS-1$

		// Initializes the option for layout mode.
		ConfigurableOption editMode = new ConfigurableOption( EDIT_MODE );
		editMode.setDisplayName( getMessage( "OptionDisplayValue.EditMode" ) );
		editMode.setDataType( IConfigurableOption.DataType.BOOLEAN );
		editMode.setDisplayType( IConfigurableOption.DisplayType.CHECKBOX );
		editMode.setDefaultValue( Boolean.TRUE );
		editMode.setToolTip( null );
		editMode.setDescription( getMessage( "OptionDescription.EditMode" ) ); //$NON-NLS-1$
		
		options = new IConfigurableOption[]{bidiProcessing, textWrapping,
				fontSubstitution, pageOverFlow, repaginateForPDF, chartDpi, editMode};
		
		applyDefaultValues( );
	}

	@Override
	public IConfigurableOptionObserver createOptionObserver( )
	{
		return new PPTXOptionObserver( );
	}

	private String getMessage( String key )
	{
		return Messages.getString( key, locale );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDescription()
	 */
	public String getDescription( )
	{
		return getMessage( "PPTXEmitter.Description" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDisplayName()
	 */
	public String getDisplayName( )
	{
		return getMessage( "PPTXEmitter.DisplayName" ); //$NON-NLS-1$
	}

	public String getRenderOptionName( String name )
	{
		assert name != null;
		if ( TEXT_WRAPPING.equals( name ) )
		{
			return IPDFRenderOption.PDF_TEXT_WRAPPING;
		}
		if ( BIDI_PROCESSING.equals( name ) )
		{
			return IPDFRenderOption.PDF_BIDI_PROCESSING;
		}
		if ( FONT_SUBSTITUTION.equals( name ) )
		{
			return IPDFRenderOption.PDF_FONT_SUBSTITUTION;
		}
		if ( CHART_DPI.equals( name ) )
		{
			return IRenderOption.CHART_DPI;
		}
		if ( REPAGINATE_FOR_PDF.equals( name ) )
		{
			return IPDFRenderOption.REPAGINATE_FOR_PDF;
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#getID()
	 */
	public String getID( )
	{
		return "org.eclipse.birt.report.engine.emitter.pptx"; //$NON-NLS-1$
	}

	class PPTXOptionObserver extends AbstractConfigurableOptionObserver
	{

		@Override
		public IConfigurableOption[] getOptions( )
		{
			return options;
		}

		@Override
		public IRenderOption getPreferredRenderOption( )
		{
			PDFRenderOption renderOption = new PDFRenderOption( );

			renderOption.setEmitterID( getID( ) );
			renderOption.setOutputFormat( "pptx" ); //$NON-NLS-1$

			if ( values != null && values.length > 0 )
			{
				for ( IOptionValue optionValue : values )
				{
					if ( optionValue != null )
					{
						renderOption.setOption(
								getRenderOptionName( optionValue.getName( ) ),
								optionValue.getValue( ) );
					}
				}
			}

			return renderOption;
		}
	}

}
