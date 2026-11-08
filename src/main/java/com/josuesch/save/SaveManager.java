package com.josuesch.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.josuesch.save.dto.GameSaveDTO;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class SaveManager {

    public static final Path DEFAULT_SAVE_PATH = Path.of("save.json");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean save(GameSaveDTO data) {
        return save(data, DEFAULT_SAVE_PATH);
    }

    public static boolean save(GameSaveDTO data, Path path) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                GSON.toJson(data, writer);
            }
            System.out.println("Savestate gravado com sucesso em: " + path.toAbsolutePath());
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao gravar savestate: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static Optional<GameSaveDTO> load() {
        return load(DEFAULT_SAVE_PATH);
    }

    public static Optional<GameSaveDTO> load(Path path) {
        if (!hasSave(path)) {
            System.out.println(
                    "Nenhum arquivo de savestate encontrado em: " + path.toAbsolutePath());
            return Optional.empty();
        }

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            GameSaveDTO data = GSON.fromJson(reader, GameSaveDTO.class);
            return Optional.ofNullable(data);
        } catch (Exception e) {
            System.err.println("Erro ao carregar savestate: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static boolean hasSave() {
        return hasSave(DEFAULT_SAVE_PATH);
    }

    public static boolean hasSave(Path path) {
        return Files.isRegularFile(path);
    }

    public static boolean deleteSave() {
        return deleteSave(DEFAULT_SAVE_PATH);
    }

    public static boolean deleteSave(Path path) {
        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Erro ao deletar savestate: " + e.getMessage());
            return false;
        }
    }
}
