package thonk.decksimulator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import thonk.decksimulator.cards.*;
import thonk.decksimulator.simulation.Card;

public class CardFactory {
    private static final Map<String, Supplier<? extends Card>> registry = new HashMap<>();

    static {
        registry.put("AdrenalineRush", AdrenalineRush::new);
        registry.put("AllSeeingEye", AllSeeingEye::new);
        registry.put("Ambrosia", Ambrosia::new);
        registry.put("Avalanche", Avalanche::new);
        registry.put("BBSlippers", BBSlippers::new);
        registry.put("BeastMaster", BeastMaster::new);
        registry.put("BeastSense", BeastSense::new);
        registry.put("BootsofSwiftness", BootsofSwiftness::new);
        registry.put("BoundingStrides", BoundingStrides::new);
        registry.put("Brilliance", Brilliance::new);
        registry.put("CashCow", CashCow::new);
        registry.put("ChillStep", ChillStep::new);
        registry.put("ColdSnap", ColdSnap::new);
        registry.put("Composure", Composure::new);
        registry.put("Consolidation", Consolidation::new);
        registry.put("Decay", Decay::new);
        registry.put("Deepfrost", Deepfrost::new);
        registry.put("DelvingGrace", DelvingGrace::new);
        registry.put("DisappearingAct", DisappearingAct::new);
        registry.put("DungeonRepairs", DungeonRepairs::new);
        registry.put("EerieSilence", EerieSilence::new);
        registry.put("EmberSeeker", EmberSeeker::new);
        registry.put("Enlightenment", Enlightenment::new);
        registry.put("Equilibrium", Equilibrium::new);
        registry.put("Eureka", Eureka::new);
        registry.put("Evasion", Evasion::new);
        registry.put("EyesonthePrize", EyesonthePrize::new);
        registry.put("FeedingFrenzy", FeedingFrenzy::new);
        registry.put("FindersKeepers", FindersKeepers::new);
        registry.put("FrostFocus", FrostFocus::new);
        registry.put("FrozenFrenzy", FrozenFrenzy::new);
        registry.put("FrozenTears", FrozenTears::new);
        registry.put("FuzzyBunnySlippers", FuzzyBunnySlippers::new);
        registry.put("GamblersParadise", GamblersParadise::new);
        registry.put("GloriousMoment", GloriousMoment::new);
        registry.put("GreedyEyes", GreedyEyes::new);
        registry.put("Hopscotch", Hopscotch::new);
        registry.put("InNOut", InNOut::new);
        registry.put("LootandScoot", LootandScoot::new);
        registry.put("MomentofClarity", MomentofClarity::new);
        registry.put("NimbleLooting", NimbleLooting::new);
        registry.put("OnefortheRoad", OnefortheRoad::new);
        registry.put("PackageDeal", PackageDeal::new);
        registry.put("PaytoWin", PaytoWin::new);
        registry.put("PiratesBooty", PiratesBooty::new);
        registry.put("PorkChopPower", PorkChopPower::new);
        registry.put("Quickstep", Quickstep::new);
        registry.put("RavenousInclination", RavenousInclination::new);
        registry.put("RecklessCharge", RecklessCharge::new);
        registry.put("RestoringVeil", RestoringVeil::new);
        registry.put("Revelation", Revelation::new);
        registry.put("Reversal", Reversal::new);
        registry.put("RiskyReserves", RiskyReserves::new);
        registry.put("SafeAndSound", SafeAndSound::new);
        registry.put("Scatter", Scatter::new);
        registry.put("SecondWind", SecondWind::new);
        registry.put("SilentRunner", SilentRunner::new);
        registry.put("SmashandGrab", SmashandGrab::new);
        registry.put("Sneak", Sneak::new);
        registry.put("SneakyPants", SneakyPants::new);
        registry.put("SpeedRunner", SpeedRunner::new);
        registry.put("SpendMoneyMakeMoney", SpendMoneyMakeMoney::new);
        registry.put("SpiritSense", SpiritSense::new);
        registry.put("Sprint", Sprint::new);
        registry.put("Stability", Stability::new);
        registry.put("StrideWithPride", StrideWithPride::new);
        registry.put("Stumble", Stumble::new);
        registry.put("SuitUp", SuitUp::new);
        registry.put("Swagger", Swagger::new);
        registry.put("TacticalApproach", TacticalApproach::new);
        registry.put("TheGunShow", TheGunShow::new);
        registry.put("TippyToes", TippyToes::new);
        registry.put("Trailblazer", Trailblazer::new);
        registry.put("TreadLightly", TreadLightly::new);
        registry.put("TreasureHunter", TreasureHunter::new);
        registry.put("WellFed", WellFed::new);
    }

    public static Card create(String name) {
        Supplier<? extends Card> supplier = registry.get(name);
        if (supplier != null) {
            return supplier.get();
        }
        return null;
    }

    public static boolean check(String name) {
        Supplier<? extends Card> supplier = registry.get(name);
        return supplier != null;
    }
}