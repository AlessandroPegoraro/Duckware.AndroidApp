package com.swe.duckware.megalexa.alexa;

public class Workflow {

    private String wfName;

    public Workflow(String workflowName) {
        this.wfName = workflowName;
    }

    @Override
    public String toString() {
        return wfName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Workflow))
            return false;

        Workflow wf = (Workflow) obj;
        return wf.wfName.equals(wfName);
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = 31 * res + wfName.hashCode();
        return res;
    }
}
