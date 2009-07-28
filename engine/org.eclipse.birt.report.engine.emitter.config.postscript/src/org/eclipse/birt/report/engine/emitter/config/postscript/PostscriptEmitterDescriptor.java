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

package org.eclipse.birt.report.engine.emitter.config.postscript;

import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.emitter.config.AbstractConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.AbstractEmitterDescriptor;
import org.eclipse.birt.report.engine.emitter.config.ConfigurableOption;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOption;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.IOptionValue;
import org.eclipse.birt.report.engine.emitter.config.OptionValue;
import org.eclipse.birt.report.engine.emitter.config.postscript.i18n.Messages;
import org.eclipse.birt.report.engine.emitter.postscript.PostscriptRenderOption;

/**
 * This class is a descriptor of postscript emitter.
 */
public class PostscriptEmitterDescriptor extends AbstractEmitterDescriptor
{

	private static final String FONT_SUBSTITUTION = "FontSubstitution";
	private static final String BIDI_PROCESSING = "BIDIProcessing";
	private static final String TEXT_WRAPPING = "TextWrapping";

	private IConfigurableOption[] options;

	public PostscriptEmitterDescriptor( )
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

		// Initializes the option for copies.
		ConfigurableOption copies = new ConfigurableOption(
				PostscriptRenderOption.OPTION_COPIES );
		copies
				.setDisplayName( Messages
						.getString( "OptionDisplayValue.Copies" ) ); //$NON-NLS-1$
		copies.setDataType( IConfigurableOption.DataType.INTEGER );
		copies.setDisplayType( IConfigurableOption.DisplayType.TEXT );
		copies.setDefaultValue( 1 );
		copies.setToolTip( null );
		copies
				.setDescription( Messages
						.getString( "OptionDescription.Copies" ) ); //$NON-NLS-1$

		// Initializes the option for collate.
		ConfigurableOption collate = new ConfigurableOption(
				PostscriptRenderOption.OPTION_COLLATE );
		collate.setDisplayName( Messages
				.getString( "OptionDisplayValue.Collate" ) ); //$NON-NLS-1$
		collate.setDataType( IConfigurableOption.DataType.BOOLEAN );
		collate.setDisplayType( IConfigurableOption.DisplayType.CHECKBOX );
		collate.setDefaultValue( Boolean.FALSE );
		collate.setToolTip( null );
		collate.setDescription( Messages
				.getString( "OptionDescription.Collate" ) ); //$NON-NLS-1$

		// Initializes the option for duplex.
		ConfigurableOption duplex = new ConfigurableOption(
				PostscriptRenderOption.OPTION_DUPLEX );
		duplex
				.setDisplayName( Messages
						.getString( "OptionDisplayValue.Duplex" ) ); //$NON-NLS-1$
		duplex.setDataType( IConfigurableOption.DataType.STRING );
		duplex.setDisplayType( IConfigurableOption.DisplayType.TEXT );
		duplex.setDefaultValue( null );
		duplex.setToolTip( null );
		duplex
				.setDescription( Messages
						.getString( "OptionDescription.Duplex" ) ); //$NON-NLS-1$

		// Initializes the option for paperSize.
		ConfigurableOption paperSize = new ConfigurableOption(
				PostscriptRenderOption.OPTION_PAPER_SIZE );
		paperSize
				.setDisplayName( Messages
				.getString( "OptionDisplayValue.PaperSize" ) ); //$NON-NLS-1$
		paperSize.setDataType( IConfigurableOption.DataType.STRING );
		paperSize.setDisplayType( IConfigurableOption.DisplayType.TEXT );
		paperSize.setDefaultValue( "A4" );
		paperSize.setToolTip( null );
		paperSize
				.setDescription( Messages
				.getString( "OptionDescription.PaperSize" ) ); //$NON-NLS-1$

		// Initializes the option for paperTray.
		ConfigurableOption paperTray = new ConfigurableOption(
				PostscriptRenderOption.OPTION_PAPER_TRAY );
		paperTray.setDisplayName( Messages
				.getString( "OptionDisplayValue.PaperTray" ) ); //$NON-NLS-1$
		paperTray.setDataType( IConfigurableOption.DataType.STRING );
		paperTray.setDisplayType( IConfigurableOption.DisplayType.TEXT );
		paperTray.setDefaultValue( null );
		paperTray.setToolTip( null );
		paperTray.setDescription( Messages
				.getString( "OptionDescription.PaperTray" ) ); //$NON-NLS-1$

		options = new IConfigurableOption[]{bidiProcessing, textWrapping,
				fontSubstitution, pageOverFlow, copies, collate, duplex,
				paperSize, paperTray};
	}

	@Override
	public IConfigurableOptionObserver createOptionObserver( )
	{
		return new PostscriptOptionObserver( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDescription()
	 */
	public String getDescription( )
	{
		return Messages.getString( "PostscriptEmitter.Description" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "PostscriptEmitter.DisplayName" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#getID()
	 */
	public String getID( )
	{
		return "org.eclipse.birt.report.engine.emitter.postscript"; //$NON-NLS-1$
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
		return name;
	}

	class PostscriptOptionObserver extends AbstractConfigurableOptionObserver
	{

		@Override
		public IConfigurableOption[] getOptions( )
		{
			return options;
		}

		@Override
		public IRenderOption getPreferredRenderOption( )
		{
			RenderOption renderOption = new RenderOption( );

			renderOption.setEmitterID( getID( ) );
			renderOption.setOutputFormat( "postscript" ); //$NON-NLS-1$

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
