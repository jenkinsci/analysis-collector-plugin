package hudson.plugins.analysis.collector.workflow;

import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.junit.ClassRule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.*;

import hudson.FilePath;
import hudson.model.Result;
import hudson.plugins.analysis.collector.AnalysisResultAction;

/**
 * Test workflow compatibility.
 */
public class WorkflowCompatibilityTest {
    @ClassRule
    public static JenkinsRule j = new JenkinsRule();

    /**
     * General collector test using Findbugsas example.
     */
    @Test
    public void collectorPublisherWorkflowStep() throws Exception {
        WorkflowJob job = j.jenkins.createProject(WorkflowJob.class, "wf");
        FilePath workspace = j.jenkins.getWorkspaceFor(job);
        FilePath report = workspace.child("target").child("findbugs.xml");
        report.copyFrom(WorkflowCompatibilityTest.class.getResourceAsStream("/findbugs-native.xml"));
        FilePath report2 = workspace.child("target").child("checkstyle-result.xml");
        report2.copyFrom(WorkflowCompatibilityTest.class.getResourceAsStream("/checkstyle-result-build2.xml"));
        job.setDefinition(new CpsFlowDefinition(
        "node {\n" +
        "  step([$class: 'FindBugsPublisher'])\n" +
        "  step([$class: 'CheckStylePublisher'])\n" +
        // Ignore checkstyle report
        "  step([$class: 'AnalysisPublisher', checkStyleActivated: false])\n" +
        "}"));
        j.assertBuildStatusSuccess(job.scheduleBuild2(0));
        AnalysisResultAction result = job.getLastBuild().getAction(AnalysisResultAction.class);
        // Only 2 annotation coming from Findbugs, Checkstyle is ignored bhy configuration
        assertEquals(2, result.getResult().getAnnotations().size());
    }

    /**
     * Failure test.
     */
    @Test
    public void collectorPublisherWorkflowStepSetLimits() throws Exception {
        WorkflowJob job = j.jenkins.createProject(WorkflowJob.class, "wf2");
        FilePath workspace = j.jenkins.getWorkspaceFor(job);
        FilePath report = workspace.child("target").child("findbugs.xml");
        report.copyFrom(WorkflowCompatibilityTest.class.getResourceAsStream("/findbugs-native.xml"));
        job.setDefinition(new CpsFlowDefinition(
        "node {\n" +
        "  step([$class: 'FindBugsPublisher'])\n" +
        "  step([$class: 'AnalysisPublisher', failedTotalAll: '0', usePreviousBuildAsReference: false])\n" +
        "}"));
        j.assertBuildStatus(Result.FAILURE, job.scheduleBuild2(0).get());
        AnalysisResultAction result = job.getLastBuild().getAction(AnalysisResultAction.class);
        assertEquals(result.getResult().getAnnotations().size(), 2);
    }

    /**
     * Unstable test.
     */
    @Test
    public void collectorPublisherWorkflowStepFailure() throws Exception {
        WorkflowJob job = j.jenkins.createProject(WorkflowJob.class, "wf3");
        FilePath workspace = j.jenkins.getWorkspaceFor(job);
        FilePath report = workspace.child("target").child("findbugs.xml");
        report.copyFrom(WorkflowCompatibilityTest.class.getResourceAsStream("/findbugs-native.xml"));
        job.setDefinition(new CpsFlowDefinition(
                "node {\n" +
                        "  step([$class: 'FindBugsPublisher'])\n" +
                        "  step([$class: 'AnalysisPublisher', unstableTotalAll: '0', usePreviousBuildAsReference: false])\n" +
                        "}"));
        j.assertBuildStatus(Result.UNSTABLE, job.scheduleBuild2(0).get());
        AnalysisResultAction result = job.getLastBuild().getAction(AnalysisResultAction.class);
        assertEquals(result.getResult().getAnnotations().size(), 2);
    }
}

