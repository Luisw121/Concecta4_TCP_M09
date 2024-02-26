import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClientTCP {
    public static void main(String[] args) throws IOException {
        //Estableixem la conexió amb el servidor en loaclhost en el port 5000
        Socket socket = new Socket("localhost", 5000);
        System.out.println("Connected to server");

        //Configuració de fluixos d'entrada actual del tauler desde el servidor
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            //Recibeix i mostra l'estat actual del tauler desde el servidor
            String serverResponse = input.readLine();
            System.out.println(serverResponse);

            //Verificació si és el torn del jugador o si el joc ha terminat
            if (serverResponse.startsWith("CURRENT_PLAYER")) {
                int currentPlayer = Integer.parseInt(serverResponse.split(":")[1]);
                if (currentPlayer == 1) {
                    System.out.println("Es el teu turn (Jugador 1)");
                    System.out.print("Escriu el número de la columna en on vols tirar la ficha (del 1 al 7): ");
                    int columna = scanner.nextInt();
                    output.println(columna);
                } else if (currentPlayer == 2) {
                    System.out.println("Es el teu tu turn (Jugador 2)");
                    System.out.print("Escribe el número de la columna en on vols tirar la ficha (del 1 al 7): ");
                    int columna = scanner.nextInt();
                    output.println(columna);
                }
            } else if (serverResponse.startsWith("GAME_RESULT")) {
                //mostrem el resultat del joc enviat pel servidor
                System.out.println(serverResponse.split(":")[1]);
                break;
            } else if (serverResponse.equals("INVALID_MOVE")) {
                //mostrem un misatje de moviment invàlid enviat pel servidor
                System.out.println("Moviment invalid. Intenta-lo de nou.");
            }
        }

        socket.close();//Tanquem la conexió amb el servidor
        System.out.println("Desconectado");
    }
}
