package tst;

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
}
