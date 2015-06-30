package onehao.threads;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/***
 * more please refer to the JAVA quartz lib.
 * @author Michael Wan
 *
 */
public class TraditionalTimerTest {
	
	private static int count = 0;
	public static void main(String[] args) {
		
//		new Timer().schedule(new TimerTask(){
//			@Override
//			public void run() {
//				System.out.println("Invoking!!!!");
//			}
//		}, 10000, 3000);
		class MyTimerTask extends TimerTask{
			@Override
			public void run() {
				count = (count + 1) % 2;
				System.out.println("Invoking~~~~~~~~");
				new Timer().schedule(new MyTimerTask(), 2000 + 2000*count);
			}
		}
		new Timer().schedule(new MyTimerTask(), 2000);
		
		while(true){
			System.out.println(new Date().getSeconds());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	}
	
}

