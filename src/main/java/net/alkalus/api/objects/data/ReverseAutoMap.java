package net.alkalus.api.objects.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReverseAutoMap<N> extends AutoMap<N> {

    /**
     * The Internal Map
     */
    private final Map<N, Integer> mInternalMapReverseLookup = new HashMap<N, Integer>();

    /**
     * The Internal ID
     */
    private int mInternalID = 0;
    private static final long serialVersionUID = 3771412318075131790L;

    @Override
    public Iterator<N> iterator() {
        return values().iterator();
    }

    @Override
    public synchronized boolean setValue(final N object) {
        final int mOriginalID = this.mInternalID;
        put(object);
        if (this.mInternalMap.get(mOriginalID).equals(object)
                || mOriginalID > this.mInternalID) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized N put(final N object) {
        return set(object);
    }

    @Override
    public synchronized N set(final N object) {
        final int newID = getNextFreeMapID();
        mInternalMapReverseLookup.put(object, newID);
        return mInternalMap.put(newID, object);
    }

    public synchronized int putToInternalMap(final N object) {
        return setInternalMap(object);
    }

    public synchronized int setInternalMap(final N object) {
        final int newID = getNextFreeMapID();
        mInternalMap.put(newID, object);
        mInternalMapReverseLookup.put(object, newID);
        return newID;
    }

    public synchronized boolean injectCleanDataToAutoMap(final Integer g, final N object) {
        if (!mInternalMap.containsKey(g)
                && !mInternalMapReverseLookup.containsKey(object)) {
            int a1 = 0, a2 = 0, a11 = 0, a22 = 0;
            a1 = mInternalMap.size();
            a2 = mInternalMapReverseLookup.size();
            a11 = a1;
            a22 = a2;
            mInternalMap.put(g, object);
            a1 = mInternalMap.size();
            mInternalMapReverseLookup.put(object, g);
            a2 = mInternalMapReverseLookup.size();
            if (a1 > a11 && a2 > a22) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean injectDataToAutoMap(final Integer g, final N object) {
        int a1 = 0, a2 = 0, a11 = 0, a22 = 0;
        a1 = mInternalMap.size();
        a2 = mInternalMapReverseLookup.size();
        a11 = a1;
        a22 = a2;
        mInternalMap.put(g, object);
        a1 = mInternalMap.size();
        mInternalMapReverseLookup.put(object, g);
        a2 = mInternalMapReverseLookup.size();
        if (a1 > a11 && a2 > a22) {
            return true;
        }
        return false;
    }

    private boolean raiseInternalID() {
        final int mOld = mInternalID;
        mInternalID++;
        return mInternalID > mOld;
    }

    public synchronized int getNextFreeMapID() {
        if (raiseInternalID()) {
            return mInternalID;
        }
        return Short.MIN_VALUE;
    }

    @Override
    public synchronized N get(final int id) {
        return mInternalMap.get(id);
    }

    public synchronized int get(final N key) {
        return mInternalMapReverseLookup.get(key);
    }

    @Override
    public synchronized Collection<N> values() {
        return mInternalMap.values();
    }

    public synchronized Collection<Integer> keys() {
        return mInternalMapReverseLookup.values();
    }

    @Override
    public synchronized int size() {
        return mInternalMap.size();
    }

    @Override
    public synchronized int hashCode() {
        return mInternalMap.hashCode() + mInternalMapReverseLookup.hashCode();
    }

    @Override
    public synchronized boolean containsKey(final int key) {
        return mInternalMap.containsKey(key);
    }

    @Override
    public synchronized boolean containsValue(final N value) {
        return mInternalMap.containsValue(value);
    }

    public synchronized boolean containsKey(final N key) {
        return mInternalMapReverseLookup.containsKey(key);
    }

    public synchronized boolean containsValue(final int value) {
        return mInternalMapReverseLookup.containsValue(value);
    }

    @Override
    public synchronized boolean isEmpty() {
        return mInternalMap.isEmpty() && mInternalMapReverseLookup.isEmpty();
    }

    @Override
    public synchronized void clear() {
        this.mInternalID = 0;
        this.mInternalMap.clear();
        this.mInternalMapReverseLookup.clear();
        return;
    }

    @Override
    public synchronized N[] toArray() {
        final Collection<N> col = this.mInternalMap.values();
        @SuppressWarnings("unchecked")
        final
        N[] val = (N[]) col.toArray();
        return val;
    }

    public synchronized Integer[] toArrayInternalMap() {
        final Collection<Integer> col = this.mInternalMapReverseLookup.values();
        final Integer[] val = col.toArray(new Integer[col.size()]);
        return val;
    }

}
