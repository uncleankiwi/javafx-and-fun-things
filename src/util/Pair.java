package util;

import java.util.Objects;

public class Pair<T> {
	public T x;
	public T y;

	public Pair(T x, T y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + ',' + y + ')';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Pair<?> pair = (Pair<?>) o;
		return Objects.equals(x, pair.x) && Objects.equals(y, pair.y);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}
