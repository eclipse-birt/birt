/*
 *************************************************************************
 * Copyright (c) 2004-2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.script;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Implements Javascript ColumnDefn object, which wraps one field in a odi
 * IResultClass.
 */
public class JSColumnDefn extends ScriptableObject {
	private static String INDEX = "index";
	private static String NAME = "name";
	private static String TYPE = "type";
	private static String NATIVE_TYPE = "nativeType";
	private static String LABEL = "label";
	private static String ALIAS = "alias";

	private static String INTEGER = "integer";
	private static String FLOAT = "float";
	private static String DECIMAL = "decimal";
	private static String BOOLEAN = "boolean";
	private static String STRING = "string";
	private static String DATETIME = "dateTime";

	private static String INTEGER_VAL = "integer";
	private static String FLOAT_VAL = "float";
	private static String DECIMAL_VAL = "decimal";
	private static String BOOLEAN_VAL = "boolean";
	private static String STRING_VAL = "string";
	private static String DATETIME_VAL = "dateTime";

	private static String[] propNames = { INDEX, NAME, TYPE, NATIVE_TYPE, LABEL, ALIAS, INTEGER, FLOAT, DECIMAL,
			BOOLEAN, STRING, DATETIME };

	private static HashSet propNameSet = new HashSet(Arrays.asList(propNames));

	private IResultClass resultClass;
	private int fieldIndex;

	private static Logger logger = Logger.getLogger(JSColumnDefn.class.getName());
	private static final long serialVersionUID = -4456827193707814588L;

	/**
	 * Constructor
	 *
	 * @param index 1-based index of column in resultClass
	 */
	JSColumnDefn(IResultClass resultClass, int index) {
		logger.entering(JSColumnDefn.class.getName(), "JSColumnDefn");
		assert resultClass != null;
		assert index > 0 && index <= resultClass.getFieldCount();
		this.resultClass = resultClass;
		this.fieldIndex = index;

		// This object is not modifiable in any way
		sealObject();
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#get(java.lang.String,
	 *      org.mozilla.javascript.Scriptable)
	 */
	@Override
	public Object get(String name, Scriptable start) {
		logger.entering(JSColumnDefn.class.getName(), "get", name);
		if (!propNameSet.contains(name)) {
			logger.exiting(JSColumnDefn.class.getName(), "get", super.get(name, start));
			return super.get(name, start);
		}

		// Static properties
		if (name.equals(INTEGER)) {
			logger.exiting(JSColumnDefn.class.getName(), "get", INTEGER_VAL);
			return INTEGER_VAL;
		}
		if (name.equals(FLOAT)) {
			logger.exiting(JSColumnDefn.class.getName(), "get", FLOAT_VAL);
			return FLOAT_VAL;
		}
		if (name.equals(DECIMAL)) {
			logger.exiting(JSColumnDefn.class.getName(), "get", DECIMAL_VAL);
			return DECIMAL_VAL;
		}
		if (name.equals(BOOLEAN)) {
			logger.exiting(JSColumnDefn.class.getName(), "get", BOOLEAN_VAL);
			return BOOLEAN_VAL;
		}
		if (name.equals(STRING)) {
			logger.exiting(JSColumnDefn.class.getName(), "get", STRING_VAL);
			return STRING_VAL;
		}
		if (name.equals(DATETIME)) {
			logger.exiting(JSColumnDefn.class.getName(), "get", DATETIME_VAL);
			return DATETIME_VAL;
		}

		try {
			// Result class properties
			if (name.equals(INDEX)) {
				logger.exiting(JSColumnDefn.class.getName(), "get", Integer.valueOf(fieldIndex));
				return Integer.valueOf(fieldIndex);
			}
			if (name.equals(NAME)) {
				logger.exiting(JSColumnDefn.class.getName(), "get", resultClass.getFieldName(fieldIndex));
				return resultClass.getFieldName(fieldIndex);
			}
			if (name.equals(TYPE)) {
				Class c = resultClass.getFieldValueClass(fieldIndex);
				if (c == Integer.class) {
					logger.exiting(JSColumnDefn.class.getName(), "get", INTEGER_VAL);
					return INTEGER_VAL;
				}
				if (c == Double.class) {
					logger.exiting(JSColumnDefn.class.getName(), "get", FLOAT_VAL);
					return FLOAT_VAL;
				}
				if (c == String.class) {
					logger.exiting(JSColumnDefn.class.getName(), "get", STRING_VAL);
					return STRING_VAL;
				}
				if (c == BigDecimal.class) {
					logger.exiting(JSColumnDefn.class.getName(), "get", DECIMAL_VAL);
					return DECIMAL_VAL;
				}
				if (c == Boolean.class) {
					logger.exiting(JSColumnDefn.class.getName(), "get", BOOLEAN_VAL);
					return BOOLEAN_VAL;
				}
				if (c == Date.class || c == Time.class || c == Timestamp.class) {
					logger.exiting(JSColumnDefn.class.getName(), "get", DATETIME_VAL);
					return DATETIME_VAL;
				}
				// unknown type
				logger.exiting(JSColumnDefn.class.getName(), "get", null);
				return null;
			}

			if (name.equals(NATIVE_TYPE)) {
				// TODO: need IResultClass to return the native data type string
				logger.exiting(JSColumnDefn.class.getName(), "get", null);
				return null;
			}

			if (name.equals(LABEL)) {
				logger.exiting(JSColumnDefn.class.getName(), "get", resultClass.getFieldLabel(fieldIndex));
				return resultClass.getFieldLabel(fieldIndex);
			}
			if (name.equals(ALIAS)) {
				logger.exiting(JSColumnDefn.class.getName(), "get", resultClass.getFieldAlias(fieldIndex));
				return resultClass.getFieldAlias(fieldIndex);
			}
		} catch (DataException e) {
			logger.logp(Level.FINER, JSColumnDefn.class.getName(), "get", e.getMessage(), e);
			logger.exiting(JSColumnDefn.class.getName(), "get", null);
			return null;
		}

		// Should never get here
		assert false;
		logger.exiting(JSColumnDefn.class.getName(), "get", null);
		return null;
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#getClassName()
	 */
	@Override
	public String getClassName() {
		return "ColumnDefn";
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#getIds()
	 */
	@Override
	public Object[] getIds() {
		return propNames;
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#has(java.lang.String,
	 *      org.mozilla.javascript.Scriptable)
	 */
	@Override
	public boolean has(String name, Scriptable start) {
		logger.entering(JSColumnDefn.class.getName(), "has", name);
		if (propNameSet.contains(name)) {
			logger.exiting(JSColumnDefn.class.getName(), "has", Boolean.TRUE);
			return true;
		} else {
			logger.exiting(JSColumnDefn.class.getName(), "has", Boolean.valueOf(super.has(name, start)));
			return super.has(name, start);
		}
	}
}
