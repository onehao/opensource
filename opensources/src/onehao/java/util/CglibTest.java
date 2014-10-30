package onehao.java.util;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibTest {
	
	public static void main(String[] args) {
		BaseProxyCglib cglib = new BaseProxyCglib();
		BaseImpl proxy = (BaseImpl)cglib.getInstance(new BaseImpl());
		
		proxy.baseEcho();
		proxy.baseImplEcho();
	}
}

class MyBase
{
	protected void baseEcho()
	{
		System.out.println("this is from base class.");
	}
}

class BaseImpl extends MyBase
{
	public void baseImplEcho()
	{
		baseEcho();
		System.out.println("this is from the Base Impl.");
	}
}

class BaseProxyCglib implements MethodInterceptor {  
    private Object target;  
  
    /** 
     * 创建代理对象 
     *  
     * @param target 
     * @return 
     */  
    public Object getInstance(Object target) {  
        this.target = target;  
        Enhancer enhancer = new Enhancer();  
        enhancer.setSuperclass(this.target.getClass());  
        // 回调方法  
        enhancer.setCallback(this);  
        // 创建代理对象  
        return enhancer.create();  
    }  
  
    @Override  
    // 回调方法  
    public Object intercept(Object obj, Method method, Object[] args,  
            MethodProxy proxy) throws Throwable {  
        System.out.println("事物开始");  
        proxy.invokeSuper(obj, args);  
        System.out.println("事物结束");  
        return null;  
  
    }
}
