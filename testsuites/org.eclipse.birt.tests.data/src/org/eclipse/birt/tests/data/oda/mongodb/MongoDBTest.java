/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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
package org.eclipse.birt.tests.data.oda.mongodb;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import org.bson.BsonType;
import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.BSONTimestamp;
import org.eclipse.birt.data.oda.mongodb.internal.impl.DriverUtil;
import org.eclipse.birt.data.oda.mongodb.internal.impl.MDbMetaData;
import org.eclipse.birt.data.oda.mongodb.internal.impl.MDbMetaData.DocumentsMetaData;
import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryModel;
import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryProperties;
import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryProperties.CommandOperationType;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.mongodb.client.MongoCursor;

import junit.framework.TestCase;

public class MongoDBTest extends TestCase {
	public void testDocumentAndArrayJson() {
		Document document = new Document("nested", new Document("value", "child"))
				.append("values", Arrays.asList(1, 2)).append("date", new Date(0))
				.append("binary", new Binary(new byte[] { 1, 2 }));
		Document restored = Document.parse(DriverUtil.toJson(document));
		assertEquals(document, restored);
		Document array = Document.parse("{ \"values\": " + DriverUtil.toJson(Arrays.asList(document, null)) + " }");
		assertEquals(Arrays.asList(document, null), array.get("values"));
	}

	public void testQueryPropertiesRoundTrip() throws Exception {
		QueryProperties properties = new QueryProperties("orders");
		properties.setOperationType(CommandOperationType.AGGREGATE);
		properties.setOperationExpression("{ $match: { amount: { $gt: 10 } } }, { $limit: 5 }");
		properties.setQueryReadPreference("secondaryPreferred");
		properties.setBatchSize(25);
		properties.setNumDocsToSkip(2);
		properties.setAutoFlattening(true);
		properties.setFindQueryExpr("{ status: 'active' }");

		QueryProperties restored = QueryProperties.deserialize(properties.serialize());
		assertEquals("orders", restored.getCollectionName());
		assertEquals(properties.getOperationType(), restored.getOperationType());
		assertEquals(properties.getOperationExpression(), restored.getOperationExpression());
		assertEquals("secondaryPreferred", restored.getQueryReadPreference().getName());
		assertEquals(Integer.valueOf(25), restored.getBatchSize());
		assertEquals(Integer.valueOf(2), restored.getNumDocsToSkip());
		assertTrue(restored.isAutoFlattening());
		assertEquals(properties.getFindQueryExpr(), restored.getFindQueryExpr());
	}

	public void testQueryAndCommandSyntax() throws Exception {
		QueryModel.validateQuerySyntax("{ amount: { $gte: 10 }, status: 'active' }");
		QueryModel.validateSortExprSyntax("{ amount: -1 }");
		QueryModel.validateCommandSyntax(CommandOperationType.AGGREGATE,
				"[{ $match: { status: 'active' } }, { $group: { _id: '$status', total: { $sum: 1 } } }]");
		QueryModel.validateCommandSyntax(CommandOperationType.RUN_DB_COMMAND, "{ count: 'orders' }");
		try {
			QueryModel.validateQuerySyntax("{ amount: ");
			fail("Invalid JSON must be rejected");
		} catch (OdaException expected) {
			assertNotNull(expected.getCause());
		}
	}

	public void testNativeTypesAndNestedMetadata() {
		Document document = new Document("text", "value").append("integer", 1).append("long", 2L)
				.append("double", 3.5).append("boolean", true).append("null", null)
				.append("date", new Date(0)).append("timestamp", new BSONTimestamp(1, 2))
				.append("binary", new Binary(new byte[] { 1, 2 }))
				.append("bytes", new byte[] { 3, 4 })
				.append("nested", new Document("value", "child"))
				.append("numbers", Arrays.asList(1, 2))
				.append("documents", Collections.singletonList(new Document("value", 1)));
		DocumentsMetaData metadata = MDbMetaData.getMetaData(Collections.singletonList(document));
		assertType(metadata, "text", BsonType.STRING);
		assertType(metadata, "integer", BsonType.INT32);
		assertType(metadata, "long", BsonType.INT64);
		assertType(metadata, "double", BsonType.DOUBLE);
		assertType(metadata, "boolean", BsonType.BOOLEAN);
		assertType(metadata, "null", BsonType.NULL);
		assertType(metadata, "date", BsonType.DATE_TIME);
		assertType(metadata, "timestamp", BsonType.TIMESTAMP);
		assertType(metadata, "binary", BsonType.BINARY);
		assertType(metadata, "bytes", BsonType.BINARY);
		assertTrue(metadata.getFieldMetaData("nested").hasChildDocuments());
		assertEquals("nested.value",
				metadata.getFieldMetaData("nested").getChildMetaData().getFieldMetaData("value").getFullName());
		assertTrue(metadata.getFieldMetaData("numbers").isArrayOfScalarValues());
		assertTrue(metadata.getFieldMetaData("documents").isArrayOfDocuments());
	}

	@SuppressWarnings("unchecked")
	public void testMetadataClosesLimitedCursor() {
		Iterator<Document> documents = Arrays.asList(new Document("first", 1), new Document("second", 2)).iterator();
		boolean[] closed = { false };
		MongoCursor<Document> cursor = (MongoCursor<Document>) Proxy.newProxyInstance(
				MongoCursor.class.getClassLoader(), new Class<?>[] { MongoCursor.class }, (proxy, method, args) -> {
					switch (method.getName()) {
					case "hasNext":
						return documents.hasNext();
					case "next":
						return documents.next();
					case "close":
						closed[0] = true;
						return null;
					default:
						throw new UnsupportedOperationException(method.getName());
					}
				});
		DocumentsMetaData metadata = MDbMetaData.getMetaData(() -> cursor, 1);
		assertEquals(Collections.singletonList("first"), metadata.getFieldNames());
		assertTrue(closed[0]);
		assertTrue(documents.hasNext());
	}

	private void assertType(DocumentsMetaData metadata, String fieldName, BsonType type) {
		assertTrue(fieldName, metadata.getFieldMetaData(fieldName).getNativeDataTypes().contains(type.getValue()));
	}
}
