package net.alkalus.api.interfaces;

import net.alkalus.api.objects.misc.AcLog;

public interface IPlugin {

    /**
     * @return A {@link String} object which returns the {@link IPlugin}'s name.
     */
    public String getPluginName();

    /**
     * @return A {@link String} object which returns the {@link IPlugin}'s short
     *         name. This String should only contain 4 Characters.
     */
    public String getPluginAbbreviation();

    /**
     * @param message - A {@link String} object which holds a message to be
     *                logged to console.
     */
    default void log(final String message) {
        AcLog.INFO("[" + getPluginAbbreviation() + "] " + message);
    }

    /**
     * @param message - A {@link String} object which holds a warning/error
     *                message to be logged to console.
     */
    default void logDebug(final String message) {
        AcLog.WARNING("[" + getPluginAbbreviation() + "] " + message);
    }

    public boolean preInit();

    public boolean init();

    public boolean postInit();

}
