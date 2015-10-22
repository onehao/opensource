package onehao.java.weakreference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class WeakReferenceStack<E> {
	private final List<WeakReference<E>> stackReferences;
	private int stackPointer = 0;

	public WeakReferenceStack() {
		this.stackReferences = new ArrayList<>();
	}

	public void push(E element) {
		this.stackReferences.add(stackPointer, new WeakReference<>(element));
		stackPointer++;
	}

	public E pop() {
		stackPointer--;
		return this.stackReferences.get(stackPointer).get();
	}

	public E peek() {
		return this.stackReferences.get(stackPointer - 1).get();
	}
	
	public static void main(String[] args) {
		final WeakReferenceStack<ValueContainer> stack = new WeakReferenceStack<>();
		final ValueContainer expected = new ValueContainer(
				"Value for the stack");
		stack.push(new ValueContainer("Value for the stack"));
		ValueContainer peekedValue = stack.peek();
		assertEquals(expected, peekedValue);
		assertEquals(expected, stack.peek());
		peekedValue = null;
		System.gc();
		assertNull(stack.peek());
	}

	public static class ValueContainer {
		private final String value;

		public ValueContainer(final String value) {
			this.value = value;
		}

		@Override
		protected void finalize() throws Throwable {
			super.finalize();
			System.out.printf("Finalizing for [%s]%n", toString());
		}
		/* equals, hashCode and toString omitted */
		
		@Override
		public boolean equals(Object obj) {
			// TODO Auto-generated method stub
			return value.equals(((ValueContainer)obj).value);
		}
		
		@Override
		public int hashCode() {
			return value.hashCode();
		}
	}
}
