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

import java.util.List;
import java.util.Stack;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test for CompoundRecord.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testAppend()}</td>
 * <td>append one record to list</td>
 * <td>size of list is equal to one</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testUndo()}</td>
 * <td>execute record</td>
 * <td>state of activityrecord is done_state</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>undo record</td>
 * <td>state of activityrecord is undone_state</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testRedo()}</td>
 * <td>execute record</td>
 * <td>state of activityrecord is done_state</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>undo record</td>
 * <td>state of activityrecord is undone_state</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>redo record</td>
 * <td>state of activityrecord is redone_state</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testCanUndo()}</td>
 * <td>execute record</td>
 * <td>both records canUnod are true</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>set both records canUndo are true</td>
 * <td>both records canUnod are true</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>set one record canUndo is false</td>
 * <td>one record canUndo is true</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testCanRedo()}</td>
 * <td>execute record</td>
 * <td>record canRedo is false</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>set both records canRedo are true</td>
 * <td>both records canRedo are true</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testPop()}</td>
 * <td>pop from empty list</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>add one record to list and pop it twice</td>
 * <td>first pop return record , second pop return null</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testExecute()}</td>
 * <td>execute empty record list</td>
 * <td>true</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>add two records and execute record list</td>
 * <td>true</td>
 * </tr>
 *
 * </table>
 *
 */
public class CompoundRecordTest extends BaseTestCase {

	CompoundRecord compoundRecord = null;
	MockupActivityRecord activityRecord = null;
	ActivityStack activityStack = null;

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.compoundRecord = new CompoundRecord("SampleCompoundRecord"); //$NON-NLS-1$
		this.activityRecord = new MockupActivityRecord(1);
		this.activityStack = new ActivityStack(null);
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		this.compoundRecord = null;
		this.activityRecord = null;
		this.activityStack = null;
	}

	/**
	 * test append().
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>append one record to list</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>size of list is equal to one</li>
	 * </ul>
	 */

	public void testAppend() {
		List records = compoundRecord.getRecords();
		assertEquals(0, records.size());

		activityStack.execute(activityRecord);
		compoundRecord.append(activityRecord);

		assertEquals(1, records.size());
	}

	/**
	 * Test undo().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>execute record</li>
	 * <li>undo record</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>state of activityrecord is done_state</li>
	 * <li>state of activityrecord is undone_state</li>
	 * </ul>
	 */

	public void testUndo() {
		MockupActivityRecord record1 = new MockupActivityRecord(1);
		MockupActivityRecord record2 = new MockupActivityRecord(1);
		activityStack.execute(record1);
		activityStack.execute(record2);

		// after execute, make sure state of activityrecord is done_state

		assertEquals(ActivityRecord.DONE_STATE, record1.getState());
		assertEquals(ActivityRecord.DONE_STATE, record2.getState());

		compoundRecord.append(record1);
		compoundRecord.append(record2);

		compoundRecord.undo();

		// after undo, check state of activityrecord is become undone_state

		assertEquals(ActivityRecord.UNDONE_STATE, record1.getState());
		assertEquals(ActivityRecord.UNDONE_STATE, record2.getState());
	}

	/**
	 * Test redo().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>execute record</li>
	 * <li>undo record</li>
	 * <LI>redo record</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>state of activityrecord is done_state</li>
	 * <li>state of activityrecord is undone_state</li>
	 * <li>state of activityrecord is redone_state</li>
	 * </ul>
	 */

	public void testRedo() {
		MockupActivityRecord record1 = new MockupActivityRecord(1);
		MockupActivityRecord record2 = new MockupActivityRecord(2);

		activityStack.execute(record1);
		activityStack.execute(record2);

		// after execute, make sure state of activityrecord is done_state

		assertEquals(ActivityRecord.DONE_STATE, record1.getState());
		assertEquals(ActivityRecord.DONE_STATE, record2.getState());

		compoundRecord.append(record1);
		compoundRecord.append(record2);

		compoundRecord.undo();

		// after undo, check state of activityrecord is undone_state

		assertEquals(ActivityRecord.UNDONE_STATE, record1.getState());
		assertEquals(ActivityRecord.UNDONE_STATE, record2.getState());

		compoundRecord.redo();

		// after redo, check state of activityrecord is redone_state

		assertEquals(ActivityRecord.REDONE_STATE, record1.getState());
		assertEquals(ActivityRecord.REDONE_STATE, record2.getState());
	}

	/**
	 * Test canUndo().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>execute record</li>
	 * <li>set both records canUndo are true</li>
	 * <LI>set one record canUndo is false</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>both records canUnod are true</li>
	 * <li>both records canUnod are true</li>
	 * <li>one record canUndo is true</li>
	 * </ul>
	 */

	public void testCanUndo() {
		MockupActivityRecord record1 = new MockupActivityRecord(1);
		MockupActivityRecord record2 = new MockupActivityRecord(2);
		activityStack.execute(record1);
		activityStack.execute(record2);

		compoundRecord.append(record1);
		compoundRecord.append(record2);

		assertEquals(true, record1.canUndo);
		assertEquals(true, record2.canUndo);

		// CanUndo only when all ActivityRecords inside canUndo.
		assertEquals(true, compoundRecord.canUndo());

		record1.canUndo = true;
		record2.canUndo = false;

		// CanUndo only when all ActivityRecords inside canUndo.
		assertEquals(false, compoundRecord.canUndo());
	}

	/**
	 * Test canRedo().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>execute record</li>
	 * <li>set both records canRedo are true</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>record canRedo is false</li>
	 * <li>both records canRedo are true</li>
	 * </ul>
	 */

	public void testCanRedo() {
		MockupActivityRecord record1 = new MockupActivityRecord(1);
		MockupActivityRecord record2 = new MockupActivityRecord(2);
		activityStack.execute(record1);
		activityStack.execute(record2);

		compoundRecord.append(record1);
		compoundRecord.append(record2);

		assertEquals(false, compoundRecord.canRedo());

		record1.canRedo = true;
		record2.canRedo = true;

		// CanUndo only when all ActivityRecords inside the CompoundRecord
		// canUndo.
		assertEquals(true, compoundRecord.canRedo());
	}

	/**
	 * Test canPop().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>pop from empty list</li>
	 * <li>add one record to list and pop it twice</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>null</li>
	 * <li>first pop return record , second pop return null</li>
	 * </ul>
	 */

	public void testPop() {
		assertNull(compoundRecord.pop());

		MockupActivityRecord record1 = new MockupActivityRecord(1);
		activityStack.execute(record1);

		compoundRecord.append(record1);
		assertEquals(record1, compoundRecord.pop());
		assertNull(compoundRecord.pop());
	}

	/**
	 * Test canPop().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>execute empty record list</li>
	 * <li>add two records and execute record list</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>true</li>
	 * <li>true</li>
	 * </ul>
	 */

	public void testExecute() {
		// when list is empty , it is true

		compoundRecord.execute();

		// when state of every element in list is done_state
		// it is true , else assert false error

		MockupActivityRecord record1 = new MockupActivityRecord(1);
		MockupActivityRecord record2 = new MockupActivityRecord(2);

		activityStack.execute(record1);
		activityStack.execute(record2);

		compoundRecord.append(record1);
		compoundRecord.append(record2);

		compoundRecord.execute();

	}

	/**
	 *
	 *
	 * Mockup ElementRecord. The target of the ActivityRecord is linked to a
	 * MockupDesignElement, and suppose execute operation is always successful.
	 *
	 */

	class MockupActivityRecord extends AbstractElementRecord {

		/**
		 * Whether or not this ActivityRecord can be Undo.
		 */
		boolean canUndo = false;

		/**
		 * Whether or not this ActivityRecord can be Redo.
		 */
		boolean canRedo = false;

		/**
		 * Activity internal id.
		 */
		int activityID;

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.design.core.activity.ActivityRecord
		 * #canRedo()
		 */
		@Override
		public boolean canRedo() {
			return this.canRedo;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.design.core.activity.ActivityRecord
		 * #canUndo()
		 */
		@Override
		public boolean canUndo() {
			return this.canUndo;
		}

		/**
		 * Constructor
		 *
		 * @param activityID
		 */
		public MockupActivityRecord(int activityID) {
			this.activityID = activityID;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.design.core.activity.ActivityRecord
		 * #execute()
		 */
		@Override
		public void execute() {
			this.canUndo = true;
			this.canRedo = false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.design.core.activity.ActivityRecord
		 * #undo()
		 */
		@Override
		public void undo() {
			if (this.canUndo) {
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
		@Override
		public void redo() {
			if (this.canRedo) {
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
		@Override
		public DesignElement getTarget() {
			return new MockupDesignElement();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.design.core.activity.AbstractElementRecord
		 * #getEvent()
		 */
		@Override
		public NotificationEvent getEvent() {
			return new MockupEvent();
		}

		@Override
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
				if (cr instanceof FilterEventsCompoundRecord) {
					return true;
				}
			}

			return false;
		}
	}

	/**
	 * Mock up DesignElement, it has a "counter" inside, it can be increase() or
	 * decrease() throw Activity.
	 */

	class MockupDesignElement extends DesignElement {

		/**
		 * increase the counter by 1.
		 */
		public void increase() {
		}

		/**
		 * decrease the counter by 1.
		 */
		public void decrease() {
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.design.core.elements.DesignElement# apply
		 * (org.eclipse.birt.report.model.design.report.elements.DesignVisitor)
		 */
		@Override
		public void apply(ElementVisitor visitor) {
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.design.core.elements.DesignElement#
		 * getElementName()
		 */
		@Override
		public String getElementName() {
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.design.core.elements.DesignElement#
		 * getHandle (org.eclipse.birt.report.model.design.core.activity.DesignContext)
		 */
		@Override
		public DesignElementHandle getHandle(Module rootElement) {
			return null;
		}
	}

	class MockupEvent extends NotificationEvent {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.activity.NotificationEvent#getEventType ()
		 */
		@Override
		public int getEventType() {
			return NotificationEvent.CONTENT_EVENT;
		}

	}

}
