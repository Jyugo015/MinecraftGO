/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Asus
 */
public class MultipleTool extends MultitoolGUIController{

    private Node head;
    private Node tail;
    private int size;

    // public MultipleTool() {
    //     this.head = null;
    //     this.tail = null;
    //     this.size = 0;
    // }

    public MultipleTool(String username) throws SQLException{
        this.head = null;
        this.tail = null;
        this.size = 0;
        List<Tool> toollist = database_item2.retrieveMultitool(username);
        toollist.forEach(tool->this.addTool(tool));
    }

    private class Node {

        Tool tool;
        Node next;
        Node prev;

        public Node(Tool tool) {
            this(tool, null,null);
        }

        public Node(Tool tool, Node next, Node prev){
            this.tool = tool;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * Adds a new tool to the end of the list.
     *
     * @param tool The tool to be added.
     */
    public void addTool(Tool tool) {
        Node newNode = new Node(tool);
        if (head == null) {
            head = tail = newNode; // first tool being added
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
            // Adding circular link
            tail.next = head;
            head.prev = tail;
        }
        size++;
    }

    /**
     * Inserts a tool at the specified index in the list.
     *
     * @param index The index where the tool should be inserted.
     * @param tool The tool to insert.
     * @throws IllegalArgumentException if the index is out of bounds.
     */
    public void insertTool(int index, Tool tool) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("Invalid index: " + index);
        }
        Node newNode = new Node(tool);
        if (index == 0) {
            if (head == null) {
                head = tail = newNode;// if list is empty, initialize it with the new node
                newNode.next = newNode.prev = newNode; // Maintain circularity in a single-node scenario
            } else {
                newNode.next = head;
                newNode.prev = tail;
                head.prev = newNode;
                tail.next = newNode;
                head = newNode;
            }
        } else {
            Node current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            newNode.next = current.next;
            newNode.prev = current;
            current.next.prev = newNode;
            current.next = newNode;
            if (index == size) {
                tail = newNode; // Update tail if inserted at the end
            }
        }
        size++;
    }

    /**
     * Removes the specified tool from the list.
     *
     * @param tool The tool to be removed.
     * @return The removed tool, or null if it wasn't found.
     * @throws IllegalStateException if the tool is not found.
     */
    public Tool removeTool(Tool tool) {
        Node toRemove = findNode(tool);
        if (toRemove == null) {
            throw new IllegalStateException("Tool not found: " + tool);
        }
        if (size == 1) {
            head = tail = null;// handle case with only one node
        } else {
            toRemove.prev.next = toRemove.next;
            toRemove.next.prev = toRemove.prev;
            if (toRemove == head) {
                head = toRemove.next;// update head if necessary
            }
            if (toRemove == tail) {
                tail = toRemove.prev;// update tail if necessary
            }
        }
        size--;
        return toRemove.tool;
    }

    /**
     * Retrieves a tool from the list based on its position index.
     *
     * @param index The zero-based index of the tool to retrieve.
     * @return The tool at the specified index in the list.
     * @throws IndexOutOfBoundsException if the index is out of the list's
     * bounds.
     */
    public Tool getTool(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.tool;
    }

    /**
     * Switches to the next tool in the list based on the current tool. If the
     * current tool is the last one, it wraps around to the first tool in the
     * list.
     *
     * @param currentTool The current tool being used.
     * @return The next tool in the list. Returns the first tool if the current
     * tool is the last one.
     */
    public Tool switchToolDown(Tool currentTool) {
        Node currentNode = findNode(currentTool);
        if (currentNode != null && currentNode.next != null) {
            return currentNode.next.tool;
        } else {
            return head.tool; // Return to the first tool if current is the last tool
        }
    }
    
    /**
     * Switches to the previous tool in the list based on the current tool. If the
     * current tool is the first one, it wraps around to the last tool in the
     * list.
     *
     * @param currentTool The current tool being used.
     * @return The next tool in the list. Returns the first tool if the current
     * tool is the last one.
     */
    public Tool switchToolUp(Tool currentTool) {
        Node currentNode = findNode(currentTool);
        if (currentNode != null && currentNode.next != null) {
            return currentNode.prev.tool;
        } else {
            return head.tool; // Return to the first tool if current is the last tool
        }
    }

    /**
     * Finds the node containing the specified tool. Uses linear search to check
     * each node starting from the head and wraps around due to circular nature.
     *
     * @param tool The tool to find in the list.
     * @return The node containing the tool, or null if the tool is not found.
     */
    private Node findNode(Tool tool) {
        Node current = head;
        if (tool == null) {
            return null;
        }
        do {
            if (current.tool.equals(tool)) {
                return current;
            }
            current = current.next;
        } while (current != head);
        return null;
    }

    /**
     * Increases the grade of a specified tool.
     *
     * @param tool The tool whose grade is to be increased.
     * @param upgrade The amount to increase the grade by. Throws
     * IllegalStateException if the tool is not found.
     */
    public void upgradeItem(Tool tool, int upgrade) {
        Node currentNode = findNode(tool);
        if (currentNode != null) {
            currentNode.tool.setGrade(currentNode.tool.getGrade() + upgrade);
            System.out.println("Grade for " + currentNode.tool.getName() + " is now " + 
                                currentNode.tool.getGrade());
        } else {
            System.out.println("Tool not found.");
        }
    }

    /**
     * Decreases the grade of a specified tool.
     *
     * @param tool The tool whose grade is to be decreased.
     * @param downgrade The amount to decrease the grade by. Throws
     * IllegalStateException if the tool is not found.
     */
    public void downgradeItem(Tool tool, int downgrade) {
        Node currentNode = findNode(tool);
        if (currentNode != null) {
            currentNode.tool.setGrade(currentNode.tool.getGrade() - downgrade);
            System.out.println("Grade for " + currentNode.tool.getName() + " is now " + 
                                currentNode.tool.getGrade());
        } else {
            System.out.println("Tool not found.");
        }
    }

    /**
     * Clears the entire list, resetting the structure to its initial state.
     * This is useful for reinitialize the tool list without creating a new
     * MultipleTool instance.
     */
    public void clear() {
        head = tail = null;
        size = 0;
    }

    /**
     * Returns the current number of tools in the list.
     *
     * @return The size of the list.
     */
    public int getSize() {
        return size;
    }

    public List<Tool> getAllTools() {
        List<Tool> tools = new ArrayList<>();
        if (head == null) {
            return tools;  
        }
        Node current = head;
        do {
            if (current != null && current.tool != null) { 
                tools.add(current.tool);  
                current = current.next;  
            }
        } while (current != head && current != null);  
        return tools;
    }

}
