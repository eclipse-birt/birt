/* 
 * (c) 2010 Alessandro Colantonio
 * <mailto:colanton@mat.uniroma3.it>
 * <http://ricerca.mat.uniroma3.it/users/colanton>
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
    	
package it.uniroma3.mat.extendedset;


import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * This class provides a skeletal implementation of the {@link ExtendedSet}
 * interface to minimize the effort required to implement this interface.
 * <p>
 * The process of implementing a set by extending this class is very similar,
 * for example, to that of implementing a {@link Collection} by extending
 * {@link AbstractCollection}.
 * 
 * @author Alessandro Colantonio
 * @version $Id: //Actuate/Project-Branches/1584-Remove-Need-4-PreBuild-Cubes/JRP/source/plugins/com.actuate.birt.data.linkeddatamodel/src/it/uniroma3/mat/extendedset/AbstractExtendedSet.java#1 $
 * 
 * @param <T>
 *            the type of elements maintained by this set
 */
public abstract class AbstractExtendedSet<T> extends AbstractSet<T> implements ExtendedSet<T> {
	/**
	 * {@inheritDoc}
	 */
	
	public ExtendedSet<T> intersection(Collection<? extends T> other) {
		ExtendedSet<T> clone = clone();
		clone.retainAll(other);
		return clone;
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public ExtendedSet<T> union(Collection<? extends T> other) {
		ExtendedSet<T> clone = clone();
		clone.addAll(other);
		return clone;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public ExtendedSet<T> difference(Collection<? extends T> other) {
		ExtendedSet<T> clone = clone();
		clone.removeAll(other);
		return clone;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public ExtendedSet<T> symmetricDifference(Collection<? extends T> other) {
		ExtendedSet<T> res = union(other);
		res.removeAll(intersection(other));
		return res;
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public ExtendedSet<T> complemented() {
		ExtendedSet<T> clone = clone();
		clone.complement();
		return clone;
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public boolean containsAny(Collection<? extends T> other) {
		return other == null || other.isEmpty() || intersectionSize(other) > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public boolean containsAtLeast(Collection<? extends T> other, int minElements) {
		if (minElements < 1)
			throw new IllegalArgumentException();
		return intersectionSize(other) >= minElements;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public int intersectionSize(Collection<? extends T> other) {
		if (other == null || other.isEmpty() || isEmpty())
			return 0;
		return intersection(other).size();
	}

	/**
	 * {@inheritDoc}
	 */
	
	public int unionSize(Collection<? extends T> other) {
		return other == null ? size() : size() + other.size() - intersectionSize(other);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public int symmetricDifferenceSize(Collection<? extends T> other) {
		return other == null ? size() : size() + other.size() - 2 * intersectionSize(other);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public int differenceSize(Collection<? extends T> other) {
		return other == null ? size() : size() - intersectionSize(other);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public int complementSize() {
		return complemented().size();
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public abstract ExtendedSet<T> empty();

	/**
	 * {@inheritDoc}
	 */
	
	public ExtendedSet<T> headSet(T toElement) {
		return new ExtendedSubSet(null, toElement);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public ExtendedSet<T> subSet(T fromElement, T toElement) {
		return new ExtendedSubSet(fromElement, toElement);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public ExtendedSet<T> tailSet(T fromElement) {
		return new ExtendedSubSet(fromElement, null);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public T first() {
		if (isEmpty()) 
			throw new NoSuchElementException();
		return iterator().next();
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public T last() {
		if (isEmpty()) 
			throw new NoSuchElementException();
		return descendingIterator().next();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>NOTE:</b> When overriding this method, please note that
	 * <code>Object.clone()</code> is much slower then performing
	 * <code>new</code> and "manually" copying data!
	 */
	@SuppressWarnings("unchecked")
	
	public ExtendedSet<T> clone() {
		try {
			return (ExtendedSet<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	
	public abstract double bitmapCompressionRatio();

	/**
	 * {@inheritDoc}
	 */
	
	public abstract double collectionCompressionRatio();

	/**
	 * {@inheritDoc}
	 */
	
	@SuppressWarnings("unchecked")
	public ExtendedIterator<T> descendingIterator() {
		// used to compare items
		Comparator<? super T> tmpComp = AbstractExtendedSet.this.comparator();
		if (tmpComp == null)
			tmpComp = new Comparator<T>() {
				
				public int compare(T o1, T o2) {
					return ((Comparable) o1).compareTo(o2);
				}
			};
		final Comparator<? super T> comp = tmpComp;
			
		return new ExtendedIterator<T>() {
			// iterator from last element
			private final ListIterator<T> itr = new ArrayList<T>(AbstractExtendedSet.this)
					.listIterator(AbstractExtendedSet.this.size());
			
			
			public boolean hasNext() {
				return itr.hasPrevious();
			}

			
			public T next() {
				return itr.previous();
			}
			
			
			public void skipAllBefore(T element) {
				// iterate until the element is found
				while (itr.hasPrevious()) {
					int res = comp.compare(itr.previous(), element);
					
					// the element has not been found, thus the next call to
					// itr.previous() will provide the right value
					if (res < 0)
						return;

					// the element has been found. Hence, we have to get back
					// to make itr.previous() provide the right value
					if (res == 0) {
						itr.next();
						return;
					}
				}
			}

			
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	
	public Iterable<T> descending() {
		return new Iterable<T>() {
			
			public Iterator<T> iterator() {
				return descendingIterator();
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	
	public List<? extends ExtendedSet<T>> powerSet() {
		return powerSet(1, Integer.MAX_VALUE);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public List<? extends ExtendedSet<T>> powerSet(int min, int max) {
		if (min < 1 || max < min)
			throw new IllegalArgumentException();

		// special cases
		List<ExtendedSet<T>> res = new ArrayList<ExtendedSet<T>>();
		if (size() < min)
			return res;
		if (size() == min) {
			res.add(this.clone());
			return res;
		}
		if (size() == min + 1) {
			for (T item : this.descending()) {
				ExtendedSet<T> set = this.clone();
				set.remove(item);
				res.add(set);
			}
			if (max > min)
				res.add(this.clone());
			return res;
		}

		// the first level contains only one prefix made up of all 1-subsets
		List<List<ExtendedSet<T>>> level = new ArrayList<List<ExtendedSet<T>>>();
		level.add(new ArrayList<ExtendedSet<T>>());
		for (T item : this) {
			ExtendedSet<T> single = this.empty();
			single.add(item);
			level.get(0).add(single);
		}
		if (min == 1)
			res.addAll(level.get(0));

		// all combinations
		int l = 2;
		while (!level.isEmpty() && l <= max) {
			List<List<ExtendedSet<T>>> newLevel = new ArrayList<List<ExtendedSet<T>>>();
			for (List<ExtendedSet<T>> prefix : level) {
				for (int i = 0; i < prefix.size() - 1; i++) {
					List<ExtendedSet<T>> newPrefix = new ArrayList<ExtendedSet<T>>();
					for (int j = i + 1; j < prefix.size(); j++) {
						ExtendedSet<T> x = prefix.get(i).clone();
						x.add(prefix.get(j).last());
						newPrefix.add(x);
						if (l >= min)
							res.add(x);
					}
					if (newPrefix.size() > 1)
						newLevel.add(newPrefix);
				}
			}
			level = newLevel;
			l++;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public int powerSetSize() {
		return isEmpty() ? 0 : (int) Math.pow(2, size()) - 1;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public int powerSetSize(int min, int max) {
		if (min < 1 || max < min)
			throw new IllegalArgumentException();
		final int size = size();

		// special cases
		if (size < min)
			return 0;
		if (size == min)
			return 1;

		/*
		 * Compute the sum of binomial coefficients ranging from (size choose
		 * max) to (size choose min) using dynamic programming
		 */

		// trivial cases
		max = Math.min(size, max);
		if (max == min && (max == 0 || max == size))
			return 1;

		// compute all binomial coefficients for "n"
		int[] b = new int[size + 1];    
		for (int i = 0; i <= size; i++)
			b[i] = 1;
		for (int i = 1; i <= size; i++)   
			for (int j = i - 1; j > 0; j--)             
				b[j] += b[j - 1];        
		
		// sum binomial coefficients
		int res = 0;
		for (int i = min; i <= max; i++)
			res += b[i];
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	
	public int compareTo(ExtendedSet<T> o) {
		Iterator<T> thisIterator = this.descendingIterator();
		Iterator<T> otherIterator = o.descendingIterator();
		while (thisIterator.hasNext() && otherIterator.hasNext()) {
			T thisItem = thisIterator.next();
			T otherItem = otherIterator.next();
			int res = ((Comparable) thisItem).compareTo(otherItem);
			if (res != 0)
				return res;
		}
		return thisIterator.hasNext() ? 1 : (otherIterator.hasNext() ? -1 : 0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public void fill(T from, T to) {
		ExtendedSet<T> toAdd = empty();
		toAdd.add(to);
		toAdd.complement();
		toAdd.add(to);

		ExtendedSet<T> toRemove = empty();
		toRemove.add(from);
		toRemove.complement();
		
		toAdd.removeAll(toRemove);
		
		this.addAll(toAdd);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	
	public void clear(T from, T to) {
		ExtendedIterator<T> itr = iterator();
		itr.skipAllBefore(from);
		while (itr.hasNext()) {
			if (((Comparable) itr.next()).compareTo(to) < 0)
				itr.remove();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public void flip(T e) {
		if (!add(e))
			remove(e);
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public T get(int i) {
		int size = size();
		if (i < 0 || i >= size)
			throw new IndexOutOfBoundsException();
		
		Iterator<T> itr;
		if (i < (size / 2)) {
			itr = iterator();
			for (int j = 0; j <= i - 1; j++) 
				itr.next();
		} else {
			itr = descendingIterator();
			for (int j = size - 1; j >= i + 1; j--) 
				itr.next();
		}
		return itr.next();
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public int indexOf(T e) {
		Iterator<T> itr = iterator();
		int i = 0;
		while (itr.hasNext()) {
			if (itr.next().equals(e))
				return i;
			i++;
		}
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public ExtendedSet<T> unmodifiable() {
		return new UnmodifiableExtendedSet();
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public abstract ExtendedIterator<T> iterator();
	
	/**
	 * {@inheritDoc}
	 */
	
	public double jaccardSimilarity(ExtendedSet<T> other) {
		if (isEmpty() && other.isEmpty())
			return 1D;
		int inters = intersectionSize(other);
		return (double) inters / (size() + other.size() - inters);
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public double jaccardDistance(ExtendedSet<T> other) {
		return 1D - jaccardSimilarity(other);
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public double weightedJaccardSimilarity(ExtendedSet<T> other) {
		if (isEmpty() && other.isEmpty())
			return 1D;
		ExtendedSet<T> inters = intersection(other);
		double intersSum = 0D;
		for (T t : inters) 
			if (t instanceof Integer) 
				intersSum += (Integer) t;
			else if (t instanceof Double) 
				intersSum += (Double) t;
			else if (t instanceof Float) 
				intersSum += (Float) t;
			else if (t instanceof Byte) 
				intersSum += (Byte) t;
			else if (t instanceof Long) 
				intersSum += (Long) t;
			else if (t instanceof Short) 
				intersSum += (Short) t;
			else
				throw new IllegalArgumentException("A collection of numbers is required");

		ExtendedSet<T> symmetricDiff = symmetricDifference(other);
		double symmetricDiffSum = 0D;
		for (T t : symmetricDiff) 
			if (t instanceof Integer) 
				symmetricDiffSum += (Integer) t;
			else if (t instanceof Double) 
				symmetricDiffSum += (Double) t;
			else if (t instanceof Float) 
				symmetricDiffSum += (Float) t;
			else if (t instanceof Byte) 
				symmetricDiffSum += (Byte) t;
			else if (t instanceof Long) 
				symmetricDiffSum += (Long) t;
			else if (t instanceof Short) 
				symmetricDiffSum += (Short) t;
			else
				throw new IllegalArgumentException("A collection of numbers is required");
		
		return intersSum / (intersSum + symmetricDiffSum);
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public double weightedJaccardDistance(ExtendedSet<T> other) {
		return 1D - weightedJaccardSimilarity(other);
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public ExtendedSet<T> convert(Object... e) {
		if (e == null)
			return empty();
		return convert(Arrays.asList(e));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	
	public ExtendedSet<T> convert(Collection<?> c) {
		ExtendedSet<T> res = empty();
		res.addAll((Collection<? extends T>) c);
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public String debugInfo() {
		return toString();
	}

	/**
	 * Base class for {@link ExtendedSubSet} and {@link UnmodifiableExtendedSet}
	 */
	protected abstract class FilteredSet implements ExtendedSet<T> {
		/**
		 * @return the container instance, namely the "internal" representation
		 */
		protected abstract ExtendedSet<T> raw();

		/*
		 * Converter methods that allows for good performances with collection
		 * operations by directly working on internal representation
		 */
		 public ExtendedSet<T> convert(Collection<?> c) {
			if (c instanceof AbstractExtendedSet.FilteredSet)
				convert(((AbstractExtendedSet.FilteredSet) c).raw());
			return raw().convert(c);
		}

		 public ExtendedSet<T> convert(Object... e) {
			return raw().convert(e);
		}

		/*
		 * Methods that directly apply to container instance 
		 */
		 public ExtendedSet<T> clone() {return AbstractExtendedSet.this.clone();}
		 public ExtendedSet<T> empty() {return AbstractExtendedSet.this.empty();}
		 public Comparator<? super T> comparator() {return AbstractExtendedSet.this.comparator();}

		/*
		 * Read-only methods
		 */
		 public ExtendedSet<T> unmodifiable() {return raw().unmodifiable();}
		 public ExtendedIterator<T> iterator() {return raw().iterator();}
		 public ExtendedIterator<T> descendingIterator() {return raw().descendingIterator();}
		 public boolean isEmpty() {return raw().isEmpty();}
		 public boolean equals(Object o) {return raw().equals(o);}
		 public int hashCode() {return raw().hashCode();}
		 public int compareTo(ExtendedSet<T> o) {return raw().compareTo(o);}
		 public T first() {return raw().first();}
		 public T last() {return raw().last();}
		 public double bitmapCompressionRatio() {return raw().bitmapCompressionRatio();}
		 public double collectionCompressionRatio() {return raw().collectionCompressionRatio();}
		 public List<? extends ExtendedSet<T>> powerSet() {return raw().powerSet();}
		 public List<? extends ExtendedSet<T>> powerSet(int mins, int maxs) {return raw().powerSet(mins, maxs);}
		 public int powerSetSize() {return raw().powerSetSize();}
		 public int powerSetSize(int mins, int maxs) {return raw().powerSetSize(mins, maxs);}
		 public Object[] toArray() {return raw().toArray();}
		 public <X> X[] toArray(X[] a) {return raw().toArray(a);}
		 public String toString() {return raw().toString();}
		 public ExtendedSet<T> complemented() {return raw().complemented();}
		 public int complementSize() {return raw().complementSize();}
		 public int size() {return raw().size();}
		 public boolean contains(Object o) {return raw().contains(o);}
		 public Iterable<T> descending() {return raw().descending();}
		 public String debugInfo() {return raw().debugInfo();}
		 public T get(int i) {return raw().get(i);}
		 public int indexOf(T e) {return raw().indexOf(e);}

		/*
		 * Methods that requires a call to convert() to assure good performances
		 */
		 public double jaccardDistance(ExtendedSet<T> other) {return raw().jaccardDistance(convert(other));}
		 public double jaccardSimilarity(ExtendedSet<T> other) {return raw().jaccardSimilarity(convert(other));}
		 public double weightedJaccardDistance(ExtendedSet<T> other) {return raw().weightedJaccardDistance(convert(other));}
		 public double weightedJaccardSimilarity(ExtendedSet<T> other) {return raw().weightedJaccardSimilarity(convert(other));}
		 public ExtendedSet<T> difference(Collection<? extends T> other) {return raw().difference(convert(other));}
		 public ExtendedSet<T> symmetricDifference(Collection<? extends T> other) {return raw().symmetricDifference(convert(other));}
		 public ExtendedSet<T> intersection(Collection<? extends T> other) {return raw().intersection(convert(other));}
		 public ExtendedSet<T> union(Collection<? extends T> other) {return raw().union(convert(other));}
		 public int intersectionSize(Collection<? extends T> other) {return raw().intersectionSize(convert(other));}
		 public int differenceSize(Collection<? extends T> other) {return raw().differenceSize(convert(other));}
		 public int unionSize(Collection<? extends T> other) {return raw().unionSize(convert(other));}
		 public int symmetricDifferenceSize(Collection<? extends T> other) {return raw().symmetricDifferenceSize(convert(other));}
		 public boolean containsAll(Collection<?> c) {return raw().containsAll(convert(c));}
		 public boolean containsAny(Collection<? extends T> other) {return raw().containsAny(convert(other));}
		 public boolean containsAtLeast(Collection<? extends T> other, int minElements) {return raw().containsAtLeast(convert(other), minElements);}
	}
	
	/**
	 * Read-only view of the set.
	 * <p>
	 * Note that it extends {@link AbstractExtendedSet} instead of implementing
	 * {@link ExtendedSet} because of the methods {@link #tailSet(Object)},
	 * {@link #headSet(Object)}, and {@link #subSet(Object, Object)}.
	 */
	protected class UnmodifiableExtendedSet extends AbstractExtendedSet<T>.FilteredSet {
		// exception message when writing operations are performed on {@link #unmodifiable()}
		private final static String UNSUPPORTED_MSG = "The class is read-only!";
		
		/*
		 * Unsupported writing methods
		 */
		 public boolean add(T e) {throw new UnsupportedOperationException(UNSUPPORTED_MSG);}
		 public boolean addAll(Collection<? extends T> c) {throw new UnsupportedOperationException(UNSUPPORTED_MSG);}
		 public boolean remove(Object o) {throw new UnsupportedOperationException(UNSUPPORTED_MSG);}
		 public boolean removeAll(Collection<?> c) {throw new UnsupportedOperationException(UNSUPPORTED_MSG);}
		 public boolean retainAll(Collection<?> c) {throw new UnsupportedOperationException(UNSUPPORTED_MSG);}
		 public void clear() {throw new UnsupportedOperationException(UNSUPPORTED_MSG);}
		 public void clear(T from, T to) {throw new UnsupportedOperationException(UNSUPPORTED_MSG);}
		 public void fill(T from, T to) {throw new UnsupportedOperationException(UNSUPPORTED_MSG);}
		 public void complement() {throw new UnsupportedOperationException(UNSUPPORTED_MSG);}
		 public void flip(T e) {throw new UnsupportedOperationException(UNSUPPORTED_MSG);}
		
		/*
		 * Special purpose methods
		 */
		
		// create new iterators where the remove() operation is not permitted
		 public ExtendedIterator<T> iterator() {
			final ExtendedIterator<T> itr = AbstractExtendedSet.this.iterator();
			return new ExtendedIterator<T>() {
				 public boolean hasNext() {return itr.hasNext();}
				 public T next() {return itr.next();}
				 public void skipAllBefore(T element) {itr.skipAllBefore(element);}
				 public void remove() {throw new UnsupportedOperationException(UNSUPPORTED_MSG);}
			};
		}
		 public ExtendedIterator<T> descendingIterator() {
			final ExtendedIterator<T> itr = AbstractExtendedSet.this.descendingIterator();
			return new ExtendedIterator<T>() {
				 public boolean hasNext() {return itr.hasNext();}
				 public T next() {return itr.next();}
				 public void skipAllBefore(T element) {itr.skipAllBefore(element);}
				 public void remove() {throw new UnsupportedOperationException(UNSUPPORTED_MSG);}
			};
		}
		
		/** Returns a read-only subset */
		// TODO: There is a known bug. Indeed, this implementation does not work
		// since modifications to the read-write set are not reflected to the
		// read-only set.
		private ExtendedSet<T> unmodifiableSubSet(T min, T max) {
			ExtendedSet<T> res;
			ExtendedSet<T> range = AbstractExtendedSet.this.empty();
			if (min != null && max != null) {
				range.fill(min, max);
				range.remove(max);
				res = AbstractExtendedSet.this.intersection(range).unmodifiable();
			} else if (max != null) {
				range.add(max);
				range.complement();
				res = AbstractExtendedSet.this.intersection(range).unmodifiable();
			} else {
				range.add(min);
				range.complement();
				res = AbstractExtendedSet.this.difference(range).unmodifiable();
			}
			return res;
		}
		
		// subset operations must be read-only 
		 public ExtendedSet<T> headSet(T toElement) {return unmodifiableSubSet(null, toElement);}
		 public ExtendedSet<T> subSet(T fromElement, T toElement) {return unmodifiableSubSet(fromElement, toElement);}
		 public ExtendedSet<T> tailSet(T fromElement) {return unmodifiableSubSet(fromElement, null);}
		
		 public ExtendedSet<T> unmodifiable() {
			// useless to create another instance
			return this;
		}

		 protected ExtendedSet<T> raw() {
			return AbstractExtendedSet.this;
		}
	}

	/**
	 * Used by  {@link AbstractExtendedSet#headSet(T)} , {@link AbstractExtendedSet#tailSet(T)}  and  {@link AbstractExtendedSet#subSet(T,T)} to offer a restricted view of the entire set
	 */
	protected class ExtendedSubSet extends AbstractExtendedSet<T>.FilteredSet {
		/**
		 * Minimun allowed element (included) and maximum allowed element
		 * (excluded)
		 */
		private final T min;

		/**
		 * Minimun allowed element (included) and maximum allowed element
		 * (excluded)
		 */
		private final T max;

		/**
		 * When <code>max != null</code>, it contains all elements from  {@link #min}   to   {@link #max}   - 1. Otherwise, it contains all the elements <i>strictly</i> below   {@link #min} 
		 * @uml.property  name="range"
		 * @uml.associationEnd  
		 */
		private final ExtendedSet<T> range;

		/**
		 * Creates the subset
		 * 
		 * @param min
		 *            minimun allowed element (<i>included</i>)
		 * @param max
		 *            maximum allowed element (<i>excluded</i>)
		 */
		public ExtendedSubSet(T min, T max) {
			if (min == null && max == null)
				throw new IllegalArgumentException();

			if (min != null && max != null
					&& localComparator.compare(min, max) > 0)
				throw new IllegalArgumentException("min > max");

			this.min = min;
			this.max = max;

			// add all elements that are strictly less than "max"
			range = AbstractExtendedSet.this.empty();
			if (min != null && max != null) {
				range.fill(min, max);
				range.remove(max);
			} else if (max != null) {
				range.add(max);
				range.complement();
			} else {
				range.add(min);
				range.complement();
			}
		}

		
		
		/*
		 * PRIVATE UTILITY METHODS
		 */
		
		/**
		 * Comparator for elements of type <code>T</code>
		 */
		private final Comparator<? super T> localComparator;

		// initialize the comparator
		{
			final Comparator<? super T> c = AbstractExtendedSet.this.comparator();
			if (c != null) {
				localComparator = c;
			} else {
				localComparator = new Comparator<T>() {
					@SuppressWarnings("unchecked")
					
					public int compare(T o1, T o2) {
						return ((Comparable) o1).compareTo(o2);
					}
				};
			}
		}

		/**
		 * Checks if a given set is completely contained within {@link #min} and
		 * {@link #max}
		 * 
		 * @param other
		 *            given set
		 * @return <code>true</code> if the given set is completely contained
		 *         within {@link #min} and {@link #max}
		 */
		private boolean isInRange(ExtendedSet<T> other) {
			return other.isEmpty() || 
					  ((max == null || localComparator.compare(other.last(), max) < 0)
					&& (min == null || localComparator.compare(other.first(), min) >= 0));
		}

		/**
		 * Checks if a given element is completely contained within {@link #min}
		 * and {@link #max}
		 * 
		 * @param e
		 *            given element
		 * @return <code>true</code> if the given element is completely
		 *         contained within {@link #min} and {@link #max}
		 */
		@SuppressWarnings("unchecked")
		private boolean isInRange(Object e) {
			return (max == null || localComparator.compare((T) e, max) < 0)
					&& (min == null || localComparator.compare((T) e, min) >= 0);
		}

		/**
		 * Generates a set that represent a subview of the given set, namely
		 * elements from {@link #min} (included) to {@link #max} (excluded)
		 * 
		 * @param toFilter
		 *            given set
		 * @return the subview
		 */
		private ExtendedSet<T> filter(ExtendedSet<T> toFilter) {
			if (isInRange(toFilter))
				return toFilter;
			if (max != null)
				return toFilter.intersection(range);
			return toFilter.difference(range);
		}

		
		 protected ExtendedSet<T> raw() {
			return filter(AbstractExtendedSet.this);
		}

		
		
		/*
		 * PUBLIC METHODS
		 */
		
		 public ExtendedSet<T> headSet(T toElement) {
			if (localComparator.compare(toElement, max) > 0)
				throw new IllegalArgumentException();
			return AbstractExtendedSet.this.new ExtendedSubSet(min, toElement);
		}

		 public ExtendedSet<T> subSet(T fromElement, T toElement) {
			if (localComparator.compare(fromElement, min) < 0
					|| localComparator.compare(toElement, max) > 0)
				throw new IllegalArgumentException();
			return AbstractExtendedSet.this.new ExtendedSubSet(fromElement, toElement);
		}

		 public ExtendedSet<T> tailSet(T fromElement) {
			if (localComparator.compare(fromElement, min) < 0)
				throw new IllegalArgumentException();
			return AbstractExtendedSet.this.new ExtendedSubSet(fromElement, max);
		}
		
		 public boolean addAll(Collection<? extends T> c) {
			if (c == null)
				return false;
			ExtendedSet<T> other = convert(c);
			if (!isInRange(other))
				throw new IllegalArgumentException();
			return AbstractExtendedSet.this.addAll(other);
		}

		 public boolean removeAll(Collection<?> c) {
			if (c == null)
				return false;
			return AbstractExtendedSet.this.removeAll(filter(convert(c)));
		}

		 public boolean retainAll(Collection<?> c) {
			if (c == null)
				return false;
			ExtendedSet<T> other = convert(c);
			
			if (isInRange(AbstractExtendedSet.this)) 
				return AbstractExtendedSet.this.retainAll(other);

			int sizeBefore = AbstractExtendedSet.this.size();
			ExtendedSet<T> res = AbstractExtendedSet.this.intersection(other);
			clear();
			AbstractExtendedSet.this.addAll(res);
			return AbstractExtendedSet.this.size() != sizeBefore;
		}
		
		 public boolean containsAll(Collection<?> c) {
			if (c == null)
				return false;
			ExtendedSet<T> other = convert(c);
			return isInRange(other) && AbstractExtendedSet.this.containsAll(other);
		}
		
		 public boolean add(T e) {
			if (!isInRange(e))
				throw new IllegalArgumentException();
			return AbstractExtendedSet.this.add(e);
		}

		 public void clear() {
			if (isInRange(AbstractExtendedSet.this)) 
				AbstractExtendedSet.this.clear();
			else if (max != null) 
				AbstractExtendedSet.this.removeAll(range);
			else 
				AbstractExtendedSet.this.retainAll(range);
		}

		 public boolean contains(Object o) {
			return o != null && isInRange(o) && AbstractExtendedSet.this.contains(o);
		}

		 public boolean remove(Object o) {
			return o != null && isInRange(o) && AbstractExtendedSet.this.remove(o);
		}

		 public int size() {
			if (isInRange(AbstractExtendedSet.this))
				return AbstractExtendedSet.this.size();
			if (max != null)
				return AbstractExtendedSet.this.intersectionSize(range);
			return AbstractExtendedSet.this.differenceSize(range);
		}
		
		 public void complement() {
			ExtendedSet<T> c = complemented();
			clear();
			AbstractExtendedSet.this.addAll(c);
		}

		 public int complementSize() {
			return complemented().size();
		}

		 public ExtendedSet<T> complemented() {
			return filter(raw().complemented());
		}

		 public String debugInfo() {
			return String.format("min = %s, max = %s\nmask = %s\nelements = %s", 
					min.toString(), max.toString(), range.debugInfo(), AbstractExtendedSet.this.toString());
		}
		
		 public void clear(T from, T to) {
			ExtendedSet<T> toRemove = empty();
			toRemove.fill(from, to);
			removeAll(toRemove);
		}

		 public boolean containsAny(Collection<? extends T> other) {
			return AbstractExtendedSet.this.containsAny(filter(convert(other)));
		}

		 public boolean containsAtLeast(Collection<? extends T> other, int minElements) {
			return AbstractExtendedSet.this.containsAtLeast(filter(convert(other)), minElements);
		}

		 public Iterable<T> descending() {
			return new Iterable<T>() {
				
				public Iterator<T> iterator() {
					return descendingIterator();
				}
			};
		}

		 public void fill(T from, T to) {
			if (!isInRange(from) || !isInRange(to))
				throw new IllegalArgumentException();
			AbstractExtendedSet.this.fill(from, to);
		}

		 public void flip(T e) {
			if (!isInRange(e))
				throw new IllegalArgumentException();
			AbstractExtendedSet.this.flip(e);
		}

		 public T get(int i) {
			int minIndex = 0;
			if (min != null)
				minIndex = AbstractExtendedSet.this.indexOf(min);
			T r = AbstractExtendedSet.this.get(minIndex + i);
			if (!isInRange(r))
				throw new IllegalArgumentException();
			return r;
		}

		 public int indexOf(T e) {
			if (!isInRange(e))
				throw new IllegalArgumentException();
			int minIndex = 0;
			if (min != null)
				minIndex = AbstractExtendedSet.this.indexOf(min);
			return AbstractExtendedSet.this.indexOf(e) - minIndex;
		}

		 
		public ExtendedSet<T> clone() {
			return raw(); 
		}
	}
}
