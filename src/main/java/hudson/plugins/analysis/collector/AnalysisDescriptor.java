package hudson.plugins.analysis.collector;

import java.util.Collection;
import java.util.HashSet;

import com.google.common.collect.Sets;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.plugins.analysis.core.PluginDescriptor;

/**
 * Descriptor for the class {@link AnalysisPublisher}. Used as a singleton. The
 * class is marked as public so that it can be accessed from views.
 *
 * @author Ulli Hafner
 */
@Extension(ordinal = 1)
public final class AnalysisDescriptor extends PluginDescriptor {
    private static final String ICONS_PREFIX = "/plugin/analysis-collector/icons/";
    /** The ID of this plug-in is used as URL. */
    static final String PLUGIN_ID = "analysis";
    /** The URL of the result action. */
    static final String RESULT_URL = PluginDescriptor.createResultUrlName(PLUGIN_ID);
    /** Icon to use for the result and project action. */
    static final String ICON_URL = ICONS_PREFIX + "analysis-24x24.png";

    public static final String CHECKSTYLE = "checkstyle";
    public static final String DRY = "dry";
    public static final String FINDBUGS = "findbugs";
    public static final String PMD = "pmd";
    public static final String TASKS = "tasks";
    public static final String WARNINGS = "warnings";
    public static final String ANDROID_LINT = "android-lint";

    /**
     * Returns the activated plug-ins.
     *
     * @return the activated plug-ins
     */
    public static Collection<String> getPlugins() {
        HashSet<String> plugins = Sets.newHashSet();

        if (isCheckStyleInstalled()) {
            plugins.add(CHECKSTYLE);
        }
        if (isDryInstalled()) {
            plugins.add(DRY);
        }
        if (isFindBugsInstalled()) {
            plugins.add(FINDBUGS);
        }
        if (isPmdInstalled()) {
            plugins.add(PMD);
        }
        if (isOpenTasksInstalled()) {
            plugins.add(TASKS);
        }
        if (isWarningsInstalled()) {
            plugins.add(WARNINGS);
        }
        if (isAndroidLintInstalled()) {
            plugins.add(ANDROID_LINT);
        }
        return plugins;
    }

    /**
     * Returns whether the Checkstyle plug-in is installed.
     *
     * @return <code>true</code> if the Checkstyle plug-in is installed,
     *         <code>false</code> if not.
     */
    public static boolean isCheckStyleInstalled() {
        return isPluginInstalled(CHECKSTYLE);
    }

    /**
     * Returns whether the Dry plug-in is installed.
     *
     * @return <code>true</code> if the Dry plug-in is installed,
     *         <code>false</code> if not.
     */
    public static boolean isDryInstalled() {
        return isPluginInstalled(DRY);
    }

    /**
     * Returns whether the FindBugs plug-in is installed.
     *
     * @return <code>true</code> if the FindBugs plug-in is installed,
     *         <code>false</code> if not.
     */
    public static boolean isFindBugsInstalled() {
        return isPluginInstalled(FINDBUGS);
    }

    /**
     * Returns whether the PMD plug-in is installed.
     *
     * @return <code>true</code> if the PMD plug-in is installed,
     *         <code>false</code> if not.
     */
    public static boolean isPmdInstalled() {
        return isPluginInstalled(PMD);
    }

    /**
     * Returns whether the Open Tasks plug-in is installed.
     *
     * @return <code>true</code> if the Open Tasks plug-in is installed,
     *         <code>false</code> if not.
     */
    public static boolean isOpenTasksInstalled() {
        return isPluginInstalled(TASKS);
    }

    /**
     * Returns whether the Warnings plug-in is installed.
     *
     * @return <code>true</code> if the Warnings plug-in is installed,
     *         <code>false</code> if not.
     */
    public static boolean isWarningsInstalled() {
        return isPluginInstalled(WARNINGS);
    }

    /**
     * Returns whether the Android Lint plug-in is installed.
     *
     * @return <code>true</code> if the Android Lint plug-in is installed,
     *         <code>false</code> if not.
     */
    public static boolean isAndroidLintInstalled() {
        return isPluginInstalled(ANDROID_LINT);
    }

    /**
     * Instantiates a new {@link AnalysisDescriptor}.
     */
    public AnalysisDescriptor() {
        super(AnalysisPublisher.class);
    }

    @Override
    public String getDisplayName() {
        return Messages.Analysis_Publisher_Name();
    }

    @Override
    public String getPluginRoot() {
        return "/plugin/analysis-collector/";
    }

    @Override
    public String getPluginName() {
        return PLUGIN_ID;
    }

    @Override
    public String getIconUrl() {
        return ICON_URL;
    }

    @Override
    public String getSummaryIconUrl() {
        return ICONS_PREFIX + "analysis-48x48.png";
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean isApplicable(final Class<? extends AbstractProject> jobType) {
        return true;
    }
}
