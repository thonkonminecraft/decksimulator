package thonk.decksimulator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Settings {

    private int noSimulations = 1000;

    private final int[] artifactClause = new int[]{1,2,3};
    private int artifactClauseIndex = 0;

    private final int[] exitClause = new int[]{1,2,3};
    private int exitClauseIndex = 0;

    private int lvlOneTimeToExit = 60;
    private int lvlOneTimeToArtifact = 70;
    private int lvlTwoTimeToExit = 60;
    private int lvlTwoTimeToArtifact = 90;
    private int lvlThreeTimeToExit = 30;
    private int lvlThreeTimeToArtifact = 80;
    private int lvlFourTimeToArtifact = 90;

    private final int[] targetLevel = new int[]{1,2,3,4};
    private int targetLevelIndex = 3;

    private int AdrenalineRushInput = 10;
    private int FeedingFrenzyInput = 6;
    private int FrozenTearsInput = 10;
    private final int[] HopscotchInput = new int[]{0,1};
    private int HopscotchInputIndex = 0;
    private int RavenousInclinationInput = 3;
    private int SpiritSenseInput = 2;

    private static final Path FILE = Path.of("config", "deck_simulator_settings.json");

    private static Settings instance;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Settings get() {
        if (instance == null) instance = load();
        return instance;
    }

    public int getNoSimulations() { return noSimulations; }
    public void setNoSimulations(int val) { noSimulations = val; save(); }

    public int getArtifactClause() { return artifactClause[artifactClauseIndex]; }
    public String tagArtifactClause() {
        return switch (artifactClause[artifactClauseIndex]) {
            case 1 -> "ASAP";
            case 2 -> "Cards Used";
            case 3 -> "Clank Block Used";
            default -> "Error";
        };
    }
    public void nextArtifactClause() {
        artifactClauseIndex = (artifactClauseIndex + 1) % artifactClause.length;
        save();
    }

    public int getExitClause() { return exitClause[exitClauseIndex]; }
    public String tagExitClause() {
        return switch (exitClause[exitClauseIndex]) {
            case 1 -> "ASAP";
            case 2 -> "Cards Used";
            case 3 -> "Clank Block Used";
            default -> "Error";
        };
    }
    public void nextExitClause() {
        exitClauseIndex = (exitClauseIndex + 1) % exitClause.length;
        save();
    }

    public int getLvlOneTimeToExit() { return lvlOneTimeToExit; }
    public void setLvlOneTimeToExit(int val) { lvlOneTimeToExit = val; save(); }

    public int getLvlOneTimeToArtifact() { return lvlOneTimeToArtifact; }
    public void setLvlOneTimeToArtifact(int val) { lvlOneTimeToArtifact = val; save(); }

    public int getLvlTwoTimeToExit() { return lvlTwoTimeToExit; }
    public void setLvlTwoTimeToExit(int val) { lvlTwoTimeToExit = val; save(); }

    public int getLvlTwoTimeToArtifact() { return lvlTwoTimeToArtifact; }
    public void setLvlTwoTimeToArtifact(int val) { lvlTwoTimeToArtifact = val; save(); }

    public int getLvlThreeTimeToExit() { return lvlThreeTimeToExit; }
    public void setLvlThreeTimeToExit(int val) { lvlThreeTimeToExit = val; save(); }

    public int getLvlThreeTimeToArtifact() { return lvlThreeTimeToArtifact; }
    public void setLvlThreeTimeToArtifact(int val) { lvlThreeTimeToArtifact = val; save(); }

    public int getLvlFourTimeToArtifact() { return lvlFourTimeToArtifact; }
    public void setLvlFourTimeToArtifact(int val) { lvlFourTimeToArtifact = val; save(); }

    public int getTargetLevel() { return targetLevel[targetLevelIndex]; }
    public String tagTargetLevel() { return String.valueOf(targetLevel[targetLevelIndex]); }
    public void nextTargetLevel() {
        targetLevelIndex = (targetLevelIndex + 1) % targetLevel.length;
        save();
    }

    public int getAdrenalineRushInput() { return AdrenalineRushInput; }
    public void setAdrenalineRushInput(int val) { AdrenalineRushInput = val; save(); }

    public int getFeedingFrenzyInput() { return FeedingFrenzyInput; }
    public void setFeedingFrenzyInput(int val) { FeedingFrenzyInput = val; save(); }

    public int getFrozenTearsInput() { return FrozenTearsInput; }
    public void setFrozenTearsInput(int val) { FrozenTearsInput = val; save(); }

    public int getHopscotchInput() { return HopscotchInput[HopscotchInputIndex]; }
    public String tagHopscotchInput() {
        return switch (HopscotchInput[HopscotchInputIndex]) {
            case 0 -> "Treasure";
            case 1 -> "Embers";
            default -> "Error";
        };
    }
    public void nextHopscotchInput() {
        HopscotchInputIndex = (HopscotchInputIndex + 1) % HopscotchInput.length;
        save();
    }

    public int getRavenousInclinationInput() { return RavenousInclinationInput; }
    public void setRavenousInclinationInput(int val) { RavenousInclinationInput = val; save(); }

    public int getSpiritSenseInput() { return SpiritSenseInput; }
    public void setSpiritSenseInput(int val) { SpiritSenseInput = val; save(); }

    private static Settings load() {
        try {
            if (Files.exists(FILE)) {
                try (Reader r = Files.newBufferedReader(FILE)) {
                    return GSON.fromJson(r, Settings.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Settings();
    }

    private void save() {
        try {
            Files.createDirectories(FILE.getParent());
            try (Writer w = Files.newBufferedWriter(FILE)) {
                GSON.toJson(this, w);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
