package hw2;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.*;

/**
 * Created by tylerangert on 1/30/17.
 */


public class RedBlackTree {

  public static void main(String[] args){
    System.out.println("Hello world");
  }

  //To String
  public String toString() {
    /*
    (key:value (left-subtree) (right-subtree))
    If the key:value pair is contained in a red node, prepend a star to it: *key:value
    If the key:value pair is contained in a black node, prepend a pound sign to it: #key:value
      */

    return "";
  }


  //Storing RED and BLACK as booleans.
  private static final boolean RED = true;
  private static final boolean BLACK = false;
  //placeholder nil
  private Node nil = new Node();
  //Root node.
  private Node root = nil;

  //Constructor
  public RedBlackTree(){
    root.left = nil;
    root.right = nil;
    root.parent = nil;
  }

  //PRIMARY METHODS
  //ADD
  public void add(String key, String value) {

    Node temp = root;
    if (root == nil) {
      root = new Node(key, value, BLACK);
      root.parent = nil;
    }
    else {
      Node newNode = new Node();
      newNode.color = RED;
      int keyComparison = compare(newNode.key, key);

      while(true) {
        //traversing to the left
        if (keyComparison < 0) {
          if (temp.left == nil) {
            temp.left = newNode;
            newNode.parent = temp;
            break;
          } else {
            temp = temp.left;
          }
        }
        //traversing to the right
        else if (keyComparison >= 0) {
          if (temp.right == nil) {
            temp.right = newNode;
            break;
          }
          else {
            temp = temp.right;
          }
        }
      }
      addFix(newNode);
    }
  }

  //REMOVE
  public void remove(String key) {
    //this would be the equivalent of the input node.
    Node toBeRemoved = getNodeFromKey(key);

    if(toBeRemoved==null) {
      System.out.println("whoops!");
    }
    else {
      Node x;
      Node y = toBeRemoved; // temporary reference y
      boolean y_original_color = y.color;

      if (toBeRemoved.left == nil) {
        x = toBeRemoved.right;
        transplant(toBeRemoved, toBeRemoved.right);
      } else if (toBeRemoved.right == nil) {
        x = toBeRemoved.left;
        transplant(toBeRemoved, toBeRemoved.left);
      } else {
        y = subTreeMinNode(toBeRemoved.right);
        y_original_color = y.color;
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
      if (y_original_color == BLACK)
        removeFix(x);
    }
  }

  //FIND
  public String search(String key) {
    Node current = root;
    if (current == null) {
      System.out.println("oops!");
    } else {
      while (current != nil) {
        if (current.key.equals(key)) {
          return current.key;
        }
        else if( compare(current.key, key) < 0) {
          current = current.right;
        }
        else {
          current = current.left;
        }
      }
    }
    return null;
  }

  //HELPER METHODS
  public int compare(String key1, String key2) {
    return key1.compareTo(key2);
  }

  public Node getNodeFromKey(String key) {
    Node current = root;
    while (current!= null) {
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
        }
        if (node == node.parent.right) {
          //Double rotation needed
          node = node.parent;
          rotateLeft(node);
        }
        node.parent.color = BLACK;
        node.parent.parent.color = RED;
        //if the "else if" code hasn't executed, this
        //is a case where we only need a single rotation
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
        if (node == node.parent.left) {
          //Double rotation needed
          node = node.parent;
          rotateRight(node);
        }
        node.parent.color = BLACK;
        node.parent.parent.color = RED;
        //if the "else if" code hasn't executed, this
        //is a case where we only need a single rotation
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
        if(grandpa.right.color == RED){
          grandpa.color = node.parent.color;
          node.parent.color = BLACK;
          grandpa.right.color = BLACK;
          rotateLeft(node.parent);
          node = root;
        }
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
        if(grandma.left.color == RED){
          grandma.color = node.parent.color;
          node.parent.color = BLACK;
          grandma.left.color = BLACK;
          rotateRight(node.parent);
          node = root;
        }
      }
    }
    node.color = BLACK;

  }

  public void rotateRight(Node node) {
    if (node.parent != nil) {
      if (node == node.parent.left) {
        node.parent.left = node.left;
      } else {
        node.parent.right = node.left;
      }

      node.left.parent = node.parent;
      node.parent = node.left;
      if (node.left.right != nil) {
        node.left.right.parent = node;
      }
      node.left = node.left.right;
      node.parent.right = node;
    } else {//Need to rotate root
      Node left = root.left;
      root.left = root.left.right;
      left.right.parent = root;
      root.parent = left;
      left.right = root;
      left.parent = nil;
      root = left;
    }

  }

  public void rotateLeft(Node node) {
    if (node.parent != nil) {
      if (node == node.parent.left) {
        node.parent.left = node.right;
      } else {
        node.parent.right = node.right;
      }
      node.right.parent = node.parent;
      node.parent = node.right;
      if (node.right.left != nil) {
        node.right.left.parent = node;
      }
      node.right = node.right.left;
      node.parent.left = node;
    } else {//Need to rotate root
      Node right = root.right;
      root.right = right.left;
      right.left.parent = root;
      root.parent = right;
      right.left = root;
      right.parent = nil;
      root = right;
    }

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

  public boolean treeIsEmpty() {
    return (root.right == null) && (root.left == null);
  }

  public boolean subTreeIsEmpty(Node node) {
    return (node.right == null) && (node.left == null);
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
