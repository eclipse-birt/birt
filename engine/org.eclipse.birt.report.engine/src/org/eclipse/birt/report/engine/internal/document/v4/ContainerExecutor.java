/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document.v4;

import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.executor.doc.Fragment;
import org.eclipse.birt.report.engine.internal.executor.doc.Segment;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;

/**
 * <p>
 * The container executor uses a fragment to define its children. The fragment
 * is defined by segments, each segment is defined with two InstanceID. The
 * first index of the segment may be Segment.LEFT_MOST_EDGE, which means the
 * segment starts from the first child. The second index of the segment may be
 * Segment.RIGHT_MOST_EDGE, which means it ends to the last children.
 * </p>
 * 
 * <p>
 * To create the child executor, the container needs:
 * <ul>
 * <li>if it starts with the segment, call skipToExecutor to locate the current
 * executor.</li>
 * <li>call createExecutor() to the next executor.</li>
 * <li>get the content from executor to see if it matches the edge end.</li>
 * <li>if it reaches the edge end, skip to the next segment.</li>
 * </ul>
 * </p>
 * <p>
 * To create the child executor, the container needs prepare the content
 * template which is loaded from the document. the template is loaded as:
 * <ul>
 * <li>If there exits the next content, use the content.
 * <li>otherwise,
 * 
 */
abstract public class ContainerExecutor extends ReportItemExecutor {
	/**
	 * we need prepare the next executable child
	 */
	private boolean needPrepareNext;

	protected boolean prepareFirstChild;
	/**
	 * the sections defined in the fragment.
	 */
	protected Object[][] sections;
	/**
	 * if we need jump to next section
	 */
	protected boolean useNextSection;
	/**
	 * the current section used by nextFragment
	 */
	protected int nextSection;

	protected long nextOffset;

	protected ContainerExecutor(ExecutorManager manager, int type) {
		super(manager, type);
		needPrepareNext = true;
		prepareFirstChild = true;
		sections = null;
		useNextSection = true;
		nextSection = -1;
		nextOffset = -1;
	}

	public void close() {
		if (nextOffset != -1) {
			reader.unloadContent(nextOffset);
			nextOffset = -1;
		}
		prepareFirstChild = true;
		needPrepareNext = true;
		sections = null;
		useNextSection = true;
		nextSection = -1;
		nextOffset = -1;
		super.close();
	}

	private IReportItemExecutor childExecutor;

	public boolean hasNextChild() {
		if (needPrepareNext) {
			try {
				childExecutor = prepareChildExecutor();
			} catch (Exception ex) {
				childExecutor = null;
				logger.log(Level.WARNING, ex.getMessage(), ex);
				context.addException(this.getDesign(), new EngineException(ex.getLocalizedMessage(), ex));
			}
			needPrepareNext = false;
		}
		return childExecutor != null;
	}

	public IReportItemExecutor getNextChild() {
		if (hasNextChild()) {
			needPrepareNext = true;
			return childExecutor;
		}
		return null;
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
					long leftOffset = leftIndex.getOffset();

					if (leftOffset == -1) {
						DocumentExtension docExt = (DocumentExtension) content
								.getExtension(IContent.DOCUMENT_EXTENSION);
						if (docExt != null) {
							leftOffset = docExt.getFirstChild();
						}
					}

					while (leftOffset != -1) {
						IContent leftContent = reader.loadContent(leftOffset);
						InstanceID contentId = leftContent.getInstanceID();
						if (compare(leftId, contentId) <= 0) {
							break;
						}
						reader.unloadContent(leftOffset);
						DocumentExtension docExt = (DocumentExtension) leftContent
								.getExtension(IContent.DOCUMENT_EXTENSION);
						assert docExt != null;
						leftOffset = docExt.getNext();
					}

					nextOffset = leftOffset;
					doSkipToExecutor(leftId, nextOffset);
					uniqueId = leftId.getUniqueID();
				}
			}
		}

		// nextOffset points to the offset of next child in the document
		// nextInstanceID points to the instance id of the next child

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
					if (docExt.getIndex() == nextOffset) {
						nextOffset = docExt.getNext();
					}
				}
			}
		}
		return childExecutor;
	}

	abstract protected ReportItemExecutor doCreateExecutor(long offset) throws Exception;

	/**
	 * adjust the nextItem to the nextContent.
	 * 
	 * before call this method, both the nextContent and the nextFragment can't be
	 * NULL.
	 * 
	 * @return
	 */
	abstract protected void doSkipToExecutor(InstanceID id, long offset) throws Exception;
}
