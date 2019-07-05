package mx.edu.chmd2.modelos;

public class Circular {
    private String idCircular;
    private String encabezado,nombre,textoCircular;
    private String fecha1,fecha2;
    private int estado;

    public Circular(String idCircular, String encabezado, String nombre,
                    String textoCircular, String fecha1, String fecha2, int estado) {
        this.idCircular = idCircular;
        this.encabezado = encabezado;
        this.nombre = nombre;
        this.textoCircular = textoCircular;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
        this.estado = estado;
    }

    public String getEncabezado() {
        return encabezado;
    }

    public void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdCircular() {
        return idCircular;
    }

    public void setIdCircular(String idCircular) {
        this.idCircular = idCircular;
    }

    public String getTextoCircular() {
        return textoCircular;
    }

    public void setTextoCircular(String textoCircular) {
        this.textoCircular = textoCircular;
    }

    public String getFecha1() {
        return fecha1;
    }

    public void setFecha1(String fecha1) {
        this.fecha1 = fecha1;
    }

    public String getFecha2() {
        return fecha2;
    }

    public void setFecha2(String fecha2) {
        this.fecha2 = fecha2;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
