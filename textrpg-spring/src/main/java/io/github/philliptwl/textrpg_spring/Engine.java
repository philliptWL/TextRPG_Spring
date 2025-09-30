package io.github.philliptwl.textrpg_spring;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Scanner;

@Service
public class Engine{
    private static final String world = "Aethros";
    private static final String villain = "The Ash King";

    public static void start(){
        boolean blGameOver = false;
        ArrayList<String> path = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        int response;

        //Intro
        logo();
        gameIntro();

        //RPGCharacter creation
        RPGCharacter player = createCharacter(scan);
        postCharacterCreation(player);

        menu(new String[]{"Continue through the Cinder Gate."},true);
        response = scan.nextInt();

        //Game loop
        switch (response) {
            case 1:
                while(!blGameOver) {
                    //First decision and encounter
                    path = startJourney(player, path, scan);
                    blGameOver = firstEncounter(player, path, scan);
                    if (blGameOver) break;
                    resetPlayerStats(player);
                    increasePlayerLevel(player, 2);

                    //Second decision and encounter
                    path = decisionSecondPath(player, path, scan);
                    blGameOver = secondEncounter(player, path, scan);
                    if (blGameOver) break;
                    resetPlayerStats(player);
                    increasePlayerLevel(player, 3);

                    //Third encounter based on previous decision
                    pathConverges(path);
                    blGameOver = thirdEncounter(player, path, scan);
                    if (blGameOver) break;
                    resetPlayerStats(player);
                    increasePlayerLevel(player, 4);

                    //Final encounter
                    blGameOver = encounterAshKing(player, scan);
                    if (blGameOver) break;
                    gameOutro(player, path);
                    blGameOver = true;
                }
                break;
            case 0:
                isGameOver(player, scan, true);
                scan.close();
                return;
            default:
                optInvalid("option");
                break;
        }
        scan.close();
    }

    private static boolean isGameOver(RPGCharacter player, Scanner scan, boolean override) {

        if(player.isDead()){
            typeOut(String.format("""
                    %s's light has faded away...
                    """, player.getName()),25);
            menu(new String[]{"Try again?"},true);
        } else if (override) {
            typeOut("""
                    Your journey has ended...
                    """,25);
            menu(new String[]{"Restart?"},true);
        }
        int response = scan.nextInt();
        switch (response) {
            case 1:
                start();
                break;
            case 0:
                System.out.println("GAME OVER");
                return true;
            default:
                optInvalid("option");
        }
        return false;
    }

    public static RPGCharacter createCharacter(Scanner scan){
        RPGCharacter player = null;
        String name;
        int startLevel = 1;

        while(player == null) {
            menu(new String[]{"Barbarian", "Ranger", "Mage"},false);
            int response = scan.nextInt();
            scan.nextLine();
            switch (response) {
                case 1: //io.github.philliptwl.textrpg_spring.Barbarian
                    name = namePlayerCharacter(scan,"Barbarian");
                    player = new RPGCharacter(name , false, new Barbarian(115.0, chooseSword(name,scan),100,
                                                new Charge()), startLevel);
                    break;
                case 2: //io.github.philliptwl.textrpg_spring.Ranger
                    name = namePlayerCharacter(scan,"Ranger");
                    player =  new RPGCharacter(name, false, new Ranger(110.0,chooseBow(name,scan),110,
                                                new Evade()), startLevel);
                    break;
                case 3: //io.github.philliptwl.textrpg_spring.Mage
                    name = namePlayerCharacter(scan,"Mage");
                    player =  new RPGCharacter(name, false, new Mage(100.0,chooseStaff(name,scan),115,
                                                new Restore()), startLevel);
                    break;
                default:
                    optInvalid("character class");
            }
        }
        return player;
    }

    public static String namePlayerCharacter(Scanner scan, String role){
        typeOut("What do they call you " + role + "?",25);
        String name = "";
        while (name.isEmpty()) {
            name = scan.nextLine().trim();
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static Sword chooseSword(String name,Scanner scan) {
        String weapon = "swords";
        selectWeaponDialogue(name, weapon);

        String[] names = {"Shadowfang", "Embercleaver", "Frostbane"};
        Sword sword = null;

        while (sword == null) {
            menu(names, false);
            int response = scan.nextInt();
            switch (response) {
                case 1:
                    sword = new Sword(names[0], 25.0);
                    break;
                case 2:
                    sword = new Sword(names[1], 20.0);
                    break;
                case 3:
                    sword = new Sword(names[2], 23.0);
                    break;
                default:
                    optInvalid("sword");
                    break;
            }
        }
        return sword;
    }

    public static Bow chooseBow(String name, Scanner scan) {
        String weapon = "bows";
        selectWeaponDialogue(name, weapon);

        String[] names = {"Quicksilver", "Moonshot", "Heartseeker"};
        Bow bow = null;

        while (bow == null) {
            menu(names, false);
            int response = scan.nextInt();
            switch (response) {
                case 1:
                    bow = new Bow(names[0], 10);
                    break;
                case 2:
                    bow = new Bow(names[1], 13);
                    break;
                case 3:
                    bow = new Bow(names[2], 12);
                    break;
                default:
                    optInvalid("weapons");
                    break;
            }
        }
        return bow;
    }

    public static Staff chooseStaff(String name,Scanner scan) {
        String weapon = "staves";
        selectWeaponDialogue(name, weapon);

        String[] names = {"Voidlight Scepter", "Stormoak Rod", "Dawnfire Aegis"};
        Staff staff = null;

        while (staff == null) {
            menu(names, false);
            int response = scan.nextInt();
            switch (response) {
                case 1:
                    staff = new Staff(names[0], 17);
                    break;
                case 2:
                    staff = new Staff(names[1], 15);
                    break;
                case 3:
                    staff = new Staff(names[2], 14);
                    break;
                default:
                    optInvalid("staff");
                    break;
            }
        }
        return staff;
    }

    public static void selectWeaponDialogue(String name, String weapon) {
        typeOut(String.format("""
                %s is it? I see, well then you will be needing a weapon. Which of these %s catches your eye?
                """, name, weapon), 25);
    }

    public static void optInvalid(String option){
        typeOut(String.format("""
                Please choose a valid %s.
                """,option),25);
    }

    public static void typeOut(String s, long baseMs) {
        long next = System.nanoTime();
        for (char c : s.toCharArray()) {
            System.out.print(c);
            System.out.flush();

            long extra = (c == '.' || c == '!' || c == '?') ? baseMs * 6
                    : (c == ',' || c == ';' || c == ':') ? baseMs * 3 : 0;

            next += (baseMs + extra) * 1_000_000L;
            long waitMs = Math.max(0, (next - System.nanoTime()) / 1_000_000L);
            try { Thread.sleep(waitMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }

            while (System.nanoTime() < next) Thread.onSpinWait();
        }
        System.out.println();
    }

    public static void gameIntro() {
        typeOut(String.format("""
            The world of %s has always teetered between light and shadow.

            Kingdoms rise, only to fall into dust. Old gods whisper from forgotten temples.

            Strange stars burn above, heralding change.
            """, world), 25);

        typeOut(String.format("""
            The lantern you see is mine. They call me the Dawnwatcher.

            I stand where the road meets the dark, tending one flame for travelers of %s.
            """, world), 25);

        typeOut("""
            Step into the light, that I may see your eyes.

            Fate turns to you now.

            Will you walk the path of blade, weapon, or spell?

            Will you carve your legend in steel and fire, or vanish like so many nameless wanderers before you?
            """, 25);

        typeOut("""
            Tell me, traveler…
            Who are you?
            """, 25);
    }

    public static void postCharacterCreation(RPGCharacter player){
        typeOut(String.format("""
                        Ah, %s, excellent choice.
                        
                        These are worrying times, %s. The ash has been a blight upon this world.
                        
                        Some whisper the ash has a master...
                        
                        The one they call %s...
                        """, player.getRole().getWeapon().name(),player.getName(),villain), 25);

        typeOut(String.format("""
                        You know...legend tells of a %s that will save %s from the clutches of %s.
                        
                        I pray that you, %s, are that hero...
                        """,
                player.getRole().type(), world, villain, player.getName()), 25);
        typeOut(String.format("""
                        The roads of %s darken by the day. Ash rides the wind; even the rivers carry cinders.

                        To stand against %s, you'll need more than steel and magic. You'll need a spark the ash cannot smother.
                
                        Your first steps lead to the Cinder Gate. Beyond it, the path divides north into the Whispering Fields,
                        
                        east along the Old Road.
                
                        Walk steady, %s. All of %s is watching.
                        """, world, villain, player.getName(),world), 25);
    }

    public static void menu(String[] items, boolean end){
        for (int i = 0; i < items.length; i++){
            System.out.printf("""
                    [%d] %s
                    """,(i+1), items[i]);
        }
        if(end) {
            System.out.println("""
                    
                    [0] End Your Journey
                    """);
        }
    }

    public static ArrayList<String> selectPath(ArrayList<String> path, String route1, String route2, Scanner scan){
        int response = -1;

        while(response == -1) {
            response = scan.nextInt();
            switch (response) {
                case 1:
                    path.add(route1);
                    break;
                case 2:
                    path.add(route2);
                    break;
                default:
                    response = -1;
                    optInvalid("path");
            }
        }
        return path;
    }

    public static ArrayList<String> startJourney(RPGCharacter player,ArrayList<String> path, Scanner scan) {
        typeOut(String.format("""
            The Cinder Gate creaks as it parts, and the road spills into the wilds of %s.

            Ash drifts like pale snow. Far off, a bell tolls once.
            
            "Remember," says the Dawnwatcher, lifting the lantern, "the shadow of %s is long...but not endless."
            """, world, villain), 25);

        typeOut("""
            You tighten your straps and step beyond the light.

            To the North, the Whispering Fields breathe in waves of silver grass.
            
            To the East, the Old Road runs toward Embercross and its watch-fires.
            """, 25);

        typeOut(String.format("""
            Your first choice will set the rhythm of your legend, %s.

            Where do your feet carry you?
            """, player.getName()), 25);

        menu(new String[]{"Head North into the Whispering Fields","Follow the Old Road East to Embercross"}, false);

        return selectPath(path,"Whispering Fields","Embercross", scan);
    }

    public static ArrayList<String> decisionSecondPath(RPGCharacter player, ArrayList<String> path, Scanner scan) {
        String path1;
        String path2;

        switch (path.get(0)) {
            case "Whispering Fields":
                path1 = "Cross the River Ford";
                path2 = "Descend to the Sunken Shrine";

                typeOut(String.format("""
                    You leave the Whispering Fields behind and come to another break in the road.
                    
                    One path leads to the boiling River Ford.

                    The other descends into a Sunken Shrine, lost to time and ash.

                    Where does your ambition lead, %s?
                    """, player.getName()), 25);
                menu(new String[]{path1, path2}, false);
                break;

            case "Embercross":
                path1 = "Climb the Shattered Aqueduct";
                path2 = "Enter the Cinder Orchard";

                typeOut(String.format("""
                    Embercross smolders behind and you again travel further down the road.
                    
                    To the east, a Shattered Aqueduct ribs the sky with broken arches.

                    To the west, a Cinder Orchard whispers under gray boughs and ember-lit fruit.

                    Which way, %s?
                    """, player.getName()), 25);
                menu(new String[]{path1, path2}, false);
                break;
        }
        return pathDiverges(path,path.size()-1,"Whispering Fields","Embercross",
                "River Ford", "Sunken Shrine","Shattered Aqueduct",
                "Cinder Orchard",scan);
    }

    public static ArrayList<String> pathDiverges(ArrayList<String> path, int split, String previousPath1,
                                                 String previousPath2, String newPath1, String newPath2, String newPath3,
                                                 String newPath4, Scanner scan){
        String currentPath = path.get(split);

        if (currentPath.equals(previousPath1)) {
            selectPath(path, newPath1, newPath2, scan);
        } else if (currentPath.equals(previousPath2)) {
            selectPath(path, newPath3, newPath4, scan);
        }

        return path;
    }

    public static boolean firstEncounter(RPGCharacter player, ArrayList<String> path, Scanner scan) {
        RPGCharacter enemy;
        RPGCharacter victorious = null;
        int fight = 0;

        switch (path.get(0)) {
            case "Whispering Fields":
                enemy = new RPGCharacter("Giant Ash-Wolf", false, new Enemy(70.0,new Fangs(
                                        "Razor-Sharp Fangs", 12.0),50, new Evade()), 1
                                );

                typeOut("""
                        Silver grass brushes your knees as you step into the Whispering Fields.
                        
                        The wind carries voices of long forgotten souls.
            
                        Ahead, the stalks part. A Giant Ash-Wolf pads into view, ribs like blackened lattice,
                        
                        eyes ember-bright; standing at the height of two men.
                        
                        It hunches down, bears its teeth and growls at you with hunger in its eyes.
                        """, 25);
                typeOut(String.format("""
                        The Fields watch, and the lantern-light is far behind.
            
                        You draw %s and steel yourself, ready to battle the ferocious beast!
                        """, player.getRole().getWeapon().name()), 25);

                victorious = combat(player,enemy,scan);
                fight = 1;

                break;

            case "Embercross":
                enemy = new RPGCharacter("Ember Knight", false, new Enemy(75.0,new Sword(
                                        "Rusted Ember Sword",13.0),50, new Charge()), 1
                                );
                typeOut(String.format("""
                        The Old Road unwinds like a scar. Milestones lean; cart tracks vanish into ash.
                        
                        A toppled wagon smolders ahead, its wheels half-buried.
            
                        A lone Ember Knight stands guard, armor soot-dulled, visor glowing like a coal.
                        
                        The sigil of %s is scorched into his shield.
                        """, villain), 25);
                typeOut(String.format("""
                        Your foot steps alert the Knight! He draws his sword and bellows a hellish shriek of the ember-cursed.
                        
                        You draw %s and ready for combat for this Knight intends to take your head.
                        """,player.getRole().getWeapon().name()),25);

                victorious = combat(player,enemy,scan);
                fight = 2;

                break;

        }

        assert victorious != null;
        return combatDecisionFirstEncounter(player, scan, victorious, fight);
    }

    public static void victoryAshWolf(RPGCharacter player) {
        typeOut("""
            With a final snarl, the Giant Ash-Wolf comes apart. No blood, only drifting cinders and a curl of smoke.
            
            The Whispering Fields fall quiet.
            """, 25);

        typeOut("""
            You steady your breath and lower your weapon. Ember-bright eyes gutter out, leaving only gray.
            
            A brittle fang of cooled ash rests where the beast fell.
            """, 25);

        typeOut(String.format("""
            "First step taken," the wind seems to say.
            
            Walk on, %s, the Fields will remember.
            """, player.getName()), 25);
    }

    public static void victoryEmberKnight(RPGCharacter player) {
        typeOut("""
            With a hollow clang, the Ember Knight drops to one knee. Cracks glow, then dim;
            
            the heat sighs out and the coal within gutters to gray.

            The Old Road falls still.
            """, 25);

        typeOut("""
            You steady your breath and lower your weapon. Soot drifts like snow around the ruined helm.
            
            Among the ash lies a shard of emberplate, its sigil of the Ash King scorched but unbroken.
            """, 25);

        typeOut(String.format("""
            "One lantern brighter," the Dawnwatcher would say.

            Walk on, %s, the Old Road will remember.
            """, player.getName()), 25);
    }

    public static boolean combatDecisionFirstEncounter(RPGCharacter player, Scanner scan, RPGCharacter victorious, int fight) {
        assert victorious != null;
        if(victorious.equals(player)){
            switch(fight){
                case 1:
                    victoryAshWolf(player);
                    return false;
                case 2:
                    victoryEmberKnight(player);
                    return false;
            }
        }else {
            return isGameOver(player,scan,false);
        }
        return true;
    }

    public static boolean secondEncounter(RPGCharacter player, ArrayList<String> path, Scanner scan) {
        RPGCharacter enemy;
        RPGCharacter victorious = null;
        String name = player.getName();
        int fight = 0;

        switch (path.get(1)) {
            case "River Ford":
                enemy = new RPGCharacter("Cinder Serpent", false, new Enemy(65.0, new Fangs("Ember Fangs",
                                        14.0), 60, new Evade()), 2
                                );

                typeOut(String.format("""
                        You head down to the River Ford, overcome with a heavy scent of sulfur and scalding steam.
                        
                        The river runs low and hot, steaming over black stones.
                        
                        A long shape uncoils in the shallows, scales like banked coals, eyes like sparks.
                        
                        The Cinder Serpent lifts its head, tasting the ash on the wind.
                        
                        It locks eyes with you, bearing its blackened fangs and hissing, ready to strike.
                        
                        Ready yourself, %s, time remembers not the weak!
                        """,name), 25);

                victorious = combat(player,enemy,scan);
                fight = 1;

                break;
            case "Sunken Shrine":
                enemy = new RPGCharacter("Ash Wraith", false, new Enemy(55.0, new Sword("Spectral Scythe",
                                        14.0), 80, new Evade()), 2
                                );

                typeOut(String.format("""
                        You descend into the Sunken Shrine, wary of the horrors that lurk in this holy place.
                        
                        Steps sag into a drowned chapel. Candles gutter under gray water.
                        
                        From the altar, a figure lifts, ragged as smoke, crowned in cold cinderlight.
                        
                        The Ash Wraith drifts toward you without a ripple.
                        
                        Your heart pounds and your blood runs cold, you can feel your muscles turn to stone.
                        
                        Summon the courage to vanquish this spectre, %s, lest your soul be entombed here as well!
                        """,name), 25);

                victorious = combat(player,enemy,scan);
                fight = 2;

                break;
            case "Shattered Aqueduct":
                enemy = new RPGCharacter("Gutter Ghoul", false, new Enemy(62.0, new Fangs("Filth-Slick Claws",
                                        13.0), 60, new Evade()), 2
                                );

                typeOut(String.format("""
                        The old aqueduct looms in broken ribs over the road.
                        
                        You begin your climb, gripping the crumbling stone.
                        
                        Water hisses through cracked channels, falling as warm mist.
                        
                        You find a ledge leading into an opening in the aqueduct, giving you but a moment of respite.
                        
                        In the shadowed arches, something drips... then moves.
                        
                        A Gutter Ghoul crawls into view, jaw unhinged, claws slick with runoff.
                        
                        There is no room for retreat, %s. Stand ready!
                        """,name), 25);

                victorious = combat(player,enemy,scan);
                fight = 3;

                break;
            case "Cinder Orchard":
                enemy = new RPGCharacter("Cinder Treant", false, new Enemy(80.0, new Fangs("Charred Limbs",
                                        16.0), 50, new Charge()), 2
                                );

                typeOut(String.format("""
                        You venture into the Cinder Orchard leaving Embercross to fade in the ash.
                        
                        Charred trunks stand like blackened pillars. Wind stirs, and embers glow under ash.
                        
                        Suddenly, you feel the earth begin to quake.
                        
                        Roots heave; a Cinder Treant wrenches free of the soil, bark split with slow fire.
                        
                        The beast lumbers toward you without intent to stop.
                        
                        Either this foe will fall, or you will, %s!
                        """,name), 25);

                victorious = combat(player,enemy,scan);
                fight = 4;

                break;
        }
        assert victorious != null;
        return combatDecisionSecondEncounter(player, scan, victorious, fight);
    }

    public static void victoryCinderSerpent(RPGCharacter player) {
        typeOut("""
        With a final lash, the Cinder Serpent slackens and sinks. Steam shrieks, then thins;
        
        red scales dull to gray. The River Ford runs quiet.
        """, 25);

        typeOut("""
        You steady your breath and lower your weapon. Heat lifts from the stones.
        
        In the shallows lies an ember-scale, cooled but faintly warm to the touch.
        """, 25);

        typeOut(String.format("""
        The River Ford takes your name into its current, %s.
        
        Cross while the steam thins.
        """, player.getName()), 25);
    }

    public static void victoryAshWraith(RPGCharacter player) {
        typeOut("""
        With a sigh like snuffed incense, the Ash Wraith unravels, cinderlight fades,
        
        and the drowned chapel falls still.
        """, 25);

        typeOut("""
        You steady your breath and lower your weapon. Ripples smooth to glass.
        
        Upon the altar remains a shard of soot-dark glass, cold as midnight.
        """, 25);

        typeOut(String.format("""
        The drowned bells keep no toll, only your passage, %s.
        
        Leave these stones to their silence.
        """, player.getName()), 25);
    }

    public static void victoryGutterGhoul(RPGCharacter player) {
        typeOut("""
        With a wet rattle, the Gutter Ghoul collapses; runoff washes the grime away,
        
        and mist threads the broken arches of the aqueduct.
        """, 25);

        typeOut("""
        You steady your breath and lower your weapon. Drips count the seconds.
        
        In a puddle gleams a rusted rivet stamped with an old maker’s mark.
        """, 25);

        typeOut(String.format("""
        Drip by drip, the arches carry word of you, %s.
        
        Move on before the mist remembers its hunger.
        """, player.getName()), 25);
    }

    public static void victoryCinderTreant(RPGCharacter player) {
        typeOut("""
        With a final crack, the Cinder Treant splits; embers scatter and fade,
        
        and the Orchard exhales a long, ashen breath.
        """, 25);

        typeOut("""
        You steady your breath and lower your weapon. Char flurries drift like black snow.
        
        At your feet smolders a knot of char-heart, bead of ember-sap sealed within.
        """, 25);

        typeOut(String.format("""
        Among the cinders, a seed holds one ember. Let it be yours, %s.
        
        Walk until green returns.
        """, player.getName()), 25);
    }

    public static boolean combatDecisionSecondEncounter(RPGCharacter player, Scanner scan, RPGCharacter victorious, int fight) {
        assert victorious != null;
        if(victorious.equals(player)){
            switch(fight){
                case 1:
                    victoryCinderSerpent(player);
                    return false;
                case 2:
                    victoryAshWraith(player);
                    return false;
                case 3:
                    victoryGutterGhoul(player);
                    return false;
                case 4:
                    victoryCinderTreant(player);
                    return false;
            }
        }else {
           return isGameOver(player,scan,false);
        }
        return true;
    }

    public static void pathConverges(ArrayList<String> path){

        typeOut("""
                You come to the final obstacle on the path to the Obsidian Gates.
                """,25);

        switch (path.get(1)){
            case "River Ford", "Sunken Shrine":
                typeOut("""
                        The Cinder Court...
                        """,25);
                path.add("Cinder Court");
                break;
            case "Shattered Aqueduct", "Cinder Orchard":
                path.add("Funeral Stair");
                typeOut("""
                        The Funeral Stair...
                        """,25);
                break;
        }
    }

    public static boolean thirdEncounter(RPGCharacter player, ArrayList<String> path, Scanner scan) {
        RPGCharacter enemy;
        RPGCharacter victorious = null;
        String name = player.getName();
        int fight = 0;

        switch (path.get(2)) {
            case "Cinder Court":
                enemy = new RPGCharacter("Ash Cantor", false, new Enemy(88.0,
                                        new Staff("Cinder Crozier", 17.0), 120, new Evade()),
                        3
                                );
                typeOut(String.format("""
                        You step into the Cinder Court, a blasted garden of black statues and soot-pale shrubs.
                        
                        Your eyes scan amongst the court for any semblance of life.
                        
                        A robed figure emerges from behind a cracked fountain, voice threading ash into shapes.
                        
                        The Ash Cantor turns, lifts the crozier, and the cinders rise to meet you.
                        
                        You are so close to %s, do not falter, %s.
                        """,villain,name), 25);

                victorious = combat(player,enemy,scan);
                fight = 1;

                break;
            case "Funeral Stair":
                enemy = new RPGCharacter("Pyre Castellan", false, new Enemy(100.0,
                                        new Sword("Branding Halberd", 19.0), 90, new Charge()),
                        3
                                );
                typeOut(String.format("""
                        You climb the Funeral Stair; broad steps mantled in ash, lanterns guttering in iron niches.
                        
                        You strain your eyes to see. Then , on the edges of your vision, a shadow moves..
                        
                        A mailed figure waits halfway up, visor aglow like banked coals.
                        
                        The Pyre Castellan lowers his halberd across the steps, barring the way.
                        
                        Defy death, %s, %s lies just beyond this battle.
                        """,name,villain), 25);

                victorious = combat(player,enemy,scan);
                fight = 2;

                break;
        }
        assert victorious != null;
        return combatDecisionThirdEncounter(player,scan,victorious,fight);
    }

    public static void victoryAshCantor(RPGCharacter player) {
        typeOut("""
                The crozier cracks; the ash-song tears itself apart in a hiss of cinders.
                
                Robes collapse like burned parchment, and the Cinder Court holds its breath once more.
                """, 25);

        typeOut("""
                You steady your breath and lower your weapon. Soot drifts from the statues’ brows.
                
                In the cracked basin, a small note-stone remains, black glass etched with a single mute line.
                """, 25);

        typeOut(String.format("""
                This is it, %s...
                
                The Obsidian Gates await...
                """,player.getName()), 25);
    }

    public static void victoryPyreCastellan(RPGCharacter player) {
        typeOut("""
                With a splintering clang, the Pyre Castellan’s halberd skitters down the ash-mantled steps.
                
                The glow behind his visor gutters; ash sloughs from the plates, and the Funeral Stair falls silent.
                """, 25);

        typeOut("""
                You steady your breath and lower your weapon. Heat leaks from the iron niches along the stair.
                
                At your feet lies a scorched tassel from the Castellan’s crest, edges crisped to black.
                """, 25);

        typeOut(String.format("""
                This is it, %s...
                
                The Obsidian Gates await...
                """,player.getName()), 25);
    }

    public static boolean combatDecisionThirdEncounter(RPGCharacter player, Scanner scan, RPGCharacter victorious, int fight) {
        assert victorious != null;
        if(victorious.equals(player)){
            switch(fight){
                case 1:
                    victoryAshCantor(player);
                    return false;
                case 2:
                    victoryPyreCastellan(player);
                    return false;
            }
        }else {
            return isGameOver(player,scan,false);
        }
        return true;
    }

    public static boolean encounterAshKing(RPGCharacter player, Scanner scan) {
        RPGCharacter victorious;
        String name = player.getName();
        Role rPlayer = player.getRole();
        String weapon = rPlayer.getWeapon().name();

         RPGCharacter theAshKing = new RPGCharacter(
                          villain,
                 false,
                 new Enemy(200.0, new Sword("Crownblade", 22.0), 120, new Charge()), 4
                  );

        typeOut("""
                You heave open the Obsidian Gates and approach the castle, its spires clawing at an ashen sky.
        
                The castle doors yaw open on iron hinges. Ash eddies in the draft as you cross the threshold of the throne room.
        
                Black banners hang like nightfall. A dead fire smolders in the braziers, painting the world in ember-red.
        
                Upon the charred throne sits a monarch of cinder and shadow. The crown is a halo of flame that will not die.
                """, 25);

        typeOut(String.format("""
                "%s," the voice says quietly, and all the more terrible for it. "You have come farther than most, but all roads end here."
    
                He lifts one hand. Ash rises from the floor, gathers like storm clouds, and stills.
                """, name), 25);

        typeOut(String.format("""
                "Kneel, and I will let your name drift like ash into wind. Stand, and be cast down in flame."
    
                %s leans forward. The room seems smaller, the air thinner.
                """, villain), 25);

        typeOut(String.format("""
                You set your stance and draw %s. Your heartbeat counts the distance between you.
                
                %s, enraged by your defiance shouts,
                """, weapon,villain), 25);
        typeOut("""
                "YOU WILL PERISH IN FLAME AND ASH!!!"
                """,50);
        victorious = combat(player,theAshKing,scan);

        return combatDecisionAshKing(player,scan,victorious);
    }

    public static boolean combatDecisionAshKing(RPGCharacter player, Scanner scan, RPGCharacter victorious) {
        if(victorious.equals(player)){
            victoryAshKing(player);
            return false;
        }else {
            defeatAshKing(player);
            return isGameOver(player,scan,false);
        }
    }

    public static void victoryAshKing(RPGCharacter player) {
        typeOut("""
                Your blow lands true. The crown of coals flares white then bursts.
                
                The Ash King staggers, cracks racing through a body of cinder and night,
                
                and collapses upon the charred throne in a hush of falling ash.
                """, 25);

        typeOut("""
                Banners sag. Emberlight fades from the braziers until only honest darkness remains.
                
                At your feet lies a fractured circlet-stone, still warm, pulsing once… then still.
                """, 25);

        typeOut(String.format("""
                The long dusk breaks.
    
                Rise, %s. The throne no longer answers to ash and the world will remember who unseated it.
                """, player.getName()), 25);
    }

    public static void defeatAshKing(RPGCharacter player) {
        typeOut("""
                The crown brightens; ash swarms like a storm.
                
                A single sweep scatters your guard. Impact blooms in your ribs and the floor rushes up like night.
                """, 25);

        typeOut("""
                Your weapon slips from numb fingers. Breath tastes of cinder.
                
                Above you, the ember-halo turns, patient and unending.
                """, 25);

        typeOut(String.format("""
                "All fires come home," the Ash King murmurs. "Rest, %s."
    
                The lantern goes out, and the throne room keeps its silence.
                """, player.getName()), 25);
    }

    public static RPGCharacter combat(RPGCharacter player, RPGCharacter enemy, Scanner scan) {
        Role rEnemy = enemy.getRole();
        Role rPlayer = player.getRole();
        String weapon = rPlayer.getWeapon().name();
        boolean dodge;
        RPGCharacter victor = null;

        while(!enemy.isDead() && !player.isDead()) {
            dodge = false;
            //Status
            viewStatus(player, enemy, rEnemy, rPlayer);
            //Menu
            menu(new String[]{rPlayer.useWeapon() + " | DMG: " + String.format("%.2f",rPlayer.getWeapon().damage()),
                    rPlayer.getAbility().getName() + " | RESOURCE COST: " + rPlayer.getAbility().cost()}, false);

            int response = scan.nextInt();
            boolean moveEnemy = moveEnemyRandom();

            //Action Choice
            switch (response) {
                case 1: //Use io.github.philliptwl.textrpg_spring.Weapon
                    if (moveEnemy && rEnemy.getAbility().type().equals("evasion")){
                        typeOut(String.format("""
                                %s evades your attack!
                                """,enemy.getName()),25);
                    }else {
                        rEnemy.setHealth(rEnemy.getHealth() - rPlayer.getWeapon().damage());
                        typeOut(String.format("""
                                You %s!
                                
                                You strike %s with %s, for %.2f damage!
                                """,rPlayer.getWeapon().attackType(),enemy.getName(), weapon, rPlayer.getWeapon().damage()), 25);
                    }
                    break;

                case 2: //Use io.github.philliptwl.textrpg_spring.Ability
                    if(rPlayer.getResource() >= rPlayer.getAbility().cost()) {
                        switch (rPlayer.getAbility().type()) {
                            case "damage":
                                rPlayer.setResource(rPlayer.useAbility());
                                if (moveEnemy && rEnemy.getAbility().type().equals("evasion")){
                                    typeOut(String.format("""
                                            %s evades your %s!
                                            
                                            Your rage builds...
                                            """,enemy.getName(),rPlayer.getAbility().getName())
                                            ,25);
                                }else {
                                    rEnemy.setHealth(rPlayer.getWeapon().useWeapon(rEnemy.getHealth()));
                                    typeOut(String.format("""
                                            You charge %s head first and strike them in the chest knocking them back!
                                            
                                            Your rage is sated, for now...
                                            """,enemy.getName()),25);
                                }
                                break;
                            case "evasion":
                                rPlayer.setResource(rPlayer.useAbility());
                                if (moveEnemy && rEnemy.getAbility().type().equals("evasion")){
                                    typeOut("""
                                            Your foe matches your attempt to evade the other's attack.
                                            
                                            They seem cunning...
                                            """,25);
                                }else{
                                    typeOut(String.format("""
                                            You evade %s's attack!
                                            
                                            Your speed and cunning have no equal...
                                            """,enemy.getName()),25);

                                }
                                dodge =  true;
                                break;
                            case "heal":
                                rPlayer.setResource(rPlayer.useAbility());
                                if (rPlayer.getHealth() >= 90.0) {
                                    typeOut("""
                                            Your attempt to heal beyond your capacity fails, a foolish endeavor no doubt...
                                            """, 25);
                                } else {
                                    rPlayer.setHealth(rPlayer.getHealth() + rPlayer.getAbility().getHeal());
                                    typeOut(String.format("""
                                            You use the aether to restore your life-force and mend your wounds!
                                            
                                            You heal for %.2f and feel the energy flow within your veins...
                                            """,rPlayer.getAbility().getHeal()),25);
                                }
                                break;
                            default:
                                optInvalid("combat option");
                        }
                    }else {
                        typeOut("""
                                You have exhausted yourself! Your vision swims and your reserves run dry.
                                
                                You cannot use that ability right now...
                                """,25);
                    }
            }
            if(rEnemy.getHealth() <= 0) {
                enemy.setDead(true);
                victor = player;
                break;
            }
            if(!dodge) {
                if (enemyHitChance()) {
                    if (!moveEnemy) {
                        rPlayer.setHealth(rEnemy.getWeapon().useWeapon(rPlayer.getHealth()));
                        typeOut(String.format("""
                                %s strikes with %s!
                                
                                The attack hits! You lose %.2f of your life-force.
                                """, enemy.getName(), rEnemy.getWeapon().name(), rEnemy.getWeapon().damage()), 25);
                    }
                    if (moveEnemy && rEnemy.getAbility().type().equals("damage")) {
                        rPlayer.setHealth(rPlayer.getHealth() - rEnemy.getAbility().getDamage());
                        typeOut(String.format("""
                                %s charges at you with hatred in their eyes!
                                
                                The attack hits! You lose %.2f of your life-force.
                                """, enemy.getName(), rEnemy.getAbility().getDamage()), 25);
                    }
                } else {
                    typeOut(String.format("""
                            %s misses their attack!
                            
                            Fate smiles upon you, hero...
                            """, enemy.getName()), 25);
                }
            }
            if (rPlayer.getHealth() <= 0) {
                typeOut(String.format("""
                    %s strikes a fatal blow!
                    
                    You fall to your knees and drop %s at your side as the world turns dark...
                    """,enemy.getName(),rPlayer.getWeapon().name()),25);
                player.setDead(true);
                victor = enemy;
                break;
            }else {
                typeOut(String.format("""
                        You reset your stance and ready %s. Be wary of the next attack, hero...
                        """, rPlayer.getWeapon().name()), 25);
            }
        }
        return victor;
    }

    public static void viewStatus(RPGCharacter player, RPGCharacter enemy, Role rEnemy, Role rPlayer) {
        typeOut(String.format("""
                %s: %.2f HEALTH | LEVEL: %d
                """, enemy.getName(), rEnemy.getHealth(), enemy.getLevel()), 25);
        typeOut(String.format("""
                %s: %.2f HEALTH | LEVEL: %d | RESOURCE: %d
                """, player.getName(), rPlayer.getHealth(), player.getLevel(), rPlayer.getResource()),25);
    }

    public static boolean moveEnemyRandom(){
        double random = Math.random();

        return random < 0.3;
    }

    public static void resetPlayerStats(RPGCharacter player) {
        player.getRole().resetHealth();
        player.getRole().resetResource();
    }

    public static void increasePlayerLevel(RPGCharacter player, int newLevel) {
        Role rPlayer = player.getRole();
        int currentResource = rPlayer.getResource();
        double currentHealth = rPlayer.getHealth();

        player.setLevel(newLevel);
        rPlayer.setResource((int) Math.round(currentResource + ((currentResource / 20.0) * (newLevel + (newLevel / 2.0)))));
        rPlayer.setHealth(currentHealth + ((currentHealth / 20) * (newLevel + ((double) newLevel / 2))));
        rPlayer.getWeapon().setWeaponDamage((rPlayer.getWeapon().damage() * 1.2));

        typeOut(String.format("""
                Your light shines brighter from the battle and you gain power and knowledge!
                
                LEVEL: %d | HEALTH: %.2f | RESOURCE: %d | WEAPON DAMAGE: %.2f
                """,player.getLevel(),rPlayer.getHealth(),rPlayer.getResource(),rPlayer.getWeapon().damage()),25);
    }

    public static boolean enemyHitChance(){
        double random = Math.random();

        return random > 0.3;
    }

    public static void gameOutro(RPGCharacter player, ArrayList<String> path){
        String output = "";
        Role rPlayer = player.getRole();

        typeOut("""
            You step from the throne room into a hall of cooling stone. The ash-quiet is different now.
            
            Behind you, a broken crown cools; ahead, the road runs home.
            """, 25);

        typeOut(String.format("""
            Hours later, the lantern at the Cinder Gate burns steady.
            
            The Dawnwatcher is waiting.

            "So. You return, %s."
            """, player.getName()), 25);
        typeOut("""
                He reaches out, touches your shoulder and closes his eyes.
                
                "Yes, I can see all of those shadows you faced..."
                """,25);

        for (String s : path) {
            output = switch (s) {
                case "Whispering Fields" -> """
                        In the Whispering Fields you unraveled the Giant Ash-Wolf into drifting cinders.
                        """;
                case "Embercross" -> """
                        At Embercross you shattered the Ember Knight; his emberplate cooled to gray.
                        """;
                case "River Ford" -> """
                        Along the boiling River Ford you severed the Cinder Serpent; steam screamed, then fell silent.
                        """;
                case "Sunken Shrine" -> """
                        Within the Sunken Shrine you unbound the Ash Wraith; the drowned bells kept no toll.
                        """;
                case "Shattered Aqueduct" -> """
                        On the Shattered Aqueduct you toppled the Gutter Ghoul; the mist carried its last rattle away.
                        """;
                case "Cinder Orchard" -> """
                        In the Cinder Orchard you felled the Cinder Treant among the charred boughs.
                        """;
                case "Cinder Court" -> """
                        In the Cinder Court you silenced the Ash Cantor; the ash-song fell to a hush.
                        """;
                case "Funeral Stair" -> """
                        On the Funeral Stair you broke the Pyre Castellan; his halberd clattered down the steps.
                        """;
                default -> output;
            };
            typeOut(output, 25);
        }
        typeOut(String.format("""
                But your greatest feat, you accomplished the impossible...
                
                You, %s, you vanquished %s. His reign is no more.
                """,player.getName(),villain),25);

        typeOut(String.format("""
            Take your rest, %s. The shadows of the Ash King will soon fade from this place.
            
            %s remembers more than ash, now.
            
            It will forever remember the great %s %s, the hero who stood against the fires of %s.
            """, player.getName(),world,rPlayer.type(),player.getName(),villain), 25);
    }

    public static void logo(){
        System.out.println();
        typeOut("""
                       ▄████████    ▄████████    ▄█    █▄       ▄████████    ▄████████       ▄██████▄     ▄████████         ▄████████    ▄████████     ███        ▄█    █▄       ▄████████  ▄██████▄     ▄████████
                      ███    ███   ███    ███   ███    ███     ███    ███   ███    ███      ███    ███   ███    ███        ███    ███   ███    ███ ▀█████████▄   ███    ███     ███    ███ ███    ███   ███    ███
                      ███    ███   ███    █▀    ███    ███     ███    █▀    ███    █▀       ███    ███   ███    █▀         ███    ███   ███    █▀     ▀███▀▀██   ███    ███     ███    ███ ███    ███   ███    █▀
                      ███    ███   ███         ▄███▄▄▄▄███▄▄  ▄███▄▄▄       ███             ███    ███  ▄███▄▄▄            ███    ███  ▄███▄▄▄         ███   ▀  ▄███▄▄▄▄███▄▄  ▄███▄▄▄▄██▀ ███    ███   ███
                    ▀███████████ ▀███████████ ▀▀███▀▀▀▀███▀  ▀▀███▀▀▀     ▀███████████      ███    ███ ▀▀███▀▀▀          ▀███████████ ▀▀███▀▀▀         ███     ▀▀███▀▀▀▀███▀  ▀▀███▀▀▀▀▀   ███    ███ ▀███████████
                      ███    ███          ███   ███    ███     ███    █▄           ███      ███    ███   ███               ███    ███   ███    █▄      ███       ███    ███   ▀███████████ ███    ███          ███
                      ███    ███    ▄█    ███   ███    ███     ███    ███    ▄█    ███      ███    ███   ███               ███    ███   ███    ███     ███       ███    ███     ███    ███ ███    ███    ▄█    ███
                      ███    █▀   ▄████████▀    ███    █▀      ██████████  ▄████████▀        ▀██████▀    ███               ███    █▀    ██████████    ▄████▀     ███    █▀      ███    ███  ▀██████▀   ▄████████▀
                                                                                                                                                                                ███    ███
                    """,2);
    }
}