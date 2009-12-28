/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.config.pdf;

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
import org.eclipse.birt.report.engine.emitter.config.pdf.i18n.Messages;

/**
 * This class is a descriptor of pdf emitter.
 */
public class PDFEmitterDescriptor extends AbstractEmitterDescriptor
{

	private static final String FONT_SUBSTITUTION = "FontSubstitution";
	private static final String BIDI_PROCESSING = "BIDIProcessing";
	private static final String TEXT_WRAPPING = "TextWrapping";
	private static final String EMBEDDED_FONT = "EmbeddedFont";
	private static final String CHART_DPI = "ChartDpi";

	private IConfigurableOption[] options;

	public PDFEmitterDescriptor( )
	{
		initOptions( );
	}

	private void initOptions( )
	{
		// Initializes the option for BIDIProcessing.
		ConfigurableOption bidiProcessing = new ConfigurableOption(
				BIDI_PROCESSING );
		bidiProcessing.setDisplayName( Messages
				.getString( "OptionDisplayValue.BidiProcessing" ) ); //$NON-NLS-1$
		bidiProcessing.setDataType( IConfigurableOption.DataType.BOOLEAN );
		bidiProcessing.setDisplayType( IConfigurableOption.DisplayType.CHECKBOX );
		bidiProcessing.setDefaultValue( Boolean.TRUE );
		bidiProcessing.setToolTip( null );
		bidiProcessing.setDescription( Messages
				.getString( "OptionDescription.BidiProcessing" ) ); //$NON-NLS-1$

		// Initializes the option for TextWrapping.
		ConfigurableOption textWrapping = new ConfigurableOption( TEXT_WRAPPING );
		textWrapping.setDisplayName( Messages
				.getString( "OptionDisplayValue.TextWrapping" ) ); //$NON-NLS-1$
		textWrapping.setDataType( IConfigurableOption.DataType.BOOLEAN );
		textWrapping.setDisplayType( IConfigurableOption.DisplayType.CHECKBOX );
		textWrapping.setDefaultValue( Boolean.TRUE );
		textWrapping.setToolTip( null );
		textWrapping.setDescription( Messages
				.getString( "OptionDescription.TextWrapping" ) ); //$NON-NLS-1$

		// Initializes the option for fontSubstitution.
		ConfigurableOption fontSubstitution = new ConfigurableOption(
				FONT_SUBSTITUTION );
		fontSubstitution.setDisplayName( Messages
				.getString( "OptionDisplayValue.FontSubstitution" ) );
		fontSubstitution.setDataType( IConfigurableOption.DataType.BOOLEAN );
		fontSubstitution
				.setDisplayType( IConfigurableOption.DisplayType.CHECKBOX );
		fontSubstitution.setDefaultValue( Boolean.TRUE );
		fontSubstitution.setToolTip( null );
		fontSubstitution.setDescription( Messages
				.getString( "OptionDescription.FontSubstitution" ) ); //$NON-NLS-1$

		// Initializes the option for PageOverFlow.
		ConfigurableOption pageOverFlow = new ConfigurableOption(
				IPDFRenderOption.PAGE_OVERFLOW );
		pageOverFlow.setDisplayName( Messages
				.getString( "OptionDisplayValue.PageOverFlow" ) ); //$NON-NLS-1$
		pageOverFlow.setDataType( IConfigurableOption.DataType.INTEGER );
		pageOverFlow.setDisplayType( IConfigurableOption.DisplayType.COMBO );
		pageOverFlow
				.setChoices( new OptionValue[]{
						new OptionValue(
								IPDFRenderOption.CLIP_CONTENT,
								Messages
										.getString( "OptionDisplayValue.CLIP_CONTENT" ) ), //$NON-NLS-1$
						new OptionValue(
								IPDFRenderOption.FIT_TO_PAGE_SIZE,
								Messages
										.getString( "OptionDisplayValue.FIT_TO_PAGE_SIZE" ) ), //$NON-NLS-1$
						new OptionValue(
								IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES,
								Messages
										.getString( "OptionDisplayValue.OUTPUT_TO_MULTIPLE_PAGES" ) ), //$NON-NLS-1$
						new OptionValue(
								IPDFRenderOption.ENLARGE_PAGE_SIZE,
								Messages
										.getString( "OptionDisplayValue.ENLARGE_PAGE_SIZE" ) ) //$NON-NLS-1$
				} );
		pageOverFlow.setDefaultValue( IPDFRenderOption.CLIP_CONTENT );
		pageOverFlow.setToolTip( null );
		pageOverFlow.setDescription( Messages
				.getString( "OptionDescription.PageOverFlow" ) ); //$NON-NLS-1$

		// Initializes the option for isEmbededFont.
		ConfigurableOption embeddedFont = new ConfigurableOption( EMBEDDED_FONT );
		embeddedFont.setDisplayName( Messages
				.getString( "OptionDisplayValue.EmbeddedFont" ) ); //$NON-NLS-1$
		embeddedFont.setDataType( IConfigurableOption.DataType.BOOLEAN );
		embeddedFont
				.setDisplayType( IConfigurableOption.DisplayType.CHECKBOX );
		embeddedFont.setDefaultValue( Boolean.TRUE );
		embeddedFont.setToolTip( null );
		embeddedFont.setDescription( Messages
				.getString( "OptionDescription.EmbeddedFont" ) ); //$NON-NLS-1$
		
		// Initializes the option for chart DPI.
		ConfigurableOption chartDpi = new ConfigurableOption( CHART_DPI );
		chartDpi.setDisplayName( Messages
				.getString( "OptionDisplayValue.ChartDpi" ) ); //$NON-NLS-1$
		chartDpi.setDataType( IConfigurableOption.DataType.INTEGER );
		chartDpi
				.setDisplayType( IConfigurableOption.DisplayType.TEXT );
		chartDpi.setDefaultValue( new Integer( 192 ) );
		chartDpi.setToolTip( null );
		chartDpi.setDescription( Messages
				.getString( "OptionDescription.ChartDpi" ) ); //$NON-NLS-1$

		options = new IConfigurableOption[]{bidiProcessing, textWrapping,
				fontSubstitution, pageOverFlow, embeddedFont, chartDpi};

	}

	@Override
	public IConfigurableOptionObserver createOptionObserver( )
	{
		return new PDFOptionObserver( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDescription()
	 */
	public String getDescription( )
	{
		return Messages.getString( "PDFEmitter.Description" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "PDFEmitter.DisplayName" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#getID()
	 */
	public String getID( )
	{
		return "org.eclipse.birt.report.engine.emitter.pdf"; //$NON-NLS-1$
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
		if ( EMBEDDED_FONT.equals( name ) )
		{
			return IPDFRenderOption.IS_EMBEDDED_FONT;
		}
		if ( CHART_DPI.equals( name ) )
		{
			return IRenderOption.CHART_DPI;
		}
		return name;
	}

	/**
	 * PDFOptionObserver
	 */
	class PDFOptionObserver extends AbstractConfigurableOptionObserver
	{

		public IConfigurableOption[] getOptions( )
		{
			return options;
		}

		public IRenderOption getPreferredRenderOption( )
		{
			PDFRenderOption renderOption = new PDFRenderOption( );

			renderOption.setEmitterID( getID( ) );
			renderOption.setOutputFormat( "pdf" ); //$NON-NLS-1$

			for ( IOptionValue optionValue : values )
			{
				if ( optionValue != null )
				{
					renderOption.setOption( getRenderOptionName( optionValue
							.getName( ) ), optionValue.getValue( ) );
				}
			}

			return renderOption;
		}

	}

}
