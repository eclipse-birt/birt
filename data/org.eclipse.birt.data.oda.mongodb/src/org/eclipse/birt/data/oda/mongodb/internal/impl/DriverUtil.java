/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
 *
 *************************************************************************
 */

package org.eclipse.birt.data.oda.mongodb.internal.impl;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.bson.BsonType;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.bson.types.BSONTimestamp;
import org.bson.types.Binary;
import org.bson.types.Code;
import org.bson.types.CodeWithScope;
import org.bson.types.Decimal128;
import org.bson.types.MaxKey;
import org.bson.types.MinKey;
import org.bson.types.ObjectId;
import org.bson.types.Symbol;
import org.eclipse.birt.data.oda.mongodb.impl.MongoDBDriver;
import org.eclipse.birt.data.oda.mongodb.nls.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Internal constant variables and utilities.
 */
public final class DriverUtil {
	public static final String EMPTY_STRING = ""; //$NON-NLS-1$

	// trace logging
	private static Logger sm_logger = Logger.getLogger(MongoDBDriver.ODA_DATA_SOURCE_ID);
	@SuppressWarnings("deprecation")
	private static final JsonWriterSettings JSON_SETTINGS = JsonWriterSettings.builder().outputMode(JsonMode.STRICT)
			.build();

	public static Logger getLogger() {
		return sm_logger;
	}

	static Object parseJSONExpr(String jsonExpr) throws OdaException {
		try {
			// A containing document also allows top-level arrays and scalar expressions.
			return Document.parse("{\"value\":" + jsonExpr + "}").get("value"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} catch (RuntimeException ex) {
			String errMsg = Messages.bind(Messages.driverUtil_parsingError, jsonExpr);
			DriverUtil.getLogger().log(Level.INFO, errMsg, ex); // caller may choose to ignore it; log at INFO level

			OdaException newEx = new OdaException(errMsg);
			newEx.initCause(ex);
			throw newEx;
		}
	}

	static Object parseExprToDBObject(String jsonExpr) throws OdaException {
		Object parsedObj = parseJSONExpr(jsonExpr);
		if (parsedObj instanceof Document || parsedObj instanceof List<?>) {
			return parsedObj;
		}
		throw new OdaException(Messages.bind(Messages.driverUtil_invalidExpr,
				parsedObj == null ? "null" : parsedObj.getClass().getSimpleName())); //$NON-NLS-1$
	}

	/**
	 * Serializes a document or array using the extended JSON format used by the
	 * legacy driver, rather than Java collection {@code toString()}.
	 */
	public static String toJson(Object value) {
		String json = new Document("value", value).toJson(JSON_SETTINGS); //$NON-NLS-1$
		return json.substring(json.indexOf(':') + 1, json.lastIndexOf('}')).trim();
	}

	static int getBsonType(Object value) {
		if (value == null) {
			return BsonType.NULL.getValue();
		}
		if (value instanceof Integer || value instanceof Short || value instanceof Byte) {
			return BsonType.INT32.getValue();
		}
		if (value instanceof Long) {
			return BsonType.INT64.getValue();
		}
		if (value instanceof Decimal128) {
			return BsonType.DECIMAL128.getValue();
		}
		if (value instanceof Number) {
			return BsonType.DOUBLE.getValue();
		}
		if (value instanceof String || value instanceof Character) {
			return BsonType.STRING.getValue();
		}
		if (value instanceof List<?> || (value.getClass().isArray() && !(value instanceof byte[]))) {
			return BsonType.ARRAY.getValue();
		}
		if (value instanceof Map<?, ?>) {
			return BsonType.DOCUMENT.getValue();
		}
		if (value instanceof Boolean) {
			return BsonType.BOOLEAN.getValue();
		}
		if (value instanceof java.util.Date) {
			return BsonType.DATE_TIME.getValue();
		}
		if (value instanceof Binary || value instanceof byte[]) {
			return BsonType.BINARY.getValue();
		}
		if (value instanceof ObjectId) {
			return BsonType.OBJECT_ID.getValue();
		}
		if (value instanceof BSONTimestamp) {
			return BsonType.TIMESTAMP.getValue();
		}
		if (value instanceof Pattern) {
			return BsonType.REGULAR_EXPRESSION.getValue();
		}
		if (value instanceof CodeWithScope) {
			return BsonType.JAVASCRIPT_WITH_SCOPE.getValue();
		}
		if (value instanceof Code) {
			return BsonType.JAVASCRIPT.getValue();
		}
		if (value instanceof Symbol) {
			return BsonType.SYMBOL.getValue();
		}
		if (value instanceof MinKey) {
			return BsonType.MIN_KEY.getValue();
		}
		if (value instanceof MaxKey) {
			return BsonType.MAX_KEY.getValue();
		}
		return -1; // unrecognized Java value, matching the legacy driver's sentinel
	}

}
