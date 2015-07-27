package onehao.lucene;

import java.io.IOException;
import java.util.Collection;

import onehao.lucene.utils.FileToDocument;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.Lock;
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
		
		IndexWriter indexWriter = new IndexWriter(new Directory() {
			
			@Override
			public void sync(Collection<String> names) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void renameFile(String source, String dest) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public IndexInput openInput(String name, IOContext context)
					throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Lock makeLock(String name) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String[] listAll() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public long fileLength(String name) throws IOException {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public void deleteFile(String name) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public IndexOutput createOutput(String name, IOContext context)
					throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void close() throws IOException {
				// TODO Auto-generated method stub
				
			}
		}, conf);
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
