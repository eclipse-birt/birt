package org.eclipse.birt.report.engine.nLayout.area.impl;

public interface ITextListener {

	public void onAddEvent(TextArea textArea);

	public void onNewLineEvent();

	public void onTextEndEvent();
}
