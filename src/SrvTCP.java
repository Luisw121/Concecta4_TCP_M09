import java.net.*;
import java.io.*;
import java.util.Scanner;

public class SrvTCP {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server is listening on port 5000");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Nuevo cliente conectado");

            // Lógica del Juego
            char[][] tablero = new char[6][7];
            int jugador_actual = 1;
            boolean fin_juego = false;

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            while (!fin_juego) {
                // Send the board state to the client
                output.println("BOARD_STATE:" + boardToString(tablero));
                output.println("CURRENT_PLAYER:" + jugador_actual);

                // Get the player's move from the client
                String columnInput = input.readLine();
                int columna = Integer.parseInt(columnInput) - 1;

                // Place the player's piece on the board
                int fila = 5;
                while (tablero[fila][columna] != '\u0000') {
                    fila--;
                    if (fila < 0) {
                        //Si la columna esta plena li comuniquem al client(jugador)
                        output.println("INVALID_MOVE");
                        continue;
                    }
                }
                tablero[fila][columna] = (jugador_actual == 1) ? 'X' : 'O';

                //Verificació de que el joc ha terminat
                if (checkWin(tablero, fila, columna)) {
                    output.println("GAME_RESULT:WIN," + jugador_actual);
                    fin_juego = true;
                } else if (tablero_lleno(tablero)) {
                    output.println("GAME_RESULT:DRAW");
                    fin_juego = true;
                } else {
                    // Switch to the next player
                    jugador_actual = (jugador_actual == 1) ? 2 : 1;
                }
            }

            socket.close();
        }
    }
    //Metode per poder comprobar la victoria
    private static boolean checkWin(char[][] tablero, int fila, int columna) {
        char jugador = tablero[fila][columna];
        int consecutivas;

        //Aqui fem la comprovació en horizontal
        consecutivas = 0;
        for (int j = 0; j < 7; j++) {
            if (tablero[fila][j] == jugador) {
                consecutivas++;
                if (consecutivas == 4) return true;
            } else {
                consecutivas = 0;
            }
        }

        //Aqui fem la comprovació en horizontal
        consecutivas = 0;
        for (int i = 0; i < 6; i++) {
            if (tablero[i][columna] == jugador) {
                consecutivas++;
                if (consecutivas == 4) return true;
            } else {
                consecutivas = 0;
            }
        }

        //Aqui fem la comprovació en diagonal
        int startRow = fila - Math.min(fila, columna);
        int startColumn = columna - Math.min(fila, columna);
        consecutivas = 0;
        while (startRow < 6 && startColumn < 7) {
            if (tablero[startRow][startColumn] == jugador) {
                consecutivas++;
                if (consecutivas == 4) return true;
            } else {
                consecutivas = 0;
            }
            startRow++;
            startColumn++;
        }

        //Aqui fem la comprovació en diagonal
        startRow = fila + Math.min(5 - fila, columna);
        startColumn = columna - Math.min(5 - fila, columna);
        consecutivas = 0;
        while (startRow >= 0 && startColumn < 7) {
            if (tablero[startRow][startColumn] == jugador) {
                consecutivas++;
                if (consecutivas == 4) return true;
            } else {
                consecutivas = 0;
            }
            startRow--;
            startColumn++;
        }

        return false;
    }

    private static boolean tablero_lleno(char[][] tablero) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (tablero[i][j] == '\u0000') {
                    return false;
                }
            }
        }
        return true;
    }


    private static String boardToString(char[][] tablero) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                sb.append((tablero[i][j] == '\u0000') ? "-" : tablero[i][j]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}