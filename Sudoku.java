import java.awt.*;        // Uses AWT's Layout Managers
import java.awt.event.*;  // Uses AWT's Event Handlers
import java.util.Random;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;     // Uses Swing's Container/Components
 
/**
 * The Sudoku game.
 * To solve the number puzzle, each row, each column, and each of the
 * nine 3×3 sub-grids shall contain all of the digits from 1 to 9
 */
public class Sudoku extends JFrame {
   // Name-constants for the game properties
   public static final int GRID_SIZE = 9;    // Size of the board
   public static final int SUBGRID_SIZE = 3; // Size of the sub-grid
 
   // Name-constants for UI control (sizes, colors and fonts)
   public static final int CELL_SIZE = 60;   // Cell width/height in pixels
   public static final int CANVAS_WIDTH  = CELL_SIZE * GRID_SIZE;
   public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
                                             // Board width/height in pixels
   public static final Color OPEN_CELL_BGCOLOR = Color.YELLOW;
   public static final Color OPEN_CELL_TEXT_YES = new Color(0, 255, 0);  // RGB
   public static final Color OPEN_CELL_TEXT_NO = Color.RED;
   public static final Color CLOSED_CELL_BGCOLOR = new Color(240, 240, 240); // RGB
   public static final Color CLOSED_CELL_TEXT = Color.BLACK;
   public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);
 
   private String fileGameOver = "applause_y.wav";
   private Clip soundClipGameOver;
   private String fileSong = "universe.wav";
   private Clip soundSong;
   
   // The game board composes of 9x9 JTextFields,
   // each containing String "1" to "9", or empty String
   private JTextField[][] tfCells = new JTextField[GRID_SIZE][GRID_SIZE];
 
   // Puzzle to be solved and the mask (which can be used to control the
   //  difficulty level).
   // Hardcoded here. Extra credit for automatic puzzle generation
   //  with various difficulty levels.
   private int[][] puzzle =
      {{5, 3, 4, 6, 7, 8, 9, 1, 2},
       {6, 7, 2, 1, 9, 5, 3, 4, 8},
       {1, 9, 8, 3, 4, 2, 5, 6, 7},
       {8, 5, 9, 7, 6, 1, 4, 2, 3},
       {4, 2, 6, 8, 5, 3, 7, 9, 1},
       {7, 1, 3, 9, 2, 4, 8, 5, 6},
       {9, 6, 1, 5, 3, 7, 2, 8, 4},
       {2, 8, 7, 4, 1, 9, 6, 3, 5},
       {3, 4, 5, 2, 8, 6, 1, 7, 9}};
   // For testing, open only 2 cells.
   private boolean[][] masks =
      {{false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false}};
   /**
    * Constructor to setup the game and the UI Components
    */
   
   private void createMenuBar() {
	   //create a panel for menubar
	   JPanel menuPanel = new JPanel();
	   menuPanel.setLayout(new FlowLayout());
	   
       //Create the menu bar.
       JMenuBar menuBar = new JMenuBar();

       //Build the first menu.
       JMenu menu = new JMenu("New Game");
       menu.getAccessibleContext().setAccessibleDescription(
               "The only menu in this program that has menu items");
       menuBar.add(menu);
       
       JMenuItem restart = new JMenuItem("Restart", KeyEvent.VK_Z);
       menu.add(restart); //
       restart.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
         int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to restart the game? Game will not be saved.", "Confirm Dialog", JOptionPane.YES_NO_CANCEL_OPTION);
         if (answer == JOptionPane.YES_OPTION) {
          Sudoku.this.dispose();
          new Sudoku();
         }
        }
       });
       
       JMenuItem easy = new JMenuItem("Easy");
       menu.add(easy);
       easy.addActionListener(new ActionListener() {
    	   @Override
    	   public void actionPerformed(ActionEvent e) {
    		  RandomPuzzle();
    		  masks =randomEasy();
    		  ResetSudoku();
    	   }
       });
       
       JMenuItem medium = new JMenuItem("Medium");
       menu.add(medium);
       medium.addActionListener(new ActionListener() {
    	   @Override
    	   public void actionPerformed(ActionEvent e) {
    		  RandomPuzzle();
    		  masks =randomMedium();
    		  ResetSudoku();
    	   }
       });
       
       JMenuItem hard = new JMenuItem("Hard");
       //menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
       menu.add(hard);
       hard.addActionListener(new ActionListener() {
    	   @Override
    	   public void actionPerformed(ActionEvent e) {
    		  RandomPuzzle();
    		  masks = randomHard();
    		  ResetSudoku();
    	   }
       });
       
       menu = new JMenu("Options");
       JMenuItem mute = new JMenuItem("Mute");
       menu.add(mute);
       menu.setMnemonic(KeyEvent.VK_Z);
       menu.getAccessibleContext().setAccessibleDescription(
       			"This menu display the options");
       menuBar.add(menu);
       mute.addActionListener(new ActionListener() {
       		@Override
       		public void actionPerformed(ActionEvent e) {
       			soundSong.stop();	
       		}
       		
       });
       
       JMenuItem unmute = new JMenuItem("Unmute");
       menu.add(unmute);
       menu.setMnemonic(KeyEvent.VK_Z);
       menu.getAccessibleContext().setAccessibleDescription(
       			"This menu display the options");
       menuBar.add(menu);
       unmute.addActionListener(new ActionListener() {
       		@Override
       		public void actionPerformed(ActionEvent e) {
       			soundSong.start();
       			
       		}
       });
       
       menu = new JMenu("Help");
       menu.setMnemonic(KeyEvent.VK_O);
       menu.getAccessibleContext().setAccessibleDescription(
               "This menu show how to play the game");
       menuBar.add(menu);
       
       JMenuItem help = new JMenuItem("How to play");
       menu.add(help); //"How to play" appear as second bar of "Menu" tab
       help.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
         JOptionPane.showMessageDialog(null, "The objective is to fill a 9x9 grid so that each column, each row, and each of the nine 3x3 boxes (also called blocks or regions) contains the digits from 1 to 9.", "Help", JOptionPane.INFORMATION_MESSAGE);
        }
       });

       menu = new JMenu("Quit");
       menu.setMnemonic(KeyEvent.VK_H);
       menu.getAccessibleContext().setAccessibleDescription(
               "This menu exit the game");
       menuBar.add(menu);
       menuPanel.add(menuBar, 0);
       setJMenuBar(menuBar);
       
       JMenuItem quit = new JMenuItem("Exit");
       menu.add(quit);
       quit.addActionListener(new ActionListener() {
    	   @Override
    	   public void actionPerformed(ActionEvent e) {
    	    int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the game? Game will not be saved.", "Confirm Dialog", JOptionPane.YES_NO_CANCEL_OPTION);
    	    if (answer == JOptionPane.YES_OPTION) {
    	     System.exit(0);
    	    }
    	   }
    	});
   }
   
   private void SudokuMain() {
      Container cp = getContentPane();
      cp.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));  // 9x9 GridLayout
      
      Difficulty initial = new Difficulty(); //create initial random puzzle
      masks = initial.Easy();
      // Allocate a common listener as the ActionEvent listener for all the
      //  JTextFields
      InputListener listener = new InputListener();
      RandomPuzzle();
      // Construct 9x9 JTextFields and add to the content-pane
      for (int row = 0; row < GRID_SIZE; ++row) {
         for (int col = 0; col < GRID_SIZE; ++col) {
            tfCells[row][col] = new JTextField(); // Allocate element of array
            cp.add(tfCells[row][col]);            // ContentPane adds JTextField
            if (masks[row][col]) {
               tfCells[row][col].setText("");     // set to empty string
               tfCells[row][col].setEditable(true);
               tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);
               // Add ActionEvent listener to process the input
               tfCells[row][col].addActionListener(listener);   // For all editable rows and cols
            } else {
               tfCells[row][col].setText(puzzle[row][col] + "");
               tfCells[row][col].setEditable(false);
               tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
               tfCells[row][col].setForeground(CLOSED_CELL_TEXT);
               if (row >= 0 && row < 3) {
    			   if ((col >=0 && col < 3) ||  (col >= 6 && col < 9)) {
    			   tfCells[row][col].setBackground(Color.PINK);
    			   }
    		   }
    		   if (row >= 3 && row < 6) {
    			   if (col >= 3 && col < 6) {
    			   tfCells[row][col].setBackground(Color.PINK);
    			   }
    		   }
    		   if (row >= 6 && row < 9) {
    			   if (col >= 0 && col < 3 || col >= 6 && col < 9) {
    			   tfCells[row][col].setBackground(Color.PINK);
    			   }
    		   }
            }
            
            // Beautify all the cells
            tfCells[row][col].setHorizontalAlignment(JTextField.CENTER);
            tfCells[row][col].setFont(FONT_NUMBERS);
         }
      }
 
      // Set the size of the content-pane and pack all the components
      //  under this container.
      cp.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
      pack();
 
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window closing
      setTitle("Sudoku");
      setVisible(true);
   }
   
   private void ResetSudoku() {
	   InputListener listener = new InputListener();
	      RandomPuzzle();
	      // Construct 9x9 JTextFields and add to the content-pane
	      for (int row = 0; row < GRID_SIZE; ++row) {
	         for (int col = 0; col < GRID_SIZE; ++col) {
	            if (masks[row][col]) {
	               tfCells[row][col].setText("");     // set to empty string
	               tfCells[row][col].setEditable(true);
	               tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);
	               // Add ActionEvent listener to process the input
	               tfCells[row][col].addActionListener(listener);   // For all editable rows and cols
	            } else {
	               tfCells[row][col].setText(puzzle[row][col] + "");
	               tfCells[row][col].setEditable(false);
	               tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
	               tfCells[row][col].setForeground(CLOSED_CELL_TEXT);
	               if (row >= 0 && row < 3) {
	    			   if ((col >=0 && col < 3) ||  (col >= 6 && col < 9)) {
	    			   tfCells[row][col].setBackground(Color.PINK);
	    			   }
	    		   }
	    		   if (row >= 3 && row < 6) {
	    			   if (col >= 3 && col < 6) {
	    			   tfCells[row][col].setBackground(Color.PINK);
	    			   }
	    		   }
	    		   if (row >= 6 && row < 9) {
	    			   if (col >= 0 && col < 3 || col >= 6 && col < 9) {
	    			   tfCells[row][col].setBackground(Color.PINK);
	    			   }
	    		   }
	            }
	            
	            // Beautify all the cells
	            tfCells[row][col].setHorizontalAlignment(JTextField.CENTER);
	            tfCells[row][col].setFont(FONT_NUMBERS);
	         }
	      }
	 
	      // Set the size of the content-pane and pack all the components
	      //  under this container.
	      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window closing
	      setTitle("Sudoku");
	      setVisible(true);
   }
   public Sudoku() {
	   setTitle("Sudoku");
	   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   createMenuBar();
	   SudokuMain();  
	   try {
	          // Generate an URL from filename
	          URL url = this.getClass().getClassLoader().getResource(fileSong);
	          if (url == null) {
	             System.err.println("Couldn't find file: " + fileSong);
	          } else {
	             // Get an audio input stream to read the audio data
	             AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	             // Allocate a sound clip, used by Java internal
	             soundSong = AudioSystem.getClip();
	             // Read the audio data into sound clip
	             soundSong.open(audioIn);
	             soundSong.loop(Clip.LOOP_CONTINUOUSLY);
	          }
	       } catch (UnsupportedAudioFileException e) {
	          System.err.println("Audio Format not supported: " + fileSong);
	       } catch (Exception e) {
	          e.printStackTrace();
	       }
	      
	      try {
	          // Generate an URL from filename
	          URL url = this.getClass().getClassLoader().getResource(fileGameOver);
	          if (url == null) {
	             System.err.println("Couldn't find file: " + fileGameOver);
	          } else {
	             // Get an audio input stream to read the audio data
	             AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	             // Allocate a sound clip, used by Java internal
	             soundClipGameOver = AudioSystem.getClip();
	             // Read the audio data into sound clip
	             soundClipGameOver.open(audioIn);
	             
	          }
	       } catch (UnsupportedAudioFileException e) {
	          System.err.println("Audio Format not supported: " + fileSong);
	       } catch (Exception e) {
	          e.printStackTrace();
	       }
   }
   
   // Random Puzzle for different levels (change number of boxes to be filled in) 
   public boolean[][] randomEasy() {
	  Difficulty easy = new Difficulty();
	  easy.Easy();
	  return easy.masks;
   }
   public boolean[][] randomMedium() {
	   Difficulty medium = new Difficulty();
	   medium.Medium();
	   return medium.masks;
   }
   public boolean[][] randomHard() {
	   Difficulty hard = new Difficulty();
	   hard.Hard();
	   return hard.masks;
   }
   public void RandomPuzzle() {
	   Random rand = new Random();
	   // randomly swapping rows
	   int row1, row2;
	   int x= rand.nextInt(3); // see which big row to do the swapping
	   switch (x) {
	   case 0: 
		   do {
			   row1 = getRandomNumberInRange(0,2); 
			   row2 = getRandomNumberInRange(0,2);
		   } while (row1 == row2);
		   swapRow(row1, row2);
	   break;
	   case 1: 
		   do {
			   row1 = getRandomNumberInRange(3,5);
			   row2 = getRandomNumberInRange(3,5);
		   } while (row1 == row2);
		   swapRow(row1, row2);
	   break;
	   case 2: 
		   do {
			   row1 = getRandomNumberInRange(6,8);
			   row2 = getRandomNumberInRange(6,8);
		   } while (row1 == row2);
		   swapRow(row1, row2);
	   break;
	   }
	   
	   // randomly swapping columns
	   int col1, col2;
	   int y= rand.nextInt(3); // see which big column to do the swapping
	   switch (y) {
	   case 0: 
		   do {
			   col1 = getRandomNumberInRange(0,2); 
			   col2 = getRandomNumberInRange(0,2);
		   } while (col1 == col2);
		   swapCol(col1, col2);
	   break;
	   case 1: 
		   do {
			   col1 = getRandomNumberInRange(3,5);
			   col2 = getRandomNumberInRange(3,5);
		   } while (col1 == col2);
		   swapCol(col1, col2);
	   break;
	   case 2: 
		   do {
			   col1 = getRandomNumberInRange(6,8);
			   col2 = getRandomNumberInRange(6,8);
		   } while (col1 == col2);
		   swapCol(col1, col2);
	   break;
	   }
   }
   
   // swap the columns
   public void swapCol(int col1, int col2) {
	   int temp;
	   for (int row = 0; row < 9; ++row) {
		   temp = puzzle[row][col1];
		   puzzle[row][col1] = puzzle[row][col2];
		   puzzle[row][col2] = temp;
	   }
   }
   // swap the rows
   public void swapRow(int row1, int row2) {
	   int temp;
	   for (int col = 0; col < 9; ++col) {
		   temp = puzzle[row1][col];
		   puzzle[row1][col] = puzzle[row2][col];
		   puzzle[row2][col] = temp;
	   }
   }
   private static int getRandomNumberInRange(int min, int max) {
	   if (min >= max) {
		   throw new IllegalArgumentException("max must be greater than min");
	   }
	   Random r = new Random();
	   return r.nextInt((max - min) + 1) + min;
   }
   /* The entry main() entry method */
   public static void main(String[] args) {
	   SwingUtilities.invokeLater(new Runnable() {
           public void run() {
              new Sudoku();
           }
       });
   }
   private class InputListener implements ActionListener {
 
      @Override
      public void actionPerformed(ActionEvent e) {
         // All the 9*9 JTextFileds invoke this handler. We need to determine
         // which JTextField (which row and column) is the source for this invocation.
         int rowSelected = -1;
         int colSelected = -1;
 
         // Get the source object that fired the event
         JTextField source = (JTextField)e.getSource();
         // Scan JTextFileds for all rows and columns, and match with the source object
         boolean found = false;
         for (int row = 0; row < GRID_SIZE && !found; ++row) {
            for (int col = 0; col < GRID_SIZE && !found; ++col) {
               if (tfCells[row][col] == source) {
                  rowSelected = row;
                  colSelected = col;
                  found = true;  // break the inner/outer loops
               }
            }
         }
 
         String input = tfCells[rowSelected][colSelected].getText();
         int answer = Integer.parseInt(input); //convert String to int
         if (answer == (puzzle[rowSelected][colSelected])) {
        	 tfCells[rowSelected][colSelected].setBackground(Color.GREEN);
        	 masks[rowSelected][colSelected] = false;
         } else {
        	 tfCells[rowSelected][colSelected].setBackground(Color.RED);
         }
         boolean solvePuzzle = true;
         for (int row = 0; row < GRID_SIZE; ++row) {
             for (int col = 0; col < GRID_SIZE; ++col) {
            	 if (masks[row][col] == true) {
            		 solvePuzzle = false;
            	 }
             }
         }
         if (solvePuzzle == true) {
        	 soundClipGameOver.start(); //add in clap sound
        	 JOptionPane.showMessageDialog(null, "Congratulations! The puzzle is solved!");
         }
      }
   }
}
