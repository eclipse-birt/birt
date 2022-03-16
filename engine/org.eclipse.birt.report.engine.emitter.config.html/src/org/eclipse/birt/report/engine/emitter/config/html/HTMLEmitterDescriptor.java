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

package org.eclipse.birt.report.engine.emitter.config.html;

import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.emitter.config.AbstractConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.AbstractEmitterDescriptor;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOption;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.html.i18n.Messages;

/**
 * This class is a descriptor of html emitter.
 */
public class HTMLEmitterDescriptor extends AbstractEmitterDescriptor {

	@Override
	protected void initOptions() {

	}

	@Override
	public IConfigurableOptionObserver createOptionObserver() {
		return new HTMLOptionObserver();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitters.IEmitterDescriptor#getDescription ()
	 */
	@Override
	public String getDescription() {
		return getMessage("HTMLEmitter.Description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitters.IEmitterDescriptor#getDisplayName ()
	 */
	@Override
	public String getDisplayName() {
		return getMessage("HTMLEmitter.DisplayName"); //$NON-NLS-1$
	}

	protected String getMessage(String key) {
		return Messages.getString(key, locale);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.emitters.IEmitterDescriptor#getID()
	 */
	@Override
	public String getID() {
		return "org.eclipse.birt.report.engine.emitter.html"; //$NON-NLS-1$
	}

	class HTMLOptionObserver extends AbstractConfigurableOptionObserver {

		@Override
		public IConfigurableOption[] getOptions() {
			return null;
		}

		@Override
		public IRenderOption getPreferredRenderOption() {
			HTMLRenderOption renderOption = new HTMLRenderOption();

			renderOption.setEmitterID(getID());
			renderOption.setOutputFormat("html"); //$NON-NLS-1$

			// TODO set option values

			return renderOption;
		}

	}

}
