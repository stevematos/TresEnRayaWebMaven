package TresEnRaya;

/**
 * Inteligencia artificial: un enfoque moderno (3ª ed.): Página 173. <br>
 *
 * <pre>
 * <code>
 * la función ALPHA-BETA-BUSQUEDA (estado) devuelve una acción
 *   v = MAX-VALOR (estado, -infinito, + infinito)
 *   devuelve la acción en ACCIONES (estado) con valor v
 *
 * la función MAX-VALUE (estado, alfa, beta) devuelve un valor de utilidad
 *   si TERMINAL-TEST (estado) devuelve UTIlIDAD (estado)
 *   v = -infinito
 *   para cada una en ACCIONES (estado) hacer
 *       v = MAX (v, VALOR MÍNIMO (RESULTADO (s, a), alfa, beta))
 *       si v> = beta entonces devuelve v
 *       alfa = MAX (alfa, v)
 *   retorno v
 *
 * la función MIN-VALOR (estado, alfa, beta) devuelve un valor de utilidad
 *   si TERMINAL-TEST (estado) devuelve UTILIDAD (estado)
 *   v = infinito
 *   para cada una en ACCIONES (estado) hacer
 *       v = MIN (v, MAX-VALOR (RESULTADO (s, a), alfa, beta))
 *       si v <= alfa entonces devuelve v
 *       beta = MIN (beta, v)
 *   retorno v
 * </code>
 * </pre>
 *
 * Figura 5.7 El algoritmo de búsqueda alfa-beta. Tenga en cuenta que estas
 * rutinas son lo mismo que las funciones MINIMAX en la Figura 5.3, excepto las
 * dos líneas en cada uno de MIN-VALOR y MAX-VALOR que mantienen alfa y beta.
 *
 * @autor Ruediger Lunde
 *
 * @param <ESTADO> Tipo que se usa para estados en el juego.
 * @param <ACCION> Tipo que se usa para acciones en el juego.
 * @param <JUGADOR> Tipo que se usa para los jugadores en el juego.
 */
public class AlphaBeta<ESTADO, ACCION, JUGADOR> implements
        BusquedaDeAdversario<ESTADO, ACCION> {

    Juego<ESTADO, ACCION, JUGADOR> juego;
    private int nodosExpandidos;

    /**
     * Crea un nuevo objeto de búsqueda para un juego dado.
     */
    public static <ESTADO, ACCION, JUGADOR> AlphaBeta<ESTADO, ACCION, JUGADOR> createFor(
            Juego<ESTADO, ACCION, JUGADOR> juego) {
        return new AlphaBeta<ESTADO, ACCION, JUGADOR>(juego);
    }

    public AlphaBeta(Juego<ESTADO, ACCION, JUGADOR> juego) {
        this.juego = juego;
    }

    @Override
    public ACCION tomarDecision(ESTADO estado) {
        nodosExpandidos = 0;
        ACCION resultado = null;
        double resultValor = Double.NEGATIVE_INFINITY;
        JUGADOR jugador = juego.obtenerJugador(estado);
        for (ACCION accion : juego.obtenerAcciones(estado)) {
            double valor = minValor(juego.obtenerResultado(estado, accion), jugador,
                    Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (valor > resultValor) {
                resultado = accion;
                resultValor = valor;
            }
        }
        return resultado;
    }

    public double maxValor(ESTADO state, JUGADOR player, double alpha, double beta) {
        nodosExpandidos++;
        if (juego.esTerminal(state)) {
            return juego.obtenerUtilidad(state, player);
        }
        double valor = Double.NEGATIVE_INFINITY;
        for (ACCION accion : juego.obtenerAcciones(state)) {
            valor = Math.max(valor, minValor(juego.obtenerResultado(state, accion), player, alpha, beta));
            if (valor >= beta) {
                return valor;
            }
            alpha = Math.max(alpha, valor);
        }
        return valor;
    }

    public double minValor(ESTADO estado, JUGADOR jugador, double alpha, double beta) {
        nodosExpandidos++;
        if (juego.esTerminal(estado)) {
            return juego.obtenerUtilidad(estado, jugador);
        }
        double valor = Double.POSITIVE_INFINITY;
        for (ACCION accion : juego.obtenerAcciones(estado)) {
            valor = Math.min(valor, maxValor(juego.obtenerResultado(estado, accion), jugador, alpha, beta));
            if (valor <= alpha) {
                return valor;
            }
            beta = Math.min(beta, valor);
        }
        return valor;
    }

    @Override
    public Metricas obtenerMetricas() {
        Metricas resultado = new Metricas();
        resultado.set("nodos expandidos", nodosExpandidos);
        return resultado;
    }
}
