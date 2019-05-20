package TresEnRaya;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Un estado del juego de Tic-tac-toe se caracteriza por un tablero que contiene
 * los símbolos X y O, el siguiente jugador a mover y una información de
 * utilidad.
 *
 * @author Ruediger Lunde
 *
 */
public class Estados implements Cloneable {

    public static final String O = "O";
    public static final String X = "X";
    public static final String VACIO = "-";
    //
    private String[] board = new String[]{VACIO, VACIO, VACIO, VACIO, VACIO,
        VACIO, VACIO, VACIO, VACIO};

    private String primerJugador = X;
    private double utilidad = -1; // 1: gana X, 0: gana O, 0.5: empate

    public String getPlayerToMove() {
        return primerJugador;
    }

    public boolean isEmpty(int columna, int fila) {
        return getBoard()[getAbsPosition(columna, fila)] == VACIO;
    }

    public String getValue(int columna, int fila) {
        return getBoard()[getAbsPosition(columna, fila)];
    }

    public double getUtility() {
        return utilidad;
    }

    public void mark(UbicacionXY accion) {
        marcar(accion.getCoordenadaX(), accion.getCoordenadaY());
    }

    public void marcar(int columna, int fila) {
        if (utilidad == -1 && getValue(columna, fila) == VACIO) {
            board[getAbsPosition(columna, fila)] = primerJugador;
            analizarUtilidad();
            setPrimerJugador(primerJugador == X ? O : X);
        }
    }

    private void analizarUtilidad() {
        if (lineaEnTablero()) {
            utilidad = (primerJugador == X ? 1 : 0);
        } else if (obtenerNumerodePosicionesMarcadas() == 9) {
            utilidad = 0.5;
        }
    }

    public boolean lineaEnTablero() {
        return (algunaFilaCompleta() || algunaColumnaCompleta() || algunaDiagonalCompleta());
    }

    private boolean algunaFilaCompleta() {
        for (int fila = 0; fila < 3; fila++) {
            String val = getValue(0, fila);
            if (val != VACIO && val == getValue(1, fila) && val == getValue(2, fila)) {
                return true;
            }
        }
        return false;
    }

    private boolean algunaColumnaCompleta() {
        for (int columna = 0; columna < 3; columna++) {
            String val = getValue(columna, 0);
            if (val != VACIO && val == getValue(columna, 1) && val == getValue(columna, 2)) {
                return true;
            }
        }
        return false;
    }

    private boolean algunaDiagonalCompleta() {
        boolean retVal = false;
        String val = getValue(0, 0);
        if (val != VACIO && val == getValue(1, 1) && val == getValue(2, 2)) {
            return true;
        }
        val = getValue(0, 2);
        if (val != VACIO && val == getValue(1, 1) && val == getValue(2, 0)) {
            return true;
        }
        return retVal;
    }

    public int obtenerNumerodePosicionesMarcadas() {
        int retVal = 0;
        for (int columna = 0; columna < 3; columna++) {
            for (int fila = 0; fila < 3; fila++) {
                if (!(isEmpty(columna, fila))) {
                    retVal++;
                }
            }
        }
        return retVal;
    }

    public List<UbicacionXY> obtenerPosicionesNoMarcadas() {
        List<UbicacionXY> result = new ArrayList<UbicacionXY>();
        for (int columna = 0; columna < 3; columna++) {
            for (int fila = 0; fila < 3; fila++) {
                if (isEmpty(columna, fila)) {
                    result.add(new UbicacionXY(columna, fila));
                }
            }
        }
        return result;
    }

    @Override
    public Estados clone() {
        Estados copia = null;
        try {
            copia = (Estados) super.clone();
            copia.setBoard(Arrays.copyOf(getBoard(), getBoard().length));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace(); // nunca deberia pasar...
        }
        return copia;
    }

    @Override
    public boolean equals(Object unObjeto) {
        if (unObjeto != null && unObjeto.getClass() == getClass()) {
            Estados otroEstado = (Estados) unObjeto;
            for (int i = 0; i < 9; i++) {
                if (getBoard()[i] != otroEstado.getBoard()[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        // Debe asegurarse de que los objetos iguales tengan códigos
        // hash equivalentes (Número 77).
        return toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                strBuilder.append(getValue(columna, fila) + " ");
            }
            strBuilder.append("\n");
        }
        return strBuilder.toString();
    }

    //
    // Metodos Privados
    //
    private int getAbsPosition(int col, int row) {
        return row * 3 + col;
    }

    /**
     * @param board the board to set
     */
    public void setBoard(String[] board) {
        this.board = board;
    }

    /**
     * @return the board
     */
    public String[] getBoard() {
        return board;
    }

    /**
     * @param primerJugador the primerJugador to set
     */
    public void setPrimerJugador(String primerJugador) {
        this.primerJugador = primerJugador;
    }
}
