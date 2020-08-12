package org.serverct.parrot.machinecommand.data;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.serverct.parrot.parrotstructure.data.Structure;
import org.serverct.parrot.parrotx.data.PData;
import org.serverct.parrot.parrotx.data.PID;
import org.serverct.parrot.parrotx.utils.BasicUtil;
import org.serverct.parrot.parrotx.utils.EnumUtil;
import org.serverct.parrot.parrotx.utils.I18n;
import org.serverct.parrot.parrotx.utils.ItemUtil;

import java.io.File;
import java.util.*;

public class Machine extends PData {

    public final String name;
    public final ItemStack item;
    public final List<String> commandList;
    private final Map<Character, MachineIngredient> ingredientMap = new HashMap<>();
    private final List<List<String>> arch = new ArrayList<>();
    public boolean valid;
    public MachineStructure structure;

    public Machine(final File file, PID id) {
        super(file, id);
        this.data = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection root = data.getConfigurationSection("Machine");
        if (Objects.isNull(root)) {
            this.valid = false;
            this.name = "无效结构";
            this.item = new ItemStack(Material.AIR);
            this.commandList = new ArrayList<>();
            plugin.lang.logError(I18n.LOAD, getTypeName(), "结构数据文件无效.");
            return;
        }

        this.name = root.getString("Name", "未命名");
        this.item = ItemUtil.build(plugin, Objects.requireNonNull(root.getConfigurationSection("Active")));
        this.commandList = root.getStringList("Active.Commands");
        load(file);
    }

    @Override
    public String getTypeName() {
        return "结构数据文件/" + getFileName();
    }

    @Override
    public void init() {
    }

    @Override
    public void saveDefault() {
    }

    @Override
    public void load(File file) {
        ConfigurationSection root = data.getConfigurationSection("Machine");
        if (BasicUtil.isNull(plugin, root, I18n.LOAD, getTypeName(), "未找到机械根数据节")) return;
        ConfigurationSection structure = root.getConfigurationSection("Structure");
        if (BasicUtil.isNull(plugin, structure, I18n.LOAD, getTypeName(), "结构数据节无效.")) return;
        ConfigurationSection ingredient = structure.getConfigurationSection("Ingredient");
        if (BasicUtil.isNull(plugin, ingredient, I18n.LOAD, getTypeName(), "未找到结构成分数据节.")) return;
        for (String key : ingredient.getKeys(false)) {
            char symbol = key.toCharArray()[0];
            if (ingredient.isConfigurationSection(key)) {
                ConfigurationSection keySection = ingredient.getConfigurationSection(key);
                if (BasicUtil.isNull(plugin, keySection, I18n.LOAD, getTypeName(), "结构成分数据中的 " + key + " 无效."))
                    continue;
                ingredientMap.put(symbol, new MachineIngredient(
                        EnumUtil.getMaterial(keySection.getString("Material")),
                        EnumUtil.valueOf(Structure.StructurePart.TriggerType.class, keySection.getString("Trigger")),
                        keySection.getBoolean("Disappear", false),
                        keySection.getBoolean("Broke", false),
                        (float) keySection.getDouble("Explode", -1)
                ));
            } else {
                ingredientMap.put(symbol, new MachineIngredient(
                        EnumUtil.getMaterial(ingredient.getString(key)),
                        Structure.StructurePart.TriggerType.NONE
                ));
            }
        }

        List<Structure.StructurePart> warps = new ArrayList<>();
        arch.addAll((List<List<String>>) structure.get("Arch"));
        int y = 0;
        for (List<String> floor : arch) {
            int x = 0;
            for (String str : floor) {
                int z = 0;
                for (char symbol : str.toCharArray()) {
                    if (symbol == ' ') {
                        z++;
                        continue;
                    }
                    MachineIngredient ingre = ingredientMap.get(symbol);
                    if (BasicUtil.isNull(plugin, ingre, I18n.LOAD, getTypeName(), "结构数据中的 " + symbol + " 未找到."))
                        continue;
                    warps.add(new Structure.StructurePart(
                            ingre.material,
                            ingre.triggerType,
                            new Structure.RelativeLocation(x, y, z),
                            ingre.disappear,
                            ingre.explode,
                            ingre.broke
                    ));
                    z++;
                }
                x++;
            }
            y++;
        }
        this.structure = new MachineStructure(new NamespacedKey(plugin, "STRUCTURE_" + getFileName()), warps, this);
    }

    @Override
    public void save() {
    }

    private @ToString
    @AllArgsConstructor @RequiredArgsConstructor
    static class MachineIngredient {
        private final Material material;
        private final Structure.StructurePart.TriggerType triggerType;
        private boolean disappear = false;
        private boolean broke = false;
        private float explode = -1;
    }
}
