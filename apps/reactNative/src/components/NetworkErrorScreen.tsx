import React from 'react';
import {
    View,
    Text,
    TouchableOpacity,
    StyleSheet,
    SafeAreaView,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import AppBar from './AppBar';

interface NetworkErrorScreenProps {
    onRetry: () => void;
    onNavigateBack?: () => void;
}

const NetworkErrorScreen: React.FC<NetworkErrorScreenProps> = ({
    onRetry,
    onNavigateBack,
}) => {
    return (
        <SafeAreaView style={styles.container}>
            {onNavigateBack && (
                <AppBar title="" onBackPress={onNavigateBack} />
            )}

            <View style={styles.content}>
                <Ionicons name="warning" size={80} color="#f44336" />

                <Text style={styles.title}>No Internet Connection</Text>

                <Text style={styles.message}>
                    Please check your network connection and try again.
                </Text>

                <TouchableOpacity style={styles.retryButton} onPress={onRetry}>
                    <Ionicons name="refresh" size={20} color="#fff" />
                    <Text style={styles.retryButtonText}>Retry</Text>
                </TouchableOpacity>

                <TouchableOpacity style={styles.reportButton}>
                    <Ionicons name="send" size={20} color="#1565C0" />
                    <Text style={styles.reportButtonText}>Report Issue</Text>
                </TouchableOpacity>
            </View>
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
    },
    content: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 24,
    },
    title: {
        fontSize: 24,
        fontWeight: '600',
        color: '#333',
        marginTop: 24,
        marginBottom: 16,
    },
    message: {
        fontSize: 16,
        color: '#666',
        textAlign: 'center',
        lineHeight: 24,
        marginBottom: 32,
    },
    retryButton: {
        flexDirection: 'row',
        backgroundColor: '#E3F2FD',
        paddingHorizontal: 24,
        paddingVertical: 12,
        borderRadius: 8,
        alignItems: 'center',
        marginBottom: 16,
        minWidth: 200,
        justifyContent: 'center',
    },
    retryButtonText: {
        color: '#1565C0',
        fontSize: 16,
        fontWeight: '600',
        marginLeft: 8,
    },
    reportButton: {
        flexDirection: 'row',
        borderWidth: 1,
        borderColor: '#1565C0',
        paddingHorizontal: 24,
        paddingVertical: 12,
        borderRadius: 8,
        alignItems: 'center',
        minWidth: 200,
        justifyContent: 'center',
    },
    reportButtonText: {
        color: '#1565C0',
        fontSize: 16,
        fontWeight: '600',
        marginLeft: 8,
    },
});

export default NetworkErrorScreen;