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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.data.dte;

import java.io.InputStream;

import junit.framework.TestCase;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.parser.ReportParser;

/**
 */
public class ReportQueryBuilderTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testQueryBuilder() throws Exception {
		String SAMPLE_DESIGN = "report.xml";
		InputStream in = this.getClass().getResourceAsStream(SAMPLE_DESIGN);
		assertTrue(in != null);
		ReportParser parser = new ReportParser();
		Report report = parser.parse("", in);
		assertTrue(report != null);
		getQueryBuilder(report).build();
		testGrid(report, report.getContent(2));
		testList(report, report.getContent(1));
		testTable(report, report.getContent(0));
	}

	private ReportQueryBuilder getQueryBuilder(Report report) throws BirtException {
		DataSessionContext context = new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION);
		DataRequestSession dteSession = DataRequestSession.newSession(context);
		ReportQueryBuilder reportQueryBuilder = new ReportQueryBuilder(report, new ExecutionContext(), dteSession);
		return reportQueryBuilder;
	}

	private void testGrid(Report report, ReportItemDesign item) {
		assertTrue(item.getQuery() instanceof QueryDefinition);
		assertTrue(report.getQueries().contains(item.getQuery()));
		assertEquals(1, ((QueryDefinition) item.getQuery()).getBindings().size());
	}

	private void testList(Report report, ReportItemDesign item) {
		ListItemDesign list = (ListItemDesign) item;
		assertTrue(item.getQuery() instanceof QueryDefinition);
		assertTrue(report.getQueries().contains(item.getQuery()));
		assertEquals(1, ((QueryDefinition) item.getQuery()).getBindings().size());
		assertEquals(1, ((QueryDefinition) item.getQuery()).getGroups().size());
		assertTrue(((QueryDefinition) item.getQuery()).usesDetails());
		assertEquals(1, ((QueryDefinition) item.getQuery()).getSubqueries().size());

		ListBandDesign listHeader = (ListBandDesign) list.getHeader();
		TableItemDesign table = (TableItemDesign) listHeader.getContent(0);
		assertTrue(table.getQuery() instanceof QueryDefinition);
		assertTrue(report.getQueries().contains(table.getQuery()));

		table = (TableItemDesign) listHeader.getContent(1);
		assertTrue(table.getQuery() instanceof SubqueryDefinition);
		assertTrue(((QueryDefinition) list.getQuery()).getSubqueries().contains(table.getQuery()));

		GroupDesign group = list.getGroup(0);
		assertEquals(1, ((QueryDefinition) list.getQuery()).getGroups().size());
		GroupDefinition grp = (GroupDefinition) ((QueryDefinition) list.getQuery()).getGroups().get(0);

		ListBandDesign groupHeader = (ListBandDesign) group.getHeader();
		table = (TableItemDesign) groupHeader.getContent(0);
		assertTrue(table.getQuery() instanceof QueryDefinition);
		assertTrue(report.getQueries().contains(table.getQuery()));
		table = (TableItemDesign) groupHeader.getContent(1);
		assertTrue(table.getQuery() instanceof SubqueryDefinition);
		assertTrue(grp.getSubqueries().contains(table.getQuery()));

		ListBandDesign groupFooter = (ListBandDesign) group.getFooter();
		table = (TableItemDesign) groupFooter.getContent(0);
		assertTrue(table.getQuery() instanceof QueryDefinition);
		assertTrue(report.getQueries().contains(table.getQuery()));
		table = (TableItemDesign) group.getFooter().getContent(1);
		assertTrue(table.getQuery() instanceof SubqueryDefinition);
		assertTrue(grp.getSubqueries().contains(table.getQuery()));

		ListBandDesign listDetail = (ListBandDesign) list.getDetail();
		assertTrue(report.getQueries().contains(listDetail.getContent(0).getQuery()));
		assertTrue(listDetail.getContent(0).getQuery() instanceof QueryDefinition);

	}

	private void testTable(Report report, ReportItemDesign item) {
		assertTrue(item.getQuery() instanceof QueryDefinition);
		IQueryDefinition query = (IQueryDefinition) item.getQuery();
		assertTrue(query.getSorts().size() == 2);
		assertTrue(query.getFilters().size() == 1);
		assertTrue(query.getBindings().size() == 14);

		GroupDefinition grp = (GroupDefinition) ((QueryDefinition) item.getQuery()).getGroups().get(0);
		assertTrue(grp.getSorts().size() == 2);
		assertTrue(grp.getFilters().size() == 1);
	}

}
