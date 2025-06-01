import { useEffect, useState } from 'react';
import NetworkUtils from '../utils/NetworkUtils';

export const useNetwork = () => {
    const [isConnected, setIsConnected] = useState(true);

    useEffect(() => {
        // Check initial state
        NetworkUtils.isNetworkAvailable().then(setIsConnected);

        // Subscribe to changes
        const unsubscribe = NetworkUtils.subscribeToNetworkChanges(setIsConnected);

        return unsubscribe;
    }, []);

    return isConnected;
};