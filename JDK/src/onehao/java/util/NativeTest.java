package onehao.java.util;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Scanner;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT.HANDLE;

//native reference: http://msdn.microsoft.com/en-us/library/windows/desktop/aa363858(v=vs.85).aspx

//API doc : http://twall.github.io/jna/4.1.0/

//reference: http://stackoverflow.com/questions/7399862/calling-createfile-using-jna-gives-unsatisfiedlinkerror-error-looking-up-functi

/**
 * 
 * @author wanhao01
 *
 */
public class NativeTest {

	static{

		   System.loadLibrary("kernel32");
//		    System.loadLibrary("vtkFilteringJava");
//		    System.loadLibrary("vtkIOJava");
//		    System.loadLibrary("vtkImagingJava");
//		    System.loadLibrary("vtkGraphicsJava");
//		    System.loadLibrary("vtkRenderingJava");
//
		}
	static int GENERIC_ACCESS = 268435456;
    static int EXCLUSIVE_ACCESS = 0;
    static int OPEN_EXISTING = 3;
	
	public static native boolean createFileExclusively(String path)
            throws IOException;
	
	
	public static native void CreateFile(String lpFileName, int dwDesiredAccess,int dwShareMode, Object lpSecurityAttributes,
			  int dwCreationDisposition,
			  int dwFlagsAndAttributes,
			  Object hTemplateFile);
	
	public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary)
            Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"),
                               CLibrary.class);
   
        void printf(String format, Object... args);
    }
	
	class ConcreteClass implements IConcreteClass
	{
		public void say()
		{
			System.out.println("this is from Concrete class");
		}
	}
	
	class MyProxy implements InvocationHandler
	{
		IConcreteClass concreteObject;
		
		MyProxy(IConcreteClass concreteObject)
		{
			this.concreteObject = concreteObject;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			// TODO Auto-generated method stub
			
			Object result = method.invoke(concreteObject, args);
			System.out.println("this is from proxy");
			return result;
		}
		
	}
	
	
	private void proxyInvocation()
	{
		MyProxy handler = new MyProxy(new ConcreteClass());
		
		IConcreteClass proxy = (IConcreteClass)Proxy.newProxyInstance(ConcreteClass.class.getClassLoader(), ConcreteClass.class.getInterfaces(), handler);
		proxy.say();
	}
	
	public static void main(String[] args) throws IOException {
		new NativeTest().proxyInvocation();
		
		Scanner in = new Scanner(System.in);
//		Kernel32 kernel32 = 
//	            (Kernel32) Native.loadLibrary("kernel32", Kernel32.class,W32APIOptions.UNICODE_OPTIONS);
		Kernel32 kernel32 = Kernel32.INSTANCE;
		HANDLE handle = kernel32.CreateFile("c:/test.txt",GENERIC_ACCESS, EXCLUSIVE_ACCESS,
	            null, 2, 32, null);
		
		 CLibrary.INSTANCE.printf("Hello, World/n");
		//CreateFile("test.txt", 7, 0, null, 2, 32, null);
		
		//X11 x11 = X11.INSTANCE;
		
		System.in.read();
	}
}
