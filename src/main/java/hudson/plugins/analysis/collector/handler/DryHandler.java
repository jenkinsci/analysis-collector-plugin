package hudson.plugins.analysis.collector.handler;

import java.util.Collection;

import org.jvnet.localizer.Localizable;

import com.google.common.collect.Lists;

import hudson.plugins.analysis.core.AbstractProjectAction;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.PluginDescriptor;
import hudson.plugins.analysis.core.ResultAction;
import hudson.plugins.dry.*;

/**
 * Handles access to the DRY plug-in.
 *
 * @author Ulli Hafner
 */
public class DryHandler implements AnalysisHandler {
    public Class<? extends AbstractProjectAction<?>> getProjectActionType() {
        return DryProjectAction.class;
    }

    public String getUrl() {
        return "dry";
    }

    public Localizable getDetailHeader() {
        return Messages._DRY_Detail_header();
    }

    public Class<? extends PluginDescriptor> getDescriptor() {
        return DryDescriptor.class;
    }

    public Collection<? extends Class<? extends ResultAction<? extends BuildResult>>> getResultActions() {
        return Lists.newArrayList(DryResultAction.class, DryMavenResultAction.class);
    }
}
