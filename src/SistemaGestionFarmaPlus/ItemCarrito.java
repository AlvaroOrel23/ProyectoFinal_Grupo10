package SistemaGestionFarmaPlus;

public class ItemCarrito {
	int id;
	String nombre;
	int cantidad;
	double precioUnitario;
	
	public ItemCarrito (int id, String nombre, double precioUnitario ) {
		this.id = id;
		this.nombre = nombre;
		this.precioUnitario = precioUnitario;
		this.cantidad = 1;
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
