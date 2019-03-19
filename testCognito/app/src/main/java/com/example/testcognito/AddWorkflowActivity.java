package com.example.testcognito;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateWorkflowMutation;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

import type.CreateWorkflowInput;

public class AddWorkflowActivity extends AppCompatActivity {

    private static final String TAG = AddWorkflowActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workflow);

        Button btnAddItem = findViewById(R.id.btn_save);
        btnAddItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    private void save() {
        final String name = ((EditText) findViewById(R.id.editTxt_name)).getText().toString();
        final String description = ((EditText) findViewById(R.id.editText_description)).getText().toString();
        final String testUpdate = ((EditText) findViewById(R.id.editText_testupdate)).getText().toString();
        CreateWorkflowInput input = CreateWorkflowInput.builder()
                .name(name)
                .description(description)
                .testUpdate(testUpdate)
                .build();

        CreateWorkflowMutation addWorkflowMutation = CreateWorkflowMutation.builder()
                .input(input)
                .build();
        com.example.testcognito.ClientFactory.appSyncClient().mutate(addWorkflowMutation).enqueue(mutateCallback);
    }

    // Mutation callback code
    private GraphQLCall.Callback<CreateWorkflowMutation.Data> mutateCallback = new GraphQLCall.Callback<CreateWorkflowMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateWorkflowMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddWorkflowActivity.this, "Added Workflow", Toast.LENGTH_SHORT).show();
                    AddWorkflowActivity.this.finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddWorkflowMutation", e);
                    Toast.makeText(AddWorkflowActivity.this, "Failed to add Workflow", Toast.LENGTH_SHORT).show();
                    AddWorkflowActivity.this.finish();
                }
            });
        }
    };
}
