/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.extension.experimental;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.ReportCreationTool;
import org.eclipse.birt.report.designer.internal.ui.palette.ReportElementFactory;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.requests.CreationFactory;

/**
 * CommandCombinedTemplateCreationEntry
 */
public class CommandCombinedTemplateCreationEntry extends CombinedTemplateCreationEntry {

	private PaletteEntryExtension paletteEntry;

	public CommandCombinedTemplateCreationEntry(PaletteEntryExtension paletteEntry) {
		super(paletteEntry.getLabel(), paletteEntry.getDescription(),
				IReportElementConstants.REPORT_ELEMENT_EXTENDED + paletteEntry.getItemName(),
				new ReportElementFactory(IReportElementConstants.REPORT_ELEMENT_EXTENDED + paletteEntry.getItemName(),
						IReportElementConstants.REPORT_ELEMENT_EXTENDED + paletteEntry.getItemName()),
				paletteEntry.getIcon(), paletteEntry.getIconLarge());
		this.paletteEntry = paletteEntry;
	}

	public Tool createTool() {
		return new PaletteEntryCreationTool(this.factory, paletteEntry);
	}
}

class PaletteEntryCreationTool extends ReportCreationTool {

	private CreationFactory factory;
	private PaletteEntryExtension paletteEntry;

	public PaletteEntryCreationTool(CreationFactory factory, PaletteEntryExtension paletteEntry) {
		super(factory, null);
		this.factory = factory;
		setFactory(factory);
		this.paletteEntry = paletteEntry;
	}

	protected void performCreation(int button) {
		try {
			getCreateRequest().setFactory(this.factory);
			CommandUtils.setVariable("targetEditPart", getTargetEditPart()); //$NON-NLS-1$
			CommandUtils.setVariable("request", getTargetRequest()); //$NON-NLS-1$
			Object model = paletteEntry.executeCreate();
			EditPartViewer viewer = getCurrentViewer();
			selectAddedObject(model, viewer);
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
	}

	// public void performCreation( EditPart editPart )
	// {
	// setTargetEditPart( editPart );
	// }

}

class PaletteEntryCreationFactory implements CreationFactory {

	private PaletteEntryExtension paletteEntry;

	public PaletteEntryCreationFactory(PaletteEntryExtension paletteEntry) {
		this.paletteEntry = paletteEntry;
	}

	public Object getNewObject() {
		try {
			return this.paletteEntry.executeCreate();
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
		return null;
	}

	public Object getObjectType() {
		return IReportElementConstants.REPORT_ELEMENT_EXTENDED + paletteEntry.getItemName();
	}

}