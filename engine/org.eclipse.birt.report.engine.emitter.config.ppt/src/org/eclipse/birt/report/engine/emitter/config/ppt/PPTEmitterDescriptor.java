/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.config.ppt;

import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IPPTRenderOption;
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
public class PPTEmitterDescriptor extends AbstractEmitterDescriptor {

	protected static final String FONT_SUBSTITUTION = "FontSubstitution";
	protected static final String BIDI_PROCESSING = "BIDIProcessing";
	protected static final String TEXT_WRAPPING = "TextWrapping";
	protected static final String CHART_DPI = "ChartDpi";
	protected static final String EXPORT_TO_OFFICE_2010_2013 = "Export2Office2010And2013";

	@Override
	protected void initOptions() {
		loadDefaultValues("org.eclipse.birt.report.engine.emitter.config.ppt");
		// Initializes the option for BIDIProcessing.
		ConfigurableOption bidiProcessing = new ConfigurableOption(BIDI_PROCESSING);
		bidiProcessing.setDisplayName(getMessage("OptionDisplayValue.BidiProcessing")); //$NON-NLS-1$
		bidiProcessing.setDataType(IConfigurableOption.DataType.BOOLEAN);
		bidiProcessing.setDisplayType(IConfigurableOption.DisplayType.CHECKBOX);
		bidiProcessing.setDefaultValue(Boolean.TRUE);
		bidiProcessing.setToolTip(null);
		bidiProcessing.setDescription(getMessage("OptionDescription.BidiProcessing")); //$NON-NLS-1$

		// Initializes the option for TextWrapping.
		ConfigurableOption textWrapping = new ConfigurableOption(TEXT_WRAPPING);
		textWrapping.setDisplayName(getMessage("OptionDisplayValue.TextWrapping")); //$NON-NLS-1$
		textWrapping.setDataType(IConfigurableOption.DataType.BOOLEAN);
		textWrapping.setDisplayType(IConfigurableOption.DisplayType.CHECKBOX);
		textWrapping.setDefaultValue(Boolean.TRUE);
		textWrapping.setToolTip(null);
		textWrapping.setDescription(getMessage("OptionDescription.TextWrapping")); //$NON-NLS-1$

		// Initializes the option for fontSubstitution.
		ConfigurableOption fontSubstitution = new ConfigurableOption(FONT_SUBSTITUTION);
		fontSubstitution.setDisplayName(getMessage("OptionDisplayValue.FontSubstitution"));
		fontSubstitution.setDataType(IConfigurableOption.DataType.BOOLEAN);
		fontSubstitution.setDisplayType(IConfigurableOption.DisplayType.CHECKBOX);
		fontSubstitution.setDefaultValue(Boolean.TRUE);
		fontSubstitution.setToolTip(null);
		fontSubstitution.setDescription(getMessage("OptionDescription.FontSubstitution")); //$NON-NLS-1$

		// Initializes the option for PageOverFlow.
		ConfigurableOption pageOverFlow = new ConfigurableOption(IPDFRenderOption.PAGE_OVERFLOW);
		pageOverFlow.setDisplayName(getMessage("OptionDisplayValue.PageOverFlow")); //$NON-NLS-1$
		pageOverFlow.setDataType(IConfigurableOption.DataType.INTEGER);
		pageOverFlow.setDisplayType(IConfigurableOption.DisplayType.COMBO);
		pageOverFlow.setChoices(new OptionValue[] {
				new OptionValue(IPDFRenderOption.CLIP_CONTENT, getMessage("OptionDisplayValue.CLIP_CONTENT")), //$NON-NLS-1$
				new OptionValue(IPDFRenderOption.FIT_TO_PAGE_SIZE, getMessage("OptionDisplayValue.FIT_TO_PAGE_SIZE")), //$NON-NLS-1$
				new OptionValue(IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES,
						getMessage("OptionDisplayValue.OUTPUT_TO_MULTIPLE_PAGES")), //$NON-NLS-1$
				new OptionValue(IPDFRenderOption.ENLARGE_PAGE_SIZE, getMessage("OptionDisplayValue.ENLARGE_PAGE_SIZE")) //$NON-NLS-1$
		});
		pageOverFlow.setDefaultValue(IPDFRenderOption.CLIP_CONTENT);
		pageOverFlow.setToolTip(null);
		pageOverFlow.setDescription(getMessage("OptionDescription.PageOverFlow")); //$NON-NLS-1$

		// Initializes the option for chart DPI.
		ConfigurableOption chartDpi = new ConfigurableOption(CHART_DPI);
		chartDpi.setDisplayName(getMessage("OptionDisplayValue.ChartDpi")); //$NON-NLS-1$
		chartDpi.setDataType(IConfigurableOption.DataType.INTEGER);
		chartDpi.setDisplayType(IConfigurableOption.DisplayType.TEXT);
		chartDpi.setDefaultValue(new Integer(192));
		chartDpi.setToolTip(getMessage("Tooltip.ChartDpi"));
		chartDpi.setDescription(getMessage("OptionDescription.ChartDpi")); //$NON-NLS-1$

		// Initializes the option for exporting.
		ConfigurableOption export2Office2010And2013 = new ConfigurableOption(EXPORT_TO_OFFICE_2010_2013);
		export2Office2010And2013.setDisplayName(getMessage("OptionDisplayValue.Export2Office2010And2013")); //$NON-NLS-1$
		export2Office2010And2013.setDataType(IConfigurableOption.DataType.BOOLEAN);
		export2Office2010And2013.setDisplayType(IConfigurableOption.DisplayType.CHECKBOX);
		export2Office2010And2013.setDefaultValue(Boolean.FALSE);
		export2Office2010And2013.setToolTip(null);
		export2Office2010And2013.setDescription(getMessage("OptionDescription.Export2Office2010And2013")); //$NON-NLS-1$

		options = new IConfigurableOption[] { bidiProcessing, textWrapping, fontSubstitution, pageOverFlow, chartDpi,
				export2Office2010And2013 };

		applyDefaultValues();
	}

	private String getMessage(String key) {
		return Messages.getString(key, locale);
	}

	@Override
	public IConfigurableOptionObserver createOptionObserver() {
		return new PPTOptionObserver();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDescription()
	 */
	@Override
	public String getDescription() {
		return getMessage("PPTEmitter.Description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return getMessage("PPTEmitter.DisplayName"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#getID()
	 */
	@Override
	public String getID() {
		return "org.eclipse.birt.report.engine.emitter.ppt"; //$NON-NLS-1$
	}

	public String getRenderOptionName(String name) {
		assert name != null;
		if (TEXT_WRAPPING.equals(name)) {
			return IPDFRenderOption.PDF_TEXT_WRAPPING;
		}
		if (BIDI_PROCESSING.equals(name)) {
			return IPDFRenderOption.PDF_BIDI_PROCESSING;
		}
		if (FONT_SUBSTITUTION.equals(name)) {
			return IPDFRenderOption.PDF_FONT_SUBSTITUTION;
		}
		if (CHART_DPI.equals(name)) {
			return IRenderOption.CHART_DPI;
		}
		if (EXPORT_TO_OFFICE_2010_2013.equals(name)) {
			return IPPTRenderOption.EXPORT_FILE_FOR_MICROSOFT_OFFICE_2010_2013;
		}
		return name;
	}

	class PPTOptionObserver extends AbstractConfigurableOptionObserver {

		@Override
		public IConfigurableOption[] getOptions() {
			return options;
		}

		@Override
		public IRenderOption getPreferredRenderOption() {
			PDFRenderOption renderOption = new PDFRenderOption();

			renderOption.setEmitterID(getID());
			renderOption.setOutputFormat("ppt"); //$NON-NLS-1$

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
