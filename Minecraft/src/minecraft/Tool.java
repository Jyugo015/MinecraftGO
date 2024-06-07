/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

import java.sql.SQLException;

/**
 *
 * @author Asus
 */
public class Tool{
    private String name; 
    private String type; 
    private String function; 
    private int grade;
    private int toolId;
    private int quantity;

    public Tool(String name, String type, String function, int grade, int toolId) {
        this.name = name;
        this.type = type;
        this.function = function;
        this.grade = grade;
        this.toolId = toolId;
    }

    public Tool(String name, String type, String function, int grade) {
        this.name = name;
        this.type = type;
        this.function = function;
        this.grade = grade;
    }

    public Tool(Tool tool, int toolId){
        this.name = tool.getName();
        this.type = tool.getType();
        this.function = tool.getFunction();
        this.grade = tool.getGrade();
        this.toolId = toolId;
    }

    public Tool(String toolname) throws SQLException{
        this.name = toolname;
        this.type = database_itemBox.retrieveType(toolname);
        this.function = database_itemBox.retrieveFunction(toolname);
        this.quantity = database_itemBox.retrieveQuantity(toolname, "defaultUser");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getToolId() {
        return toolId;
    }

    public void setToolId(int toolId) {
        this.toolId = toolId;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("%-18s",name) + " Grade:" + grade;
    }
}
