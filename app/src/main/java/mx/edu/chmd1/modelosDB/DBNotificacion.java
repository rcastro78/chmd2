package mx.edu.chmd1.modelosDB;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "appNotificacion", id="id")
public class DBNotificacion extends Model {
    @Column(name="idCircular")
    public String idCircular;
    @Column(name="titulo")
    public String titulo;
    @Column(name="descripcion")
    public String descripcion;
    @Column(name="tipo")
    public String tipo;
    @Column(name="recibido")
    public String recibido;
    @Column(name="estado")
    public int estado;

}
