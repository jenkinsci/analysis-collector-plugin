package hudson.plugins.analysis.collector.dashboard;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.plugins.analysis.collector.AnalysisDescriptor;
import hudson.plugins.analysis.collector.AnalysisProjectAction;
import hudson.plugins.analysis.collector.Messages;
import hudson.plugins.analysis.collector.OriginGraph;
import hudson.plugins.analysis.core.AbstractProjectAction;
import hudson.plugins.analysis.dashboard.AbstractWarningsGraphPortlet;
import hudson.plugins.analysis.graph.BuildResultGraph;
import hudson.plugins.view.dashboard.DashboardPortlet;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * A portlet that shows the warnings trend graph of warnings by type.
 *
 * @author Ulli Hafner
 */
public final class WarningsOriginGraphPortlet extends AbstractWarningsGraphPortlet {
    private final boolean isCheckStyleDeactivated;
    private final boolean isDryDeactivated;
    private final boolean isFindBugsDeactivated;
    private final boolean isPmdDeactivated;
    private final boolean isOpenTasksDeactivated;
    private final boolean isWarningsDeactivated;
    private final boolean isAndroidLintDeactivated;

    /**
     * Creates a new instance of {@link WarningsOriginGraphPortlet}.
     *
     * @param name
     *            the name of the portlet
     * @param width
     *            width of the graph
     * @param height
     *            height of the graph
     * @param dayCountString
     *            number of days to consider
     * @param checkStyleActivated
     *            determines whether to show the warnings from Checkstyle
     * @param dryActivated
     *            determines whether to show the warnings from DRY
     * @param findBugsActivated
     *            determines whether to show the warnings from FindBugs
     * @param pmdActivated
     *            determines whether to show the warnings from PMD
     * @param openTasksActivated
     *            determines whether to show open tasks
     * @param warningsActivated
     *            determines whether to show compiler warnings
     */
    // CHECKSTYLE:OFF
    @DataBoundConstructor @SuppressWarnings("PMD.ExcessiveParameterList")
    public WarningsOriginGraphPortlet(final String name, final String width, final String height, final String dayCountString,
            final boolean checkStyleActivated, final boolean dryActivated,
            final boolean findBugsActivated, final boolean pmdActivated,
            final boolean openTasksActivated, final boolean warningsActivated, final boolean androidLintActivated) {
        super(name, width, height, dayCountString);

        isDryDeactivated = !dryActivated;
        isFindBugsDeactivated = !findBugsActivated;
        isPmdDeactivated = !pmdActivated;
        isOpenTasksDeactivated = !openTasksActivated;
        isWarningsDeactivated = !warningsActivated;
        isCheckStyleDeactivated = !checkStyleActivated;
        isAndroidLintDeactivated = !androidLintActivated;

        configureGraph(getGraphType());
    }
    // CHECKSTYLE:ON

    /**
     * Returns whether CheckStyle results should be collected.
     *
     * @return <code>true</code> if CheckStyle results should be collected, <code>false</code> otherwise
     */
    public boolean isCheckStyleActivated() {
        return !isCheckStyleDeactivated;
    }

    /**
     * Returns whether DRY results should be collected.
     *
     * @return <code>true</code> if DRY results should be collected, <code>false</code> otherwise
     */
    public boolean isDryActivated() {
        return !isDryDeactivated;
    }

    /**
     * Returns whether FindBugs results should be collected.
     *
     * @return <code>true</code> if FindBugs results should be collected, <code>false</code> otherwise
     */
    public boolean isFindBugsActivated() {
        return !isFindBugsDeactivated;
    }

    /**
     * Returns whether PMD results should be collected.
     *
     * @return <code>true</code> if PMD results should be collected, <code>false</code> otherwise
     */
    public boolean isPmdActivated() {
        return !isPmdDeactivated;
    }

    /**
     * Returns whether open tasks should be collected.
     *
     * @return <code>true</code> if open tasks should be collected, <code>false</code> otherwise
     */
    public boolean isOpenTasksActivated() {
        return !isOpenTasksDeactivated;
    }

    /**
     * Returns whether compiler warnings results should be collected.
     *
     * @return <code>true</code> if compiler warnings results should be collected, <code>false</code> otherwise
     */
    public boolean isWarningsActivated() {
        return !isWarningsDeactivated;
    }

    /**
     * Returns whether Android lint results should be collected.
     *
     * @return <code>true</code> if Android lint results should be collected, <code>false</code> otherwise
     */
    public boolean isAndroidLintActivated() {
        return !isAndroidLintDeactivated;
    }

    @Override
    protected Class<? extends AbstractProjectAction<?>> getAction() {
        return AnalysisProjectAction.class;
    }

    @Override
    protected String getPluginName() {
        return "analysis";
    }

    @Override
    protected BuildResultGraph getGraphType() {
        return new OriginGraph(isCheckStyleActivated(), isDryActivated(), isFindBugsActivated(), isPmdActivated(), isOpenTasksActivated(), isWarningsActivated(), isAndroidLintActivated());
    }

    /**
     * Extension point registration.
     */
    @Extension(optional = true)
    public static class WarningsGraphDescriptor extends Descriptor<DashboardPortlet> {
        /**
         * Returns whether the Checkstyle plug-in is installed.
         *
         * @return <code>true</code> if the Checkstyle plug-in is installed,
         *         <code>false</code> if not.
         */
        public boolean isCheckStyleInstalled() {
            return AnalysisDescriptor.isCheckStyleInstalled();
        }

        /**
         * Returns whether the Dry plug-in is installed.
         *
         * @return <code>true</code> if the Dry plug-in is installed,
         *         <code>false</code> if not.
         */
        public boolean isDryInstalled() {
            return AnalysisDescriptor.isDryInstalled();
        }

        /**
         * Returns whether the FindBugs plug-in is installed.
         *
         * @return <code>true</code> if the FindBugs plug-in is installed,
         *         <code>false</code> if not.
         */
        public boolean isFindBugsInstalled() {
            return AnalysisDescriptor.isFindBugsInstalled();
        }

        /**
         * Returns whether the PMD plug-in is installed.
         *
         * @return <code>true</code> if the PMD plug-in is installed,
         *         <code>false</code> if not.
         */
        public boolean isPmdInstalled() {
            return AnalysisDescriptor.isPmdInstalled();
        }

        /**
         * Returns whether the Open Tasks plug-in is installed.
         *
         * @return <code>true</code> if the Open Tasks plug-in is installed,
         *         <code>false</code> if not.
         */
        public boolean isOpenTasksInstalled() {
            return AnalysisDescriptor.isOpenTasksInstalled();
        }

        /**
         * Returns whether the Warnings plug-in is installed.
         *
         * @return <code>true</code> if the Warnings plug-in is installed,
         *         <code>false</code> if not.
         */
        public boolean isWarningsInstalled() {
            return AnalysisDescriptor.isWarningsInstalled();
        }

        /**
         * Returns whether the Android Lint plug-in is installed.
         *
         * @return <code>true</code> if the Android Lint plug-in is installed,
         *         <code>false</code> if not.
         */
        public boolean isAndroidLintInstalled() {
            return AnalysisDescriptor.isAndroidLintInstalled();
        }

        @Override
        public String getDisplayName() {
            return Messages.Portlet_WarningsOriginGraph();
        }
    }
}

