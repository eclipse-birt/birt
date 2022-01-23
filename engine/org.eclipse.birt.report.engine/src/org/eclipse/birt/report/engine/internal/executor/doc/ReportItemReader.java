/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.executor.doc;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IExecutorContext;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.document.v3.CachedReportContentReaderV3;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;

public class ReportItemReader implements IReportItemExecutor {

	protected ExecutionContext context;

	protected CachedReportContentReaderV3 reader;

	protected ReportItemReader parent;

	protected Fragment fragment;

	ReportItemReader(ExecutionContext context) {
		this.context = context;
	}

	void initialize(ReportItemReader parent, long offset, Fragment frag) {
		assert offset != -1;
		this.parent = parent;
		if (parent != null) {
			this.reader = parent.reader;
		}
		this.offset = offset;
		this.content = null;
		this.child = -1;
		this.rsets = null;
		this.fragment = frag;
	}

	long offset;
	IContent content;
	long child;
	IBaseResultSet[] rsets;

	public IContent getContent() {
		return content;
	}

	public IExecutorContext getContext() {
		throw new UnsupportedOperationException();
	}

	public Object getModelObject() {
		return null;
	}

	public IReportItemExecutor getParent() {
		return parent;
	}

	public IBaseResultSet[] getQueryResults() {
		return rsets;
	}

	public void setContext(IExecutorContext context) {
		throw new IllegalStateException("the caller should never call setContext() of system executor");
	}

	public void setModelObject(Object handle) {
		throw new IllegalStateException("the caller should never call setContext() of system executor");
	}

	public void setParent(IReportItemExecutor parent) {
		throw new IllegalStateException("the caller should never call setParent() of system executor");

	}

	public void close() {
		unloadContent();
	}

	public IContent execute() {
		try {
			// load it from the content stream
			loadContent();
			// setup the report design
			initializeContent();
		} catch (IOException ex) {
			context.addException(new EngineException(ex.getLocalizedMessage(), ex));
		} catch (BirtException bex) {
			context.addException(bex);
		}
		return content;
	}

	ReportItemReader createExecutor(ReportItemReader parent, long child, Fragment frag) {
		ReportItemReader reader = new ReportItemReader(context);
		reader.initialize(parent, offset, frag);
		return reader;
	}

	public IReportItemExecutor getNextChild() {
		if (hasNextChild()) {
			try {
				Fragment childFrag = fragment == null ? null : fragment.getFragment(new Long(child));
				ReportItemReader childReader = createExecutor(this, child, childFrag);
				child = childReader.findNextSibling();
				if (child != -1 && fragment != null) {
					if (!fragment.inFragment(new Long(child))) {
						Fragment nextFragment = fragment.getNextFragment(new Long(child));
						if (nextFragment != null) {
							child = ((Long) nextFragment.index).longValue();
						} else {
							child = -1;
						}
					}
				}
				return childReader;
			} catch (IOException ex) {
				child = -1;
				context.addException(new EngineException(ex.getLocalizedMessage(), ex));
			}
		}
		return null;
	}

	public boolean hasNextChild() {
		return child != -1;
	}

	protected long findFirstChild() throws IOException {
		loadContent();
		if (content != null) {
			DocumentExtension docExt = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
			long firstChild = docExt.getFirstChild();
			if (firstChild != -1 && fragment != null) {
				if (!fragment.inFragment(new Long(firstChild))) {
					Fragment childFragment = fragment.getFirstFragment();
					if (childFragment != null) {
						return ((Long) childFragment.index).longValue();
					}
					return -1;
				}
			}
			return firstChild;
		}
		return -1;
	}

	protected long findNextSibling() throws IOException {
		loadContent();
		if (content != null) {
			DocumentExtension docExt = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
			return docExt.getNext();
		}
		return -1;
	}

	protected IBaseResultSet getResultSet() {
		if (rsets == null || rsets[0] == null) {
			if (parent != null) {
				return parent.getResultSet();
			}
			return null;
		}
		return rsets[0];
	}

	protected void loadContent() throws IOException {
		if (content == null) {
			content = reader.loadContent(offset);
			if (content != null) {
				DocumentExtension docExt = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
				child = docExt.getFirstChild();
				if (child != -1 && fragment != null) {
					if (!fragment.inFragment(new Long(child))) {
						Fragment childFragment = fragment.getFirstFragment();
						if (childFragment != null) {
							child = ((Long) childFragment.index).longValue();
						} else {
							child = -1;
						}
					}
				}
			}
		}
	}

	/**
	 * intialize the content loaded from the report document.
	 * 
	 * Once the report content is loaded, it is not associated with the report
	 * design, so it almost contains nothing, most of the data should be retetrived
	 * from the design element.
	 * 
	 * In the intialization, it first search the report design to see which design
	 * element creates the report content, then re-load the data from the report
	 * document and uses the data to re-fill some fields of the content.
	 * 
	 * Each content can be intailzied only once.
	 * 
	 */
	private void initializeContent() throws BirtException {
		assert content != null;
		Report report = context.getReport();
		// set up the design object
		InstanceID id = content.getInstanceID();
		if (id != null) {
			long designId = id.getComponentID();
			if (designId != -1) {
				Object generateBy = report.getReportItemByID(designId);
				content.setGenerateBy(generateBy);
				// System.out.println( generateBy.getClass( ));
				if (generateBy instanceof ReportItemDesign) {
					context.setItemDesign((ReportItemDesign) generateBy);
				}
			}
		}
		context.setContent(content);

		/**
		 * filter emtpy group content, a special case is the group content is the ending
		 * of page hint. if not filter it, group header will diplay at this page.
		 */
		if (fragment != null && hasNextChild()) {
			Object genBy = content.getGenerateBy();
			if (content instanceof ITableContent) {
				if (genBy instanceof TableItemDesign) {
					TableItemDesign tableDesign = (TableItemDesign) genBy;
					if (((ITableContent) content).isHeaderRepeat() && tableDesign.getHeader() != null) {
						addHeaderToFragment(content);
					}
				}
			} else if (content instanceof IGroupContent) {
				if (genBy instanceof GroupDesign) {
					GroupDesign groupDesign = (GroupDesign) genBy;
					if (((IGroupContent) content).isHeaderRepeat() && groupDesign.getHeader() != null) {
						addHeaderToFragment(content);
					}
				}
			} else if (content instanceof IListContent) {
				if (genBy instanceof ListItemDesign) {
					ListItemDesign listDesign = (ListItemDesign) genBy;
					if (((IListContent) content).isHeaderRepeat() && listDesign.getHeader() != null) {
						addHeaderToFragment(content);
					}
				}
			}
		}

		IBaseResultSet prset = parent == null ? null : parent.getResultSet();
		// restore the parent result set
		context.setResultSet(prset);
		// open the query used by the content, locate the resource
		rsets = openQueries(prset, content);
		if (rsets != null) {
			context.setResultSets(rsets);
		}
		// execute extra intialization
		initalizeContentVisitor.visit(content, null);
	}

	private void addHeaderToFragment(IContent content) {
		assert fragment != null;
		DocumentExtension docExt = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
		if (docExt != null) {
			long headerOffset = docExt.getFirstChild();
			if (headerOffset != -1) {
				fragment.insertFragment(headerOffset);
				// reset the child offset
				child = headerOffset;
			}
		}
	}

	/**
	 * the content is loaded, and it will not be used by any one else.
	 */
	protected void unloadContent() {
		if (rsets != null) {
			closeQueries(rsets);
			rsets = null;
		}
		reader.unloadContent(offset);
	}

	protected IBaseResultSet[] openQueries(IBaseResultSet rset, IContent content) throws BirtException {
		/*
		 * Before 2.1.2, the dataId is defined by the result set of content itself and
		 * it has been changed in 2.1.3 or later. The dataId is defined by the result
		 * set of the parent content. In the localization stage, the extended item
		 * always assume the result set is a fresh one and always calls next() to
		 * position the cursor to the first row. so we can't move the result set to
		 * first row in open queries.
		 */

		// open the query associated with the current report item
		IBaseResultSet[] rsets = doOpenQueries(rset, content);

		// if it is extended item, we need only locate to result if the item has
		// no query
		Object generateBy = content.getGenerateBy();
		if (generateBy instanceof ExtendedItemDesign) {
			if (rsets == null) {
				doPositionResultSet(rset, content);
			}
		} else {
			if (rsets != null) {
				rset = rsets[0];
			}
			doPositionResultSet(rset, content);
		}
		return rsets;
	}

	protected IBaseResultSet[] doOpenQueries(IBaseResultSet rset, IContent content) throws BirtException {
		IBaseResultSet[] rsets = null;
		Object generateBy = content.getGenerateBy();
		// open the query associated with the current report item
		if (generateBy instanceof ReportItemDesign) {
			ReportItemDesign design = (ReportItemDesign) generateBy;
			boolean useCache = design.useCachedResult();
			IDataQueryDefinition[] queries = design.getQueries();
			if (queries == null) {
				DesignElementHandle itemHandle = design.getHandle();
				if (itemHandle instanceof ReportElementHandle) {
					Report report = context.getReport();
					queries = report.getQueryByReportHandle((ReportElementHandle) itemHandle);
				}
			}
			if (queries != null && queries.length > 0) {
				InstanceID iid = content.getInstanceID();
				if (iid != null) {
					// To the current report item,
					// if the dataId exist and it's deteSet id is not null,
					// and we can find it has parent,
					// we'll try to skip to the current row of the parent
					// query.
					DataID dataId = iid.getDataID();
					if (dataId != null) {
						DataSetID dataSetId = dataId.getDataSetID();
						if (dataSetId != null) {
							DataSetID parentSetId = dataSetId.getParentID();
							// the parent exist.
							if (rset != null) {
								if (rset.getType() == IBaseResultSet.QUERY_RESULTSET) {
									IQueryResultSet qRset = (IQueryResultSet) rset;
									long parentRowId = dataSetId.getRowID();
									if (parentSetId != null && parentRowId != -1) {
										// the parent query's result set is not
										// null, skip to the right row according
										// row id.
										if (parentRowId != qRset.getRowIndex()) {
											qRset.skipTo(parentRowId);
										}
									}
								} else if (rset.getType() == IBaseResultSet.CUBE_RESULTSET) {
									ICubeResultSet qRset = (ICubeResultSet) rset;
									String cellId = dataSetId.getCellID();
									if (cellId != null && !cellId.equals(qRset.getCellIndex())) {
										qRset.skipTo(cellId);
									}
								}
							}
						}
					}
				}
				rsets = new IBaseResultSet[queries.length];
				for (int i = 0; i < rsets.length; i++) {
					// execute query
					try {
						rsets[i] = context.executeQuery(rset, queries[i], design.getHandle(), useCache);
					} catch (BirtException ex) {
						context.addException(design, ex);
					}
				}
			}
		}
		return rsets;
	}

	protected void doPositionResultSet(IBaseResultSet rset, IContent content) throws BirtException {
		// locate the row position to the current position
		InstanceID iid = content.getInstanceID();
		if (iid != null) {
			DataID dataId = iid.getDataID();
			if (dataId != null) {
				if (rset != null) {
					if (rset.getType() == IBaseResultSet.QUERY_RESULTSET) {
						IQueryResultSet qRset = (IQueryResultSet) rset;
						long rowId = dataId.getRowID();

						// rowId should not be -1. If rowId equals to -1 that
						// means the result set is empty.
						// call IResultIterator.next() to force result set
						// start.
						if (rowId == -1) {
							qRset.next();
						}
						if (rowId != -1 && rowId != qRset.getRowIndex()) {
							qRset.skipTo(rowId);
						}
					} else if (rset.getType() == IBaseResultSet.CUBE_RESULTSET) {
						ICubeResultSet qRset = (ICubeResultSet) rset;
						String cellId = dataId.getCellID();
						if (cellId != null && !cellId.equals(qRset.getCellIndex())) {
							qRset.skipTo(cellId);
						}
					}
				}
			}
		}
	}

	protected void closeQueries(IBaseResultSet[] rsets) {
		if (rsets != null) {
			for (int i = 0; i < rsets.length; i++) {
				if (rsets[i] != null) {
					rsets[i].close();
				}
			}
		}
	}

	protected IContentVisitor initalizeContentVisitor = new ContentVisitorAdapter() {

		public Object visitLabel(ILabelContent label, Object value) {
			if (label.getGenerateBy() instanceof TemplateDesign) {
				TemplateDesign design = (TemplateDesign) label.getGenerateBy();
				String promptTextKey = design.getPromptTextKey();
				label.setLabelKey(promptTextKey);
				String promptText = design.getPromptText();
				label.setLabelText(promptText);
			}
			return value;
		}

		public Object visitAutoText(IAutoTextContent autoText, Object value) {
			if (autoText.getType() == IAutoTextContent.TOTAL_PAGE) {
				autoText.setText(String.valueOf(context.getTotalPage()));
			}
			return value;
		}

		public Object visitTable(ITableContent table, Object value) {
			Report report = context.getReport();
			int colCount = table.getColumnCount();
			for (int i = 0; i < colCount; i++) {
				IColumn col = table.getColumn(i);
				InstanceID id = col.getInstanceID();
				if (id != null) {
					long cid = id.getComponentID();
					ColumnDesign colDesign = (ColumnDesign) report.getReportItemByID(cid);
					col.setGenerateBy(colDesign);
				}
			}

			return value;

		}

		public Object visitData(IDataContent data, Object value) {
			if (data.getGenerateBy() instanceof DataItemDesign) {
				DataItemDesign design = (DataItemDesign) data.getGenerateBy();
				if (design.getMap() == null) {
					String bindingColumn = design.getBindingColumn();
					if (bindingColumn != null) {
						IBaseResultSet rset = context.getResultSet();
						if (rset instanceof IQueryResultSet) {
							try {
								Object dataValue = ((IQueryResultSet) rset).getValue(bindingColumn);
								data.setValue(dataValue);
							} catch (BirtException ex) {
								context.addException(design.getHandle(), ex);
							}
						} else if (rset instanceof ICubeResultSet) {
							try {
								Object dataValue = ((ICubeResultSet) rset).getCubeCursor().getObject(bindingColumn);
								data.setValue(dataValue);
							} catch (Exception ex) {
								// context.addException( ex );
							}
						}
					}
					/*
					 * String valueExpr = design.getValue( ); if ( valueExpr != null ) { Object
					 * dataValue = context.evaluate( valueExpr ); data.setValue( dataValue ); }
					 */
				}
			}
			return value;
		}

		public Object visitTableBand(ITableBandContent tableBand, Object value) {
			int bandType = tableBand.getBandType();
			switch (bandType) {
			case IBandContent.BAND_HEADER:
				ITableContent table = getParentTable(tableBand);
				Object genObj = table.getGenerateBy();
				if (genObj instanceof TableItemDesign) {
					TableItemDesign tableDesign = (TableItemDesign) genObj;
					tableBand.setGenerateBy(tableDesign.getHeader());
				}
				break;

			case IBandContent.BAND_FOOTER:
				table = getParentTable(tableBand);
				genObj = table.getGenerateBy();
				if (genObj instanceof TableItemDesign) {
					TableItemDesign tableDesign = (TableItemDesign) genObj;
					tableBand.setGenerateBy(tableDesign.getFooter());
				}
				break;
			case IBandContent.BAND_DETAIL:
				table = getParentTable(tableBand);
				genObj = table.getGenerateBy();
				if (genObj instanceof TableItemDesign) {
					TableItemDesign tableDesign = (TableItemDesign) genObj;
					tableBand.setGenerateBy(tableDesign.getDetail());
				}
				break;

			case IBandContent.BAND_GROUP_FOOTER:
			case IBandContent.BAND_GROUP_HEADER:
				setupGroupBand(tableBand);
				break;
			default:
				assert false;
			}
			return value;
		}

		ITableContent getParentTable(ITableBandContent band) {
			IContent parent = (IContent) band.getParent();
			while (parent != null) {
				if (parent instanceof ITableContent) {
					return (ITableContent) parent;
				}
				parent = (IContent) parent.getParent();
			}
			return null;
		}

		IListContent getParentList(IListBandContent band) {
			IContent parent = (IContent) band.getParent();
			while (parent != null) {
				if (parent instanceof IListContent) {
					return (IListContent) parent;
				}
				parent = (IContent) parent.getParent();
			}
			return null;
		}

		public Object visitListBand(IListBandContent listBand, Object value) {
			int bandType = listBand.getBandType();
			switch (bandType) {
			case IBandContent.BAND_HEADER:
				IListContent list = getParentList(listBand);
				Object genObj = list.getGenerateBy();
				if (genObj instanceof ListItemDesign) {
					ListItemDesign listDesign = (ListItemDesign) genObj;
					listBand.setGenerateBy(listDesign.getHeader());
				}
				break;

			case IBandContent.BAND_FOOTER:
				list = getParentList(listBand);
				genObj = list.getGenerateBy();
				if (genObj instanceof ListItemDesign) {
					ListItemDesign listDesign = (ListItemDesign) genObj;
					listBand.setGenerateBy(listDesign.getFooter());
				}
				break;

			case IBandContent.BAND_DETAIL:
				list = getParentList(listBand);
				genObj = list.getGenerateBy();
				if (genObj instanceof ListItemDesign) {
					ListItemDesign listDesign = (ListItemDesign) genObj;
					listBand.setGenerateBy(listDesign.getDetail());
				}
				break;

			case IBandContent.BAND_GROUP_FOOTER:
			case IBandContent.BAND_GROUP_HEADER:
				setupGroupBand(listBand);
				break;
			default:
				assert false;
			}
			return value;
		}

		protected void setupGroupBand(IBandContent bandContent) {
			IContent parent = (IContent) bandContent.getParent();
			if (parent instanceof IGroupContent) {
				IGroupContent group = (IGroupContent) parent;
				Object genBy = group.getGenerateBy();
				if (genBy instanceof GroupDesign) {
					GroupDesign groupDesign = (GroupDesign) genBy;
					int bandType = bandContent.getBandType();
					if (bandType == IBandContent.BAND_GROUP_HEADER) {
						bandContent.setGenerateBy(groupDesign.getHeader());
					} else {
						bandContent.setGenerateBy(groupDesign.getFooter());
					}
				}
			}
		}
	};

}
