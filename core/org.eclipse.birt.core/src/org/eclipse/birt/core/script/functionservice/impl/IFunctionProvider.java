package org.eclipse.birt.core.script.functionservice.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.functionservice.IScriptFunction;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionCategory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public interface IFunctionProvider {
	/**
	 * Return all the categories defined by extensions.
	 *
	 * @return
	 * @throws BirtException
	 */
	IScriptFunctionCategory[] getCategories() throws BirtException;

	/**
	 * Return the functions that defined in a category.
	 *
	 * @param categoryName
	 * @return
	 * @throws BirtException
	 */
	IScriptFunction[] getFunctions(String categoryName) throws BirtException;

	/**
	 * Register script functions to scope.
	 *
	 * @param cx
	 * @param scope
	 * @throws BirtException
	 */
	void registerScriptFunction(Context cx, Scriptable scope) throws BirtException;
}
