package Entities;

import java.util.ArrayList;

public class Demon extends MinionsComposit{

    /**A continuación se definen los atributos**/
    private String description;
    private ArrayList<MinionsComposit> minionsComposits;

    /**A continuación se definen las operaciones**/
    public String getDescription() {
        return description;
    }
    public void setDescripcion(String descripcion) {
        this.description = descripcion;
    }

    public ArrayList<MinionsComposit> getMinionsComposits() {
        return minionsComposits;
    }
    public void setMinionsComposites(ArrayList<MinionsComposit> minionsComposits) {
        this.minionsComposits = minionsComposits;
    }
}
