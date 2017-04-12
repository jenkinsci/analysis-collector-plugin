package hudson.plugins.analysis.collector;

import org.jvnet.localizer.Localizable;
import org.kohsuke.stapler.DataBoundConstructor;

import jenkins.model.Jenkins;

import hudson.Extension;
import hudson.model.Job;
import hudson.plugins.analysis.collector.handler.AnalysisHandler;
import hudson.plugins.analysis.collector.handler.AndroidLintHandler;
import hudson.plugins.analysis.collector.handler.CheckStyleHandler;
import hudson.plugins.analysis.collector.handler.DryHandler;
import hudson.plugins.analysis.collector.handler.FindBugsHandler;
import hudson.plugins.analysis.collector.handler.PmdHandler;
import hudson.plugins.analysis.collector.handler.TasksHandler;
import hudson.plugins.analysis.collector.handler.WarningsHandler;
import hudson.plugins.analysis.core.PluginDescriptor;
import hudson.plugins.analysis.util.HtmlPrinter;
import hudson.views.ListViewColumnDescriptor;

/**
 * A column that shows the total number of warnings in a job.
 *
 * @author Ulli Hafner
 */
public class WarningsCountColumn extends hudson.plugins.analysis.views.WarningsCountColumn<AnalysisProjectAction> {
    private final WarningsAggregator warningsAggregator;

    /**
     * Creates a new instance of {@link WarningsCountColumn}.
     *
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
    @DataBoundConstructor
    public WarningsCountColumn(final boolean checkStyleActivated,
            final boolean dryActivated, final boolean findBugsActivated, final boolean pmdActivated,
            final boolean openTasksActivated, final boolean warningsActivated, final boolean androidLintActivated) {
        super();

        warningsAggregator = new WarningsAggregator(checkStyleActivated, dryActivated,
                findBugsActivated, pmdActivated, openTasksActivated, warningsActivated, androidLintActivated);
    }

    /**
     * Returns whether CheckStyle results should be shown.
     *
     * @return <code>true</code> if CheckStyle results should be shown, <code>false</code> otherwise
     */
    public boolean isCheckStyleActivated() {
        return warningsAggregator.isCheckStyleActivated();
    }

    /**
     * Returns whether DRY results should be shown.
     *
     * @return <code>true</code> if DRY results should be shown, <code>false</code> otherwise
     */
    public boolean isDryActivated() {
        return warningsAggregator.isDryActivated();
    }

    /**
     * Returns whether FindBugs results should be shown.
     *
     * @return <code>true</code> if FindBugs results should be shown, <code>false</code> otherwise
     */
    public boolean isFindBugsActivated() {
        return warningsAggregator.isFindBugsActivated();
    }

    /**
     * Returns whether PMD results should be shown.
     *
     * @return <code>true</code> if PMD results should be shown, <code>false</code> otherwise
     */
    public boolean isPmdActivated() {
        return warningsAggregator.isPmdActivated();
    }

    /**
     * Returns whether open tasks should be shown.
     *
     * @return <code>true</code> if open tasks should be shown, <code>false</code> otherwise
     */
    public boolean isOpenTasksActivated() {
        return warningsAggregator.isOpenTasksActivated();
    }

    /**
     * Returns whether compiler warnings results should be shown.
     *
     * @return <code>true</code> if compiler warnings results should be shown, <code>false</code> otherwise
     */
    public boolean isWarningsActivated() {
        return warningsAggregator.isWarningsActivated();
    }

    /**
     * Returns whether Android Lint results should be shown.
     *
     * @return <code>true</code> if Android lint results should be shown, <code>false</code> otherwise
     */
    public boolean isAndroidLintActivated() {
        return warningsAggregator.isAndroidLintActivated();
    }

    /**
     * Returns the total number of annotations for the selected job.
     *
     * @param project
     *            the selected project
     * @return the total number of annotations
     */
    public String getNumberOfAnnotations(final Job<?, ?> project) {
        return warningsAggregator.getTotal(project);
    }

    @Override
    protected Class<AnalysisProjectAction> getProjectAction() {
        return AnalysisProjectAction.class;
    }

    /**
     * Returns the number of warnings for the specified job separated by each plug-in.
     *
     * @param job
     *            the job to get the warnings for
     * @return the number of warnings, formatted as HTML string
     */
    public String getDetails(final Job<?, ?> job) {
        HtmlPrinter printer = new HtmlPrinter();
        printer.append("<table>");
        if (isCheckStyleActivated()) {
            printLine(printer, new CheckStyleHandler(), warningsAggregator.getCheckStyle(job));
        }
        if (isDryActivated()) {
            printLine(printer, new DryHandler(), warningsAggregator.getDry(job));
        }
        if (isFindBugsActivated()) {
            printLine(printer, new FindBugsHandler(), warningsAggregator.getFindBugs(job));
        }
        if (isPmdActivated()) {
            printLine(printer, new PmdHandler(), warningsAggregator.getPmd(job));
        }
        if (isOpenTasksActivated()) {
            printLine(printer, new TasksHandler(), warningsAggregator.getTasks(job));
        }
        if (isWarningsActivated()) {
            printLine(printer, new WarningsHandler(), warningsAggregator.getCompilerWarnings(job));
        }
        if (isAndroidLintActivated()) {
            printLine(printer, new AndroidLintHandler(), warningsAggregator.getAndroidLint(job));
        }
        printer.append("</table>");
        return printer.toString();
    }

    private void printLine(final HtmlPrinter printer, final AnalysisHandler handler, final String warnings) {
        printLine(printer, handler.getDetailHeader(), warnings, handler.getDescriptor());
    }

    private void printLine(final HtmlPrinter printer, final Localizable header, final String warnings,
            final Class<? extends PluginDescriptor> descriptor) {
        PluginDescriptor pluginDescriptor = Jenkins.getInstance().getDescriptorByType(descriptor);
        String image = "<img hspace=\"10\" align=\"absmiddle\" width=\"24\" height=\"24\" src=\""
                + Jenkins.RESOURCE_PATH + pluginDescriptor.getIconUrl() + "\"/>";
        printer.append(printer.line(image + header + ": " + warnings));
    }

    /**
     * Descriptor for the column.
     */
    @Extension
    public static class ColumnDescriptor extends ListViewColumnDescriptor {
            @Override
        public boolean shownByDefault() {
            return false;
        }

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
            return Messages.Analysis_Warnings_Column();
        }
    }
}
