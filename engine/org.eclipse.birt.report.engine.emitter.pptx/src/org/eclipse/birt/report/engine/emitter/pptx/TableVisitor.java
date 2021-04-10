
package org.eclipse.birt.report.engine.emitter.pptx;

import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.RowArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea;

public class TableVisitor extends TreeVisitor<IArea> {

	TableVisitor() {
	}

	/**
	 * return the next row
	 * 
	 * @param row
	 * @param rowSpan
	 * @return
	 */
	public RowArea getNextRow(RowArea row, final int rowSpan) {
		assert row != null;
		assert rowSpan >= 1;

		AreaTreeNode currentRow = new AreaTreeNode(row);
		ITreeNode<IArea> nextRow = new TreeVisitor<IArea>().forEach(currentRow, new IFilter<IArea>() {

			int rowCount = 0;

			public int getRowCount() {
				return rowCount;
			}

			@Override
			public boolean matches(IArea value) {
				if (value instanceof RowArea) {
					rowCount++;
					if (rowCount > rowSpan) {
						return true;
					}
				}
				return false;
			}
		});

		if (nextRow == null) {
			return null;
		}
		return (RowArea) nextRow.getValue();

	}

	public class AreaTreeNode implements ITreeNode<IArea> {

		private AreaTreeNode parent;
		/**
		 * index in the parent
		 */
		private int index;
		/**
		 * area of current node
		 */
		private IArea value;

		/**
		 * create a root node for table.
		 * 
		 * @param table
		 */
		public AreaTreeNode(IArea value) {
			this.parent = null;
			this.index = 0;
			this.value = value;
		}

		/**
		 * create a child root in parent using index
		 * 
		 * @param parent
		 * @param index
		 */
		public AreaTreeNode(AreaTreeNode parent, int index, IArea area) {
			this.parent = parent;
			this.index = index;
			this.value = area;
		}

		@Override
		public ITreeNode<IArea> getParent() {
			initParent();
			return parent;
		}

		/**
		 * get the next sibling of current node.
		 * 
		 * @return
		 */
		public ITreeNode<IArea> getNext() {
			initParent();
			if (parent == null) {
				return null;
			}
			IArea next = getChildValue(parent.getValue(), index + 1);
			if (next != null) {
				return new AreaTreeNode(parent, index + 1, next);
			}
			return null;
		}

		/**
		 * get the first child of current node.
		 * 
		 * rowArea is handled as leaf node.
		 * 
		 * @return
		 */
		@Override
		public ITreeNode<IArea> getChild() {
			if (getChildCount(value) <= 0) {
				return null;
			}
			return new AreaTreeNode(this, 0, getChildValue(value, 0));
		}

		@Override
		public IArea getValue() {
			return value;
		}

		private void initParent() {
			if (parent == null) {
				IArea container = getParent(value);
				if (container != null) {
					index = getChildIndex(container, value);
					parent = new AreaTreeNode(container);
				}
			}
		}

		private IArea getParent(IArea area) {
			if (area instanceof TableArea) {
				// table has no parent
				return null;
			}
			if (area instanceof ContainerArea) {
				return ((ContainerArea) area).getParent();
			}
			return null;
		}

		private int getChildIndex(IArea parent, IArea child) {
			if (parent instanceof ContainerArea) {
				ContainerArea container = (ContainerArea) parent;
				for (int i = 0; i < container.getChildrenCount(); i++) {
					if (container.getChild(i) == child) {
						return i;
					}
				}
			}
			return -1;
		}

		private IArea getChildValue(IArea value, int index) {
			if (value instanceof ContainerArea) {
				ContainerArea container = (ContainerArea) value;
				if (container.getChildrenCount() > index) {
					return container.getChild(index);
				}
			}
			return null;
		}

		private int getChildCount(IArea value) {
			if (value == null) {
				return 0;
			}
			if (value instanceof RowArea) {
				return 0;
			}

			if (value instanceof ContainerArea) {
				return ((ContainerArea) value).getChildrenCount();
			}
			return 0;
		}
	}
}
