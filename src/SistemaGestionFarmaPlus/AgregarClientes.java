package SistemaGestionFarmaPlus;

import java.util.HashMap;
import java.util.Map;

public class AgregarClientes {
	String DNI, Nombre, Apellidos, Nacimiento, Telefono, Direccion; 
	
	public AgregarClientes(String Nombre, String Apellidos,
			String Nacimiento, String Telefono, String Direccion) {
		
		this.Nombre = Nombre;
		this.Apellidos = Apellidos; 
		this.Nacimiento = Nacimiento;
		this.Telefono = Telefono; 
		this.Direccion = Direccion;
	}
	
	Map<String, AgregarClientes> registros = new HashMap<>();
	
	public void IngresarRegistro () {
		
	}
	
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
