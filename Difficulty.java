import java.util.Random;

public class Difficulty {

	public boolean[][] masks =
	      {{false, false, false, false, false, false, false, false, false},
	       {false, false, false, false, false, false, false, false, false},
	       {false, false, false, false, false, false, false, false, false},
	       {false, false, false, false, false, false, false, false, false},
	       {false, false, false, false, false, false, false, false, false},
	       {false, false, false, false, false, false, false, false, false},
	       {false, false, false, false, false, false, false, false, false},
	       {false, false, false, false, false, false, false, false, false},
	       {false, false, false, false, false, false, false, false, false}};
	
	public Difficulty() {}
	
	public boolean[][] Easy() {
		Random rand = new Random();
		int[] x = new int[5];
		int[] y = new int[5];
		
		for (int i=0; i<5; ++i) {
			x[i] = rand.nextInt(9); // randomise row and store 0-8
			y[i] = rand.nextInt(9); // randomise column and store 0-8
		}
		// changing the mask from false to true
		for (int i=0; i<5; ++i) {
			masks[x[i]][y[i]] = true;
		}
		return masks;
	}
	
	public boolean[][] Medium() {
		Random rand = new Random();
		int[] x = new int[20];
		int[] y = new int[20];
		
		for (int i=0; i<20; ++i) {
			x[i] = rand.nextInt(9);
			y[i] = rand.nextInt(9);
		}
		// changing the mask from false to true
		for (int i=0; i<20; ++i) {
			masks[x[i]][y[i]] = true;
		}
		return masks;
	}
	
	public boolean[][] Hard() {
		Random rand = new Random();
		int[] x = new int[30];
		int[] y = new int[30];
		
		for (int i=0; i<30; ++i) {
			x[i] = rand.nextInt(9);
			y[i] = rand.nextInt(9);
		}
		// changing the mask from false to true
		for (int i=0; i<30; ++i) {
			masks[x[i]][y[i]] = true;
		}
		return masks;
	}
	
}
