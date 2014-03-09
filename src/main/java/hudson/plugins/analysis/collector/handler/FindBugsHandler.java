package hudson.plugins.analysis.collector.handler;

import java.util.Collection;

import org.jvnet.localizer.Localizable;

import com.google.common.collect.Lists;

import hudson.plugins.analysis.core.AbstractProjectAction;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.PluginDescriptor;
import hudson.plugins.analysis.core.ResultAction;
import hudson.plugins.findbugs.*;

/**
 * Handles access to the FindBugs plug-in.
 *
 * @author Ulli Hafner
 */
public class FindBugsHandler implements AnalysisHandler {
    public Class<? extends AbstractProjectAction<?>> getProjectActionType() {
        return FindBugsProjectAction.class;
    }

    public String getUrl() {
        return "findbugs";
    }

    public Localizable getDetailHeader() {
        return Messages._FindBugs_Detail_header();
    }

    public Class<? extends PluginDescriptor> getDescriptor() {
        return FindBugsDescriptor.class;
    }

    public Collection<? extends Class<? extends ResultAction<? extends BuildResult>>> getResultActions() {
        return Lists.newArrayList(FindBugsResultAction.class, FindBugsMavenResultAction.class);
    }
}
