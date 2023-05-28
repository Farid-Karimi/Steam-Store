package Server;

import Shared.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler implements Runnable{
    private ObjectOutputStream objectOutput;
    private ObjectInputStream objectInput;
    private boolean isLoggedIn = false;
    private User loggedInUser = null;
    private BufferedReader input;
    private PrintWriter output;
    private Database database;
    Socket socket;



    public ClientHandler(Socket socket, Database database) throws IOException {
        this.socket = socket;
        this.database = database;

        // Initializing in and out streams
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            objectInput = new ObjectInputStream(socket.getInputStream());
            objectOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        // Waiting for request from client
        Request r;
        try {
            while ((r = (Request) objectInput.readObject()) != null){
                manageRequest(r);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection Lost!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void manageRequest(Request request) throws IOException, SQLException {
        String command = request.getCommand();
        Response response = new Response();

        // remove line below
        if(isLoggedIn){
            GameHandler crudGame = new GameHandler(database);
            if(command.equals("logout")){
                loggedInUser = null;
                isLoggedIn = false;
                response.setMessage("Logged out!");
            }

            else if(command.equals("showCatalog")){
                List<Game> games = crudGame.getAllGames();
                System.out.println("all games:");
                System.out.println(games);
                Map<String, Object> gamesMap = new HashMap<>();

                for(Game game : games){
                    Map<String, Object> gameMap = new HashMap<>();
                    gameMap.put("title", game.getTitle());
                    gameMap.put("developer", game.getDeveloper());
                    gameMap.put("genre", game.getGenre());
                    gameMap.put("id", game.getId());
                    gameMap.put("price", game.getPrice());
                    gameMap.put("reviews", game.getReviews());
                    gameMap.put("release_year", game.getReleaseYear());
                    gameMap.put("path_file", game.getPath());
                    gameMap.put("size", game.getSize());

                    gamesMap.put(game.getTitle(), gameMap);
                }

                Gson gson = new Gson();
                String a = gson.toJson(gamesMap);
                response.setMessage("games sent!");
                response.setJson(a);
                objectOutput.writeObject(response);
                objectOutput.flush();
            }

            else if (command.equals("viewGame")) {
                String rawJSON = request.getJson();
                Gson gson = new Gson();
                int id = gson.fromJson(rawJSON, int.class);
                Game game = crudGame.getGameByID(id);

                response.setJson(gson.toJson(game));
                objectOutput.writeObject(response);
                objectOutput.flush();
            }

            else if(command.equals("downloadGame")){
                String rawJSON = request.getJson();
                Gson gson = new Gson();
                int id = gson.fromJson(rawJSON, int.class);
                Game game = crudGame.getGameByID(id);

                String path = game.getPath();
                System.out.println(path);
                File file = new File(path);
                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

                byte[] bytes = new byte[(int) file.length()];
                bufferedInputStream.read(bytes, 0, bytes.length);

                response.setJson(gson.toJson(bytes));
                objectOutput.writeObject(response);
                objectOutput.flush();

                DownloadHandler downloadHandler = new DownloadHandler(database);
                downloadHandler.Download(game.getId(), loggedInUser.getId());
            }
        }

        else{
            UserHandler crudUser = new UserHandler(database);
            if(command.equals("newAccount")){
                String json = request.getJson();
                ObjectMapper objectMapper = new ObjectMapper();
                User user = objectMapper.readValue(json, User.class);
                user.setPassword(user.encryption());
                crudUser.createUser(user);
                response.setMessage("User created!");
                objectOutput.writeObject(response);
                objectOutput.flush();
            }

            else if(command.equals("login")){
                String json = request.getJson();
                ObjectMapper objectMapper = new ObjectMapper();
                User user = objectMapper.readValue(json, User.class);
                System.out.println(user);
                User dbUser = crudUser.findUserByName(user.getUsername()).get(0);

                HashMap<String, Object> map = new HashMap<>();
                if(BCrypt.checkpw(user.getPassword(), dbUser.getPassword())){
                    loggedInUser = crudUser.findUserByName(user.getUsername()).get(0);
                    isLoggedIn = true;
                    response.setMessage("Logged In successfully");
                    Gson gson = new Gson();
                    response.setJson(gson.toJson(loggedInUser));
                }
                else {
                    map.put("logged_in", false);
                    response.setMessage("Wrong password or username!");
                }

                objectOutput.writeObject(response);
                objectOutput.flush();
            }
        }
    }
}
