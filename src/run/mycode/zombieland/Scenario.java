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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Scenario {
    List<ZombieLand> worlds = new ArrayList<>();

    private Scenario() {

    }

    private void addWorld(ZombieLand world) {
        worlds.add(world);
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
        return worlds.get(num);
    }

    public static Scenario load(File file) throws IOException {
        Scenario scenario = new Scenario();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            // Get a handle to the root of the world description
            Element document = doc.getDocumentElement();
            NodeList worlds = document.getElementsByTagName("world");

            for (int i = 0; i < worlds.getLength(); i++) {
                scenario.addWorld(loadWorld((Element)worlds.item(i), file.getName()));

            }
        } catch (ParserConfigurationException | SAXException e) {
            System.err.println("Could not load file " + file.getName() + ": " + e.getMessage());
        }

        return scenario;
    }

    private static ZombieLand loadWorld(Element root, String defaultName) {
        // Set the world width and height
        int width = Integer.parseInt(root.getAttribute("width"));
        int height = Integer.parseInt(root.getAttribute("height"));

        String name = root.getAttribute("name");
        if (name.length() == 0) {
            name = defaultName;
        }

        final ZombieLand zl = new ZombieLand(width, height, name);

        // Get handles to the initial and objective description nodes
        Node initial = root.getElementsByTagName("initial").item(0);
        Node objective = root.getElementsByTagName("objective").item(0);

        loadInitialState(zl, initial);
        loadGoalState(zl, objective);

        return zl;
    }

    private static void loadInitialState(ZombieLand zl, Node initial) {
//        ClassLoader cl = Scenario.class.getClassLoader();

        NodeList initialObjects = ((Element)initial).getElementsByTagName("object");
        for (int i = 0; i < initialObjects.getLength(); i++) {
            // Get the object description
            Element obj = (Element) initialObjects.item(i);

            // Determine the classname that describes the object
            String className = obj.getAttribute("classname");

            // Make sure the class is loaded into the Java runtime
//            Class objClass = cl.loadClass(className);

            // Determine where the class instances are located
            NodeList locations = obj.getElementsByTagName("location");
            for (int j = 0; j < locations.getLength(); j++) {
                Location loc = Location.from(locations.item(j), zl);

                switch (className) {
                    case "Brain":
                        brainAt(loc);
                        break;

                    case "Fire":
                        fireAt(loc);
                        break;

                    case "Wall":
                        wallAt(loc);
                        break;

                    case "ZombieGoal":
                        goalAt(loc);
                        break;

//                    case "Bucket":
//                        bucketAt(loc);
//                        break;

                    case "MyZombie":
                    case "Zombie":
                        Element pos = (Element)locations.item(j);
                        String zombieClass = pos.getAttribute("classname");
                        if (zombieClass.length() == 0) {
                            zombieClass = "Zombie";
                        }
                        int numBrains = 0;
                        if (pos.hasAttribute("brains")) {
                            numBrains = Integer.parseInt(pos.getAttribute("brains"));
                        }
                        zombieAt(zombieClass, loc, numBrains);
                        break;
                }
//                else {
//                    // Create instances at this location
//                    Constructor constructor = objClass.getConstructor();
//                    for (; count > 0; count--) {
//                        Actor a = (Actor) constructor.newInstance();
//                        zl.addObject(a, x, y);
//                        a.setRotation(dir);
//
//                        if (calls != null) {
//                            for (String[] call : calls) {
//                                Class[] params = {int.class};
//                                Method m = objClass.getMethod(call[0], params);
//                                m.invoke(a, Integer.parseInt(call[1]));
//                            }
//                        }
//                    }
//                }
            }
        }
    }

    private static void loadGoalState(ZombieLand zl, Node objective) {
    }

    private static Brain brainAt(Location loc) {
        Brain brain = new Brain(loc.x, loc.y, loc.count, loc.world);

        // Perform any method calls specified for this brain stack
        makeCalls(brain, loc);

        return brain;
    }

    @SuppressWarnings({"rawtypes"})
    private static void makeCalls(Object instance, Location loc) {
        if (loc.calls != null) {
            for (String[] call : loc.calls) {
                Class[] params = {int.class};
                try {
                    Method m = instance.getClass().getMethod(call[0], params);
                    m.invoke(instance, Integer.parseInt(call[1]));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    System.err.println("method call " + instance.getClass().getName() + "." + call[0] + " failed. " + e.getMessage());
                }
            }
        }
    }

    private static Fire fireAt(Location loc) {
        Fire fire = new Fire(loc.x, loc.y, loc.world);

        // Perform any method calls specified for this fire
        makeCalls(fire, loc);

        return fire;
    }

    private static Wall wallAt(Location loc) {
        return new Wall(loc.x, loc.y, loc.world);
    }

    private static ZombieGoal goalAt(Location loc) {
        ZombieGoal goal = new ZombieGoal (loc.x, loc.y, loc.world);

        // Perform any method calls specified for this goal
        makeCalls(goal, loc);

        return goal;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Zombie zombieAt(String zombieClass, Location loc, int numBrains) {
        if (zombieClass.equals("Zombie")) {
            zombieClass = "run.mycode.zombieland.Zombie";
        }

        Zombie z;

        try {
            ClassLoader cl = Scenario.class.getClassLoader();

            // Make sure the proper Zombie subclass is loaded into the Java runtime
            Class objClass = cl.loadClass(zombieClass);
            // Create an instance of the class at this location
            Constructor constructor = objClass.getDeclaredConstructor(int.class, int.class, int.class, int.class, World.class);
            z = (Zombie) constructor.newInstance(loc.x, loc.y, loc.dir, numBrains, loc.world);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 ClassNotFoundException e) {
            throw new RuntimeException("Cannot make Zombie at (" + loc.x + "," + loc.y + ")", e);
        }

        // Perform any method calls specified for this goal
        makeCalls(z, loc);

        return z;
    }

    private static class Location {
        int x;
        int y;
        int dir;
        int count;
        List<String[]> calls;

        World world;


        public static Location from(Node item, ZombieLand zl) {
            Location loc = new Location();

            loc.world = zl;

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

            NodeList callList = pos.getElementsByTagName("call");
            loc.calls = null;
            if (callList.getLength() > 0) {
                loc.calls = new ArrayList<>();

                for (int k = 0; k < callList.getLength(); k++) {
                    Element method = ((Element) callList.item(k));
                    String[] callSignature = new String[2];
                    callSignature[0] = method.getAttribute("name");
                    callSignature[1] = method.getAttribute("value");

                    loc.calls.add(callSignature);
                }
            }

            return loc;
        }
    }
}