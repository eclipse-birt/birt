/*
 *************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
