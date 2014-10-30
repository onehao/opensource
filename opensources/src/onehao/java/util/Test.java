package onehao.java.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Test implements Serializable {


	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, Exception {


		ArrayList<String> list = new ArrayList<String>();

		// add 10 elements.
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");
		list.add("e");
		list.add("f");
		list.add("g");
		list.add("h");
		list.add("i");
		list.add("j");

		ArrayList<String> list2 = new ArrayList<String>();

		{
			// add 10 elements.
			list2.add("1");
			list2.add("2");
			list2.add("3");
			list2.add("4");
			list2.add("5");
			list2.add("6");
			list2.add("7");
			list2.add("8");
			list2.add("9");
			list2.add("10");
		}

		list.addAll(3, list2);

		list.removeAll(list2);

		// copyOf
		String[] a = new String[10];
		String[] temp = list.toArray(a);
		temp[3] = null;

		String[] b = new String[9];
		temp = list.toArray(b);

		String[] c = new String[13];
		temp = list.toArray(c);

		temp = list.toArray(new String[1]);
		//temp = list.toArray(null);

		// resize
		((ArrayList<String>) list).trimToSize();

		String[] strings = {};

		//strings[10] = "a";

		List<String> list3 = new ArrayList<String>();
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(stream);
		
		out.writeObject(list);
		
		
		out.close();
		
		
		
		InputStream instream = new ByteArrayInputStream(stream.toByteArray());
		ObjectInputStream in = new ObjectInputStream(instream);
		
		list3 = (ArrayList<String>)in.readObject();

	}

	Object[] elementData = new Object[3];

//	@SuppressWarnings("unchecked")
//	E elementData(int index) {
//		return (E) elementData[index];
//	}
	
	public static native void arraycopy(Object src,  int  srcPos,
            Object dest, int destPos,
            int length);
	
	public static native boolean createFileExclusively(String path)
            throws IOException;
	
	
	public static native void CreateFile(String lpFileName, int dwDesiredAccess,int dwShareMode, Object lpSecurityAttributes,
			  int dwCreationDisposition,
			  int dwFlagsAndAttributes,
			  Object hTemplateFile);
}