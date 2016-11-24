package main;


public class Main {
	public static void main(String args[]) {
		try {
			new Game().start();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
