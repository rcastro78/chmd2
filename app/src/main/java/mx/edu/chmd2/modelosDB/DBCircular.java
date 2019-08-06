package mx.edu.chmd2.modelosDB;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

//String idCircular, String nombre, String textoCircular, String estado
@Table(name = "appCircular", id="id")
public class DBCircular extends Model {
    @Column(name="idCircular",unique = true)
    public String idCircular;
    @Column(name="nombre")
    public String nombre;
    @Column(name="textoCircular")
    public String textoCircular;
    @Column(name="estado")
    public String estado;
}
