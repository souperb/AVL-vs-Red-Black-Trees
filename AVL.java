/*
*  An AVL tree class.
*
*  Author: Ethan Campbell
*  Date: Nov. 4, 2021
*/

/**
*  The Node class.
*  Creates a node that point to two more nodes.
*  Stores an integer value.
*/

public class AVL {

  private Node root; // Stores the root node of the tree
  private Node trail1; // Tracks the node immediate behind the current node
  private Node trail2; // Tracks the node 2 behind the current node

  /**
  *  Constructor for the AVL class.
  *  Initializes root to null.
  */
  public AVL() {
    root = null;
    trail1 = null;
    trail2 = null;
  }


  /**
  *  The Node class.
  *  Creates a node that point to two more nodes.
  *  Stores an integer value.
  */
  private class Node {
    int value;
    int height;
    Node left;
    Node right;

    /**
    *  Constructor for the Node class.
    *  Stores the given value and creates two more
    *  null nodes to point to.
    *
    *  @param value The integer to store in the node
    */
    private Node(int value) {
      this.value = value;
      height = 1;
      left = null;
      right = null;
    }
  }


  /**
  *  Inserts a value into the AVL if the value is not already in it
  *
  *  @param value The value that is to be added to the tree
  */
  public void insert(int value) {
    root = addNode(root, value); // Call addNode to place the value
  }


  /**
  *  This function will add a value to a node if it is null.
  *  Otherwise, it will try to add it on the left or right recursively
  *  depending on if the value is smaller or larger than the current node.
  *  If the value is already in the tree, it is not added.
  *  Prints the path from the added node back to the root node
  *
  *  @param node The current node the value is being compared to
  *  @param value The value that is to be added to the tree
  */
  private Node addNode(Node node, int value) {
    if (node == null) { // If there is no node at this location, create one with this value
      node = new Node(value);

      // Update the trails
      trail2 = null;
      //trail1 = null;
      //printParent(node);
      trail1 = node;

    } else if (node.value > value) { // If the value is smaller than the node, try it on the left
      node.left = addNode(node.left, value);
      // printParent(node);
      node = updateHeight(node);
      node = checkBalance(node, trail1, trail2);

      // Update the trails
      trail1 = node;
      trail2 = node.left;

    } else if (node.value < value) { // If the value is larger than the node, try it on the right
      node.right = addNode(node.right, value);
      // printParent(node);
      node = updateHeight(node);
      node = checkBalance(node, trail1, trail2);

      // Update the trails
      trail1 = node;
      trail2 = node.right;

    // If none of these statements executed, that means the value is the same as one already
    // in the tree and therefore it is not added.
    }

    return node;
  }


  /**
  *  Updates the height of a node based on its the taller of its children
  *
  *  @param node The node who's height to update
  */
  private Node updateHeight(Node node) {
    node.height = Math.max(getNodeHeight(node.left), getNodeHeight(node.right)) + 1;
    return node;
  }


  /**
  *  Checks the height of a node's two children. If the difference is greater than 1 then it calls the proper roation to rebalance them
  *
  *  @param node The node who's childrens heights to check
  *  @param trail1 The first trail coming off of node
  *  @param trail2 The second trail that follows trail1
  */
  private Node checkBalance(Node node, Node trail1, Node trail2) {
    // Check the balance of the heights
    if (getNodeHeight(node.left) - getNodeHeight(node.right) > 1 || getNodeHeight(node.left) - getNodeHeight(node.right) < -1) {

      // If the difference is greater than 1 then call the apportiate rotation to restore balance
      if (getNodeValue(node) < getNodeValue(trail1) && getNodeValue(trail1) < getNodeValue(trail2)) {
        node = ABCRotation(node);
      } else if (getNodeValue(node) > getNodeValue(trail1) && getNodeValue(trail1) > getNodeValue(trail2)) {
        node = CBARotation(node);
      } else if (getNodeValue(node) < getNodeValue(trail1) && getNodeValue(trail1) > getNodeValue(trail2)) {
        node = ACBRotation(node);
      } else {
        node = CABRotation(node);
      }
    }
    return node;
  }


  // Does an ABC rotation and updates the heights of all 3 nodes
  private Node ABCRotation(Node node) {

    Node a = node;
    Node b = a.right;
    Node c = b.right;

    a.right = b.left;
    b.left = a;

    a = updateHeight(a);
    c = updateHeight(c);
    b = updateHeight(b);

    return b;
  }

  // Does an CBA rotation and updates the heights of all 3 nodes
  private Node CBARotation(Node node) {

    Node c = node;
    Node b = c.left;
    Node a = b.left;

    c.left = b.right;
    b.right = c;

    a = updateHeight(a);
    c = updateHeight(c);
    b = updateHeight(b);

    return b;
  }

  // Does an ACB rotation and updates the heights of all 3 nodes
  private Node ACBRotation(Node node) {

    Node a = node;
    Node c = node.right;
    Node b = c.left;

    a.right = b.left;
    c.left = b.right;
    b.left = a;
    b.right = c;

    a = updateHeight(a);
    c = updateHeight(c);
    b = updateHeight(b);

    return b;
  }

  // Does an CAB rotation and updates the heights of all 3 nodes
  private Node CABRotation(Node node) {

    Node c = node;
    Node a = c.left;
    Node b = a.right;

    a.right = b.left;
    c.left = b.right;
    b.left = a;
    b.right = c;

    a = updateHeight(a);
    c = updateHeight(c);
    b = updateHeight(b);

    return b;
  }


  /**
  *  Prints the value of a node and the two values trailing it
  *
  *  @param node The node who's value to print
  */
  private void printParent(Node node) {

    if (trail1 == null) {
      System.out.println("" + node.value + " -> null -> null");
    } else if (trail2 == null) {
      System.out.println("" + node.value + " -> " + trail1.value + " -> null");
    } else {
      System.out.println("" + node.value + " -> " + trail1.value + " -> " + trail2.value);
    }
  }


  /**
  *  Prints the values of the entire tree
  */
  public void printTree() {
    System.out.println("\n=== AVL Tree ===");
    printNode(root);
  }


  /**
  *  Prints the depth, value, and values of the node on the
  *  left and right for every node starting at the one passed in.
  *  Uses in-order traversal to print values from smallest to largest.
  *
  *  @param node The node to start printing at
  *  @param depth Tracks the depth of the current node
  */
  private void printNode(Node node) {

    // If there is a node with a smaller value print it first
    if (node.left != null) printNode(node.left);

    // Print the depth and value of the current node
    System.out.print("Height " + node.height + ", value = " + node.value);

    // Print the value of the node on the left, or null if it is null
    if (node.left == null) {
      System.out.print(" , left = null");
    } else {
      System.out.print(" , left = " + node.left.value);
    }

    // Print the value of the node on the right, or null if it is null
    if (node.right == null) {
      System.out.print(" , right = null\n");
    } else {
      System.out.print(" , right = " + node.right.value + "\n");
    }

    // If there is a node with a larger value print it
    if (node.right != null) printNode(node.right);
  }

  /**
  *  Returns the height of the root node
  *
  *  @return The height of the root node
  */
  public int getTreeHeight() {return getNodeHeight(root);}

  // Returns the height of a node. If the node is null, returns 0
  private int getNodeHeight(Node node) {return node == null ? 0 : node.height;}

  // Returns the value stored in a node. If the node is null, returns -1
  private int getNodeValue(Node node) {return node == null ? -1 : node.value;}
}
