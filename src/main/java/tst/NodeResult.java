package tst;

public final class NodeResult<T> {
	public final boolean b;
	public final Node<T> n;

	public NodeResult(boolean b, Node<T> n) {
		this.b = b;
		this.n = n;
	}
}
