/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.soapengine.processor;

public interface IBirtOperators {
	String Operator_QueryExport_Literal = "QueryExport"; //$NON-NLS-1$
	String Operator_GetToc_Literal = "GetToc"; //$NON-NLS-1$
	String Operator_GetPage_Literal = "GetPage"; //$NON-NLS-1$
	String Operator_GetCascadeParameter_Literal = "GetCascadingParameter"; //$NON-NLS-1$
	String Operator_ChangeParameter_Literal = "ChangeParameter"; //$NON-NLS-1$
	String Operator_CacheParameter_Literal = "CacheParameter"; //$NON-NLS-1$
	String Operator_CancelTask_Literal = "CancelTask"; //$NON-NLS-1$
	String Operator_GetPageAll_Literal = "GetPageAll"; //$NON-NLS-1$
}
