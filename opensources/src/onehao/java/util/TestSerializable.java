package onehao.java.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TestSerializable implements Serializable {
	private transient int size = 10;
	private transient int[] head = { 1, 2, 3, 4, 5, 6 };

	private transient String test = "abc";
	private String test2 = "abc";

	// Other code

	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		s.writeInt(size);
		// Write out all elements in the proper order.
		// for (int e : head)
		// s.writeObject(e);
		s.writeObject(head);
		s.writeObject(test);
	}

	public static void main(String[] args) throws Exception {
		TestSerializable ts = new TestSerializable();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(stream);

		out.writeObject(ts);

		out.close();

		InputStream instream = new ByteArrayInputStream(stream.toByteArray());
		ObjectInputStream in = new ObjectInputStream(instream);

		TestSerializable ts2 = (TestSerializable) in.readObject();

		testExternalizable();

		System.out.println();
	}

	private static void testExternalizable() throws Exception {
		TestExternalizable ts = new TestExternalizable();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(stream);

		out.writeObject(ts);

		out.close();

		InputStream instream = new ByteArrayInputStream(stream.toByteArray());
		ObjectInputStream in = new ObjectInputStream(instream);

		TestExternalizable ts2 = (TestExternalizable) in.readObject();

		System.out.println();
	}
}

class TestExternalizable implements Externalizable {
	private transient int size = 10;
	private transient int[] head = { 1, 2, 3, 4, 5, 6 };

	private transient String test = "abc";
	private String test2 = "abc";

	public TestExternalizable() {
	};

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeInt(size);
		// Write out all elements in the proper order.
		// for (int e : head)
		// s.writeObject(e);
		out.writeObject(head);
		out.writeObject(test);
		out.writeObject(test2);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub

		size = in.readInt();
		head = (int[]) in.readObject();
		test = (String) in.readObject();

		test2 = (String) in.readObject();
	}

}
