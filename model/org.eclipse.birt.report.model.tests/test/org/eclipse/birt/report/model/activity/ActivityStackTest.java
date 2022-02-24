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

package org.eclipse.birt.report.model.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.table.LayoutTableModel;
import org.eclipse.birt.report.model.core.CoreTestUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Unit test for ActivityStack.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testExecute()}</td>
 * <td>initial activityRecord and execute it</td>
 * <td>state can change by controlling</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>add transition before execute</td>
 * <td>canUndo is false</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testUndo()}</td>
 * <td>execute record</td>
 * <td>counter is one</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>undo execute</td>
 * <td>counter is zero</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testRedo()}</td>
 * <td>execute record</td>
 * <td>counter is one</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>undo execute</td>
 * <td>counter is zero</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>redo execute</td>
 * <td>counter is one</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testCanUndo()}</td>
 * <td>execute record</td>
 * <td>canUndo is true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>undo execute</td>
 * <td>canUndo is false</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>execute record under transition</td>
 * <td>canUndo is false</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>commit record under transition</td>
 * <td>canUndo is true</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testCanRedo()}</td>
 * <td>execute record</td>
 * <td>canUndo is false</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>undo execute</td>
 * <td>canUndo is true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>redo</td>
 * <td>canUndo is false</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>put in transition and execute record</td>
 * <td>canUndo is false</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>commit transition</td>
 * <td>canUndo is true</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testFlush()}</td>
 * <td>when initial state of as can't do undo and redo operation , flush</td>
 * <td>canUndo and canRedo both false and no exception throws out</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>add two records , and let one in the undo stack and one in the redo
 * stack.</td>
 * <td>let canUndo and canRedo both true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>call flush method</td>
 * <td>canUndo and canRedo both false</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetRecords()}</td>
 * <td>execute three times</td>
 * <td>get the right activityID</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSetStackLimit()}</td>
 * <td>execute three times</td>
 * <td>size of the undo stack is three</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>set stack limit to just two</td>
 * <td>size of the undo stack is two</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetRedoRecord()}</td>
 * <td>get initial redoRecord</td>
 * <td>0</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>execute and undo one time</td>
 * <td>get one record</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetUndoRecord()}</td>
 * <td>get initial undoRecord</td>
 * <td>null</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>execute and undo one time</td>
 * <td>get one record</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetRedoLabel()}</td>
 * <td>get initial redoLabel</td>
 * <td>null</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>set label , then execute and undo one time</td>
 * <td>get the same label</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetUndoLabel()}</td>
 * <td>get initial undoRecord</td>
 * <td>0</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>set label ,then execute one time</td>
 * <td>get the same label</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testStartTrans()}</td>
 * <td>execute record and commit transition</td>
 * <td>after commit , canUndo is true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>nested transactions , two levels</td>
 * <td>first commit , canUndo still is false after second commit ( outer commit
 * ) ,canUndo is true</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetCurrentTransNo()}</td>
 * <td>execute twice and check number</td>
 * <td>2</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>undo and redo operation</td>
 * <td>1 , then change to 2</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>execute one time and check number</td>
 * <td>3</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testNotification()}</td>
 * <td>execute one record</td>
 * <td>transaction done should be notified.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>undo the above execution</td>
 * <td>transaction undone should be notified.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>redo the above execution</td>
 * <td>transaction redone should be notified.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>execute one transaction of two records</td>
 * <td>transaction done should be notified.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>undo the above transaction</td>
 * <td>transaction undone should be notified.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>redo the above transaction</td>
 * <td>transaction redone should be notified.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>start one transaction, but roll it back</td>
 * <td>no listener should be notified.</td>
 * </tr>
 * </table>
 * 
 * <tr>
 * <td>{@link #testRedoUndoStack()}</td>
 * <td>Test redo/undo stack for executing records/transactions</td>
 * <td>Redo/undo stacks are expected.</td>
 * </tr>
 * 
 */

public class ActivityStackTest extends BaseTestCase {

	/**
	 * Mock up activityRecord, the target of this record is linked to a
	 * MockupDesignElement.
	 */

	MockupActivityRecord record = null;

	/**
	 * Instance of ActivityStack.
	 */
	ActivityStack as = null;

	/**
	 * Mock up DesignElement.
	 */
	MockupDesignElement designElement = null;

	private static final String mockupLabel = "MockupActivityRecord"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		this.designElement = new MockupDesignElement();
		this.record = new MockupActivityRecord(designElement);
		this.as = new ActivityStack(null);
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		this.designElement = null;
		this.record = null;
		this.as = null;
	}

	/**
	 * Test execute() with a ActivityStack.
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>initial activityRecord and execute it</li>
	 * <li>add transition before execute</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>state can change by controlling</li>
	 * <li>canUndo is false</li>
	 * </ul>
	 * 
	 */
	public void testExecute() {
		// initial state of the Record.

		assertEquals(ActivityRecord.INITIAL_STATE, this.record.getState());
		assertEquals(false, this.record.executed);
		assertEquals(false, this.record.sendNotification);
		assertEquals(0, designElement.counter);

		as.execute(record);

		// check if the ActivityRecord has been successfully executed.

		assertEquals(ActivityRecord.DONE_STATE, this.record.getState());
		assertEquals(true, this.record.executed);
		assertEquals(true, this.record.sendNotification);
		assertEquals(1, designElement.counter);

		this.record = new MockupActivityRecord(designElement);
		as.execute(record);
		assertEquals(2, designElement.counter);

		assertEquals(true, as.canUndo());
		as.undo();
		assertEquals(true, as.canRedo());

		// The the redo stack is cleared up during execution.

		this.record = new MockupActivityRecord(designElement);
		as.execute(record);
		assertEquals(false, as.canRedo());

		// add transition before execute and canUndo is false

		as.startTrans(null);
		this.record = new MockupActivityRecord(designElement);
		as.execute(this.record);
		assertEquals(false, as.canUndo());
		assertEquals(3, designElement.counter);
		as.commit();

	}

	/**
	 * Test undo().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>execute record</li>
	 * <li>undo execute</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>counter is one</li>
	 * <li>counter is zero</li>
	 * </ul>
	 */
	public void testUndo() {
		as.execute(record);
		assertEquals(1, designElement.counter);

		as.undo();
		assertEquals(0, designElement.counter);
	}

	/**
	 * test redo().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>execute record</li>
	 * <li>undo execute</li>
	 * <li>redo execute</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>counter is one</li>
	 * <li>counter is zero</li>
	 * <li>counter is one</li>
	 * </ul>
	 */
	public void testRedo() {
		assertEquals(0, designElement.counter);

		as.execute(record);
		assertEquals(1, designElement.counter);

		as.undo();
		assertEquals(0, designElement.counter);

		as.redo();
		assertEquals(1, designElement.counter);
	}

	/**
	 * test canUndo().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>execute record</li>
	 * <li>undo execute</li>
	 * <li>execute record under transition</li>
	 * <li>commit record under transition</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>canUndo is true</li>
	 * <li>canUndo is false</li>
	 * <li>canUndo is false</li>
	 * <li>canUndo is true</li>
	 * </ul>
	 */
	public void testCanUndo() {
		assertEquals(false, as.canUndo());

		as.execute(record);
		assertEquals(true, as.canUndo());

		as.undo();
		assertEquals(false, as.canUndo());

		as.startTrans("Trans1"); //$NON-NLS-1$
		assertEquals(false, as.canUndo());

		as.execute(new MockupActivityRecord(designElement));
		assertEquals(false, as.canUndo());

		as.commit();
		assertEquals(true, as.canUndo());
	}

	/**
	 * test canRedo().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>execute record</li>
	 * <li>undo execute</li>
	 * <li>redo</li>
	 * <li>put in transition and execute record</li>
	 * <li>commit transition</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>canRedo is false</li>
	 * <li>canRedo is true</li>
	 * <li>canUndo is false</li>
	 * <li>canUndo is false</li>
	 * <li>canUndo is true</li>
	 * </ul>
	 */
	public void testCanRedo() {
		assertEquals(false, as.canRedo());

		as.execute(record);
		assertEquals(false, as.canRedo());

		as.undo();
		assertEquals(true, as.canRedo());

		as.redo();
		assertEquals(false, as.canRedo());

		as.startTrans("Tran2"); //$NON-NLS-1$
		assertEquals(false, as.canRedo());

		as.execute(new MockupActivityRecord(designElement));
		assertEquals(false, as.canUndo());

		as.commit();
		assertEquals(true, as.canUndo());

	}

	/**
	 * Test flush().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>when initial state of as can't do undo and redo operation , flush</li>
	 * <li>add two records , and let one in the undo stack and one in the redo
	 * stack.</li>
	 * <li>call flush method</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>canUndo and canRedo both false and no exception throws out</li>
	 * <li>let canUndo and canRedo both true</li>
	 * <li>canUndo and canRedo both false</li>
	 * </ul>
	 */
	public void testFlush() {
		// make sure flush can't throw out exception ,
		// when initial state of as can't do undo and redo operation

		assertEquals(false, as.canUndo());
		assertEquals(false, as.canRedo());

		as.flush();

		assertEquals(false, as.canUndo());
		assertEquals(false, as.canRedo());

		// add two records , and let one in the undo stack
		// and one in the redo stack.

		as.execute(record);
		assertEquals(true, as.canUndo());

		as.execute(new MockupActivityRecord(designElement));

		assertEquals(true, as.canUndo());

		// undo operation , make sure as can undo and redo operation

		as.undo();

		assertEquals(true, as.canUndo());
		assertEquals(true, as.canRedo());

		// after flush method , as can not undo and redo operation

		as.flush();

		assertEquals(false, as.canUndo());
		assertEquals(false, as.canRedo());

	}

	/**
	 * test getRecords.
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>execute three times</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>get the right activityID</li>
	 * </ul>
	 */

	public void testGetRecords() {
		assertEquals(0, as.getRecords().length);

		as.execute(new MockupActivityRecord(designElement, 1));
		assertEquals(1, as.getRecords().length);

		as.execute(new MockupActivityRecord(designElement, 2));
		assertEquals(2, as.getRecords().length);

		as.execute(new MockupActivityRecord(designElement, 3));
		assertEquals(3, as.getRecords().length);

		// check the order. The records list should be in the order as they were
		// executed.
		assertEquals(1, ((MockupActivityRecord) as.getRecords()[0]).activityID);
		assertEquals(2, ((MockupActivityRecord) as.getRecords()[1]).activityID);
		assertEquals(3, ((MockupActivityRecord) as.getRecords()[2]).activityID);
	}

	/**
	 * test setStackLimit().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>execute three times</li>
	 * <li>set stack limit to just two</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>size of the undo stack is three</li>
	 * <li>size of the undo stack is two</li>
	 * </ul>
	 */
	public void testSetStackLimit() {
		// initial count = 0
		assertEquals(0, designElement.counter);

		// execute 3 times, now the undo stack.size = 3
		this.record = new MockupActivityRecord(designElement);
		as.execute(record);

		this.record = new MockupActivityRecord(designElement);
		as.execute(record);

		this.record = new MockupActivityRecord(designElement);
		as.execute(record);

		assertEquals(3, as.getRecords().length);
		as.setStackLimit(2);
		assertEquals(2, as.getRecords().length);
	}

	/**
	 * test getRedoRecord().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>get initial redoRecord</li>
	 * <li>execute and undo one time</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>0</li>
	 * <li>get one record</li>
	 * </ul>
	 */

	public void testGetRedoRecord() {
		assertNull(as.getRedoRecord());

		as.execute(record);
		as.undo();

		assertEquals(record, as.getRedoRecord());
	}

	/**
	 * test getUndoRecord().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>get initial undoRecord</li>
	 * <li>execute one time</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>null</li>
	 * <li>get one record</li>
	 * </ul>
	 */

	public void testGetUndoRecord() {
		assertNull(as.getUndoRecord());

		as.execute(record);
		assertEquals(record, as.getUndoRecord());
	}

	/**
	 * test getUndoLabel().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>get initial undoRecord</li>
	 * <li>set label ,then execute one time</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>0</li>
	 * <li>get the same label</li>
	 * </ul>
	 */

	public void testGetUndoLabel() {
		assertNull(as.getUndoLabel());
		record.setLabel(mockupLabel);

		as.execute(record);
		assertEquals(mockupLabel, as.getUndoLabel());
	}

	/**
	 * test getRedoLabel().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>get initial redoLabel</li>
	 * <li>set label , then execute and undo one time</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>null</li>
	 * <li>get the same label</li>
	 * </ul>
	 */

	public void testGetRedoLabel() {
		assertNull(as.getRedoLabel());
		record.setLabel(mockupLabel);

		as.execute(record);
		as.undo();

		assertEquals(mockupLabel, as.getRedoLabel());
	}

	/**
	 * test startTrans().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>execute record and commit transition</li>
	 * <li>nested transactions , two levels</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>after commit , canUndo is true</li>
	 * <li>first commit , canUndo still is false after second commit ( outer commit
	 * ) ,canUndo is true</li>
	 * </ul>
	 */
	public void teststartTrans() {
		as.execute(record);
		as.undo();

		assertEquals(true, as.canRedo());
		assertEquals(0, designElement.counter);

		as.startTrans(null);
		// The redo stack should have been flushed.
		assertEquals(false, as.canRedo());
		as.execute(new MockupActivityRecord(this.designElement, 1));
		as.execute(new MockupActivityRecord(this.designElement, 2));

		// Record executes separately within a Transaction.
		assertEquals(2, designElement.counter);

		// Cannot be undone until commit.
		assertEquals(false, as.canUndo());

		as.commit();

		// The Transaction(CompoundActivity) is pushed into the the undo stack
		// when
		// commit.
		assertEquals(true, as.canUndo());

		// undo execution will undo the whole compoundActivity.
		as.undo();
		assertEquals(0, designElement.counter);

		// nested transactions

		as.startTrans(null);
		as.execute(new MockupActivityRecord(this.designElement, 1));

		// nested transaction started.
		as.startTrans(null);
		as.execute(new MockupActivityRecord(this.designElement, 2));

		// commit the second one. One transaction remained uncommitted.
		as.commit();
		assertEquals(false, as.canUndo());

		// commit the first one. All transaction committed.
		as.commit();
		assertEquals(true, as.canUndo());

	}

	/**
	 * test getCurrentTransNo().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>execute twice and check number</li>
	 * <li>undo and redo operation</li>
	 * <li>execute one time and check number</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>2</li>
	 * <li>1 , then change to 2</li>
	 * <li>3</li>
	 * </ul>
	 * 
	 */

	public void testGetCurrentTransNo() {
		// the undo stack is empty and initial number is zero

		assertEquals(0, as.getCurrentTransNo());

		// execute twice

		as.execute(new MockupActivityRecord(this.designElement, 1));
		as.execute(new MockupActivityRecord(this.designElement, 2));
		assertEquals(2, as.getCurrentTransNo());

		// the undo stack contains one element,return element number

		as.undo();
		assertEquals(1, as.getCurrentTransNo());

		// let the undo stack is empty , and return transNo

		as.redo();
		assertEquals(2, as.getCurrentTransNo());

		// execute one time , and transNo become three

		as.execute(new MockupActivityRecord(this.designElement, 3));
		assertEquals(3, as.getCurrentTransNo());

	}

	/**
	 * Tests the event notification of record execution and stack change.
	 * 
	 */

	public void testNotification() {
		DesignElement container = new MockupDesignElement();
		CoreTestUtil.setContainer(designElement, container, 1);

		MyActivityStackListener listener = new MyActivityStackListener();
		as.addListener(listener);

		MyElementListener elementListener = new MyElementListener();
		designElement.addListener(elementListener);

		MockupActivityRecord record1;
		MockupActivityRecord record2;

		// One execution
		record1 = new MockupActivityRecord(designElement, 1);
		as.execute(record1);
		assertEquals(ActivityStackEvent.DONE, listener.action);

		// Undo it

		listener.action = -1;
		as.undo();
		assertEquals(ActivityStackEvent.UNDONE, listener.action);

		// Redo it

		listener.action = -1;
		as.redo();
		assertEquals(ActivityStackEvent.REDONE, listener.action);

		// Transaction committed

		record1 = new MockupActivityRecord(this.designElement, 1);
		record2 = new MockupActivityRecord(this.designElement, 2);
		listener.action = -1;
		as.startTrans(null);
		as.execute(record1);
		as.execute(record2);
		as.commit();
		assertEquals(ActivityStackEvent.DONE, listener.action);

		// Undo the transaction

		listener.action = -1;
		as.undo();
		// All records in transaction send event in transaction.
		assertEquals(ActivityStackEvent.UNDONE, listener.action);

		// Redo the transaction

		listener.action = -1;
		as.redo();
		// All records in transaction send event in transaction.
		assertEquals(ActivityStackEvent.REDONE, listener.action);

		// Transaction rolled back

		record1 = new MockupActivityRecord(this.designElement, 1);
		record2 = new MockupActivityRecord(this.designElement, 2);
		listener.action = -1;
		as.startTrans(null);
		as.execute(record1);
		as.execute(record2);
		as.startTrans(null);
		record1 = new MockupActivityRecord(this.designElement, 1);
		as.execute(record1);
		as.rollback();
		// not the outmost transaction, no notifications.
		assertEquals(-1, listener.action);
		as.rollback();
		assertEquals(ActivityStackEvent.ROLL_BACK, listener.action);
	}

	/**
	 * Tests undo/redo stack status when executing a record or an transaction.
	 * 
	 * The redo stack will be cleared only when the activity stack executing a
	 * top-most record/transaction. Otherwise, the redo stack doesn't change.
	 */

	public void testRedoUndoStack() {
		// One execution

		ActivityRecord record1 = new MockupActivityRecord(this.designElement, 1);
		as.execute(record1);
		assertNull(as.getRedoRecord());
		assertNotNull(as.getUndoRecord());

		as.undo();

		record1 = new MockupActivityRecord(this.designElement, 1);
		as.execute(record1);
		assertNull(as.getRedoRecord());
		assertNotNull(as.getUndoRecord());

		// enable redo

		as.undo();

		// test commit cases.

		as.startTrans(null);

		as.execute(new MockupActivityRecord(this.designElement, 1));
		as.execute(new MockupActivityRecord(this.designElement, 2));

		// Record executes separately within a Transaction.

		assertEquals(2, designElement.counter);

		assertNotNull(as.getRedoRecord());

		as.commit();

		assertNull(as.getRedoRecord());

		as.undo();

		// rollback cases.

		as.startTrans(null);

		as.execute(new MockupActivityRecord(this.designElement, 1));
		as.execute(new MockupActivityRecord(this.designElement, 2));

		// Record executes separately within a Transaction.

		assertEquals(2, designElement.counter);
		assertNotNull(as.getRedoRecord());

		ActivityRecord record = (ActivityRecord) as.getRedoRecord();
		int transNo = record.getTransNo();

		as.rollback();

		// still can redo.

		assertNotNull(as.getRedoRecord());

		record = (ActivityRecord) as.getRedoRecord();
		assertEquals(transNo, record.getTransNo());

		assertEquals(0, designElement.counter);

		// nested cases.

		as.startTrans(null);

		as.execute(new MockupActivityRecord(this.designElement, 1));

		as.startTrans(null);

		as.execute(new MockupActivityRecord(this.designElement, 2));

		assertNotNull(as.getRedoRecord());
		record = (ActivityRecord) as.getRedoRecord();
		transNo = record.getTransNo();

		as.commit();

		assertNotNull(as.getRedoRecord());
		record = (ActivityRecord) as.getRedoRecord();
		assertEquals(transNo, record.getTransNo());

		as.rollback();

		assertNotNull(as.getRedoRecord());
		record = (ActivityRecord) as.getRedoRecord();
		assertEquals(transNo, record.getTransNo());

	}

	/**
	 * Tests persistent transaction feature.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testPersistentTransaction() throws Exception {
		createDesign();
		GridHandle grid = designHandle.getElementFactory().newGridItem("grid", 3, 3); //$NON-NLS-1$
		designHandle.getBody().add(grid);
		ActivityStack stack = designHandle.getModule().getActivityStack();

		// case one: do a persistent transaction which has only one operation
		// and call rollbackAll()

		stack.flush();
		stack.startTrans(null);
		grid.setComments("comments for grid"); //$NON-NLS-1$
		grid.setOnCreate("on create"); //$NON-NLS-1$
		stack.startPersistentTrans();
		grid.setName("New grid"); //$NON-NLS-1$
		stack.commit();
		stack.rollbackAll();
		assertEquals("New grid", grid.getName()); //$NON-NLS-1$
		// after roll back all, the top-level transaction is not done, but the
		// inner persisten transaction is done, the persistent transantion can
		// be undone.
		assertTrue(stack.canUndo());
		assertFalse(stack.canRedo());

		// case two: do a persistent transaction which has only one operation
		// and call rollback()

		grid.clearAllProperties();
		stack.flush();
		stack.startTrans(null);
		grid.setComments("comments for grid"); //$NON-NLS-1$
		grid.setOnCreate("on create"); //$NON-NLS-1$
		stack.startPersistentTrans();
		grid.setName("New grid"); //$NON-NLS-1$
		stack.commit();
		stack.rollback();
		assertEquals("New grid", grid.getName()); //$NON-NLS-1$
		// nested transaction has only one operation, so Model will delete the
		// nested the transaction and add the operation directly into the
		// top-level transaction; in this condition rollback=rollbackAll
		assertTrue(stack.canUndo());
		assertFalse(stack.canRedo());

		// case three: do a persistent transaction which has more than one
		// operation and call rollback()

		grid.clearAllProperties();
		stack.flush();
		stack.startTrans(null);
		grid.setComments("comments for grid"); //$NON-NLS-1$
		grid.setOnCreate("on create"); //$NON-NLS-1$
		stack.startPersistentTrans();
		grid.setName("New grid"); //$NON-NLS-1$
		grid.setHeight(12);
		stack.commit();
		stack.rollback();
		assertEquals("New grid", grid.getName()); //$NON-NLS-1$
		assertTrue(stack.canUndo());
		assertFalse(stack.canRedo());

		// case four: do a persistent transaction which has more than one
		// operation and call rollbackAll()

		grid.clearAllProperties();
		stack.flush();
		stack.startTrans(null);
		grid.setComments("comments for grid"); //$NON-NLS-1$
		grid.setOnCreate("on create"); //$NON-NLS-1$
		stack.startPersistentTrans();
		grid.setName("New grid"); //$NON-NLS-1$
		grid.setHeight(12);
		stack.commit();
		stack.rollbackAll();
		assertEquals("New grid", grid.getName()); //$NON-NLS-1$
		// after roll back all, the top-level transaction is not done
		assertTrue(stack.canUndo());
		assertFalse(stack.canRedo());

		// case four: do a persistent transaction which has more than one
		// operation and call rollbackAll()

		grid.clearAllProperties();
		stack.flush();
		stack.startTrans(null);
		grid.setComments("comments for grid"); //$NON-NLS-1$
		grid.setOnCreate("on create"); //$NON-NLS-1$
		stack.startPersistentTrans();
		// stack.startTrans( null );
		grid.setName("New grid"); //$NON-NLS-1$
		grid.setHeight(12);
		stack.rollback();
		stack.commit();
		assertNull(grid.getName());
		// after roll back all, the top-level transaction is not done
		assertTrue(stack.canUndo());
		assertFalse(stack.canRedo());
	}

	/**
	 * test the undo sequence after a persistent transaction is committed.
	 * 
	 * @throws ContentException
	 * @throws NameException
	 */
	public void testPersistentTransactionUndo() throws ContentException, NameException {

		createDesign();
		GridHandle grid = designHandle.getElementFactory().newGridItem("grid"); //$NON-NLS-1$

		TableHandle table = designHandle.getElementFactory().newTableItem("mytable"); //$NON-NLS-1$

		StyleHandle style = designHandle.getElementFactory().newStyle("style"); //$NON-NLS-1$

		DataSetHandle dataset = designHandle.getElementFactory().newOdaDataSet("dataset", null); //$NON-NLS-1$

		DataSourceHandle datasource = designHandle.getElementFactory().newOdaDataSource("datasource", null); //$NON-NLS-1$

		LabelHandle label = designHandle.getElementFactory().newLabel("label"); //$NON-NLS-1$

		ActivityStack stack = designHandle.getModule().getActivityStack();
		stack.flush();

		stack.startTrans(null);
		designHandle.getStyles().add(style);

		stack.startTrans(null);
		designHandle.getBody().add(grid);

		stack.startTrans(null);
		designHandle.getComponents().add(label);

		stack.startPersistentTrans();
		designHandle.getBody().add(table);
		stack.commit();

		stack.startPersistentTrans();
		designHandle.getDataSets().add(dataset);

		stack.startPersistentTrans();
		designHandle.getDataSources().add(datasource);
		stack.commit();

		stack.rollback();

		stack.commit();

		stack.rollback();

		stack.commit();

		assertTrue(stack.canUndo());
		assertFalse(stack.canRedo());
		assertEquals(style, designHandle.getStyles().get(0));
		assertEquals(table, designHandle.getBody().get(0));
		assertEquals(datasource, designHandle.getDataSources().get(0));
		assertEquals(0, designHandle.getComponents().getCount());

		assertEquals(null, designHandle.getDataSets().get(0));

		stack.undo();
		assertEquals(0, designHandle.getDataSources().getCount());

		stack.undo();
		assertEquals(0, designHandle.getBody().getCount());

		assertTrue(stack.canRedo());
		assertTrue(stack.canUndo());

		stack.undo();
		assertEquals(0, designHandle.getStyles().getCount());

		assertTrue(stack.canRedo());
		assertFalse(stack.canUndo());

	}

	/**
	 * 
	 * @throws ContentException
	 * @throws NameException
	 */
	public void testPersistentTransactionUndo2() throws ContentException, NameException {

		createDesign();
		GridHandle grid = designHandle.getElementFactory().newGridItem("grid"); //$NON-NLS-1$

		TableHandle table = designHandle.getElementFactory().newTableItem("mytable"); //$NON-NLS-1$

		StyleHandle style = designHandle.getElementFactory().newStyle("style"); //$NON-NLS-1$

		DataSetHandle dataset = designHandle.getElementFactory().newOdaDataSet("dataset", null); //$NON-NLS-1$

		DataSourceHandle datasource = designHandle.getElementFactory().newOdaDataSource("datasource", null); //$NON-NLS-1$

		ActivityStack stack = designHandle.getModule().getActivityStack();
		stack.flush();

		stack.startTrans(null);
		designHandle.getStyles().add(style);

		stack.startTrans(null);
		designHandle.getBody().add(grid);

		stack.startPersistentTrans();
		designHandle.getBody().add(table);
		stack.commit();

		stack.startPersistentTrans();
		designHandle.getDataSets().add(dataset);

		stack.startPersistentTrans();
		designHandle.getDataSources().add(datasource);
		stack.commit();

		stack.rollback();

		stack.rollback();

		stack.commit();

		assertTrue(stack.canUndo());
		assertFalse(stack.canRedo());
		assertEquals(style, designHandle.getStyles().get(0));
		assertEquals(table, designHandle.getBody().get(0));
		assertEquals(datasource, designHandle.getDataSources().get(0));
		assertEquals(0, designHandle.getComponents().getCount());

		assertEquals(null, designHandle.getDataSets().get(0));

		stack.undo();
		assertEquals(0, designHandle.getDataSources().getCount());

		stack.undo();
		assertEquals(0, designHandle.getBody().getCount());

		assertTrue(stack.canRedo());
		assertTrue(stack.canUndo());

		stack.undo();
		assertEquals(0, designHandle.getStyles().getCount());

		assertTrue(stack.canRedo());
		assertFalse(stack.canUndo());

	}

	/**
	 * 
	 * @throws ContentException
	 * @throws NameException
	 */
	public void testPersistentTransactionUndo3() throws ContentException, NameException {

		createDesign();
		GridHandle grid = designHandle.getElementFactory().newGridItem("grid"); //$NON-NLS-1$

		TableHandle table = designHandle.getElementFactory().newTableItem("mytable"); //$NON-NLS-1$

		StyleHandle style = designHandle.getElementFactory().newStyle("style"); //$NON-NLS-1$

		DataSetHandle dataset = designHandle.getElementFactory().newOdaDataSet("dataset", null); //$NON-NLS-1$

		DataSourceHandle datasource = designHandle.getElementFactory().newOdaDataSource("datasource", null); //$NON-NLS-1$

		LabelHandle label = designHandle.getElementFactory().newLabel("label"); //$NON-NLS-1$

		ActivityStack stack = designHandle.getModule().getActivityStack();
		stack.flush();

		stack.startTrans(null);
		designHandle.getStyles().add(style);

		stack.startTrans(null);
		designHandle.getBody().add(grid);

		stack.startPersistentTrans();
		designHandle.getBody().add(table);
		stack.commit();

		stack.startPersistentTrans();
		designHandle.getDataSets().add(dataset);

		stack.startPersistentTrans();
		designHandle.getDataSources().add(datasource);
		stack.commit();

		stack.startTrans(null);
		designHandle.getComponents().add(label);
		stack.commit();

		stack.rollback();

		stack.rollback();

		stack.commit();

		assertTrue(stack.canUndo());
		assertFalse(stack.canRedo());
		assertEquals(style, designHandle.getStyles().get(0));
		assertEquals(table, designHandle.getBody().get(0));
		assertEquals(datasource, designHandle.getDataSources().get(0));
		assertEquals(0, designHandle.getComponents().getCount());

		assertEquals(null, designHandle.getDataSets().get(0));

		stack.undo();
		assertEquals(0, designHandle.getDataSources().getCount());

		stack.undo();
		assertEquals(0, designHandle.getBody().getCount());

		assertTrue(stack.canRedo());
		assertTrue(stack.canUndo());

		stack.undo();
		assertEquals(0, designHandle.getStyles().getCount());

		assertTrue(stack.canRedo());
		assertFalse(stack.canUndo());

	}

	/**
	 * 
	 * @throws ContentException
	 * @throws NameException
	 */
	public void testPersistentTransactionUndo4() throws ContentException, NameException {

		createDesign();
		GridHandle grid = designHandle.getElementFactory().newGridItem("grid"); //$NON-NLS-1$

		TableHandle table = designHandle.getElementFactory().newTableItem("mytable"); //$NON-NLS-1$

		StyleHandle style = designHandle.getElementFactory().newStyle("style"); //$NON-NLS-1$

		DataSetHandle dataset = designHandle.getElementFactory().newOdaDataSet("dataset", null); //$NON-NLS-1$

		DataSourceHandle datasource = designHandle.getElementFactory().newOdaDataSource("datasource", null); //$NON-NLS-1$

		LabelHandle label = designHandle.getElementFactory().newLabel("label"); //$NON-NLS-1$

		ActivityStack stack = designHandle.getModule().getActivityStack();
		stack.flush();

		stack.startTrans(null);
		designHandle.getStyles().add(style);

		stack.startTrans(null);
		designHandle.getBody().add(grid);

		stack.startPersistentTrans();
		designHandle.getBody().add(table);
		stack.commit();

		stack.startPersistentTrans();
		designHandle.getDataSets().add(dataset);

		stack.startPersistentTrans();
		designHandle.getDataSources().add(datasource);

		stack.startTrans(null);
		designHandle.getComponents().add(label);
		stack.commit();

		stack.commit();

		stack.rollback();

		stack.rollback();

		stack.commit();

		assertTrue(stack.canUndo());
		assertFalse(stack.canRedo());
		assertEquals(style, designHandle.getStyles().get(0));
		assertEquals(table, designHandle.getBody().get(0));
		assertEquals(datasource, designHandle.getDataSources().get(0));
		assertEquals(1, designHandle.getComponents().getCount());

		assertEquals(null, designHandle.getDataSets().get(0));

		stack.undo();
		assertEquals(0, designHandle.getDataSources().getCount());

		stack.undo();
		assertEquals(0, designHandle.getBody().getCount());
		assertEquals(0, designHandle.getComponents().getCount());

		assertTrue(stack.canRedo());
		assertTrue(stack.canUndo());

		stack.undo();
		assertEquals(0, designHandle.getStyles().getCount());

		assertTrue(stack.canRedo());
		assertFalse(stack.canUndo());

	}

	/**
	 * Tests cases for persistent transaction undo for bug 215321.
	 * 
	 * @throws Exception
	 */
	public void testPersistentTransactionUndo_1() throws Exception {
		createDesign();
		GridHandle grid = designHandle.getElementFactory().newGridItem("grid"); //$NON-NLS-1$

		TableHandle table = designHandle.getElementFactory().newTableItem("mytable"); //$NON-NLS-1$

		StyleHandle style = designHandle.getElementFactory().newStyle("style"); //$NON-NLS-1$

		ActivityStack stack = designHandle.getModule().getActivityStack();
		stack.flush();

		// add grid
		stack.startTrans(null);
		designHandle.getBody().add(grid);

		stack.startTrans(null);
		designHandle.getBody().add(table);

		stack.startPersistentTrans();
		designHandle.getStyles().add(style);

		// stack: commit -- rollback -- rollback
		stack.commit();

		stack.rollback();

		stack.rollback();

		assertTrue(designHandle.needsSave());
		assertTrue(stack.canUndo());
	}

	/**
	 * Tests slient transaction feature. Starts the transaction and executes
	 * records, but there is no event received.
	 * 
	 * <ul>
	 * <li>Silent transaction with add() operations. Before the commit, the layout
	 * is not updated.</li>
	 * <li>Undo/redo with silent transaction. After undo/redo, the layout is
	 * updated.</li>
	 * <li>Drop methods with filter transaction. The layout is updated.</li>
	 * <li>Silent transaction with drop methods (filter transaction). The layout is
	 * updated after the commit.</li>
	 * <li>Silent transaction with drop methods (filter transaction). The layout
	 * keeps same after rollback.</li>
	 * <li>Silent transaction with add() methods(). The layout is updated after the
	 * commit.</li>
	 * </ul>
	 * 
	 * @throws Exception
	 * 
	 */

	public void testLayoutTransaction() throws Exception {
		// normal transaction without filtering events.
		createDesign();

		TableHandle table = designHandle.getElementFactory().newTableItem("testTable", 3, 1, 1, 1); //$NON-NLS-1$

		ElementListener elementListener = new ElementListener();
		table.addListener(elementListener);
		designHandle.getBody().add(table);

		// set the color of the first column as blue

		table.getColumns().get(0).setProperty(IStyleModel.COLOR_PROP, ColorPropertyType.BLUE);

		((ActivityStack) designHandle.getCommandStack()).startSilentTrans(null);

		// silent transaction

		RowHandle row = designHandle.getElementFactory().newTableRow(3);

		// the layout has not been updated.

		table.getDetail().add(row);

		// no event received

		assertEquals(0, elementListener.notifications.size());

		CellHandle cell = (CellHandle) row.getCells().get(0);

		// not updated, must throw NPE

		assertEquals(ColorPropertyType.BLACK, cell.getProperty(IStyleModel.COLOR_PROP));

		designHandle.getCommandStack().commit();

		// event received

		assertEquals(2, elementListener.notifications.size());

		// now it is OK

		assertEquals(ColorPropertyType.BLUE, cell.getProperty(IStyleModel.COLOR_PROP));

		designHandle.getCommandStack().undo();

		// event received

		assertEquals(4, elementListener.notifications.size());

		// detached from the table, the color is default

		assertEquals(ColorPropertyType.BLACK, cell.getProperty(IStyleModel.COLOR_PROP));

		designHandle.getCommandStack().redo();

		assertEquals(ColorPropertyType.BLUE, cell.getProperty(IStyleModel.COLOR_PROP));

		// reset events

		ElementListener rowListener = new ElementListener();
		row.addListener(rowListener);
		ElementListener cellListener = new ElementListener();
		row.getCells().get(0).addListener(cellListener);

		// normal transaction, events are expected to be received.

		row.drop();

		assertEquals(0, rowListener.notifications.size());
		assertEquals(0, cellListener.notifications.size());

		// detached from the table, the color is default

		assertEquals(ColorPropertyType.BLACK, cell.getProperty(IStyleModel.COLOR_PROP));

		designHandle.getCommandStack().undo();

		assertEquals(ColorPropertyType.BLUE, cell.getProperty(IStyleModel.COLOR_PROP));

		// must rehook the listener to the row element again.

		row.addListener(rowListener);

		((ActivityStack) designHandle.getCommandStack()).startSilentTrans(null);

		row.drop();

		assertEquals(0, rowListener.notifications.size());

		// layout is not updated, but the cell containment has been updated.

		LayoutTableModel model = table.getLayoutModel();
		assertEquals(2, model.getLayoutSlotDetail().getRowCount());
		assertEquals(ColorPropertyType.BLACK, cell.getProperty(IStyleModel.COLOR_PROP));

		((ActivityStack) designHandle.getCommandStack()).commit();
		assertEquals(1, model.getLayoutSlotDetail().getRowCount());

		((ActivityStack) designHandle.getCommandStack()).undo();
		assertEquals(2, model.getLayoutSlotDetail().getRowCount());
		assertEquals(ColorPropertyType.BLUE, cell.getProperty(IStyleModel.COLOR_PROP));

		((ActivityStack) designHandle.getCommandStack()).startSilentTrans(null);

		row.drop();

		assertEquals(2, model.getLayoutSlotDetail().getRowCount());
		assertEquals(ColorPropertyType.BLACK, cell.getProperty(IStyleModel.COLOR_PROP));

		((ActivityStack) designHandle.getCommandStack()).rollback();

		assertEquals(2, model.getLayoutSlotDetail().getRowCount());
		assertEquals(ColorPropertyType.BLUE, cell.getProperty(IStyleModel.COLOR_PROP));

		designHandle.getCommandStack().startTrans(null);

		RowHandle row1 = designHandle.getElementFactory().newTableRow(3);
		RowHandle row2 = designHandle.getElementFactory().newTableRow(3);

		cell = (CellHandle) row1.getCells().get(0);
		table.getDetail().add(row1);

		((ActivityStack) designHandle.getCommandStack()).startSilentTrans(null);

		cell = (CellHandle) row2.getCells().get(0);

		table.getDetail().add(row2);

		// not updated, returns the default color

		assertEquals(ColorPropertyType.BLACK, cell.getProperty(IStyleModel.COLOR_PROP));

		designHandle.getCommandStack().commit();

		// layout is updated.

		assertEquals(ColorPropertyType.BLUE, cell.getProperty(IStyleModel.COLOR_PROP));

		designHandle.getCommandStack().commit();

	}

	/**
	 * Test filter event transaction.
	 * 
	 * @throws SemanticException
	 */

	public void testFilterEventTransaction() throws SemanticException {
		createDesign();

		ElementListener listener = new ElementListener();

		ElementFactory factory = new ElementFactory(designHandle.getModule());
		GridHandle gridHandle = factory.newGridItem("Grid1", 10, 10); //$NON-NLS-1$

		// Register the listener on every element within the grid.

		designHandle.addListener(listener);
		gridHandle.addListener(listener);

		List columns = gridHandle.getColumns().getContents();
		for (Iterator iter = columns.iterator(); iter.hasNext();) {
			((ColumnHandle) iter.next()).addListener(listener);
		}

		List rows = gridHandle.getRows().getContents();
		for (Iterator iter = rows.iterator(); iter.hasNext();) {
			RowHandle row = (RowHandle) iter.next();

			List cells = row.getCells().getContents();
			for (Iterator iter1 = cells.iterator(); iter1.hasNext();) {
				CellHandle cell = (CellHandle) iter1.next();
				cell.addListener(listener);
			}

			row.addListener(listener);
		}

		designHandle.getBody().add(gridHandle);
		listener.restart();

		// drop grid, ELEMENT_DELETED, container event, stack event.

		gridHandle.drop();
		assertEquals(124, listener.notifications.size());
		listener.restart();

		designHandle.addListener(listener);
		gridHandle.addListener(listener);

		// cells has been removed, so it is 242 - 100 = 142 notifications.

		designHandle.getCommandStack().undo();
		assertEquals(144, listener.notifications.size());
	}

	/**
	 * test the send notification is correct when the FilterEventCompoundRecord is
	 * in a normal compound record.
	 * 
	 * @throws SemanticException
	 */

	public void testNestedFilterEventTransaction() throws SemanticException {

		createDesign();

		ElementListener listener = new ElementListener();

		ElementFactory factory = new ElementFactory(designHandle.getModule());
		TableHandle table = factory.newTableItem("newTable"); //$NON-NLS-1$
		GridHandle grid = factory.newGridItem("newGrid"); //$NON-NLS-1$

		try {
			designHandle.getBody().add(table);
			designHandle.getBody().add(grid);
		} catch (ContentException e) {
			assert false;
		} catch (NameException e) {
			assert false;
		}

		designHandle.addListener(listener);
		ActivityStack stack = (ActivityStack) designHandle.getCommandStack();

		stack.startTrans(null);
		stack.startFilterEventTrans("drop table"); //$NON-NLS-1$

		table.drop();

		stack.startFilterEventTrans("drop grids"); //$NON-NLS-1$

		grid.drop();

		stack.commit();
		assertEquals(0, listener.notifications.size());

		stack.commit();
		assertEquals(4, listener.notifications.size());

		stack.commit();

		assertEquals(8, listener.notifications.size());

	}

	/**
	 * Tests clearListeners() to destory context of ActivityStack.
	 */

	public void testDispose() {
		MyActivityStackListener listener = new MyActivityStackListener();
		as.addListener(listener);

		as.execute(record);
		as.undo();

		as.startTrans(null);

		as.clearListeners();
		as.flush();

		assertFalse(as.canRedo());
		assertFalse(as.canUndo());
		assertEquals(0, as.getRecords().length);
		assertNull(ActivityTestUtil.getActivityListener(as));
	}

	/**
	 * 
	 */

	private static class ElementListener implements Listener {

		List notifications = new ArrayList();

		public static class Notification {

			DesignElementHandle target = null;
			NotificationEvent event = null;

			Notification(DesignElementHandle element, NotificationEvent event) {
				this.target = element;
				this.event = event;
			}
		}

		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			this.notifications.add(new Notification(focus, ev));
		}

		/**
		 * 
		 */

		public void restart() {
			this.notifications.clear();
		}
	}

	/**
	 * Mock up ElementRecord. The target of the ActivityRecord is linked to a
	 * MockupDesignElement.
	 */

	class MockupActivityRecord extends AbstractElementRecord {

		/**
		 * Whether or not this ActivityRecord has been executed.
		 */
		boolean executed = false;

		/**
		 * Whether or not this ActivityRecord has sentNodifications.
		 */
		boolean sendNotification = false;

		/**
		 * Whether or not this ActivityRecord can be Undo.
		 */
		boolean canUndo = false;

		/**
		 * Whether or not this ActivityRecord can be Redo.
		 */
		boolean canRedo = false;

		/**
		 * Target element of the ActivityRecord.
		 */
		MockupDesignElement target = null;

		/**
		 * Activity internal id.
		 */
		int activityID;

		/**
		 * Constructor
		 * 
		 * @param target
		 * 
		 * @param activityID
		 */
		public MockupActivityRecord(MockupDesignElement target, int activityID) {
			this.target = target;
			this.activityID = activityID;
		}

		/**
		 * Constructor
		 * 
		 * @param target
		 */
		public MockupActivityRecord(MockupDesignElement target) {
			this.target = target;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.design.core.activity.ActivityRecord
		 * #execute()
		 */
		public void execute() {
			this.executed = true;

			this.target.increase();
			this.canUndo = true;
			this.canRedo = false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.design.core.activity.ActivityRecord
		 * #undo()
		 */
		public void undo() {
			if (this.canUndo) {
				this.target.decrease();
				this.canRedo = true;
				this.canUndo = false;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.design.core.activity.ActivityRecord
		 * #redo()
		 */
		public void redo() {
			if (this.canRedo) {
				this.target.increase();
				this.canUndo = true;
				this.canRedo = false;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.design.core.activity.AbstractElementRecord
		 * #getTarget()
		 */
		public DesignElement getTarget() {
			return this.target;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.design.core.activity.AbstractElementRecord
		 * #getEvent()
		 */

		public NotificationEvent getEvent() {
			return new MockupEvent();
		}

		protected List getPostTasks() {
			this.sendNotification = true;

			List retList = new ArrayList();
			retList.add(new NotificationRecordTask(target, getEvent()));
			return retList;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.activity.ActivityRecord#sendNotifcations
		 * (Stack)
		 */

		public void rollback() {
			undo();
		}

		/**
		 * Returns <code>true</code> if need to hold the event at this time. We need to
		 * hold the event if it is sent inside a transaction that declared to filter
		 * notification events( <code>FilterEventsCompoundRecord</code>).
		 * 
		 * @param transStack the transaction stack.
		 * @return <code>true</code> if need to hold the event at this time, returns
		 *         <code>false</code> otherwise.
		 */
		protected final boolean holdEvent(Stack transStack) {
			if (transStack != null && !transStack.isEmpty()) {
				CompoundRecord cr = (CompoundRecord) transStack.peek();
				if (cr instanceof FilterEventsCompoundRecord)
					return true;
			}

			return false;
		}

	}

	class MockupEvent extends NotificationEvent {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.activity.NotificationEvent#getEventType ()
		 */
		public int getEventType() {
			return NotificationEvent.CONTENT_EVENT;
		}

	}

	/**
	 * Mock up DesignElement, it has a "counter" inside, it can be increase() or
	 * decrease() throw Activity.
	 */

	class MockupDesignElement extends DesignElement {

		private int counter = 0;

		/**
		 * increase the counter by 1.
		 */
		public void increase() {
			++counter;
		}

		/**
		 * decrease the counter by 1.
		 */
		public void decrease() {
			--counter;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.design.core.elements.DesignElement# apply
		 * (org.eclipse.birt.report.model.design.report.elements.DesignVisitor)
		 */
		public void apply(ElementVisitor visitor) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.design.core.elements.DesignElement#
		 * getElementName()
		 */
		public String getElementName() {
			return ReportDesignConstants.TABLE_ITEM;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.design.core.elements.DesignElement#
		 * getHandle (org.eclipse.birt.report.model.design.core.activity.DesignContext)
		 */
		public DesignElementHandle getHandle(Module rootElement) {
			return null;
		}

	}

	class MyActivityStackListener implements ActivityStackListener {

		ActivityStackEvent event = null;

		int action = -1;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.model.activity.ActivityStackListener#stackChanged
		 * (org.eclipse.birt.report.model.activity.ActivityStackEvent)
		 */
		public void stackChanged(ActivityStackEvent event) {
			this.event = event;
			action = event.getAction();

		}
	}

	class MyElementListener implements Listener {

		NotificationEvent event = null;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.Listener#elementChanged(org.eclipse
		 * .birt.report.model.api.DesignElementHandle,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			event = ev;
			assertNotNull(event);
		}

	}

}
