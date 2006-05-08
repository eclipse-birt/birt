/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content;

public interface IAutoTextContent extends ITextContent
{
	public static final int TOTAL_PAGE = 0;
	public static final int PAGE_NUMBER = 1;
	
	void setType ( int type );
	
	int getType ( );
}
