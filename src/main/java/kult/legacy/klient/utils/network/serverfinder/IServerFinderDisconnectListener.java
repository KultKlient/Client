package kult.legacy.klient.utils.network.serverfinder;

public interface IServerFinderDisconnectListener {
    void onServerDisconnect();
    void onServerFailed();
}
