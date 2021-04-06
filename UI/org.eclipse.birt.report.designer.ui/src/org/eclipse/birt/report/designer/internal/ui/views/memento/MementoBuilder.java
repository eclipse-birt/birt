package org.eclipse.birt.report.designer.internal.ui.views.memento;

import org.eclipse.ui.IMemento;

public class MementoBuilder {
	private static Memento memento;

	public final Memento getRootMemento() {
		if (memento == null) {
			MementoElement element = new MementoElement();
			element.setMementoType(MementoElement.Type_Memento);
			memento = new Memento(element, this);
		} else {
			if (memento.getBuilder() != this)
				memento.setBuilder(this);
		}
		return memento;
	}

	public IMemento createMemento(MementoElement element) {
		return new Memento(element, this);
	}

	public MementoElement createElement(String id, String type) {
		if (id != null && !"".equals(id)) //$NON-NLS-1$
		{
			MementoElement memento;

			memento = new MementoElement(id);
			memento.setMementoType(type);

			return memento;
		} else
			return null;
	}
}
