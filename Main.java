/*
*  This program compares the heights of binary search and AVL trees using both in-order and random data.
*
*  Author: Ethan Campbell
*  Date: Nov. 4, 2021
*/

import java.util.*;

/**
*  A binary search tree demonstration.
*
* @author Ethan Campbell
*/

public class Main {

  private int randomNum; // The number of random values to add to the tree
  private int arr[]; // Holds the generated values
  private RedBlack redBlack; // The binary search tree object
  private AVL avl; // The AVL tree object
  private int trialNum; // The number of values to add for in-order trees


  /**
  *  Constructor for the Lab3 class.
  *  Generates random integers and adds them to the AVL.
  */
  public Main() {

    trialNum = 100; // Run for 100 trials for random arrays of size 50 000
    for (int j = 0; j < trialNum; j++) {
      randomNum = 50000; // Size of the random arrays
      arr = randomArray(randomNum); // Generate the random values
      redBlack = new RedBlack();
      avl = new AVL(); // Create the AVL

      // Print the values and add them to the tree
      for (int i = 0; i < randomNum; i++) {
        //System.out.println("\nAdding to Red-Black: " + arr[i]);
        redBlack.insert(arr[i]);
      }

      // Print the values and add them to the tree
      for (int i = 0; i < randomNum; i++) {
        //System.out.println("\nAdding to AVL: " + arr[i]);
        avl.insert(arr[i]);
      }

      System.out.println("" + getRatio());
    }
     //redBlack.printTree();
    // System.out.println("" + redBlack.getTreeHeight());
     //avl.printTree();
     //System.out.println("" + avl.getTreeHeight());
  }


  /**
  *  Generates an int array with random values.
  *  Takes an unput for the array length. The
  *  possible random values are from 0 to twice
  *  the length of the array. (E.g. an array of
  *  length 10 can get values from 0-20).
  *
  *  @param arrayLength The length of the generated arrray
  */
  public static int[] randomArray(int arrayLength) {
    int[] myArray = new int[arrayLength];

    // Random number generator from 0 to twice the array's length
    Random rand = new Random();
    int upperbound = arrayLength * 2 + 1;

    // Generate a random number for each position in the array
    for (int i = 0; i < arrayLength; i++) {
      myArray[i] = rand.nextInt(upperbound);
    }

    return myArray;
  }

  /**
  *  Casts the heights of two trees into doubles and return the ratio
  *
  *  @return The ratio of the height of two trees
  */
  private double getRatio() {
    return Double.valueOf(redBlack.getTreeHeight()) / Double.valueOf(avl.getTreeHeight());
  }


  /**
  *  Main method to start the program
  *  @param args Unused
  */
  public static void main(String[] args) {
    new Main();
  }
}
