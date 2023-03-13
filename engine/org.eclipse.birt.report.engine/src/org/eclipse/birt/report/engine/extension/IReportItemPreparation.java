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

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.core.exception.BirtException;

/**
 * IReportItemPreparation is initially designed for extended item, which handles
 * its nested items. So it shouldn't prepare itself again, otherwise an infinite
 * loop occurs.
 *
 */
public interface IReportItemPreparation {

	void init(IReportItemPreparationInfo info);

	void prepare() throws BirtException;
}
