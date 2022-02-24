
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.engine.api.impl.GetParameterDefinitionTask.SelectionChoice;

/**
 * 
 */

public class GetParameterDefinitionTaskUtil {
	public static class SelectionChoiceUtil {
		public static SelectionChoice getSelectionChoice(Object obj) {
			return (SelectionChoice) obj;
		}

		public static Object getValue(Object obj) {
			return ((SelectionChoice) obj).getValue();
		}
	}

//	public SelectionChoice getInstance(Object obj)
//	{
//		return (SelectionChoice)obj;
//	}
}
