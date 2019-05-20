/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import TresEnRaya.AlphaBeta;
import TresEnRaya.AlphaBetaProfundidadIterativa;
import TresEnRaya.BusquedaDeAdversario;
import TresEnRaya.Estados;
import TresEnRaya.Juego;
import TresEnRaya.Jugar;
import TresEnRaya.Metricas;
import TresEnRaya.Minimax;
import TresEnRaya.UbicacionXY;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.json.JsonArray;
import javax.json.stream.JsonParser;
import org.json.JSONArray;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author steve
 */
public class servletTresEnRaya extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        if(br != null){
            json = br.readLine();
        }
        
	
        Estados estadoActual = (Estados) request.getSession().getAttribute("estadoActual"); 
        if (estadoActual == null) {
          estadoActual = new Estados();
        }

        
        
        JSONObject obj = new JSONObject(json);
        
        Boolean reset = obj.getBoolean("reset");
        
        String respuesta="Comenzando";
        
        if (!reset) {
            Boolean jugandoAutomatico = obj.getBoolean("auto");
            JSONArray valores = obj.getJSONArray("data");
            String aux = "";
            ArrayList<String> valoresAux = new ArrayList<>();
            for (int i = 0; i < valores.length(); i++){
                aux = (String)valores.getString(i);
                if ("".equals(aux))
                    valoresAux.add("-");
                else
                    valoresAux.add(aux);
            }

            String[] valoresData = (String[]) valoresAux.toArray(new String[valoresAux.size()]);

            Jugar juego = new Jugar();
            Metricas buscarMetricas = null;
            if (jugandoAutomatico){
                String tipo_algoritmo = obj.getString("type");
                BusquedaDeAdversario<Estados, UbicacionXY> busqueda;
                UbicacionXY accion;
                switch (tipo_algoritmo) {
                    case "max_min":
                        busqueda = Minimax.createFor(juego);
                        break;
                    case "alpha_beta":
                        busqueda = AlphaBeta.createFor(juego);
                        break;
                    case "aplha_beta_iterativo":
                        busqueda = AlphaBetaProfundidadIterativa.createFor(juego, 0.0,
                                1.0, 1000);
                        break;
                    default:
                        busqueda = Minimax.createFor(juego);
                }
                accion = busqueda.tomarDecision(estadoActual);
                buscarMetricas = busqueda.obtenerMetricas();
                estadoActual = juego.obtenerResultado(estadoActual, accion);
            }else{
                int ubicacion = 0;
                for (int i = 0; i < estadoActual.getBoard().length; i++) {
                    if (!estadoActual.getBoard()[i].equals(valoresData[i])){
                        ubicacion=i;
                        break;
                    }

                }
                estadoActual = juego.obtenerResultado(estadoActual,
                            new UbicacionXY(ubicacion % 3, ubicacion / 3));
                
            }
            respuesta = "Siguiente Movimiento: " + juego.obtenerJugador(estadoActual);
            if (buscarMetricas != null) {
                respuesta += "  ,  " + buscarMetricas.toString().substring(1, buscarMetricas.toString().length()-1);
            }
        }else
            estadoActual = new Estados();
        request.getSession().setAttribute("estadoActual", estadoActual);
        
        
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(Arrays.toString(estadoActual.getBoard())+";"+respuesta);
        response.getWriter().flush();
        response.getWriter().close();
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
