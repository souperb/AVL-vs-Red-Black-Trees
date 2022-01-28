/*
*  A Red-Black tree class.
*
*  Author: Ethan Campbell
*  Date: Nov. 22, 2021
*/

public class RedBlack {

  private Node root; // Stores the root node of the tree
  private Node trail;
  private boolean violation;
  private boolean abc;
  private boolean cba;
  private boolean acb;
  private boolean cab;


  /**
  *  Constructor for the RedBlack class.
  *  Initializes root to null.
  */
  public RedBlack() {
    root = null;
    trail = null;
    violation = false;
    abc = false;
    cba = false;
    acb = false;
    cab = false;
  }


  /**
  *  The Node class.
  *  Creates a node that point to two more nodes.
  *  Stores an integer value.
  */
  private class Node {
    int value;
    int height;
    char colour;
    Node parent;
    Node left;
    Node right;

    /**
    *  Constructor for the Node class.
    *  Stores the given value and creates two more
    *  null nodes to point to.
    *
    *  @param value The integer to store in the node
    */
    private Node(int value, Node parent) {
      this.value = value;
      height = 1;
      colour = 'r'; // New nodes are inserted as red
      this.parent = parent;
      left = null;
      right = null;
    }
  }


  /**
  *  Inserts a value into the Red-Black tree if the value is not already in it
  *
  *  @param value The value that is to be added to the tree
  */
  public void insert(int value) {
    root = addNode(root, value); // Call addNode to place the value
    root.colour = 'b';
  }


  /**
  *  This function will add a value to a node if it is null.
  *  Otherwise, it will try to add it on the left or right recursively
  *  depending on if the value is smaller or larger than the current node.
  *  If the value is already in the tree, it is not added.
  *
  *  @param node The current node the value is being compared to
  *  @param value The value that is to be added to the tree
  */
  private Node addNode(Node node, int value) {
    if (node == null) { // If there is no node at this location, create one with this value
      node = new Node(value, trail);

    } else if (node.value > value) { // If the value is smaller than the node, try it on the left
      trail = node;
      node.left = addNode(node.left, value);
    
      if (getNodeColour(node) == 'r' && getNodeColour(node.left) == 'r') {
        violation = true;
      }

      node = updateHeight(node);


    } else if (node.value < value) { // If the value is larger than the node, try it on the right
      trail = node;
      node.right = addNode(node.right, value);
      if (getNodeColour(node) == 'r' && getNodeColour(node.right) == 'r') {
        violation = true;
      }

      node = updateHeight(node);
    }

    // If none of these statements executed, that means the value is the same as one already
    // in the tree and therefore it is not added.

    // Now I do my rotations that were flagged from the previous level of recursion (see code below)
    // The appropriate colour changes and rotations are done to restore the red-black properties
    if (abc) {
        node.colour = 'r';
        node.right.colour = 'b';
        node = ABCRotation(node);
        abc = false;
    } else if (cba) {
        node.colour = 'r';
        node.left.colour = 'b';
        node = CBARotation(node);
        cba = false;
    } else if (acb) {
        node.colour = 'r';
        node.right.colour = 'b';
        node = ACBRotation(node);
        acb = false;
    } else if (cab) {
        node.colour = 'r';
        node.left.colour = 'b';
        node = CABRotation(node);
        cab = false;
    }

    // If there was a double red violation flagged earlier then either the colours are swapped
    // or the appropriate rotation is flagged for when it recurses back up
    if (violation && node.parent != null) {
        // Find which side of the grandparent the parent is (parent currently 'node')
        if (getNodeValue(node) < getNodeValue(node.parent)) {
            // If the sibling is red then re-colour the nodes appropriately
            if (getNodeColour(node.parent.right) == 'r') {
                node.colour = 'b';
                node.parent.right.colour = 'b';
                node.parent.colour = 'r';

            // If the sibling is black, then flag the appropriate rotation
            } else {
                if (getNodeColour(node.left) == 'r') {
                    cba = true;
                } else if (getNodeColour(node.right) == 'r') {
                    cab = true;
                }
            }
        } else {
            // If the sibling is red then re-colour the nodes appropriately
            if (getNodeColour(node.parent.left) == 'r') {
                node.colour = 'b';
                node.parent.left.colour = 'b';
                node.parent.colour = 'r';

            // If the sibling is black, then flag the appropriate rotation
            } else {
                if (getNodeColour(node.left) == 'r') {
                    acb = true;
                } else if (getNodeColour(node.right) == 'r') {
                    abc = true;
                }
            }

        }
        // Reset the flag
        violation = false;
    }

    return node;
  }


// Does an ABC rotation and updates the heights and parents of all 3 nodes
  private Node ABCRotation(Node node) {

    Node a = node;
    Node b = a.right;
    Node c = b.right;

    a.right = b.left;
    if (a.right != null) a.right.parent = a;
    b.left = a;

    b.parent = a.parent;
    a.parent = b;

    a = updateHeight(a);
    c = updateHeight(c);
    b = updateHeight(b);

    return b;
  }

  // Does an CBA rotation and updates the heights and parents of all 3 nodes
  private Node CBARotation(Node node) {

    Node c = node;
    Node b = c.left;
    Node a = b.left;

    c.left = b.right;
    if (c.left != null) c.left.parent = c;
    b.right = c;

    b.parent = c.parent;
    c.parent = b;

    a = updateHeight(a);
    c = updateHeight(c);
    b = updateHeight(b);

    return b;
  }

  // Does an ACB rotation and updates the heights and parents of all 3 nodes
  private Node ACBRotation(Node node) {

    Node a = node;
    Node c = node.right;
    Node b = c.left;

    a.right = b.left;
    c.left = b.right;
    if (a.right != null) a.right.parent = a;
    if (c.left != null) c.left.parent = c;
    b.left = a;
    b.right = c;

    b.parent = a.parent;
    a.parent = b;
    c.parent = b;

    a = updateHeight(a);
    c = updateHeight(c);
    b = updateHeight(b);

    return b;
  }

  // Does an CAB rotation and updates the heights and parents of all 3 nodes
  private Node CABRotation(Node node) {

    Node c = node;
    Node a = c.left;
    Node b = a.right;

    a.right = b.left;
    c.left = b.right;
    if (a.right != null) a.right.parent = a;
    if (c.left != null) c.left.parent = c;
    b.left = a;
    b.right = c;

    b.parent = c.parent;
    a.parent = b;
    c.parent = b;

    a = updateHeight(a);
    c = updateHeight(c);
    b = updateHeight(b);

    return b;
  }


  // Updates the height of a node based on its childrens' heights
  private Node updateHeight(Node node) {
    node.height = Math.max(getNodeHeight(node.left), getNodeHeight(node.right)) + 1;
    return node;
  }


  /**
  *  Prints the values of the entire tree
  */
  public void printTree() {
    System.out.println("\n=== Red Black Tree ===");
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
    System.out.print("Height " + getNodeHeight(node) + ", colour " + getNodeColour(node) + ", value = " + getNodeValue(node));

    // Print the value of the node on the left, or null if it is null
    if (node.left == null) {
      System.out.print(" , left = null");
    } else {
      System.out.print(" , left = " + getNodeValue(node.left));
    }

    // Print the value of the node on the right, or null if it is null
    if (node.right == null) {
      System.out.print(" , right = null\n");
    } else {
      System.out.print(" , right = " + getNodeValue(node.right) + "\n");
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
  public int getNodeHeight(Node node) {return node == null ? 0 : node.height;}

  // Returns the colour of a node. If the node is null, returns 'b'
  public char getNodeColour(Node node) {return node == null ? 'b' : node.colour;}

  // Returns the value stored in a node. If the node is null, returns -1
  private int getNodeValue(Node node) {return node == null ? -1 : node.value;}
}
