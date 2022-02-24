/*
 *************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api;

import java.util.Collection;

/**
 * This interface defines a set of methods that relate to the operation against
 * Collections. It is currently used by cube only.
 * 
 * @author Administrator
 *
 */
public interface ICollectionConditionalExpression {
	public static int OP_IN = IConditionalExpression.OP_IN;
	public static int OP_NOT_IN = IConditionalExpression.OP_NOT_IN;

	public Collection<IScriptExpression> getExpr();

	public Collection<Collection<IScriptExpression>> getOperand();

	public int getOperator();

}
