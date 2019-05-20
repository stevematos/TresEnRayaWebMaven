package TresEnRaya;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementa una búsqueda minimax de profundización iterativa con poda
 * alfa-beta y orden de acción. El tiempo de cálculo máximo se especifica en
 * segundos. El algoritmo se implementa como método de plantilla y puede
 * configurarse y ajustarse por subclasificación.
 *
 * @author Ruediger Lunde
 *
 * @param <ESTADO> Tipo que se usa para estados en el juego.
 * @param <ACCION> Tipo que se usa para acciones en el juego.
 * @param <JUGADOR> Tipo que se usa para los jugadores en el juego.
 */
public class AlphaBetaProfundidadIterativa<ESTADO, ACCION, JUGADOR>
        implements BusquedaDeAdversario<ESTADO, ACCION> {

    protected Juego<ESTADO, ACCION, JUGADOR> juego;
    protected double utilMax;
    protected double utilMin;
    protected int limiteProfundidadActual;
    private boolean maxProfundidadAlcanzada;
    private long tiempoMax;
    private boolean logHabilitado;

    private int nodosExpandidos;
    private int maxProfundidad;

    /**
     * Crea un nuevo objeto de búsqueda para un juego dado.
     */
    public static <ESTADO, ACCION, JUGADOR> AlphaBetaProfundidadIterativa<ESTADO, ACCION, JUGADOR> createFor(
            Juego<ESTADO, ACCION, JUGADOR> juego, double utilMin, double utilMax,
            int tiempo) {
        return new AlphaBetaProfundidadIterativa<ESTADO, ACCION, JUGADOR>(
                juego, utilMin, utilMax, tiempo);
    }

    public AlphaBetaProfundidadIterativa(Juego<ESTADO, ACCION, JUGADOR> juego,
            double utilMin, double utilMax, int tiempo) {
        this.juego = juego;
        this.utilMin = utilMin;
        this.utilMax = utilMax;
        this.tiempoMax = tiempo * 1000; // interno: ms en lugar de s
    }

    public void setLogHabilitado(boolean b) {
        logHabilitado = b;
    }

    @Override
    public ACCION tomarDecision(ESTADO estado) {
        List<ACCION> resultados = null;
        double resultValor = Double.NEGATIVE_INFINITY;
        JUGADOR jugador = juego.obtenerJugador(estado);
        StringBuffer logText = null;
        nodosExpandidos = 0;
        maxProfundidad = 0;
        limiteProfundidadActual = 0;
        long tiempoInicio = System.currentTimeMillis();
        boolean salir = false;
        do {
            incrementarLimiteProfundidad();
            maxProfundidadAlcanzada = false;
            List<ACCION> nuevosResultados = new ArrayList<ACCION>();
            double nuevoValorDeResultado = Double.NEGATIVE_INFINITY;
            double segundoMejorValor = Double.NEGATIVE_INFINITY;
            if (logHabilitado) {
                logText = new StringBuffer("profundidad " + limiteProfundidadActual + ": ");
            }
            for (ACCION accion : ordenarAcciones(estado, juego.obtenerAcciones(estado),
                    jugador, 0)) {
                if (resultados != null
                        && System.currentTimeMillis() > tiempoInicio + tiempoMax) {
                    salir = true;
                    break;
                }
                double valor = minValor(juego.obtenerResultado(estado, accion), jugador,
                        Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1);
                if (logHabilitado) {
                    logText.append(accion + "->" + valor + " ");
                }
                if (valor >= nuevoValorDeResultado) {
                    if (valor > nuevoValorDeResultado) {
                        segundoMejorValor = nuevoValorDeResultado;
                        nuevoValorDeResultado = valor;
                        nuevosResultados.clear();
                    }
                    nuevosResultados.add(accion);
                } else if (valor > segundoMejorValor) {
                    segundoMejorValor = valor;
                }
            }
            if (logHabilitado) {
                System.out.println(logText);
            }
            if (!salir || isSignificantementeMejor(nuevoValorDeResultado, resultValor)) {
                resultados = nuevosResultados;
                resultValor = nuevoValorDeResultado;
            }
            if (!salir && resultados.size() == 1
                    && this.isSignificantementeMejor(resultValor, segundoMejorValor)) {
                break;
            }
        } while (!salir && maxProfundidadAlcanzada && !tieneGanadorSeguro(resultValor));
        return resultados.get(0);
    }

    public double maxValor(ESTADO estado, JUGADOR jugador, double alpha,
            double beta, int profundidad) { // returns an utility value
        nodosExpandidos++;
        maxProfundidad = Math.max(maxProfundidad, profundidad);
        if (juego.esTerminal(estado) || profundidad >= limiteProfundidadActual) {
            return evaluar(estado, jugador);
        } else {
            double value = Double.NEGATIVE_INFINITY;
            for (ACCION action : ordenarAcciones(estado, juego.obtenerAcciones(estado),
                    jugador, profundidad)) {
                value = Math.max(value, minValor(juego.obtenerResultado(estado, action), //
                        jugador, alpha, beta, profundidad + 1));
                if (value >= beta) {
                    return value;
                }
                alpha = Math.max(alpha, value);
            }
            return value;
        }
    }

    public double minValor(ESTADO estado, JUGADOR jugador, double alpha,
            double beta, int profundidad) { // returns an utility
        nodosExpandidos++;
        maxProfundidad = Math.max(maxProfundidad, profundidad);
        if (juego.esTerminal(estado) || profundidad >= limiteProfundidadActual) {
            return evaluar(estado, jugador);
        } else {
            double valor = Double.POSITIVE_INFINITY;
            for (ACCION accion : ordenarAcciones(estado, juego.obtenerAcciones(estado),
                    jugador, profundidad)) {
                valor = Math.min(valor, maxValor(juego.obtenerResultado(estado, accion), //
                        jugador, alpha, beta, profundidad + 1));
                if (valor <= alpha) {
                    return valor;
                }
                beta = Math.min(beta, valor);
            }
            return valor;
        }
    }

    /**
     * Retorna algunos datos estadisticos de la ultima busqueda
     */
    @Override
    public Metricas obtenerMetricas() {
        Metricas result = new Metricas();
        result.set("nodos expandidos ", nodosExpandidos);
        result.set("max profundidad", maxProfundidad);
        return result;
    }

    /**
     * Operación primitiva que se llama al comienzo de un paso de búsqueda
     * limitada en profundidad. Esta implementación incrementa el límite de
     * profundidad actual en uno.
     */
    protected void incrementarLimiteProfundidad() {
        limiteProfundidadActual++;
    }

    /**
     * Operación primitiva que se utiliza para detener la búsqueda de
     * profundización iterativa en situaciones donde existe una acción clara y
     * óptima. Esta implementación devuelve siempre falso.
     */
    protected boolean isSignificantementeMejor(double nuevaUtilidad, double utilidad) {
        return false;
    }

    /**
     * Operación primitiva que se utiliza para detener la búsqueda de
     * profundización iterativa en situaciones en las que se ha identificado un
     * ganador seguro. Esta implementación devuelve verdadero si el valor dado
     * (para el resultado de acción preferido actualmente) es el valor de
     * utilidad más alto o más bajo posible.
     */
    protected boolean tieneGanadorSeguro(double utilidadResultante) {
        return utilidadResultante <= utilMin || utilidadResultante >= utilMax;
    }

    /**
     * Operación primitiva, que estima el valor para los estados (no
     * necesariamente terminales). Esta implementación devuelve el valor de
     * utilidad para estados de terminal y <code> (utilMin + utilMax) / 2
     * </code> para estados no terminales.
     */
    protected double evaluar(ESTADO estado, JUGADOR jugador) {
        if (juego.esTerminal(estado)) {
            return juego.obtenerUtilidad(estado, jugador);
        } else {
            maxProfundidadAlcanzada = true;
            return (utilMin + utilMax) / 2;
        }
    }

    /**
     * Operación primitiva para ordenar la acción. Esta implementación conserva
     * el orden original (proporcionado por el juego).
     */
    public List<ACCION> ordenarAcciones(ESTADO estado, List<ACCION> acciones,
            JUGADOR jugador, int profundidad) {
        return acciones;
    }
}
