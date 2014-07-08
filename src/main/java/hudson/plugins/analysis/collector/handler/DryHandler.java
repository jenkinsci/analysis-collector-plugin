package hudson.plugins.analysis.collector.handler;

import java.util.Collection;

import org.jvnet.localizer.Localizable;

import com.google.common.collect.Lists;

import hudson.plugins.analysis.collector.AnalysisDescriptor;
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
    @Override
    public Class<? extends AbstractProjectAction<?>> getProjectActionType() {
        return DryProjectAction.class;
    }

    @Override
    public String getUrl() {
        return AnalysisDescriptor.DRY;
    }

    @Override
    public Localizable getDetailHeader() {
        return Messages._DRY_Detail_header();
    }

    @Override
    public Class<? extends PluginDescriptor> getDescriptor() {
        return DryDescriptor.class;
    }

    @Override
    public Collection<? extends Class<? extends ResultAction<? extends BuildResult>>> getResultActions() {
        return Lists.newArrayList(DryResultAction.class, DryMavenResultAction.class);
    }
}
