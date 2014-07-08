package hudson.plugins.analysis.collector.handler;

import java.util.Collection;

import org.jvnet.localizer.Localizable;

import com.google.common.collect.Lists;

import hudson.plugins.analysis.collector.AnalysisDescriptor;
import hudson.plugins.analysis.core.AbstractProjectAction;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.PluginDescriptor;
import hudson.plugins.analysis.core.ResultAction;
import hudson.plugins.warnings.AggregatedWarningsProjectAction;
import hudson.plugins.warnings.Messages;
import hudson.plugins.warnings.WarningsDescriptor;
import hudson.plugins.warnings.WarningsResultAction;

/**
 * Handles access to the warnings plug-in.
 *
 * @author Ulli Hafner
 */
public class WarningsHandler implements AnalysisHandler {
    @Override
    public Class<? extends AbstractProjectAction<?>> getProjectActionType() {
        return AggregatedWarningsProjectAction.class;
    }

    @Override
    public String getUrl() {
        return AnalysisDescriptor.WARNINGS;
    }

    @Override
    public Localizable getDetailHeader() {
        return Messages._Warnings_ProjectAction_Name();
    }

    @Override
    public Class<? extends PluginDescriptor> getDescriptor() {
        return WarningsDescriptor.class;
    }

    @Override
    public Collection<? extends Class<? extends ResultAction<? extends BuildResult>>> getResultActions() {
        return Lists.newArrayList(WarningsResultAction.class);
    }
}
