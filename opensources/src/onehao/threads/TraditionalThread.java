package onehao.threads;

public class TraditionalThread {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Thread thread = new Thread(){
			@Override
			public void run(){
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("A - " + Thread.currentThread().getName());
					System.out.println("B - " + this.getName());
				}
			}
		};
		
		thread.start();
		
		Thread thread2 = new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("C - " + Thread.currentThread().getName());
				}
				
			}
			
		});
		thread2.start();
		
		new Thread(new Runnable(){
				@Override
				public void run() {
					while(true){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("E - " + Thread.currentThread().getName());
					}

		}
		}){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("D - " + Thread.currentThread().getName());
				}
		}

		}.start();
	}

}
