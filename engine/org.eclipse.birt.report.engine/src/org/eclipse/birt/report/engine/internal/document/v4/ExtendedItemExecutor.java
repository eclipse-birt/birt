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

package org.eclipse.birt.report.engine.internal.document.v4;

import java.io.IOException;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.executor.doc.Fragment;
import org.eclipse.birt.report.engine.internal.executor.doc.Segment;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;

/**
 * Processes an extended item.
 */
public class ExtendedItemExecutor extends ContainerExecutor {

	public ExtendedItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.EXTENDEDITEM);
	}

	public void close() {
		closeQuery();
		super.close();
	}

	protected IReportItemExecutor prepareChildExecutor() throws Exception {
		// prepare the offset of the next content
		if (prepareFirstChild) {
			if (fragment == null && nextOffset == -1) {
				DocumentExtension docExt = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
				if (docExt != null) {
					nextOffset = docExt.getFirstChild();
				}
			}
			if (fragment != null) {
				if (sections == null) {
					sections = fragment.getSections();
					nextSection = -1;
					useNextSection = true;
				}
			}
			prepareFirstChild = false;
		}

		if (fragment != null) {
			if (useNextSection) {
				useNextSection = false;
				nextSection++;
				if (sections == null || nextSection >= sections.length) {
					// this is the last one
					return null;
				}

				Object leftEdge = sections[nextSection][0];
				if (leftEdge == Segment.LEFT_MOST_EDGE) {
					DocumentExtension docExt = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
					if (docExt != null) {
						nextOffset = docExt.getFirstChild();
					}
				} else {
					InstanceIndex leftIndex = (InstanceIndex) leftEdge;
					InstanceID leftId = leftIndex.getInstanceID();
					nextOffset = leftIndex.getOffset();
					uniqueId = leftId.getUniqueID();
				}
			}
		}

		// nextOffset points to the offset of next child in the document
		// nextInstanceID points to the instance id of the next child
		if (nextOffset == -1) {
			return null;
		}

		ReportItemExecutor childExecutor = doCreateExecutor(nextOffset);
		if (childExecutor != null) {
			IContent childContent = childExecutor.execute();
			if (childContent != null) {
				// find fragment for the child.
				if (fragment != null) {
					InstanceID childId = childContent.getInstanceID();
					Fragment childFragment = fragment.getFragment(childId);
					if (childFragment != null) {
						childExecutor.setFragment(childFragment);
					}
					Object rightEdge = sections[nextSection][1];
					if (rightEdge != Segment.RIGHT_MOST_EDGE) {
						InstanceIndex rightIndex = (InstanceIndex) rightEdge;
						InstanceID rightId = rightIndex.getInstanceID();
						if (isSameInstance(rightId, childId)) {
							useNextSection = true;
						}
					}
				}

				DocumentExtension docExt = (DocumentExtension) childContent.getExtension(IContent.DOCUMENT_EXTENSION);
				if (docExt != null) {
					nextOffset = docExt.getNext();
				}
			}
		}
		return childExecutor;
	}

	public IContent execute() {

		if (!executed) {
			executed = true;
			try {
				if (offset != -1) {
					/*
					 * increase the unique id
					 */
					generateUniqueID();
					content = reader.loadContent(offset);
					content.setGenerateBy(design);
					// content.setInstanceID( instanceId );
					instanceId = content.getInstanceID();
					IContent pContent = getParentContent();
					if (pContent != null) {
						content.setParent(pContent);
						instanceId = new InstanceID(pContent.getInstanceID(), instanceId.getUniqueID(),
								instanceId.getComponentID(), instanceId.getDataID());
						content.setInstanceID(instanceId);
					}
					doExecute();
				}
			} catch (BirtException ex) {
				logger.log(Level.WARNING, ex.getMessage(), ex);
				context.addException(this.getDesign(), new EngineException(ex));
			} catch (IOException ex) {
				logger.log(Level.WARNING, ex.getMessage(), ex);
				context.addException(this.getDesign(), new EngineException(ex.getLocalizedMessage(), ex));
			}
		}
		return content;
	}

	protected void doExecute() throws IOException, BirtException {
		InstanceID iid = content.getInstanceID();
		DataID dataId = iid.getDataID();
		if (dataId != null) {
			IBaseResultSet rset = getResultSet();
			if (rset == null) {
				rset = restoreParentResultSet();
			}
			if (rset != null) {
				if (rset instanceof IQueryResultSet) {
					IQueryResultSet qrset = (IQueryResultSet) rset;
					long rowId = dataId.getRowID();
					if (rowId != -1) {
						qrset.skipTo(rowId);
					}
				} else if (rset instanceof ICubeResultSet) {
					ICubeResultSet crset = (ICubeResultSet) rset;
					String cid = dataId.getCellID();
					if (cid != null) {
						crset.skipTo(cid);
					}
				}
			}
		}
		executeQuery();
	}

	protected void executeQuery() {
		getParentResultSet();
		/*
		 * we needn't skip to the first row as other report items.
		 */
		if (design != null) {
			boolean useCache = design.useCachedResult();
			IDataQueryDefinition[] queries = design.getQueries();
			if (queries != null) {
				rsets = new IBaseResultSet[queries.length];
				try {
					IBaseResultSet prset = restoreParentResultSet();
					for (int i = 0; i < queries.length; i++) {
						if (queries[i] == null) {
							rsets[i] = null;
						} else {
							IBaseResultSet rset = context.executeQuery(prset, queries[i], design.getHandle(), useCache);
							if (rset != null && rset.getType() == IBaseResultSet.CUBE_RESULTSET
									&& ((ICubeResultSet) rset).getCubeCursor() == null) {
								rsets[i] = null;
							} else {
								rsets[i] = rset;
							}
						}
					}
					context.setResultSets(rsets);
				} catch (BirtException ex) {
					context.addException(this.getDesign(), ex);
				}
			}
		}
	}

	protected IContent doCreateContent() {
		throw new java.lang.IllegalStateException("can not create the content for extended item");
	}

	protected ReportItemExecutor doCreateExecutor(long offset) throws Exception {
		if (offset != -1) {
			IContent content = reader.loadContent(offset);
			InstanceID iid = content.getInstanceID();
			ReportItemDesign design = (ReportItemDesign) report.getDesign().getReportItemByID(iid.getComponentID());
			return manager.createExecutor(this, design, offset);
		}
		return null;
	}

	protected void doSkipToExecutor(InstanceID id, long offset) throws Exception {
	}
}
