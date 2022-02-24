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

package org.eclipse.birt.report.designer.ui.actions;

import java.text.MessageFormat;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.command.WrapperCommandStack;
import org.eclipse.birt.report.designer.internal.ui.editors.IReportEditor;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.editors.AbstractMultiPageEditor;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * Undo/Redo action for contribution of toolbar or menu.
 */

public abstract class StackWindowAction implements IWorkbenchWindowActionDelegate {

	private ActivityStackListener commandStackListener = new ActivityStackListener() {

		public void stackChanged(ActivityStackEvent event) {
			setAction(iaction, canDo());
		}
	};
	private CommandStack commandStack;
	private ModuleHandle designHandle;
	private IAction iaction;

	protected String getLabelForCommand(Command command) {
		if (command == null)
			return "";//$NON-NLS-1$
		if (command.getLabel() == null)
			return "";//$NON-NLS-1$
		return command.getLabel();
	}

	/**
	 * Returns command stack listener.
	 */
	public ActivityStackListener getCommandStackListener() {
		return commandStackListener;
	}

	/**
	 * 
	 */
	public StackWindowAction() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
		WrapperCommandStack stack = (WrapperCommandStack) getCommandStack();
		if (stack != null) {
			stack.removeCommandStackListener(getCommandStackListener());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.
	 * IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		WrapperCommandStack stack = (WrapperCommandStack) getCommandStack();
		if (stack != null) {
			designHandle = getDesignHandle();

			stack.setActivityStack(getDesignHandle().getCommandStack());
			stack.addCommandStackListener(getCommandStackListener());
		}
	}

	private void resetCommandListener() {
		WrapperCommandStack stack = (WrapperCommandStack) getCommandStack();
		if (stack != null && getDesignHandle() != designHandle) {
			designHandle = getDesignHandle();

			stack.removeCommandStackListener(getCommandStackListener());
			stack.setActivityStack(getDesignHandle().getCommandStack());
			stack.addCommandStackListener(getCommandStackListener());
		}
	}

	protected CommandStack getCommandStack() {
		if (commandStack == null) {
			commandStack = new WrapperCommandStack();
		}
		return commandStack;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Stack window action >> Run ..."); //$NON-NLS-1$
		}
		if (canDo()) {
			doStack();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.
	 * IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		iaction = action;
		changeEnabled(action);
		resetCommandListener();
	}

	private void changeEnabled(IAction action) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage[] pages = window.getPages();

		boolean isEnabled = false;
		for (int i = 0; i < pages.length; i++) {
			IEditorReference[] refs = pages[i].getEditorReferences();

			for (int j = 0; j < refs.length; j++) {
				IEditorPart editor = refs[j].getEditor(false);

				// if ( editor != null
				// && editor.getEditorInput( ) instanceof IReportEditorInput )
				// {
				// if ( editor instanceof AbstractMultiPageEditor )
				// {
				// isEnabled = canDo( );
				// break;
				// }
				// }

				if (editor instanceof AbstractMultiPageEditor) {
					isEnabled = canDo();
					break;
				} else if (editor instanceof IReportEditor) {
					IEditorPart activeEditor = ((IReportEditor) editor).getEditorPart();
					if (activeEditor instanceof AbstractMultiPageEditor) {
						isEnabled = canDo();
						break;
					}
				}
			}
		}
		setAction(action, isEnabled);
	}

	private void setAction(IAction action, boolean isEnabled) {
		action.setEnabled(isEnabled);
		changeLabel(action);
	}

	protected ModuleHandle getDesignHandle() {
		return SessionHandleAdapter.getInstance().getReportDesignHandle();
	}

	abstract protected boolean canDo();

	abstract protected void doStack();

	abstract protected void changeLabel(IAction action);

	public static class UndoWindowAction extends StackWindowAction {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.ui.actions.StackWindowAction#canDo()
		 */
		protected boolean canDo() {
			return getDesignHandle().getCommandStack().canUndo();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.ui.actions.StackWindowAction#doStack()
		 */
		protected void doStack() {
			getDesignHandle().getCommandStack().undo();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.designer.ui.actions.StackWindowAction#changeLabel(org
		 * .eclipse.jface.action.IAction)
		 */
		protected void changeLabel(IAction action) {
			Command undoCmd = getCommandStack().getUndoCommand();
			action.setToolTipText(MessageFormat.format(Messages.getString("UndoAction_Tooltip"), //$NON-NLS-1$
					new Object[] { getLabelForCommand(undoCmd) }).trim());
		}
	}

	public static class RedoWindowAction extends StackWindowAction {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.ui.actions.StackWindowAction#canDo()
		 */
		protected boolean canDo() {
			return getDesignHandle().getCommandStack().canRedo();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.ui.actions.StackWindowAction#doStack()
		 */
		protected void doStack() {
			getDesignHandle().getCommandStack().redo();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.designer.ui.actions.StackWindowAction#changeLabel(org
		 * .eclipse.jface.action.IAction)
		 */
		protected void changeLabel(IAction action) {
			Command redoCmd = getCommandStack().getRedoCommand();
			action.setToolTipText(MessageFormat.format(Messages.getString("RedoAction_Tooltip"), //$NON-NLS-1$
					new Object[] { getLabelForCommand(redoCmd) }).trim());
		}
	}
}
