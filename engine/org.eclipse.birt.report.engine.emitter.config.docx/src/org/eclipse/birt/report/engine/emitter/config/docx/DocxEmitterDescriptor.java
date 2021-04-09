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

package org.eclipse.birt.report.engine.emitter.config.docx;

import static org.eclipse.birt.report.engine.api.DocxRenderOption.OPTION_EMBED_HTML;
import static org.eclipse.birt.report.engine.api.DocxRenderOption.OPTION_WORD_VERSION;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.emitter.config.AbstractConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.AbstractEmitterDescriptor;
import org.eclipse.birt.report.engine.emitter.config.ConfigurableOption;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOption;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.IOptionValue;
import org.eclipse.birt.report.engine.emitter.config.OptionValue;
import org.eclipse.birt.report.engine.emitter.config.docx.i18n.Messages;


/**
 * This class is a descriptor of word emitter.
 */
public class DocxEmitterDescriptor extends AbstractEmitterDescriptor
{
	protected static final String CHART_DPI = "ChartDpi";
	protected static final String EMBED_HTML = "EmbedHtml";
	protected static final String WORD_VERSION = "WordVersion";

	protected void initOptions( )
	{
		loadDefaultValues( "org.eclipse.birt.report.engine.emitter.config.docx" );
		// Initializes the option for chart DPI.
		ConfigurableOption chartDpi = new ConfigurableOption( CHART_DPI );
		chartDpi.setDisplayName( getMessage( "OptionDisplayValue.ChartDpi" ) ); //$NON-NLS-1$
		chartDpi.setDataType( IConfigurableOption.DataType.INTEGER );
		chartDpi.setDisplayType( IConfigurableOption.DisplayType.TEXT );
		chartDpi.setDefaultValue( new Integer( 192 ) );
		chartDpi.setToolTip( getMessage( "Tooltip.ChartDpi" ) );
		chartDpi.setDescription( getMessage( "OptionDescription.ChartDpi" ) ); //$NON-NLS-1$

		ConfigurableOption embedHtml = new ConfigurableOption( EMBED_HTML );
		embedHtml.setDisplayName( getMessage( "OptionDisplayValue.EmbedHtml" ) ); //$NON-NLS-1$
		embedHtml.setDataType( IConfigurableOption.DataType.BOOLEAN );
		embedHtml.setDisplayType( IConfigurableOption.DisplayType.CHECKBOX );
		embedHtml.setDefaultValue( new Boolean(Boolean.TRUE) );
		embedHtml.setToolTip( getMessage( "Tooltip.EmbedHtml" ) );
		embedHtml.setDescription( getMessage( "OptionDescription.EmbedHtml" ) ); //$NON-NLS-1$

		ConfigurableOption wordVersion = new ConfigurableOption( WORD_VERSION );
		wordVersion.setDisplayName( getMessage( "OptionDisplayValue.WordVersion" ) ); //$NON-NLS-1$
		wordVersion.setDataType( IConfigurableOption.DataType.INTEGER );
		wordVersion.setDisplayType( IConfigurableOption.DisplayType.TEXT );
		wordVersion.setDefaultValue( new Integer( 2016 ) );
		IOptionValue[] choices = { new OptionValue(2010), new OptionValue(2016) };
		wordVersion.setChoices(choices);
		wordVersion.setToolTip( getMessage( "Tooltip.WordVersion" ) );
		wordVersion.setDescription( getMessage( "OptionDescription.WordVersion" ) ); //$NON-NLS-1$

		options = new IConfigurableOption[]{chartDpi, embedHtml, wordVersion};
		applyDefaultValues( );
	}
	@Override
	public IConfigurableOptionObserver createOptionObserver( )
	{
		return new DocxOptionObserver( );
	}

	private String getMessage(String key) {
		return Messages.getString(key, locale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDescription()
	 */
	public String getDescription() {
		return getMessage("DocxEmitter.Description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDisplayName()
	 */
	public String getDisplayName() {
		return getMessage("DocxEmitter.DisplayName"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#getID()
	 */
	public String getID() {
		return "org.eclipse.birt.report.engine.emitter.docx"; //$NON-NLS-1$
	}

	public String getRenderOptionName(String name) {
		assert name != null;
		if (CHART_DPI.equals(name)) {
			return IRenderOption.CHART_DPI;
		}
		
		if ( EMBED_HTML.equals( name ) )
		{
			return OPTION_EMBED_HTML;
		}

		if ( WORD_VERSION.equals( name ) )
		{
			return OPTION_WORD_VERSION;
		}
		return name;
	}

	class DocxOptionObserver extends AbstractConfigurableOptionObserver
	{

		@Override
		public IConfigurableOption[] getOptions() {
			return options;
		}

		@Override
		public IRenderOption getPreferredRenderOption() {
			RenderOption renderOption = new RenderOption();

			renderOption.setEmitterID(getID());
			renderOption.setOutputFormat("docx"); //$NON-NLS-1$

			if (values != null && values.length > 0) {
				for (IOptionValue optionValue : values) {
					if (optionValue != null) {
						renderOption.setOption(getRenderOptionName(optionValue.getName()), optionValue.getValue());
					}
				}
			}

			return renderOption;
		}
	}

}
