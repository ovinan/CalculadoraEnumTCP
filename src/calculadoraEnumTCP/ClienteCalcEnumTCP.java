package calculadoraEnumTCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author oscar
 */
public class ClienteCalcEnumTCP {
    
    public static void main(String[] args) throws UnknownHostException, IOException {
        System.out.println("Introduce la operacion a realizar, separando los operandos por comas.");
        System.out.println("Introduce la palabra quit para salir (y cerrar tambien el servidor).");
        System.out.println("Ejemplos: SUMA 5,6    o    RESTA 8,2");
        Scanner teclado = new Scanner(System.in);
        String operacion = teclado.nextLine();
        
        // Creamos un nuevo objeto socket,conectado al servidor
        Socket socket = new Socket("localhost", ServidorCalcEnumTCP.NUM_PUERTO);

        // Obtenemos el input stream del socket y abrimos un BufferedReader en el 
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Obtenemos el output stream del socket, y abrimos un buffer PrintWriter en el
        PrintWriter pw = new PrintWriter(socket.getOutputStream());

        // Le pasamos al servidor la operacion a realizar
        pw.println(operacion);
        pw.flush();

        if (!"quit".equals(operacion.trim())) {
            // Obtenemos el resultado del servidor
            String line = reader.readLine();
            reader.close();
            System.out.println("Resultado: " + line);
        }
        socket.close();
    }    
}
