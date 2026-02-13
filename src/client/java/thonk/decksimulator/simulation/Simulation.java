package thonk.decksimulator.simulation;

import thonk.decksimulator.Settings;
import thonk.decksimulator.cards.*;
import thonk.decksimulator.ui.MultiLineTextBox;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Simulation {

    private ArrayList<Card> permanentDeck = new ArrayList<>();
    private ArrayList<Card> deck = new ArrayList<>();
    private float avgClankBlock, avgHazardBlock, avgClank, avgHazard, avgEmberQueue, avgTreasureQueue, avgRecycle, avgCardShop, avgBonusEmbers, avgSpendMoneyMakeMoney;
    private float avgEmbers, avgCrowns, avgCoins, avgLVLOneKeys, avgLVLThreeKeys, avgLVLTwoKeys, avgTime, avgCardPlays;
    
    private int noSim;

    private Deck activeDeck;
    private PlayerInventory inv;

    private int time;
    private int currentLevel;
    private int staircasesUnlocked;
    private boolean artifactFound;
    private int actionTime;
    private int cardInterval;
    private boolean riskyReservesPlayed;

    private int emberQueue;
    private int treasureQueue;
    private int clankBlock;
    private int clankQueue;
    private int hazardBlock;
    private int hazardQueue;
    private int recycleQueue;
    private int speedQueue;
    private int noCardsPlayed;
    private int noBanesPlayed;
    private int shriekerCount;

    private int lvlOneEmberQueues;
    private int lvlOneTreasureQueues;
    private int lvlTwoEmberQueues;
    private int lvlTwoTreasureQueues;
    private int lvlThreeEmberQueues;
    private int lvlThreeTreasureQueues;
    private int lvlFourEmberQueues;
    private int lvlFourTreasureQueues;

    private int BBSlippersCount;
    private int CashCowCount;
    private int ChillStepCount;
    private int ColdSnapCount;
    private int DisappearingActCount;
    private int FindersKeepersCount;
    private int NimbleLootingCount;
    private int StrideWithPrideCount;

    private boolean BBSlippersActive;
    private boolean BootsofSwiftnessActive;
    private boolean DelvingGraceActive;
    private boolean FuzzyBunnySlippersActive;
    private boolean GloriousMomentActive;
    private boolean InNOutActive;
    private boolean RiskyReservesActive;
    private boolean SafeAndSoundActive;
    private boolean SilentRunnerActive;
    private boolean SpeedRunnerActive;
    private boolean SpendMoneyMakeMoneyActive;
    private boolean SuitUpActive;

    // Loaded from Settings
    private int targetLevel;
    private int lvlOneTimeToExit;
    private int lvlOneTimeToArtifact;
    private int lvlTwoTimeToExit;
    private int lvlTwoTimeToArtifact;
    private int lvlThreeTimeToExit;
    private int lvlThreeTimeToArtifact;
    private int lvlFourTimeToArtifact;
    private int artifactClause;
    private int exitClause;
    private int AdrenalineRushInput;
    private int FeedingFrenzyInput;
    private int FrozenTearsInput;
    private int HopscotchInput;
    private int RavenousInclinationInput;
    private int SpiritSenseInput;


    private Level level1;
    private Level level2;
    private Level level3;
    private Level level4;

    public Simulation() {
        loadLevels();
        loadSettings();
    }

    private void loadSettings() {
        noSim = Settings.get().getNoSimulations();

        targetLevel = Settings.get().getTargetLevel();
        lvlOneTimeToExit = Settings.get().getLvlOneTimeToExit();
        lvlOneTimeToArtifact = Settings.get().getLvlOneTimeToArtifact();
        lvlTwoTimeToExit = Settings.get().getLvlTwoTimeToExit();
        lvlTwoTimeToArtifact = Settings.get().getLvlTwoTimeToArtifact();
        lvlThreeTimeToExit = Settings.get().getLvlThreeTimeToExit();
        lvlThreeTimeToArtifact = Settings.get().getLvlThreeTimeToArtifact();
        lvlFourTimeToArtifact = Settings.get().getLvlFourTimeToArtifact();

        // 1: ASAP
        // 2: Run out of Cards, Treasure & Ember
        // 3: Run out of Cards, Clank Block, Treasure & Ember
        artifactClause = Settings.get().getArtifactClause();
        exitClause = Settings.get().getExitClause();

        AdrenalineRushInput = Settings.get().getAdrenalineRushInput();
        FeedingFrenzyInput = Settings.get().getFeedingFrenzyInput();
        FrozenTearsInput = Settings.get().getFrozenTearsInput();
        HopscotchInput = Settings.get().getHopscotchInput();
        RavenousInclinationInput = Settings.get().getRavenousInclinationInput();
        SpiritSenseInput = Settings.get().getSpiritSenseInput();
    }

    public void loadDeck(ArrayList<Card> deck) {
        ArrayList<Card> newPermanentDeck = new ArrayList<>();
        ArrayList<Card> newDeck = new ArrayList<>();
        for (Card c : deck) {
            if (c.isPermanent()) {
                newPermanentDeck.add(c);
            } else {
                newDeck.add(c);
            }
        }
        permanentDeck = newPermanentDeck;
        this.deck = newDeck;
    }

    public boolean isDeckLoaded() {
        return (!deck.isEmpty() || !permanentDeck.isEmpty());
    }

    private void loadLevels() {
        level1 = new Level(Arrays.asList(
                // Ice Entrance
                new LootSpot("Ice Entrance 1",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 8, 0, 1, 0, 0))
                , new LootSpot("Ice Entrance 2",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 8, 0, 1, 0, 0))
                // Ice Stream
                , new LootSpot("Ice Stream",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(2, 8, 0, 7, 0, 2, 0, 0))
                , new LootSpot("Ice Path 1",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(3, 9, 0, 7, 0, 2, 0, 0))
                // Ice Path
                , new LootSpot("Ice Path 2",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 6, 0, 2, 0, 0))
                , new LootSpot("Ice Path 3",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 6, 0, 2, 0, 0))
                // Ice Path to Key
                , new LootSpot("Ice Path to Key",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 6, 0, 3, 0, 0))
                // Crypt Key Room
                , new LootSpot("Statue at Key",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 8, 0, 1, 0, 0))
                , new LootSpot("Left of Key",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 7, 0, 2, 0, 0))
                // Royal Tomb
                , new LootSpot("Royal Tomb",
                        /* Ember */     new Dropper(1, 4, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 6, 1, 2, 0, 0))
                , new LootSpot("Royal Tomb Hallway",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 2, 0, 1, 0, 0))
                // Right Tombs
                , new LootSpot("Ice Jump Boost",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 3, 3, 0, 0))
                , new LootSpot("Cobweb",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 6, 0, 2, 0, 0))
                // Crypt Stair
                , new LootSpot("Crypt Stair Cove",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 6, 0, 3, 0, 0))
                // Lower Crypt
                , new LootSpot("Lower Crypt 1",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 6, 0, 3, 0, 0))
                , new LootSpot("Lower Crypt 2",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 7, 0, 2, 0, 0))
                // TNT Lake
                , new LootSpot("TNT Lake",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 7, 0, 2, 0, 0))
                // TNT Drip
                , new LootSpot("TNT Drip 1",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 5, 2, 2, 0, 0))
                , new LootSpot("TNT Drip 2",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 7, 1, 1, 0, 0))
                , new LootSpot("TNT Ice Cove",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 5, 0, 1, 0, 0))
                // Crypt Entrance
                , new LootSpot("Crypt Entrance River",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 7, 1, 1, 0, 0))
        ), "Level 1");

        level2 = new Level(Arrays.asList(
                // Mushrooms
                new LootSpot("Mushroom 1",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 5, 2, 0, 2, 0))
                , new LootSpot("Witch Hut",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 5, 1, 0, 2, 0))
                , new LootSpot("Cave Balcony",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 4, 2, 0, 2, 0))
                // Crystal Cave
                , new LootSpot("Crystal Cave",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 4, 2, 0, 2, 0))
                // Ravager Home
                , new LootSpot("Ravager Home",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 4, 2, 0, 2, 0))
                // Pirate Cove
                , new LootSpot("Pirate Cove 1",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 5, 1, 0, 3, 0))
                , new LootSpot("Pirate Cove 2",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 3, 2, 0, 3, 0))
                , new LootSpot("Pirate Cove 3",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 4, 2, 0, 3, 0))
                , new LootSpot("Pirate Cove 4",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 4, 2, 0, 3, 0))
                , new LootSpot("Pirate Cove 5",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 2, 0, 3, 0))
                , new LootSpot("Water",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 6, 2, 0, 1, 0))
                , new LootSpot("Pirate Ship Deck",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 3, 4, 0, 2, 0))
                , new LootSpot("Pirate Ship Hull",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 4, 2, 0, 3, 0))
                // Door to Level 3
                , new LootSpot("Door to Level 3",
                        /* Ember */     null,
                        /* Treasure */  new Dropper(1, 4, 0, 7, 1, 0, 1, 0))
                // Dripstone Cave
                , new LootSpot("Dripstone Cave",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 2, 0, 3, 0))
                // Desert Tomb
                , new LootSpot("Desert Tomb",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 4, 1, 0, 3, 0))
                // Lava Cave
                , new LootSpot("Lava Cave",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 5, 3, 0, 1, 0))
                // Middle Cave
                , new LootSpot("Below Balcony",
                        /* Ember */     new Dropper(1, 7, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 0, 3, 0, 0, 0))
                // Bridge Cave
                , new LootSpot("Before Bridge",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 7, 1, 0, 1, 0))
                , new LootSpot("After Bridge",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 7, 0, 5, 2, 0, 2, 0))
                // Rusty
                , new LootSpot("Rusty",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 4, 3, 0, 2, 0))
                // Spider Cave
                , new LootSpot("Spider Cave 1",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(2, 8, 0, 4, 2, 0, 2, 0))
                , new LootSpot("Spider Cave 2",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 3, 0, 3, 0))
        ), "Level 2");

        level3 = new Level(Arrays.asList(
                // Top Minecart Room
                new LootSpot("Minecart Room",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 4, 2, 0, 0, 3))
                // Top Iron Room
                , new LootSpot("Iron Room",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 2, 3, 0, 0, 3))
                // Top Back Loop
                , new LootSpot("Back Loop 1",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 3, 0, 0, 3))
                , new LootSpot("Back Loop 2",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 2, 2, 0, 0, 3))
                // Top Staircase Down
                , new LootSpot("Staircase Down",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 3, 0, 0, 3))
                // Middle Back Room
                , new LootSpot("Back Room",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 3, 0, 0, 3))
                // Middle Lapis Room
                , new LootSpot("Lapis Room",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 4, 3, 0, 0, 2))
                // Middle Furnace
                , new LootSpot("Furnace",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 3, 0, 0, 3))
                // Middle Crane
                , new LootSpot("Crane",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 2, 0, 0, 3))
                // Middle Office
                , new LootSpot("Office",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 4, 3, 0, 0, 2))
                // Lower Cart Ledge
                , new LootSpot("Cart Ledge",
                        /* Ember */     new Dropper(1, 5, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 5, 0, 3, 2, 0, 0, 3))
                // Lower Storage
                , new LootSpot("Storage",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 3, 0, 0, 3))
                // Lower Chests
                , new LootSpot("Chests",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 3, 0, 0, 3))
                // Lower Diamond Room
                , new LootSpot("Diamond Room",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 2, 3, 0, 0, 3))
        ), "Level 3");

        level4 = new Level(Arrays.asList(
                // Starting Quadrant
                new LootSpot("SQ1",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 6, 0, 0, 0))
                , new LootSpot("SQ2",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 6, 0, 0, 0))
                , new LootSpot("SQ3",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 6, 0, 0, 0))
                , new LootSpot("SQ4",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 6, 0, 0, 0))
                // Back Quadrant
                , new LootSpot("Next to Circle",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 6, 0, 0, 0))
                , new LootSpot("Dripstone",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 6, 0, 0, 0))
                , new LootSpot("Next to Bomb",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 6, 0, 0, 0))
                // Left Quadrant
                , new LootSpot("Next to Center",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 6, 0, 0, 0))
                , new LootSpot("Vines",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 6, 0, 0, 0))
                , new LootSpot("Lava Lake",
                        /* Ember */     new Dropper(1, 6, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 6, 0, 3, 6, 0, 0, 0))
                // Towers
                , new LootSpot("Tower 1",
                        /* Ember */     new Dropper(1, 4, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 4, 0, 3, 6, 0, 0, 0))
                , new LootSpot("Tower 2",
                        /* Ember */     new Dropper(1, 4, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 4, 0, 3, 6, 0, 0, 0))
                , new LootSpot("Tower 3",
                        /* Ember */     new Dropper(1, 4, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 4, 0, 3, 6, 0, 0, 0))
                , new LootSpot("Tower 4",
                        /* Ember */     new Dropper(1, 4, 1, 0, 0, 0, 0, 0),
                        /* Treasure */  new Dropper(1, 4, 0, 3, 6, 0, 0, 0))
        ), "Level 4");
    }

    private void resetOverallStats() {
        avgClankBlock = 0; avgHazardBlock = 0; avgClank = 0; avgHazard = 0; avgEmberQueue = 0; avgTreasureQueue = 0; avgRecycle = 0; avgCardShop = 0; avgBonusEmbers = 0; avgSpendMoneyMakeMoney = 0;
        avgEmbers = 0; avgCrowns = 0; avgCoins = 0; avgLVLOneKeys = 0; avgLVLThreeKeys = 0; avgLVLTwoKeys = 0; avgTime = 0; avgCardPlays = 0;
        BBSlippersActive = false; BootsofSwiftnessActive = false; DelvingGraceActive = false; FuzzyBunnySlippersActive = false;
        GloriousMomentActive = false; InNOutActive = false; RiskyReservesActive = false; SafeAndSoundActive = false; SilentRunnerActive = false;
        SpeedRunnerActive = false; SpendMoneyMakeMoneyActive = false; SuitUpActive = false;
    }

    private void resetRunStats() {
        time = 0;
        currentLevel = 1;
        staircasesUnlocked = 0;
        artifactFound = false;
        actionTime = 0;
        cardInterval = 30;
        riskyReservesPlayed = false;

        noCardsPlayed = 0;
        noBanesPlayed = 0;

        emberQueue = 0; treasureQueue = 0; clankBlock = 0; clankQueue = 0; hazardBlock = 0; hazardQueue = 0; recycleQueue = 0; speedQueue = 0;
        shriekerCount = 0;

        lvlOneEmberQueues = 0;
        lvlOneTreasureQueues = 0;
        lvlTwoEmberQueues = 0;
        lvlTwoTreasureQueues = 0;
        lvlThreeEmberQueues = 0;
        lvlThreeTreasureQueues = 0;
        lvlFourEmberQueues = 0;
        lvlFourTreasureQueues = 0;

        if (BBSlippersActive) BBSlippersCount = 7 * 60;
        CashCowCount = 0;
        ChillStepCount = 0;
        ColdSnapCount = 0;
        DisappearingActCount = 0;
        FindersKeepersCount = 0;
        NimbleLootingCount = 0;
        StrideWithPrideCount = 0;
    }

    public void startSimulation(MultiLineTextBox resultBox) {
        resetOverallStats();
        loadSettings();

        for (Card pc : permanentDeck) {
            if (pc.hasEffect()) {
                addPermanentEffects(pc);
            }
        }

        for (int i = 0; i < noSim; i++) {
            resetRunStats();

            inv = new PlayerInventory();

            int treasureInterval = 5;
            int emberInterval = 5;
            int timeSinceLastTreasure = 0;
            int timeSinceLastEmber = 0;

            for (Card pc : permanentDeck) {
                addStats(pc);
            }

            activeDeck = new Deck(deck);

            // Time Loop
            while (currentLevel > 0) {
                timeSinceLastTreasure++;
                timeSinceLastEmber++;
                actionTime++;
                time++;

                movementLogic();

                if (currentLevel != 0) {
                    if (DisappearingActCount > 0) {
                        if (currentLevel >= 3) {
                            addEmberQueue(12 * DisappearingActCount);
                            DisappearingActCount = 0;
                        }
                    }

                    if (!activeDeck.isDeckEmpty()) {
                        if (time % cardInterval == 0) {
                            playCard();
                        }
                    }

                    addBanes();

                    processSpeed();

                    processHazard();

                    processClank();

                    treasureInterval = adjustLootInterval(treasureQueue);
                    emberInterval = adjustLootInterval(emberQueue);

                    timeSinceLastTreasure = processTreasure(timeSinceLastTreasure, treasureInterval);

                    timeSinceLastEmber = processEmber(timeSinceLastEmber, emberInterval);

                    if (BBSlippersCount > 0) BBSlippersCount--;
                }
            }

            addBonusEmbers();

            avgTime += time;
            avgCardPlays += noCardsPlayed;

            avgEmbers += inv.getEmbers();
            avgCoins += inv.getCoins();
            avgCrowns += inv.getCrowns();
            avgLVLOneKeys += inv.getLVLOneKeys();
            avgLVLTwoKeys += inv.getLVLTwoKeys();
            avgLVLThreeKeys += inv.getLVLThreeKeys();


        }
        postResults(resultBox, 3);
    }

    private int processEmber(int timeSinceLastEmber, int emberInterval) {
        if (timeSinceLastEmber >= emberInterval) {
            timeSinceLastEmber = 0;
            if (emberQueue > 0) {
                emberQueue--;
                Random r = new Random();
                int num = 2;
                int den = 5;
                switch (currentLevel) {
                    case 1 -> { if (lvlOneEmberQueues > 12) {num = 1; den = 7;} }
                    case 2 -> { if (lvlTwoEmberQueues > 18) {num = 1; den = 6;} }
                    case 3 -> { if (lvlThreeEmberQueues > 32) {den = 6;} }
                    case 4 -> { if (lvlFourEmberQueues > 32) {den = 6;} }
                }
                if (r.nextInt(den) < num) activateEmber(currentLevel);
            }
        }
        return timeSinceLastEmber;
    }

    private int processTreasure(int timeSinceLastTreasure, int treasureInterval) {
        if (timeSinceLastTreasure >= treasureInterval) {
            timeSinceLastTreasure = 0;
            if (treasureQueue > 0) {
                treasureQueue--;
                Random r = new Random();
                int num = 1;
                int den = 4;
                switch (currentLevel) {
                    case 1 -> { if (lvlOneTreasureQueues > 12) {den = 6;} }
                    case 2 -> { if (lvlTwoTreasureQueues > 18) {den = 5;} }
                    case 3 -> { if (lvlThreeTreasureQueues > 32) {num = 2; den = 6;} }
                    case 4 -> { if (lvlFourTreasureQueues > 32) {num = 2; den = 5;} }
                }
                if (r.nextInt(den) < num) activateTreasure(currentLevel);
            }
        }
        return timeSinceLastTreasure;
    }

    private void processClank() {
        if (clankQueue > 0) {
            if (clankQueue > clankBlock) {
                clankQueue = clankBlock;
                BBSlippersCount = 0;
                NimbleLootingCount = 0;
            }
            if (NimbleLootingCount > 0) {
                addTreasureQueue(clankQueue * NimbleLootingCount * 2);
            }

            clankBlock -= clankQueue;
            clankQueue = 0;
        }
    }

    private void processHazard() {
        if (hazardQueue > 0) {
            if (hazardQueue > hazardBlock) hazardQueue = hazardBlock;
            hazardBlock -= hazardQueue;
            hazardQueue = 0;
        }
    }

    private void processSpeed() {
        if (speedQueue > 0) {
            if (SilentRunnerActive) {
                for (int j = 0; j < speedQueue / 15; j++) {
                    Random r = new Random();
                    if (r.nextInt(2) == 1) addClankBlock(1);
                }
                speedQueue = 0;
            }
        }
    }

    private void addBanes() {
        int baneInterval = 120;
        if (time % baneInterval == 0) {
            activeDeck.addCard(new Stumble());
            int decayDelay = 300;
            if (time >= decayDelay + baneInterval) {
                activeDeck.addCard(new Decay());
            }
        }
    }

    private void playCard() {
        Card c = activeDeck.popRandom();

        if (recycleQueue > 0) {
            if (c.isRecyclable()) {
                activeDeck.addCard(c);
                recycleQueue--;
            }
        }

        if (RiskyReservesActive) {
            if (!riskyReservesPlayed) {
                if (activeDeck.isDeckEmpty()) {
                    riskyReservesPlayed = true;
                    int deckSize = permanentDeck.size() + deck.size();
                    addTreasureQueue(deckSize / 2);
                    addEmberQueue(deckSize / 2);
                }
            }
        }

        addStats(c);

        if (!(FuzzyBunnySlippersActive && artifactFound)) {
            if (BootsofSwiftnessActive) {
                Random r = new Random();
                if (r.nextInt(20) < 3) {
                    speedQueue += 30;
                }
            }
        }

        if (c instanceof Stumble || c instanceof Decay) noBanesPlayed++;

        if (CashCowCount > 0) {
            addTreasureQueue(CashCowCount);
        }

        if (c instanceof EmberSeeker || c instanceof Sneak || c instanceof Stability || c instanceof TreasureHunter || c instanceof MomentofClarity) {
            addEmberQueue(ChillStepCount);
        }

        if (ColdSnapCount > 0) {
            Random r = new Random();
            if (r.nextInt(4) == 3) {
                ColdSnapCount--;
            }
        }

        addEffects(c);

        noCardsPlayed++;
        StrideWithPrideCount--;
    }

    private void movementLogic() {
        if (currentLevel != targetLevel) {
            int timeToExit = getTimeToExit();
            if (!artifactFound) {
                if (actionTime >= timeToExit) {
                    currentLevel++;
                    staircasesUnlocked++;
                    if (DelvingGraceActive) {
                        addTreasureQueue(4);
                        addHazardBlock(2);
                    }
                    if (SpeedRunnerActive) {
                        if (currentLevel == 3) {
                            if (time <= 300) inv.addEmbers(8);
                        }
                    }
                    actionTime = 0;
                }
            } else {
                if (actionTime >= timeToExit) {
                    currentLevel--;
                    actionTime = 0;
                }
            }
        } else {
            if (!artifactFound) {
                int timeToArtifact = getTimeToArtifact();
                if (actionTime >= timeToArtifact) {
                    if (artifactClause == 1) pickupArtifact();
                    else if (artifactClause == 2) {
                        if (activeDeck.isDeckEmpty() && treasureQueue == 0 && emberQueue == 0) pickupArtifact();
                    }
                    else if (artifactClause == 3) {
                        if (activeDeck.isDeckEmpty() && clankBlock <= 3 && treasureQueue == 0 && emberQueue == 0) {
                            pickupArtifact();
                        }
                    }
                }
            }
            if (artifactFound) {
                boolean shouldExit = false;
                if (exitClause == 1) {
                    shouldExit = true;
                } else if (exitClause == 2) {
                    shouldExit = activeDeck.isDeckEmpty() && treasureQueue == 0 && emberQueue == 0;
                } else if (exitClause == 3) {
                    shouldExit = activeDeck.isDeckEmpty() && clankBlock <= 3 && treasureQueue == 0 && emberQueue == 0;
                }

                if (shouldExit) {
                    int timeToArtifact = getTimeToArtifact();
                    if (actionTime >= timeToArtifact) {
                        currentLevel--;
                        actionTime = 0;
                    }
                }
            }
        }
    }

    private int getTimeToArtifact() {
        return switch (currentLevel) {
            case 1 -> lvlOneTimeToArtifact;
            case 2 -> lvlTwoTimeToArtifact;
            case 3 -> lvlThreeTimeToArtifact;
            case 4 -> lvlFourTimeToArtifact;
            default -> 0;
        };
    }

    private int getTimeToExit() {
        return switch (currentLevel) {
            case 1 -> lvlOneTimeToExit;
            case 2 -> lvlTwoTimeToExit;
            case 3 -> lvlThreeTimeToExit;
            default -> 0;
        };
    }

    private void addBonusEmbers() {
        if (SafeAndSoundActive) {
            avgBonusEmbers += clankBlock;
        }

        if (InNOutActive) {
            avgBonusEmbers += (permanentDeck.size() + deck.size() - shriekerCount);
        }

        switch (targetLevel) {
            case 2 -> avgBonusEmbers += 3;
            case 3 -> avgBonusEmbers += 8;
            case 4 -> avgBonusEmbers += 15;
        }
    }

    private void postResults(MultiLineTextBox resultBox, int d) {
        avgClankBlock /= noSim;
        avgHazardBlock /= noSim;
        avgClank /= noSim;
        avgHazard /= noSim;
        avgEmberQueue /= noSim;
        avgTreasureQueue /= noSim;
        avgRecycle /= noSim;
        avgCardShop /= noSim;
        avgBonusEmbers /= noSim;
        avgSpendMoneyMakeMoney /= noSim;
        avgEmbers /= noSim;
        avgCrowns /= noSim;
        avgCoins /= noSim;
        avgLVLOneKeys /= noSim;
        avgLVLThreeKeys /= noSim;
        avgLVLTwoKeys /= noSim;
        avgTime /= noSim;
        avgCardPlays /= noSim;
        // Post results
        ArrayList<String> results = new ArrayList<>();
        results.add("Avg. results of " + noSim + " L" + targetLevel + " run(s):");
        results.add("Time: " + formatTime((long) avgTime) + " (" + avgCardPlays + " Cards)");
        results.add("Embers: " + round(avgEmbers, d));
        if (avgBonusEmbers > 0) results.add("Bonus Embers: " + round(avgBonusEmbers, d));
        results.add("Ember Queues: " + round(avgEmberQueue, d));
        results.add("Crowns: " + round(avgCrowns, d));
        results.add("Coins: " + round(avgCoins, d));
        results.add("Keys: " + round(avgLVLOneKeys + avgLVLTwoKeys + avgLVLThreeKeys, d));
        results.add("Treasure Queues: " + round(avgTreasureQueue, d));
        results.add("Clank Block: " + round(avgClankBlock, d));
        results.add("Clank: " + round(avgClank, d));
        results.add("Hazard Block: " + round(avgHazardBlock, d));
        results.add("Hazard: " + round(avgHazard, d));
        if (avgRecycle > 0) results.add("Recycle: " + round(avgRecycle, d));
        if (avgCardShop > 0) results.add("Extra Cards in Shop: " + round(avgCardShop, d));
        if (avgSpendMoneyMakeMoney > 1) results.add("Artifact Multiplier: " + round(avgSpendMoneyMakeMoney, d) + "x");
        resultBox.setLines(results);
    }

    public String formatTime(long seconds) {
        return Duration.ofSeconds(seconds)
                .toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    private void pickupArtifact() {
        artifactFound = true;

        if (SpendMoneyMakeMoneyActive) {
            if (inv.getEmbers() >= 17) {
                inv.subEmbers(17);
                avgSpendMoneyMakeMoney += 1.5F;
            } else {
                avgSpendMoneyMakeMoney += 1F;
            }
        } else {
            avgSpendMoneyMakeMoney += 1F;
        }

        if (FuzzyBunnySlippersActive) cardInterval = 15;

        if (GloriousMomentActive) {
            addRecycleQueue(3);
            addHazardBlock(3);
            addClankBlock(3);
        }

        if (FindersKeepersCount > 0) {
            inv.addEmbers(6 * FindersKeepersCount);
        }

        addClankQueue(3);

        actionTime = 0;
    }

    private int adjustLootInterval(int q) {
        if (q <= 15) return 5;
        else if (q <= 45) return 3;
        else return 2;
    }

    private void activateTreasure(int level) {
        switch (level) {
            case 1 -> {
                level1.triggerTreasure(inv);
                lvlOneTreasureQueues++;
            }
            case 2 -> {
                level2.triggerTreasure(inv);
                lvlTwoTreasureQueues++;
            }
            case 3 -> {
                level3.triggerTreasure(inv);
                lvlThreeTreasureQueues++;
            }
            case 4 -> {
                level4.triggerTreasure(inv);
                lvlFourTreasureQueues++;
            }
        }
    }

    private void activateEmber(int level) {
        switch (level) {
            case 1 -> {
                level1.triggerEmber(inv);
                lvlOneEmberQueues++;
            }
            case 2 -> {
                level2.triggerEmber(inv);
                lvlTwoEmberQueues++;
            }
            case 3 -> {
                level3.triggerEmber(inv);
                lvlThreeEmberQueues++;
            }
            case 4 -> {
                level4.triggerEmber(inv);
                lvlFourEmberQueues++;
            }
        }
    }

    private void addStats(Card c) {
        addTreasureQueue(c.getTreasureQueue());

        addEmberQueue(c.getEmberQueue());

        addClankBlock(c.getClankBlock());

        addHazardBlock(c.getHazardBlock());

        if (c.getClank() > 0) addClankQueue(c.getClank());
        else avgClank += c.getClank();

        if (c.getHazard() > 0) addHazardQueue(c.getHazard());
        else avgHazard += c.getHazard();

        addRecycleQueue(c.getRecycle());

        if (!(FuzzyBunnySlippersActive && artifactFound)) speedQueue += c.getSpeed();

        avgCardShop += c.getCardShop();
    }

    private void addPermanentEffects(Card c) {
        if (c instanceof BBSlippers) {
            BBSlippersActive = true;
        } else if (c instanceof BootsofSwiftness) {
            BootsofSwiftnessActive = true;
        } else if (c instanceof DelvingGrace) {
            DelvingGraceActive = true;
        } else if (c instanceof FuzzyBunnySlippers) {
            FuzzyBunnySlippersActive = true;
        } else if (c instanceof GloriousMoment) {
            GloriousMomentActive = true;
        } else if (c instanceof InNOut) {
            InNOutActive = true;
        } else if (c instanceof RiskyReserves) {
            RiskyReservesActive = true;
        } else if (c instanceof SafeAndSound) {
            SafeAndSoundActive = true;
        } else if (c instanceof SilentRunner) {
            SilentRunnerActive = true;
        } else if (c instanceof SpeedRunner) {
            SpeedRunnerActive = true;
        } else if (c instanceof SpendMoneyMakeMoney) {
            SpendMoneyMakeMoneyActive = true;
        } else if (c instanceof SuitUp) {
            SuitUpActive = true;
        }
    }

    private void addEffects(Card c) {
        if (c instanceof AdrenalineRush) {
            addTreasureQueue(AdrenalineRushInput);
        } else if (c instanceof Avalanche) {
            addEmberQueue(noCardsPlayed);
        } else if (c instanceof CashCow) {
            CashCowCount++;
        } else if (c instanceof ChillStep) {
            ChillStepCount++;
        } else if (c instanceof ColdSnap) {
            ColdSnapCount++;
        } else if (c instanceof Composure) {
            addClankBlock(noBanesPlayed);
        } else if (c instanceof Consolidation) {
            int totalQueue = treasureQueue + hazardBlock + clankBlock;
            treasureQueue = 0; hazardBlock = 0; clankBlock = 0;
            addEmberQueue(totalQueue / 2);
        } else if (c instanceof Deepfrost) {
            addEmberQueue(staircasesUnlocked * 6);
        } else if (c instanceof DisappearingAct) {
            if (currentLevel >= 3) addEmberQueue(12);
            else DisappearingActCount++;
        } else if (c instanceof EerieSilence) {
            activeDeck.popRandom();
        } else if (c instanceof Equilibrium) {
            int evenQueue = (treasureQueue + emberQueue + hazardBlock + clankBlock) / 4;
            treasureQueue = evenQueue; emberQueue = evenQueue; hazardBlock = evenQueue; clankBlock = evenQueue;
        } else if (c instanceof FeedingFrenzy) {
            addTreasureQueue(FeedingFrenzyInput);
        } else if (c instanceof FindersKeepers) {
            FindersKeepersCount++;
        } else if (c instanceof FrozenFrenzy) {
            inv.addEmbers(20);
            // Implement Ember despawn if reworking loot pickup timing
        } else if (c instanceof FrozenTears) {
            addEmberQueue(FrozenTearsInput);
        } else if (c instanceof Hopscotch) {
            if (HopscotchInput == 0) {
                addTreasureQueue(10);
            } else {
                addShriekerClank();
                addEmberQueue(10);
            }
        } else if (c instanceof NimbleLooting) {
            if (NimbleLootingCount < 6) NimbleLootingCount++;
        } else if (c instanceof RavenousInclination) {
            addEmberQueue(2 * RavenousInclinationInput);
        } else if (c instanceof RecklessCharge) {
            addShriekerClank();
            addEmberQueue(10);
        } else if (c instanceof SpiritSense) {
            for (int i = 0; i < SpiritSenseInput; i++) {
                addShriekerClank();
                addEmberQueue(5);
            }
        } else if (c instanceof StrideWithPride) {
            StrideWithPrideCount = 4;
        } else if (c instanceof Swagger) {
            activeDeck.addCard(new Stumble());
            activeDeck.addCard(new Stumble());
        } else if (c instanceof Trailblazer) {
            int halfQueue = clankBlock / 2;
            addEmberQueue(halfQueue + clankBlock % 2);
            addTreasureQueue(halfQueue);
        }
    }

    private void addEmberQueue(int n) {
        if (ColdSnapCount > 0) {
            n *= 2;
        } else if (BBSlippersCount > 0) {
            n *= 2;
        }
        emberQueue += n; avgEmberQueue += n;
    }

    private void addTreasureQueue(int n) {
        if (StrideWithPrideCount > 0) {
            n *= 2;
        }
        treasureQueue += n; avgTreasureQueue += n;
    }

    private void addClankBlock(int n) {
        if (StrideWithPrideCount > 0) {
            n /= 2;
        }
        clankBlock += n; avgClankBlock += n;
    }
    private void addClankQueue(int n) {clankQueue += n; avgClank += n;}
    private void addHazardBlock(int n) {hazardBlock += n; avgHazardBlock += n;}
    private void addHazardQueue(int n) {hazardQueue += n; avgHazard += n;}
    private void addRecycleQueue(int n) {recycleQueue += n; avgRecycle += n;}

    private void addShriekerClank() {
        shriekerCount++;
        if (BBSlippersCount > 0) BBSlippersCount -= 20;

        if (SuitUpActive) {
            Random r = new Random();
            if (r.nextInt(4) == 3) clankQueue += 2; avgClank += 2;
        } else {
            clankQueue++; avgClank++;
        }
    }

    public static float round(float value, int decimals) {
        float scale = (float) Math.pow(10, decimals);
        return Math.round(value * scale) / scale;
    }
}
