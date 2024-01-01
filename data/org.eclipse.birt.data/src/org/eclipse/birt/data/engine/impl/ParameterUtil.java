/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import java.sql.Blob;
import java.sql.Clob;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataType.AnyType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.IParameterDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odaconsumer.ParameterHint;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Merge the paramter definition and evaluate the expression of paramter
 */
public class ParameterUtil {
	private Scriptable outerScope;
	private DataSetRuntime dsRT;
	private IQueryDefinition queryDefn;
	private Scriptable scope;

	private Logger logger = Logger.getLogger(ParameterUtil.class.getName());
	private ScriptContext context;

	/**
	 * @param outerResults
	 * @param dsRT
	 * @param queryDefn
	 * @param scope
	 */
	public ParameterUtil(Scriptable outerScope, DataSetRuntime dsRT, IQueryDefinition queryDefn, Scriptable scope,
			ScriptContext context) {
		Object[] params = { outerScope, dsRT, queryDefn, scope };
		logger.entering(ParameterUtil.class.getName(), "ParameterUtil", //$NON-NLS-1$
				params);
		this.outerScope = outerScope;
		this.dsRT = dsRT;
		this.queryDefn = queryDefn;
		this.scope = scope;
		this.context = context;
		logger.exiting(ParameterUtil.class.getName(), "ParameterUtil"); //$NON-NLS-1$
	}

	/**
	 * Resolve parameter bindings and return a Collection of ParameterHints, which
	 * merged information obtained from query parameter binding and the data set
	 * parameter definition
	 *
	 */
	public Collection<ParameterHint> resolveDataSetParameters(boolean evaluateValue) throws DataException {
		List<IParameterDefinition> paramDefns = this.dsRT.getParameters();
		int nParams = paramDefns == null ? 0 : paramDefns.size();

		// array of parameter hints
		ParameterHint[] paramHints = new ParameterHint[nParams];
		// whether corresponding item in paramHints has been bound
		boolean[] bindingResolved = new boolean[nParams];

		// First create param hints for all data set params
		for (int i = 0; i < nParams; i++) {
			IParameterDefinition paramDefn = paramDefns.get(i);
			paramHints[i] = createParameterHint(paramDefn, paramDefn.getDefaultInputValue());
			bindingResolved[i] = false;

			// Can the data set RT provide an input parameter value? (this has the highest
			// priority, over bindings)
			if (paramDefn.isInputMode() && paramDefn.getName() != null) {
				Object paramValue = DataSetRuntime.UNSET_VALUE;
				try {
					paramValue = this.dsRT.getInputParameterValue(paramDefn.getName());
				} catch (BirtException e) {
					// This is unexpected; the parameter must be in the list
					assert false;
					throw DataException.wrap(e);
				}

				if (paramValue != DataSetRuntime.UNSET_VALUE) {
					if (paramHints[i].getDataType() == null) // for AnyType parameter
					{
						if (paramValue != null) {
							Class clazz = paramValue.getClass();
							paramHints[i].setDataType(clazz);
						} else {
							// AnyType parameter and the parameter value is null
							paramHints[i].setDataType(String.class);
						}
					}
//					String paramValueStr = this.getParameterValueString( paramHints[i].getDataType( ),
//							paramValue );
					paramHints[i].setDefaultInputValue(paramValue);
					bindingResolved[i] = true;
				}
			}
		}

		if (evaluateValue) {
			// Resolve parameter bindings

			// Parameter values are determined in the following order of priority
			// (1) Input param values set by scripts (already resolved above)
			// (2) Query parameter bindings
			// (3) Data set parameter bindings

			resolveParameterBindings(this.queryDefn.getInputParamBindings(), paramHints, bindingResolved,
					Context.getCurrentContext());

			resolveParameterBindings(this.dsRT.getInputParamBindings(), paramHints, bindingResolved,
					Context.getCurrentContext());
		}

		return Arrays.asList(paramHints);
	}

	/**
	 * Resolve a list of parameter bindings and update the hints
	 *
	 * @param cx JS context to evaluate binding. If null, binding does not need to
	 *           be evaluated
	 */
	private void resolveParameterBindings(Collection bindings, ParameterHint[] paramHints, boolean[] bindingResolved,
			Context cx) throws DataException {
		if (bindings == null) {
			return;
		}

		Iterator it = bindings.iterator();
		while (it.hasNext()) {
			resolveParameterBinding((IInputParameterBinding) it.next(), paramHints, bindingResolved, cx);
		}
	}

	/**
	 * Resolve a parameter binding and update the hints
	 *
	 * @param cx JS context to evaluate binding. If null, binding does not need to
	 *           be evaluated
	 */
	private void resolveParameterBinding(IInputParameterBinding binding, ParameterHint[] paramHints,
			boolean[] bindingResolved, Context cx) throws DataException {
		// Find the hint which matches the binding
		int i = findParameterHint(paramHints, binding.getPosition(), binding.getName());

		if (i < 0) {
			// A binding exists but the data set has no definition for the
			// bound parameter, log an error and ignore the param
			if (logger != null) {
				logger.warning("Ignored binding defined for non-exising data set parameter: " //$NON-NLS-1$
						+ "name=" //$NON-NLS-1$
						+ binding.getName() + ", position=" //$NON-NLS-1$
						+ binding.getPosition());
			}
		}

		// Do not set binding value if the parameter has already been resolved
		// (e.g., query binding has already been evaluated for this param, and
		// we are now checking data set binding for the same param )
		else if (!bindingResolved[i]) {
			Object value = (cx != null) ? evaluateInputParameterValue(this.scope, cx, binding) : binding.getExpr(); // binding.getExpr()???

			if (paramHints[i].getDataType() == null) // for AnyType parameter
			{
				if (value != null) {
					Class clazz = value.getClass();
					paramHints[i].setDataType(clazz);
				} else {
					// AnyType parameter and the parameter value is null
					paramHints[i].setDataType(String.class);
				}
			}
//			String valueStr = getParameterValueString( paramHints[i].getDataType( ),
//						value );
			paramHints[i].setDefaultInputValue(value);
			bindingResolved[i] = true;

			// Also give the value to data set RT for script access
			if (cx != null && paramHints[i].isInputMode() && paramHints[i].getName() != null) {
				try {
					this.dsRT.setInputParameterValue(paramHints[i].getName(), value);
				} catch (BirtException e) {
					// Unexpected
					assert false;
					throw DataException.wrap(e);
				}
			}
		}
	}

	/**
	 * Find index of matching parameter hint in paramHints array, based on param
	 * name or position. Returns index of param hint found in array, or -1 if no
	 * match
	 */
	private int findParameterHint(ParameterHint[] hints, int position, String name) {
		for (int i = 0; i < hints.length; i++) {
			ParameterHint paramHint = hints[i];
			if (position <= 0) {
				if (paramHint.getName().equalsIgnoreCase(name)) {
					return i;
				}
			} else if (paramHint.getPosition() == position) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param scope
	 * @param cx
	 * @param iParamBind
	 * @return
	 * @throws DataException
	 */
	private Object evaluateInputParameterValue(Scriptable scope, Context cx, IInputParameterBinding iParamBind)
			throws DataException {
		// Evaluate Expression:
		// If the expression has been prepared,
		// use its handle to getValue() from outerResultIterator
		// else use Rhino to evaluate in corresponding scope
		Object evaluateResult = null;

		try {
			if (iParamBind.getExpr() instanceof IScriptExpression) {

				ScriptContext evalContext = this.outerScope == null ? context : context.newContext(this.outerScope);
				// for java script, need to set its compiled handle
				if (iParamBind.getExpr().getHandle() == null
						&& !(BaseExpression.constantId.equals(iParamBind.getExpr().getScriptId()))) {
					iParamBind.getExpr().setHandle(evalContext.compile("javascript", null, 0,
							((IScriptExpression) iParamBind.getExpr()).getText()));
				}
				evaluateResult = ScriptEvalUtil.evalExpr(iParamBind.getExpr(), evalContext, ScriptExpression.defaultID,
						0);
			}
		} catch (BirtException e) {
			// do not expect a exception here.
			DataException dataEx = new DataException(ResourceConstants.UNEXPECTED_ERROR, e);
			if (logger != null) {
				logger.logp(Level.FINE, PreparedOdaDSQuery.class.getName(), "getMergedParameters", //$NON-NLS-1$
						"Error occurs in IQueryResults.getResultIterator()", //$NON-NLS-1$
						e);
			}
			throw dataEx;
		}
		// TODO throw DataException
// if( evaluateResult instanceof DataExceptionMocker )
//			{
//				BirtException e = ((DataExceptionMocker) evaluateResult).getCause( );
//				DataException dataEx = new DataException( ResourceConstants.UNEXPECTED_ERROR,
//						e );
//				if ( logger != null )
//					logger.logp( Level.FINE,
//							PreparedOdaDSQuery.class.getName( ),
//							"getMergedParameters",
//							"Error occurs in IQueryResults.getResultIterator()",
//							e );
//				throw dataEx;
//			}
//		if ( evaluateResult == null )
//			throw new DataException( ResourceConstants.DEFAULT_INPUT_PARAMETER_VALUE_CANNOT_BE_NULL );
		return evaluateResult;
	}

	/**
	 * Create a parameter hint based on Parameter definition and value
	 *
	 * @param paramDefn
	 * @param evaValue
	 */
	private ParameterHint createParameterHint(IParameterDefinition paramDefn, Object paramValue) throws DataException {
		ParameterHint parameterHint = new ParameterHint(paramDefn.getName(), paramDefn.isInputMode(),
				paramDefn.isOutputMode());
		if (paramDefn.getPosition() > 0) {
			parameterHint.setPosition(paramDefn.getPosition());
		}

		parameterHint.setNativeName(paramDefn.getNativeName());

		Class dataTypeClass = DataType.getClass(paramDefn.getType());

		parameterHint.setNativeDataType(paramDefn.getNativeType());
		parameterHint.setIsInputOptional(paramDefn.isInputOptional());
		if (parameterHint.isInputMode()) {
			parameterHint.setDefaultInputValue(paramValue);
		}
		parameterHint.setIsNullable(paramDefn.isNullable());
		// ParameterHint does not support AnyType
		// the real type for AnyType is determined by the real parameter value
		if (dataTypeClass != AnyType.class) {
			if (dataTypeClass == Blob.class) {
				parameterHint.setDataType(IBlob.class);
			} else if (dataTypeClass == Clob.class) {
				parameterHint.setDataType(IClob.class);
			} else {
				parameterHint.setDataType(dataTypeClass);
			}
		}
		return parameterHint;
	}

}
