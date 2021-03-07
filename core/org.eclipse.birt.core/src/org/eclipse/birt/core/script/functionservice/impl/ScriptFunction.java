
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
package org.eclipse.birt.core.script.functionservice.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.functionservice.IScriptFunction;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionArgument;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionCategory;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;

/**
 * This class is an implementation of IScriptFuction interface.
 */

public class ScriptFunction implements IScriptFunction {
	private static final long serialVersionUID = 1L;
	//
	private String name;
	private IScriptFunctionCategory category;
	private String dataType;
	private String desc;
	private IScriptFunctionExecutor executor;
	private IScriptFunctionArgument[] argument;
	private boolean allowVarArguments;
	private boolean isStatic;
	private boolean isConstructor;
	private boolean isVisible;

	/**
	 * Constructor.
	 *
	 * @param name
	 * @param category
	 * @param argument
	 * @param dataType
	 * @param desc
	 * @param executor
	 */
	public ScriptFunction(String name, IScriptFunctionCategory category, IScriptFunctionArgument[] argument,
			String dataType, String desc, IScriptFunctionExecutor executor, boolean allowVarArguments, boolean isStatic,
			boolean isConstructor) {
		this.name = name;
		this.category = category;
		this.argument = argument;
		this.dataType = dataType;
		this.desc = desc;
		this.executor = executor;
		this.allowVarArguments = allowVarArguments;
		this.isStatic = isStatic;
		this.isConstructor = isConstructor;
		this.isVisible = true;
	}

	/**
	 * Constructor
	 *
	 * @param name
	 * @param category
	 * @param argument
	 * @param dataType
	 * @param desc
	 * @param executor
	 * @param allowVarArguments
	 * @param isStatic
	 * @param isConstructor
	 * @param isVisible
	 */
	public ScriptFunction(String name, IScriptFunctionCategory category, IScriptFunctionArgument[] argument,
			String dataType, String desc, IScriptFunctionExecutor executor, boolean allowVarArguments, boolean isStatic,
			boolean isConstructor, boolean isVisible) {
		this.name = name;
		this.category = category;
		this.argument = argument;
		this.dataType = dataType;
		this.desc = desc;
		this.executor = executor;
		this.allowVarArguments = allowVarArguments;
		this.isStatic = isStatic;
		this.isConstructor = isConstructor;
		this.isVisible = isVisible;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.script.functionservice.IScriptFunction#getArguments()
	 */
	@Override
	public IScriptFunctionArgument[] getArguments() {
		return this.argument;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.script.functionservice.IScriptFunction#getCategory()
	 */
	@Override
	public IScriptFunctionCategory getCategory() {
		return this.category;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.script.functionservice.IScriptFunction#getDataType()
	 */
	@Override
	public String getDataTypeName() {
		return this.dataType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.script.functionservice.INamedObject#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.script.functionservice.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.desc;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor#execute(
	 * java.lang.Object[])
	 */
	@Override
	public Object execute(Object[] arguments, IScriptFunctionContext context) throws BirtException {
		if (this.executor != null) {
			return this.executor.execute(arguments, context);
		}
		return null;
	}

	@Override
	public boolean allowVarArguments() {
		return this.allowVarArguments;
	}

	/**
	 * Returns whether the function is visible.
	 *
	 */
	@Override
	public boolean isVisible() {
		return this.isVisible;
	}

	@Override
	public boolean isConstructor() {
		return this.isConstructor;
	}

	@Override
	public boolean isStatic() {
		return this.isStatic;
	}

}
