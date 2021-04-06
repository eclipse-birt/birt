/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.emitter.config.ods;

import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.IExcelRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.emitter.config.AbstractConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.AbstractEmitterDescriptor;
import org.eclipse.birt.report.engine.emitter.config.ConfigurableOption;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOption;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.IOptionValue;
import org.eclipse.birt.report.engine.emitter.config.ods.i18n.Messages;

/**
 * This class is a descriptor of ODS emitter.
 */
public class OdsEmitterDescriptor extends AbstractEmitterDescriptor {

	protected static final String TEXT_WRAPPING = "TextWrapping";
	protected static final String CHART_DPI = "ChartDpi";
	private static final String HIDE_GRIDLINES = "HideGridlines";

	protected void initOptions() {
		loadDefaultValues("org.eclipse.birt.report.engine.emitter.config.ods");
		// Initializes the option for WrappingText.
		ConfigurableOption wrappingText = initializeWrappingText();

		// Initializes the option for chart DPI.
		ConfigurableOption chartDpi = new ConfigurableOption(CHART_DPI);
		chartDpi.setDisplayName(getMessage("OptionDisplayValue.ChartDpi")); //$NON-NLS-1$
		chartDpi.setDataType(IConfigurableOption.DataType.INTEGER);
		chartDpi.setDisplayType(IConfigurableOption.DisplayType.TEXT);
		chartDpi.setDefaultValue(new Integer(192));
		chartDpi.setToolTip(getMessage("Tooltip.ChartDpi"));
		chartDpi.setDescription(getMessage("OptionDescription.ChartDpi")); //$NON-NLS-1$

		ConfigurableOption hideGridlines = new ConfigurableOption(HIDE_GRIDLINES);
		hideGridlines.setDisplayName(getMessage("OptionDisplayValue.HideGridlines")); //$NON-NLS-1$
		hideGridlines.setDataType(IConfigurableOption.DataType.BOOLEAN);
		hideGridlines.setDisplayType(IConfigurableOption.DisplayType.CHECKBOX);
		hideGridlines.setDefaultValue(Boolean.FALSE);
		hideGridlines.setToolTip(null);
		hideGridlines.setDescription(getMessage("OptionDescription.HideGridlines")); //$NON-NLS-1$

		options = new IConfigurableOption[] { wrappingText, chartDpi };
		applyDefaultValues();
	}

	protected String getMessage(String key) {
		return Messages.getString(key, locale);
	}

	protected ConfigurableOption initializeWrappingText() {
		ConfigurableOption wrappingText = new ConfigurableOption(TEXT_WRAPPING);
		wrappingText.setDisplayName(getMessage("OptionDisplayValue.WrappingText")); //$NON-NLS-1$
		wrappingText.setDataType(IConfigurableOption.DataType.BOOLEAN);
		wrappingText.setDisplayType(IConfigurableOption.DisplayType.CHECKBOX);
		wrappingText.setDefaultValue(Boolean.TRUE);
		wrappingText.setToolTip(null);
		wrappingText.setDescription(getMessage("OptionDescription.WrappingText")); //$NON-NLS-1$
		return wrappingText;
	}

	@Override
	public IConfigurableOptionObserver createOptionObserver() {
		return new OdsOptionObserver();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.config.AbstractEmitterDescriptor
	 * #getDescription()
	 */
	public String getDescription() {
		return getMessage("OdsEmitter.Description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitters.IEmitterDescriptor#getDisplayName ()
	 */
	public String getDisplayName() {
		return getMessage("OdsEmitter.DisplayName"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitters.IEmitterDescriptor#getID()
	 */
	public String getID() {
		return "org.eclipse.birt.report.engine.emitter.prototype.ods"; //$NON-NLS-1$
	}

	public String getRenderOptionName(String name) {
		assert name != null;
		if (TEXT_WRAPPING.equals(name)) {
			return IExcelRenderOption.WRAPPING_TEXT;
		}
		if (CHART_DPI.equals(name)) {
			return IRenderOption.CHART_DPI;
		}
		if (HIDE_GRIDLINES.equals(name)) {
			return IExcelRenderOption.HIDE_GRIDLINES;
		}
		return name;
	}

	/**
	 * ExcelOptionObserver
	 */
	class OdsOptionObserver extends AbstractConfigurableOptionObserver {

		public IConfigurableOption[] getOptions() {
			return options;
		}

		public IRenderOption getPreferredRenderOption() {
			EXCELRenderOption renderOption = new EXCELRenderOption();

			renderOption.setEmitterID(getID());
			renderOption.setOutputFormat("ods"); //$NON-NLS-1$

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
