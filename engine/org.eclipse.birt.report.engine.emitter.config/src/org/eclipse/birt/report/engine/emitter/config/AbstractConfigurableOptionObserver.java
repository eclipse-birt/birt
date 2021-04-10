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

package org.eclipse.birt.report.engine.emitter.config;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.emitter.config.i18n.Messages;

/**
 * AbstractConfigurableOptionObserver
 */
public abstract class AbstractConfigurableOptionObserver implements IConfigurableOptionObserver {

	private static final String CHART_DPI = "ChartDpi"; //$NON-NLS-1$
	protected IOptionValue[] values;

	public IOptionValue[] getOptionValues() {
		return values;
	}

	public IConfigurableOption[] getOptions() {
		return null;
	}

	public IRenderOption getPreferredRenderOption() {
		return null;
	}

	public boolean update(IOptionValue... values) {
		this.values = values;

		return false;
	}

	public List validate(IOptionValue... values) {
		List<BirtException> errors = new LinkedList<BirtException>();
		for (IOptionValue option : values) {
			if (CHART_DPI.equals(option.getName())) {
				Object value = option.getValue();
				if (value != null) {
					int resolution = 0;
					if (value instanceof Number) {
						resolution = ((Number) value).intValue();
					} else {
						try {
							resolution = Integer.parseInt(value.toString());
						} catch (NumberFormatException ex) {
							errors.add(new BirtException(Messages.getString("INVALID_CHART_DPI"))); //$NON-NLS-1$
							continue;
						}
					}
					if (resolution < 96) {
						errors.add(new BirtException(Messages.getString("TOO_SMALL_CHART_DPI"))); //$NON-NLS-1$
					}
				}
			}
		}
		return errors;
	}

}
