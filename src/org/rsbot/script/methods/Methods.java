package org.rsbot.script.methods;

import org.rsbot.bot.input.Listener;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.wrappers.RSPlayer;
import org.rsbot.util.ScreenshotUtil;

import java.util.logging.Logger;

/**
 * Provides access to methods that can be used by RSBot scripts.
 */
public class Methods {

    /**
     * The logger instance
     */
    protected Logger log = Logger.getLogger(getClass().getName());

    /**
     * The instance of {@link java.util.Random} for random number generation.
     */
    protected java.util.Random random = new java.util.Random();

    /**
     * The singleton of Skills
     */
    protected Skills skills;

    /**
     * The singleton of Settings
     */
    protected Settings settings;

    /**
     * The singleton of Magic
     */
    protected Magic magic;

    /**
     * The singleton of Bank
     */
    protected Bank bank;

    /**
     * The singleton of Players
     */
    protected Players players;

    /**
     * The singleton of Store
     */
    protected Store store;

    /**
     * The singleton of GrandExchange
     */
    protected GrandExchange grandExchange;

    /**
     * The singleton of Camera
     */
    protected Camera camera;

    /**
     * The singleton of NPCs
     */
    protected NPCs npcs;

    /**
     * The singleton of GameScreen
     */
    protected Game game;

    /**
     * The singleton of Combat
     */
    protected Combat combat;

    /**
     * The singleton of Interfaces
     */
    protected Interfaces interfaces;

    /**
     * The singleton of Mouse
     */
    protected Mouse mouse;

    /**
     * The singleton of Keyboard
     */
    protected Keyboard keyboard;

    /**
     * The singleton of Menu
     */
    protected Menu menu;

    /**
     * The singleton of Tiles
     */
    protected Tiles tiles;

    /**
     * The singleton of Objects
     */
    protected Objects objects;

    /**
     * The singleton of Walking
     */
    protected Walking walking;

    /**
     * The singleton of Calculations
     */
    protected Calculations calc;

    /**
     * The singleton of Inventory
     */
    protected Inventory inventory;

    /**
     * The singleton of Equipment
     */
    protected Equipment equipment;

    /**
     * The singleton of GroundItems
     */
    protected GroundItems groundItems;

    /**
     * The singleton of Account
     */
    protected Account account;

    /**
     * The singleton of Summoning
     */
    protected Summoning summoning;

    private MethodContext ctx;

    /**
     * For internal use only: initializes the
     * method providers.
     *
     * @param ctx The MethodContext.
     */
    public void init(MethodContext ctx) {
        this.skills = ctx.skills;
        this.settings = ctx.settings;
        this.magic = ctx.magic;
        this.bank = ctx.bank;
        this.players = ctx.players;
        this.store = ctx.store;
        this.grandExchange = ctx.grandExchange;
        this.camera = ctx.camera;
        this.npcs = ctx.npcs;
        this.game = ctx.game;
        this.combat = ctx.combat;
        this.interfaces = ctx.interfaces;
        this.mouse = ctx.mouse;
        this.keyboard = ctx.keyboard;
        this.menu = ctx.menu;
        this.tiles = ctx.tiles;
        this.objects = ctx.objects;
        this.walking = ctx.walking;
        this.calc = ctx.calc;
        this.inventory = ctx.inventory;
        this.equipment = ctx.equipment;
        this.groundItems = ctx.groundItems;
        this.account = ctx.account;
        this.summoning = ctx.summoning;
        this.ctx = ctx;
    }

    /**
     * Returns the current client's local player.
     *
     * @return The current client's <tt>RSPlayer</tt>.
     * @see Players#getMyPlayer()
     */
    public RSPlayer getMyPlayer() {
        return players.getMyPlayer();
    }

    /**
     * Returns a random integer with min as the inclusive
     * lower bound and max as the exclusive upper bound.
     *
     * @param min The inclusive lower bound.
     * @param max The exclusive upper bound.
     * @return Random integer min <= n < max.
     */
    public int random(int min, int max) {
        int n = Math.abs(max - min);
        return Math.min(min, max) + (n == 0 ? 0 : random.nextInt(n));
    }

    /**
     * Returns a random double with min as the inclusive
     * lower bound and max as the exclusive upper bound.
     *
     * @param min The inclusive lower bound.
     * @param max The exclusive upper bound.
     * @return Random double min <= n < max.
     */
    public double random(double min, double max) {
        return Math.min(min, max) + random.nextDouble() * Math.abs(max - min);
    }

    /**
     * Pauses execution for a random amount of time between two values.
     *
     * @param minSleep The minimum time to sleep.
     * @param maxSleep The maximum time to sleep.
     * @see #sleep(int)
     * @see #random(int,int)
     */
    public void sleep(int minSleep, int maxSleep) {
        sleep(random(minSleep, maxSleep));
    }

    /**
     * Pauses execution for a given number of milliseconds.
     *
     * @param toSleep The time to sleep in milliseconds.
     */
    public void sleep(int toSleep) {
        try {
            long start = System.currentTimeMillis();
            Thread.sleep(toSleep);

            // Guarantee minimum sleep
            long now;
            while (start + toSleep > (now = System.currentTimeMillis())) {
                Thread.sleep(start + toSleep - now);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints to the RSBot log.
     *
     * @param message Object to log.
     */
    public void log(Object message) {
        log.info(message.toString());
    }

    /**
     * Halts the current script without logging out.
     */
    public void stopScript() {
        stopScript(false);
    }

    /**
     * You may override this in a subclass of Script in order
     * to execute code whenever the script is stopped.
     */
    public void onFinish() {

    }

    /**
     * Stops the current script; player can be logged out before
     * the script is stopped.
     *
     * @param logout <tt>true</tt> if the player should be logged
     *               out before the script is stopped.
     */
    public void stopScript(boolean logout) {
        log.info("Script stopped.");
        if (bank.isOpen()) {
            bank.close();
        }
        if (game.isLoggedIn() && logout) {
            game.logout(false);
        }
        ctx.bot.getScriptHandler().stopScript();
    }

    /**
     * Controls user input.
     *
     * @param enable <tt>true</tt> to enable user input.
     */
    public void setUserInput(boolean enable) {
        Listener.blocked = !enable;
    }

    /**
     * Takes and saves a screenshot.
     *
     * @param hideUsername <tt>true</tt> to cover the player's
     *                     username; otherwise <tt>false</tt>.
     */
    public void takeScreenshot(boolean hideUsername) {
        ScreenshotUtil.takeScreenshot(ctx.bot, hideUsername);
    }

    /**
     * Enables a random to be allowed to execute
     *
     * @param name the random's class name or it's Manifest name
     * @return <tt>true</tt> if random was found and set to enabled; otherwise <tt>false</tt>
     */
    public boolean enableRandom(String name) {
        for (final Random random : ctx.bot.getScriptHandler().getRandoms()) {
            if (random.getClass().getName().toLowerCase().equals(name.toLowerCase())
                || random.getClass().getAnnotation(ScriptManifest.class).name().toLowerCase().equals(name.toLowerCase())) {
                if (random.isEnabled())
                    return true;
                else {
                    random.setEnabled(true);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Disables a random to not be allowed to execute
     *
     * @param name the random's class name or it's Manifest name
     * @return <tt>true</tt> if random was found and set to disabled; otherwise <tt>false</tt>
     */
    public boolean disableRandom(String name) {
        for (final Random random : ctx.bot.getScriptHandler().getRandoms()) {
            if (random.getClass().getName().toLowerCase().equals(name.toLowerCase())
                || random.getClass().getAnnotation(ScriptManifest.class).name().toLowerCase().equals(name.toLowerCase())) {
                if (!random.isEnabled())
                    return true;
                else {
                    random.setEnabled(false);
                    return true;
                }
            }
        }
        return false;
    }

}
