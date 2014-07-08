package hudson.plugins.analysis.collector.handler;

import java.util.Collection;

import org.jvnet.localizer.Localizable;

import com.google.common.collect.Lists;

import hudson.plugins.analysis.collector.AnalysisDescriptor;
import hudson.plugins.analysis.core.AbstractProjectAction;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.PluginDescriptor;
import hudson.plugins.analysis.core.ResultAction;
import hudson.plugins.pmd.*;

/**
 * Handles access to the PMD plug-in.
 *
 * @author Ulli Hafner
 */
public class PmdHandler implements AnalysisHandler {
    @Override
    public Class<? extends AbstractProjectAction<?>> getProjectActionType() {
        return PmdProjectAction.class;
    }

    @Override
    public String getUrl() {
        return AnalysisDescriptor.PMD;
    }

    @Override
    public Localizable getDetailHeader() {
        return Messages._PMD_Detail_header();
    }

    @Override
    public Class<? extends PluginDescriptor> getDescriptor() {
        return PmdDescriptor.class;
    }

    @Override
    public Collection<? extends Class<? extends ResultAction<? extends BuildResult>>> getResultActions() {
        return Lists.newArrayList(PmdResultAction.class, PmdMavenResultAction.class);
    }
}
