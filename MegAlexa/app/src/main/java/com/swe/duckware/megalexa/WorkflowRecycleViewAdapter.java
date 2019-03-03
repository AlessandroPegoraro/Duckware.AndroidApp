package com.swe.duckware.megalexa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.swe.duckware.megalexa.alexa.Workflow;
import com.swe.duckware.megalexa.netrequests.HTTPRequest;
import com.swe.duckware.megalexa.netrequests.HTTPRequestAction;

import java.util.HashMap;
import java.util.List;

public class WorkflowRecycleViewAdapter extends RecyclerView.Adapter<WorkflowRecycleViewAdapter.ViewHolder> {

    private final List<Workflow> mWorkflows;

    private final AppCompatActivity activity;

    public WorkflowRecycleViewAdapter(List<Workflow> workflows, AppCompatActivity activity) {
        mWorkflows = workflows;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_workflow, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.workflow = mWorkflows.get(position);

        holder.workflowName.setText( mWorkflows.get(position).toString() );
        holder.workflowName.setOnClickListener(view -> {
            Intent intent = new Intent(holder.workflowName.getContext(), ConnectorActivity.class);
            intent.putExtra("workflow_name", holder.workflowName.getText());

            holder.workflowName.getContext().startActivity(intent);
        });
        holder.workflowDelete.setOnClickListener(view -> {
            deleteWokflow(view, position);
        });
    }

    @Override
    public int getItemCount() {
        return mWorkflows.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View parentView;
        public final TextView workflowName;
        public final Button workflowDelete;
        public Workflow workflow;

        public ViewHolder(View itemView) {
            super(itemView);

            this.parentView = itemView;
            this.workflowName = itemView.findViewById(R.id.list_item_workflow_labelName);
            this.workflowDelete = itemView.findViewById(R.id.buttonDeleteWorkflow);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + workflowName.getText() + "'";
        }
    }

    private void deleteWokflow(View view, int workflowPosition) {
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        View promptView = layoutInflater.inflate(R.layout.dialog_delete_workflow, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(promptView);

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.delete, null);
        alertDialogBuilder.setNegativeButton(R.string.cancel, null);

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.setOnShowListener(dialogInterface -> {

            Button button = alert.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view1 -> {
                promptView.findViewById(R.id.progressDeleteWorkflow).setVisibility(View.VISIBLE);

                //Get username and pass
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                String username = prefs.getString("username_login", "-");
                String password = prefs.getString("password_login", "-");

                //Build the request
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("workflowIndex", String.valueOf(workflowPosition));

                new HTTPRequest(params, HTTPRequestAction.WorkflowDelete).doRequest(this.activity,
                        (response) -> {
                            int itemPosition = workflowPosition;

                            mWorkflows.remove(itemPosition);
                            notifyItemRemoved(itemPosition);

                            alert.dismiss();
                        },
                        (error) -> {
                            Toast.makeText(view.getContext(), Resources.getSystem().getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                        });
            });

        });

        alert.show();
    }

}
