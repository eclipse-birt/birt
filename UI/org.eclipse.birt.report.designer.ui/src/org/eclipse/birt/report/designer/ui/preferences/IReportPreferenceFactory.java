/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.core.runtime.Preferences;

public interface IReportPreferenceFactory {

	public Preferences getReportPreference(Object adaptable);

	public boolean removeReportPreference(Object adaptable);

	public boolean saveReportPreference(Object adaptable);

	public boolean hasSpecialSettings(Object adaptable, String name);
}
