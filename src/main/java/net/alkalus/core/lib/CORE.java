package net.alkalus.core.lib;

import java.util.Random;

import javax.security.auth.login.Configuration;

import net.alkalus.api.objects.misc.XSTR;
import net.alkalus.core.util.sys.GeoUtils;

public class CORE {

    // Math Related
    public static final float PI = (float) Math.PI;
    public static volatile Random RANDOM = new XSTR();

    // Env. Variables
    public static Configuration Config;
    public static boolean DEVENV = false;
    public static boolean DEBUG = false;

    // Mod Variables

    public static final String NAME = "AlkLib";
    public static final String VERSION = "0.0.1";
    public static String USER_COUNTRY = GeoUtils.determineUsersCountry();

}
