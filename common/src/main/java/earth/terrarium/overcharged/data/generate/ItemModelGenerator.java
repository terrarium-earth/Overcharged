package earth.terrarium.overcharged.data.generate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ItemModelGenerator {
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final List<String> itemModels;

    public ItemModelGenerator() {
        this.itemModels = List.of(
                "constantan_shovel",
                "constantan_pickaxe",
                "constantan_axe",
                "constantan_sword",
                "constantan_hoe",
                "constantan_aiot"
        );
    }

    public static void main(String[] args) throws IOException {
        new ItemModelGenerator().generate();
    }

    public void generate() throws IOException {
        for (String itemModel : itemModels) {
            var root = new RootItemModel("item/handheld", itemModel);
            var charged = new DefaultItemModel("item/handheld", itemModel + "_charged");
            var empowered = new DefaultItemModel("item/handheld", itemModel + "_empowered");
            String rootFilePath = "common/src/main/resources/assets/overcharged/models/item/" + itemModel + ".json";
            String chargedFilePath = "common/src/main/resources/assets/overcharged/models/item/" + itemModel + "_charged.json";
            String empoweredFilePath = "common/src/main/resources/assets/overcharged/models/item/" + itemModel + "_empowered.json";
            File newRootJson = new File(rootFilePath);
            File newChargedJson = new File(chargedFilePath);
            File newEmpoweredJson = new File(empoweredFilePath);
            newRootJson.createNewFile();
            newChargedJson.createNewFile();
            newEmpoweredJson.createNewFile();
            FileWriter rootWriter = new FileWriter(rootFilePath);
            GSON.toJson(root, rootWriter);
            rootWriter.close();
            FileWriter chargedWriter = new FileWriter(chargedFilePath);
            GSON.toJson(charged, chargedWriter);
            chargedWriter.close();
            FileWriter empoweredWriter = new FileWriter(empoweredFilePath);
            GSON.toJson(empowered, empoweredWriter);
            empoweredWriter.close();
        }
    }
}
