import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Random;

public class TernarySearchTrie {
	/**
	 * An inner class of Ternary Search Trie that represents a node in the trie.
	 */
	public static class TSTNode {
		/** The key to the node. */
		public int data = 0;
		public int weight = 0;
		/** The relative nodes. */
		public TSTNode LOKID;
		public TSTNode EQKID;
		public TSTNode HIKID;
		public TSTNode PARENT;
		/** The char used in the split. */
		public char splitchar;

		/**
		 * Constructor method.
		 *
		 * @param splitchar
		 *            The char used in the split.
		 * @param parent
		 *            The parent node.
		 */
		public TSTNode(char splitchar, TSTNode p) {
			this.splitchar = splitchar;
			PARENT = p;
		}
	}

	public static class TSTItem implements Comparable {
		/** The key to the node. */
		public int data = 0;
		public int weight = 0;
		/** The char used in the split. */
		public String key;

		/**
		 * Constructor method.
		 *
		 * @param splitchar
		 *            The char used in the split.
		 * @param parent
		 *            The parent node.
		 */
		public TSTItem(String k, int d, int w) {
			this.key = k;
			this.data = d;
			this.weight = w;
		}

		public int compareTo(Object obj) {
			TSTItem that = (TSTItem) obj;
			return (that.weight - weight);
		}
	}

	/** The base node in the trie. */
	private TSTNode root;

	public static String getDir() {
		String dir = System.getProperty("dic.dir");
		if (dir == null)
			dir = "/dic/";
		else if (!dir.endsWith("/"))
			dir += "/";
		return dir;
	}

	private static TernarySearchTrie dicSug = new TernarySearchTrie();

	/**
	 *
	 * @return the singleton of the dictionary
	 */
	public static TernarySearchTrie getInstance() {
		return dicSug;
	}

	private TernarySearchTrie() {
		this("suggestDic.txt");
	}

	/**
	 * Constructs a Ternary Search Trie and loads data from a <code>File</code>
	 * into the Trie. The file is a normal text document, where each line is of
	 * the form word : integer.
	 *
	 * @param file
	 *            The <code>File</code> with the data to load into the Trie.
	 * @exception IOException
	 *                A problem occured while reading the data.
	 */
	public TernarySearchTrie(String dic) {
		try {
			InputStream file = null;
			if (System.getProperty("dic.dir") == null)
				file = getClass().getResourceAsStream(
						TernarySearchTrie.getDir() + dic);
			else
				file = new FileInputStream(new File(TernarySearchTrie.getDir()
						+ dic));
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(file, "GBK"));
			String word;
			int occur = 0;
			int weight = 0;
			while ((word = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(word, "%");
				String key = st.nextToken();
				occur = Integer.parseInt(st.nextToken());
				weight = Integer.parseInt(st.nextToken());
				if (root == null) {
					root = new TSTNode(key.charAt(0), null);
				}
				TSTNode node = null;
				if (key.length() > 0 && root != null) {
					TSTNode currentNode = root;
					int charIndex = 0;
					while (true) {
						if (currentNode == null)
							break;
						int charComp = (key.charAt(charIndex) - currentNode.splitchar);
						if (charComp == 0) {
							charIndex++;
							if (charIndex == key.length()) {
								node = currentNode;
								break;
							}
							currentNode = currentNode.EQKID;
						} else if (charComp < 0) {
							currentNode = currentNode.LOKID;
						} else {
							currentNode = currentNode.HIKID;
						}
					}
					int occur2 = 0;
					if (node != null) {
						occur2 = node.data;
					}
					if (occur2 != 0) {
						occur += occur2;
					}
					currentNode = getOrCreateNode(key);
					occur2 = currentNode.data;
					if (occur2 != 0) {
						// System.out.println("add");
						occur += occur2;
					}
					currentNode.data = occur;
					currentNode.weight = weight;
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the node indexed by key, creating that node if it doesn't exist,
	 * and creating any required intermediate nodes if they don't exist.
	 *
	 * @param key
	 *            A <code>String</code> that indexes the node that is returned.
	 * @return The node object indexed by key. This object is an instance of an
	 *         inner class named <code>TernarySearchTrie.TSTNode</code>.
	 * @exception NullPointerException
	 *                If the key is <code>null</code>.
	 * @exception IllegalArgumentException
	 *                If the key is an empty <code>String</code>.
	 */
	protected TSTNode getOrCreateNode(String key) throws NullPointerException,
			IllegalArgumentException {
		if (key == null) {
			throw new NullPointerException(
					"attempt to get or create node with null key");
		}
		if ("".equals(key)) {
			throw new IllegalArgumentException(
					"attempt to get or create node with key of zero length");
		}
		if (root == null) {
			root = new TSTNode(key.charAt(0), null);
		}
		TSTNode currentNode = root;
		int charIndex = 0;
		while (true) {
			int charComp = (key.charAt(charIndex) - currentNode.splitchar);
			if (charComp == 0) {
				charIndex++;
				if (charIndex == key.length()) {
					return currentNode;
				}
				if (currentNode.EQKID == null) {
					currentNode.EQKID = new TSTNode(key.charAt(charIndex),
							currentNode);
				}
				currentNode = currentNode.EQKID;
			} else if (charComp < 0) {
				if (currentNode.LOKID == null) {
					currentNode.LOKID = new TSTNode(key.charAt(charIndex),
							currentNode);
				}
				currentNode = currentNode.LOKID;
			} else {
				if (currentNode.HIKID == null) {
					currentNode.HIKID = new TSTNode(key.charAt(charIndex),
							currentNode);
				}
				currentNode = currentNode.HIKID;
			}
		}
	}

	/**
	 * Returns the node indexed by key, or <code>null</code> if that node
	 * doesn't exist. The search begins at root node.
	 *
	 * @param key2
	 *            A <code>String</code> that indexes the node that is returned.
	 * @param startNode
	 *            The top node defining the subtrie to be searched.
	 * @return The node object indexed by key. This object is an instance of an
	 *         inner class named <code>TernarySearchTrie.TSTNode</code>.
	 */
	protected TSTNode getNode(String key, TSTNode startNode) {
		if (key == null || startNode == null || key.length() == 0) {
			return null;
		}
		TSTNode currentNode = startNode;
		int charIndex = 0;
		while (true) {
			if (currentNode == null) {
				return null;
			}
			int charComp = key.charAt(charIndex) - currentNode.splitchar;
			if (charComp == 0) {
				charIndex++;
				if (charIndex == key.length()) {
					return currentNode;
				}
				currentNode = currentNode.EQKID;
			} else if (charComp < 0) {
				currentNode = currentNode.LOKID;
			} else {
				currentNode = currentNode.HIKID;
			}
		}
	}

	/**
	 * Returns the node indexed by key, or <code>null</code> if that node
	 * doesn't exist. Search begins at root node.
	 *
	 * @param key
	 *            A <code>String</code> that indexes the node that is returned.
	 * @return The node object indexed by key. This object is an instance of an
	 *         inner class named <code>TernarySearchTrie.TSTNode</code>.
	 */
	public TSTNode getNode(String key) {
		return getNode(key, root);
	}

	/**
	 * Returns the key that indexes the node argument.
	 *
	 * @param node
	 *            The node whose index is to be calculated.
	 * @return The <code>String</code> that indexes the node argument.
	 */
	protected String getKey(TSTNode node) {
		StringBuffer getKeyBuffer = new StringBuffer();
		getKeyBuffer.setLength(0);
		getKeyBuffer.append(node.splitchar);
		TSTNode currentNode;
		TSTNode lastNode;
		currentNode = node.PARENT;
		lastNode = node;
		while (currentNode != null) {
			if (currentNode.EQKID == lastNode) {
				getKeyBuffer.append(currentNode.splitchar);
			}
			lastNode = currentNode;
			currentNode = currentNode.PARENT;
		}
		getKeyBuffer.reverse();
		return getKeyBuffer.toString();
	}

	/**
	 * Returns an <code>List</code> of all keys in the trie that begin with a
	 * given prefix. Only keys for nodes having non-null data are included in
	 * the <code>List</code>.
	 *
	 * @param prefix
	 *            Each key returned from this method will begin with the
	 *            characters in prefix.
	 * @param numReturnValues
	 *            The maximum number of values returned from this method.
	 * @return A <code>List</code> with the results
	 */
	public TSTItem[] matchPrefix(String prefix, int numReturnValues) {
		TSTNode startNode = getNode(prefix);
		if (startNode == null) {
			return null;
		}
		ArrayList<TSTItem> sortKeysResult = new ArrayList<TSTItem>();
		ArrayList<TSTItem> wordTable = sortKeysRecursion(startNode.EQKID,
				((numReturnValues < 0) ? -1 : numReturnValues), sortKeysResult);
		int retNum = Math.min(numReturnValues, wordTable.size());
//		Select.selectRandom(wordTable, wordTable.size(), retNum, 0);

		TSTItem[] fullResults = new TSTItem[retNum];
		for (int i = 0; i < retNum; ++i) {
			fullResults[i] = wordTable.get(i);
		}
		return fullResults;
	}

	/**
	 * Returns keys sorted in alphabetical order. This includes the current Node
	 * and all nodes connected to the current Node.
	 * <p>
	 * Sorted keys will be appended to the end of the resulting
	 * <code>List</code>. The result may be empty when this method is invoked,
	 * but may not be <code>null</code>.
	 *
	 * @param currentNode
	 *            The current node.
	 * @param sortKeysNumReturnValues
	 *            The maximum number of values in the result.
	 * @param sortKeysResult2
	 *            The results so far.
	 * @return A <code>List</code> with the results.
	 */
	private ArrayList<TSTItem> sortKeysRecursion(TSTNode currentNode,
			int sortKeysNumReturnValues, ArrayList<TSTItem> sortKeysResult2) {
		if (currentNode == null) {
			return sortKeysResult2;
		}

		ArrayList<TSTItem> sortKeysResult = sortKeysRecursion(
				currentNode.LOKID, sortKeysNumReturnValues, sortKeysResult2);
		if (currentNode.data != 0) {
			sortKeysResult.add(new TSTItem(getKey(currentNode),
					currentNode.data, currentNode.weight));
		}
		sortKeysResult = sortKeysRecursion(currentNode.EQKID,
				sortKeysNumReturnValues, sortKeysResult);
		return sortKeysRecursion(currentNode.HIKID, sortKeysNumReturnValues,
				sortKeysResult);
	}

	public static void main(String[] args) {
		TernarySearchTrie sugDic = TernarySearchTrie.getInstance();
		String prefix = "m";
		TSTItem[] ret = sugDic.matchPrefix(prefix, 10);
		for (TSTItem i : ret) {
			System.out.println(i.key + ":" + i.data + ":" + i.weight);
		}
		
		System.console().readLine();
	}
}