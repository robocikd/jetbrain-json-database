package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import messages.JsonMsg;
import messages.ResponseJson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataBase {

    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);
    private final ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    private final Path DB_PATH;
    private final JsonObject JSON_DB;

    public DataBase(String dbPath) throws IOException {
        this.DB_PATH = Path.of(dbPath);
        createDbIfNotExists(this.DB_PATH);
        this.JSON_DB = readDbFromFile(this.DB_PATH).getAsJsonObject();
    }

    private void createDbIfNotExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.writeString(path, "{}");
        }
    }

    private JsonElement readDbFromFile(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            return JsonParser.parseReader(reader);
        }
    }

    public void writeDbToFile(JsonObject db, Path path) throws IOException { // synchronize db
        try (Writer writer = Files.newBufferedWriter(path)) {
            new Gson().toJson(db, writer);
        }
    }

    public String get(String key) {
        readLock.lock();
        boolean hasKey = JSON_DB.has(key);
        try {
            if (hasKey) {
                return new Gson().toJson(new ResponseJson("OK", JSON_DB.get(key).toString(), null));
            }
            return new Gson().toJson(new ResponseJson("ERROR", null, "No such key"));
        } finally {
            readLock.unlock();
        }
    }

    public String set(String key, String value) {
        writeLock.lock();
        try {
            JSON_DB.addProperty(key, value);
            writeDbToFile(JSON_DB, DB_PATH);
            return new Gson().toJson(new ResponseJson("OK", null, null));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
        return null;
    }

    public String delete(String key) {
        writeLock.lock();
        boolean hasKey = JSON_DB.has(key);
        try {
            if (hasKey) {
                JSON_DB.remove(key);
                writeDbToFile(JSON_DB, DB_PATH);
                return new Gson().toJson(new ResponseJson("OK", JSON_DB.get(key).toString(), null));
            }
            return new Gson().toJson(new ResponseJson("ERROR", null, "No such key"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
        return null;
    }

    public String processData(String msgFromClient) {
        String msgToClient = null;
        Gson gson = new Gson();
        JsonMsg jsonMsg = gson.fromJson(msgFromClient, JsonMsg.class);
        String key = jsonMsg.getKey();
        switch (jsonMsg.getType()) {
            case "get":
                msgToClient = this.get(key);
                break;
            case "set":
                msgToClient = this.set(key, jsonMsg.getValue());
                break;
            case "delete":
                msgToClient = this.delete(key);
                break;
            case "exit":
                msgToClient = gson.toJson(new ResponseJson("OK", null, null));
                Main.server.stop();
                break;
            default:
                System.out.println("Bad action.");
        }
        return msgToClient;
    }

}
