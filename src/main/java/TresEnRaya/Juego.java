package TresEnRaya;

import java.util.List;

/**
 * Inteligencia artificial Un enfoque moderno (3ª edición): página 165. <br>
 * <br>
 * Un juego puede definirse formalmente como un tipo de problema de búsqueda con
 * los siguientes elementos: <br>
 * <ul>
 * <li> S0: el estado inicial, que especifica cómo se configura el juego desde
 * el principio. </li>
 * <li> JUGADOR (es): define qué jugador tiene el movimiento en un estado. </li>
 * <li> ACCIONES (s): devuelve el conjunto de movimientos legales en un estado.
 * </li>
 * <li> RESULTADO (s, a): El modelo de transición, que define el resultado de un
 * movimiento. </li>
 * <li> TERMINAL-TEST (s): Una prueba de terminal, que es verdadera cuando el
 * juego ha terminado y falso ESTADOS TERMINALES de lo contrario. Los estados
 * donde el juego ha finalizado se denominan estados terminales. </li>
 * <li> UTILIDAD (s, p): una función de utilidad (también llamada función
 * objetivo o función de pago), define el valor numérico final para un juego que
 * termina en estados terminales s para un jugador p. En el ajedrez, el
 * resultado es una victoria, una pérdida o un empate, con valores +1, 0 o 1/2.
 * Algunos juegos tienen una variedad más amplia de resultados posibles; los
 * pagos en el backgammon van desde 0 a +192. Un juego de suma cero se define
 * (confusamente) como uno donde la recompensa total para todos los jugadores es
 * la misma para cada instancia del juego. El ajedrez es suma cero porque cada
 * juego tiene una recompensa de 0 + 1, 1 + 0 o 1/2 + 1/2. "Suma constante"
 * hubiera sido un término mejor, pero la suma cero es tradicional y tiene
 * sentido si imaginas que a cada jugador se le cobra una tarifa de entrada de
 * 1/2. </li>
 * </ul>
 *
 * @author Ruediger Lunde
 *
 * @param <ESTADO> Tipo que se usa para estados en el juego.
 * @param <ACCION> Tipo que se usa para acciones en el juego.
 * @param <JUGADOR> Tipo que se usa para los jugadores en el juego.
 */
public interface Juego<ESTADO, ACCION, JUGADOR> {

    ESTADO obtenerEstadoInicial();

    JUGADOR[] obtenerJugadores();

    JUGADOR obtenerJugador(ESTADO estado);

    List<ACCION> obtenerAcciones(ESTADO estado);

    ESTADO obtenerResultado(ESTADO estado, ACCION accion);

    boolean esTerminal(ESTADO estado);

    double obtenerUtilidad(ESTADO estado, JUGADOR jugador);
}
