package com.example.testcognito;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.amazonaws.amplify.generated.graphql.ListWorkflowsQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    com.example.testcognito.WorkflowAdapter mAdapter;

    private ArrayList<ListWorkflowsQuery.Item> mWorkflows;
    private final String TAG = MainActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        mAdapter = new com.example.testcognito.WorkflowAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        com.example.testcognito.ClientFactory.init(this);

        FloatingActionButton btnAddWorkflow = findViewById(R.id.btn_addWorkflow);
        btnAddWorkflow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent addWorkflowIntent = new Intent(MainActivity.this, AddWorkflowActivity.class);
                MainActivity.this.startActivity(addWorkflowIntent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Query list data when we return to the screen
        query();
    }

    public void query(){
        com.example.testcognito.ClientFactory.appSyncClient().query(ListWorkflowsQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);
    }

    private GraphQLCall.Callback<ListWorkflowsQuery.Data> queryCallback = new GraphQLCall.Callback<ListWorkflowsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListWorkflowsQuery.Data> response) {

            mWorkflows = new ArrayList<>(response.data().listWorkflows().items());

            Log.i(TAG, "Retrieved list items: " + mWorkflows.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setItems(mWorkflows);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };
    public void logout(View view){
        AWSMobileClient.getInstance().signOut();

        Intent i = new Intent(MainActivity.this,AuthenticationActivity.class );
        startActivity(i);
    }
}