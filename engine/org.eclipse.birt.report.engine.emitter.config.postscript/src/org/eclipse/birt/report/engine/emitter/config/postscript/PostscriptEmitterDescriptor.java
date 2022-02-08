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

package org.eclipse.birt.report.engine.emitter.config.postscript;

import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IPostscriptRenderOption;
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
public class PostscriptEmitterDescriptor extends AbstractEmitterDescriptor {

	private static final String FONT_SUBSTITUTION = "FontSubstitution";
	private static final String BIDI_PROCESSING = "BIDIProcessing";
	private static final String TEXT_WRAPPING = "TextWrapping";
	private static final String CHART_DPI = "ChartDpi";

	protected void initOptions() {
		loadDefaultValues("org.eclipse.birt.report.engine.emitter.config.postscript");
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

		// Initializes the option for copies.
		ConfigurableOption copies = new ConfigurableOption(PostscriptRenderOption.OPTION_COPIES);
		copies.setDisplayName(getMessage("OptionDisplayValue.Copies")); //$NON-NLS-1$
		copies.setDataType(IConfigurableOption.DataType.INTEGER);
		copies.setDisplayType(IConfigurableOption.DisplayType.TEXT);
		copies.setDefaultValue(1);
		copies.setToolTip(null);
		copies.setDescription(getMessage("OptionDescription.Copies")); //$NON-NLS-1$

		// Initializes the option for collate.
		ConfigurableOption collate = new ConfigurableOption(PostscriptRenderOption.OPTION_COLLATE);
		collate.setDisplayName(getMessage("OptionDisplayValue.Collate")); //$NON-NLS-1$
		collate.setDataType(IConfigurableOption.DataType.BOOLEAN);
		collate.setDisplayType(IConfigurableOption.DisplayType.CHECKBOX);
		collate.setDefaultValue(Boolean.FALSE);
		collate.setToolTip(null);
		collate.setDescription(getMessage("OptionDescription.Collate")); //$NON-NLS-1$

		// Initializes the option for duplex.
		ConfigurableOption duplex = new ConfigurableOption(PostscriptRenderOption.OPTION_DUPLEX);
		duplex.setDisplayName(getMessage("OptionDisplayValue.Duplex")); //$NON-NLS-1$
		duplex.setDataType(IConfigurableOption.DataType.STRING);
		duplex.setDisplayType(IConfigurableOption.DisplayType.TEXT);
		duplex.setChoices(new OptionValue[] {
				new OptionValue(IPostscriptRenderOption.DUPLEX_SIMPLEX,
						getMessage("OptionDisplayValue.DUPLEX_SIMPLEX")), //$NON-NLS-1$
				new OptionValue(IPostscriptRenderOption.DUPLEX_FLIP_ON_SHORT_EDGE,
						getMessage("OptionDisplayValue.DUPLEX_FLIP_ON_SHORT_EDGE")), //$NON-NLS-1$
				new OptionValue(IPostscriptRenderOption.DUPLEX_FLIP_ON_LONG_EDGE,
						getMessage("OptionDisplayValue.DUPLEX_FLIP_ON_LONG_EDGE")) //$NON-NLS-1$
		});
		duplex.setDefaultValue(IPostscriptRenderOption.DUPLEX_SIMPLEX);
		duplex.setToolTip(null);
		duplex.setDescription(getMessage("OptionDescription.Duplex")); //$NON-NLS-1$

		// Initializes the option for paperSize.
		ConfigurableOption paperSize = new ConfigurableOption(PostscriptRenderOption.OPTION_PAPER_SIZE);
		paperSize.setDisplayName(getMessage("OptionDisplayValue.PaperSize")); //$NON-NLS-1$
		paperSize.setDataType(IConfigurableOption.DataType.STRING);
		paperSize.setDisplayType(IConfigurableOption.DisplayType.TEXT);
		paperSize.setDefaultValue(null);
		paperSize.setToolTip(null);
		paperSize.setDescription(getMessage("OptionDescription.PaperSize")); //$NON-NLS-1$

		// Initializes the option for paperTray.
		ConfigurableOption paperTray = new ConfigurableOption(PostscriptRenderOption.OPTION_PAPER_TRAY);
		paperTray.setDisplayName(getMessage("OptionDisplayValue.PaperTray")); //$NON-NLS-1$
		paperTray.setDataType(IConfigurableOption.DataType.STRING);
		paperTray.setDisplayType(IConfigurableOption.DisplayType.TEXT);
		paperTray.setDefaultValue(null);
		paperTray.setToolTip(null);
		paperTray.setDescription(getMessage("OptionDescription.PaperTray")); //$NON-NLS-1$

		ConfigurableOption scale = new ConfigurableOption(PostscriptRenderOption.OPTION_SCALE);
		scale.setDisplayName(getMessage("OptionDisplayValue.Scale")); //$NON-NLS-1$
		scale.setDataType(IConfigurableOption.DataType.INTEGER);
		scale.setDisplayType(IConfigurableOption.DisplayType.TEXT);
		scale.setDefaultValue(100);
		scale.setToolTip(null);
		scale.setDescription(getMessage("OptionDescription.Scale")); //$NON-NLS-1$

		ConfigurableOption resolution = new ConfigurableOption(PostscriptRenderOption.OPTION_RESOLUTION);
		resolution.setDisplayName(getMessage("OptionDisplayValue.Resolution")); //$NON-NLS-1$
		resolution.setDataType(IConfigurableOption.DataType.STRING);
		resolution.setDisplayType(IConfigurableOption.DisplayType.TEXT);
		resolution.setDefaultValue(null);
		resolution.setToolTip(null);
		resolution.setDescription(getMessage("OptionDescription.Resolution")); //$NON-NLS-1$

		ConfigurableOption color = new ConfigurableOption(PostscriptRenderOption.OPTION_COLOR);
		color.setDisplayName(getMessage("OptionDisplayValue.Color")); //$NON-NLS-1$
		color.setDataType(IConfigurableOption.DataType.BOOLEAN);
		color.setDisplayType(IConfigurableOption.DisplayType.CHECKBOX);
		color.setDefaultValue(Boolean.TRUE);
		color.setToolTip(null);
		color.setDescription(getMessage("OptionDescription.Color")); //$NON-NLS-1$

		// Initializes the option for chart DPI.
		ConfigurableOption chartDpi = new ConfigurableOption(CHART_DPI);
		chartDpi.setDisplayName(getMessage("OptionDisplayValue.ChartDpi")); //$NON-NLS-1$
		chartDpi.setDataType(IConfigurableOption.DataType.INTEGER);
		chartDpi.setDisplayType(IConfigurableOption.DisplayType.TEXT);
		chartDpi.setDefaultValue(new Integer(192));
		chartDpi.setToolTip(getMessage("Tooltip.ChartDpi"));
		chartDpi.setDescription(getMessage("OptionDescription.ChartDpi")); //$NON-NLS-1$

		// Initializes the option for auto page size selection.
		ConfigurableOption autoPaperSizeSelection = new ConfigurableOption(
				PostscriptRenderOption.OPTION_AUTO_PAPER_SIZE_SELECTION);
		autoPaperSizeSelection.setDisplayName(getMessage("OptionDisplayValue.AutoPaperSizeSelection")); //$NON-NLS-1$
		autoPaperSizeSelection.setDataType(IConfigurableOption.DataType.BOOLEAN);
		autoPaperSizeSelection.setDisplayType(IConfigurableOption.DisplayType.CHECKBOX);
		autoPaperSizeSelection.setDefaultValue(true);
		autoPaperSizeSelection.setToolTip(null);
		autoPaperSizeSelection.setDescription(getMessage("OptionDescription.AutoPaperSizeSelection")); //$NON-NLS-1$

		// Initializes the option for collate.
		ConfigurableOption fitToPaper = new ConfigurableOption(PostscriptRenderOption.OPTION_FIT_TO_PAPER);
		fitToPaper.setDisplayName(getMessage("OptionDisplayValue.FitToPaper")); //$NON-NLS-1$
		fitToPaper.setDataType(IConfigurableOption.DataType.BOOLEAN);
		fitToPaper.setDisplayType(IConfigurableOption.DisplayType.CHECKBOX);
		fitToPaper.setDefaultValue(Boolean.FALSE);
		fitToPaper.setToolTip(null);
		fitToPaper.setDescription(getMessage("OptionDescription.FitToPaper")); //$NON-NLS-1$

		options = new IConfigurableOption[] { bidiProcessing, textWrapping, fontSubstitution, pageOverFlow, copies,
				collate, duplex, paperSize, paperTray, scale, resolution, color, chartDpi, autoPaperSizeSelection,
				fitToPaper };

		applyDefaultValues();
	}

	private String getMessage(String key) {
		return Messages.getString(key, locale);
	}

	@Override
	public IConfigurableOptionObserver createOptionObserver() {
		return new PostscriptOptionObserver();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDescription()
	 */
	public String getDescription() {
		return getMessage("PostscriptEmitter.Description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDisplayName()
	 */
	public String getDisplayName() {
		return getMessage("PostscriptEmitter.DisplayName"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#getID()
	 */
	public String getID() {
		return "org.eclipse.birt.report.engine.emitter.postscript"; //$NON-NLS-1$
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
		return name;
	}

	class PostscriptOptionObserver extends AbstractConfigurableOptionObserver {

		@Override
		public IConfigurableOption[] getOptions() {
			return options;
		}

		@Override
		public IRenderOption getPreferredRenderOption() {
			RenderOption renderOption = new RenderOption();

			renderOption.setEmitterID(getID());
			renderOption.setOutputFormat("postscript"); //$NON-NLS-1$

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
