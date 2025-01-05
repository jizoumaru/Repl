package tst;

import java.util.Objects;

public final class KeyValue<K, V> {
	public static <K, V> KeyValue<K, V> of(K key, V value) {
		return new KeyValue<K, V>(key, value);
	}

	public final K key;
	public final V value;

	public KeyValue(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		return "(" + key + ", " + value + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		@SuppressWarnings("unchecked")
		var other = (KeyValue<K, V>) obj;
		return Objects.equals(key, other.key) && Objects.equals(value, other.value);
	}

}
