package TresEnRaya;

import java.util.List;

/**
 * Proporciona una implementación del juego Tres en Raya que se puede usar para
 * experimentos con el algoritmo Minimax.
 *
 * @author Ruediger Lunde
 *
 */
public class Jugar implements Juego<Estados, UbicacionXY, String> {

    Estados initialState = new Estados();

    @Override
    public Estados obtenerEstadoInicial() {
        return initialState;
    }

    @Override
    public String[] obtenerJugadores() {
        return new String[]{Estados.X, Estados.O};
    }

    @Override
    public String obtenerJugador(Estados estado) {
        return estado.getPlayerToMove();
    }

    @Override
    public List<UbicacionXY> obtenerAcciones(Estados estado) {
        return estado.obtenerPosicionesNoMarcadas();
    }

    @Override
    public Estados obtenerResultado(Estados estado, UbicacionXY accion) {
        Estados result = estado.clone();
        result.mark(accion);
        return result;
    }

    @Override
    public boolean esTerminal(Estados estado) {
        return estado.getUtility() != -1;
    }

    @Override
    public double obtenerUtilidad(Estados estado, String jugador) {
        double resultado = estado.getUtility();
        if (resultado != -1) {
            if (jugador == Estados.O) {
                resultado = 1 - resultado;
            }
        } else {
            throw new IllegalArgumentException("No es estado terminal.");
        }
        return resultado;
    }
}
