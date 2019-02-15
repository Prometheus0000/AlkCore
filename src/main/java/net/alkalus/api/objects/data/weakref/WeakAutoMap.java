package net.alkalus.api.objects.data.weakref;

import java.util.WeakHashMap;

import net.alkalus.api.objects.data.AutoMap;

public class WeakAutoMap<T> extends AutoMap<T> {
	private static final long serialVersionUID = 8328345351801363386L;
	public WeakAutoMap() {
		super(new WeakHashMap<Integer, T>());
	}	
}
