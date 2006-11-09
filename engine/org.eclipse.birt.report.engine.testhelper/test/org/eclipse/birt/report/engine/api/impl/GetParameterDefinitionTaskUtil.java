
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.engine.api.impl.GetParameterDefinitionTask.SelectionChoice;

/**
 * 
 */

public class GetParameterDefinitionTaskUtil
{
	public static class SelectionChoiceUtil
	{
		public static SelectionChoice getSelectionChoice(Object obj)
		{
			return (SelectionChoice)obj;
		}
		
		public static Object getValue(Object obj)
		{
			return ((SelectionChoice)obj).getValue( );
		}
	}
	
//	public SelectionChoice getInstance(Object obj)
//	{
//		return (SelectionChoice)obj;
//	}
}
