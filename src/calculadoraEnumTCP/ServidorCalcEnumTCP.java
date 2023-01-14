package calculadoraEnumTCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author oscar
 */
public class ServidorCalcEnumTCP {
    /*
    UTILIZAREMOS UN TIPO ENUMERADO PARA LAS POSIBLES OPERACIONES QUE PUEDE HACER EL SERVIDOR
    Un objeto de un tipo de enumeración sólo puede llegar a contener los valores definidos por la lista. 
    Son una manera de definir un nuevo tipo de datos que tiene un número fijo de valores que se consideran validos.
    En Java, las enumeración se denominan enum.
    IMPORTANTE: 
     Las enumeraciones en java son realmente una clase, en particular que hereda de la clase Enum (java.lang.Enum).
     Por tanto tienen una serie de métodos heredados de esa clase padre (por ejemplo: toString,name, values..)     
     La única limitación que tienen los enumerados respecto a una clase normal es que si tiene constructor, 
     este debe de ser privado para que no se puedan crear nuevos objetos.
    */
    enum OPERADOR { SUMA, RESTA, MULT, DIV };
    public static final int NUM_PUERTO = 8888;


    /**
     * @param args the command linea arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket socketServidor = new ServerSocket(NUM_PUERTO);
        System.out.println("... el servidor acepta peticiones.");

        while (true) {
            Socket socket = socketServidor.accept();
            // Capturamos el mensaje enviado por el cliente
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String linea = reader.readLine();

            if (linea.trim().startsWith("quit")) {
                System.out.println("... servidor apagandose ...");
                socket.close();
                break;
            } else {
                // Procesamos el mensaje enviado por el cliente
                procesaPeticion(socket, linea);
            }
        }

        System.out.println("... cerrando los sockets del servidor ...");
        socketServidor.close();
    }

    private static void procesaPeticion(Socket socket, String line) throws IOException, InterruptedException {
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        
        // Hacemos una primera division del mensaje del cliente, usando el caracter espacio
        String[] tokens = line.split(" ");

        if (tokens.length != 2) {
            pw.println("comando invalido: " + line);
            socket.close();
            return;
        } 
        
        // Hacemos una segunda division de la parte del mensaje del cliente, 
        // a la derecha del caracter espacio, usando el caracter coma,
        // para identificar a los operandos
        String[] operandos = tokens[1].split(",");

        if (operandos.length != 2) {
            pw.println("comando invalido: " + line);
            socket.close();
            return;
        } 

        String operador = tokens[0].trim();

        try {
            Double operand1 = Double.valueOf(operandos[0].trim());
            Double operand2 = Double.valueOf(operandos[1].trim());
            double resultado = 0;
            // Accedemos al enumerado para determinar el calculo a realizar
            OPERADOR  op = OPERADOR.valueOf(operador.toUpperCase());
            switch (op) {
            case SUMA:
                resultado = operand1 + operand2;
                break;
            case RESTA:
                resultado = operand1 - operand2;
                break;
            case MULT:
                resultado = operand1 * operand2;
                break;
            case DIV:
                resultado = operand1 / operand2;
                break;
            default:
                pw.println("operando invalido: " + line);
                pw.flush();
                socket.close();
                return;
            }
            pw.println(resultado);
        } catch (NumberFormatException nfe) {
            pw.println("operando invalido: " + line);
        }

        pw.flush();
        socket.close();
    }
    
}
