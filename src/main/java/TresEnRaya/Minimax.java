package TresEnRaya;

/**
 * Inteligencia artificial Un enfoque moderno (3ª edición): página 169. <br>
 * <pre>
 * <code>
 * La función MINIMAX-DECISIÓN (estado) devuelve una acción
 *    devolver argmax_ [a en ACCIONES (s)] MIN-VALOR (RESULTADO (estado, a))
 *
 * La función MAX-VALOR (estado) devuelve un valor de utilidad
 *    si TERMINAL-TEST (estado) devuelve UTILIDAD (estado)
 *    v = -infinito
 *    para cada una en ACCIONES (estado) hacer
 *      v = MAX (v, MIN-VALOR (RESULTADO (s, a)))
 *    volver v
 *
 * La función MIN-VALOR (estado) devuelve un valor de utilidad
 *    si TERMINAL-TEST (estado) devuelve UTILIDAD (estado)
 *      v = infinito
 *      para cada una en ACCIONES (estado) hacer
 *        v = MIN (v, MAX-VALOR (RESULTADO (s, a)))
 *    volver v
 * </code>
 * </pre>
 *
 * Figura 5.3 Un algoritmo para el cálculo de decisiones minimax. Devuelve la
 * acción correspondiente al mejor movimiento posible, es decir, el movimiento
 * que conduce al resultado con la mejor utilidad, bajo el supuesto de que el
 * oponente juega para minimizar la utilidad. Las funciones MAX-VALUE y
 * MIN-VALUE van a través de todo el árbol del juego, hasta las hojas, para
 * determinar el valor respaldado de un estado. La notación argmax_ [a en S] f
 * (a) calcula el elemento a del conjunto S que tiene el valor máximo de f (a).
 *
 *
 * @author Ruediger Lunde
 *
 * @param <ESTADO> Tipo que se usa para estados en el juego.
 * @param <ACCION> Tipo que se usa para acciones en el juego.
 * @param <JUGADOR> Tipo que se usa para los jugadores en el juego.
 */
public class Minimax<ESTADO, ACCION, JUGADOR> implements
        BusquedaDeAdversario<ESTADO, ACCION> {

    private Juego<ESTADO, ACCION, JUGADOR> juego;
    private int nodosExpandidos;

    /**
     * Crea un nuevo objeto de busqueda para un juego dado.
     */
    public static <ESTADO, ACCION, JUGADOR> Minimax<ESTADO, ACCION, JUGADOR> createFor(
            Juego<ESTADO, ACCION, JUGADOR> juego) {
        return new Minimax<ESTADO, ACCION, JUGADOR>(juego);
    }

    public Minimax(Juego<ESTADO, ACCION, JUGADOR> juego) {
        this.juego = juego;
    }

    @Override
    public ACCION tomarDecision(ESTADO estado) {
        nodosExpandidos = 0;
        ACCION resultado = null;
        double resultValue = Double.NEGATIVE_INFINITY;
        JUGADOR jugador = juego.obtenerJugador(estado);
        for (ACCION accion : juego.obtenerAcciones(estado)) {
            double valor = minValor(juego.obtenerResultado(estado, accion), jugador);
            if (valor > resultValue) {
                resultado = accion;
                resultValue = valor;
            }
        }
        return resultado;
    }

    public double maxValor(ESTADO estado, JUGADOR jugador) { // retorna un valor 
        //utilitario
        nodosExpandidos++;
        if (juego.esTerminal(estado)) {
            return juego.obtenerUtilidad(estado, jugador);
        }
        double value = Double.NEGATIVE_INFINITY;
        for (ACCION action : juego.obtenerAcciones(estado)) {
            value = Math.max(value,
                    minValor(juego.obtenerResultado(estado, action), jugador));
        }
        return value;
    }

    public double minValor(ESTADO estado, JUGADOR jugador) { // retorna un valor 
        //utilitario
        nodosExpandidos++;
        if (juego.esTerminal(estado)) {
            return juego.obtenerUtilidad(estado, jugador);
        }
        double valor = Double.POSITIVE_INFINITY;
        for (ACCION accion : juego.obtenerAcciones(estado)) {
            valor = Math.min(valor,
                    maxValor(juego.obtenerResultado(estado, accion), jugador));
        }
        return valor;
    }

    @Override
    public Metricas obtenerMetricas() {
        Metricas resultado = new Metricas();
        resultado.set("nodosExpandidos", nodosExpandidos);
        return resultado;
    }
}
