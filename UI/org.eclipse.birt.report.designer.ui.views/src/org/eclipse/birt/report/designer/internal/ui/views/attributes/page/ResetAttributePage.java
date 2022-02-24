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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * The sup-class of all resetable attribute page.
 */
public abstract class ResetAttributePage extends AttributePage {

	public void reset() {
		if (!canReset())
			return;

		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(Messages.getString("ResetAttributePage.Style.Restore.Transaction.Name")); //$NON-NLS-1$

		Section[] sectionArray = getSections();
		for (int i = 0; i < sectionArray.length; i++) {
			Section section = sectionArray[i];
			section.reset();
		}
		stack.commit();
	}

	protected void resetAll() {
		if (!canResetAll())
			return;

		List handles = DEUtil.getInputElements(input);
		if (handles != null) {
			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			stack.startTrans(Messages.getString("ResetAttributePage.Style.Restore.All.Transaction.Name")); //$NON-NLS-1$

			try {
				for (Object handle : handles) {
					if (handle instanceof DesignElementHandle) {
						DEUtil.resetAllStyleProperties((DesignElementHandle) handle);
					}
				}

				stack.commit();
			} catch (SemanticException e) {
				stack.rollback();

				ExceptionUtil.handle(e);
			}
		}
	}

	protected boolean canReset() {
		return true;
	}

	protected boolean canResetAll() {
		return canReset();
	}

	class ResetAction extends Action {

		ResetAction() {
			super(null, canResetAll() ? IAction.AS_DROP_DOWN_MENU : IAction.AS_PUSH_BUTTON);
			setImageDescriptor(ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_STYLE_RESOTRE));
			setToolTipText(Messages.getString("ResetAttributePage.Style.Restore.TooltipText")); //$NON-NLS-1$

			if (canResetAll()) {
				setMenuCreator(new IMenuCreator() {

					private Menu mMenu, cMenu;

					@Override
					public Menu getMenu(Menu parent) {
						if (mMenu != null && !mMenu.isDisposed()) {
							return mMenu;
						}
						mMenu = new Menu(parent);
						initMenu(mMenu);
						return mMenu;
					}

					@Override
					public Menu getMenu(Control parent) {
						if (cMenu != null && !cMenu.isDisposed()) {
							return cMenu;
						}

						cMenu = new Menu(parent);
						initMenu(cMenu);
						return cMenu;
					}

					private void initMenu(Menu parent) {
						MenuItem mi = new MenuItem(parent, SWT.PUSH);
						mi.setText(Messages.getString("ResetAttributePage.Style.Restore.Menu.Name")); //$NON-NLS-1$
						mi.addSelectionListener(new SelectionAdapter() {

							public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
								reset();
							};
						});

						mi = new MenuItem(parent, SWT.PUSH);
						mi.setText(Messages.getString("ResetAttributePage.Style.Restore.All.Menu.Name")); //$NON-NLS-1$
						mi.addSelectionListener(new SelectionAdapter() {

							public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
								resetAll();
							};
						});
					}

					@Override
					public void dispose() {
						if (mMenu != null && !mMenu.isDisposed()) {
							mMenu.dispose();
							mMenu = null;
						}

						if (cMenu != null && !cMenu.isDisposed()) {
							cMenu.dispose();
							cMenu = null;
						}
					}
				});
			}
		}

		public void run() {
			reset();
		}

	}

	public Object getAdapter(Class adapter) {
		if (adapter == IAction.class && canReset()) {
			return new Action[] { new ResetAction() };
		}
		return null;
	}
}
