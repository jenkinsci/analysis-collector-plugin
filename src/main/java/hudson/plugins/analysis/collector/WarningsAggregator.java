package hudson.plugins.analysis.collector;

import org.apache.commons.lang.StringUtils;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

import hudson.model.Job;
import hudson.plugins.analysis.collector.handler.*;
import hudson.plugins.analysis.core.AbstractProjectAction;
import hudson.plugins.analysis.core.BuildResult;

/**
 * Aggregates the warnings of the analysis plug-in from a single job.
 *
 * @author Ulli Hafner
 */
public class WarningsAggregator {
    /** Message to be shown if no result action is found. */
    private static final String NO_RESULTS_FOUND = "-";
    private static final String CLOSE_TAG = ">";
    private static final String OPEN_TAG = "<";

    private final boolean isCheckStyleActivated;
    private final boolean isDryActivated;
    private final boolean isFindBugsActivated;
    private final boolean isPmdActivated;
    private final boolean isOpenTasksActivated;
    private final boolean isWarningsActivated;
    private boolean hideJobPrefix;

    /**
     * Creates a new instance of {@link WarningsAggregator}.
     *
     * @param isCheckStyleActivated
     *            determines whether to show the warnings from Checkstyle
     * @param isDryActivated
     *            determines whether to show the warnings from DRY
     * @param isFindBugsActivated
     *            determines whether to show the warnings from FindBugs
     * @param isPmdActivated
     *            determines whether to show the warnings from PMD
     * @param isOpenTasksActivated
     *            determines whether to show open tasks
     * @param isWarningsActivated
     *            determines whether to show compiler warnings
     */
    public WarningsAggregator(final boolean isCheckStyleActivated,
            final boolean isDryActivated, final boolean isFindBugsActivated, final boolean isPmdActivated,
            final boolean isOpenTasksActivated, final boolean isWarningsActivated) {
        this.isCheckStyleActivated = isCheckStyleActivated;
        this.isDryActivated = isDryActivated;
        this.isFindBugsActivated = isFindBugsActivated;
        this.isPmdActivated = isPmdActivated;
        this.isOpenTasksActivated = isOpenTasksActivated;
        this.isWarningsActivated = isWarningsActivated;
    }

    private int toInt(final String value) {
        try {
            if (value.contains(OPEN_TAG)) {
                return Integer.parseInt(StringUtils.substringBetween(value, CLOSE_TAG, OPEN_TAG));
            }
            else {
                return Integer.parseInt(value);
            }
        }
        catch (NumberFormatException exception) {
            return 0;
        }
    }

    /**
     * Returns whether CheckStyle results should be shown.
     *
     * @return <code>true</code> if CheckStyle results should be shown, <code>false</code> otherwise
     */
    public boolean isCheckStyleActivated() {
        return isCheckStyleActivated;
    }

    /**
     * Returns whether DRY results should be shown.
     *
     * @return <code>true</code> if DRY results should be shown, <code>false</code> otherwise
     */
    public boolean isDryActivated() {
        return isDryActivated;
    }

    /**
     * Returns whether FindBugs results should be shown.
     *
     * @return <code>true</code> if FindBugs results should be shown, <code>false</code> otherwise
     */
    public boolean isFindBugsActivated() {
        return isFindBugsActivated;
    }

    /**
     * Returns whether PMD results should be shown.
     *
     * @return <code>true</code> if PMD results should be shown, <code>false</code> otherwise
     */
    public boolean isPmdActivated() {
        return isPmdActivated;
    }

    /**
     * Returns whether open tasks should be shown.
     *
     * @return <code>true</code> if open tasks should be shown, <code>false</code> otherwise
     */
    public boolean isOpenTasksActivated() {
        return isOpenTasksActivated;
    }

    /**
     * Returns whether compiler warnings results should be shown.
     *
     * @return <code>true</code> if compiler warnings results should be shown, <code>false</code> otherwise
     */
    public boolean isWarningsActivated() {
        return isWarningsActivated;
    }

    /**
     * Returns the number of Checkstyle warnings for the specified job.
     *
     * @param job
     *            the job to get the warnings for
     * @return the number of Checkstyle warnings
     */
    public String getCheckStyle(final Job<?, ?> job) {
        if (isCheckStyleActivated()) {
            return getWarnings(job, new CheckStyleHandler());
        }
        return NO_RESULTS_FOUND;
    }

    /**
     * Returns the number of duplicate code warnings for the specified job.
     *
     * @param job
     *            the job to get the warnings for
     * @return the number of duplicate code warnings
     */
    public String getDry(final Job<?, ?> job) {
        if (isDryActivated()) {
            return getWarnings(job, new DryHandler());
        }
        return NO_RESULTS_FOUND;
    }

    /**
     * Returns the number of FindBugs warnings for the specified job.
     *
     * @param job
     *            the job to get the warnings for
     * @return the number of FindBugs warnings
     */
    public String getFindBugs(final Job<?, ?> job) {
        if (isFindBugsActivated()) {
            return getWarnings(job, new FindBugsHandler());
        }
        return NO_RESULTS_FOUND;
    }

    /**
     * Returns the number of PMD warnings for the specified job.
     *
     * @param job
     *            the job to get the warnings for
     * @return the number of PMD warnings
     */
    public String getPmd(final Job<?, ?> job) {
        if (isPmdActivated()) {
            return getWarnings(job, new PmdHandler());
        }
        return NO_RESULTS_FOUND;
    }

    /**
     * Returns the number of open tasks for the specified job.
     *
     * @param job
     *            the job to get the tasks for
     * @return the number of open tasks
     */
    public String getTasks(final Job<?, ?> job) {
        if (isOpenTasksActivated()) {
            return getWarnings(job, new TasksHandler());
        }
        return NO_RESULTS_FOUND;
    }

    /**
     * Returns the total number of warnings for the specified job.
     *
     * @param job
     *            the job to get the warnings for
     * @return the number of compiler warnings
     */
    public String getCompilerWarnings(final Job<?, ?> job) {
        if (isWarningsActivated()) {
            return getWarnings(job, new WarningsHandler());
        }
        return NO_RESULTS_FOUND;
    }

    /**
     * Returns the number of warnings for the specified job.
     *
     * @param job
     *            the job to get the warnings for
     * @return the number of warnings
     */
    public String getTotal(final Job<?, ?> job) {
        return String.valueOf(toInt(getCheckStyle(job))
                + toInt(getDry(job))
                + toInt(getFindBugs(job))
                + toInt(getPmd(job))
                + toInt(getTasks(job))
                + toInt(getCompilerWarnings(job)));
    }

    /**
     * Returns the warnings for the specified action.
     *
     *
     * @param job
     *            the job to get the action from
     * @param handler
     *            the analysis plug-in handler
     * @return the number of warnings
     */
    @SuppressWarnings("NP")
    private String getWarnings(final Job<?, ?> job, final AnalysisHandler handler) {
        AbstractProjectAction<?> action = job.getAction(handler.getProjectActionType());
        if (action != null && action.hasValidResults()) {
            BuildResult result = action.getLastAction().getResult();
            int numberOfAnnotations = result.getNumberOfAnnotations();
            String value;
            if (numberOfAnnotations > 0) {
                value = String.format("<a href=\"%s%s\">%d</a>", getJobPrefix(job), handler.getUrl(), numberOfAnnotations);
            }
            else {
                value = String.valueOf(numberOfAnnotations);
            }
            if (result.isSuccessfulTouched() && !result.isSuccessful()) {
                return value + result.getResultIcon();
            }
            return value;
        }
        return NO_RESULTS_FOUND;
    }

    private String getJobPrefix(final Job<?, ?> job) {
        return hideJobPrefix ? StringUtils.EMPTY : job.getShortUrl();
    }

    /**
     * Returns whether Checkstyle results are available for the specified job.
     *
     * @param job
     *            the job to get the warnings for
     * @return <code>true</code> if there are results, false otherwise
     */
    public boolean hasCheckStyle(final Job<?, ?> job) {
        if (isCheckStyleActivated()) {
            return hasAction(job, new CheckStyleHandler());
        }
        return false;
    }

    /**
     * Returns whether DRY results are available for the specified job.
     *
     * @param job
     *            the job to get the warnings for
     * @return <code>true</code> if there are results, false otherwise
     */
    public boolean hasDry(final Job<?, ?> job) {
        if (isDryActivated()) {
            return hasAction(job, new DryHandler());
        }
        return false;
    }

    /**
     * Returns whether FindBugs results are available for the specified job.
     *
     * @param job
     *            the job to get the warnings for
     * @return <code>true</code> if there are results, false otherwise
     */
    public boolean hasFindBugs(final Job<?, ?> job) {
        if (isFindBugsActivated()) {
            return hasAction(job, new FindBugsHandler());
        }
        return false;
    }

    /**
     * Returns whether PMD results are available for the specified job.
     *
     * @param job
     *            the job to get the warnings for
     * @return <code>true</code> if there are results, false otherwise
     */
    public boolean hasPmd(final Job<?, ?> job) {
        if (isPmdActivated()) {
            return hasAction(job, new PmdHandler());
        }
        return false;
    }

    /**
     * Returns whether open tasks results are available for the specified job.
     *
     * @param job
     *            the job to get the warnings for
     * @return <code>true</code> if there are results, false otherwise
     */
    public boolean hasTasks(final Job<?, ?> job) {
        if (isOpenTasksActivated()) {
            return hasAction(job, new TasksHandler());
        }
        return false;
    }

    /**
     * Returns whether compiler warning results are available for the specified job.
     *
     * @param job
     *            the job to get the warnings for
     * @return <code>true</code> if there are results, false otherwise
     */
    public boolean hasCompilerWarnings(final Job<?, ?> job) {
        if (isWarningsActivated()) {
            return hasAction(job, new WarningsHandler());
        }
        return false;
    }

    private boolean hasAction(final Job<?, ?> job, final AnalysisHandler handler) {
        AbstractProjectAction<?> action = job.getAction(handler.getProjectActionType());

        return action != null && action.hasValidResults();
    }

    /**
     * Removes the job/job-name prefix in the URL.
     */
    public void hideJobPrefix() {
        hideJobPrefix = true;
    }
}
