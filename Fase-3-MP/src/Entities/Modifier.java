package Entities;

public class Modifier {

    /**A continuación se definen atributos**/
    private String name;
    private int value;

    /**A continuación se define el constructor**/
    public Modifier() {}

    /**A continuación se definen Getters y Setters**/
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }
    public void setValue(int value) {
        this.value = value;
    }
}
