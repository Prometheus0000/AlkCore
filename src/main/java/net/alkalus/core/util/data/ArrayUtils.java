package net.alkalus.core.util.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.alkalus.api.objects.data.AutoMap;
import net.alkalus.api.objects.misc.AcLog;

public class ArrayUtils {

	public static <V> V[] expandArray(final V[] someArray, final V newValueToAdd) {
		V[] series = someArray;
		series = addElement(series, newValueToAdd);
		return series;
	}

	private static <V> V[] addElement(V[] series, final V newValueToAdd) {
		series  = Arrays.copyOf(series, series.length + 1);
		series[series.length - 1] = newValueToAdd;
		return series;
	}
	
	/*public static <V> Object getMostCommonElement(List<V> list) {
		Optional r = list.stream().map(V::getTextureSet).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
		return r.get();
	}*/

	@SuppressWarnings("unchecked")
	public static <V> V[] removeNulls(final V[] v) {
		List<V> list = new ArrayList<V>(Arrays.asList(v));
		list.removeAll(Collections.singleton(null));
		return (V[]) list.toArray();
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<T> combineSetData(Set<T> S, Set<T> J) {
		Set<T> mData = new HashSet<T>();
		T[] array1 = (T[]) S.toArray();		
		Collections.addAll(mData, array1);
		T[] array2 = (T[]) J.toArray();		
		Collections.addAll(mData, array2);		
		return mData;
	}

	public static <A> AutoMap<A> mergeTwoMaps(AutoMap<A> a, AutoMap<A> b) {
		AutoMap<A> c = new AutoMap<A>();
		for (A g : a.values()) {
			c.put(g);
		}
		for (A g : b.values()) {
			c.put(g);
		}		
		return c;
	}
	
	public static <T> T[][] rotateArrayClockwise(T[][] mat) {
		AcLog.INFO("Rotating Array 90' Clockwise");
		try {
	    final int M = mat.length;
	    final int N = mat[0].length;
		AcLog.INFO("Dimension X: "+M);
		AcLog.INFO("Dimension Z: "+N);
	    @SuppressWarnings("unchecked")
		T[][] ret = (T[][]) new Object[N][M];
	    for (int r = 0; r < M; r++) {
	        for (int c = 0; c < N; c++) {
	            ret[c][M-1-r] = mat[r][c];
	        }
	    }	   
		AcLog.INFO("Returning Rotated Array"); 
	    return ret;
		}
		catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Java method to sort Map in Java by value e.g. HashMap or Hashtable
	 * throw NullPointerException if Map contains null values
	 * It also sort values even if they are duplicates
	 * https://stackoverflow.com/a/18003532
	 * Read more: http://javarevisited.blogspot.com/2012/12/how-to-sort-hashmap-java-by-key-and-value.html#ixzz2akXStsGj
	 */
	public static <K extends Comparable,V extends Comparable> Map<K, V> sortMapByValues(Map<K,V> map){
	    List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());

	    Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

	        @Override
	        public int compare(Entry<K, V> o1, Entry<K, V> o2) {
	            return o1.getValue().compareTo(o2.getValue());
	            // to compare alphabetically case insensitive return this instead
	            // o1.getValue().toString().compareToIgnoreCase(o2.getValue().toString()); 
	        }
	    });

	    //LinkedHashMap will keep the keys in the order they are inserted
	    //which is currently sorted on natural ordering
	    Map<K,V> sortedMap = new LinkedHashMap<K,V>();

	    for(Map.Entry<K,V> entry: entries){
	        sortedMap.put(entry.getKey(), entry.getValue());
	    }
	    return sortedMap;
	}
	
	/**
	 * Clones a Map, does not ensure that the objects within the map are cloneable.
	 * @param <A> - Map Key Type
	 * @param <B> - Map Data Type
	 * @param aMap - The Map to clone, try to avoid using maps containing unique objects.
	 */
	public static <A, B> Map<A, B> cloneMap(Map<A, B> aMap) {
		Set<Entry<A, B>> aKeys = aMap.entrySet();		
		LinkedHashMap<A, B> aNewMap = new LinkedHashMap<A, B>();
		for (Entry<A, B> data : aKeys) {
			aNewMap.put(data.getKey(), data.getValue());
		}
		return aNewMap;		
	}
	
	public static String toString(Object[] array) {
		return org.apache.commons.lang3.ArrayUtils.toString(array);
	}

}

