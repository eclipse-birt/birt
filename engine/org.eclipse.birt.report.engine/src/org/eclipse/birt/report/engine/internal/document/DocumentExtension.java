package org.eclipse.birt.report.engine.internal.document;

public class DocumentExtension {
	long index = -1;
	long parent = -1;
	long firstChild = -1;
	long lastChild = -1;
	long previous = -1;
	long next = -1;

	/**
	 * the content unique id.
	 */
	long contentId = -1;

	long firstChildId;
	long lastChildId;

	ExtensionSegment head = null;
	ExtensionSegment latest = null;

	public DocumentExtension(long index) {
		this.index = index;
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public long getFirstChild() {
		return firstChild;
	}

	public void setFirstChild(long firstChild) {
		this.firstChild = firstChild;
	}

	public long getNext() {
		return next;
	}

	public void setNext(long next) {
		this.next = next;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}

	public long getPrevious() {
		return previous;
	}

	public void setPrevious(long previous) {
		this.previous = previous;
	}

	public long getContentId() {
		return contentId;
	}

	public void setContentId(long contentId) {
		this.contentId = contentId;
	}

	public void add(DocumentExtension ext) {
		ext.setParent(index);
		if (firstChild == -1) {
			updateFirstChild(ext);
			lastChild = firstChild;
			lastChildId = firstChildId;
			return;
		}
		if (head == null) {
			// link ext at the tail of latest ext.
			if (ext.contentId == lastChildId + 1) {
				ext.setPrevious(lastChild);
				ext.setNext(-1);
				lastChild = ext.index;
				lastChildId = ext.contentId;
			} else {
				head = new ExtensionSegment(firstChildId, firstChild, lastChildId, lastChild);
				insert(ext);
			}
		} else {
			if (latest != null && ext.contentId == latest.endId + 1) {
				ext.setPrevious(latest.endIndex);
				if (latest.next != null) {
					ext.setNext(latest.next.startIndex);
				} else {
					ext.setNext(-1);
				}
				latest.endId = ext.contentId;
				latest.endIndex = ext.index;
			} else {
				insert(ext);
			}
		}
	}

	private void updateFirstChild(DocumentExtension ext) {
		firstChild = ext.index;
		firstChildId = ext.contentId;
	}

	private void insert(DocumentExtension ext) {
		ExtensionSegment current = head;
		do {
			// add a new segment before current segment.
			if (ext.contentId < current.startId - 1) {
				ExtensionSegment newSeg = new ExtensionSegment(ext);
				newSeg.next = current;
				newSeg.prev = current.prev;
				if (current.prev != null) {
					current.prev.next = newSeg;
				} else {
					head = newSeg;
				}
				current.prev = newSeg;

				if (newSeg.prev != null) {
					ext.setPrevious(newSeg.prev.endIndex);
					ext.setNext(current.startIndex);
				} else {
					ext.setPrevious(-1);
					ext.setNext(firstChild);
					updateFirstChild(ext);
				}
				latest = newSeg;
				return;
			}

			// append ext before current segment.
			if (ext.contentId == current.startId - 1) {
				if (current.prev != null) {
					ext.setPrevious(current.prev.endIndex);
					ext.setNext(current.startIndex);
				} else {
					ext.setPrevious(-1);
					ext.setNext(firstChild);
					updateFirstChild(ext);
				}
				current.appendBefore(ext);
				return;
			}

			// append ext after current segment.
			if (ext.contentId == current.endId + 1) {
				ext.setPrevious(current.endIndex);
				if (current.next != null) {
					ext.setNext(current.next.startIndex);

				} else {
					ext.setNext(-1);
				}
				current.appendAfter(ext);
				latest = current;
				return;
			}

			if (current.next == null) {
				break;
			}
			current = current.next;

		} while (true);

		// add a new segment after current segment.
		assert (ext.contentId > current.endId + 1);

		ext.setPrevious(current.endIndex);
		ext.setNext(-1);

		ExtensionSegment newSeg = new ExtensionSegment(ext);
		current.next = newSeg;
		newSeg.next = null;
		newSeg.prev = current;
		latest = newSeg;
		return;
	}
}

class ExtensionSegment {
	long startId;
	long startIndex;
	long endId;
	long endIndex;
	ExtensionSegment prev;
	ExtensionSegment next;

	ExtensionSegment(DocumentExtension ext) {
		startId = ext.contentId;
		startIndex = ext.index;
		endId = ext.contentId;
		endIndex = ext.index;
	}

	ExtensionSegment(long startId, long startIndex, long endId, long endIndex) {
		this.startId = startId;
		this.startIndex = startIndex;
		this.endId = endId;
		this.endIndex = endIndex;
	}

	void appendBefore(DocumentExtension ext) {
		startId = ext.contentId;
		startIndex = ext.index;
	}

	void appendAfter(DocumentExtension ext) {
		endId = ext.contentId;
		endIndex = ext.index;
	}

}
