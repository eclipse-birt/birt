/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.data.dte;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.model.api.DesignFileException;

import junit.framework.TestCase;

public class DataEngineTest extends TestCase {

	private static final int MODE_GENERATION = 0;
	private static final int MODE_PRESENTATION = 1;

	private String SINGLE_DATASET_DESIGN = "SingleDataSet.xml";
	private String NESTED_DATASET_DESIGN = "NestedDataSet.xml";
	private String SUBQUERY_DATASET_DESIGN = "SubqueryDataSet.xml";

	private String ARCHIVE_PATH = "docArchive";
	private String ARCHIVE_METANAME = "metaName";

	private Report getReport(String designName) throws DesignFileException {
		InputStream in = this.getClass().getResourceAsStream(designName);
		assertTrue(in != null);
		ReportParser parser = new ReportParser();
		Report report = parser.parse("", in);
		assertTrue(report != null);

		return report;
	}

	IDocArchiveWriter archWriter;
	IDocArchiveReader archReader;

	private IDataEngine getDataEngine(Report report, String archivePath, String archiveMetaName, int mode)
			throws Exception {
		ExecutionContext context = new ExecutionContext();

		if (mode == MODE_GENERATION) {
			archWriter = new FileArchiveWriter(archivePath);
			archWriter.initialize();
			DataGenerationEngine dataGenEngine = new DataGenerationEngine(null, context, archWriter);
			dataGenEngine.prepare(report, null);
			return dataGenEngine;
		} else if (mode == MODE_PRESENTATION) {
			archReader = new FileArchiveReader(archivePath);
			archReader.open();
			DataPresentationEngine dataPresEngine = new DataPresentationEngine(null, context, archReader, false);
			dataPresEngine.prepare(report, null);
			return dataPresEngine;
		} else {
			return null;
		}
	}

	protected String loadResource(String resourceName) throws Exception {
		InputStream in = this.getClass().getResourceAsStream(resourceName);
		assertTrue(in != null);
		byte[] buffer = new byte[in.available()];
		in.read(buffer);
		return new String(buffer);
	}

	private void delete(File dir) {
		if (dir.isFile()) {
			dir.delete();
		}

		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 1; i < files.length; i++) {
				delete(files[i]);
			}
			dir.delete();
		}

	}

	@Override
	public void tearDown() {
		File file = new File(ARCHIVE_PATH);
		if (file.exists()) {
			delete(file);
		}
		file = new File(ARCHIVE_METANAME);
		if (file.exists()) {
			delete(file);
		}

	}

	public void test() throws Exception {
		doTestSingleQGeneration();
		doTestSingleQPresentation();
		doTestNestedQGeneration();
		doTestNestedQPresentation();
		doTestSubqueryGeneration();
		doTestSubqueryPresentation();
	}

	public void doTestSingleQGeneration() throws Exception {
		Report report = getReport(SINGLE_DATASET_DESIGN);
		IDataEngine dataEngine = getDataEngine(report, ARCHIVE_PATH, ARCHIVE_METANAME, MODE_GENERATION);

		Iterator iter = report.getQueries().iterator();
		IQueryResultSet resultSet = null;

		String goldenFile = "SingleDataSet.txt";
		String goldenStr = loadResource(goldenFile);
		StringBuilder resultStr = new StringBuilder();
		while (iter.hasNext()) {
			IQueryDefinition query = (IQueryDefinition) iter.next();
			Map map = query.getBindings();
			String[] columns = (String[]) map.keySet().toArray(new String[] {});
			Arrays.sort(columns);
			resultSet = (IQueryResultSet) dataEngine.execute(query);
			int i = 0;
			while (resultSet.next() && i < 3) {
				for (int j = 0; j < columns.length; j++) {
					resultStr.append(resultSet.getResultIterator().getString(columns[j]));
				}
				i++;
			}
		}
		resultSet.close();
		dataEngine.shutdown();
		archWriter.finish();
		assertEquals(goldenStr, resultStr.toString());
	}

	public void doTestSingleQPresentation() throws Exception {
		Report report = getReport(SINGLE_DATASET_DESIGN);
		IDataEngine dataEngine = getDataEngine(report, ARCHIVE_PATH, ARCHIVE_METANAME, MODE_PRESENTATION);

		Iterator iter = report.getQueries().iterator();
		IQueryResultSet resultSet = null;

		String goldenFile = "SingleDataSet.txt";
		String goldenStr = loadResource(goldenFile);
		StringBuilder resultStr = new StringBuilder();
		while (iter.hasNext()) {
			IQueryDefinition query = (IQueryDefinition) iter.next();
			Map map = query.getBindings();
			String[] columns = (String[]) map.keySet().toArray(new String[] {});
			Arrays.sort(columns);

			resultSet = (IQueryResultSet) dataEngine.execute(query);
			int i = 0;
			while (resultSet.next() && i < 3) {
				for (int j = 0; j < columns.length; j++) {
					resultStr.append(resultSet.getResultIterator().getString(columns[j]));
				}
				i++;
			}
		}
		resultSet.close();
		dataEngine.shutdown();
		archReader.close();
		assertEquals(goldenStr, resultStr.toString());
	}

	public void doTestNestedQGeneration() throws Exception {
		Report report = getReport(NESTED_DATASET_DESIGN);
		IDataEngine dataEngine = getDataEngine(report, ARCHIVE_PATH, ARCHIVE_METANAME, MODE_GENERATION);

		Iterator iter = report.getQueries().iterator();

		String goldenFile = "NestedDataSet.txt";
		String goldenStr = loadResource(goldenFile);
		StringBuilder resultStr = new StringBuilder();
		// now the sequence of query be stored in report has been changed.
		// the children will first be stored then the parent.
		IQueryDefinition childQuery = (IQueryDefinition) iter.next();
		IQueryDefinition parentQuery = (IQueryDefinition) iter.next();
		IQueryResultSet parentRSet;
		IQueryResultSet childRSet = null;

		parentRSet = (IQueryResultSet) dataEngine.execute(parentQuery);

		while (parentRSet.next()) {
			Map parentMap = parentQuery.getBindings();
			Iterator parentIter = parentMap.keySet().iterator();
			while (parentIter.hasNext()) {
				String nextPar = (String) parentIter.next();
				Object value = parentRSet.getValue(nextPar);
				if (value != null) {
					resultStr.append(value.toString());
				} else {
					resultStr.append("null");
				}
			}
			childRSet = (IQueryResultSet) dataEngine.execute(parentRSet, childQuery, null, false);
			while (childRSet.next()) {
				Map map = childQuery.getBindings();
				String[] columns = (String[]) map.keySet().toArray(new String[] {});
				Arrays.sort(columns);
				for (int j = 0; j < columns.length; j++) {
					Object value = childRSet.getValue(columns[j]);
					if (value != null) {
						resultStr.append(value.toString());
					} else {
						resultStr.append("null");
					}
				}
			}
			childRSet.close();
		}

		parentRSet.close();
		dataEngine.shutdown();
		archWriter.finish();
		assertEquals(goldenStr, resultStr.toString());
	}

	public void doTestNestedQPresentation() throws Exception {
		Report report = getReport(NESTED_DATASET_DESIGN);
		IDataEngine dataEngine = getDataEngine(report, ARCHIVE_PATH, ARCHIVE_METANAME, MODE_PRESENTATION);

		Iterator iter = report.getQueries().iterator();

		String goldenFile = "NestedDataSet.txt";
		String goldenStr = loadResource(goldenFile);

		StringBuilder resultStr = new StringBuilder();
		IQueryDefinition childQuery = (IQueryDefinition) iter.next();
		IQueryDefinition parentQuery = (IQueryDefinition) iter.next();
		IQueryResultSet parentRSet;
		IQueryResultSet childRSet = null;

		parentRSet = (IQueryResultSet) dataEngine.execute(parentQuery);

		while (parentRSet.next()) {
			Map parentMap = parentQuery.getBindings();
			Iterator parentIter = parentMap.keySet().iterator();
			while (parentIter.hasNext()) {
				String nextPar = (String) parentIter.next();
				Object value = parentRSet.getValue(nextPar);
				if (value != null) {
					resultStr.append(value.toString());
				} else {
					resultStr.append("null");
				}
			}
			childRSet = (IQueryResultSet) dataEngine.execute(parentRSet, childQuery, null, false);
			while (childRSet.next()) {
				Map map = childQuery.getBindings();
				String[] columns = (String[]) map.keySet().toArray(new String[] {});
				Arrays.sort(columns);
				for (int j = 0; j < columns.length; j++) {
					Object value = childRSet.getValue(columns[j]);
					if (value != null) {
						resultStr.append(value.toString());
					} else {
						resultStr.append("null");
					}
				}
			}
			childRSet.close();
		}

		parentRSet.close();
		dataEngine.shutdown();
		archReader.close();
		assertEquals(goldenStr, resultStr.toString());
	}

	public void doTestSubqueryGeneration() throws Exception {
		Report report = getReport(SUBQUERY_DATASET_DESIGN);
		IDataEngine dataEngine = getDataEngine(report, ARCHIVE_PATH, ARCHIVE_METANAME, MODE_GENERATION);

		Iterator iter = report.getQueries().iterator();
		IQueryResultSet resultSet = null;

		String goldenFile = "SubqueryDataSet.txt";
		String goldenStr = loadResource(goldenFile);
		StringBuilder resultStr = new StringBuilder();
		while (iter.hasNext()) {
			IBaseQueryDefinition query = (IBaseQueryDefinition) iter.next();
			resultSet = (IQueryResultSet) dataEngine.execute(query);
			while (resultSet.next()) {
				int startGroup = resultSet.getStartingGroupLevel();
				if (startGroup == 0 || startGroup == 1) {
					Iterator groupIterator = query.getGroups().iterator();

					IGroupDefinition group = (IGroupDefinition) groupIterator.next();
					Iterator subQueryIter = group.getSubqueries().iterator();
					IBaseQueryDefinition subQuery = (IBaseQueryDefinition) subQueryIter.next();
					IQueryResultSet subResultSet = (IQueryResultSet) dataEngine.execute(resultSet, subQuery, null,
							false);
					Map map = subQuery.getBindings();
					resultStr.append(getResultSet(subResultSet, map.keySet()));
					subResultSet.close();

				}
			}
		}
		resultSet.close();
		dataEngine.shutdown();
		archWriter.finish();
		assertEquals(goldenStr, resultStr.toString());
	}

	private String getResultSet(IQueryResultSet resultSet, Set columnsSet) throws Exception {
		StringBuilder res = new StringBuilder();
		String[] columns = (String[]) columnsSet.toArray(new String[] {});
		Arrays.sort(columns);

		while (resultSet.next()) {
			for (int j = 0; j < columns.length; j++) {
				res.append(resultSet.getString(columns[j]));
			}
		}
		return res.toString();
	}

	public void doTestSubqueryPresentation() throws Exception {
		Report report = getReport(SUBQUERY_DATASET_DESIGN);
		IDataEngine dataEngine = getDataEngine(report, ARCHIVE_PATH, ARCHIVE_METANAME, MODE_PRESENTATION);

		Iterator iter = report.getQueries().iterator();
		IQueryResultSet resultSet = null;

		String goldenFile = "SubqueryDataSet.txt";
		String goldenStr = loadResource(goldenFile);
		StringBuilder resultStr = new StringBuilder();

		while (iter.hasNext()) {
			IBaseQueryDefinition query = (IBaseQueryDefinition) iter.next();
			resultSet = (IQueryResultSet) dataEngine.execute(query);
			while (resultSet.next()) {
				int startGroup = resultSet.getStartingGroupLevel();
				if (startGroup == 0 || startGroup == 1) {
					Iterator groupIterator = query.getGroups().iterator();

					IGroupDefinition group = (IGroupDefinition) groupIterator.next();
					Iterator subQueryIter = group.getSubqueries().iterator();
					IBaseQueryDefinition subQuery = (IBaseQueryDefinition) subQueryIter.next();
					IQueryResultSet subResultSet = (IQueryResultSet) dataEngine.execute(resultSet, subQuery, null,
							false);
					Map map = subQuery.getBindings();
					resultStr.append(getResultSet(subResultSet, map.keySet()));
					subResultSet.close();

				}
			}
		}
		resultSet.close();
		dataEngine.shutdown();
		archReader.close();
		assertEquals(goldenStr, resultStr.toString());
	}
}
