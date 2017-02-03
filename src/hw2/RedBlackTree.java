package hw2;
import java.util.*;

/**
 * Created by tylerangert on 1/30/17.
 */


public class RedBlackTree {

  public static void main(String[] args){
    System.out.println("WE WORKIN");

    RedBlackTree myTree = new RedBlackTree();

    String lowAlphabet = "abcdefghijklmnopqrstuvwxyz";
    String highAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    for(int i = 0; i < lowAlphabet.length(); i++) {
      myTree.easyAdd(Character.toString(lowAlphabet.charAt(i)));
    }

    for(int i = 0; i < 10; i++) {
      myTree.easyAdd(Integer.toString(i));
    }

    for(int i = 0; i < 9; i++) {
      myTree.remove(Integer.toString(i));
    }

    System.out.println("Searched value: " + myTree.search("2"));
    System.out.println(myTree.toString() + "\n");

    System.out.println("Red Count: " + myTree.reds.size() + ":" + myTree.reds);
    System.out.println("Black Count: " + myTree.blacks.size() + ":" + myTree.blacks);
    System.out.println("Nil Count: " + myTree.nils.size());
    System.out.println(myTree.toString(myTree.getNodeFromKey("a")));

  }

  //testing variables
  ArrayList<String> reds = new ArrayList<String>();
  ArrayList<String> blacks = new ArrayList<String>();
  ArrayList<String> nils = new ArrayList<String>();
  String treeTravesalString = "";

  //toString for the entire tree.
  public String toString() {
    treeTravesalString = "";
    return preOrderTraversal(root);
  }

  //this toString prints the tree starting at any chosen root node
  public String toString(Node rootNode) {
    treeTravesalString = "";
    if(rootNode == null) return "No subtree at this node";
    else {
      System.out.println("\nSub tree at node: " + rootNode.key);
      return preOrderTraversal(rootNode);
    }
  }

  /*
  (key:value (left-subtree) (right-subtree))
  If the key:value pair is contained in a red node, prepend a star to it: *key:value
  If the key:value pair is contained in a black node, prepend a pound sign to it: #key:value
  */
  public String preOrderTraversal(Node node) {
      if (node == nil) {
        nils.add("NIL");
        return treeTravesalString+=("(NIL)");
      } else if (node.color == RED) {
        reds.add(node.key);
        treeTravesalString+=("(*" + node.key + ":" + node.value);
      } else if (node.color == BLACK) {
        blacks.add(node.key);
        treeTravesalString+=("(#" + node.key + ":" + node.value);
      }

      preOrderTraversal(node.left);
      preOrderTraversal(node.right);
      return treeTravesalString+=(")");
  }

  private static final boolean RED = true;
  private static final boolean BLACK = false;
  private Node nil = new Node();
  private Node root = nil;

  //Constructor
  public RedBlackTree(){
    root.left = nil;
    root.right = nil;
    root.parent = nil;
  }

  //PRIMARY METHODS
  //ADD NODE
  public void add(String key, String value) {
    Node y = nil;
    Node x = root;
    Node toBeAdded = new Node(key, value, BLACK);

    if (root == nil) {
      root = toBeAdded;
      root.parent = nil;
    } else {
      while (x != nil) {
        y = x;
        if ( compare(toBeAdded.key, x.key) < 0) {
          x = x.left;
        } else {
          x = x.right;
        }
      }
      toBeAdded.parent = y;

      if (y == nil) {
        root = toBeAdded;
      }
      else if (compare(toBeAdded.key, y.key) < 0) {
        y.left = toBeAdded;
      } else {
        y.right = toBeAdded;
      }
      toBeAdded.left = nil;
      toBeAdded.right = nil;
      toBeAdded.color = RED;
      addFix(toBeAdded);
    }

  }

  //ADD for test purposes
  //automatically creates a value with the appropriate key.
  public void easyAdd(String key) {
    Node y = nil;
    Node x = root;
    String val = key + "-val";
    Node toBeAdded = new Node(key, val, BLACK);

    if (root == nil) {
      root = toBeAdded;
      root.parent = nil;
    }
    else {
      while (x != nil) {
        y = x;
        if ( compare(toBeAdded.key, x.key) < 0) {
          x = x.left;
        } else {
          x = x.right;
        }
      }
      toBeAdded.parent = y;

      if (y == nil) {
        root = toBeAdded;
      }
      else if (compare(toBeAdded.key, y.key) < 0) {
        y.left = toBeAdded;
      } else {
        y.right = toBeAdded;
      }
      toBeAdded.left = nil;
      toBeAdded.right = nil;
      toBeAdded.color = RED;
      addFix(toBeAdded);
    }

  }

  //REMOVE NODE
  public void remove(String key) {
    Node toBeRemoved = getNodeFromKey(key);
    if(toBeRemoved==null) {
      return;
    }

    else {
      Node x;
      Node y = toBeRemoved;
      boolean yOriginalColor = y.color;

      if (toBeRemoved.left == nil) {
        x = toBeRemoved.right;
        transplant(toBeRemoved, toBeRemoved.right);
      } else if (toBeRemoved.right == nil) {
        x = toBeRemoved.left;
        transplant(toBeRemoved, toBeRemoved.left);
      } else {
        y = subTreeMinNode(toBeRemoved.right);
        yOriginalColor = y.color;
        x = y.right;
        if (y.parent == toBeRemoved)
          x.parent = y;
        else {
          transplant(y, y.right);
          y.right = toBeRemoved.right;
          y.right.parent = y;
        }
        transplant(toBeRemoved, y);
        y.left = toBeRemoved.left;
        y.left.parent = y;
        y.color = toBeRemoved.color;
      }
      if (yOriginalColor == BLACK)
        removeFix(x);
    }
  }

  //FIND NODE
  public String search(String key) {
    Node searchedNode = getNodeFromKey(key);
    if (searchedNode != null) {
      return searchedNode.value;
    }
    return null;
  }

  //HELPER METHODS
  public int compare(String key1, String key2) {
    return key1.compareTo(key2);
  }

  public Node getNodeFromKey(String key) {
    Node current = root;
    if (current == nil) {
      System.out.println("Nil node!");
    }
    while (current!= nil) {
      if(current.key.equals(key)) {
        //gets the node with a specified key.
        return current;
      }
      else if (compare(current.key, key)<0) {
        current = current.right;
      }
      else {
        current = current.left;
      }
    }
    System.out.println("No node at all.");
    return null;
  }

  public void transplant(Node node1, Node node2) {
    if(node1.parent == nil) root = node2;
    else if(node1 == node1.parent.left){
      node1.parent.left = node2;
    }else
      node1.parent.right = node2;
    node2.parent = node1.parent;
  }

  public void addFix(Node node) {
    while (node.parent.color == RED) {
      Node uncle = nil;
      if (node.parent == node.parent.parent.left) {
        uncle = node.parent.parent.right;

        if (uncle != nil && uncle.color == RED) {
          node.parent.color = BLACK;
          uncle.color = BLACK;
          node.parent.parent.color = RED;
          node = node.parent.parent;
          continue;
        }else if (node == node.parent.right) {
          node = node.parent;
          rotateLeft(node);
        }
        node.parent.color = BLACK;
        node.parent.parent.color = RED;
        rotateRight(node.parent.parent);
      } else {
        uncle = node.parent.parent.left;
        if (uncle != nil && uncle.color == RED) {
          node.parent.color = BLACK;
          uncle.color = BLACK;
          node.parent.parent.color = RED;
          node = node.parent.parent;
          continue;
        }
        else if (node == node.parent.left) {
          node = node.parent;
          rotateRight(node);
        }
        node.parent.color = BLACK;
        node.parent.parent.color = RED;
        rotateLeft(node.parent.parent);
      }
    }
    root.color = BLACK;
  }

  public void removeFix(Node node){
    while(node!=root && node.color == BLACK){
      if(node == node.parent.left){
        Node grandpa = node.parent.right;
        if(grandpa.color == RED){
          grandpa.color = BLACK;
          node.parent.color = RED;
          rotateLeft(node.parent);
          grandpa = node.parent.right;
        }
        if(grandpa.left.color == BLACK && grandpa.right.color == BLACK){
          grandpa.color = RED;
          node = node.parent;
          continue;
        }
        else if(grandpa.right.color == BLACK){
          grandpa.left.color = BLACK;
          grandpa.color = RED;
          rotateRight(grandpa);
          grandpa = node.parent.right;
        }
//        if(grandpa.right.color == RED){
          grandpa.color = node.parent.color;
          node.parent.color = BLACK;
          grandpa.right.color = BLACK;
          rotateLeft(node.parent);
          node = root;
//        }
      }else{
        Node grandma = node.parent.left;
        if(grandma.color == RED){
          grandma.color = BLACK;
          node.parent.color = RED;
          rotateRight(node.parent);
          grandma = node.parent.left;
        }
        if(grandma.right.color == BLACK && grandma.left.color == BLACK){
          grandma.color = RED;
          node = node.parent;
          continue;
        }
        else if(grandma.left.color == BLACK){
          grandma.right.color = BLACK;
          grandma.color = RED;
          rotateLeft(grandma);
          grandma = node.parent.left;
        }
//        if(grandma.left.color == RED){
          grandma.color = node.parent.color;
          node.parent.color = BLACK;
          grandma.left.color = BLACK;
          rotateRight(node.parent);
          node = root;
//        }
      }
    }
    node.color = BLACK;

  }

  public void rotateRight(Node pivot) {
    Node left = pivot.left;
    pivot.left = left.right;
    if (left.right != nil) {
      left.right.parent = pivot;
    }
    left.parent = pivot.parent;
    if (pivot.parent == nil) {
      root = left;
    } else if (pivot == pivot.parent.right) {
      pivot.parent.right = left;
    } else {
      pivot.parent.right = left;
    }
    left.right = pivot;
    pivot.parent = left;
  }

  public void rotateLeft(Node pivot) {
    Node right = pivot.right;
    pivot.right = right.left;
    if (right.left != nil) {
      right.left.parent = pivot;
    }
    right.parent = pivot.parent;
    if (pivot.parent == nil) {
      root = right;
    } else if (pivot == pivot.parent.left) {
      pivot.parent.left = right;
    } else {
      pivot.parent.right = right;
    }
    right.left = pivot;
    pivot.parent = right;

  }

  //UTILITY METHODS
  public Node subTreeMinNode(Node node) {
    while (node.left != nil) {
      node = node.left;
    }
    return node;
  }

  public String subTreeMinVal(Node node) {
    while (node.left != nil) {
      node = node.left;
    }
    return node.value;
  }

  private class Node {
    String key;
    String value;
    boolean color;

    Node left = nil;
    Node right = nil;
    Node parent = nil;

    //Constructor 1
    Node(String key, String value, boolean color) {
      this.key = key;
      this.value = value;
      this.color = color;
    }

    //Constructor 2
    Node(){}
  }

  //NODE Helper Methods
  private boolean isRed(Node node) {
    if (node == nil) return false;
    return node.color == RED;
  }

  private boolean isBlack(Node node) {
    return !isRed(node);
  }

}
