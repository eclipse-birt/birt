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

package org.eclipse.birt.report.engine.emitter.config.excel;

import static org.eclipse.birt.report.engine.api.IExcelRenderOption.HIDE_GRIDLINES;
import static org.eclipse.birt.report.engine.api.IExcelRenderOption.WRAPPING_TEXT;
import static org.eclipse.birt.report.engine.api.IRenderOption.CHART_DPI;

import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.emitter.config.AbstractConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.AbstractEmitterDescriptor;
import org.eclipse.birt.report.engine.emitter.config.ConfigurableOption;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOption;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.IOptionValue;
import org.eclipse.birt.report.engine.emitter.config.excel.i18n.Messages;

/**
 * This class is a descriptor of excel emitter.
 */
public class ExcelEmitterDescriptor extends AbstractEmitterDescriptor {
	@Override
	protected void initOptions() {
		loadDefaultValues("org.eclipse.birt.report.engine.emitter.config.excel");
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
		ConfigurableOption wrappingText = new ConfigurableOption(WRAPPING_TEXT);
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
		return new ExcelOptionObserver();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.emitter.config.AbstractEmitterDescriptor
	 * #getDescription()
	 */
	@Override
	public String getDescription() {
		return getMessage("ExcelEmitter.Description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitters.IEmitterDescriptor#getDisplayName ()
	 */
	@Override
	public String getDisplayName() {
		return getMessage("ExcelEmitter.DisplayName"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.emitters.IEmitterDescriptor#getID()
	 */
	@Override
	public String getID() {
		return "org.eclipse.birt.report.engine.emitter.prototype.excel"; //$NON-NLS-1$
	}

	/**
	 * ExcelOptionObserver
	 */
	class ExcelOptionObserver extends AbstractConfigurableOptionObserver {

		@Override
		public IConfigurableOption[] getOptions() {
			return options;
		}

		@Override
		public IRenderOption getPreferredRenderOption() {
			EXCELRenderOption renderOption = new EXCELRenderOption();

			renderOption.setEmitterID(getID());
			renderOption.setOutputFormat("xls"); //$NON-NLS-1$

			if (values != null && values.length > 0) {
				for (IOptionValue optionValue : values) {
					if (optionValue != null) {
						renderOption.setOption(optionValue.getName(), optionValue.getValue());
					}
				}
			}

			return renderOption;
		}
	}
}
