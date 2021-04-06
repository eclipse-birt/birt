/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.xml;

import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.designer.ui.editors.schematic.action.TextSaveAction;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.texteditor.DefaultRangeIndicator;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.texteditor.StatusTextEditor;

/**
 * XMLEditor
 */
public abstract class XMLEditor extends StatusTextEditor {

	/**
	 * Helper for managing the decoration support of this editor's viewer.
	 *
	 * <p>
	 * This field should not be referenced by subclasses. It is
	 * <code>protected</code> for API compatibility reasons and will be made
	 * <code>private</code> soon. Use
	 * {@link #getSourceViewerDecorationSupport(ISourceViewer)} instead.
	 * </p>
	 */
	protected SourceViewerDecorationSupport fSourceViewerDecorationSupport;

	/**
	 * The overview ruler of this editor.
	 *
	 * <p>
	 * This field should not be referenced by subclasses. It is
	 * <code>protected</code> for API compatibility reasons and will be made
	 * <code>private</code> soon. Use {@link #getOverviewRuler()} instead.
	 * </p>
	 */
	protected IOverviewRuler fOverviewRuler;

	/**
	 * Helper for accessing annotation from the perspective of this editor.
	 *
	 * <p>
	 * This field should not be referenced by subclasses. It is
	 * <code>protected</code> for API compatibility reasons and will be made
	 * <code>private</code> soon. Use {@link #getAnnotationAccess()} instead.
	 * </p>
	 */
	protected IAnnotationAccess fAnnotationAccess;

//	/**
//	 * Preference key for highlighting current line.
//	 */
//	private final static String CURRENT_LINE= AbstractDecoratedTextEditorPreferenceConstants.EDITOR_CURRENT_LINE;
//	/**
//	 * Preference key for highlight color of current line.
//	 */
//	private final static String CURRENT_LINE_COLOR= AbstractDecoratedTextEditorPreferenceConstants.EDITOR_CURRENT_LINE_COLOR;

	private ColorManager colorManager;

	public XMLEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));

		setRangeIndicator(new DefaultRangeIndicator());
//		initializeEditor();
	}

	@Override
	protected void createActions() {
		super.createActions();
		setAction(ITextEditorActionConstants.SAVE, new TextSaveAction(this));
	}

	public void dispose() {
		if (fSourceViewerDecorationSupport != null) {
			fSourceViewerDecorationSupport.dispose();
			fSourceViewerDecorationSupport = null;
		}

		fAnnotationAccess = null;

		colorManager.dispose();
		super.dispose();
		((MultiPageEditorSite) getSite()).dispose();
	}

	public void refreshDocument() {
		setInput(getEditorInput());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.texteditor.AbstractTextEditor#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		IReportProvider provider = getProvider();
		if (provider != null) {
			setDocumentProvider(provider.getReportDocumentProvider(input));
		}
		super.init(site, input);
	}

	protected abstract IReportProvider getProvider();

	/*
	 * @see
	 * org.eclipse.ui.texteditor.AbstractTextEditor#createSourceViewer(Composite,
	 * IVerticalRuler, int)
	 */
	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		ISourceViewer viewer = super.createSourceViewer(parent, ruler, styles);
//		getSourceViewerDecorationSupport(viewer);
		return viewer;
	}

//	/**
//	 * Returns the source viewer decoration support.
//	 *
//	 * @param viewer the viewer for which to return a decoration support
//	 * @return the source viewer decoration support
//	 */
//	protected SourceViewerDecorationSupport getSourceViewerDecorationSupport(ISourceViewer viewer) {
//		if (fSourceViewerDecorationSupport == null) {
//			fSourceViewerDecorationSupport= new SourceViewerDecorationSupport(viewer, getOverviewRuler(), getAnnotationAccess(), getSharedColors());
//			configureSourceViewerDecorationSupport(fSourceViewerDecorationSupport);
//		}
//		return fSourceViewerDecorationSupport;
//	}

//	/**
//	 * Returns the overview ruler.
//	 *
//	 * @return the overview ruler
//	 */
//	protected IOverviewRuler getOverviewRuler() {
//		if (fOverviewRuler == null)
//			fOverviewRuler= createOverviewRuler(getSharedColors());
//		return fOverviewRuler;
//	}

//	/**
//	 * Returns the annotation access.
//	 *
//	 * @return the annotation access
//	 */
//	protected IAnnotationAccess getAnnotationAccess() {
//		if (fAnnotationAccess == null)
//			fAnnotationAccess= createAnnotationAccess();
//		return fAnnotationAccess;
//	}

//	/**
//	 * Creates the annotation access for this editor.
//	 *
//	 * @return the created annotation access
//	 */
//	protected IAnnotationAccess createAnnotationAccess() {
//		return new DefaultMarkerAnnotationAccess();
//	}

//	protected ISharedTextColors getSharedColors() {
//		ISharedTextColors sharedColors= EditorsPlugin.getDefault().getSharedTextColors();
//		return sharedColors;
//	}

//	protected IOverviewRuler createOverviewRuler(ISharedTextColors sharedColors) {
//		IOverviewRuler ruler= new OverviewRuler(getAnnotationAccess(), VERTICAL_RULER_WIDTH, sharedColors);
//		return ruler;
//	}

//	/**
//	 * Configures the decoration support for this editor's source viewer. Subclasses may override this
//	 * method, but should call their superclass' implementation at some point.
//	 *
//	 * @param support the decoration support to configure
//	 */
//	protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
//		support.setCursorLinePainterPreferenceKeys(CURRENT_LINE, CURRENT_LINE_COLOR);
//	}

//	/**
//	 * Initializes this editor. Subclasses may re-implement. If sub-classes do
//	 * not change the contract, this method should not be extended, i.e. do not
//	 * call <code>super.initializeEditor()</code> in order to avoid the
//	 * temporary creation of objects that are immediately overwritten by
//	 * subclasses.
//	 */
//	protected void initializeEditor() {
//		setPreferenceStore(EditorsPlugin.getDefault().getPreferenceStore());
//	}

//	/*
//	 * @see AbstractTextEditor#handlePreferenceStoreChanged(PropertyChangeEvent)
//	 */
//	protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
//
//		try {
//
//			ISourceViewer sourceViewer= getSourceViewer();
//			if (sourceViewer == null)
//				return;
//
//			String property= event.getProperty();
//
//			if (AbstractDecoratedTextEditorPreferenceConstants.SHOW_RANGE_INDICATOR.equals(property)) {
//				if (isRangeIndicatorEnabled()) {
//					getSourceViewer().setRangeIndicator(getRangeIndicator());
//				} else {
//					getSourceViewer().removeRangeIndication();
//					getSourceViewer().setRangeIndicator(null);
//				}
//			}
//
//		} finally {
//			super.handlePreferenceStoreChanged(event);
//		}
//	}

//	/**
//	 * Returns whether the range indicator is enabled according to the preference
//	 * store settings. Subclasses may override this method to provide a custom
//	 * preference setting.
//	 *
//	 * @return <code>true</code> if overwrite mode is enabled
//	 * @since 3.1
//	 */
//	private boolean isRangeIndicatorEnabled() {
//		IPreferenceStore store= getPreferenceStore();
//		return store != null ? store.getBoolean(AbstractDecoratedTextEditorPreferenceConstants.SHOW_RANGE_INDICATOR) : true;
//	}

//	/*
//	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
//	 */
//	public void createPartControl(Composite parent) {
//		super.createPartControl(parent);
//		if (fSourceViewerDecorationSupport != null)
//			fSourceViewerDecorationSupport.install(getPreferenceStore());
//
//		if (!isRangeIndicatorEnabled()) {
//			getSourceViewer().removeRangeIndication();
//			getSourceViewer().setRangeIndicator(null);
//		}
//
//	}

	/*
	 * @see ITextEditor#setHighlightRange(int, int, boolean)
	 */
	public void setHighlightRange(int offset, int length, boolean moveCursor) {
		ISourceViewer fSourceViewer = getSourceViewer();
		if (fSourceViewer == null)
			return;

		if (showsHighlightRangeOnly()) {
			if (moveCursor)
				fSourceViewer.setVisibleRegion(offset, length);
		} else {
			fSourceViewer.setRangeIndication(offset, length, moveCursor);
		}
	}
}
