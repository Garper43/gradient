package org.garper.gradient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class Main extends JavaPlugin {
    private Map<String, String> legacyIDs;

    @Override
    public void onEnable() {
        // Plugin startup logic
        InputStream is = this.getResource("materials.yml");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};

        try {
            legacyIDs = mapper.readValue(is, typeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getCommand("gradient").setExecutor(new GradientCommand(legacyIDs));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
