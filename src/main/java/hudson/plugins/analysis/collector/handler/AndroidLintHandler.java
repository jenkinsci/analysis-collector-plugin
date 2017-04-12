package hudson.plugins.analysis.collector.handler;

import com.google.common.collect.Lists;

import org.jenkinsci.plugins.android_lint.LintDescriptor;
import org.jenkinsci.plugins.android_lint.LintMavenResultAction;
import org.jenkinsci.plugins.android_lint.LintProjectAction;
import org.jenkinsci.plugins.android_lint.LintResultAction;
import org.jenkinsci.plugins.android_lint.Messages;
import org.jvnet.localizer.Localizable;

import java.util.Collection;

import hudson.plugins.analysis.core.AbstractProjectAction;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.PluginDescriptor;
import hudson.plugins.analysis.core.ResultAction;

/**
 * Handles access to the Android Lint plug-in.
 */
public class AndroidLintHandler implements AnalysisHandler {
    @Override
    public Class<? extends AbstractProjectAction<?>> getProjectActionType() {
        return LintProjectAction.class;
    }

    @Override
    public String getUrl() {
        return "androidLint";
    }

    @Override
    public Localizable getDetailHeader() {
        return Messages._AndroidLint_Warnings_ColumnHeader();
    }

    @Override
    public Class<? extends PluginDescriptor> getDescriptor() {
        return LintDescriptor.class;
    }

    @Override
    public Collection<? extends Class<? extends ResultAction<? extends BuildResult>>> getResultActions() {
        return Lists.newArrayList(LintResultAction.class, LintMavenResultAction.class);
    }
}
