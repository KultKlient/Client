package kultklient.legacy.client.utils.network.serverfinder;

public interface IServerFinderDisconnectListener {
    void onServerDisconnect();
    void onServerFailed();
}
