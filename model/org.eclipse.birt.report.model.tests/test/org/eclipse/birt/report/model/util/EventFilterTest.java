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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.activity.EventFilter;
import org.eclipse.birt.report.model.activity.FilterConditionFactory;
import org.eclipse.birt.report.model.activity.NotificationRecordTask;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.ElementDeletedEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.core.CoreTestUtil;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IGridItemModel;

/**
 * Test event filter class
 *
 */

public class EventFilterTest extends BaseTestCase {

	EventFilter filter = null;

	ReportDesign design = null;
	GridItem grid = null;
	TableRow row = null;
	Cell cell1 = null;
	Cell cell2 = null;

	@Override
	protected void setUp() throws Exception {
		List conds = new ArrayList();
		conds.add(FilterConditionFactory.createFilterCondition(FilterConditionFactory.ELEMENT_ADDED_FILTER_CONDITION));
		conds.add(
				FilterConditionFactory.createFilterCondition(FilterConditionFactory.ELEMENT_DELETED_FILTER_CONDITION));
		conds.add(FilterConditionFactory.createFilterCondition(FilterConditionFactory.SAME_EVENT_FILTER_CONDITION));
		filter = new EventFilter(conds);

		design = new ReportDesign(null);
		grid = new GridItem();
		row = new TableRow();
		CoreTestUtil.setContainer(row, grid, IGridItemModel.ROW_SLOT);

		cell1 = new Cell();
		CoreTestUtil.setContainer(cell1, row, TableRow.CONTENT_SLOT);

		cell2 = new Cell();
		CoreTestUtil.setContainer(cell2, row, TableRow.CONTENT_SLOT);
	}

	/**
	 * Test filter effect when delete a container element.
	 */

	public void testFilter_ElementDeleted() {
		List chain = new ArrayList();

		// Suppose we delete the grid.

		// ElementDeletedEvent sent to the element itself.

		chain.add(new NotificationRecordTask(cell1, new ElementDeletedEvent(row, cell1)));
		chain.add(new NotificationRecordTask(cell2, new ElementDeletedEvent(row, cell2)));
		chain.add(new NotificationRecordTask(row, new ElementDeletedEvent(grid, row)));
		chain.add(new NotificationRecordTask(grid, new ElementDeletedEvent(design, grid)));

		// ContentEvent sent to the contaner.

		chain.add(new NotificationRecordTask(row,
				new ContentEvent(row, cell1, TableRow.CONTENT_SLOT, ContentEvent.REMOVE)));
		chain.add(new NotificationRecordTask(row,
				new ContentEvent(row, cell2, TableRow.CONTENT_SLOT, ContentEvent.REMOVE)));
		chain.add(
				new NotificationRecordTask(grid, new ContentEvent(grid, row, GridItem.ROW_SLOT, ContentEvent.REMOVE)));
		chain.add(new NotificationRecordTask(design,
				new ContentEvent(design, grid, ReportDesign.BODY_SLOT, ContentEvent.REMOVE)));

		// Some property events to the dropped elements.

		chain.add(new NotificationRecordTask(cell1, new PropertyEvent(cell1, Cell.HEIGHT_PROP)));
		chain.add(new NotificationRecordTask(cell2, new PropertyEvent(cell2, Cell.HEIGHT_PROP)));
		chain.add(new NotificationRecordTask(row, new PropertyEvent(row, TableRow.BOOKMARK_PROP)));
		chain.add(new NotificationRecordTask(grid, new PropertyEvent(grid, GridItem.DATA_SET_PROP)));

		// Events from content are filtered.
		// Property events are filtered

		assertEquals(12, chain.size());

		List filteredEvents = filterTasks(filter.filter(chain));
		assertEquals(2, filteredEvents.size());

		NotificationEvent ev1 = ((NotificationRecordTask) filteredEvents.get(0)).getEvent();
		assertTrue(ev1 instanceof ElementDeletedEvent);
		assertEquals(grid, ev1.getTarget());

		NotificationEvent ev2 = ((NotificationRecordTask) filteredEvents.get(1)).getEvent();
		assertTrue(ev2 instanceof ContentEvent);
		assertEquals(design, ev2.getTarget());
		assertEquals(grid, ((ContentEvent) ev2).getContent());
	}

	/**
	 * Test filter effect when add a container element.
	 */

	public void testFilter_ElementAdded() {
		ReportDesign design = new ReportDesign(null);
		GridItem grid = new GridItem();
		TableRow row = new TableRow();
		CoreTestUtil.setContainer(row, grid, GridItem.ROW_SLOT);

		Cell cell1 = new Cell();
		CoreTestUtil.setContainer(cell1, row, TableRow.CONTENT_SLOT);

		Cell cell2 = new Cell();
		CoreTestUtil.setContainer(cell2, row, TableRow.CONTENT_SLOT);

		List chain = new ArrayList();

		// Suppose we add the grid to design.

		// ContentEvent sent to the contaner.

		chain.add(
				new NotificationRecordTask(row, new ContentEvent(row, cell1, TableRow.CONTENT_SLOT, ContentEvent.ADD)));
		chain.add(
				new NotificationRecordTask(row, new ContentEvent(row, cell2, TableRow.CONTENT_SLOT, ContentEvent.ADD)));
		chain.add(new NotificationRecordTask(grid, new ContentEvent(grid, row, GridItem.ROW_SLOT, ContentEvent.ADD)));
		chain.add(new NotificationRecordTask(design,
				new ContentEvent(design, grid, ReportDesign.BODY_SLOT, ContentEvent.ADD)));

		// Some property events to the dropped elements.

		chain.add(new NotificationRecordTask(cell1, new PropertyEvent(cell1, Cell.HEIGHT_PROP)));
		chain.add(new NotificationRecordTask(cell2, new PropertyEvent(cell2, Cell.HEIGHT_PROP)));
		chain.add(new NotificationRecordTask(row, new PropertyEvent(row, TableRow.BOOKMARK_PROP)));
		chain.add(new NotificationRecordTask(grid, new PropertyEvent(grid, GridItem.DATA_SET_PROP)));

		// Events from content are filtered.
		// Property events are filtered

		assertEquals(8, chain.size());

		List filteredEvents = filterTasks(filter.filter(chain));
		assertEquals(1, filteredEvents.size());

		NotificationEvent ev1 = ((NotificationRecordTask) filteredEvents.get(0)).getEvent();
		assertTrue(ev1 instanceof ContentEvent);
		assertEquals(design, ev1.getTarget());
		assertEquals(grid, ((ContentEvent) ev1).getContent());
	}

	/**
	 * Returns a list containing tasks not be filtered.
	 *
	 * @param chain the task chain
	 * @return a list containing tasks not be filtered
	 */

	private List filterTasks(List chain) {
		List filteredEvents = new ArrayList();

		for (int i = 0; i < chain.size(); i++) {
			NotificationRecordTask task = (NotificationRecordTask) chain.get(i);
			if (!task.isFiltered()) {
				filteredEvents.add(task);
			}
		}

		return filteredEvents;
	}

}
