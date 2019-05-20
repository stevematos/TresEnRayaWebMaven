package TresEnRaya;

/**
 * Variante de la interfaz de búsqueda. Dado que los jugadores solo pueden
 * controlar el siguiente Movimiento, el método <code> tomarDecision </code>
 * devuelve solo una acción, no una secuencia de acciones.
 *
 * @author Ruediger Lunde
 */
public interface BusquedaDeAdversario<ESTADO, ACCION> {

    /**
     * Devuelve la acción que parece ser la mejor en el estado dado.
     */
    ACCION tomarDecision(ESTADO estado);

    /**
     * Retorna todas las metricas de la busqueda.
     *
     * @return all the metrics of the search.
     */
    Metricas obtenerMetricas();
}
