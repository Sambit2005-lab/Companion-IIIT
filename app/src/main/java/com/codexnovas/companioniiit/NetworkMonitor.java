package com.codexnovas.companioniiit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.widget.Toast;

public class NetworkMonitor {
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private Context context;

    public NetworkMonitor(Context context) {
        this.context = context;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Callback to monitor network changes
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                // Internet is available
                Toast.makeText(context, "Internet connection is available", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLost(Network network) {
                // Internet is lost
                Toast.makeText(context, "No internet connection. Please connect to the internet.", Toast.LENGTH_LONG).show();
            }
        };
    }

    // Method to start monitoring network changes
    public void startNetworkCallback() {
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    // Method to stop monitoring network changes
    public void stopNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    // Method to check network status immediately when app starts
    public void checkInitialNetworkStatus() {
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            // Internet is available
            Toast.makeText(context, "Internet connection is available", Toast.LENGTH_SHORT).show();
        } else {
            // No internet connection
            Toast.makeText(context, "No internet connection. Please connect to the internet.", Toast.LENGTH_LONG).show();
        }
    }
}