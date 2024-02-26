import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClientTCP {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);
        System.out.println("Connected to server");

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Receive and print the board state from the server
            String serverResponse = input.readLine();
            System.out.println(serverResponse);

            // Check if it's the player's turn or if the game has ended
            if (serverResponse.startsWith("CURRENT_PLAYER")) {
                int currentPlayer = Integer.parseInt(serverResponse.split(":")[1]);
                if (currentPlayer == 1) {
                    System.out.println("Es tu turno (Jugador 1)");
                    System.out.print("Escribe el número de la columna donde quieres tirar la ficha (del 1 al 7): ");
                    int columna = scanner.nextInt();
                    output.println(columna);
                } else if (currentPlayer == 2) {
                    System.out.println("Es tu turno (Jugador 2)");
                    System.out.print("Escribe el número de la columna donde quieres tirar la ficha (del 1 al 7): ");
                    int columna = scanner.nextInt();
                    output.println(columna);
                }
            } else if (serverResponse.startsWith("GAME_RESULT")) {
                System.out.println(serverResponse.split(":")[1]);
                break;
            } else if (serverResponse.equals("INVALID_MOVE")) {
                System.out.println("Movimiento inválido. Intenta de nuevo.");
            }
        }

        socket.close();
        System.out.println("Desconectado");
    }
}
