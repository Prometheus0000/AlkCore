package net.alkalus.core.util.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

}

