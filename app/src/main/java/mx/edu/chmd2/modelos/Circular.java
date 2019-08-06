package mx.edu.chmd2.modelos;

public class Circular {
    private String idCircular;
    private String encabezado,nombre,textoCircular;
    private String fecha1,fecha2;
    private String estado;
    private int idCiclo,envio_todos;
    private int leida,favorita,compartida;

    public Circular(String idCircular, String nombre, String fecha1, String fecha2, String estado,
                    int idCiclo, int envio_todos) {
        this.idCircular = idCircular;
        this.nombre = nombre;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
        this.estado = estado;
        this.idCiclo = idCiclo;
        this.envio_todos = envio_todos;
    }

    public Circular(String idCircular, String nombre, String textoCircular,
                    String estado,int leida,int favorita,int compartida) {
        this.idCircular = idCircular;
        this.nombre = nombre;
        this.textoCircular = textoCircular;
        this.estado = estado;
        this.leida = leida;
        this.favorita = favorita;
        this.compartida = compartida;
    }

    //Esto es para el detalle
    public Circular(String idCircular, String nombre) {
        this.idCircular = idCircular;
        this.nombre = nombre;
    }


    //id,titulo,contenido,estatus
    //id,titulo,estatus,ciclo_escolar_id,created_at,updated_at,status_envio,envio_todos

    public Circular(String idCircular, String encabezado, String nombre,
                    String textoCircular, String fecha1, String fecha2, String estado,
                    int leida,int favorita,int compartida) {
        this.idCircular = idCircular;
        this.encabezado = encabezado;
        this.nombre = nombre;
        this.textoCircular = textoCircular;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
        this.estado = estado;
        this.leida = leida;
        this.favorita = favorita;
        this.compartida = compartida;
    }


    public int getLeida() {
        return leida;
    }

    public void setLeida(int leida) {
        this.leida = leida;
    }

    public int getFavorita() {
        return favorita;
    }

    public void setFavorita(int favorita) {
        this.favorita = favorita;
    }

    public int getCompartida() {
        return compartida;
    }

    public void setCompartida(int compartida) {
        this.compartida = compartida;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public int getEnvio_todos() {
        return envio_todos;
    }

    public void setEnvio_todos(int envio_todos) {
        this.envio_todos = envio_todos;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
