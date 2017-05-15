package de.adrodoc55;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Utils {

	public static void removeAllOnce(Collection<?> from, Collection<?> remove) {
		for (Object e : remove) {
			from.remove(e);
		}
	}

	@SafeVarargs
	public static <E> ArrayList<E> newArrayList(E... elements) {
		ArrayList<E> arrayList;
		if (elements == null) {
			arrayList = new ArrayList<E>(1);
			arrayList.add(null);
		} else {
			arrayList = new ArrayList<E>(elements.length);
			for (E e : elements) {
				arrayList.add(e);
			}
		}
		return arrayList;
	}

	public static <E> ArrayList<E> newArrayList(Iterable<E> elements) {
		ArrayList<E> arrayList = new ArrayList<E>();
		for (E e : elements) {
			arrayList.add(e);
		}
		return arrayList;
	}

	@SafeVarargs
	public static <E> List<E> commonElementsOf(List<E>... lists) {
		List<E> commonElements = newArrayList(lists[0]);
		for (int x = 1; x < lists.length; x++) {
			List<E> list = lists[x];
			ArrayList<E> listCopy = newArrayList(list);
			Iterator<E> it = commonElements.iterator();
			while (it.hasNext()) {
				E e = it.next();
				if (listCopy.contains(e)) {
					listCopy.remove(e);
				} else {
					it.remove();
//					commonElements.remove(e);
				}
			}
		}
		return commonElements;
	}

}
