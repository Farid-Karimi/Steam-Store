package Server;

import Shared.GameHandler;
import Shared.Game;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ServerMain {
    private int port;
    private ArrayList<ClientService> clients = new ArrayList<>();
    private ServerSocket serverSocket;
    private Database database;

    public ServerMain(int port) {
        this.port = port;
        database = new Database();
    }

    public void runServer() throws SQLException, IOException {
        // Connecting to database
        try {
            database.connect();
            System.out.println("Connected to database!");
        } catch (SQLException e) {
            System.out.println("Unable to connect to database!");
            e.printStackTrace();
        }

        importData();

        // Creating server socket
        System.out.println("Starting server socket on port " + port);
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Server started!\nListening on port " + port + " ...");
        while (true){
            try {
                Socket socket = serverSocket.accept();
                ClientService clientService = new ClientService(socket, database);
                System.out.println("New client connected: " + socket.getRemoteSocketAddress());
                clients.add(clientService);
                Thread thread = new Thread(clientService);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void importData() throws IOException, SQLException {
        String path = "src/main/java/Server/Resources/";
        File listFiles = new File(path);

        for(File file : listFiles.listFiles()){
            if(file.getName().equals("imported")){
                System.out.println("Data already imported!");
                return;
            }
        }

        Set<String> files = new HashSet<>();
        for(String string : listFiles.list()){
            files.add(string.substring(0,string.length()-4));
        }
        files.remove(".git");
        System.out.println(files);

        for(String filename : files){
            File file = new File(path + filename + ".txt");
            Scanner scanner = new Scanner(file);

            int id = scanner.nextInt(); scanner.nextLine();
            String title = scanner.nextLine();
            String developer = scanner.nextLine();
            String genre = scanner.nextLine();
            int price = (int) scanner.nextFloat();
            int release_year = scanner.nextInt(); scanner.nextLine();
            String controller_support = scanner.nextLine();
            int reviews = scanner.nextInt();
            int size = scanner.nextInt();

            System.out.println("importing: " + title);

            Game game = new Game();

            game.setId(id);
            game.setTitle(title);
            game.setDeveloper(developer);
            game.setGenre(genre);
            game.setPrice(price);
            game.setReleaseYear(release_year);
            game.setControllerSupport(controller_support.equals("True"));
            game.setReviews(reviews);
            game.setSize(size);
            game.setPath(path + filename + ".png");

            GameHandler gameHandler = new GameHandler(database);
            gameHandler.createGame(game);
        }

        File file = new File(path + "imported");
        file.createNewFile();
    }

    public static void main(String[] args) throws SQLException, IOException {
        ServerMain serverMain = new ServerMain(8000);
        serverMain.runServer();
    }
}
