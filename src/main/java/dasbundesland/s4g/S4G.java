package dasbundesland.s4g;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.math.BlockPos;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class S4G implements ModInitializer {

    private static BlockPos globalSpawn = new BlockPos(0,0,0);
    private static final AtomicBoolean enabled = new AtomicBoolean(false);
    private static SpawnMode spawnMode = SpawnMode.GLOBAL;
    private static Hashtable<Long, BlockPos> seedSpawnTable = new Hashtable<>();
    enum SpawnMode{
        GLOBAL,
        SEED
    }
    @Override
    public void onInitialize() {
        updateConfig();
    }

    public static BlockPos getSetSpawn(long seed){
        if(spawnMode.equals(SpawnMode.SEED)){
            return seedSpawnTable.get(seed);
        } else{
            return globalSpawn;
        }
    }

    public static AtomicBoolean getEnabled(){
        return enabled;
    }

    public static void updateConfig(){

        try {
            FileReader fileReader = new FileReader("s4g_config.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String enabled = bufferedReader.readLine();
            if(enabled.equals("")){
                S4G.enabled.set(false);
                bufferedReader.close();
                BufferedWriter writer = new BufferedWriter(new FileWriter("s4g_config.txt"));
                writer.write("seed");
                writer.write("//1.16 ssg (default config)");
                writer.write("SEED 2483313382402348964 @ -232,0,839");
                writer.close();
                return;
            }
            if(enabled.equals("global")){
                S4G.spawnMode = SpawnMode.GLOBAL;
                S4G.enabled.set(true);
            } else if(enabled.equals("seed")){
                S4G.spawnMode = SpawnMode.SEED;
                S4G.enabled.set(true);
            } else {
                S4G.enabled.set(false);
            }
            if(spawnMode == SpawnMode.GLOBAL){
                String pos = bufferedReader.readLine();
                String[] delimited = pos.split(",");
                S4G.globalSpawn = new BlockPos(Integer.parseInt(delimited[0]), Integer.parseInt(delimited[1]), Integer.parseInt(delimited[2]));
            } else{
                String line;
                List<String> seedDefs = new ArrayList<>();
                while((line = bufferedReader.readLine()) != null){
                    if(line.startsWith("SEED")){
                        seedDefs.add(line);
                    }
                }
                seedDefs.forEach(s -> {
                    String[] delimited = s.split(" ");
                    String[] pos = delimited[3].split(",");
                    BlockPos spawn = new BlockPos(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(pos[2]));
                    long seed = Long.parseLong(delimited[1]);
                    seedSpawnTable.put(seed, spawn);
                    System.out.println(seedSpawnTable);
                });

            }
            bufferedReader.close();

        } catch (IOException e) {
            enabled.set(false);
            e.printStackTrace();
        }
    }


}
