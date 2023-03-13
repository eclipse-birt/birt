/*************************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.script;

import org.eclipse.birt.report.designer.internal.ui.script.JSDocumentProvider;
import org.eclipse.birt.report.designer.internal.ui.script.JSEditorInput;
import org.eclipse.birt.report.designer.internal.ui.script.JSSourceViewerConfiguration;
import org.eclipse.birt.report.designer.internal.ui.script.JSSyntaxContext;
import org.eclipse.birt.report.designer.internal.ui.script.PreferenceNames;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.editors.schematic.action.TextSaveAction;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.IVerticalRulerColumn;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.StatusTextEditor;
import org.eclipse.ui.texteditor.TextOperationAction;

/**
 * The text editor for script.
 */
public class ScriptEditor extends StatusTextEditor implements IScriptEditor {

	/**
	 * The javascript syntax context, provides methods to access avaible Type
	 * meta-data.
	 */
	private final JSSyntaxContext context = new JSSyntaxContext();

	/** The editor input for javascript. */
	private IEditorInput input = createScriptInput(null);

	/** The action registry */
	private ActionRegistry actionRegistry = null;

	/** The parent editor. */
	private final IEditorPart parent;

	/**
	 * Constructs a script editor with the specified parent.
	 *
	 * @param parent the parent editor.
	 */
	public ScriptEditor(IEditorPart parent) {
		this(parent, null);
	}

	/**
	 * Constructs a script editor with the specified parent and the specified
	 * script.
	 *
	 * @param parent the parent editor.
	 * @param script the script to edit
	 */
	public ScriptEditor(IEditorPart parent, String script) {
		super();
		this.parent = parent;
		setSourceViewerConfiguration(new JSSourceViewerConfiguration(context));
		setDocumentProvider(new JSDocumentProvider(parent));
		setScript(script);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.texteditor.StatusTextEditor#createPartControl(org.eclipse.swt.
	 * widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		if (input != null) {
			setInput(input);
		}
		super.createPartControl(parent);
		// bidi_hcg: Force LTR orientation of the StyledText widget
		getSourceViewer().getTextWidget().setOrientation(SWT.LEFT_TO_RIGHT);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.texteditor.AbstractTextEditor#editorContextMenuAboutToShow(org
	 * .eclipse.jface.action.IMenuManager)
	 */
	@Override
	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		menu.add(new Separator(ITextEditorActionConstants.GROUP_UNDO));
		menu.add(new Separator(ITextEditorActionConstants.GROUP_COPY));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		if (isEditable()) {
			addAction(menu, ITextEditorActionConstants.GROUP_UNDO, ITextEditorActionConstants.UNDO);

			addAction(menu, ITextEditorActionConstants.GROUP_COPY, ITextEditorActionConstants.CUT);

			addAction(menu, ITextEditorActionConstants.GROUP_COPY, ITextEditorActionConstants.COPY);

			addAction(menu, ITextEditorActionConstants.GROUP_COPY, ITextEditorActionConstants.PASTE);
		} else {
			addAction(menu, ITextEditorActionConstants.GROUP_COPY, ITextEditorActionConstants.COPY);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#
	 * isEditorInputIncludedInContextMenu()
	 */
	@Override
	protected boolean isEditorInputIncludedInContextMenu() {
		return false;
	}

	/**
	 * Creates an editor input with the specified script.
	 *
	 * @param script the script to edit.
	 * @return an editor input with the specified script.
	 */
	protected IEditorInput createScriptInput(String script) {
		return new JSEditorInput(script);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.WorkbenchPart#getSite()
	 */
	@Override
	public IWorkbenchPartSite getSite() {
		IWorkbenchPartSite site = super.getSite();

		if (site == null) {
			site = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getSite();
		}
		return site;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#createActions()
	 */
	@Override
	protected void createActions() {
		super.createActions();

		IAction contentAssistAction = new TextOperationAction(Messages.getReportResourceBundle(),
				"ContentAssistProposal_", this, ISourceViewer.CONTENTASSIST_PROPOSALS, true);//$NON-NLS-1$

		contentAssistAction.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		setAction("ContentAssistProposal", contentAssistAction);//$NON-NLS-1$
		setAction(ITextEditorActionConstants.SAVE, new TextSaveAction(this));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#setAction(java.lang.String,
	 * org.eclipse.jface.action.IAction)
	 */
	@Override
	public void setAction(String actionID, IAction action) {
		super.setAction(actionID, action);

		if (action != null) {
			if (action.getId() == null) {
				action.setId(actionID);
			}
			getActionRegistry().registerAction(action);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor#
	 * getActionRegistry()
	 */
	@Override
	public ActionRegistry getActionRegistry() {
		if (actionRegistry == null) {
			actionRegistry = new ActionRegistry();
		}
		return actionRegistry;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor#
	 * getViewer()
	 */
	@Override
	public ISourceViewer getViewer() {
		return getSourceViewer();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor#
	 * getScript()
	 */
	@Override
	public String getScript() {
		IDocumentProvider provider = getDocumentProvider();
		String script = ""; //$NON-NLS-1$

		if (provider != null) {
			IDocument document = provider.getDocument(getEditorInput());

			if (document != null) {
				script = document.get();
			}
		}
		return script;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor#
	 * setScript(java.lang.String)
	 */
	@Override
	public void setScript(String script) {
		try {
			IDocumentProvider provider = getDocumentProvider();

			if (provider != null) {
				IDocument document = provider.getDocument(getEditorInput());

				if (document != null) {
					document.set(script == null ? "" : script); //$NON-NLS-1$
					return;
				}
			}
			input = createScriptInput(script);
		} finally {
			ISourceViewer viewer = getSourceViewer();

			if (viewer instanceof SourceViewer) {
				IUndoManager undoManager = ((SourceViewer) viewer).getUndoManager();

				if (undoManager != null) {
					undoManager.reset();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor#
	 * getContext()
	 */
	@Override
	public JSSyntaxContext getContext() {
		return context;
	}

	/**
	 * Creates a new line number ruler column that is appropriately initialized.
	 *
	 * @param annotationModel
	 *
	 * @return the created line number column
	 */
	private IVerticalRulerColumn createLineNumberRulerColumn() {
		LineNumberRulerColumn column = new LineNumberRulerColumn();

		column.setForeground(JSSourceViewerConfiguration.getColorByCategory(PreferenceNames.P_LINENUMBER_COLOR));
		return column;
	}

	/**
	 * Creates a new line number ruler column that is appropriately initialized.
	 *
	 * @return the created line number column
	 */
	private CompositeRuler createCompositeRuler() {
		CompositeRuler ruler = new CompositeRuler();

		ruler.setModel(new AnnotationModel());
		return ruler;
	}

	/**
	 * Creates the vertical ruler to be used by this editor.
	 *
	 * @return the vertical ruler
	 */
	@Override
	protected IVerticalRuler createVerticalRuler() {
		CompositeRuler ruler = createCompositeRuler();

		ruler.addDecorator(0, createLineNumberRulerColumn());
		return ruler;
	}

	/**
	 * Returns the parent editor.
	 *
	 * @return the parent editor.
	 */
	protected IEditorPart getParent() {
		return parent;
	}
}
