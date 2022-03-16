/*******************************************************************************
 * Copyright (c) 2021, 2022 Solme AB and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Claes Rosell  - initial API and implementation
 *  Alexander Fedorov (ArSysOp)  - structural improvements
 *******************************************************************************/
package org.eclipse.birt.report.designer.internal.ui.handlers;

import java.util.List;
import java.util.Optional;

import org.eclipse.birt.report.designer.ui.preview.PreviewUtil;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 *
 * Abstract handler used for all the report file handlers
 *
 */
abstract class AbstractFileHandler extends AbstractHandler {

	@Override
	public final Object execute(ExecutionEvent event) throws ExecutionException {
		// FIXME: AF: not sure if we always need to clear properties
		PreviewUtil.clearSystemProperties();
		Optional<IFile> selected = selectedFile(HandlerUtil.getCurrentStructuredSelection(event));
		// it is always true if we formulated the right expression in plugin.xml
		if (selected.isPresent()) {
			try {
				execute(selected.get());
			} catch (Exception e) {
				ExceptionUtil.handle(e);
				// FIXME:AF: do we add any value for user throwing from here?
				throw new ExecutionException("Error executing handler", e); //$NON-NLS-1$
			}
		}
		return null;
	}

	protected Optional<IFile> selectedFile(IStructuredSelection selection) {
		List<?> list = selection.toList();
		// FIXME: AF: this constraint should go to plugin.xml
		if (list.size() != 1) {
			return Optional.empty();
		}
		return list.stream()//
				.filter(IFile.class::isInstance)//
				.map(IFile.class::cast)//
				.findFirst();
	}

	protected abstract void execute(IFile file) throws Exception;

}
