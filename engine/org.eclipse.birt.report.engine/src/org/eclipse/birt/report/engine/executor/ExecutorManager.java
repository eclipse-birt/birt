/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.executor;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.executor.optimize.ExecutionPolicy;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IExecutorContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.AutoTextItemDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl;
import org.eclipse.birt.report.engine.ir.DynamicTextItemDesign;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ListGroupDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.script.internal.ReportContextImpl;
import org.eclipse.birt.report.engine.util.FastPool;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 * 
 * report item executor manager
 * 
 */
public class ExecutorManager {

	public static final String BOOKMARK_PREFIX = "__bookmark_";

	/**
	 * item executor type
	 */
	public static final int GRIDITEM = 0;
	public static final int IMAGEITEM = 1;
	public static final int LABELITEM = 2;
	public static final int LISTITEM = 3;
	public static final int TABLEITEM = 4;
	public static final int DYNAMICTEXTITEM = 5;
	public static final int TEXTITEM = 6;
	public static final int DATAITEM = 7;
	public static final int EXTENDEDITEM = 8;
	public static final int TEMPLATEITEM = 9;
	public static final int AUTOTEXTITEM = 10;
	public static final int LISTBANDITEM = 11;
	public static final int TABLEBANDITEM = 12;
	public static final int ROWITEM = 13;
	public static final int CELLITEM = 14;
	public static final int LISTGROUPITEM = 15;
	public static final int TABLEGROUPITEM = 16;
	public static final int DUMMYITEM = 17;
	public static final int REPORTLETITEM = 18;

	/**
	 * the number of suppported executor
	 */
	public static final int NUMBER = 19;

	/**
	 * execution context
	 */
	protected ExecutionContext context;

	/**
	 * the report executor use this manager.
	 */
	protected ReportExecutor executor;

	protected IExecutorContext executorContext;

	/**
	 * factory used to create the report executor
	 */
	protected ExecutorFactory executorFactory;
	/**
	 * array of free list
	 */
	protected FastPool[] freeList = new FastPool[NUMBER];

	/**
	 * the sequence id of the auto generated bookmarks for the report items that
	 * with query defined and has no user defined bookmarks.
	 */
	private int sequenceID = 0;

	/**
	 * constructor
	 * 
	 * @param context
	 * @param visitor
	 */
	public ExecutorManager(ReportExecutor executor) {
		this.executor = executor;
		this.context = executor.getContext();
		this.executorContext = new ExecutorContext(context);
		for (int i = 0; i < NUMBER; i++) {
			freeList[i] = new FastPool();
		}
		executorFactory = new ExecutorFactory();
	}

	long generateUniqueID() {
		return executor.generateUniqueID();
	}

	/**
	 * generate bookmarks automatically for the report items that with query defined
	 * and has not user specifed bookmarks.
	 * 
	 * @return
	 */
	public String nextBookmarkID() {
		String bookmark = null;
		do {
			bookmark = BOOKMARK_PREFIX + (++sequenceID);
		} while (context.isBookmarkExist(bookmark));
		return bookmark;

	}

	public IExecutorContext getExecutorContext() {
		return executorContext;
	}

	/**
	 * get item executor
	 * 
	 * @param type the executor type
	 * @return item executor
	 */
	protected ReportItemExecutor getItemExecutor(int type) {
		assert (type >= 0) && (type < NUMBER);
		if (!freeList[type].isEmpty()) {
			// the free list is non-empty
			return (ReportItemExecutor) freeList[type].remove();
		}
		switch (type) {
		case GRIDITEM:
			return new GridItemExecutor(this);
		case IMAGEITEM:
			return new ImageItemExecutor(this);
		case LABELITEM:
			return new LabelItemExecutor(this);
		case LISTITEM:
			return new ListItemExecutor(this);
		case TABLEITEM:
			return new TableItemExecutor(this);
		case DYNAMICTEXTITEM:
			return new DynamicTextItemExecutor(this);
		case TEXTITEM:
			return new TextItemExecutor(this);
		case DATAITEM:
			return new DataItemExecutor(this);
		case EXTENDEDITEM:
			return new ExtendedItemExecutor(this);
		case TEMPLATEITEM:
			return new TemplateExecutor(this);
		case AUTOTEXTITEM:
			return new AutoTextItemExecutor(this);
		case LISTBANDITEM:
			return new ListBandExecutor(this);
		case TABLEBANDITEM:
			return new TableBandExecutor(this);
		case ROWITEM:
			return new RowExecutor(this);
		case CELLITEM:
			return new CellExecutor(this);
		case LISTGROUPITEM:
			return new ListGroupExecutor(this);
		case TABLEGROUPITEM:
			return new TableGroupExecutor(this);
		case DUMMYITEM:
			return new DummyItemExecutor(this);
		case REPORTLETITEM:
			return new ReportletItemExecutor(this);
		default:
			throw new UnsupportedOperationException("unsupported executor!"); //$NON-NLS-1$
		}
	}

	public ReportItemExecutor createExecutor(IReportItemExecutor parent, ReportItemDesign design) {
		ReportItemExecutor executor = executorFactory.createExecutor(design);
		if (executor != null) {
			/*
			 * if the parent is not ReportItemExecutor, that means the generated executor is
			 * created by a extended item, the parent will be set in parent's
			 * getChildExecutor()
			 */
			if (parent instanceof ReportItemExecutor) {
				executor.setParent(parent);
			}
			executor.setModelObject(design);
		}
		return executor;
	}

	public ReportItemExecutor createExtendedExecutor(IReportItemExecutor parent, IReportItemExecutor executor) {
		ExtendedItemExecutor wrapper = (ExtendedItemExecutor) getItemExecutor(EXTENDEDITEM);
		if (wrapper != null) {
			wrapper.executor = executor;
			wrapper.setParent(parent);
		}
		return wrapper;
	}

	/**
	 * release item executor
	 * 
	 * @param type         the executor type
	 * @param itemExecutor the item executor
	 */
	public void releaseExecutor(int type, ReportItemExecutor itemExecutor) {
		if (type >= 0 && type < NUMBER) {
			freeList[type].add(itemExecutor);
		}
	}

	protected Logger getLogger() {
		return context.getLogger();
	}

	class ExecutorFactory extends DefaultReportItemVisitorImpl {

		public ReportItemExecutor createExecutor(ReportItemDesign design) {
			ExecutionPolicy executionPolicy = context.getExecutionPolicy();
			if (executionPolicy != null) {
				if (!executionPolicy.needExecute(design)) {
					return getItemExecutor(DUMMYITEM);
				}
			}
			return (ReportItemExecutor) design.accept(this, null);
		}

		public Object visitAutoTextItem(AutoTextItemDesign autoText, Object value) {
			return getItemExecutor(AUTOTEXTITEM);
		}

		public Object visitCell(CellDesign cell, Object value) {
			return getItemExecutor(CELLITEM);
		}

		public Object visitDataItem(DataItemDesign data, Object value) {
			return getItemExecutor(DATAITEM);
		}

		public Object visitExtendedItem(ExtendedItemDesign item, Object value) {
			ExtendedItemExecutor extExecutor = (ExtendedItemExecutor) getItemExecutor(EXTENDEDITEM);

			ExtendedItemHandle handle = (ExtendedItemHandle) item.getHandle();
			IReportItemExecutor executor = context.getExtendedItemManager().createExecutor(handle,
					ExecutorManager.this);
			executor.setContext(executorContext);
			executor.setModelObject(handle);

			extExecutor.executor = executor;

			return extExecutor;
		}

		public Object visitFreeFormItem(FreeFormItemDesign container, Object value) {
			return null;
		}

		public Object visitGridItem(GridItemDesign grid, Object value) {
			return getItemExecutor(GRIDITEM);
		}

		public Object visitImageItem(ImageItemDesign image, Object value) {
			return getItemExecutor(IMAGEITEM);
		}

		public Object visitLabelItem(LabelItemDesign label, Object value) {
			return getItemExecutor(LABELITEM);
		}

		public Object visitListBand(ListBandDesign band, Object value) {
			return getItemExecutor(LISTBANDITEM);
		}

		public Object visitListItem(ListItemDesign list, Object value) {
			return getItemExecutor(LISTITEM);
		}

		public Object visitDynamicTextItem(DynamicTextItemDesign dynamicText, Object value) {
			return getItemExecutor(DYNAMICTEXTITEM);
		}

		public Object visitRow(RowDesign row, Object value) {
			return getItemExecutor(ROWITEM);
		}

		public Object visitTableBand(TableBandDesign band, Object value) {
			return getItemExecutor(TABLEBANDITEM);
		}

		public Object visitTableItem(TableItemDesign table, Object value) {
			return getItemExecutor(TABLEITEM);
		}

		public Object visitTemplate(TemplateDesign template, Object value) {
			return getItemExecutor(TEMPLATEITEM);
		}

		public Object visitTextItem(TextItemDesign text, Object value) {
			return getItemExecutor(TEXTITEM);
		}

		public Object visitListGroup(ListGroupDesign group, Object value) {
			return getItemExecutor(LISTGROUPITEM);
		}

		public Object visitTableGroup(TableGroupDesign group, Object value) {
			return getItemExecutor(TABLEGROUPITEM);
		}

	}

	private class ExecutorContext extends ReportContextImpl implements IExecutorContext {

		public ExecutorContext(ExecutionContext context) {
			super(context);
		}

		public IReportItemExecutor createExecutor(IReportItemExecutor parent, Object handle) {
			if (handle instanceof ReportElementHandle) {
				Report report = context.getReport();
				ReportElementHandle reportElementHandle = (ReportElementHandle) handle;
				ReportItemDesign design = report.findDesign(reportElementHandle);
				return ExecutorManager.this.createExecutor(parent, design);
			}
			return null;
		}

		public IBaseResultSet executeQuery(IBaseResultSet parent, IDataQueryDefinition query) {
			return executeQuery(parent, query, null);
		}

		public IBaseResultSet executeQuery(IBaseResultSet parent, IDataQueryDefinition query, Object handle) {
			if (query != null) {
				boolean useCache = false;

				if (handle instanceof ReportItemHandle) {
					ReportItemHandle referenceHandle = ((ReportItemHandle) handle).getDataBindingReference();
					if (referenceHandle != null) {
						useCache = true;
					}
				}
				try {
					IDataEngine dataEngine = context.getDataEngine();
					IBaseResultSet rset = dataEngine.execute(parent, query, handle, useCache);
					context.setResultSet(rset);
					return rset;
				} catch (BirtException ex) {
					getLogger().log(Level.SEVERE, ex.getMessage(), ex);
					context.addException(this.getDesignHandle(), new EngineException(ex));
					return null;
				}
			}
			return null;
		}

		public IReportContent getReportContent() {
			return context.getReportContent();
		}

		public IDataQueryDefinition[] getQueries(Object handle) {
			if (handle instanceof ReportElementHandle) {
				ReportElementHandle reportElementHandle = (ReportElementHandle) handle;
				Report report = context.getReport();
				return report.getQueryByReportHandle(reportElementHandle);
			}
			return null;
		}

	}
}
