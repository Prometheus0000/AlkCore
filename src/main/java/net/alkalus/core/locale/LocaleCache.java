package net.alkalus.core.locale;

import java.io.File;
import java.util.HashMap;

import net.alkalus.api.objects.data.AutoMap;
import net.alkalus.api.objects.misc.AcLog;
import net.alkalus.core.util.data.FileUtils;

public class LocaleCache {

    /**
     * Holds all common locale names
     */
    private static final String[] INTERNAL_LOCALE_NAME_CACHE = new String[] {
            "af_ZA", "am_ET", "ar_AE", "ar_BH", "ar_DZ", "ar_EG", "ar_IQ",
            "ar_JO", "ar_KW", "ar_LB", "ar_LY", "ar_MA", "arn_CL", "ar_OM",
            "ar_QA", "ar_SA", "ar_SY", "ar_TN", "ar_YE", "as_IN", "az_Cyrl_AZ",
            "az_Latn_AZ", "ba_RU", "be_BY", "bg_BG", "bn_BD", "bn_IN", "bo_CN",
            "br_FR", "bs_Cyrl_BA", "bs_Latn_BA", "ca_ES", "co_FR", "cs_CZ",
            "cy_GB", "da_DK", "de_AT", "de_CH", "de_DE", "de_LI", "de_LU",
            "dsb_DE", "dv_MV", "el_GR", "en_029", "en_AU", "en_BZ", "en_CA",
            "en_GB", "en_IE", "en_IN", "en_JM", "en_MY", "en_NZ", "en_PH",
            "en_SG", "en_TT", "en_US", "en_ZA", "en_ZW", "es_AR", "es_BO",
            "es_CL", "es_CO", "es_CR", "es_DO", "es_EC", "es_ES", "es_GT",
            "es_HN", "es_MX", "es_NI", "es_PA", "es_PE", "es_PR", "es_PY",
            "es_SV", "es_US", "es_UY", "es_VE", "et_EE", "eu_ES", "fa_IR",
            "fi_FI", "fil_PH", "fo_FO", "fr_BE", "fr_CA", "fr_CH", "fr_FR",
            "fr_LU", "fr_MC", "fy_NL", "ga_IE", "gd_GB", "gl_ES", "gsw_FR",
            "gu_IN", "ha_Latn_NG", "he_IL", "hi_IN", "hr_BA", "hr_HR", "hsb_DE",
            "hu_HU", "hy_AM", "id_ID", "ig_NG", "ii_CN", "is_IS", "it_CH",
            "it_IT", "iu_Cans_CA", "iu_Latn_CA", "ja_JP", "ka_GE", "kk_KZ",
            "kl_GL", "km_KH", "kn_IN", "kok_IN", "ko_KR", "ky_KG", "lb_LU",
            "lo_LA", "lt_LT", "lv_LV", "mi_NZ", "mk_MK", "ml_IN", "mn_MN",
            "mn_Mong_CN", "moh_CA", "mr_IN", "ms_BN", "ms_MY", "mt_MT", "nb_NO",
            "ne_NP", "nl_BE", "nl_NL", "nn_NO", "nso_ZA", "oc_FR", "or_IN",
            "pa_IN", "pl_PL", "prs_AF", "ps_AF", "pt_BR", "pt_PT", "qut_GT",
            "quz_BO", "quz_EC", "quz_PE", "rm_CH", "ro_RO", "ru_RU", "rw_RW",
            "sah_RU", "sa_IN", "se_FI", "se_NO", "se_SE", "si_LK", "sk_SK",
            "sl_SI", "sma_NO", "sma_SE", "smj_NO", "smj_SE", "smn_FI", "sms_FI",
            "sq_AL", "sr_Cyrl_BA", "sr_Cyrl_CS", "sr_Cyrl_ME", "sr_Cyrl_RS",
            "sr_Latn_BA", "sr_Latn_CS", "sr_Latn_ME", "sr_Latn_RS", "sv_FI",
            "sv_SE", "sw_KE", "syr_SY", "ta_IN", "te_IN", "tg_Cyrl_TJ", "th_TH",
            "tk_TM", "tn_ZA", "tr_TR", "tt_RU", "tzm_Latn_DZ", "ug_CN", "uk_UA",
            "ur_PK", "uz_Cyrl_UZ", "uz_Latn_UZ", "vi_VN", "wo_SN", "xh_ZA",
            "yo_NG", "zh_CN", "zh_HK", "zh_MO", "zh_SG", "zh_TW", "zu_ZA" };

    /*
     * Final
     */
    public final boolean VALID;
    public final char DELIMITER;
    private final HashMap<String, File> INTERNAL_LOCALE_FILE_CACHE = new HashMap<String, File>();

    /*
     * Instance Data
     */
    public String mLocale;
    private HashMap<String, String> mCurrentlySelectedLocaleData = new HashMap<String, String>();

    /**
     * Default LocaleCache settings Uses en_US with a = delimiter
     */
    public LocaleCache() {
        this("en_US", '=');
    }

    /**
     * 
     * Designated LocaleCache settings Uses specified locale with a = delimiter
     *
     * @param aLocale - Locale to load
     */
    public LocaleCache(final String aLocale) {
        this(aLocale, '=');
    }

    /**
     * 
     * @param aLocale    - Locale to load
     * @param aDelimiter - Custom delimiter for splitting locale data
     */
    public LocaleCache(final String aLocale, final char aDelimiter) {
        mLocale = aLocale;
        DELIMITER = aDelimiter;
        for (final String g : INTERNAL_LOCALE_NAME_CACHE) {
            File file;
            try {
                file = new File(LocaleCache.class
                        .getResource("/locale/" + g + ".lang").getFile());
            } catch (final Throwable t) {
                continue;
            }
            if (file != null && file.exists() && !file.isDirectory()) {
                INTERNAL_LOCALE_FILE_CACHE.put(g, file);
                log("Found locale data for " + g);
            }
        }
        if (INTERNAL_LOCALE_FILE_CACHE.size() > 0) {
            VALID = true;
        } else {
            log("Could not find default locale file " + mLocale);
            VALID = false;
        }
        if (VALID) {
            populate();
        }
    }

    /**
     * {@link Override} this if you'd like to log things about locale using your
     * own logger.
     * 
     * @param aMessage - String passed to the logger.
     */
    public void log(final String aMessage) {
        AcLog.INFO(aMessage);
    }

    /**
     * Gets a localized name for the string provided.
     * 
     * @param aUnlocalName
     * @return
     */
    public String getLocalizedString(final String aUnlocalName) {
        final String aValue = mCurrentlySelectedLocaleData.get(aUnlocalName);
        if (aValue == null) {
            log("[Error] key: '" + aUnlocalName
                    + "' does not have localized String");
        }
        return aValue;
    }

    /**
     * Gets the default language, en_US.
     * 
     * @return - The en_US.lang file
     */
    public final File getSelectedLocaleFile() {
        return INTERNAL_LOCALE_FILE_CACHE.get(mLocale);
    }

    /**
     * Gets the specified language.
     * 
     * @param aLocale - The language to use, specified as "xx_XX".
     * @return - The designated .lang file
     */
    public final File getSelectedLocaleFile(final String aLocale) {
        return INTERNAL_LOCALE_FILE_CACHE.get(aLocale);
    }

    /**
     * Used to designate the locale to currently use.
     * 
     * @param aLocale - The language to use, specified as "xx_XX".
     */
    public final void changeDefaultLocale(final String aLocale) {
        mCurrentlySelectedLocaleData.clear();
        mLocale = aLocale;
        populate();
    }

    /**
     * Populates the locale map.
     */
    private final void populate() {
        mCurrentlySelectedLocaleData = processLocaleData(mLocale);
        AcLog.INFO("Using Locale: " + mLocale + " | "
                + mCurrentlySelectedLocaleData.size());
    }

    /**
     * Creates a HashMap with keypairs of internal names and localized names
     * 
     * @param aLocale - A designated locale
     * @return - Populated Keypair HashMap.
     */
    public HashMap<String, String> processLocaleData(final String aLocale) {
        return processLocaleData(aLocale, DELIMITER);
    }

    /**
     * Internal handling of locale data, can be overridden in
     * processLocaleData(String aLocale) if undesired.
     * 
     * @param aLocale - Input Locale
     * @param aSplit  - Delimiter
     * @return - Populated HashMap of Locale:Localized pairs
     */
    private final HashMap<String, String> processLocaleData(final String aLocale,
            final char aSplit) {
        final AutoMap<String> aReadData = FileUtils
                .readLines(getSelectedLocaleFile());
        // log("Found "+aReadData.size()+" line within the locale file.");
        final HashMap<String, String> aOutputData = new HashMap<String, String>();
        int aLineCounter = 0;
        for (String aLocaleEntry : aReadData) {
            if (aLocaleEntry != null && aLocaleEntry.length() > 0) {
                aLocaleEntry = aLocaleEntry.trim();
                if (aLocaleEntry.length() < 3) {
                    continue;
                }
                final String[] aData = aLocaleEntry.split("" + aSplit); // Limit to 1?
                if (aData != null && aData.length > 1) {
                    if ((aData[0] != null && aData[0].length() >= 1)
                            && (aData[1] != null && aData[1].length() >= 1)) {

                        aOutputData.put(aData[0], aData[1]);
                        // log("Found good locale data on line "+aLineCounter);
                    } else {
                        log("Found bad locale data on line " + aLineCounter
                                + " | String split oddly.");
                    }
                } else {

                    if (!aLocaleEntry.startsWith("//")
                            && !aLocaleEntry.startsWith("#")
                            && !aLocaleEntry.startsWith("--")) {
                        log("Found bad locale data on line " + aLineCounter
                                + " | Unable to split String");
                    }

                }
            } else {
                log("Found bad locale data on line " + aLineCounter
                        + " | No Length (Maybe Null)");
            }
            aLineCounter++;
        }
        return aOutputData;
    }

    /**
     * 
     * @return - The current Locales data set.
     */
    public synchronized final HashMap<String, String> getCurrentLocaleData() {
        return mCurrentlySelectedLocaleData;
    }

    public void dumpLocaleMappings() {
        for (final String g : mCurrentlySelectedLocaleData.keySet()) {
            if (g != null && g.length() >= 1) {
                final String aBuilt = (g + " : "
                        + mCurrentlySelectedLocaleData.get(g));
                log(aBuilt);
            }

        }
    }

}
