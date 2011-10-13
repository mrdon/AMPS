package com.atlassian.maven.plugins.amps.util;

import com.dmurph.tracking.AnalyticsConfigData;
import com.dmurph.tracking.JGoogleAnalyticsTracker;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.logging.Log;

/**
 * @since version
 */
public class GoogleAmpsTracker
{
    private static final String TRACKING_CODE = "UA-6032469-43";
    private static final String AMPS = "AMPS";
    private static final String EVENT_PREFIX = AMPS + ":";

    public static final String CREATE_PLUGIN = "Create Plugin";
    public static final String DEBUG = "Debug";
    public static final String RUN = "Run";
    public static final String RUN_STANDALONE = "Run Standalone";
    public static final String RELEASE = "Release";
    public static final String CREATE_HOME_ZIP = "Create Home Zip";
    public static final String CREATE_PLUGIN_MODULE = "Create Plugin Module";

    private final AnalyticsConfigData config;
    private final JGoogleAnalyticsTracker tracker;
    private final Log mavenLogger;
    private String productId;

    public GoogleAmpsTracker(String productId, Log mavenLogger)
    {
        this(mavenLogger);
        this.productId = productId;
    }

    public GoogleAmpsTracker(Log mavenLogger)
    {
        this.mavenLogger = mavenLogger;
        this.config = new AnalyticsConfigData(TRACKING_CODE);
        this.tracker = new JGoogleAnalyticsTracker(config, JGoogleAnalyticsTracker.GoogleAnalyticsVersion.V_4_7_2);

        tracker.setDispatchMode(JGoogleAnalyticsTracker.DispatchMode.MULTI_THREAD);
    }

    public void track(String eventName) {
        mavenLogger.info("Sending event to Google Analytics: "  + getCategoryName() + " - " + eventName);
        tracker.trackEvent(getCategoryName(),eventName);
    }

    public void track(String eventName, String label) {
        mavenLogger.info("Sending event to Google Analytics: "  + getCategoryName() + " - " + eventName + " - " + label);
        tracker.trackEvent(getCategoryName(),eventName,label);
    }

    private String getCategoryName()
    {
        if(StringUtils.isNotBlank(productId)) {
            return EVENT_PREFIX + productId;
        } else {
            return AMPS;
        }

    }

    public String getProductId()
    {
        return productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public void setEnabled(boolean enabled) {
        tracker.setEnabled(enabled);
    }
}
