package hudson.plugins.analysis.collector.handler;

import java.util.Collection;

import org.jvnet.localizer.Localizable;

import hudson.plugins.analysis.core.AbstractProjectAction;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.PluginDescriptor;
import hudson.plugins.analysis.core.ResultAction;

/**
 * Handles access to the individual plug-in results.
 *
 * @author Ulli Hafner
 */
public interface AnalysisHandler {
    /**
     * Returns the class of the project action of the plug-in.
     *
     * @return project action class
     */
    Class<? extends AbstractProjectAction<?>> getProjectActionType();

    /**
     * Returns the URL of the plugin.
     *
     * @return plug-in URL
     */
    String getUrl();

    /**
     * Returns the header of the plug-in details.
     *
     * @return localized header of the details
     */
    Localizable getDetailHeader();

    /**
     * Returns the descriptor of the plug-in.
     *
     * @return plugin descriptor
     */
    Class<? extends PluginDescriptor> getDescriptor();

    /**
     * Returns the class of the project action of the plug-in.
     *
     * @return project action class
     */
    Collection<? extends Class<? extends ResultAction<? extends BuildResult>>> getResultActions();
}
