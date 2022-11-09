package run.mycode.zombieland;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Scenario {
    private final List<Context> worlds = new ArrayList<>();

    private Scenario() {

    }

    private void addWorld(ZombieLand world, Objective objective) {
        worlds.add(new Context(world, objective));
    }

    /**
     * @return The number of worlds contained in this Scenario
     */
    public int numWorlds() {
        return worlds.size();
    }

    /**
     * Retrieve a world from this scenario
     * @param num The world to get a reference to [0..numWorlds)
     * @return the requested world from this scenario
     */
    public ZombieLand getWorld(int num) {
        return worlds.get(num).world;
    }

    public static Scenario load(File file) throws IOException {
        Scenario scenario = new Scenario();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            // Get a handle to the root of the world description
            Element scenarioRoot = doc.getDocumentElement();


            String zombieClass = scenarioRoot.getAttribute("zombieclass");
            if (zombieClass.length() == 0) {
                zombieClass = "Zombie";
            }

            NodeList worlds = scenarioRoot.getElementsByTagName("world");
            for (int i = 0; i < worlds.getLength(); i++) {
                Element world = (Element)worlds.item(i);
                // Set the world width and height
                int width = Integer.parseInt(world.getAttribute("width"));
                int height = Integer.parseInt(world.getAttribute("height"));

                String name = world.getAttribute("name");
                if (name.length() == 0) {
                    name = file.getName();
                }

                final ZombieLand z = new ZombieLand(width, height, name);
                final Node initial = world.getElementsByTagName("initial").item(0);
                loadState(z, initial, zombieClass);

                final Objective o = new Objective(width, height, name);
                final Node objective = world.getElementsByTagName("objective").item(0);
                loadState(o, objective, zombieClass);

                scenario.addWorld(z, o);
            }
        } catch (ParserConfigurationException | SAXException e) {
            System.err.println("Could not load file " + file.getName() + ": " + e.getMessage());
        }

        return scenario;
    }

    private static void loadState(ZombieLand zl, Node stateNode, String zombieClass) {
        NodeList initialObjects = ((Element)stateNode).getElementsByTagName("object");
        for (int i = 0; i < initialObjects.getLength(); i++) {
            // Get the object description
            Element obj = (Element) initialObjects.item(i);

            // Determine the classname that describes the object
            String className = obj.getAttribute("classname");

            // Determine where the class instances are located
            NodeList locations = obj.getElementsByTagName("location");
            for (int j = 0; j < locations.getLength(); j++) {
                Location loc = Location.from(locations.item(j));

                switch (className) {
                    case "Brain":
                        new Brain(loc.x, loc.y, loc.count, zl);
                        break;

                    case "Fire":
                        new Fire(loc.x, loc.y, zl);
                        break;

                    case "Wall":
                        new Wall(loc.x, loc.y, zl);
                        break;

                    case "ZombieGoal":
                        new ZombieGoal (loc.x, loc.y, zl);
                        break;

                    case "Bucket":
                        new Bucket(loc.x, loc.y, zl);
                        break;

                    case "MyZombie":
                    case "Zombie":
                        Element pos = (Element)locations.item(j);

                        int numBrains = 0;
                        if (pos.hasAttribute("brains")) {
                            numBrains = Integer.parseInt(pos.getAttribute("brains"));
                        }

                        zombieAt(zombieClass, numBrains, loc, zl);
                        break;
                }
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void zombieAt(String zombieClass, int numBrains, Location loc,  ZombieLand world) {
        if (zombieClass.equals("Zombie")) {
            zombieClass = "run.mycode.zombieland.Zombie";
        }

        try {
            ClassLoader cl = Scenario.class.getClassLoader();

            // Make sure the proper Zombie subclass is loaded into the Java runtime
            Class objClass = cl.loadClass(zombieClass);
            // Create an instance of the class at this location, in this world
            Constructor constructor = objClass.getDeclaredConstructor(int.class, int.class, int.class, int.class, World.class);
            constructor.newInstance(loc.x, loc.y, loc.dir, numBrains, world);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 ClassNotFoundException e) {
            throw new RuntimeException("Cannot make Zombie at (" + loc.x + "," + loc.y + ")", e);
        }
    }

    private static class Location {
        int x;
        int y;
        int dir;
        int count;

        public static Location from(Node item) {
            Location loc = new Location();

            Element pos = (Element) item;

            // Find out the coordinates of the location
            loc.x = Integer.parseInt(pos.getAttribute("x"));
            loc.y = Integer.parseInt(pos.getAttribute("y"));

            loc.dir = 0;
            if (pos.hasAttribute("dir")) {
                loc.dir = Integer.parseInt(pos.getAttribute("dir"));
            }

            // Find out how many instances are present in this location
            loc.count = 1;
            if (pos.hasAttribute("count")) {
                loc.count = Integer.parseInt(pos.getAttribute("count"));
            }

            return loc;
        }
    }

    private static class Context {
        ZombieLand world;
        Objective objective;

        public Context(ZombieLand world, Objective objective) {
            this.world = world;
            this.objective = objective;
        }
    }

    private static class Objective extends ZombieLand {

        public Objective(int width, int height, String name) {
            super(width, height, name);
        }

        @Override
        public void act() { /* Do nothing */ }

    }
}