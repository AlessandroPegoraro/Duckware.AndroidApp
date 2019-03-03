package com.swe.duckware.megalexa;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.swe.duckware.megalexa.alexa.Workflow;
import com.swe.duckware.megalexa.netrequests.HTTPRequest;
import com.swe.duckware.megalexa.netrequests.HTTPRequestAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WorkflowActivity extends AppCompatActivity {

    private ArrayList<Workflow> mWorkflows = new ArrayList<>();

    private RecyclerView mRecyclerView;

    private WorkflowRecycleViewAdapter mWorkflowAdapter = new WorkflowRecycleViewAdapter(mWorkflows, this);

    private SharedPreferences prefs = null;

    private void addFloatButtonWorkflow() {
        FloatingActionButton fab = findViewById(R.id.fabAddWorkflow);
        fab.setOnClickListener(view -> {
            LayoutInflater layoutInflater = LayoutInflater.from(WorkflowActivity.this);
            View promptView = layoutInflater.inflate(R.layout.dialog_add_workflow, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WorkflowActivity.this);
            alertDialogBuilder.setView(promptView);

            EditText wfName = promptView.findViewById(R.id.newWorkflowName);
            promptView.findViewById(R.id.progressAddWorkflow).setVisibility(View.GONE);

            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(R.string.add, null);
            alertDialogBuilder.setNegativeButton(R.string.cancel, null);

            // create an alert dialog
            AlertDialog alert = alertDialogBuilder.create();
            alert.setOnShowListener(dialogInterface -> {

                Button button = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(view1 -> {
                    Workflow wflow = new Workflow(wfName.getText().toString());

                    if (mWorkflows.contains(wflow)) {
                        Toast.makeText(this, getString(R.string.duplicate_wf), Toast.LENGTH_LONG).show();
                    } else {
                        promptView.findViewById(R.id.progressAddWorkflow).setVisibility(View.VISIBLE);

                        //Get username and pass
                        String username = prefs.getString("username_login", "-");
                        String password = prefs.getString("password_login", "-");
                        String workflowID = wfName.getText().toString();

                        //Build the request
                        HashMap<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("password", password);
                        params.put("workflowID", workflowID);
                        params.put("workflowDefinition", "qwerty");
                        params.put("workflowIndex", String.valueOf(mWorkflows.size()));

                        new HTTPRequest(params, HTTPRequestAction.WorkflowAdd).doRequest(WorkflowActivity.this,
                                (response) -> {
                                    mWorkflows.add(wflow);
                                    mWorkflowAdapter.notifyItemInserted(mWorkflows.indexOf(wflow));

                                    alert.dismiss();
                                },
                                (error) -> {
                                    Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                                });
                    }

                });

            });

            alert.show();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow);

        this.prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //RecyclerView setup
        mRecyclerView = findViewById(R.id.listWorkflow);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mWorkflowAdapter);

        //Add workflows to the list
        getUserWorkflows();

        //Add a new workflow with the button
        addFloatButtonWorkflow();
    }

    public void setWorkflows(List<Workflow> workflows) {
        for(Workflow workflow : workflows) {
            if (!mWorkflows.contains(workflow)) {
                mWorkflows.add(workflow);
                mWorkflowAdapter.notifyItemInserted(
                        mWorkflows.indexOf(workflow)
                );
            }
        }
    }

    public void getUserWorkflows() {
        String data = prefs.getString("userdata_login", "-");
        List<Workflow> wfList = new ArrayList<>();

        //If the content is not "-" or another single char that indicates "no workflows"
        if (data.length() > 1) {
            data = data.replaceAll("[{}\\[\\]]", "");
            String[] workflows = data.split(",");

            for(String workflow : workflows) {
                if (workflow.contains("WorkflowID"))
                    wfList.add(new Workflow( workflow.split("=")[1] ));
            }

            setWorkflows(wfList);
        }
    }

}
