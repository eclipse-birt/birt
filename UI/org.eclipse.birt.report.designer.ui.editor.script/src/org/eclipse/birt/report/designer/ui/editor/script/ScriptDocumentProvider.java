/*************************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.editor.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.script.JSDocumentProvider;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.texteditor.MarkerAnnotation;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;

/**
 * Subclass of <code>JSDocumentProvider</code>, provides an annotation model.
 */
public class ScriptDocumentProvider extends JSDocumentProvider {
	public static final String MARK_TYPE = "org.eclipse.birt.report.debug.ui.script.scriptLineBreakpointMarker";
	/**
	 * ID key
	 */
	public static final String SUBNAME = "sub name";//$NON-NLS-1$
	/**
	 * File name key
	 */
	public static final String FILENAME = "file name";//$NON-NLS-1$
	private String id = ""; //$NON-NLS-1$
	private String fileName = ""; //$NON-NLS-1$

	private boolean isSameElement;

	/**
	 * Creates a new script's document provider with the specified saveable part.
	 * 
	 * @param part the saveable part.
	 */
	public ScriptDocumentProvider(ISaveablePart part) {
		super(part);
	}

	public boolean isSameElement() {
		return isSameElement;
	}

	public void setSameElement(boolean isSameElement) {
		this.isSameElement = isSameElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.DocumentProvider#
	 * createAnnotationModel(java.lang.Object)
	 */
	protected IAnnotationModel createAnnotationModel(Object element) throws CoreException {
		return new DebugResourceMarkerAnnotationModel(ResourcesPlugin.getWorkspace().getRoot());
	}

	/**
	 * Gets the id.
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Update the script to refesh the break point.
	 * 
	 * @param annotationModel
	 */
	public void update(IAnnotationModel annotationModel) {
		if (!(annotationModel instanceof DebugResourceMarkerAnnotationModel)) {
			return;
		}

		DebugResourceMarkerAnnotationModel debugAnno = (DebugResourceMarkerAnnotationModel) annotationModel;
		debugAnno.disconnected();
		debugAnno.connected();
	}

	/**
	 * DebugResourceMarkerAnnotationModel
	 */
	public class DebugResourceMarkerAnnotationModel extends ResourceMarkerAnnotationModel {
		private boolean patch = false;

		private Map<MarkerAnnotation, Position> markMap = new HashMap<MarkerAnnotation, Position>();
		private boolean change = false;

		public DebugResourceMarkerAnnotationModel(IResource resource) {
			super(resource);
		}

		public void beforeChangeText() {
			for (Iterator e = getAnnotationIterator(true); e.hasNext();) {
				Object o = e.next();
				if (o instanceof MarkerAnnotation) {
					MarkerAnnotation a = (MarkerAnnotation) o;

					// System.out.println(a.getType( ));
					IMarker mark = a.getMarker();
					try {
						if (mark == null || !getId().equals(mark.getAttribute(SUBNAME))) {
							continue;
						}
						if (!(ScriptDocumentProvider.MARK_TYPE.equals(a.getMarker().getType()))) {
							continue;
						}
					} catch (CoreException e1) {
						continue;
					}
					Position p = getPosition(a);
					if (p != null && !p.isDeleted()) {
						Position tempCopy = new Position(p.getOffset(), p.getLength());
						markMap.put(a, tempCopy);
					}
				}
			}

			change = true;
		}

		protected boolean isAcceptable(IMarker marker) {
			boolean bool = super.isAcceptable(marker);
			try {
				return bool && getId().equals(marker.getAttribute(SUBNAME))
						&& getFileName().equals(marker.getAttribute(FILENAME));
			} catch (CoreException e) {
				return false;
			}
		}

		protected void disconnected() {
			super.disconnected();
		}

		protected void connected() {
			super.connected();
		}

		/**
		 * 
		 */
		public void resetReportMarkers() {
			if (!change) {
				return;
			}
			for (Iterator<MarkerAnnotation> e = markMap.keySet().iterator(); e.hasNext();) {
				MarkerAnnotation temp = e.next();
				markMap.get(temp).isDeleted = false;
			}
			patch = true;
			resetMarkers();
			patch = false;

			List<MarkerAnnotation> markList = new ArrayList<MarkerAnnotation>();
			for (Iterator e = getAnnotationIterator(true); e.hasNext();) {
				Object o = e.next();
				if (o instanceof MarkerAnnotation) {
					MarkerAnnotation a = (MarkerAnnotation) o;

					try {
						if (a.getMarker() == null || !getId().equals(a.getMarker().getAttribute(SUBNAME))) {
							continue;
						}
						if (!ScriptDocumentProvider.MARK_TYPE.equals(a.getMarker().getType())) {
							continue;
						}
					} catch (CoreException e1) {
						continue;
					}

					IMarker p = findTrueMark(a);
					// removeAnnotation(a, true);
					if (p != null) {

					} else if (isSameElement()) {
						markList.add(a);
					}
				}
			}

			for (Iterator<MarkerAnnotation> e = markMap.keySet().iterator(); e.hasNext();) {
				MarkerAnnotation temp = e.next();
				// if (!markList.contains( temp.getMarker( ) ))
				{
					removeAnnotation(temp, true);
					try {
						addAnnotation(temp, markMap.get(temp), true);
					} catch (BadLocationException e1) {
					}
				}
			}

			removeAnnotations(markList, true, true);
			markMap.clear();
			markList.clear();
			change = false;
		}

		@Override
		protected Position createPositionFromMarker(IMarker marker) {
			Position p = super.createPositionFromMarker(marker);
			if (p == null && patch) {
				p = new Position(0, 0);

				p.isDeleted = true;
			}
			return p;
		}

		protected void addAnnotation(Annotation annotation, Position position, boolean fireModelChanged)
				throws BadLocationException {
			if (annotation instanceof MarkerAnnotation) {
				IMarker marker = ((MarkerAnnotation) annotation).getMarker();
				if (marker != null) {
					try {
						if (!getId().equals(marker.getAttribute(SUBNAME))) {
							return;
						}
						if (!(ScriptDocumentProvider.MARK_TYPE.equals(marker.getType()))) {
							return;
						}

					} catch (CoreException e) {
						// do nothing now
					}
				}
			}
			super.addAnnotation(annotation, position, fireModelChanged);
		}

		private IMarker findTrueMark(MarkerAnnotation a) {
			for (Iterator<MarkerAnnotation> e = markMap.keySet().iterator(); e.hasNext();) {
				MarkerAnnotation temp = e.next();
				if (a.getMarker().equals(temp.getMarker())) {
					return temp.getMarker();
				}
			}
			return null;
		}
	}

	/**
	 * Gets the file name.
	 * 
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Set the file name.
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
