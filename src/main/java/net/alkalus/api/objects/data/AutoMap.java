package net.alkalus.api.objects.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;

import net.alkalus.api.objects.misc.GenericException;

public class AutoMap<V> implements Iterable<V>, Cloneable, Serializable,
        Collection<V>, Queue<V>, List<V> {

    /**
     * The Internal Map
     */
    protected final Map<Integer, V> mInternalMap;
    protected final Map<String, Integer> mInternalNameMap;

    /**
     * The Internal ID
     */
    private int mInternalID = 0;
    private static final long serialVersionUID = 3771412318075131790L;

    public AutoMap() {
        this(new LinkedHashMap<Integer, V>());
    }

    public AutoMap(final Map<Integer, V> defaultMapType) {
        mInternalMap = defaultMapType;
        mInternalNameMap = new LinkedHashMap<String, Integer>();
    }

    public synchronized final Map<Integer, V> getAsMap() {
        return mInternalMap;
    }

    @Override
    public Iterator<V> iterator() {
        return values().iterator();
    }

    public synchronized boolean setValue(final V object) {
        final int mOriginalID = this.mInternalID;
        put(object);
        if (this.mInternalMap.get(mOriginalID).equals(object)
                || mOriginalID > this.mInternalID) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized V put(final V object) {
        return set(object);
    }

    @Override
    public synchronized boolean add(final V object) {
        return set(object) != null;
    }

    public synchronized V set(final V object) {
        if (object == null) {
            return null;
        }
        mInternalNameMap.put("" + object.hashCode(), (mInternalID + 1));
        return mInternalMap.put(mInternalID++, object);
    }

    @Override
    public synchronized V get(final int id) {
        return mInternalMap.get(id);
    }

    public synchronized Collection<V> values() {
        return mInternalMap.values();
    }

    @Override
    public synchronized int size() {
        return mInternalMap.size();
    }

    @Override
    public synchronized int hashCode() {
        return mInternalMap.hashCode();
    }

    public synchronized boolean containsKey(final int key) {
        return mInternalMap.containsKey(key);
    }

    public synchronized boolean containsValue(final V value) {
        return mInternalMap.containsValue(value);
    }

    @Override
    public synchronized boolean isEmpty() {
        return mInternalMap.isEmpty();
    }

    @Override
    public synchronized void clear() {
        this.mInternalID = 0;
        this.mInternalMap.clear();
        this.mInternalNameMap.clear();
        return;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized V[] toArray() {
        final Collection<V> col = this.mInternalMap.values();
        final List<V> abcList = new ArrayList<V>();
        for (final V g : col) {
            abcList.add(g);
        }
        return (V[]) abcList.toArray();
        // return (V[]) new AutoArray(this).getGenericArray();
    }

    public synchronized final int getInternalID() {
        return mInternalID;
    }

    @Override
    public synchronized final boolean remove(final Object value) {
        if (this.mInternalMap.containsValue(value)) {
            return this.mInternalMap
                    .remove(mInternalNameMap.get("" + value.hashCode()), value);
        }
        return false;
    }

    @Override
    public boolean contains(final Object o) {
        for (final V g : this.mInternalMap.values()) {
            if (g.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V[] toArray(final V[] a) {
        return (V[]) toArray();
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        boolean aTrue = true;
        for (final Object g : c) {
            if (!this.contains(g)) {
                aTrue = false;
            }
        }
        return aTrue;
    }

    @Override
    public boolean addAll(final Collection<? extends V> c) {
        boolean aTrue = true;
        for (final V g : c) {
            if (!this.add(g)) {
                aTrue = false;
            }
        }
        return aTrue;
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        boolean aTrue = true;
        for (final Object g : c) {
            if (!this.remove(g)) {
                aTrue = false;
            }
        }
        return aTrue;
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        AutoMap<?> aTempAllocation = new AutoMap();
        boolean aTrue = false;
        aTempAllocation = this;
        aTempAllocation.removeAll(c);
        aTrue = this.removeAll(aTempAllocation);
        aTempAllocation.clear();
        return aTrue;
    }

    @Override
    public boolean offer(final V e) {
        return add(e);
    }

    @Override
    public V remove() {
        final V y = this.get(0);
        if (remove(y)) {
            return y;
        } else {
            return null;
        }
    }

    @Override
    public V poll() {
        if (this.mInternalMap.isEmpty()) {
            return null;
        }
        return remove();
    }

    @Override
    public V element() {
        if (this.mInternalMap.isEmpty()) {
            return null;
        }
        return this.get(0);
    }

    @Override
    public V peek() {
        return element();
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends V> c) {
        return addAll(c);
    }

    @Override
    public V set(final int index, final V element) {
        return set(element);
    }

    @Override
    public void add(final int index, final V element) {
        add(element);
    }

    @Override
    public V remove(final int index) {
        final V aIndexedItem = mInternalMap.get(index);
        if (aIndexedItem == null) {
            return null;
        } else {
            if (remove(aIndexedItem)) {
                return aIndexedItem;
            } else {
                return null;
            }
        }
    }

    @Override
    public int indexOf(final Object o) {
        int aIndex = 0;
        for (@SuppressWarnings("unused")
        final
        int y = 0; aIndex < mInternalMap.size(); aIndex++) {
            if (o.equals(mInternalMap.get(aIndex))) {
                return aIndex;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(final Object o) {
        return indexOf(o);
    }

    @Override
    public ListIterator<V> listIterator() {
        /*
         * ArrayList<V> aInternalList = new ArrayList<V>(); for (V h : values())
         * { aInternalList.add(h); } return aInternalList.listIterator();
         */

        new GenericException("LIST ITERATOR ON AUTOMAP - NULL IMPLEMENTATION");
        return null;
    }

    @Override
    public ListIterator<V> listIterator(final int index) {
        /*
         * ArrayList<V> aInternalList = new ArrayList<V>(); for (V h : values())
         * { aInternalList.add(h); } return aInternalList.listIterator(index);
         */
        // TODO Auto-generated method stub
        new GenericException("LIST ITERATOR ON AUTOMAP - NULL IMPLEMENTATION");
        return null;
    }

    @Override
    public List<V> subList(final int fromIndex, final int toIndex) {
        final ArrayList<V> aData = new ArrayList<V>();
        int aCount = fromIndex;
        while (aCount < toIndex) {
            aData.add(mInternalMap.get(aCount));
            aCount++;
        }
        return aData;
    }

}
