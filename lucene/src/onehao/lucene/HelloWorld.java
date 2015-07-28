package onehao.lucene;

import java.io.IOException;

import onehao.lucene.utils.FileToDocument;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class HelloWorld {
	String filePath = "E:\\Workspace\\lucene\\luceneDatasource\\IndexWriter addDocument's a javadoc .txt";
	String indexPath = "E:\\Workspace\\lucene\\lucenceIndex";
	Analyzer analyzer = new StandardAnalyzer();
	
	/**
	 * create index. 
	 * IndexWriter (add, update, delete index lib)
	 * @throws IOException 
	 */
	@Test
	public void createIndex() throws IOException{
		// file --> document.
		Document doc = FileToDocument.file2Document(filePath);
		
		Directory dir = FSDirectory.ge
		IndexWriter indexWriter = new IndexWriter(, conf);
		indexWriter.addDocument(doc);
		
		indexWriter.close();
	}
	
	/**
	 * search
	 */
	@Test
	public void search(){
		String queryString = "document";
	}
}
