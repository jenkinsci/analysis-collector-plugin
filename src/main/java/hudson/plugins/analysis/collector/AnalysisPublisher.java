package hudson.plugins.analysis.collector; // NOPMD

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.FilePath;
import hudson.Launcher;
import hudson.matrix.MatrixAggregator;
import hudson.matrix.MatrixBuild;
import hudson.model.BuildListener;
import hudson.model.Run;
import hudson.plugins.analysis.collector.handler.CheckStyleHandler;
import hudson.plugins.analysis.collector.handler.DryHandler;
import hudson.plugins.analysis.collector.handler.FindBugsHandler;
import hudson.plugins.analysis.collector.handler.PmdHandler;
import hudson.plugins.analysis.collector.handler.TasksHandler;
import hudson.plugins.analysis.collector.handler.WarningsHandler;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.HealthAwarePublisher;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.core.ResultAction;
import hudson.plugins.analysis.util.PluginLogger;
import hudson.plugins.analysis.util.model.FileAnnotation;

/**
 * Collects the results of the various analysis plug-ins.
 *
 * @author Ulli Hafner
 */
// CHECKSTYLE:COUPLING-OFF
public class AnalysisPublisher extends HealthAwarePublisher {
    private static final long serialVersionUID = 5512072640635006098L;

    private boolean isCheckStyleDeactivated;
    private boolean isDryDeactivated;
    private boolean isFindBugsDeactivated;
    private boolean isPmdDeactivated;
    private boolean isOpenTasksDeactivated;
    private boolean isWarningsDeactivated;

    private static final String PLUGIN_ID = "ANALYSIS-COLLECTOR";

    /**
     * Default constructor.
     * Use setter to initialize fields if needed.
     */
    @DataBoundConstructor
    public AnalysisPublisher() {
        super(PLUGIN_ID);
    }

    /**
     * Returns whether CheckStyle results should be collected.
     *
     * @return <code>true</code> if CheckStyle results should be collected, <code>false</code> otherwise
     */
    public boolean isCheckStyleActivated() {
        return !isCheckStyleDeactivated;
    }

    /**
     * @see #isCheckStyleActivated()
     */
    @DataBoundSetter
    public void setCheckStyleActivated(final boolean checkStyleActivated) {
        isCheckStyleDeactivated = !checkStyleActivated;
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
     * @see #isDryActivated()
     */
    @DataBoundSetter
    public void setDryActivated(final boolean dryActivated) {
        isDryDeactivated = !dryActivated;
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
     * @see #isFindBugsActivated()
     */
    @DataBoundSetter
    public void setFindBugsActivated(final boolean findBugsActivated) {
        isFindBugsDeactivated = !findBugsActivated;
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
     * @see #isPmdActivated()
     */
    @DataBoundSetter
    public void setPmdActivated(final boolean pmdActivated) {
        isPmdDeactivated = !pmdActivated;
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
     * @see #isOpenTasksActivated()
     */
    @DataBoundSetter
    public void setOpenTasksActivated(final boolean openTasksActivated) {
        isOpenTasksDeactivated = !openTasksActivated;
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
     * @see #isWarningsActivated()
     */
    @DataBoundSetter
    public void setWarningsActivated(final boolean warningsActivated) {
        isWarningsDeactivated = !warningsActivated;
    }

    /**
     * Initializes the plug-ins that should participate in the results of this
     * analysis collector.
     *
     * @return the plug-in actions to read the results from
     */
    @SuppressWarnings({"PMD.NPathComplexity", "PMD.CyclomaticComplexity"})
    private List<Class<? extends ResultAction<? extends BuildResult>>> getParticipatingPlugins() {
        ArrayList<Class<? extends ResultAction<? extends BuildResult>>> pluginResults;
        pluginResults = new ArrayList<Class<? extends ResultAction<? extends BuildResult>>>();

        if (AnalysisDescriptor.isCheckStyleInstalled() && isCheckStyleActivated()) {
            pluginResults.addAll(new CheckStyleHandler().getResultActions());
        }
        if (AnalysisDescriptor.isDryInstalled() && isDryActivated()) {
            pluginResults.addAll(new DryHandler().getResultActions());
        }
        if (AnalysisDescriptor.isFindBugsInstalled() && isFindBugsActivated()) {
            pluginResults.addAll(new FindBugsHandler().getResultActions());
        }
        if (AnalysisDescriptor.isPmdInstalled() && isPmdActivated()) {
            pluginResults.addAll(new PmdHandler().getResultActions());
        }
        if (AnalysisDescriptor.isOpenTasksInstalled() && isOpenTasksActivated()) {
            pluginResults.addAll(new TasksHandler().getResultActions());
        }
        if (AnalysisDescriptor.isWarningsInstalled() && isWarningsActivated()) {
            pluginResults.addAll(new WarningsHandler().getResultActions());
        }

        return pluginResults;
    }

    @Override
    public BuildResult perform(final Run<?, ?> build, final FilePath workspace, final PluginLogger logger) throws InterruptedException, IOException {
        ParserResult overallResult = new ParserResult(workspace);
        for (Class<? extends ResultAction<? extends BuildResult>> result : getParticipatingPlugins()) {
            ResultAction<? extends BuildResult> action = build.getAction(result);
            if (action != null) {
                BuildResult actualResult = action.getResult();
                Collection<FileAnnotation> annotations = actualResult.getAnnotations();
                overallResult.addAnnotations(annotations);
            }
        }

        AnalysisResult result = new AnalysisResult(build, getDefaultEncoding(), overallResult,
                usePreviousBuildAsReference(), useOnlyStableBuildsAsReference());
        build.addAction(new AnalysisResultAction(build, this, result));

        return result;
    }

    @Override
    public AnalysisDescriptor getDescriptor() {
        return (AnalysisDescriptor)super.getDescriptor();
    }

    @Override
    public MatrixAggregator createAggregator(final MatrixBuild build, final Launcher launcher,
            final BuildListener listener) {
        return new AnalysisAnnotationsAggregator(build, launcher, listener, this, getDefaultEncoding(),
                usePreviousBuildAsReference(), useOnlyStableBuildsAsReference());
    }
}
