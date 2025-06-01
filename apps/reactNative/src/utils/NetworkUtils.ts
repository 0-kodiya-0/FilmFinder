import NetInfo from '@react-native-community/netinfo';

class NetworkUtils {
    static async isNetworkAvailable(): Promise<boolean> {
        try {
            const state = await NetInfo.fetch();
            return state.isConnected === true;
        } catch (error) {
            console.error('Error checking network status:', error);
            return false;
        }
    }

    static subscribeToNetworkChanges(callback: (isConnected: boolean) => void): () => void {
        const unsubscribe = NetInfo.addEventListener(state => {
            callback(state.isConnected === true);
        });

        return unsubscribe;
    }
}

export default NetworkUtils;