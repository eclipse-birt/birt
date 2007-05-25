/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document;

public interface IPageHintConstant
{

	/**
	 * version used before 2.0
	 */
	static final int VERSION_0 = 0;
	/**
	 * this is the version used before 2.1.3 and 2.2RC0
	 */
	static final int VERSION_1 = 1;

	/**
	 * this is the version used by 2.1.3 and 2.2RC0 release.
	 */
	static final int VERSION_2 = 2;
	
	/**
	 * this is the version used by 2.2RC2 release.
	 */
	static final int VERSION_3 = 3;
	/**
	 * the current release is version 3.
	 */
	static final int VERSION = VERSION_3;

}
