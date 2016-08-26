package vazkii.rem;

public class Main {

	public static void main(String[] args) {
		try {
			Rem rem = new Rem();
			rem.connect();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
