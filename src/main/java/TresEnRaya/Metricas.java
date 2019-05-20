package TresEnRaya;

import java.util.Hashtable;
import java.util.Set;

/**
 * Almacena pares clave-valor para el análisis de eficiencia.
 *
 * @author Ravi Mohan
 * @author Ruediger Lunde
 */
public class Metricas {

    private Hashtable<String, String> hash;

    public Metricas() {
        this.hash = new Hashtable<String, String>();
    }

    public void set(String nombre, int i) {
        hash.put(nombre, Integer.toString(i));
    }

    public void set(String nombre, double d) {
        hash.put(nombre, Double.toString(d));
    }

    public void set(String nombre, long l) {
        hash.put(nombre, Long.toString(l));
    }

    public int getInt(String nombre) {
        return new Integer(hash.get(nombre)).intValue();
    }

    public double getDouble(String nombre) {
        return new Double(hash.get(nombre)).doubleValue();
    }

    public long getLong(String nombre) {
        return new Long(hash.get(nombre)).longValue();
    }

    public String get(String nombre) {
        return hash.get(nombre);
    }

    public Set<String> keySet() {
        return hash.keySet();
    }

    public String toString() {
        return hash.toString();
    }
}
