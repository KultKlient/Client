package kult.klient.utils.network.serverfinder;

public interface IServerFinderDisconnectListener {
    void onServerDisconnect();
    void onServerFailed();
}
