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

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.core.exception.BirtException;

/**
 * IReportItemPreparation is initially designed for extended item, which handles
 * its nested items. So it shouldn't prepare itself again, otherwise an infinite
 * loop occurs.
 * 
 */
public interface IReportItemPreparation {

	public void init(IReportItemPreparationInfo info);

	public void prepare() throws BirtException;
}
