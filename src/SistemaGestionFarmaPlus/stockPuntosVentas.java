package SistemaGestionFarmaPlus;

import java.util.HashMap;
import java.util.Map;

public class stockPuntosVentas {
	
	String nombre; 
	Map<String, Integer> inventario;
	
	public stockPuntosVentas(String nombre) {
		this.nombre = nombre;
		this.inventario = new HashMap<>();
	}
	
	public void NuevoIngreso (String nombreMed, int stock) {
		inventario.put(nombreMed, stock);
	}
	
	
	public static void main(String[] args) {
		

	}

}
