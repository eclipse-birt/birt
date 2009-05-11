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

package org.eclipse.birt.report.engine.emitter.config.ppt;

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
import org.eclipse.birt.report.engine.emitter.config.ppt.i18n.Messages;

/**
 * This class is a descriptor of ppt emitter.
 */
public class PPTEmitterDescriptor extends AbstractEmitterDescriptor
{

	private IConfigurableOption[] options;

	public PPTEmitterDescriptor( )
	{
		initOptions( );
	}

	private void initOptions( )
	{
		// Initializes the option for BIDIProcessing.
		ConfigurableOption bidiProcessing = new ConfigurableOption( IPDFRenderOption.PDF_BIDI_PROCESSING );
		bidiProcessing.setDisplayName( Messages.getString( "OptionDisplayValue.BidiProcessing" ) ); //$NON-NLS-1$
		bidiProcessing.setDataType( IConfigurableOption.DataType.BOOLEAN );
		bidiProcessing.setDisplayType( IConfigurableOption.DispayType.CHECKBOX );
		bidiProcessing.setDefaultValue( Boolean.FALSE );
		bidiProcessing.setToolTip( null );
		bidiProcessing.setDescription( Messages.getString( "OptionDescription.BidiProcessing" ) ); //$NON-NLS-1$

		// Initializes the option for TextWrapping.
		ConfigurableOption textWrapping = new ConfigurableOption( IPDFRenderOption.PDF_TEXT_WRAPPING );
		textWrapping.setDisplayName( Messages.getString( "OptionDisplayValue.TextWrapping" ) ); //$NON-NLS-1$
		textWrapping.setDataType( IConfigurableOption.DataType.BOOLEAN );
		textWrapping.setDisplayType( IConfigurableOption.DispayType.CHECKBOX );
		textWrapping.setDefaultValue( Boolean.TRUE );
		textWrapping.setToolTip( null );
		textWrapping.setDescription( Messages.getString( "OptionDescription.TextWrapping" ) ); //$NON-NLS-1$

		// Initializes the option for fontSubstitution.
		ConfigurableOption fontSubstitution = new ConfigurableOption(
				IPDFRenderOption.PDF_FONT_SUBSTITUTION );
		fontSubstitution.setDisplayName( Messages
				.getString( "OptionDisplayValue.fontSubstitution" ) );
		fontSubstitution.setDataType( IConfigurableOption.DataType.BOOLEAN );
		fontSubstitution
				.setDisplayType( IConfigurableOption.DispayType.CHECKBOX );
		fontSubstitution.setDefaultValue( Boolean.TRUE );
		fontSubstitution.setToolTip( null );
		fontSubstitution.setDescription( Messages
				.getString( "OptionDescription.fontSubstitution" ) ); //$NON-NLS-1$

		// Initializes the option for PageOverFlow.
		ConfigurableOption pageOverFlow = new ConfigurableOption(
				IPDFRenderOption.PAGE_OVERFLOW );
		pageOverFlow.setDisplayName( Messages
				.getString( "OptionDisplayValue.PageOverFlow" ) ); //$NON-NLS-1$
		pageOverFlow.setDataType( IConfigurableOption.DataType.INTEGER );
		pageOverFlow.setDisplayType( IConfigurableOption.DispayType.COMBO );
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

		options = new IConfigurableOption[]{
bidiProcessing, textWrapping,
				fontSubstitution, pageOverFlow,
		};
	}

	@Override
	public IConfigurableOptionObserver createOptionObserver( )
	{
		return new PPTOptionObserver( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDescription()
	 */
	public String getDescription( )
	{
		return Messages.getString( "PPTEmitter.Description" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "PPTEmitter.DisplayName" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#getID()
	 */
	public String getID( )
	{
		return "org.eclipse.birt.report.engine.emitter.ppt"; //$NON-NLS-1$
	}

	class PPTOptionObserver extends AbstractConfigurableOptionObserver
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
			renderOption.setOutputFormat( "ppt" ); //$NON-NLS-1$

			for ( IOptionValue optionValue : values )
			{
				if ( optionValue != null )
				{
					renderOption.setOption( optionValue.getName( ), optionValue
							.getValue( ) );
				}
			}

			return renderOption;
		}
	}

}
